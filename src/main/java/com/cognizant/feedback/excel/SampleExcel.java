//package com.cognizant.feedback.excel;
//
//import com.cognizant.feedback.dto.UserDTO;
//import com.cognizant.feedback.exceptions.FMSException;
//import com.cognizant.feedback.properties.ExcelProperties;
//import com.cognizant.feedback.repository.UserRepository;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.web.JsonPath;
//import org.springframework.stereotype.Service;
//
//import java.util.*;
//
//@Slf4j
//@Service
//@AllArgsConstructor
//public class SampleExcel {
//
//    private final UserRepository userRepository;
//    private final PricingFactory pricingFactory;
//    private final ExportExcelService exportService;
//    private final ExcelProperties excelProperties;
//    private final CommandFactory commandFactory;
//
//    public Map fetchDataForExcelFileExport(String fileName) {
//        Pricing pricingFactoryData = pricingFactory.fetchPricingFactoryData(searchEntity);
//        return Optional.ofNullable(pricingFactoryData)
//                .map(factoryData -> fetchExcelData(searchQuery, searchEntity, factoryData,timezone))
//                .orElseThrow(() -> new JBHValidationException(INVALID_SEARCH_ENTITY));
//    }
//
//    private HashMap fetchExcelData(List<UserDTO> userDTOs) {
//        String responseAsString = fetchElasticData(searchQuery, searchEntity, factoryData);
//        List<ExcelFileExportDTO> excelFileExportDTOList = factoryData.getFieldDto();
//        Map<String, List<Object>> entityField = doExcelColumnMapping(excelFileExportDTOList,timezone);
//        List<Map<String, Object>> rows = new ArrayList<>();
//        DocumentContext responseContext = JsonPath.parse(responseAsString);
//        List<String> respList = responseContext.read("$.hits.hits[*]", List.class);
//        Integer respListSize = validateListSize(respList);
//        Map<String, Object> sortingRow = new HashMap<>();
//        for (int i = 0; i < respListSize; i++) {
//            Map<String, Object> columnVal = new HashMap<>();
//            for (ExcelFileExportDTO excelExportDTO : excelFileExportDTOList) {
//                Command mapperCmd = commandFactory.getCommand(excelExportDTO.getMapperType());
//                MapperRequest mapperRequest = new MapperRequest(responseContext, i, excelExportDTO);
//                Response response = mapperCmd.execute(mapperRequest);
//                columnVal.put(excelExportDTO.getExcelFieldName(), response.getResponse());
//                if (factoryData.isSortingRowDuplicates() && i == 0) {
//                    if (excelExportDTO.getDataType().equals(DATATYPE_INTEGER)) {
//                        sortingRow.put(excelExportDTO.getExcelFieldName(), 0);
//                    } else if (excelExportDTO.getDataType().equals(DATATYPE_BIG_DECIMAL)) {
//                        sortingRow.put(excelExportDTO.getExcelFieldName(), BigDecimal.valueOf(0));
//                    } else if (excelExportDTO.getDataType().equals(DATATYPE_STRING)) {
//                        sortingRow.put(excelExportDTO.getExcelFieldName(), "--");
//                    }
//                }
//            }
//            rows.add(columnVal);
//        }
//        if (!sortingRow.isEmpty())
//            rows.add(0, sortingRow);
//
//        return Optional.ofNullable(entityField)
//                .map(entity -> export(rows, entity, factoryData.isSortingRowDuplicates()))
//                .orElseThrow(() -> new JBHValidationException(COLUMN_MAPPING_EXCEPTION));
//    }
//
//    public Map formExcelColumnHeaders(List<ExcelFileExportDTO> excelFileExportDTOList) {
//        Map<String, List<Object>> entityField = doExcelColumnMapping(excelFileExportDTOList,null);
//        return Optional.ofNullable(entityField)
//                .map(entity -> export(null, entity, false))
//                .orElseThrow(() -> new JBHValidationException(COLUMN_MAPPING_EXCEPTION));
//    }
//
//    public HashMap export(List<?> result, Map<String, List<Object>> entity, boolean removeDuplicateRow) {
//        try {
//            HashMap hashMap = exportService.exportToExcelFile(entity, result,null, Optional.empty());
//            if (removeDuplicateRow) {
//                hashMap.put("byte", Optional.ofNullable(hashMap).map(map -> {
//                    try {
//                        return removeSortingRow((byte[]) map.get("byte"));
//                    } catch (IOException e) {
//                        throw new JBHValidationException(EXPORT_TO_EXCEL_FAILED);
//                    }
//                }).orElseThrow(() -> new JBHValidationException(EXPORT_TO_EXCEL_FAILED)));
//            }
//            return hashMap;
//        } catch (JRException | IOException e) {
//            log.error(e.getMessage(), e);
//            throw new JBHValidationException(EXPORT_TO_EXCEL_FAILED);
//        }
//    }
//
//    private byte[] removeSortingRow(byte[] rawBytes) throws IOException {
//        final byte[] removedArray;
//        InputStream inputStream = new ByteArrayInputStream(rawBytes);
//        try (Workbook workbook = WorkbookFactory.create(inputStream)){
//            inputStream.close();
//            Sheet sheet = workbook.getSheetAt(0);
//            if (sheet.getRow(1) != null)
//                sheet.getRow(1).setZeroHeight(true);
//
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            workbook.write(byteArrayOutputStream);
//            removedArray = byteArrayOutputStream.toByteArray();
//            byteArrayOutputStream.close();
//        }
//        return removedArray;
//    }
//
//    private Integer validateListSize(List<String> resultSet) {
//        return Optional.of(resultSet)
//                .filter(list -> Integer.compare(list.size(), excelProperties.getMaximumRowsExcelLimit()) <= 0)
//                .map(List::size)
//                .orElseThrow(() -> new FMSException(ROWS_EXCEED_ALLOWED_EXCEL_SHEET_MAX_LIMIT));
//    }
//
//    private Object deriveDataType(String dataTypeStringFromConfig) {
//        if (DATA_TYPE_MAPPER.containsKey(dataTypeStringFromConfig)) {
//            return DATA_TYPE_MAPPER.get(dataTypeStringFromConfig);
//        }
//        return null;
//    }
//
//    private Map<String, List<Object>> doExcelColumnMapping(
//            List<ExcelFileExportDTO> excelFileExportDTOList, TimeZone timezone) {
//        Map<String, List<Object>> exportExcel = new LinkedHashMap<>();
//
//        excelFileExportDTOList.forEach(
//                excelExportDTO ->
//                        exportExcel.put(
//                                excelExportDTO.getExcelFieldName(),
//                                Arrays.asList(
//                                        excelExportDTO.getExcelFieldName(),
//                                        deriveDataType(excelExportDTO.getDataType()).getClass(),
//                                        excelExportDTO.getLength(),
//                                        excelExportDTO.isFlag(),
//                                        getFormatter(excelExportDTO.getFormatter(),timezone))));
//        return exportExcel;
//    }
//
//    private Format getFormatter(String formatter, TimeZone timezone) {
//
//        if (Objects.nonNull(formatter)) {
//            Command formatCmd = commandFactory.getCommand(formatter);
//            return ((WrappedResponse<Format>) formatCmd.execute(new WrappedRequest<TimeZone>(timezone))).getResponse();
//        }
//        return null;
//    }
//
//}
