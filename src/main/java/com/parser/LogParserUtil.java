package com.parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogParserUtil implements Constants {
	private static final Logger logger = LoggerFactory.getLogger(LogParserUtil.class);

	private static Sheet createExcelSheet(Workbook workbook) {
		Sheet sheet = workbook.createSheet(SHEET_NAME);
		Row header = sheet.createRow(0);
		XSSFFont font = ((XSSFWorkbook) workbook).createFont();
		font.setBold(true);
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		style.setFont(font);
		for (ExcelColumns column : ExcelColumns.values()) {
			sheet.setColumnWidth(column.getIndex(), column.getColumnSize());
			Cell headerCell = header.createCell(column.getIndex());
			headerCell.setCellValue(column.getName());
			headerCell.setCellStyle(style);
		}

		return sheet;
	}

	private static boolean findAndReplace(Sheet sheet, String url, Integer responseTime) {
		for (Row row : sheet) {
			Cell urlCell = row.getCell(ExcelColumns.API_URL.getIndex());
			if (urlCell.getStringCellValue().equals(url) == false) {
				continue;
			}

			Cell responseTimeCell = row.getCell(ExcelColumns.RESPONSE_TIME.getIndex());
			if (Integer.parseInt(responseTimeCell.getStringCellValue()) < responseTime) {
				responseTimeCell.setCellValue(responseTime.toString());
			}

			return true;
		}

		return false;
	}

	private static List<String> parseWeblogicLogs(String id) {
		List<String> list = new ArrayList<>();
		File file = new File(WEBLOGIC_FILE);
		if (file.exists() == false) {
			return list;
		}

		logger.info("Weblogic: processing for id {}", id);
		int lineCount = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(WEBLOGIC_FILE))) {
			String str;
			while ((str = br.readLine()) != null) {
				lineCount++;
				if (StringUtils.isEmpty(str) == true || str.contains(id) == false
						|| str.contains(RESPONSE_FILTER) == false) {
					continue;
				}

				String apiUrl = str.substring(str.indexOf(HTTP), str.indexOf(" Response:"));
				list.add(String.format(API_FORMAT, apiUrl));
				int index = str.indexOf(RESPONSE_TIME_FILTER);
				if (index > -1) {
					String data = String.format(RESPONSE_TIME_FORMAT,
							str.substring(index + RESPONSE_TIME_FILTER.length()).trim());
					list.add(data);
				}
			}
		} catch (IOException ex) {
			logger.error("Weblogic: issue at line {}", lineCount);
			logger.error(ex.getMessage(), ex);
		}

		logger.info("Weblogic: completed");
		return list;
	}

	private static List<String> parseDionLogs(String id) {
		List<String> list = new ArrayList<>();
		File file = new File(DION_FILE);
		if (file.exists() == false) {
			return list;
		}

		logger.info("Dion: processing for id {}", id);
		int lineCount = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(DION_FILE))) {
			String str;
			while ((str = br.readLine()) != null) {
				lineCount++;
				if (StringUtils.isEmpty(str) == true || str.contains(id) == false) {
					continue;
				}

				if (str.contains(REQUEST_FILTER) == true) {
					int commaIndex = str.indexOf(",", str.indexOf(HTTP));
					if (commaIndex == -1) {
						int textIndex = str.indexOf(" Entity");
						if (textIndex == -1) {
							logger.error("Unable to parse: {}", str);
							continue;
						}

						commaIndex = textIndex;
					}

					String data = String.format(API_FORMAT, str.substring(str.indexOf(HTTP), commaIndex));
					list.add(data);
				}

				int index = str.indexOf(RESPONSE_TIME_FILTER);
				if (str.contains(RESPONSE_FILTER) == true && index > -1) {
					String data = String.format(RESPONSE_TIME_FORMAT,
							str.substring(index + RESPONSE_TIME_FILTER.length()).trim());
					list.add(data);
				}
			}
		} catch (IOException ex) {
			logger.error("Dion: issue at line {}", lineCount);
			logger.error(ex.getMessage(), ex);
		}

		logger.info("Dion: completed");
		return list;
	}

	private static List<String> parseTRLogs(String id) {
		List<String> list = new ArrayList<>();
		File file = new File(TR_FILE);
		if (file.exists() == false) {
			return list;
		}

		logger.info("TR: processing for id {}", id);
		int lineCount = 1;
		try (BufferedReader br = new BufferedReader(new FileReader(TR_FILE))) {
			String str;
			while ((str = br.readLine()) != null) {
				lineCount++;
				if (StringUtils.isEmpty(str) == true || str.contains(id) == false) {
					continue;
				}

				if (str.contains(REQUEST_FILTER) == true) {
					String data = String.format(API_FORMAT,
							str.substring(str.indexOf(HTTP), str.indexOf(",", str.indexOf(HTTP))));
					list.add(data);
				}

				int index = str.indexOf(RESPONSE_TIME_FILTER);
				if (str.contains(RESPONSE_FILTER) == true && index > -1) {
					String data = String.format(RESPONSE_TIME_FORMAT,
							str.substring(index + RESPONSE_TIME_FILTER.length()).trim());
					list.add(data);
				}
			}
		} catch (IOException ex) {
			logger.error("TR: issue at line {}", lineCount);
			logger.error(ex.getMessage(), ex);
		}

		logger.info("TR: completed");
		return list;
	}

	private static void createCell(CellStyle style, Row row, int index, String data) {
		Cell cell = row.createCell(index);
		cell.setCellValue(data);
		cell.setCellStyle(style);
	}

	public static void main(String[] args) {
		File file = new File(TR_FILE);
		if (file.exists() == false) {
			logger.info("No file to process");
			return;
		}

		logger.info("Request Response: processing");
		Workbook workbook = new XSSFWorkbook();
		CellStyle style = workbook.createCellStyle();
		style.setWrapText(true);
		int lineCount = 1;
		Sheet sheet = createExcelSheet(workbook);
		try (BufferedReader br = new BufferedReader(new FileReader(REQUEST_RESPONSE_FILE));
				FileOutputStream fos = new FileOutputStream(String.format(OUTPUT_FILE_PATH,
						file.getName().substring(file.getName().lastIndexOf(".") + 1)));) {
			String str;
			while ((str = br.readLine()) != null) {
				lineCount++;
				if (StringUtils.isEmpty(str) == true || str.contains(TIME_FILTER) == false
						|| str.contains(URL_FILTER) == false) {
					continue;
				}

				String time = str.substring(str.indexOf(TIME_FILTER) + TIME_FILTER.length());
				int commaIndex = time.indexOf(',');
				int bracesIndex = time.indexOf('}');
				if (commaIndex == -1 || bracesIndex < commaIndex) {
					time = time.substring(0, bracesIndex);
				} else {
					time = time.substring(0, commaIndex);
				}

				Integer responseTime = Integer.parseInt(time); // capturing response time
				if (responseTime == null || responseTime <= RESPONSE_TIME_LIMIT) {
					continue;
				}

				String url = str.substring(str.indexOf(URL_FILTER) + URL_FILTER.length());
				url = url.substring(0, url.indexOf(',')); // capturing URL

				boolean found = findAndReplace(sheet, url, responseTime);
				if (found == false) {
					String[] strArr = str.split(" "); // capturing id
					String id = null;
					if (strArr.length >= 3) {
						id = strArr[2];
					}

					String date = str.substring(str.indexOf(':') + 1, str.indexOf(',')); // capturing date
					Row row = sheet.createRow(sheet.getLastRowNum() + 1);
//					row.setHeight(ROW_SIZE);
					createCell(style, row, ExcelColumns.SERIAL_NO.getIndex(), Integer.toString(sheet.getLastRowNum()));
					createCell(style, row, ExcelColumns.ID.getIndex(), id);
					createCell(style, row, ExcelColumns.API_URL.getIndex(), url);
					createCell(style, row, ExcelColumns.RESPONSE_TIME.getIndex(), responseTime.toString());
					createCell(style, row, ExcelColumns.DATE_TIME.getIndex(), date);
					createCell(style, row, ExcelColumns.WEBLOGIC_DETAILS.getIndex(),
							parseWeblogicLogs(id).stream().collect(Collectors.joining("\r\n")));
					createCell(style, row, ExcelColumns.DION_DETAILS.getIndex(),
							parseDionLogs(id).stream().collect(Collectors.joining("\r\n")));
					createCell(style, row, ExcelColumns.TR_DETAILS.getIndex(),
							parseTRLogs(id).stream().collect(Collectors.joining("\r\n")));
				}
			}

			workbook.write(fos);
			workbook.close();
		} catch (IOException ex) {
			logger.error("Request Response: issue at line {}", lineCount);
			logger.error(ex.getMessage(), ex);
		}

		logger.info("Request Response: completed");
	}
}
