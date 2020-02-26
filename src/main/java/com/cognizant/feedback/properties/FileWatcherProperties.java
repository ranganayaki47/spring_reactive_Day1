package com.cognizant.feedback.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
//@RefreshScope
@ConfigurationProperties(prefix = "outreach")
public class FileWatcherProperties {
  private String sourcePath;

  private String destinationPath;

  private String errorPath;
}
