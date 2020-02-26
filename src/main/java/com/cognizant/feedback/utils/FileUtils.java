package com.cognizant.feedback.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class FileUtils {

  public List<String> getFilesFromFolder(String source, String fileNamePattern) {
    File folder = new File(source);
    String[] files = folder.list((dir, name) -> name.startsWith(fileNamePattern));
    return Arrays.asList(files);
  }

  public void moveFilesToLocation(String source, String destination, List<String> filesToMove) {
    for (String fileName : filesToMove) {
      File sourceFile = new File(source + fileName);
      if (sourceFile.renameTo(new File(destination + sourceFile.getName()))) {
        log.info(fileName + " Successfully file moved!");
      } else {
        log.info(fileName + " moving failed!");
      }
    }
  }
}
