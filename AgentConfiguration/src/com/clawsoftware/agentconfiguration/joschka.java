/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.clawsoftware.agentconfiguration;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Clemens Lode, clemens at lode.de, University Karlsruhe (TH)
 */
public class joschka {
	/*
	 * To change this template, choose Tools | Templates and open the template
	 * in the editor.
	 */

	/** Fast & simple file copy. */
	public static void copy(final File source, final File dest)
			throws IOException {
		FileChannel in = null, out = null;
		try {
			in = new FileInputStream(source).getChannel();
			out = new FileOutputStream(dest).getChannel();

			final long size = in.size();
			final MappedByteBuffer buf = in.map(FileChannel.MapMode.READ_ONLY,
					0, size);

			out.write(buf);
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static String run() throws Exception {
		final File dir = new File(".");

		final FileFilter plotFileFilter = new FileFilter() {
			@Override
			public boolean accept(final File file) {
				if (file.getName().startsWith("plot-all-")) {
					return true;
				} else {
					return false;
				}
			}
		};
		final FileFilter configFileFilter = new FileFilter() {
			@Override
			public boolean accept(final File file) {
				if (file.getName().startsWith("config-")) {
					return true;
				} else {
					return false;
				}
			}
		};
		final FileFilter outputDirectoriesFilter = new FileFilter() {
			@Override
			public boolean accept(final File file) {
				if (file.isDirectory() && file.getName().startsWith("output")) {
					return true;
				} else {
					return false;
				}
			}
		};

		final File[] config_files = dir.listFiles(configFileFilter);
		final File[] output_directories = dir
				.listFiles(outputDirectoriesFilter);
		final File[] all_plot_files = dir.listFiles(plotFileFilter);

		final ArrayList<String> open_calculations = new ArrayList<String>();

		for (final File g : config_files) {
			final String name = g.getName();
			final String date = name.substring(7, name.length() - 4);
			boolean directory_found = false;
			for (final File f : output_directories) {
				if (f.getName().endsWith(date)) {
					directory_found = true;
					break;
				}
			}
			if (!directory_found) {
				open_calculations.add(name);
			}
		}

		final SimpleDateFormat fmt = new SimpleDateFormat(
				"dd-MM-yy--HH-mm-ss-SS");
		final String new_date = fmt.format(new Date());
		// job file
		final String joschka_file_name = "joschka-agent-" + new_date + ".txt";
		// directory to copy to ceres
		final String outputDirectory = "agent-" + new_date;
		final String batch_file_name = outputDirectory + "\\batch-agent-"
				+ new_date + ".bat";

		final File output_dir = new File(outputDirectory);
		if (!output_dir.exists()) {
			output_dir.mkdir();
		}
		// copy files
		for (final String config_file : open_calculations) {
			final File f = new File(config_file);
			if (!f.renameTo(new File(outputDirectory + "\\" + config_file))) {
				throw new Exception(
						"Could not move config file to work directory.");
			}
		}

		for (final File plot_file : all_plot_files) {
			if (!plot_file
					.renameTo(new File(outputDirectory + "\\" + plot_file))) {
				throw new Exception(
						"Could not move plot file to work directory.");
			}
		}

		final File j = new File("dist\\agentsimulator.jar");
		final File dj = new File(outputDirectory + "\\agentsimulator.jar");
		try {
			copy(j, dj);
		} catch (final IOException e) {
			throw new Exception("Error copying agentsimulator.jar file");
		}

		final File batch_file = new File(batch_file_name);
		final File joschka_file = new File(joschka_file_name);
		try {
			joschka_file.createNewFile();
			batch_file.createNewFile();
		} catch (final Exception e) {
			throw new Exception("Error opening file "
					+ joschka_file.getAbsoluteFile());
		}
		FileOutputStream f_joschka;
		PrintStream p_joschka;
		FileOutputStream f_batch;
		PrintStream p_batch;

		f_joschka = new FileOutputStream(joschka_file.getAbsoluteFile());
		p_joschka = new PrintStream(f_joschka);
		f_batch = new FileOutputStream(batch_file.getAbsoluteFile());
		p_batch = new PrintStream(f_batch);
		String batch_entry = new String(
				"java -jar -Xmx512m -Xss64m agentsimulator.jar");

		int index = 1;
		for (final String config_file : open_calculations) {
			String entry = new String("");
			entry += "cllo_" + outputDirectory + "\t";
			entry += "J\t";
			entry += "java -jar -Xmx512m -Xss64m agentsimulator.jar "
					+ config_file + "\t";
			entry += "*\t";
			entry += "NO\t";
			entry += "agentsimulator.jar;" + config_file + "\t\t\t";

			// job name
			entry += outputDirectory + "-" + index;
			p_joschka.println(entry);

			batch_entry += " " + config_file;

			index++;
		}
		batch_entry += "\n";
		p_batch.print(batch_entry);
		p_joschka.close();
		p_batch.close();

		return new_date;
	}

}
