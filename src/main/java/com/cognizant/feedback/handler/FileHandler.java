package com.cognizant.feedback.handler;

import java.io.File;

public interface FileHandler {
  public void handleFile(File sourceFile, File destFile);

  public String getType();
}
