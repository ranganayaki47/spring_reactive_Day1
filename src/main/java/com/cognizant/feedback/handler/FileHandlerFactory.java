package com.cognizant.feedback.handler;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class FileHandlerFactory {
  private Map<String, FileHandler> fileHandlerMap;

  FileHandlerFactory(ApplicationContext applicationContext) {
    fileHandlerMap = new HashMap<>();
    Map<String, FileHandler> hanlders = applicationContext.getBeansOfType(FileHandler.class);
    hanlders.forEach((key, fileHandler) -> fileHandlerMap.put(fileHandler.getType(), fileHandler));
  }

  public FileHandler getFileHandler(String handlerName) {
    return fileHandlerMap.get(handlerName);
  }
}
