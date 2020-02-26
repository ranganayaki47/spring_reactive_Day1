package com.cognizant.feedback.service;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.Iterator;

public class FeedbackManagementService {

  private static String jdbcURL = "jdbc:mysql://localhost:3306/sales?useSSL=false";
  private static String username = "root";
  private static String password = "root";

 /* public void isFileExist() {
    File tempFile = new File(excelFilePath);
    boolean exists = tempFile.exists();
    if (exists) {
      readDataFromExcel(tempFile);
    } else {
      System.out.println(" File is not available in the folder...");
    }
  }*/

  public void readDataFromExcel(File file) {
    int batchSize = 20;
    Connection connection = null;
    try {
      long start = System.currentTimeMillis();
      FileInputStream inputStream = new FileInputStream(file);
      Workbook workbook = new XSSFWorkbook(inputStream);
      Sheet firstSheet = workbook.getSheetAt(0);
      Iterator<Row> rowIterator = firstSheet.iterator();
      connection = DriverManager.getConnection(jdbcURL, username, password);
      connection.setAutoCommit(false);
      String sql = "INSERT INTO students (name, enrolled, progress) VALUES (?, ?, ?)";
      PreparedStatement statement = connection.prepareStatement(sql);
      int count = 0;
      rowIterator.next(); // skip the header row
      while (rowIterator.hasNext()) {
        Row nextRow = rowIterator.next();
        Iterator<Cell> cellIterator = nextRow.cellIterator();
        while (cellIterator.hasNext()) {
          Cell nextCell = cellIterator.next();
          int columnIndex = nextCell.getColumnIndex();
          switch (columnIndex) {
            case 0:
              String eventName = nextCell.getStringCellValue();
              statement.setString(1, eventName);
              break;
            case 1:
              String eventDescription = nextCell.getStringCellValue();
              statement.setString(2, eventDescription);
            case 2:
              Date enrollDate = nextCell.getDateCellValue();
              statement.setTimestamp(2, new Timestamp(enrollDate.getTime()));
            case 3:
              int progress = (int) nextCell.getNumericCellValue();
              statement.setInt(3, progress);
          }
        }
        statement.addBatch();
        if (count % batchSize == 0) {
          statement.executeBatch();
        }
      }
      //            workbook.close();
      // execute the remaining queries
      statement.executeBatch();
      connection.commit();
      connection.close();
      long end = System.currentTimeMillis();
      System.out.printf("Import done in %d ms\n", (end - start));
    } catch (Exception e) {
      System.out.println("e = " + e);
    }
  }
}
