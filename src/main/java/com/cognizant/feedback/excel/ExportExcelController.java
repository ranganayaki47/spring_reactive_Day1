package com.cognizant.feedback.excel;

import com.cognizant.feedback.dto.UserDTO;
import com.cognizant.feedback.exceptions.FMSException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@Slf4j
public class ExportExcelController {

  private final ExportExcelService excelService;

  @PostMapping(value = "/pmousersexporttoexcel")
  public ResponseEntity<byte[]> exportToExcelPMOUsers(@RequestParam String fileName)
      throws FMSException {
    log.debug("Export All PMO Users to Excel");
    return Optional.ofNullable(excelService.exportToExcel(fileName))
        .map(
            excelDTOs -> {
              byte[] data = (byte[]) excelDTOs.get("byte");
              return new ResponseEntity<>(
                  data, (HttpHeaders) excelDTOs.get("header"), HttpStatus.OK);
            })
        .orElse(new ResponseEntity<>(HttpStatus.NO_CONTENT));
  }
}
