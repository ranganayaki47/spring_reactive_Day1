/*
package com.cognizant.feedback.service;

import com.cognizant.feedback.dto.EventSummaryDTO;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelToDBService {

    @Autowired
    private ResourceLoader resourceLoader;

    public void readEventSummaryFile(File file){
        Resource resource = resourceLoader.getResource(file.getAbsolutePath());
        InputStream in;
        try {
            in = resource.getInputStream();
            List<EventSummaryDTO> eventSummaryList = new ArrayList<EventSummaryDTO>();

            XSSFWorkbook workbook = new XSSFWorkbook(in);
            XSSFSheet worksheet = workbook.getSheetAt(0);

            for(int i=1;i<worksheet.getPhysicalNumberOfRows() ;i++) { // Test tempStudent

                XSSFRow row = worksheet.getRow(i);
                System.out.println("val:"+row.getCell(0).getStringCellValue());
                EventSummaryDTO event = new EventSummaryDTO();
                event.setEventID(row.getCell(0).getStringCellValue());
                event.setMonth(row.getCell(1).getStringCellValue());
                event.setBaseLocation(row.getCell(2).getStringCellValue());
                event.setBeneficiaryName(row.getCell(3).getStringCellValue());
                event.setVenueAddress(row.getCell(4).getStringCellValue());
                event.setCouncilName(row.getCell(5).getStringCellValue());
                event.setProject(row.getCell(6).getStringCellValue());
                event.setCategory(row.getCell(7).getStringCellValue());
                event.setEventName(row.getCell(8).getStringCellValue());
                event.setEventDescription(row.getCell(9).getStringCellValue());
                event.setEventDate(row.getCell(10).getStringCellValue());
                event.setTotalNoOfVolunteers((int)row.getCell(11).getNumericCellValue());
                event.setTotalVolunteerHours((int)row.getCell(12).getNumericCellValue());
                event.setTotalTravelHours((int)row.getCell(13).getNumericCellValue());
                event.setOverallVolunteerHours((int)row.getCell(14).getNumericCellValue());
                event.setLivesImpacted((int)row.getCell(15).getNumericCellValue());
                event.setActivityType((int)row.getCell(16).getNumericCellValue());
                event.setStatus(row.getCell(17).getStringCellValue());
                event.setPocID((int)row.getCell(18).getNumericCellValue());
                event.setPocName(row.getCell(19).getStringCellValue());
                event.setPocContactNumber((int)row.getCell(20).getNumericCellValue());
                eventSummaryList.add(event);

            }
            Flux<EventSummaryDTO> eventFlux =   eventSummaryRepository.saveAll(eventSummaryList).log();
            eventFlux.subscribe(x -> System.out.println("saved id:"+x.getEventId()));
            //  Mono<Test> fetchedItem =	dataRepository.findByEventName("test1").log();
            //	fetchedItem.subscribe(System.out::println);
            //Mono<Test> saveItem = dataRepository.save( new Test("test2")).log();
            //    saveItem.subscribe(x -> System.out.println("saved id:"+x.getEventId()));
            //  saveItem.map((item) -> item.getEventId());
            //   System.out.println("savedId:"+saveItem.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
*/
