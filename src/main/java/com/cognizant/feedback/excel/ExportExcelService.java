package com.cognizant.feedback.excel;

import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.ColumnBuilder;
import ar.com.fdvs.dj.domain.builders.ColumnBuilderException;
import ar.com.fdvs.dj.domain.builders.DynamicReportBuilder;
import ar.com.fdvs.dj.domain.builders.StyleBuilder;
import ar.com.fdvs.dj.domain.constants.*;
import ar.com.fdvs.dj.domain.entities.columns.AbstractColumn;
import com.cognizant.feedback.dto.UserDTO;
import com.cognizant.feedback.entity.UserEntity;
import com.cognizant.feedback.entity.UserRoleEntity;
import com.cognizant.feedback.exceptions.FMSException;
import com.cognizant.feedback.mapper.UserMapper;
import com.cognizant.feedback.repository.UserRepository;
import com.cognizant.feedback.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRStyledText;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import static com.cognizant.feedback.constants.ExcelConstants.*;
import static com.cognizant.feedback.constants.UserConstants.PMO;
import static org.apache.commons.compress.archivers.ar.ArArchiveEntry.HEADER;

@Service
@Slf4j
@AllArgsConstructor
public class ExportExcelService<T> {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final UserRoleRepository userRoleRepository;

  public Map<String, ?> exportToExcel(String fileName) throws FMSException {
    HashMap<String, ?> fileByte;
    try {
      UserRoleEntity userRole = getUserRole(PMO);
      List<UserEntity> userEntities =
          userRepository.findAllByUserRoleDetailsRoleId(userRole.getRoleId());
      List<UserDTO> userDTOs = userMapper.userEntitiesToUserDTOs(userEntities);

      if (CollectionUtils.isNotEmpty(userDTOs)) {
        fileByte =
            exportToExcelFile(
                new LinkedHashMap<>(), (List<T>) userDTOs, Optional.empty(), fileName);
        log.debug("Excel File PMO User data:");
      } else {
        return null;
      }
    } catch (IOException | ColumnBuilderException | JRException e) {
      log.debug("Exception on PMO Users fetch :", e.getMessage());
      throw new FMSException(DB_FETCH_FAILED);
    }
    return fileByte;
  }

  private UserRoleEntity getUserRole(String roleName) {
    return userRoleRepository.findByRoleName(roleName);
  }

  public HashMap<String, ?> exportToExcelFile(
      Map<String, List<Object>> entityField,
      List<T> resultSet,
      Optional<Map<String, List<Object>>> colspan,
      String fileName)
      throws JRException, IOException {
    HashMap<String, Object> newMap = new HashMap<>();
    final byte[] data = exportToExcel(entityField, resultSet, colspan);
    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.parseMediaType(APPLICATION_SPREADSHEET));
    header.set(HttpHeaders.CONTENT_DISPOSITION, INLINE_FILE_NAME);
    header.setContentLength(data.length);
    newMap.put(BYTE, data);
    newMap.put(HEADER, header);

    return newMap;
  }

  public byte[] exportToExcel(
      Map<String, List<Object>> psdetails,
      List<?> listDetails,
      Optional<Map<String, List<Object>>> colspanDetails)
      throws JRException, IOException {
    DynamicReportBuilder report = new DynamicReportBuilder();
    psdetails.forEach(
        (key, value) -> {
          report.addColumn(
              createColumn(
                  key.toString(),
                  (Class) value.get(INTEGER_ONE),
                  (String) value.get(INTEGER_ZERO),
                  (Integer) value.get(INTEGER_TWO)));
        });
    colspanDetails.ifPresent(
        dtoObjects ->
            dtoObjects.forEach(
                (k, val) -> {
                  report.setColspan(
                      (Integer) val.get(INTEGER_ONE),
                      (Integer) val.get(INTEGER_TWO),
                      (String) val.get(INTEGER_ZERO),
                      createColSpanHeaderStyle());
                }));
    report.setMargins(INTEGER_ZERO, INTEGER_ZERO, INTEGER_ZERO, INTEGER_ZERO);
    report.setIgnorePagination(true);
    DynamicReport dynamicReport = report.build();
    dynamicReport.setProperty(JRStyledText.PROPERTY_AWT_IGNORE_MISSING_FONT, TRUE);
    JasperPrint jp =
        DynamicJasperHelper.generateJasperPrint(
            dynamicReport, new ClassicLayoutManager(), new JRBeanCollectionDataSource(listDetails));
    JRXlsxExporter xlsxExporter = new JRXlsxExporter();
    final byte[] rawBytes;

    try (ByteArrayOutputStream xlsReport = new ByteArrayOutputStream()) {
      xlsxExporter.setExporterInput(new SimpleExporterInput(jp));
      xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(xlsReport));
      xlsxExporter.setConfiguration(getSimpleXlsxReportConfiguration());
      xlsxExporter.exportReport();
      rawBytes = xlsReport.toByteArray();
    }
    return rawBytes;
  }

  private SimpleXlsxReportConfiguration getSimpleXlsxReportConfiguration() {
    SimpleXlsxReportConfiguration simpleXlsxReportConfiguration =
        new SimpleXlsxReportConfiguration();
    simpleXlsxReportConfiguration.setSheetNames(new String[] {DYNAMIC_REPORT});
    simpleXlsxReportConfiguration.setWrapText(true);
    return simpleXlsxReportConfiguration;
  }

  private AbstractColumn createColumn(String property, Class<?> type, String title, int width)
      throws ColumnBuilderException {
    return ColumnBuilder.getNew()
        .setColumnProperty(property, type.getName())
        .setTitle(title)
        .setWidth(width)
        .setStyle(createDetailTextStyle())
        .setHeaderStyle(createHeaderStyle())
        .build();
  }

  private static Style createHeaderStyle() {
    StyleBuilder sb = new StyleBuilder(true);
    sb.setFont(Font.VERDANA_MEDIUM_BOLD);
    sb.setBorder(Border.THIN());
    sb.setBorderBottom(Border.PEN_1_POINT());
    sb.setHorizontalAlign(HorizontalAlign.CENTER);
    sb.setVerticalAlign(VerticalAlign.MIDDLE);
    sb.setStretchWithOverflow(true);
    sb.setTransparency(Transparency.TRANSPARENT);
    return sb.build();
  }

  private static Style createColSpanHeaderStyle() {
    StyleBuilder sb = new StyleBuilder(true);
    sb.setFont(Font.VERDANA_MEDIUM_BOLD);
    sb.setBorder(Border.THIN());
    sb.setBorderBottom(Border.PEN_1_POINT());
    sb.setHorizontalAlign(HorizontalAlign.LEFT);
    sb.setVerticalAlign(VerticalAlign.MIDDLE);
    sb.setStretchWithOverflow(true);
    sb.setTransparency(Transparency.TRANSPARENT);

    return sb.build();
  }

  private static Style createDetailTextStyle() {
    StyleBuilder sb = new StyleBuilder(true);
    sb.setFont(Font.VERDANA_MEDIUM);
    sb.setBorder(Border.THIN());
    sb.setHorizontalAlign(HorizontalAlign.LEFT);
    sb.setVerticalAlign(VerticalAlign.JUSTIFIED);
    sb.setStretching(Stretching.RELATIVE_TO_TALLEST_OBJECT);
    return sb.build();
  }
}
