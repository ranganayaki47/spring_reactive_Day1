package com.cognizant.feedback.service;

import com.cognizant.feedback.constants.FileHandlerConstants;
import com.cognizant.feedback.exceptions.FMSException;
import com.cognizant.feedback.handler.FileHandler;
import com.cognizant.feedback.handler.FileHandlerFactory;
import com.cognizant.feedback.properties.FileWatcherProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.cognizant.feedback.constants.ErrorConstants.NOT_VALID_FILE;

@Service
@Slf4j
public class FilePathListenerService {
  public boolean watchEventInitiated = Boolean.FALSE;
  private final FileWatcherProperties fileShareProperties;
  private final FileHandlerFactory fileHandlerFactory;

  FilePathListenerService(
      FileWatcherProperties fileShareProperties, FileHandlerFactory fileHandlerFactory) {
    this.fileShareProperties = fileShareProperties;
    this.fileHandlerFactory = fileHandlerFactory;
  }

  @EventListener(ContextRefreshedEvent.class)
  public boolean contextEvent() {
    log.info("Start converting files");
    CompletableFuture.runAsync(
        () -> {
          processAllFiles();
          watchEvent();
        });
    this.watchEventInitiated = Boolean.TRUE;
    return watchEventInitiated;
  }

  public void processAllFiles() {
    log.info("processing all Files");
    File folder = new File(fileShareProperties.getSourcePath());
    File[] listFiles = folder.listFiles();
    if (listFiles != null) {
      for (File sourceFile : listFiles) {
        divertToHanlder(sourceFile);
      }
    }
  }

  private void watchEvent() {
    log.info("Initiating the watch event");
    try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
      Path path = Paths.get(fileShareProperties.getSourcePath());
      WatchKey watchKey =
          path.register(
              watchService,
              StandardWatchEventKinds.ENTRY_MODIFY,
              StandardWatchEventKinds.ENTRY_CREATE);
      while (true) {
        WatchKey key = watchService.take();
        List<WatchEvent<?>> watchEvents = key.pollEvents();
        for (WatchEvent genericEvent : watchEvents) {
          WatchEvent pathEvent = genericEvent;
          Path pathFile = (Path) pathEvent.context();
          String fileName = pathFile.toFile().getPath();
          handleFile(fileName);
        }
        boolean validKey = key.reset();
        if (!validKey) {
          log.info("reset watch key failed");
          this.watchEventInitiated = Boolean.FALSE;
          break;
        }
      }
    } catch (Exception exception) {
      log.error("Exception in watch event", exception);
      this.watchEventInitiated = Boolean.FALSE;
    }
  }

  private void handleFile(String fileName) {
    try {
      File sourceFile = new File(fileShareProperties.getSourcePath() + fileName);
      while (!sourceFile.renameTo(sourceFile)) {
        // Cannot read from file, windows still working on it.
        Thread.sleep(10);
      }
      divertToHanlder(sourceFile);
    } catch (Exception exception) {
      log.info("Exception in converting file", exception);
    }
  }

  private void divertToHanlder(File sourceFile) {
    log.info("Coverting the file " + sourceFile.getAbsolutePath());
    if (sourceFile.isFile()) {
      try {
        String extention = getFileExtention(sourceFile);
        switch (extention) {
          case ".xls":
            this.handleXLToCSV(sourceFile);
            break;
          case ".xlsx":
            this.handleXLToCSV(sourceFile);
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

  private void moveFileToErrorLocation(File sourceFile) {
    try {
      FileUtils.moveFile(
          sourceFile, new File(fileShareProperties.getErrorPath() + sourceFile.getName()));
    } catch (IOException e) {
      log.error(
          "Can not move the file from the source to error path " + sourceFile.getAbsolutePath());
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
    excelFileName = fileShareProperties.getDestinationPath() + excelFileName;
    return excelFileName;
  }

  private void handleXLToCSV(File sourceFile) throws FMSException {
    String destinationFileName = getDestinationFile(sourceFile);
    FileHandler fileHanlder = fileHandlerFactory.getFileHandler(FileHandlerConstants.EXCEL_TO_CSV);
    fileHanlder.handleFile(sourceFile, new File(destinationFileName));
    log.info("File converted successfully for input file " + sourceFile.getName());
  }
}
