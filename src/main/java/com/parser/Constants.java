package com.parser;

public interface Constants {
	String TIME_FILTER = "TIME=>";
	String URL_FILTER = "URL=>";
	String RESPONSE_TIME_FILTER = "RESPONSE_TIME(Milis):";
	String REQUEST_FILTER = "Call";
	String HTTP = "http";
	String RESPONSE_FILTER = "Response";
	String API_FORMAT = "API: %s";
	String RESPONSE_TIME_FORMAT = "Response Time: %s (ms)";
	String REQUEST_RESPONSE_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-request-response.log.2024-10-25";
	String WEBLOGIC_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-weblogic-api.log.2024-10-25";
	String TR_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-tr-api.log.2024-10-25";
	String DION_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-dion-api.log.2024-10-25";
	String OUTPUT_FILE_PATH = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\Parsed\\parsed-log-%s.xlsx";
	String SHEET_NAME = "Logs";
	int RESPONSE_TIME_LIMIT = 1500;
	short ROW_SIZE = 1200;
}
