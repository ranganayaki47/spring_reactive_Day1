package com.cognizant.feedback.service;

import com.cognizant.feedback.exceptions.FMSException;
import com.cognizant.feedback.properties.FileWatcherProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cognizant.feedback.constants.ErrorConstants.NOT_VALID_FILE;
import static com.cognizant.feedback.constants.FileConstants.DATE_TIME;
import static com.cognizant.feedback.constants.FileConstants.ENTRY_CREATE;

@Slf4j
@Service
@AllArgsConstructor
public class FileWatcherService {

  private final FileWatcherProperties fileWatcherProperties;
  // private Boolean watchEventInitiated = Boolean.FALSE;

  /*@EventListener(ContextRefreshedEvent.class)
  public boolean contextEvent() {
    log.debug("Start processing files");
    CompletableFuture.runAsync(
        () -> {
          // processAllFiles();
          watchEvent();
        });
    this.watchEventInitiated = Boolean.TRUE;
    return watchEventInitiated;
  }*/

  private void processAllFiles() {
    log.debug("Processing all Files");
    File folder = new File(fileWatcherProperties.getSourcePath());
    File[] listFiles = folder.listFiles();
    if (listFiles != null) {
      for (File sourceFile : listFiles) {
        divertToHanlder(sourceFile);
      }
    }
  }

  private void watchEvent() {
    log.info("Initiating the watch event");
    try {
      Path path = Paths.get(fileWatcherProperties.getSourcePath());
      WatchService watchService = FileSystems.getDefault().newWatchService();
      path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

      WatchKey key;
      while ((key = watchService.take()) != null) {
        for (WatchEvent<?> event : key.pollEvents()) {
          if (event.kind().name().equals(ENTRY_CREATE)) {
            log.debug("File affected: " + event.context());
            processAllFiles();
          }
        }
        key.reset();
      }
    } catch (Exception exception) {
      log.error("Exception in watch event", exception);
      // this.watchEventInitiated = Boolean.FALSE;
    }
  }

  private void handleFile(String fileName) {
    try {
      File sourceFile = new File(fileWatcherProperties.getSourcePath() + fileName);
      while (!sourceFile.renameTo(sourceFile)) {
        // Cannot read from file, windows still working on it.
        Thread.sleep(10);
      }
      divertToHanlder(sourceFile);
    } catch (Exception exception) {
      log.info("Exception in processing file", exception);
    }
  }

  private void divertToHanlder(File sourceFile) {
    log.info("Saving the file " + sourceFile.getAbsolutePath());
    if (sourceFile.isFile()) {
      try {
        String extention = getFileExtention(sourceFile);
        switch (extention) {
          case ".xls":
            saveExcelFileToDB(sourceFile);
            // this.handleXLToCSV(sourceFile);
            break;
          case ".xlsx":
            saveExcelFileToDB(sourceFile);
            // this.handleXLToCSV(sourceFile);
            break;
          default:
            log.info("Invalid file format");
            break;
        }
      } catch (Exception exception) {
        log.error("Exception in converting file, moving file to error location", exception);
        moveFileToErrorLocation(sourceFile);
      }
    } else {
      log.info("Folder, its not the file");
    }
  }

  private String getFileName(File file) throws FMSException {
    validateFile(file);
    String fileName = file.getName();
    int index = fileName.indexOf('.');
    return fileName.substring(0, index);
  }

  private void validateFile(File file) throws FMSException {
    if (file == null || !file.isFile()) {
      throw new FMSException(NOT_VALID_FILE);
    }
  }

  private String getFileExtention(File file) throws FMSException {
    validateFile(file);
    String fileName = file.getName();
    int index = fileName.indexOf('.');
    return fileName.substring(index);
  }

  private String getDestinationFile(File file) throws FMSException {
    validateFile(file);
    String excelFileName = getFileName(file) + ".csv";
    excelFileName = fileWatcherProperties.getDestinationPath() + excelFileName;
    return excelFileName;
  }



  private Boolean saveExcelFileToDB(File sourcefile) {
    //byte[] excelData=sourcefile.g
    return true;
  }

  public void processFiles() {
   // watchEvent();
    log.debug("Start Processing Files");
    File sourceFolder = new File(fileWatcherProperties.getSourcePath());

    if (sourceFolder.exists() && sourceFolder.isDirectory()) {
      File[] listOfFiles = sourceFolder.listFiles();
      if (listOfFiles != null) {
        for (File sourceFile : listOfFiles) {
          try {
            while (!sourceFile.renameTo(sourceFile)) {
              // <-- Cannot read from file, windows still working on it. -->
              Thread.sleep(10);
            }
            saveExcelFileToDB(sourceFile);
          } catch (Exception exception) {
            log.info("Exception in processing file", exception);
            moveFileToErrorLocation(sourceFile);
          }
          moveFileToProcessedLocation(sourceFile);
        }
      }
    } else {
      log.debug(sourceFolder + "  Folder does not exists");
    }
  }

  private void moveFileToProcessedLocation(File file) {
    log.debug("File moved to Processed location : " + file.getName());
    File destinationFolder = new File(fileWatcherProperties.getDestinationPath());

    /* if (!destinationFolder.exists()) {
      destinationFolder.mkdirs();
    }*/

    String fileName = addTimestamp(file.getName(), getCurrentDateTime());
    log.debug("Filename with TimeStamp:" + fileName);
    boolean isFileMoved = file.renameTo(new File(destinationFolder + "\\" + fileName));

    if (isFileMoved) {
      log.debug("File moved successfully");
    } else {
      log.debug("Error renaming file..Moved to Error Location :" + file.getName());
      moveFileToErrorLocation(file);
    }
  }

  private void moveFileToErrorLocation(File sourceFile) {
    try {
      FileUtils.moveFileToDirectory(
          sourceFile, new File(fileWatcherProperties.getErrorPath()), true);
    } catch (IOException e) {
      log.error(
          "Can not move the file from the source to error path " + sourceFile.getAbsolutePath());
    }
  }

  private static String addTimestamp(String name, String timeStamp) {
    int lastIndexOf = name.lastIndexOf('.');
    return (lastIndexOf == -1
            ? name + "_" + timeStamp
            : name.substring(0, lastIndexOf) + "_" + timeStamp + name.substring(lastIndexOf))
        .replaceAll("[\\/:\\*\\?\"<>| ]", "_");
  }

  private static String getCurrentDateTime() {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME);
    Date date = new Date();
    return sdf.format(date);
  }
}
