/**
 *
 * @author Clemens Lode, clemens at lode.de, University Karlsruhe (TH)
 */
package com.clawsoftware.agentconfiguration;

import java.awt.Color;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumnModel;

import agent.Configuration;

/**
 *
 * @author Clemens Lode, clemens at lode.de, University Karlsruhe (TH)
 */
public class ConfigurationFrame extends javax.swing.JFrame {

	private static BufferedWriter plot_out;
	static String timeString = new String("");
	static int conf_id = 1000;
	static ArrayList<String> config_strings = new ArrayList<String>();
	static String last_batch_file = new String();
	static String last_directory = new String();
	/**
	 * result database
	 */
	ResultsTable results = new ResultsTable();
	TableSorter sorter = new TableSorter(results);

	/**
	 * Steps = 1 Stack Size = 2 Max population = 3
	 *
	 */
	/** Creates new form ConfigurationFrame */
	public ConfigurationFrame() {
		initComponents();
		resetTimeString();
		loadSettings("default.txt");

		resultsTable.setModel(sorter);
		sorter.setTableHeader(resultsTable.getTableHeader());
		resultsTable
				.getTableHeader()
				.setToolTipText(
						"Click to specify sorting; Control-Click to specify secondary sorting");

		final ListSelectionModel listSelectionModel = resultsTable
				.getSelectionModel();
		listSelectionModel
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listSelectionModel.addListSelectionListener(new SelectionListener(
				resultsTable));
		resultsTable.setSelectionModel(listSelectionModel);

		loadResultsIntoDatabase();
	}

	private class SelectionListener implements ListSelectionListener {

		private final JTable table;

		SelectionListener(final JTable table) {
			this.table = table;
		}

		@Override
		public void valueChanged(final ListSelectionEvent e) {
			if (e.getValueIsAdjusting()) {
				return;
			}
			final int row = sorter.modelIndex(table.getSelectedRow());
			final Object[] my_row = results.getRow(row);
			final String config_string = (String) my_row[0];
			loadSettings(config_string);
		}
	}

	/**
	 * load data from a BufferedReader into the results database
	 * 
	 * @param p
	 *            A BufferedReader device, e.g. a file
	 * @throws java.lang.NumberFormatException
	 *             Error parsing an integer field
	 * @throws java.io.IOException
	 *             Error reading from BufferedReader
	 */
	private void loadResultsIntoDatabase() {
		results.datas.clear();

		/**
		 * search for all Agent-*** Directories gather there all config-* names
		 * read the appropriate goal_percentage file
		 * 
		 */
		final File dir = new File("results//");

		final FileFilter agentDirectoriesFilter = new FileFilter() {

			@Override
			public boolean accept(final File file) {
				if (file.isDirectory() && file.getName().startsWith("agent-")) {
					return true;
				} else {
					return false;
				}
			}
		};

		final FileFilter outputDirectoriesFilter = new FileFilter() {

			@Override
			public boolean accept(final File file) {
				if (file.getName().startsWith("output_")) {
					return true;
				} else {
					return false;
				}
			}
		};
		new FileFilter() {

			@Override
			public boolean accept(final File file) {
				if (file.getName().startsWith("config-")) {
					return true;
				} else {
					return false;
				}
			}
		};

		final File[] agent_directories = dir.listFiles(agentDirectoriesFilter);
		if (agent_directories != null) {
			for (final File agent_directory : agent_directories) {
				final File[] output_directories = agent_directory
						.listFiles(outputDirectoriesFilter);
				for (final File output_directory : output_directories) {
					final String directory_string = "results//"
							+ agent_directory.getName() + "//"
							+ output_directory.getName() + "//";
					final String id_string = output_directory.getName()
							.substring(7, output_directory.getName().length());
					final String config_string = directory_string + "config-"
							+ id_string + ".txt";
					final File result_file = new File(directory_string
							+ "results-" + id_string + ".dat");
					final File half_result_file = new File(directory_string
							+ "half_results-" + id_string + ".dat");

					try {
						Configuration.initialize(config_string);
					} catch (final Exception e) {
						JOptionPane.showMessageDialog(this,
								"Error opening/reading file " + config_string
										+ " (" + e + ")",
								"Error opening/reading config file",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					// if(Configuration.getBeta() != 0.05 ||
					// Configuration.getNumberOfProblems()!=10 ||
					// Configuration.getMaxStackSize() != 8) {
					// continue;
					// /}
					final String config_id = Configuration.getProblemID()
							.toString();
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					String wastedMovements = new String("--");
					String goalAgentObservedPercentage = new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					new String("--");
					String halfGoalAgentObservedPercentage = new String("--");

					if (result_file.exists()) {
						try {
							final BufferedReader p = new BufferedReader(
									new FileReader(
											result_file.getAbsoluteFile()));
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							wastedMovements = p.readLine();
							goalAgentObservedPercentage = p.readLine();

							p.close();
						} catch (final Exception e) {
							JOptionPane.showMessageDialog(
									this,
									"Error opening/reading file "
											+ result_file.getAbsoluteFile()
											+ "(" + e + ")",
									"Error opening/reading file",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					if (half_result_file.exists()) {
						try {
							final BufferedReader p = new BufferedReader(
									new FileReader(
											half_result_file.getAbsoluteFile()));
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							p.readLine();
							halfGoalAgentObservedPercentage = p.readLine();

							p.close();
						} catch (final Exception e) {
							JOptionPane.showMessageDialog(
									this,
									"Error opening/reading file "
											+ half_result_file
													.getAbsoluteFile() + "("
											+ e + ")",
									"Error opening/reading file",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
					final Object[] object_row = new Object[ResultsTable.COLUMN_COUNT];
					object_row[0] = new String(config_string);
					object_row[1] = new String(config_id);
					object_row[2] = new String(wastedMovements);
					object_row[3] = new String(halfGoalAgentObservedPercentage);
					object_row[4] = new String(goalAgentObservedPercentage);
					/*
					 * object_row[4] = new String(spreadIndividualTotalPoints);
					 * object_row[5] = new String(averageIndividualTotalPoints);
					 * object_row[6] = new String(spreadAgentDistance);
					 * object_row[7] = new String(averageAgentDistance);
					 * object_row[8] = new String(spreadGoalAgentDistance);
					 * object_row[9] = new String(averageGoalAgentDistance);
					 * object_row[10] = new String(averagePredictionError);
					 * object_row[11] = new String(coveredAreaFactor);
					 * object_row[12] = new String(wastedCoverage);
					 * object_row[13] = new String(goalJumps); object_row[14] =
					 * new String(wastedMovements);
					 */

					results.datas.add(object_row);
				}
			}
		}
		final TableColumnModel tcm = resultsTable.getColumnModel();
		for (int i = 0; i < ResultsTable.COLUMN_COUNT; i++) {
			tcm.getColumn(i).setCellRenderer(new MyTableCellRenderer());
		}
		results.fireTableDataChanged();
	}

	private class MyTableCellRenderer extends
			javax.swing.table.DefaultTableCellRenderer {

		@Override
		public Component getTableCellRendererComponent(final JTable table,
				final Object value, final boolean isSelected,
				final boolean hasFocus, final int row, final int column) {
			// component will actually be this.
			final Component component = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			final int actual_row = sorter.modelIndex(row);
			final Object[] my_row = results.getRow(actual_row);

			final String config_string = (String) my_row[0];

			try {
				Configuration.initialize(config_string);
			} catch (final Exception e) {
				// JOptionPane.showMessageDialog(this,
				// "Error opening/reading file " + config_string + "(" + e +
				// ")", "Error opening/reading config file",
				// JOptionPane.ERROR_MESSAGE);
				return null;
			}
			Color c = Color.white;
			switch (Configuration.getAgentType()) {
			case Configuration.RANDOMIZED_MOVEMENT_AGENT_TYPE:
				c = Color.white;
				break;
			case Configuration.STATIC_AI_AGENT_TYPE:
				c = Color.LIGHT_GRAY;
				break;
			case Configuration.SXCS_AGENT_TYPE:
				c = Color.cyan;
				break;
			case Configuration.DSXCS_AGENT_TYPE:
				c = Color.green;
				break;
			case Configuration.STANDARD_XCS_AGENT_TYPE:
				c = Color.yellow;
				break;
			case Configuration.EVENT_XCS_AGENT_TYPE:
				c = Color.orange;
				break;
			}

			component.setBackground(c);
			return component;
		}
	}

	private void resetTimeString() {
		final SimpleDateFormat fmt = new SimpleDateFormat(
				"dd-MM-yy--HH-mm-ss-SS");
		timeString = fmt.format(new Date());
		conf_id = 1000;
	}

	private void saveSettings(final String file_name) {
		final File my_file = new File(file_name);
		try {
			my_file.createNewFile();
		} catch (final IOException e) {
			JOptionPane.showMessageDialog(this,
					"Error opening file " + my_file.getAbsoluteFile(),
					"Error opening file", JOptionPane.ERROR_MESSAGE);
			return;
		}
		FileOutputStream f;
		PrintStream p;
		try {
			f = new FileOutputStream(my_file.getAbsoluteFile());
			p = new PrintStream(f);

			p.println(Long.valueOf(randomSeedTextField.getText()));
			p.println(Integer.valueOf(numberOfExperimentsTextField.getText()));
			p.println(Integer.valueOf(numberOfProblemsTextField.getText()));
			p.println(Integer.valueOf(numberOfStepsTextField.getText()));
			p.println(Boolean.valueOf(createAnimatedGIFCheckBox.isSelected()));

			p.println(Integer.valueOf(maxPopSizeTextField.getText()));

			p.println(useQuadraticRewardCheckBox.isSelected());

			p.println(Integer.valueOf(maxXTextField.getText()));
			p.println(Integer.valueOf(maxYTextField.getText()));

			if (randomScenarioRadioButton.isSelected()) {
				p.println(Configuration.RANDOM_SCENARIO);
			} else if (pillarScenarioRadioButton.isSelected()) {
				p.println(Configuration.PILLAR_SCENARIO);
			} else if (difficultScenarioRadioButton.isSelected()) {
				p.println(Configuration.DIFFICULT_SCENARIO);
			}

			p.println(Double.valueOf(obstaclePercentageTextField.getText()));
			p.println(Double.valueOf(obstacleConnectionFactorTextField
					.getText()));

			p.println(Double.valueOf(rewardDistanceTextField.getText()));

			p.println(Double.valueOf(sightRangeTextField.getText()));
			p.println(Integer.valueOf(maxAgentsTextField.getText()));

			p.println(Integer.valueOf(maxStackSizeTextField.getText()));

			p.println(Double.valueOf(coveringWildcardProbabilityTextField
					.getText()));
			p.println(Double.valueOf(tournamentProbabilityTextField.getText()));
			p.println(randomStartCheckBox.isSelected());
			p.println(doEvolutionaryAlgorithmCheckBox.isSelected());

			p.println(Double.valueOf(thetaSubsumerTextField.getText()));
			p.println(Double.valueOf(epsilon0TextField.getText()));

			p.println(Double.valueOf(betaTextField.getText()));

			p.println(Double.valueOf(predictionInitializationTextField
					.getText()));
			// p.println(Boolean.valueOf(predictionInitializationAdaptionCheckBox.isSelected()));
			p.println(Double.valueOf(predictionErrorInitializationTextField
					.getText()));
			p.println(Double.valueOf(fitnessInitializationTextField.getText()));

			p.println(Double.valueOf(deltaTextField.getText()));
			p.println(Double.valueOf(thetaDelTextField.getText()));

			p.println(doActionSetSubsumptionCheckBox.isSelected());

			p.println(Double.valueOf(alphaTextField.getText()));
			p.println(Double.valueOf(gammaTextField.getText()));
			p.println(Double.valueOf(nuTextField.getText()));
			p.println(Double.valueOf(thetaTextField.getText()));

			p.println(Double.valueOf(predictionErrorReductionTextField
					.getText()));
			p.println(Double.valueOf(fitnessReductionTextField.getText()));
			p.println(Double.valueOf(mutationProbabilityTextField.getText()));

			p.println(doGASubsumptionCheckBox.isSelected());

			if (randomRouletteSelectionRadioButton.isSelected()) {
				p.println(Configuration.RANDOM_ROULETTE_SELECTION_MODE);
			} else if (randomTournamentSelectionRadioButton.isSelected()) {
				p.println(Configuration.RANDOM_TOURNAMENT_SELECTION_MODE);
			} else if (randomBestSelectionRadioButton.isSelected()) {
				p.println(Configuration.RANDOM_BEST_SELECTION_MODE);
			} else if (rouletteTournamentSelectionRadioButton.isSelected()) {
				p.println(Configuration.ROULETTE_TOURNAMENT_SELECTION_MODE);
			} else if (rouletteBestSelectionRadioButton.isSelected()) {
				p.println(Configuration.ROULETTE_BEST_SELECTION_MODE);
			} else if (tournamentBestSelectionRadioButton.isSelected()) {
				p.println(Configuration.TOURNAMENT_BEST_SELECTION_MODE);
			}

			if (switchGoalObsRadioButton.isSelected()) {
				p.println(Configuration.SWITCH_GOAL_OBS_MODE);
			} else if (switchGoalSightRadioButton.isSelected()) {
				p.println(Configuration.SWITCH_GOAL_SIGHT_MODE);
			} else if (switchRewardRadioButton.isSelected()) {
				p.println(Configuration.SWITCH_REWARD_MODE);
			} else if (switchNoRadioButton.isSelected()) {
				p.println(Configuration.SWITCH_NO_MODE);
			}

			if (goalObsRadioButton.isSelected()) {
				p.println(Configuration.GOAL_OBS_MODE);
			} else if (goalSightRadioButton.isSelected()) {
				p.println(Configuration.GOAL_SIGHT_MODE);
			} else if (goalObsAgentsObsRadioButton.isSelected()) {
				p.println(Configuration.GOAL_OBS_AGENTS_OBS_MODE);
			} else if (goalObsAgentsSightRadioButton.isSelected()) {
				p.println(Configuration.GOAL_OBS_AGENTS_SIGHT_MODE);
			} else if (goalSightAgentsObsRadioButton.isSelected()) {
				p.println(Configuration.GOAL_SIGHT_AGENTS_OBS_MODE);
			} else if (goalSightAgentsSightRadioButton.isSelected()) {
				p.println(Configuration.GOAL_SIGHT_AGENTS_SIGHT_MODE);
			}

			if (totalRandomGoalAgentMovementRadioButton.isSelected()) {
				p.println(Configuration.TOTAL_RANDOM_MOVEMENT);
			} else if (randomGoalAgentMovementRadioButton.isSelected()) {
				p.println(Configuration.RANDOM_MOVEMENT);
			} else if (maxOneDirectionChangeGoalAgentMovementRadioButton
					.isSelected()) {
				p.println(Configuration.RANDOM_DIRECTION_CHANGE);
			} else if (intelligentGoalAgentMovementRadioButton.isSelected()) {
				p.println(Configuration.INTELLIGENT_MOVEMENT);
			} else if (alwaysInTheSameDirectionGoalAgentMovementRadioButton
					.isSelected()) {
				p.println(Configuration.ALWAYS_SAME_DIRECTION);
			} else if (LCSGoalAgentMovementRadioButton.isSelected()) {
				p.println(Configuration.XCS_MOVEMENT);
			}

			p.println(Double.valueOf(goalAgentMovementSpeedTextField.getText()));

			if (randomizedMovementRadioButton.isSelected()) {
				p.println(Configuration.RANDOMIZED_MOVEMENT_AGENT_TYPE);
			} else if (staticAIAgentRadioButton.isSelected()) {
				p.println(Configuration.STATIC_AI_AGENT_TYPE);
			} else if (DSXCSAgentRadioButton.isSelected()) {
				p.println(Configuration.DSXCS_AGENT_TYPE);
			} else if (SXCSAgentRadioButton.isSelected()) {
				p.println(Configuration.SXCS_AGENT_TYPE);
			} else if (standardXCSAgentRadioButton.isSelected()) {
				p.println(Configuration.STANDARD_XCS_AGENT_TYPE);
			} else if (eventXCSAgentRadioButton.isSelected()) {
				p.println(Configuration.EVENT_XCS_AGENT_TYPE);
			}

			if (noExternalRewardRadioButton.isSelected()) {
				p.println(Configuration.NO_EXTERNAL_REWARD);
			} else if (rewardAllEquallyRadioButton.isSelected()) {
				p.println(Configuration.REWARD_ALL_EQUALLY);
			} else if (rewardEgoisticRadioButton.isSelected()) {
				p.println(Configuration.REWARD_EGOISM);
			}

			p.close();
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, "Error writing to file "
					+ my_file.getAbsoluteFile() + " : " + e,
					"Error writing file", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void loadSettings(final String file_name) {
		configurationNameLabel.setText(file_name.split("//")[file_name
				.split("//").length - 1]);
		try {
			Configuration.initialize(file_name);
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, "Error opening file "
					+ file_name + ": " + e, "Error opening file",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		randomSeedTextField.setText(String.valueOf(Configuration
				.getRandomSeed()));
		numberOfExperimentsTextField.setText(String.valueOf(Configuration
				.getNumberOfExperiments()));
		numberOfProblemsTextField.setText(String.valueOf(Configuration
				.getNumberOfProblems()));
		numberOfStepsTextField.setText(String.valueOf(Configuration
				.getNumberOfSteps()));
		createAnimatedGIFCheckBox.setSelected(Configuration.isGifOutput());

		maxPopSizeTextField.setText(String.valueOf(Configuration
				.getMaxPopSize()));

		useQuadraticRewardCheckBox.setSelected(Configuration
				.isUseQuadraticReward());

		maxXTextField.setText(String.valueOf(Configuration.getMaxX()));
		maxYTextField.setText(String.valueOf(Configuration.getMaxY()));

		switch (Configuration.getScenarioType()) {
		case Configuration.RANDOM_SCENARIO:
			scenarioTypeButtonGroup.setSelected(
					randomScenarioRadioButton.getModel(), true);
			break;
		case Configuration.PILLAR_SCENARIO:
			scenarioTypeButtonGroup.setSelected(
					pillarScenarioRadioButton.getModel(), true);
			break;
		case Configuration.DIFFICULT_SCENARIO:
			scenarioTypeButtonGroup.setSelected(
					difficultScenarioRadioButton.getModel(), true);
			break;
		}

		obstaclePercentageTextField.setText(String.valueOf(Configuration
				.getObstaclePercentage()));
		obstacleConnectionFactorTextField.setText(String.valueOf(Configuration
				.getObstacleConnectionFactor()));

		rewardDistanceTextField.setText(String.valueOf(Configuration
				.getRewardDistance()));
		sightRangeTextField.setText(String.valueOf(Configuration
				.getSightRange()));

		maxAgentsTextField
				.setText(String.valueOf(Configuration.getMaxAgents()));

		// number of steps for multi step problem
		maxStackSizeTextField.setText(String.valueOf(Configuration
				.getMaxStackSize()));

		coveringWildcardProbabilityTextField.setText(String
				.valueOf(Configuration.getCoveringWildcardProbability()));
		tournamentProbabilityTextField.setText(String.valueOf(Configuration
				.getTournamentProbability()));
		randomStartCheckBox.setSelected(Configuration.isRandomStart());
		doEvolutionaryAlgorithmCheckBox.setSelected(Configuration
				.isDoEvolutionaryAlgorithm());
		doEvolutionaryAlgorithmCheckBoxActionPerformed(null);

		thetaSubsumerTextField.setText(String.valueOf(Configuration
				.getThetaSubsumer()));
		epsilon0TextField.setText(String.valueOf(Configuration.getEpsilon0()));

		betaTextField.setText(String.valueOf(Configuration.getBeta()));

		predictionInitializationTextField.setText(String.valueOf(Configuration
				.getPredictionInitialization()));
		// predictionInitializationAdaptionCheckBox.setSelected(Configuration.isPredictionInitializationAdaption());
		predictionErrorInitializationTextField.setText(String
				.valueOf(Configuration.getPredictionErrorInitialization()));
		fitnessInitializationTextField.setText(String.valueOf(Configuration
				.getFitnessInitialization()));

		deltaTextField.setText(String.valueOf(Configuration.getDelta()));
		thetaDelTextField.setText(String.valueOf(Configuration.getThetaDel()));

		doActionSetSubsumptionCheckBox.setSelected(Configuration
				.isDoActionSetSubsumption());

		alphaTextField.setText(String.valueOf(Configuration.getAlpha()));
		gammaTextField.setText(String.valueOf(Configuration.getGamma()));
		nuTextField.setText(String.valueOf(Configuration.getNu()));
		thetaTextField.setText(String.valueOf(Configuration.getThetaGA()));

		predictionErrorReductionTextField.setText(String.valueOf(Configuration
				.getPredictionErrorReduction()));
		fitnessReductionTextField.setText(String.valueOf(Configuration
				.getFitnessReduction()));
		mutationProbabilityTextField.setText(String.valueOf(Configuration
				.getMutationProbability()));

		doGASubsumptionCheckBox.setSelected(Configuration.isDoGASubsumption());

		switch (Configuration.getExplorationMode()) {
		case Configuration.RANDOM_ROULETTE_SELECTION_MODE:
			exploreExploitPhaseButtonGroup.setSelected(
					randomRouletteSelectionRadioButton.getModel(), true);
			break;
		case Configuration.RANDOM_TOURNAMENT_SELECTION_MODE:
			exploreExploitPhaseButtonGroup.setSelected(
					randomTournamentSelectionRadioButton.getModel(), true);
			break;
		case Configuration.RANDOM_BEST_SELECTION_MODE:
			exploreExploitPhaseButtonGroup.setSelected(
					randomBestSelectionRadioButton.getModel(), true);
			break;
		case Configuration.ROULETTE_TOURNAMENT_SELECTION_MODE:
			exploreExploitPhaseButtonGroup.setSelected(
					rouletteTournamentSelectionRadioButton.getModel(), true);
			break;
		case Configuration.ROULETTE_BEST_SELECTION_MODE:
			exploreExploitPhaseButtonGroup.setSelected(
					rouletteBestSelectionRadioButton.getModel(), true);
			break;
		case Configuration.TOURNAMENT_BEST_SELECTION_MODE:
			exploreExploitPhaseButtonGroup.setSelected(
					tournamentBestSelectionRadioButton.getModel(), true);
			break;
		}

		switch (Configuration.getSwitchMode()) {
		case Configuration.SWITCH_GOAL_OBS_MODE:
			switchExploreExploitButtonGroup.setSelected(
					switchGoalObsRadioButton.getModel(), true);
			break;
		case Configuration.SWITCH_GOAL_SIGHT_MODE:
			switchExploreExploitButtonGroup.setSelected(
					switchGoalSightRadioButton.getModel(), true);
			break;
		case Configuration.SWITCH_REWARD_MODE:
			switchExploreExploitButtonGroup.setSelected(
					switchRewardRadioButton.getModel(), true);
			break;
		case Configuration.SWITCH_NO_MODE:
			switchExploreExploitButtonGroup.setSelected(
					switchNoRadioButton.getModel(), true);
			break;
		}

		switch (Configuration.getGoalMode()) {
		case Configuration.GOAL_OBS_MODE:
			baseRewardFunctionButtonGroup.setSelected(
					goalObsRadioButton.getModel(), true);
			break;
		case Configuration.GOAL_SIGHT_MODE:
			baseRewardFunctionButtonGroup.setSelected(
					goalSightRadioButton.getModel(), true);
			break;
		case Configuration.GOAL_OBS_AGENTS_OBS_MODE:
			baseRewardFunctionButtonGroup.setSelected(
					goalObsAgentsObsRadioButton.getModel(), true);
			break;
		case Configuration.GOAL_OBS_AGENTS_SIGHT_MODE:
			baseRewardFunctionButtonGroup.setSelected(
					goalObsAgentsSightRadioButton.getModel(), true);
			break;
		case Configuration.GOAL_SIGHT_AGENTS_OBS_MODE:
			baseRewardFunctionButtonGroup.setSelected(
					goalSightAgentsObsRadioButton.getModel(), true);
			break;
		case Configuration.GOAL_SIGHT_AGENTS_SIGHT_MODE:
			baseRewardFunctionButtonGroup.setSelected(
					goalSightAgentsSightRadioButton.getModel(), true);
			break;
		}

		switch (Configuration.getGoalAgentMovementType()) {
		case Configuration.TOTAL_RANDOM_MOVEMENT:
			goalAgentMovementButtonGroup.setSelected(
					totalRandomGoalAgentMovementRadioButton.getModel(), true);
			break;
		case Configuration.RANDOM_MOVEMENT:
			goalAgentMovementButtonGroup.setSelected(
					randomGoalAgentMovementRadioButton.getModel(), true);
			break;
		case Configuration.RANDOM_DIRECTION_CHANGE:
			goalAgentMovementButtonGroup.setSelected(
					maxOneDirectionChangeGoalAgentMovementRadioButton
							.getModel(), true);
			break;
		case Configuration.INTELLIGENT_MOVEMENT:
			goalAgentMovementButtonGroup.setSelected(
					intelligentGoalAgentMovementRadioButton.getModel(), true);
			break;
		case Configuration.ALWAYS_SAME_DIRECTION:
			goalAgentMovementButtonGroup.setSelected(
					alwaysInTheSameDirectionGoalAgentMovementRadioButton
							.getModel(), true);
			break;
		case Configuration.XCS_MOVEMENT:
			goalAgentMovementButtonGroup.setSelected(
					LCSGoalAgentMovementRadioButton.getModel(), true);
			break;
		}

		goalAgentMovementSpeedTextField.setText(String.valueOf(Configuration
				.getGoalAgentMovementSpeed()));

		switch (Configuration.getAgentType()) {
		case Configuration.RANDOMIZED_MOVEMENT_AGENT_TYPE:
			agentTypeButtonGroup.setSelected(
					randomizedMovementRadioButton.getModel(), true);
			activateLCSControls(false);
			break;
		case Configuration.STATIC_AI_AGENT_TYPE:
			agentTypeButtonGroup.setSelected(
					staticAIAgentRadioButton.getModel(), true);
			activateLCSControls(false);
			break;
		case Configuration.DSXCS_AGENT_TYPE:
			agentTypeButtonGroup.setSelected(DSXCSAgentRadioButton.getModel(),
					true);
			activateLCSControls(true);
			break;
		case Configuration.SXCS_AGENT_TYPE:
			agentTypeButtonGroup.setSelected(SXCSAgentRadioButton.getModel(),
					true);
			activateLCSControls(true);
			break;
		case Configuration.STANDARD_XCS_AGENT_TYPE:
			agentTypeButtonGroup.setSelected(
					standardXCSAgentRadioButton.getModel(), true);
			activateLCSControls(true);
			break;
		case Configuration.EVENT_XCS_AGENT_TYPE:
			agentTypeButtonGroup.setSelected(
					eventXCSAgentRadioButton.getModel(), true);
			activateLCSControls(true);
			break;
		}

		switch (Configuration.getExternalRewardMode()) {
		case Configuration.NO_EXTERNAL_REWARD:
			externalRewardButtonGroup.setSelected(
					noExternalRewardRadioButton.getModel(), true);
			break;
		case Configuration.REWARD_ALL_EQUALLY:
			externalRewardButtonGroup.setSelected(
					rewardAllEquallyRadioButton.getModel(), true);
			break;
		case Configuration.REWARD_EGOISM:
			externalRewardButtonGroup.setSelected(
					rewardEgoisticRadioButton.getModel(), true);
			break;
		}
	}

	public void createAllPlotFile() {
		String entry = new String("");
		final int number_steps = Integer.valueOf(numberOfStepsTextField
				.getText());
		final int number_problems = Integer.valueOf(numberOfProblemsTextField
				.getText());
		Double.valueOf(sightRangeTextField.getText());

		entry += "set key left box\n" + "set xrange [0:" + number_steps
				* number_problems + "]\n";
		final String file_name = "plot-all-" + timeString + ".plt";

		String header = new String("");
		String do_plot1 = new String("");
		String do_plot2 = new String("");

		header += "set output \"plot_";
		do_plot1 += ".eps\"\n" + "set terminal postscript eps\n" + "plot ";
		do_plot2 += ".png\"\n" + "set terminal png\n" + "plot ";

		final String[] stats = { "average_last_x_steps_goal_agent_observed",
				"best_last_x_steps_goal_agent_observed" };// ,
															// "goal_percentage"};//,
															// "points_spread",
															// "points_average",
															// "distance_spread",
															// "goal_agent_distance_spread",
															// "distance_average",
															// "goal_agent_distance_average",
															// "covered_area"};
		final String[] yrange = { "0:50", "0:50", "0:50", "0:50" };

		// TODO strings gemeinsam in Configuration.java
		// String[] stats = {"goal_agent_observed", "goal_percentage",
		// "points_spread", "points_average", "distance_spread",
		// "goal_agent_distance_spread", "distance_average",
		// "goal_agent_distance_average", "covered_area"};
		// String[] yrange = {"0:" + number_steps * number_problems / 10, "0:" +
		// number_steps * number_problems, "0:" + 2.0 * sight_range, "0:" + 2.0
		// * sight_range, "0:" + 2 * sight_range, "0:" + 2 * sight_range,
		// "0.0:1.0", "0.0:1.0"};
		entry = new String("");

		for (int i = 0; i < config_strings.size(); i++) {
			entry += "set style line " + (1 + i) + " lt " + (1 + i) + " lw 1\n";
		}

		int nn = 0;
		for (final String s : stats) {
			entry += "set yrange [" + yrange[nn] + "]\n";
			int n = 0;
			nn++;

			String dat_files = new String("");
			for (final String c : config_strings) {
				n++;
				dat_files += "\"output_" + c + "\\\\" + s + "-" + c
						+ ".dat\" with lines ls " + n;
				if (n < config_strings.size()) {
					dat_files += ", ";
				}
			}

			dat_files += "\n";
			entry += header + s + "-" + timeString + do_plot1 + dat_files;
			entry += header + s + "-" + timeString + do_plot2 + dat_files;
		}

		try {
			plot_out = new BufferedWriter(new FileWriter(file_name, true));
			plot_out.write(entry);
			plot_out.flush();
			plot_out.close();
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, "Unable to open plot file: "
					+ e + " (" + file_name + ")", "Error opening file",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is always
	 * regenerated by the Form Editor.
	 */
	// <editor-fold defaultstate="collapsed"
	// desc="Generated Code">//GEN-BEGIN:initComponents
	private void initComponents() {

		fileChooser = new javax.swing.JFileChooser();
		goalAgentMovementButtonGroup = new javax.swing.ButtonGroup();
		agentTypeButtonGroup = new javax.swing.ButtonGroup();
		externalRewardButtonGroup = new javax.swing.ButtonGroup();
		scenarioTypeButtonGroup = new javax.swing.ButtonGroup();
		baseRewardFunctionButtonGroup = new javax.swing.ButtonGroup();
		exploreExploitPhaseButtonGroup = new javax.swing.ButtonGroup();
		switchExploreExploitButtonGroup = new javax.swing.ButtonGroup();
		problemDefinitionPanel = new javax.swing.JPanel();
		gridPanel = new javax.swing.JPanel();
		obstaclePercentageLabel = new javax.swing.JLabel();
		obstaclePercentageTextField = new javax.swing.JTextField();
		obstacleConnectionFactorTextField = new javax.swing.JTextField();
		maxXLabel = new javax.swing.JLabel();
		maxXTextField = new javax.swing.JTextField();
		maxYTextField = new javax.swing.JTextField();
		obstacleConnectionFactorLabel = new javax.swing.JLabel();
		rewardRangeLabel = new javax.swing.JLabel();
		rewardDistanceTextField = new javax.swing.JTextField();
		randomScenarioRadioButton = new javax.swing.JRadioButton();
		pillarScenarioRadioButton = new javax.swing.JRadioButton();
		difficultScenarioRadioButton = new javax.swing.JRadioButton();
		sightRangeTextField = new javax.swing.JTextField();
		rewardDistanceLabel = new javax.swing.JLabel();
		testsPanel = new javax.swing.JPanel();
		numberOfExperimentsLabel = new javax.swing.JLabel();
		numberOfExperimentsTextField = new javax.swing.JTextField();
		numberOfProblemsTextField = new javax.swing.JTextField();
		numberOfStepsTextField = new javax.swing.JTextField();
		randomSeedLabel = new javax.swing.JLabel();
		randomSeedTextField = new javax.swing.JTextField();
		agentCountLabel = new javax.swing.JLabel();
		maxAgentsTextField = new javax.swing.JTextField();
		stepsLabel = new javax.swing.JLabel();
		problemsLabel = new javax.swing.JLabel();
		goalAgentMovementPanel = new javax.swing.JPanel();
		totalRandomGoalAgentMovementRadioButton = new javax.swing.JRadioButton();
		randomGoalAgentMovementRadioButton = new javax.swing.JRadioButton();
		maxOneDirectionChangeGoalAgentMovementRadioButton = new javax.swing.JRadioButton();
		alwaysInTheSameDirectionGoalAgentMovementRadioButton = new javax.swing.JRadioButton();
		goalAgentMovementSpeedLabel = new javax.swing.JLabel();
		goalAgentMovementSpeedTextField = new javax.swing.JTextField();
		intelligentGoalAgentMovementRadioButton = new javax.swing.JRadioButton();
		LCSGoalAgentMovementRadioButton = new javax.swing.JRadioButton();
		lcsParametersPanel = new javax.swing.JPanel();
		gaParametersPanel = new javax.swing.JPanel();
		thetaLabel = new javax.swing.JLabel();
		predictionErrorReductionLabel = new javax.swing.JLabel();
		fitnessReductionLabel = new javax.swing.JLabel();
		fitnessReductionTextField = new javax.swing.JTextField();
		predictionErrorReductionTextField = new javax.swing.JTextField();
		thetaTextField = new javax.swing.JTextField();
		mutationProbabilityLabel = new javax.swing.JLabel();
		mutationProbabilityTextField = new javax.swing.JTextField();
		doEvolutionaryAlgorithmCheckBox = new javax.swing.JCheckBox();
		doGASubsumptionCheckBox = new javax.swing.JCheckBox();
		fitnessAndPredictionPanel = new javax.swing.JPanel();
		predictionInitializationLabel = new javax.swing.JLabel();
		predictionErrorInitializationLabel = new javax.swing.JLabel();
		fitnessInitializationLabel = new javax.swing.JLabel();
		fitnessInitializationTextField = new javax.swing.JTextField();
		predictionErrorInitializationTextField = new javax.swing.JTextField();
		predictionInitializationTextField = new javax.swing.JTextField();
		epsilon0Label = new javax.swing.JLabel();
		epsilon0TextField = new javax.swing.JTextField();
		alphaLabel = new javax.swing.JLabel();
		betaLabel = new javax.swing.JLabel();
		nuLabel = new javax.swing.JLabel();
		betaTextField = new javax.swing.JTextField();
		alphaTextField = new javax.swing.JTextField();
		nuTextField = new javax.swing.JTextField();
		gammaLabel = new javax.swing.JLabel();
		gammaTextField = new javax.swing.JTextField();
		classifierSubsumptionAndDeletionPanel = new javax.swing.JPanel();
		thetaSubsumerLabel = new javax.swing.JLabel();
		thetaSubsumerTextField = new javax.swing.JTextField();
		deltaLabel = new javax.swing.JLabel();
		thetaDelLabel = new javax.swing.JLabel();
		deltaTextField = new javax.swing.JTextField();
		thetaDelTextField = new javax.swing.JTextField();
		maxPopSizeLabel = new javax.swing.JLabel();
		maxPopSizeTextField = new javax.swing.JTextField();
		doActionSetSubsumptionCheckBox = new javax.swing.JCheckBox();
		coveringWildcardProbabilityLabel = new javax.swing.JLabel();
		coveringWildcardProbabilityTextField = new javax.swing.JTextField();
		randomStartCheckBox = new javax.swing.JCheckBox();
		tournamentProbabilityLabel = new javax.swing.JLabel();
		tournamentProbabilityTextField = new javax.swing.JTextField();
		rewardModelPanel = new javax.swing.JPanel();
		maxStackSizeLabel = new javax.swing.JLabel();
		maxStackSizeTextField = new javax.swing.JTextField();
		useQuadraticRewardCheckBox = new javax.swing.JCheckBox();
		agentTypePanel = new javax.swing.JPanel();
		agentAlgorithmPanel = new javax.swing.JPanel();
		randomizedMovementRadioButton = new javax.swing.JRadioButton();
		staticAIAgentRadioButton = new javax.swing.JRadioButton();
		DSXCSAgentRadioButton = new javax.swing.JRadioButton();
		SXCSAgentRadioButton = new javax.swing.JRadioButton();
		standardXCSAgentRadioButton = new javax.swing.JRadioButton();
		eventXCSAgentRadioButton = new javax.swing.JRadioButton();
		communicationPanel = new javax.swing.JPanel();
		noExternalRewardRadioButton = new javax.swing.JRadioButton();
		rewardAllEquallyRadioButton = new javax.swing.JRadioButton();
		rewardEgoisticRadioButton = new javax.swing.JRadioButton();
		explorationModePanel = new javax.swing.JPanel();
		exploreExploitPhasePanel = new javax.swing.JPanel();
		randomRouletteSelectionRadioButton = new javax.swing.JRadioButton();
		randomTournamentSelectionRadioButton = new javax.swing.JRadioButton();
		randomBestSelectionRadioButton = new javax.swing.JRadioButton();
		rouletteTournamentSelectionRadioButton = new javax.swing.JRadioButton();
		rouletteBestSelectionRadioButton = new javax.swing.JRadioButton();
		tournamentBestSelectionRadioButton = new javax.swing.JRadioButton();
		switchExploreExploitPanel = new javax.swing.JPanel();
		switchGoalObsRadioButton = new javax.swing.JRadioButton();
		switchGoalSightRadioButton = new javax.swing.JRadioButton();
		switchRewardRadioButton = new javax.swing.JRadioButton();
		switchNoRadioButton = new javax.swing.JRadioButton();
		baseRewardFunctionPanel = new javax.swing.JPanel();
		goalObsRadioButton = new javax.swing.JRadioButton();
		goalSightRadioButton = new javax.swing.JRadioButton();
		goalObsAgentsObsRadioButton = new javax.swing.JRadioButton();
		goalObsAgentsSightRadioButton = new javax.swing.JRadioButton();
		goalSightAgentsObsRadioButton = new javax.swing.JRadioButton();
		goalSightAgentsSightRadioButton = new javax.swing.JRadioButton();
		automaticTestsPanel = new javax.swing.JPanel();
		saveSpeedButton = new javax.swing.JButton();
		saveAllRandomButton = new javax.swing.JButton();
		saveAllExplorationButton = new javax.swing.JButton();
		saveAllStackButton = new javax.swing.JButton();
		saveAllPopulationButton = new javax.swing.JButton();
		saveStepsButton = new javax.swing.JButton();
		saveRandomButton = new javax.swing.JButton();
		saveTournamentButton = new javax.swing.JButton();
		saveLearningButton = new javax.swing.JButton();
		savePredictionDiscountButton = new javax.swing.JButton();
		saveAllExplorationButton1 = new javax.swing.JButton();
		inputOutputPanel = new javax.swing.JPanel();
		logOutputCheckBox = new javax.swing.JCheckBox();
		createAnimatedGIFCheckBox = new javax.swing.JCheckBox();
		saveNewButton = new javax.swing.JButton();
		packageButton = new javax.swing.JButton();
		updateDatabaseButton = new javax.swing.JButton();
		runLastBatchButton = new javax.swing.JButton();
		configurationNameLabel = new javax.swing.JLabel();
		resultsScrollPane = new javax.swing.JScrollPane();
		resultsTable = new javax.swing.JTable();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setTitle("Agent Configuration File Editor v1.00");

		problemDefinitionPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Problem definition",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		gridPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Grid", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		obstaclePercentageLabel.setFont(new java.awt.Font("Arial", 0, 10));
		obstaclePercentageLabel.setText("Grid percentage");
		obstaclePercentageLabel
				.setToolTipText("Percentage of the grid that is occupied by obstacles");

		obstaclePercentageTextField.setText("0.2");

		obstacleConnectionFactorTextField.setText("0.99");

		maxXLabel.setFont(new java.awt.Font("Arial", 0, 12));
		maxXLabel.setText("Max X / Max Y");

		maxXTextField.setText("16");

		maxYTextField.setText("16");

		obstacleConnectionFactorLabel
				.setFont(new java.awt.Font("Arial", 0, 10));
		obstacleConnectionFactorLabel.setText("Connection factor");

		rewardRangeLabel.setFont(new java.awt.Font("Arial", 0, 12));
		rewardRangeLabel.setText("Reward range");

		rewardDistanceTextField.setText("2.0");

		scenarioTypeButtonGroup.add(randomScenarioRadioButton);
		randomScenarioRadioButton.setSelected(true);
		randomScenarioRadioButton.setText("Random scenario");
		randomScenarioRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						randomScenarioRadioButtonActionPerformed(evt);
					}
				});

		scenarioTypeButtonGroup.add(pillarScenarioRadioButton);
		pillarScenarioRadioButton.setText("Pillar scenario");
		pillarScenarioRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						pillarScenarioRadioButtonActionPerformed(evt);
					}
				});

		scenarioTypeButtonGroup.add(difficultScenarioRadioButton);
		difficultScenarioRadioButton.setText("Difficult scenario");

		sightRangeTextField.setText("5.0");

		rewardDistanceLabel.setFont(new java.awt.Font("Arial", 0, 12));
		rewardDistanceLabel.setText("Sight range");

		final javax.swing.GroupLayout gridPanelLayout = new javax.swing.GroupLayout(
				gridPanel);
		gridPanel.setLayout(gridPanelLayout);
		gridPanelLayout
				.setHorizontalGroup(gridPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								gridPanelLayout
										.createSequentialGroup()
										.addGroup(
												gridPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																difficultScenarioRadioButton)
														.addComponent(
																randomScenarioRadioButton)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																gridPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				maxXLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				30,
																				Short.MAX_VALUE)
																		.addComponent(
																				maxXTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				25,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				maxYTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				25,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addComponent(
																pillarScenarioRadioButton)
														.addGroup(
																gridPanelLayout
																		.createSequentialGroup()
																		.addGroup(
																				gridPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addComponent(
																								rewardRangeLabel)
																						.addComponent(
																								rewardDistanceLabel))
																		.addGap(47,
																				47,
																				47)
																		.addGroup(
																				gridPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								sightRangeTextField,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								32,
																								Short.MAX_VALUE)
																						.addComponent(
																								rewardDistanceTextField,
																								javax.swing.GroupLayout.Alignment.LEADING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								32,
																								Short.MAX_VALUE)))
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																gridPanelLayout
																		.createSequentialGroup()
																		.addGap(36,
																				36,
																				36)
																		.addGroup(
																				gridPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING)
																						.addComponent(
																								obstacleConnectionFactorLabel)
																						.addComponent(
																								obstaclePercentageLabel))
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addGroup(
																				gridPanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING,
																								false)
																						.addComponent(
																								obstaclePercentageTextField)
																						.addComponent(
																								obstacleConnectionFactorTextField,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								32,
																								Short.MAX_VALUE))))
										.addContainerGap()));
		gridPanelLayout
				.setVerticalGroup(gridPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								gridPanelLayout
										.createSequentialGroup()
										.addGroup(
												gridPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																rewardDistanceLabel)
														.addComponent(
																sightRangeTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																17,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gridPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																rewardRangeLabel)
														.addComponent(
																rewardDistanceTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gridPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																maxXTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																maxYTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(maxXLabel))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(randomScenarioRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gridPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																obstaclePercentageTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																obstaclePercentageLabel))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gridPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																obstacleConnectionFactorLabel)
														.addComponent(
																obstacleConnectionFactorTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(pillarScenarioRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(
												difficultScenarioRadioButton)
										.addGap(89, 89, 89)));

		testsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
				"Tests", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		numberOfExperimentsLabel.setFont(new java.awt.Font("Arial", 0, 12));
		numberOfExperimentsLabel.setText("Experiments");

		numberOfExperimentsTextField.setText("10");

		numberOfProblemsTextField.setText("10");

		numberOfStepsTextField.setText("500");

		randomSeedLabel.setFont(new java.awt.Font("Arial", 0, 12));
		randomSeedLabel.setText("Random Seed");

		randomSeedTextField.setText("0");

		agentCountLabel.setFont(new java.awt.Font("Arial", 0, 12));
		agentCountLabel.setText("Number of agents");

		maxAgentsTextField.setText("8");

		stepsLabel.setFont(new java.awt.Font("Arial", 0, 12));
		stepsLabel.setText("Steps");

		problemsLabel.setFont(new java.awt.Font("Arial", 0, 12));
		problemsLabel.setText("Problems");

		final javax.swing.GroupLayout testsPanelLayout = new javax.swing.GroupLayout(
				testsPanel);
		testsPanel.setLayout(testsPanelLayout);
		testsPanelLayout
				.setHorizontalGroup(testsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								testsPanelLayout
										.createSequentialGroup()
										.addGroup(
												testsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																agentCountLabel)
														.addComponent(
																randomSeedLabel)
														.addComponent(
																numberOfExperimentsLabel)
														.addComponent(
																problemsLabel)
														.addComponent(
																stepsLabel))
										.addGap(28, 28, 28)
										.addGroup(
												testsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																randomSeedTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																numberOfExperimentsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																numberOfProblemsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																numberOfStepsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																maxAgentsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE))));

		testsPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { maxAgentsTextField,
						numberOfExperimentsTextField,
						numberOfProblemsTextField, numberOfStepsTextField,
						randomSeedTextField });

		testsPanelLayout
				.setVerticalGroup(testsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								testsPanelLayout
										.createSequentialGroup()
										.addGroup(
												testsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																randomSeedLabel)
														.addComponent(
																randomSeedTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												testsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																numberOfExperimentsLabel)
														.addComponent(
																numberOfExperimentsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(7, 7, 7)
										.addGroup(
												testsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																problemsLabel)
														.addComponent(
																numberOfProblemsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												testsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																stepsLabel)
														.addComponent(
																numberOfStepsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												testsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																agentCountLabel)
														.addComponent(
																maxAgentsTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		goalAgentMovementPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Goal Agent Movement",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		goalAgentMovementButtonGroup
				.add(totalRandomGoalAgentMovementRadioButton);
		totalRandomGoalAgentMovementRadioButton.setFont(new java.awt.Font(
				"Arial", 0, 12));
		totalRandomGoalAgentMovementRadioButton.setText("Total random");

		goalAgentMovementButtonGroup.add(randomGoalAgentMovementRadioButton);
		randomGoalAgentMovementRadioButton.setFont(new java.awt.Font("Arial",
				0, 12));
		randomGoalAgentMovementRadioButton.setLabel("Random neighbor");

		goalAgentMovementButtonGroup
				.add(maxOneDirectionChangeGoalAgentMovementRadioButton);
		maxOneDirectionChangeGoalAgentMovementRadioButton
				.setFont(new java.awt.Font("Arial", 0, 12));
		maxOneDirectionChangeGoalAgentMovementRadioButton
				.setText("One direction change");

		goalAgentMovementButtonGroup
				.add(alwaysInTheSameDirectionGoalAgentMovementRadioButton);
		alwaysInTheSameDirectionGoalAgentMovementRadioButton
				.setFont(new java.awt.Font("Arial", 0, 12));
		alwaysInTheSameDirectionGoalAgentMovementRadioButton
				.setText("Always same direction");

		goalAgentMovementSpeedLabel.setFont(new java.awt.Font("Arial", 0, 12));
		goalAgentMovementSpeedLabel.setText("Speed");

		goalAgentMovementSpeedTextField.setText("2");

		goalAgentMovementButtonGroup
				.add(intelligentGoalAgentMovementRadioButton);
		intelligentGoalAgentMovementRadioButton.setFont(new java.awt.Font(
				"Arial", 0, 12));
		intelligentGoalAgentMovementRadioButton.setSelected(true);
		intelligentGoalAgentMovementRadioButton.setText("Intelligent");
		intelligentGoalAgentMovementRadioButton
				.setToolTipText("Move away from other agents");

		goalAgentMovementButtonGroup.add(LCSGoalAgentMovementRadioButton);
		LCSGoalAgentMovementRadioButton.setFont(new java.awt.Font("Arial", 0,
				12));
		LCSGoalAgentMovementRadioButton.setText("SXCS");
		LCSGoalAgentMovementRadioButton
				.setToolTipText("Reward by agents out of sight");

		final javax.swing.GroupLayout goalAgentMovementPanelLayout = new javax.swing.GroupLayout(
				goalAgentMovementPanel);
		goalAgentMovementPanel.setLayout(goalAgentMovementPanelLayout);
		goalAgentMovementPanelLayout
				.setHorizontalGroup(goalAgentMovementPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(totalRandomGoalAgentMovementRadioButton)
						.addComponent(randomGoalAgentMovementRadioButton)
						.addComponent(intelligentGoalAgentMovementRadioButton)
						.addGroup(
								goalAgentMovementPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(
												goalAgentMovementSpeedLabel)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												58, Short.MAX_VALUE)
										.addComponent(
												goalAgentMovementSpeedTextField,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												43,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(20, 20, 20))
						.addGroup(
								goalAgentMovementPanelLayout
										.createSequentialGroup()
										.addComponent(
												maxOneDirectionChangeGoalAgentMovementRadioButton)
										.addContainerGap())
						.addGroup(
								goalAgentMovementPanelLayout
										.createSequentialGroup()
										.addComponent(
												alwaysInTheSameDirectionGoalAgentMovementRadioButton)
										.addContainerGap())
						.addGroup(
								goalAgentMovementPanelLayout
										.createSequentialGroup()
										.addComponent(
												LCSGoalAgentMovementRadioButton)
										.addContainerGap()));
		goalAgentMovementPanelLayout
				.setVerticalGroup(goalAgentMovementPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								goalAgentMovementPanelLayout
										.createSequentialGroup()
										.addComponent(
												totalRandomGoalAgentMovementRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												randomGoalAgentMovementRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												maxOneDirectionChangeGoalAgentMovementRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												intelligentGoalAgentMovementRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												alwaysInTheSameDirectionGoalAgentMovementRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												LCSGoalAgentMovementRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addGroup(
												goalAgentMovementPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																goalAgentMovementSpeedLabel)
														.addComponent(
																goalAgentMovementSpeedTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))));

		final javax.swing.GroupLayout problemDefinitionPanelLayout = new javax.swing.GroupLayout(
				problemDefinitionPanel);
		problemDefinitionPanel.setLayout(problemDefinitionPanelLayout);
		problemDefinitionPanelLayout
				.setHorizontalGroup(problemDefinitionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								problemDefinitionPanelLayout
										.createSequentialGroup()
										.addGroup(
												problemDefinitionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(
																goalAgentMovementPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																gridPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																0,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																testsPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		problemDefinitionPanelLayout
				.setVerticalGroup(problemDefinitionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								problemDefinitionPanelLayout
										.createSequentialGroup()
										.addComponent(
												testsPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(1, 1, 1)
										.addComponent(
												gridPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												218,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												goalAgentMovementPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		lcsParametersPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "LCS parameters",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		gaParametersPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "GA parameters",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		thetaLabel.setFont(new java.awt.Font("Arial", 0, 12));
		thetaLabel
				.setText("<html>GA threshold <i>&theta<sub>GA</sub></i></html>");
		thetaLabel
				.setToolTipText("The threshold for the GA application in an action set (time between GA runs)");

		predictionErrorReductionLabel
				.setFont(new java.awt.Font("Arial", 0, 12));
		predictionErrorReductionLabel.setText("Prediction error reduction");
		predictionErrorReductionLabel
				.setToolTipText("The reduction of the prediction error when generating an offspring classifier");

		fitnessReductionLabel.setFont(new java.awt.Font("Arial", 0, 12));
		fitnessReductionLabel.setText("Fitness Reduction");
		fitnessReductionLabel
				.setToolTipText("The reduction of the fitness when generating an offspring classifier");

		fitnessReductionTextField.setText("0.1");
		fitnessReductionTextField
				.setToolTipText("The reduction of the fitness when generating an offspring classifier");

		predictionErrorReductionTextField.setText("0.25");
		predictionErrorReductionTextField
				.setToolTipText("The reduction of the prediction error when generating an offspring classifier");

		thetaTextField.setText("25.0");
		thetaTextField
				.setToolTipText("The threshold for the GA application in an action set (time between GA runs)");

		mutationProbabilityLabel.setFont(new java.awt.Font("Arial", 0, 12));
		mutationProbabilityLabel
				.setText("<html>Mutation probability <i>&mu;</i> </html>");
		mutationProbabilityLabel
				.setToolTipText("The probability of mutating one allele and the action in an offspring classifier");

		mutationProbabilityTextField.setText("0.05");
		mutationProbabilityTextField
				.setToolTipText("The probability of mutating one allele and the action in an offspring classifier");

		doEvolutionaryAlgorithmCheckBox.setFont(new java.awt.Font("Arial", 0,
				12));
		doEvolutionaryAlgorithmCheckBox.setSelected(true);
		doEvolutionaryAlgorithmCheckBox.setText("Use genetic algorithm?");
		doEvolutionaryAlgorithmCheckBox
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						doEvolutionaryAlgorithmCheckBoxActionPerformed(evt);
					}
				});

		doGASubsumptionCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
		doGASubsumptionCheckBox.setSelected(true);
		doGASubsumptionCheckBox.setText("GA subsumption");
		doGASubsumptionCheckBox.setActionCommand("Do GA Subsumption");

		final javax.swing.GroupLayout gaParametersPanelLayout = new javax.swing.GroupLayout(
				gaParametersPanel);
		gaParametersPanel.setLayout(gaParametersPanelLayout);
		gaParametersPanelLayout
				.setHorizontalGroup(gaParametersPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								gaParametersPanelLayout
										.createSequentialGroup()
										.addGroup(
												gaParametersPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																thetaLabel)
														.addComponent(
																doGASubsumptionCheckBox)
														.addComponent(
																mutationProbabilityLabel)
														.addComponent(
																doEvolutionaryAlgorithmCheckBox)
														.addComponent(
																fitnessReductionLabel)
														.addComponent(
																predictionErrorReductionLabel))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												27, Short.MAX_VALUE)
										.addGroup(
												gaParametersPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																thetaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																mutationProbabilityTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																predictionErrorReductionTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																fitnessReductionTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE))));
		gaParametersPanelLayout
				.setVerticalGroup(gaParametersPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								gaParametersPanelLayout
										.createSequentialGroup()
										.addComponent(
												doEvolutionaryAlgorithmCheckBox)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)
										.addComponent(doGASubsumptionCheckBox)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gaParametersPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																thetaLabel)
														.addComponent(
																thetaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gaParametersPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																mutationProbabilityLabel)
														.addComponent(
																mutationProbabilityTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gaParametersPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																predictionErrorReductionLabel)
														.addComponent(
																predictionErrorReductionTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												gaParametersPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																fitnessReductionLabel)
														.addComponent(
																fitnessReductionTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap()));

		fitnessAndPredictionPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Fitness init and update",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		predictionInitializationLabel
				.setFont(new java.awt.Font("Arial", 0, 12));
		predictionInitializationLabel
				.setText("<html>Prediction init <i>p<sub>I</sub></i></html>");

		predictionErrorInitializationLabel.setFont(new java.awt.Font("Arial",
				0, 12));
		predictionErrorInitializationLabel
				.setText("<html>Prediction error init <i>&epsilon;<sub>I</sub></i></html>");

		fitnessInitializationLabel.setFont(new java.awt.Font("Arial", 0, 12));
		fitnessInitializationLabel
				.setText("<html>Fitness init <i>F<sub>I</sub></i></html>");

		fitnessInitializationTextField.setText("0.01");

		predictionErrorInitializationTextField.setText("0.0");

		predictionInitializationTextField.setText("1.0");

		epsilon0Label.setFont(new java.awt.Font("Arial", 0, 12));
		epsilon0Label
				.setText("<html>Accuracy equal below <i>&epsilon;<sub>0</sub></i></html>");
		epsilon0Label
				.setToolTipText("The error threshold (prediction error) under which the accuracy of a classifier is set to one.");

		epsilon0TextField.setText("0.01");
		epsilon0TextField
				.setToolTipText("The error threshold (prediction error) under which the accuracy of a classifier is set to one.");

		alphaLabel.setFont(new java.awt.Font("Arial", 0, 12));
		alphaLabel.setText("<html>Accuracy calculation <i>&alpha;</i></html>");
		alphaLabel.setToolTipText("The fall of rate in the fitness evaluation");

		betaLabel.setFont(new java.awt.Font("Arial", 0, 12));
		betaLabel.setText("<html>Learning rate <i>&beta;</i> </html>");
		betaLabel
				.setToolTipText("The learning rate for updating fitness, prediction, prediction error and action set size estimate in XCS's classifiers");

		nuLabel.setFont(new java.awt.Font("Arial", 0, 12));
		nuLabel.setText("<html>Accuracy power <i>&nu;</i></html>");
		nuLabel.setToolTipText("Specifies the exponent in the power function for the fitness evaluation");

		betaTextField.setText("0.01");
		betaTextField
				.setToolTipText("The learning rate for updating fitness, prediction, prediction error and action set size estimate in XCS's classifiers");

		alphaTextField.setText("0.1");
		alphaTextField
				.setToolTipText("The fall of rate in the fitness evaluation");

		nuTextField.setText("5.0");
		nuTextField
				.setToolTipText("Specifies the exponent in the power function for the fitness evaluation");

		gammaLabel.setFont(new java.awt.Font("Arial", 0, 12));
		gammaLabel.setText("<html>Prediction discount <i>&gamma;</i></html>");
		gammaLabel.setToolTipText("The discount rate in multi-step problems.");

		gammaTextField.setText("0.71");
		gammaTextField
				.setToolTipText("The discount rate in multi-step problems.");

		final javax.swing.GroupLayout fitnessAndPredictionPanelLayout = new javax.swing.GroupLayout(
				fitnessAndPredictionPanel);
		fitnessAndPredictionPanel.setLayout(fitnessAndPredictionPanelLayout);
		fitnessAndPredictionPanelLayout
				.setHorizontalGroup(fitnessAndPredictionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								fitnessAndPredictionPanelLayout
										.createSequentialGroup()
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																predictionErrorInitializationLabel)
														.addComponent(
																predictionInitializationLabel)
														.addComponent(
																fitnessInitializationLabel)
														.addComponent(
																alphaLabel)
														.addComponent(nuLabel)
														.addComponent(
																gammaLabel)
														.addComponent(betaLabel)
														.addComponent(
																epsilon0Label,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																145,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(34, 34, 34)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addGroup(
																fitnessAndPredictionPanelLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																		.addComponent(
																				epsilon0TextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				40,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				fitnessInitializationTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				40,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				predictionInitializationTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				40,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addComponent(
																				predictionErrorInitializationTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				40,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addComponent(
																alphaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																nuTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																gammaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																betaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(1, Short.MAX_VALUE)));
		fitnessAndPredictionPanelLayout
				.setVerticalGroup(fitnessAndPredictionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								fitnessAndPredictionPanelLayout
										.createSequentialGroup()
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																fitnessInitializationLabel)
														.addComponent(
																fitnessInitializationTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(1, 1, 1)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																predictionInitializationLabel)
														.addComponent(
																predictionInitializationTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																predictionErrorInitializationLabel)
														.addComponent(
																predictionErrorInitializationTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																epsilon0Label)
														.addComponent(
																epsilon0TextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																alphaLabel)
														.addComponent(
																alphaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(nuLabel)
														.addComponent(
																nuTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																gammaLabel)
														.addComponent(
																gammaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												fitnessAndPredictionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(betaLabel)
														.addComponent(
																betaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addGap(26, 26, 26)));

		classifierSubsumptionAndDeletionPanel
				.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
						"Classifier subsumption and deletion",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		thetaSubsumerLabel.setFont(new java.awt.Font("Arial", 0, 12));
		thetaSubsumerLabel
				.setText("<html>Subsumption threshold <i>&theta;<sub>sub</sub></i></html>");
		thetaSubsumerLabel
				.setToolTipText("The experience of a classifier required to be a subsumer");

		thetaSubsumerTextField.setText("20.0");
		thetaSubsumerTextField
				.setToolTipText("The experience of a classifier required to be a subsumer");

		deltaLabel.setFont(new java.awt.Font("Arial", 0, 12));
		deltaLabel.setText("<html>Fraction mean fitness <i>&delta;</i></html>");
		deltaLabel
				.setToolTipText("The fraction of the mean fitness of the population below which the fitness of a classifier may be considered in its vote for deletion");

		thetaDelLabel.setFont(new java.awt.Font("Arial", 0, 12));
		thetaDelLabel
				.setText("<html>Deletion threshold <i>&theta;<sub>del</sub></i></html>");
		thetaDelLabel
				.setToolTipText("Specified the threshold (experience!) over which the fitness of a classifier may be considered in its deletion probability");

		deltaTextField.setText("0.1");
		deltaTextField
				.setToolTipText("The fraction of the mean fitness of the population below which the fitness of a classifier may be considered in its vote for deletion");

		thetaDelTextField.setText("20.0");
		thetaDelTextField
				.setToolTipText("Specified the threshold (experience!) over which the fitness of a classifier may be considered in its deletion probability.");

		maxPopSizeLabel.setFont(new java.awt.Font("Arial", 0, 12));
		maxPopSizeLabel.setText("<html>Max population <i>N</i> </html>");

		maxPopSizeTextField.setText("512");

		doActionSetSubsumptionCheckBox
				.setFont(new java.awt.Font("Arial", 0, 12));
		doActionSetSubsumptionCheckBox.setSelected(true);
		doActionSetSubsumptionCheckBox.setText("Action set subsumption");

		coveringWildcardProbabilityLabel.setFont(new java.awt.Font("Arial", 0,
				12));
		coveringWildcardProbabilityLabel
				.setText("<html>Covering # probability <i>P<sub>#</sub></i></html>");
		coveringWildcardProbabilityLabel
				.setToolTipText("The probability of using a don't care symbol in an allele when covering");

		coveringWildcardProbabilityTextField.setText("0.5");
		coveringWildcardProbabilityTextField
				.setToolTipText("The probability of using a don't care symbol in an allele when covering");

		randomStartCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
		randomStartCheckBox.setSelected(true);
		randomStartCheckBox.setText("Random start");
		randomStartCheckBox.setActionCommand("Do GA Subsumption");

		tournamentProbabilityLabel.setFont(new java.awt.Font("Arial", 0, 12));
		tournamentProbabilityLabel.setText("Tournament probability");
		tournamentProbabilityLabel
				.setToolTipText("The probability of using a don't care symbol in an allele when covering");

		tournamentProbabilityTextField.setText("0.84");
		tournamentProbabilityTextField
				.setToolTipText("The probability of using a don't care symbol in an allele when covering");

		final javax.swing.GroupLayout classifierSubsumptionAndDeletionPanelLayout = new javax.swing.GroupLayout(
				classifierSubsumptionAndDeletionPanel);
		classifierSubsumptionAndDeletionPanel
				.setLayout(classifierSubsumptionAndDeletionPanelLayout);
		classifierSubsumptionAndDeletionPanelLayout
				.setHorizontalGroup(classifierSubsumptionAndDeletionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								classifierSubsumptionAndDeletionPanelLayout
										.createSequentialGroup()
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																thetaSubsumerLabel)
														.addComponent(
																deltaLabel)
														.addComponent(
																thetaDelLabel)
														.addComponent(
																maxPopSizeLabel)
														.addComponent(
																coveringWildcardProbabilityLabel)
														.addComponent(
																tournamentProbabilityLabel))
										.addGap(24, 24, 24)
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																thetaDelTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																thetaSubsumerTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																deltaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																maxPopSizeTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																coveringWildcardProbabilityTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)
														.addComponent(
																tournamentProbabilityTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																40,
																javax.swing.GroupLayout.PREFERRED_SIZE)))
						.addComponent(doActionSetSubsumptionCheckBox)
						.addComponent(randomStartCheckBox));
		classifierSubsumptionAndDeletionPanelLayout
				.setVerticalGroup(classifierSubsumptionAndDeletionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								classifierSubsumptionAndDeletionPanelLayout
										.createSequentialGroup()
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																maxPopSizeLabel)
														.addComponent(
																maxPopSizeTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																deltaLabel)
														.addComponent(
																deltaTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																thetaDelLabel)
														.addComponent(
																thetaDelTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																thetaSubsumerLabel)
														.addComponent(
																thetaSubsumerTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																coveringWildcardProbabilityLabel)
														.addComponent(
																coveringWildcardProbabilityTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												classifierSubsumptionAndDeletionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																tournamentProbabilityLabel)
														.addComponent(
																tournamentProbabilityTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												doActionSetSubsumptionCheckBox)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(randomStartCheckBox)
										.addGap(23, 23, 23)));

		rewardModelPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Reward model",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		maxStackSizeLabel.setFont(new java.awt.Font("Arial", 0, 12));
		maxStackSizeLabel.setText("Stack size");

		maxStackSizeTextField.setText("8");

		useQuadraticRewardCheckBox.setFont(new java.awt.Font("Arial", 0, 12));
		useQuadraticRewardCheckBox.setSelected(true);
		useQuadraticRewardCheckBox.setText("Use quadratic reward");

		final javax.swing.GroupLayout rewardModelPanelLayout = new javax.swing.GroupLayout(
				rewardModelPanel);
		rewardModelPanel.setLayout(rewardModelPanelLayout);
		rewardModelPanelLayout
				.setHorizontalGroup(rewardModelPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								rewardModelPanelLayout
										.createSequentialGroup()
										.addGroup(
												rewardModelPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																rewardModelPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				maxStackSizeLabel)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				maxStackSizeTextField,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				41,
																				javax.swing.GroupLayout.PREFERRED_SIZE))
														.addComponent(
																useQuadraticRewardCheckBox))
										.addContainerGap(77, Short.MAX_VALUE)));
		rewardModelPanelLayout
				.setVerticalGroup(rewardModelPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								rewardModelPanelLayout
										.createSequentialGroup()
										.addGroup(
												rewardModelPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																maxStackSizeLabel)
														.addComponent(
																maxStackSizeTextField,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												useQuadraticRewardCheckBox)
										.addGap(7, 7, 7)));

		final javax.swing.GroupLayout lcsParametersPanelLayout = new javax.swing.GroupLayout(
				lcsParametersPanel);
		lcsParametersPanel.setLayout(lcsParametersPanelLayout);
		lcsParametersPanelLayout
				.setHorizontalGroup(lcsParametersPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								lcsParametersPanelLayout
										.createSequentialGroup()
										.addGroup(
												lcsParametersPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(
																rewardModelPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																gaParametersPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																fitnessAndPredictionPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																0, 232,
																Short.MAX_VALUE)
														.addComponent(
																classifierSubsumptionAndDeletionPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));
		lcsParametersPanelLayout
				.setVerticalGroup(lcsParametersPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								lcsParametersPanelLayout
										.createSequentialGroup()
										.addComponent(
												classifierSubsumptionAndDeletionPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												228,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												fitnessAndPredictionPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												229,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												gaParametersPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												181,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												rewardModelPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		agentTypePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(
				null, "Agent type",
				javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
				javax.swing.border.TitledBorder.DEFAULT_POSITION,
				new java.awt.Font("Tahoma", 1, 11))); // NOI18N
		agentTypePanel.setPreferredSize(new java.awt.Dimension(300, 1153));

		agentAlgorithmPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Algorithm",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N
		agentAlgorithmPanel.setPreferredSize(new java.awt.Dimension(400, 187));

		agentTypeButtonGroup.add(randomizedMovementRadioButton);
		randomizedMovementRadioButton
				.setFont(new java.awt.Font("Arial", 0, 12));
		randomizedMovementRadioButton.setLabel("Randomized");
		randomizedMovementRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						randomizedMovementRadioButtonActionPerformed(evt);
					}
				});

		agentTypeButtonGroup.add(staticAIAgentRadioButton);
		staticAIAgentRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		staticAIAgentRadioButton.setText("Static heuristic");
		staticAIAgentRadioButton
				.setToolTipText("Randomized movement, but move to goal agent when in sight");
		staticAIAgentRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						staticAIAgentRadioButtonActionPerformed(evt);
					}
				});

		agentTypeButtonGroup.add(DSXCSAgentRadioButton);
		DSXCSAgentRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		DSXCSAgentRadioButton.setText("Delayed SXCS");
		DSXCSAgentRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						DSXCSAgentRadioButtonActionPerformed(evt);
					}
				});

		agentTypeButtonGroup.add(SXCSAgentRadioButton);
		SXCSAgentRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		SXCSAgentRadioButton.setSelected(true);
		SXCSAgentRadioButton.setText("Surveillance XCS");
		SXCSAgentRadioButton
				.setToolTipText("LCS Agent with special reward function");
		SXCSAgentRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						SXCSAgentRadioButtonActionPerformed(evt);
					}
				});

		agentTypeButtonGroup.add(standardXCSAgentRadioButton);
		standardXCSAgentRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		standardXCSAgentRadioButton.setText("Standard XCS");
		standardXCSAgentRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						standardXCSAgentRadioButtonActionPerformed(evt);
					}
				});

		agentTypeButtonGroup.add(eventXCSAgentRadioButton);
		eventXCSAgentRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		eventXCSAgentRadioButton.setText("XCS (w\\ events)");
		eventXCSAgentRadioButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						eventXCSAgentRadioButtonActionPerformed(evt);
					}
				});

		final javax.swing.GroupLayout agentAlgorithmPanelLayout = new javax.swing.GroupLayout(
				agentAlgorithmPanel);
		agentAlgorithmPanel.setLayout(agentAlgorithmPanelLayout);
		agentAlgorithmPanelLayout
				.setHorizontalGroup(agentAlgorithmPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								agentAlgorithmPanelLayout
										.createSequentialGroup()
										.addGroup(
												agentAlgorithmPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																randomizedMovementRadioButton)
														.addComponent(
																staticAIAgentRadioButton)
														.addComponent(
																standardXCSAgentRadioButton)
														.addComponent(
																eventXCSAgentRadioButton)
														.addComponent(
																SXCSAgentRadioButton)
														.addComponent(
																DSXCSAgentRadioButton))
										.addContainerGap(74, Short.MAX_VALUE)));
		agentAlgorithmPanelLayout
				.setVerticalGroup(agentAlgorithmPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								agentAlgorithmPanelLayout
										.createSequentialGroup()
										.addComponent(
												randomizedMovementRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(staticAIAgentRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												standardXCSAgentRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(eventXCSAgentRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(SXCSAgentRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(DSXCSAgentRadioButton)));

		communicationPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Communication",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		externalRewardButtonGroup.add(noExternalRewardRadioButton);
		noExternalRewardRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		noExternalRewardRadioButton.setSelected(true);
		noExternalRewardRadioButton.setText("No external reward");

		externalRewardButtonGroup.add(rewardAllEquallyRadioButton);
		rewardAllEquallyRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		rewardAllEquallyRadioButton.setText("Reward all equally");
		rewardAllEquallyRadioButton.setActionCommand("all equally");

		externalRewardButtonGroup.add(rewardEgoisticRadioButton);
		rewardEgoisticRadioButton.setFont(new java.awt.Font("Arial", 0, 12));
		rewardEgoisticRadioButton.setText("Egoistic relation");

		final javax.swing.GroupLayout communicationPanelLayout = new javax.swing.GroupLayout(
				communicationPanel);
		communicationPanel.setLayout(communicationPanelLayout);
		communicationPanelLayout
				.setHorizontalGroup(communicationPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								communicationPanelLayout
										.createSequentialGroup()
										.addGroup(
												communicationPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																noExternalRewardRadioButton)
														.addComponent(
																rewardAllEquallyRadioButton)
														.addComponent(
																rewardEgoisticRadioButton))
										.addContainerGap(82, Short.MAX_VALUE)));
		communicationPanelLayout
				.setVerticalGroup(communicationPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								communicationPanelLayout
										.createSequentialGroup()
										.addComponent(
												noExternalRewardRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												rewardAllEquallyRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(rewardEgoisticRadioButton)));

		explorationModePanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Exploration Mode",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N
		explorationModePanel.setPreferredSize(new java.awt.Dimension(400, 371));

		exploreExploitPhasePanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Explore/Exploit phase"));

		exploreExploitPhaseButtonGroup.add(randomRouletteSelectionRadioButton);
		randomRouletteSelectionRadioButton.setSelected(true);
		randomRouletteSelectionRadioButton.setText("Random/Roulette selection");

		exploreExploitPhaseButtonGroup
				.add(randomTournamentSelectionRadioButton);
		randomTournamentSelectionRadioButton
				.setText("Random/Tournament selection");

		exploreExploitPhaseButtonGroup.add(randomBestSelectionRadioButton);
		randomBestSelectionRadioButton.setText("Random/Best selection");

		exploreExploitPhaseButtonGroup
				.add(rouletteTournamentSelectionRadioButton);
		rouletteTournamentSelectionRadioButton
				.setText("Roulette/Tournament selection");

		exploreExploitPhaseButtonGroup.add(rouletteBestSelectionRadioButton);
		rouletteBestSelectionRadioButton.setText("Roulette/Best selection");

		exploreExploitPhaseButtonGroup.add(tournamentBestSelectionRadioButton);
		tournamentBestSelectionRadioButton.setText("Tournament/Best selection");

		final javax.swing.GroupLayout exploreExploitPhasePanelLayout = new javax.swing.GroupLayout(
				exploreExploitPhasePanel);
		exploreExploitPhasePanel.setLayout(exploreExploitPhasePanelLayout);
		exploreExploitPhasePanelLayout
				.setHorizontalGroup(exploreExploitPhasePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addComponent(randomRouletteSelectionRadioButton)
						.addComponent(randomTournamentSelectionRadioButton)
						.addComponent(randomBestSelectionRadioButton)
						.addComponent(rouletteTournamentSelectionRadioButton)
						.addComponent(rouletteBestSelectionRadioButton)
						.addComponent(tournamentBestSelectionRadioButton));
		exploreExploitPhasePanelLayout
				.setVerticalGroup(exploreExploitPhasePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								exploreExploitPhasePanelLayout
										.createSequentialGroup()
										.addComponent(
												randomRouletteSelectionRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												randomTournamentSelectionRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												randomBestSelectionRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												rouletteTournamentSelectionRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												rouletteBestSelectionRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												tournamentBestSelectionRadioButton)));

		switchExploreExploitPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Switch explore/exploit"));

		switchExploreExploitButtonGroup.add(switchGoalObsRadioButton);
		switchGoalObsRadioButton.setText("Goal is very near");

		switchExploreExploitButtonGroup.add(switchGoalSightRadioButton);
		switchGoalSightRadioButton.setText("Goal is near");

		switchExploreExploitButtonGroup.add(switchRewardRadioButton);
		switchRewardRadioButton.setText("Positive reward");

		switchExploreExploitButtonGroup.add(switchNoRadioButton);
		switchNoRadioButton.setSelected(true);
		switchNoRadioButton.setText("No switch (always exploit)");

		final javax.swing.GroupLayout switchExploreExploitPanelLayout = new javax.swing.GroupLayout(
				switchExploreExploitPanel);
		switchExploreExploitPanel.setLayout(switchExploreExploitPanelLayout);
		switchExploreExploitPanelLayout
				.setHorizontalGroup(switchExploreExploitPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								switchExploreExploitPanelLayout
										.createSequentialGroup()
										.addGroup(
												switchExploreExploitPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																switchGoalObsRadioButton)
														.addComponent(
																switchGoalSightRadioButton)
														.addComponent(
																switchRewardRadioButton)
														.addComponent(
																switchNoRadioButton))
										.addContainerGap(22, Short.MAX_VALUE)));
		switchExploreExploitPanelLayout
				.setVerticalGroup(switchExploreExploitPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								switchExploreExploitPanelLayout
										.createSequentialGroup()
										.addComponent(switchGoalObsRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												switchGoalSightRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(switchRewardRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(switchNoRadioButton)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		final javax.swing.GroupLayout explorationModePanelLayout = new javax.swing.GroupLayout(
				explorationModePanel);
		explorationModePanel.setLayout(explorationModePanelLayout);
		explorationModePanelLayout
				.setHorizontalGroup(explorationModePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								explorationModePanelLayout
										.createSequentialGroup()
										.addGroup(
												explorationModePanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(
																switchExploreExploitPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(
																exploreExploitPhasePanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addContainerGap()));
		explorationModePanelLayout
				.setVerticalGroup(explorationModePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								explorationModePanelLayout
										.createSequentialGroup()
										.addComponent(
												exploreExploitPhasePanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												switchExploreExploitPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		baseRewardFunctionPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Base reward function",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		baseRewardFunctionButtonGroup.add(goalObsRadioButton);
		goalObsRadioButton.setSelected(true);
		goalObsRadioButton.setText("Goal <= Observation range");

		baseRewardFunctionButtonGroup.add(goalSightRadioButton);
		goalSightRadioButton.setText("Goal <= Sight range");

		baseRewardFunctionButtonGroup.add(goalObsAgentsObsRadioButton);
		goalObsAgentsObsRadioButton.setText("Goal <= Obs, Agents > Obs");

		baseRewardFunctionButtonGroup.add(goalObsAgentsSightRadioButton);
		goalObsAgentsSightRadioButton.setText("Goal <= Obs, Agents > Sight");

		baseRewardFunctionButtonGroup.add(goalSightAgentsObsRadioButton);
		goalSightAgentsObsRadioButton.setText("Goal <= Sight, Agets > Obs");

		baseRewardFunctionButtonGroup.add(goalSightAgentsSightRadioButton);
		goalSightAgentsSightRadioButton
				.setText("Goal <= Sight, Agents > Sight");

		final javax.swing.GroupLayout baseRewardFunctionPanelLayout = new javax.swing.GroupLayout(
				baseRewardFunctionPanel);
		baseRewardFunctionPanel.setLayout(baseRewardFunctionPanelLayout);
		baseRewardFunctionPanelLayout
				.setHorizontalGroup(baseRewardFunctionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								baseRewardFunctionPanelLayout
										.createSequentialGroup()
										.addGroup(
												baseRewardFunctionPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																goalSightAgentsSightRadioButton)
														.addComponent(
																goalSightAgentsObsRadioButton)
														.addComponent(
																goalObsAgentsSightRadioButton)
														.addComponent(
																goalObsAgentsObsRadioButton)
														.addComponent(
																goalObsRadioButton)
														.addComponent(
																goalSightRadioButton))
										.addContainerGap(22, Short.MAX_VALUE)));
		baseRewardFunctionPanelLayout
				.setVerticalGroup(baseRewardFunctionPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								baseRewardFunctionPanelLayout
										.createSequentialGroup()
										.addComponent(goalObsRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(goalSightRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												goalObsAgentsObsRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												goalObsAgentsSightRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												goalSightAgentsObsRadioButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												goalSightAgentsSightRadioButton)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		final javax.swing.GroupLayout agentTypePanelLayout = new javax.swing.GroupLayout(
				agentTypePanel);
		agentTypePanel.setLayout(agentTypePanelLayout);
		agentTypePanelLayout
				.setHorizontalGroup(agentTypePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								javax.swing.GroupLayout.Alignment.TRAILING,
								agentTypePanelLayout
										.createSequentialGroup()
										.addGroup(
												agentTypePanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING)
														.addComponent(
																communicationPanel,
																javax.swing.GroupLayout.Alignment.LEADING,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addGroup(
																javax.swing.GroupLayout.Alignment.LEADING,
																agentTypePanelLayout
																		.createSequentialGroup()
																		.addContainerGap()
																		.addGroup(
																				agentTypePanelLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.TRAILING,
																								false)
																						.addComponent(
																								explorationModePanel,
																								javax.swing.GroupLayout.Alignment.LEADING,
																								0,
																								205,
																								Short.MAX_VALUE)
																						.addComponent(
																								agentAlgorithmPanel,
																								javax.swing.GroupLayout.Alignment.LEADING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								205,
																								Short.MAX_VALUE)
																						.addComponent(
																								baseRewardFunctionPanel,
																								javax.swing.GroupLayout.Alignment.LEADING,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								javax.swing.GroupLayout.DEFAULT_SIZE,
																								Short.MAX_VALUE))))
										.addContainerGap()));
		agentTypePanelLayout
				.setVerticalGroup(agentTypePanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								agentTypePanelLayout
										.createSequentialGroup()
										.addComponent(
												agentAlgorithmPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												164,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												baseRewardFunctionPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												explorationModePanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												332,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addGap(203, 203, 203)
										.addComponent(
												communicationPanel,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												javax.swing.GroupLayout.DEFAULT_SIZE,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		automaticTestsPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder("Test all ..."));

		saveSpeedButton.setText("Speed values");
		saveSpeedButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				saveSpeedButtonActionPerformed(evt);
			}
		});

		saveAllRandomButton.setText("Random scenarios");
		saveAllRandomButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						saveAllRandomButtonActionPerformed(evt);
					}
				});

		saveAllExplorationButton.setText("Exploration modes");
		saveAllExplorationButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						saveAllExplorationButtonActionPerformed(evt);
					}
				});

		saveAllStackButton.setText("Stack sizes");
		saveAllStackButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						saveAllStackButtonActionPerformed(evt);
					}
				});

		saveAllPopulationButton.setText("Max populations");
		saveAllPopulationButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						saveAllPopulationButtonActionPerformed(evt);
					}
				});

		saveStepsButton.setText("Step count");
		saveStepsButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				saveStepsButtonActionPerformed(evt);
			}
		});

		saveRandomButton.setText("Random seeds");
		saveRandomButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				saveRandomButtonActionPerformed(evt);
			}
		});

		saveTournamentButton.setText("Tournament probabilities");
		saveTournamentButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						saveTournamentButtonActionPerformed(evt);
					}
				});

		saveLearningButton.setText("Learning rates");
		saveLearningButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						saveLearningButtonActionPerformed(evt);
					}
				});

		savePredictionDiscountButton.setText("Prediction discounts");
		savePredictionDiscountButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						savePredictionDiscountButtonActionPerformed(evt);
					}
				});

		saveAllExplorationButton1.setText("Reward functions");
		saveAllExplorationButton1
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						saveAllRewardFunctionsButtonActionPerformed(evt);
					}
				});

		final javax.swing.GroupLayout automaticTestsPanelLayout = new javax.swing.GroupLayout(
				automaticTestsPanel);
		automaticTestsPanel.setLayout(automaticTestsPanelLayout);
		automaticTestsPanelLayout
				.setHorizontalGroup(automaticTestsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								automaticTestsPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												automaticTestsPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addComponent(
																saveAllRandomButton)
														.addComponent(
																saveAllExplorationButton)
														.addComponent(
																saveAllStackButton)
														.addComponent(
																saveAllPopulationButton)
														.addComponent(
																saveStepsButton)
														.addComponent(
																saveRandomButton)
														.addComponent(
																saveTournamentButton)
														.addComponent(
																saveLearningButton)
														.addComponent(
																savePredictionDiscountButton)
														.addComponent(
																saveSpeedButton)
														.addComponent(
																saveAllExplorationButton1))
										.addContainerGap(14, Short.MAX_VALUE)));

		automaticTestsPanelLayout.linkSize(
				javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { saveAllExplorationButton,
						saveAllExplorationButton1, saveAllPopulationButton,
						saveAllRandomButton, saveAllStackButton,
						saveLearningButton, savePredictionDiscountButton,
						saveRandomButton, saveSpeedButton, saveStepsButton,
						saveTournamentButton });

		automaticTestsPanelLayout
				.setVerticalGroup(automaticTestsPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								automaticTestsPanelLayout
										.createSequentialGroup()
										.addComponent(saveSpeedButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveAllRandomButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveAllExplorationButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveAllExplorationButton1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveAllStackButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveAllPopulationButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveStepsButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveRandomButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveTournamentButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(saveLearningButton)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												savePredictionDiscountButton)
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		inputOutputPanel.setBorder(javax.swing.BorderFactory
				.createTitledBorder(null, "Input/Output",
						javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
						javax.swing.border.TitledBorder.DEFAULT_POSITION,
						new java.awt.Font("Tahoma", 1, 11))); // NOI18N

		logOutputCheckBox.setText("Log output");

		createAnimatedGIFCheckBox.setText("Create GIF");

		saveNewButton.setFont(new java.awt.Font("Tahoma", 1, 11));
		saveNewButton.setText("Save");
		saveNewButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				saveNewButtonActionPerformed(evt);
			}
		});

		packageButton.setFont(new java.awt.Font("Tahoma", 1, 11));
		packageButton.setText("Package");
		packageButton.setEnabled(false);
		packageButton.addActionListener(new java.awt.event.ActionListener() {
			@Override
			public void actionPerformed(final java.awt.event.ActionEvent evt) {
				packageButtonActionPerformed(evt);
			}
		});

		updateDatabaseButton.setText("Update");
		updateDatabaseButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						updateDatabaseButtonActionPerformed(evt);
					}
				});

		runLastBatchButton.setText("Run batch");
		runLastBatchButton.setEnabled(false);
		runLastBatchButton
				.addActionListener(new java.awt.event.ActionListener() {
					@Override
					public void actionPerformed(
							final java.awt.event.ActionEvent evt) {
						runLastBatchButtonActionPerformed(evt);
					}
				});

		final javax.swing.GroupLayout inputOutputPanelLayout = new javax.swing.GroupLayout(
				inputOutputPanel);
		inputOutputPanel.setLayout(inputOutputPanelLayout);
		inputOutputPanelLayout
				.setHorizontalGroup(inputOutputPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								inputOutputPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												inputOutputPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																inputOutputPanelLayout
																		.createParallelGroup(
																				javax.swing.GroupLayout.Alignment.LEADING)
																		.addGroup(
																				inputOutputPanelLayout
																						.createSequentialGroup()
																						.addComponent(
																								logOutputCheckBox)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																						.addComponent(
																								createAnimatedGIFCheckBox))
																		.addGroup(
																				inputOutputPanelLayout
																						.createSequentialGroup()
																						.addComponent(
																								saveNewButton,
																								javax.swing.GroupLayout.PREFERRED_SIZE,
																								95,
																								javax.swing.GroupLayout.PREFERRED_SIZE)
																						.addPreferredGap(
																								javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																						.addComponent(
																								packageButton)))
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																inputOutputPanelLayout
																		.createSequentialGroup()
																		.addComponent(
																				updateDatabaseButton,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				95,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				runLastBatchButton)))
										.addContainerGap(666, Short.MAX_VALUE)));

		inputOutputPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL,
				new java.awt.Component[] { packageButton, runLastBatchButton,
						saveNewButton, updateDatabaseButton });

		inputOutputPanelLayout
				.setVerticalGroup(inputOutputPanelLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								inputOutputPanelLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												inputOutputPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																logOutputCheckBox)
														.addComponent(
																createAnimatedGIFCheckBox))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												inputOutputPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																saveNewButton)
														.addComponent(
																packageButton))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												inputOutputPanelLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.BASELINE)
														.addComponent(
																updateDatabaseButton)
														.addComponent(
																runLastBatchButton,
																javax.swing.GroupLayout.PREFERRED_SIZE,
																23,
																javax.swing.GroupLayout.PREFERRED_SIZE))
										.addContainerGap(
												javax.swing.GroupLayout.DEFAULT_SIZE,
												Short.MAX_VALUE)));

		configurationNameLabel.setText("jLabel1");

		resultsTable.setModel(results);
		resultsScrollPane.setViewportView(resultsTable);

		final javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING,
												false)
												.addComponent(
														problemDefinitionPanel,
														0, 189, Short.MAX_VALUE)
												.addComponent(
														automaticTestsPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(lcsParametersPanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										javax.swing.GroupLayout.DEFAULT_SIZE,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addGap(9, 9, 9)
								.addComponent(agentTypePanel,
										javax.swing.GroupLayout.PREFERRED_SIZE,
										245,
										javax.swing.GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(
										javax.swing.LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														resultsScrollPane,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														297,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addComponent(
														configurationNameLabel)
												.addComponent(
														inputOutputPanel,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
								.addContainerGap()));
		layout.setVerticalGroup(layout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGroup(
						layout.createSequentialGroup()
								.addGroup(
										layout.createParallelGroup(
												javax.swing.GroupLayout.Alignment.LEADING)
												.addComponent(
														lcsParametersPanel,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														javax.swing.GroupLayout.DEFAULT_SIZE,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGroup(
														layout.createSequentialGroup()
																.addComponent(
																		problemDefinitionPanel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		598,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		automaticTestsPanel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE))
												.addComponent(
														agentTypePanel,
														javax.swing.GroupLayout.PREFERRED_SIZE,
														707,
														javax.swing.GroupLayout.PREFERRED_SIZE)
												.addGroup(
														layout.createSequentialGroup()
																.addContainerGap()
																.addComponent(
																		resultsScrollPane,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		218,
																		javax.swing.GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(
																		configurationNameLabel)
																.addPreferredGap(
																		javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(
																		inputOutputPanel,
																		javax.swing.GroupLayout.PREFERRED_SIZE,
																		javax.swing.GroupLayout.DEFAULT_SIZE,
																		javax.swing.GroupLayout.PREFERRED_SIZE)))
								.addContainerGap(
										javax.swing.GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)));

		pack();
	}// </editor-fold>//GEN-END:initComponents

	private void doEvolutionaryAlgorithmCheckBoxActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_doEvolutionaryAlgorithmCheckBoxActionPerformed
		final boolean do_activate = doEvolutionaryAlgorithmCheckBox
				.isSelected();

		doGASubsumptionCheckBox.setEnabled(do_activate);

		thetaLabel.setEnabled(do_activate);
		thetaTextField.setEnabled(do_activate);

		mutationProbabilityLabel.setEnabled(do_activate);
		mutationProbabilityTextField.setEnabled(do_activate);

		predictionErrorReductionLabel.setEnabled(do_activate);
		predictionErrorReductionTextField.setEnabled(do_activate);

		fitnessReductionLabel.setEnabled(do_activate);
		fitnessReductionTextField.setEnabled(do_activate);
	}// GEN-LAST:event_doEvolutionaryAlgorithmCheckBoxActionPerformed

	private void activateRandomGridParameters(final boolean activate) {
		obstaclePercentageLabel.setEnabled(activate);
		obstaclePercentageTextField.setEnabled(activate);
		obstacleConnectionFactorLabel.setEnabled(activate);
		obstacleConnectionFactorTextField.setEnabled(activate);
	}

	private void updateDatabaseButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_updateDatabaseButtonActionPerformed
		loadResultsIntoDatabase();
	}// GEN-LAST:event_updateDatabaseButtonActionPerformed

	private void randomizedMovementRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_randomizedMovementRadioButtonActionPerformed
		activateLCSControls(!randomizedMovementRadioButton.isSelected());
	}// GEN-LAST:event_randomizedMovementRadioButtonActionPerformed

	private void staticAIAgentRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_staticAIAgentRadioButtonActionPerformed
		activateLCSControls(!staticAIAgentRadioButton.isSelected());
	}// GEN-LAST:event_staticAIAgentRadioButtonActionPerformed

	private void DSXCSAgentRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_DSXCSAgentRadioButtonActionPerformed
		activateLCSControls(DSXCSAgentRadioButton.isSelected());
	}// GEN-LAST:event_DSXCSAgentRadioButtonActionPerformed

	private void SXCSAgentRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_SXCSAgentRadioButtonActionPerformed
		activateLCSControls(SXCSAgentRadioButton.isSelected());
	}// GEN-LAST:event_SXCSAgentRadioButtonActionPerformed

	private void runLastBatchButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_runLastBatchButtonActionPerformed
		runLastBatchButton.setEnabled(false);
		try {
			final Runtime rt = Runtime.getRuntime();
			final String cur_dir = System.getProperty("user.dir");
			final File work_dir = new File(cur_dir + "\\" + last_directory);
			final Process pr = rt.exec("cmd.exe /c " + last_batch_file, null,
					work_dir);
			final BufferedReader error = new BufferedReader(
					new InputStreamReader(pr.getInputStream()));
			String line = null;
			while ((line = error.readLine()) != null) {
				System.out.println(line);
			}
			final int exitVal = pr.waitFor();
			System.out.println("Exited with error code " + exitVal);
		} catch (final Exception e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(this, "Error calling "
					+ last_batch_file + " (" + e + ")",
					"Error calling batch file", JOptionPane.ERROR_MESSAGE);
		}
	}// GEN-LAST:event_runLastBatchButtonActionPerformed

	private void packageButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_packageButtonActionPerformed
		createAllPlotFile();
		try {
			final String last_date = joschka.run();
			last_directory = "agent-" + last_date;
			last_batch_file = "batch-agent-" + last_date + ".bat";
		} catch (final Exception e) {
			JOptionPane.showMessageDialog(this, "Error packaging files: " + e,
					"Error packaging files", JOptionPane.ERROR_MESSAGE);
			return;
		}

		config_strings.clear();
		resetTimeString();

		packageButton.setEnabled(false);
		runLastBatchButton.setEnabled(true);
	}// GEN-LAST:event_packageButtonActionPerformed

	private void saveNewButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveNewButtonActionPerformed
		this.saveSettings("default.txt");
		final String id = new String(timeString + "-" + conf_id);
		this.saveSettings("config-" + id + ".txt");
		config_strings.add(id);
		conf_id++;

		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveNewButtonActionPerformed

	private void standardXCSAgentRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_standardXCSAgentRadioButtonActionPerformed
		activateLCSControls(standardXCSAgentRadioButton.isSelected());
	}// GEN-LAST:event_standardXCSAgentRadioButtonActionPerformed

	private void randomScenarioRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_randomScenarioRadioButtonActionPerformed
		activateRandomGridParameters(randomScenarioRadioButton.isSelected());
	}// GEN-LAST:event_randomScenarioRadioButtonActionPerformed

	private void pillarScenarioRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_pillarScenarioRadioButtonActionPerformed
		activateRandomGridParameters(!pillarScenarioRadioButton.isSelected());
	}// GEN-LAST:event_pillarScenarioRadioButtonActionPerformed

	private void saveScenario() {
		final String id = new String(timeString + "-" + conf_id);
		this.saveSettings("config-" + id + ".txt");
		config_strings.add(id);
		conf_id++;
	}

	private void saveAllRandomButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAllRandomButtonActionPerformed
		this.saveSettings("default.txt");
		final String orig_obstacle_percentage = obstaclePercentageTextField
				.getText();
		final String orig_obstacle_con = obstacleConnectionFactorTextField
				.getText();
		for (int k = 0; k < 3; k++) {
			switch (k) {
			case 0: {
				scenarioTypeButtonGroup.setSelected(
						randomScenarioRadioButton.getModel(), true);
				for (int i = 0; i < 5; i++) {
					switch (i) {
					case 0:
						obstaclePercentageTextField.setText("0.0");
						break;
					case 1:
						obstaclePercentageTextField.setText("0.05");
						break;
					case 2:
						obstaclePercentageTextField.setText("0.1");
						break;
					case 3:
						obstaclePercentageTextField.setText("0.2");
						break;
					case 4:
						obstaclePercentageTextField.setText("0.4");
						break;
					}
					for (int j = 0; j < 3; j++) {
						switch (j) {
						case 0:
							obstacleConnectionFactorTextField.setText("0.01");
							break;
						case 1:
							obstacleConnectionFactorTextField.setText("0.5");
							break;
						case 2:
							obstacleConnectionFactorTextField.setText("0.99");
							break;
						}
						saveScenario();
						if (i == 0) {
							break;
						}
					}
				}
			}
				break;
			case 1: {
				scenarioTypeButtonGroup.setSelected(
						pillarScenarioRadioButton.getModel(), true);
				saveScenario();
			}
				break;
			case 2: {
				scenarioTypeButtonGroup.setSelected(
						difficultScenarioRadioButton.getModel(), true);
				saveScenario();
			}
				break;
			}
		}
		obstaclePercentageTextField.setText(orig_obstacle_percentage);
		obstacleConnectionFactorTextField.setText(orig_obstacle_con);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveAllRandomButtonActionPerformed

	private void saveAllStackButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAllStackButtonActionPerformed
		this.saveSettings("default.txt");
		final String orig_stack = maxStackSizeTextField.getText();
		for (int i = 0; i < 10; i++) {
			switch (i) {
			case 0:
				maxStackSizeTextField.setText("2");
				break;
			case 1:
				maxStackSizeTextField.setText("4");
				break;
			case 2:
				maxStackSizeTextField.setText("8");
				break;
			case 3:
				maxStackSizeTextField.setText("16");
				break;
			case 4:
				maxStackSizeTextField.setText("32");
				break;
			case 5:
				maxStackSizeTextField.setText("64");
				break;
			case 6:
				maxStackSizeTextField.setText("128");
				break;
			case 7:
				maxStackSizeTextField.setText("256");
				break;
			case 8:
				maxStackSizeTextField.setText("512");
				break;
			case 9:
				maxStackSizeTextField.setText("1024");
				break;
			}
			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}
		maxStackSizeTextField.setText(orig_stack);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveAllStackButtonActionPerformed

	private void saveAllExplorationButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAllExplorationButtonActionPerformed
		this.saveSettings("default.txt");
		final ButtonModel old_model1 = exploreExploitPhaseButtonGroup
				.getSelection();
		final ButtonModel old_model2 = switchExploreExploitButtonGroup
				.getSelection();

		for (int i = 0; i < 6; i++) {
			switch (i) {
			case 0:
				exploreExploitPhaseButtonGroup.setSelected(
						randomRouletteSelectionRadioButton.getModel(), true);
				break;
			case 1:
				exploreExploitPhaseButtonGroup.setSelected(
						randomTournamentSelectionRadioButton.getModel(), true);
				break;
			case 2:
				exploreExploitPhaseButtonGroup.setSelected(
						randomBestSelectionRadioButton.getModel(), true);
				break;
			case 3:
				exploreExploitPhaseButtonGroup
						.setSelected(rouletteTournamentSelectionRadioButton
								.getModel(), true);
				break;
			case 4:
				exploreExploitPhaseButtonGroup.setSelected(
						rouletteBestSelectionRadioButton.getModel(), true);
				break;
			case 5:
				exploreExploitPhaseButtonGroup.setSelected(
						tournamentBestSelectionRadioButton.getModel(), true);
				break;
			}

			for (int j = 0; j < 4; j++) {
				switch (j) {
				case 0:
					switchExploreExploitButtonGroup.setSelected(
							switchGoalObsRadioButton.getModel(), true);
					break;
				case 1:
					switchExploreExploitButtonGroup.setSelected(
							switchGoalSightRadioButton.getModel(), true);
					break;
				case 2:
					switchExploreExploitButtonGroup.setSelected(
							switchRewardRadioButton.getModel(), true);
					break;
				case 3:
					switchExploreExploitButtonGroup.setSelected(
							switchNoRadioButton.getModel(), true);
					break;
				}
				final String id = new String(timeString + "-" + conf_id);
				this.saveSettings("config-" + id + ".txt");
				config_strings.add(id);
				conf_id++;
			}
		}
		exploreExploitPhaseButtonGroup.setSelected(old_model1, true);
		switchExploreExploitButtonGroup.setSelected(old_model2, true);

		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveAllExplorationButtonActionPerformed

	private void saveAllPopulationButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAllPopulationButtonActionPerformed
		this.saveSettings("default.txt");
		final String old_pop_size = maxPopSizeTextField.getText();

		int popSize = 8;

		for (int i = 0; i < 8; i++) {
			maxPopSizeTextField.setText(new String("" + popSize));
			popSize *= 2;

			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}
		maxPopSizeTextField.setText(old_pop_size);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveAllPopulationButtonActionPerformed

	private void saveSpeedButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveSpeedButtonActionPerformed
		this.saveSettings("default.txt");
		final String old_speed = goalAgentMovementSpeedTextField.getText();

		for (int i = 0; i < 21; i++) {
			goalAgentMovementSpeedTextField.setText(new String("" + i / 10.0));

			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}
		goalAgentMovementSpeedTextField.setText(old_speed);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveSpeedButtonActionPerformed

	private void saveStepsButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveStepsButtonActionPerformed
		this.saveSettings("default.txt");
		final String old_steps = numberOfStepsTextField.getText();

		int steps = 125;

		for (int i = 0; i < 6; i++) {
			numberOfStepsTextField.setText(new String("" + steps));
			steps *= 2;

			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}

		numberOfStepsTextField.setText(old_steps);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveStepsButtonActionPerformed

	private void saveTournamentButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveTournamentButtonActionPerformed
		this.saveSettings("default.txt");
		final String old_tournament = tournamentProbabilityTextField.getText();

		for (int i = 0; i < 11; i++) {
			tournamentProbabilityTextField.setText(new String("" + (4 * i + 60)
					/ 100.0));

			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}

		tournamentProbabilityTextField.setText(old_tournament);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveTournamentButtonActionPerformed

	private void saveRandomButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveRandomButtonActionPerformed
		this.saveSettings("default.txt");
		final String old_random = randomSeedTextField.getText();
		final Random generator = new Random();
		for (int i = 0; i < 10; i++) {
			randomSeedTextField.setText(new String("" + generator.nextInt()));

			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}

		randomSeedTextField.setText(old_random);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveRandomButtonActionPerformed

	private void saveLearningButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveLearningButtonActionPerformed
		this.saveSettings("default.txt");
		final String old_beta = betaTextField.getText();

		for (int i = 0; i < 18; i++) {
			double learning_rate = 0.0;
			switch (i) {
			case 0:
				learning_rate = 0.00001;
				break;
			case 1:
				learning_rate = 0.00005;
				break;
			case 2:
				learning_rate = 0.0001;
				break;
			case 3:
				learning_rate = 0.0005;
				break;
			case 4:
				learning_rate = 0.001;
				break;
			case 5:
				learning_rate = 0.005;
				break;
			case 6:
				learning_rate = 0.01;
				break;
			case 7:
				learning_rate = 0.05;
				break;
			case 8:
				learning_rate = 0.1;
				break;
			case 9:
				learning_rate = 0.2;
				break;
			case 10:
				learning_rate = 0.3;
				break;
			case 11:
				learning_rate = 0.4;
				break;
			case 12:
				learning_rate = 0.5;
				break;
			case 13:
				learning_rate = 0.6;
				break;
			case 14:
				learning_rate = 0.7;
				break;
			case 15:
				learning_rate = 0.8;
				break;
			case 16:
				learning_rate = 0.9;
				break;
			case 17:
				learning_rate = 0.99;
				break;
			}
			betaTextField.setText(new String("" + learning_rate));

			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}

		betaTextField.setText(old_beta);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveLearningButtonActionPerformed

	private void savePredictionDiscountButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_savePredictionDiscountButtonActionPerformed
		this.saveSettings("default.txt");
		final String old_gamma = gammaTextField.getText();

		for (int i = 0; i < 16; i++) {
			double gamma = 0.0;
			switch (i) {
			case 0:
				gamma = 0.1;
				break;
			case 1:
				gamma = 0.2;
				break;
			case 2:
				gamma = 0.3;
				break;
			case 3:
				gamma = 0.4;
				break;
			case 4:
				gamma = 0.5;
				break;
			case 5:
				gamma = 0.6;
				break;
			case 6:
				gamma = 0.65;
				break;
			case 7:
				gamma = 0.70;
				break;
			case 8:
				gamma = 0.71;
				break;
			case 9:
				gamma = 0.72;
				break;
			case 10:
				gamma = 0.75;
				break;
			case 11:
				gamma = 0.8;
				break;
			case 12:
				gamma = 0.85;
				break;
			case 13:
				gamma = 0.9;
				break;
			case 14:
				gamma = 0.95;
				break;
			case 15:
				gamma = 1.0;
				break;
			}
			gammaTextField.setText(new String("" + gamma));

			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}

		gammaTextField.setText(old_gamma);
		packageButton.setEnabled(true);
	}// GEN-LAST:event_savePredictionDiscountButtonActionPerformed

	private void eventXCSAgentRadioButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_eventXCSAgentRadioButtonActionPerformed
		activateLCSControls(eventXCSAgentRadioButton.isSelected());
	}// GEN-LAST:event_eventXCSAgentRadioButtonActionPerformed

	private void saveAllRewardFunctionsButtonActionPerformed(
			final java.awt.event.ActionEvent evt) {// GEN-FIRST:event_saveAllRewardFunctionsButtonActionPerformed
		this.saveSettings("default.txt");
		final ButtonModel old_model = baseRewardFunctionButtonGroup
				.getSelection();

		for (int i = 0; i < 6; i++) {
			switch (i) {
			case 0:
				baseRewardFunctionButtonGroup.setSelected(
						goalObsRadioButton.getModel(), true);
				break;
			case 1:
				baseRewardFunctionButtonGroup.setSelected(
						goalSightRadioButton.getModel(), true);
				break;
			case 2:
				baseRewardFunctionButtonGroup.setSelected(
						goalObsAgentsObsRadioButton.getModel(), true);
				break;
			case 3:
				baseRewardFunctionButtonGroup.setSelected(
						goalObsAgentsSightRadioButton.getModel(), true);
				break;
			case 4:
				baseRewardFunctionButtonGroup.setSelected(
						goalSightAgentsObsRadioButton.getModel(), true);
				break;
			case 5:
				baseRewardFunctionButtonGroup.setSelected(
						goalSightAgentsSightRadioButton.getModel(), true);
				break;
			}
			final String id = new String(timeString + "-" + conf_id);
			this.saveSettings("config-" + id + ".txt");
			config_strings.add(id);
			conf_id++;
		}

		baseRewardFunctionButtonGroup.setSelected(old_model, true);

		packageButton.setEnabled(true);
	}// GEN-LAST:event_saveAllRewardFunctionsButtonActionPerformed

	private void activateCommunicationControls(final boolean activate) {
		for (final Enumeration<AbstractButton> e = externalRewardButtonGroup
				.getElements(); e.hasMoreElements();) {
			e.nextElement().setEnabled(activate);
		}
	}

	private void activateLCSControls(final boolean activate) {
		rewardModelPanel.setEnabled(activate);
		explorationModePanel.setEnabled(activate);
		communicationPanel.setEnabled(activate);
		maxStackSizeLabel.setEnabled(activate);
		maxStackSizeTextField.setEnabled(activate);
		useQuadraticRewardCheckBox.setEnabled(activate);
		for (final Enumeration<AbstractButton> e = exploreExploitPhaseButtonGroup
				.getElements(); e.hasMoreElements();) {
			e.nextElement().setEnabled(activate);
		}
		for (final Enumeration<AbstractButton> e = switchExploreExploitButtonGroup
				.getElements(); e.hasMoreElements();) {
			e.nextElement().setEnabled(activate);
		}
		activateCommunicationControls(activate);
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(final String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {

			@Override
			 public void run() {
				new ConfigurationFrame().setVisible(true);
			}
		});
	}

	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JRadioButton DSXCSAgentRadioButton;
	private javax.swing.JRadioButton LCSGoalAgentMovementRadioButton;
	private javax.swing.JRadioButton SXCSAgentRadioButton;
	private javax.swing.JPanel agentAlgorithmPanel;
	private javax.swing.JLabel agentCountLabel;
	private javax.swing.ButtonGroup agentTypeButtonGroup;
	private javax.swing.JPanel agentTypePanel;
	private javax.swing.JLabel alphaLabel;
	private javax.swing.JTextField alphaTextField;
	private javax.swing.JRadioButton alwaysInTheSameDirectionGoalAgentMovementRadioButton;
	private javax.swing.JPanel automaticTestsPanel;
	private javax.swing.ButtonGroup baseRewardFunctionButtonGroup;
	private javax.swing.JPanel baseRewardFunctionPanel;
	private javax.swing.JLabel betaLabel;
	private javax.swing.JTextField betaTextField;
	private javax.swing.JPanel classifierSubsumptionAndDeletionPanel;
	private javax.swing.JPanel communicationPanel;
	private javax.swing.JLabel configurationNameLabel;
	private javax.swing.JLabel coveringWildcardProbabilityLabel;
	private javax.swing.JTextField coveringWildcardProbabilityTextField;
	private javax.swing.JCheckBox createAnimatedGIFCheckBox;
	private javax.swing.JLabel deltaLabel;
	private javax.swing.JTextField deltaTextField;
	private javax.swing.JRadioButton difficultScenarioRadioButton;
	private javax.swing.JCheckBox doActionSetSubsumptionCheckBox;
	private javax.swing.JCheckBox doEvolutionaryAlgorithmCheckBox;
	private javax.swing.JCheckBox doGASubsumptionCheckBox;
	private javax.swing.JLabel epsilon0Label;
	private javax.swing.JTextField epsilon0TextField;
	private javax.swing.JRadioButton eventXCSAgentRadioButton;
	private javax.swing.JPanel explorationModePanel;
	private javax.swing.ButtonGroup exploreExploitPhaseButtonGroup;
	private javax.swing.JPanel exploreExploitPhasePanel;
	private javax.swing.ButtonGroup externalRewardButtonGroup;
	private javax.swing.JFileChooser fileChooser;
	private javax.swing.JPanel fitnessAndPredictionPanel;
	private javax.swing.JLabel fitnessInitializationLabel;
	private javax.swing.JTextField fitnessInitializationTextField;
	private javax.swing.JLabel fitnessReductionLabel;
	private javax.swing.JTextField fitnessReductionTextField;
	private javax.swing.JPanel gaParametersPanel;
	private javax.swing.JLabel gammaLabel;
	private javax.swing.JTextField gammaTextField;
	private javax.swing.ButtonGroup goalAgentMovementButtonGroup;
	private javax.swing.JPanel goalAgentMovementPanel;
	private javax.swing.JLabel goalAgentMovementSpeedLabel;
	private javax.swing.JTextField goalAgentMovementSpeedTextField;
	private javax.swing.JRadioButton goalObsAgentsObsRadioButton;
	private javax.swing.JRadioButton goalObsAgentsSightRadioButton;
	private javax.swing.JRadioButton goalObsRadioButton;
	private javax.swing.JRadioButton goalSightAgentsObsRadioButton;
	private javax.swing.JRadioButton goalSightAgentsSightRadioButton;
	private javax.swing.JRadioButton goalSightRadioButton;
	private javax.swing.JPanel gridPanel;
	private javax.swing.JPanel inputOutputPanel;
	private javax.swing.JRadioButton intelligentGoalAgentMovementRadioButton;
	private javax.swing.JPanel lcsParametersPanel;
	private javax.swing.JCheckBox logOutputCheckBox;
	private javax.swing.JTextField maxAgentsTextField;
	private javax.swing.JRadioButton maxOneDirectionChangeGoalAgentMovementRadioButton;
	private javax.swing.JLabel maxPopSizeLabel;
	private javax.swing.JTextField maxPopSizeTextField;
	private javax.swing.JLabel maxStackSizeLabel;
	private javax.swing.JTextField maxStackSizeTextField;
	private javax.swing.JLabel maxXLabel;
	private javax.swing.JTextField maxXTextField;
	private javax.swing.JTextField maxYTextField;
	private javax.swing.JLabel mutationProbabilityLabel;
	private javax.swing.JTextField mutationProbabilityTextField;
	private javax.swing.JRadioButton noExternalRewardRadioButton;
	private javax.swing.JLabel nuLabel;
	private javax.swing.JTextField nuTextField;
	private javax.swing.JLabel numberOfExperimentsLabel;
	private javax.swing.JTextField numberOfExperimentsTextField;
	private javax.swing.JTextField numberOfProblemsTextField;
	private javax.swing.JTextField numberOfStepsTextField;
	private javax.swing.JLabel obstacleConnectionFactorLabel;
	private javax.swing.JTextField obstacleConnectionFactorTextField;
	private javax.swing.JLabel obstaclePercentageLabel;
	private javax.swing.JTextField obstaclePercentageTextField;
	private javax.swing.JButton packageButton;
	private javax.swing.JRadioButton pillarScenarioRadioButton;
	private javax.swing.JLabel predictionErrorInitializationLabel;
	private javax.swing.JTextField predictionErrorInitializationTextField;
	private javax.swing.JLabel predictionErrorReductionLabel;
	private javax.swing.JTextField predictionErrorReductionTextField;
	private javax.swing.JLabel predictionInitializationLabel;
	private javax.swing.JTextField predictionInitializationTextField;
	private javax.swing.JPanel problemDefinitionPanel;
	private javax.swing.JLabel problemsLabel;
	private javax.swing.JRadioButton randomBestSelectionRadioButton;
	private javax.swing.JRadioButton randomGoalAgentMovementRadioButton;
	private javax.swing.JRadioButton randomRouletteSelectionRadioButton;
	private javax.swing.JRadioButton randomScenarioRadioButton;
	private javax.swing.JLabel randomSeedLabel;
	private javax.swing.JTextField randomSeedTextField;
	private javax.swing.JCheckBox randomStartCheckBox;
	private javax.swing.JRadioButton randomTournamentSelectionRadioButton;
	private javax.swing.JRadioButton randomizedMovementRadioButton;
	private javax.swing.JScrollPane resultsScrollPane;
	private javax.swing.JTable resultsTable;
	private javax.swing.JRadioButton rewardAllEquallyRadioButton;
	private javax.swing.JLabel rewardDistanceLabel;
	private javax.swing.JTextField rewardDistanceTextField;
	private javax.swing.JRadioButton rewardEgoisticRadioButton;
	private javax.swing.JPanel rewardModelPanel;
	private javax.swing.JLabel rewardRangeLabel;
	private javax.swing.JRadioButton rouletteBestSelectionRadioButton;
	private javax.swing.JRadioButton rouletteTournamentSelectionRadioButton;
	private javax.swing.JButton runLastBatchButton;
	private javax.swing.JButton saveAllExplorationButton;
	private javax.swing.JButton saveAllExplorationButton1;
	private javax.swing.JButton saveAllPopulationButton;
	private javax.swing.JButton saveAllRandomButton;
	private javax.swing.JButton saveAllStackButton;
	private javax.swing.JButton saveLearningButton;
	private javax.swing.JButton saveNewButton;
	private javax.swing.JButton savePredictionDiscountButton;
	private javax.swing.JButton saveRandomButton;
	private javax.swing.JButton saveSpeedButton;
	private javax.swing.JButton saveStepsButton;
	private javax.swing.JButton saveTournamentButton;
	private javax.swing.ButtonGroup scenarioTypeButtonGroup;
	private javax.swing.JTextField sightRangeTextField;
	private javax.swing.JRadioButton standardXCSAgentRadioButton;
	private javax.swing.JRadioButton staticAIAgentRadioButton;
	private javax.swing.JLabel stepsLabel;
	private javax.swing.ButtonGroup switchExploreExploitButtonGroup;
	private javax.swing.JPanel switchExploreExploitPanel;
	private javax.swing.JRadioButton switchGoalObsRadioButton;
	private javax.swing.JRadioButton switchGoalSightRadioButton;
	private javax.swing.JRadioButton switchNoRadioButton;
	private javax.swing.JRadioButton switchRewardRadioButton;
	private javax.swing.JPanel testsPanel;
	private javax.swing.JLabel thetaDelLabel;
	private javax.swing.JTextField thetaDelTextField;
	private javax.swing.JLabel thetaLabel;
	private javax.swing.JLabel thetaSubsumerLabel;
	private javax.swing.JTextField thetaSubsumerTextField;
	private javax.swing.JTextField thetaTextField;
	private javax.swing.JRadioButton totalRandomGoalAgentMovementRadioButton;
	private javax.swing.JRadioButton tournamentBestSelectionRadioButton;
	private javax.swing.JLabel tournamentProbabilityLabel;
	private javax.swing.JTextField tournamentProbabilityTextField;
	private javax.swing.JButton updateDatabaseButton;
	private javax.swing.JCheckBox useQuadraticRewardCheckBox;
	// End of variables declaration//GEN-END:variables
}
