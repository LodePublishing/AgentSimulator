package com.clawsoftware.agentconfiguration;

/**
 *
 * @author Clemens Lode, clemens at lode.de, 2009, University Karlsruhe (TH), clemens@lode.de
 */

import java.math.BigInteger;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class ResultsTable extends AbstractTableModel {
	/**
	 * einen String, für restliche Unterschiede
	 */
	public final static String[] columnNames = { "Configuration name",
			"Configuration ID", "Wasted movements", "Half goal percentage",
			"Goal percentage"// ,
	/*
	 * "Spread points", "Average points", "Spread agent distance",
	 * "Average agent distance", "Spread goal distance",
	 * "Average goal distance", "Average prediction error", "Covered area",
	 * "Wasted coverage", "Goal jumps", "Wasted movements"
	 */

	};
	public final static int COLUMN_COUNT = columnNames.length;

	/** Vector of Object[], this are the datas of the table */
	Vector datas = new Vector();

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return datas.size();
	}

	@Override
	public String getColumnName(final int col) {
		return columnNames[col];
	}

	@Override
	public Object getValueAt(final int row, final int col) {
		Object[] array;
		do {
			array = (Object[]) datas.elementAt(row);
		} while (array.length < columnNames.length);
		return array[col];
	}

	@Override
	public Class getColumnClass(final int c) {
		return getValueAt(0, c).getClass();
	}

	public void addRow(final Object[] row) {
		datas.add(row);
	}

	public Object[] getRow(final int row) {
		return (Object[]) datas.get(row);
	}

	/**
	 * Searchs through the database if it contains the object array
	 * 
	 * @param r
	 *            object array
	 * @return -1 if the entry was not found, otherwise the index of the entry
	 */
	public int contains(final Object[] r) {
		if (datas.size() == 0) {
			return -1;
		}

		for (int row = 0; row < datas.size(); row++) {
			final BigInteger t0 = (BigInteger) getValueAt(row, 0);
			if (!t0.equals(r[0])) {
				continue;
			}
			return row;
		}
		return -1;
	}
}