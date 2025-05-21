package com.parser;

public enum ExcelColumns {
	SERIAL_NO(0, "Sr No", 1800),
	ID(1, "ID", 6000),
	API_URL(2, "API URL", 12000),
	RESPONSE_TIME(3, "Response Time (ms)", 4500),
	DATE_TIME(4, "Date Time", 5000),
	WEBLOGIC_DETAILS(5, "Weblogic Details", 15000),
	DION_DETAILS(6, "Dion Details", 15000),
	TR_DETAILS(7, "TR Details", 15000);

	private final int index;
	private final String name;
	private final int columnSize;

	private ExcelColumns(int index, String name, int columnSize) {
		this.index = index;
		this.name = name;
		this.columnSize = columnSize;
	}

	public int getIndex() {
		return index;
	}

	public String getName() {
		return name;
	}

	public int getColumnSize() {
		return columnSize;
	}

}
