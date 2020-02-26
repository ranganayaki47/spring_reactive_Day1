/*
package com.cognizant.feedback.endpoint;

import com.cognizant.feedback.service.FilePathListenerService;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Endpoint(id = "watchrefresh")
public class WatchRefreshEndpoint {
  private final FilePathListenerService filePathListenerService;

  WatchRefreshEndpoint(FilePathListenerService filePathListenerService) {
    this.filePathListenerService = filePathListenerService;
  }

  @WriteOperation
  public boolean refreshFile() {
    if (!filePathListenerService.watchEventInitiated) {
      return filePathListenerService.contextEvent();
    }
    return Boolean.FALSE;
  }

  @ReadOperation
  public Map<String, Boolean> getWatchStatus() {
    Map<String, Boolean> watchStatus = new HashMap<>();
    watchStatus.put("watchStatus", filePathListenerService.watchEventInitiated);
    return watchStatus;
  }
}
*/
