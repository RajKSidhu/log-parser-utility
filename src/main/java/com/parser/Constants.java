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
	String REQUEST_RESPONSE_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-dhanistockorchestration-request-response.log.2021-10-26";
	String WEBLOGIC_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-dhanistockorchestration-weblogic-api.log.2021-10-26";
	String TR_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-dhanistockorchestration-tr-api.log.2021-10-26";
	String DION_FILE = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\filtered-dhanistockorchestration-dion-api.log.2021-10-26";
	String OUTPUT_FILE_PATH = "C:\\Users\\rajdeep\\Documents\\Prod Logs\\Parsed\\parsed-log-%s.xlsx";
	String SHEET_NAME = "Logs";
	int RESPONSE_TIME_LIMIT = 1500;
	short ROW_SIZE = 1200;
}
