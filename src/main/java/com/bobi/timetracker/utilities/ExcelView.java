package com.bobi.timetracker.utilities;

import com.bobi.timetracker.models.Record;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"reportAll.xls\"");

        @SuppressWarnings("unchecked")
        List<Record> recordList = (List<Record>) model.get("userprojecttime");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("All data");
        sheet.setDefaultColumnWidth(30);

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Username");
        //  header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Project");
        // header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Project location / Clock-in location");
        //  header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Starttime");
        // header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Endtime");
        //header.getCell(4).setCellStyle(style);
        header.createCell(5).setCellValue("Pauses");
        // header.getCell(5).setCellStyle(style);
        header.createCell(6).setCellValue("Overtime");
        //header.getCell(6).setCellStyle(style);
        header.createCell(7).setCellValue("Total time");
        //header.getCell(7).setCellStyle(style);
        header.createCell(8).setCellValue("Comment");

        int rowCount = 1;
        for (Record record : recordList) {
            Row userProjectTimeRow = sheet.createRow(rowCount++);
            userProjectTimeRow.createCell(0).setCellValue(record.getUser().getUsername());
            userProjectTimeRow.createCell(1).setCellValue(record.getProject().getName());
            userProjectTimeRow.createCell(2).setCellValue(record.getProject().getLocation() + " / " + record.getLocation());
            userProjectTimeRow.createCell(3).setCellValue(String.valueOf(record.getStarttime()));
            userProjectTimeRow.createCell(4).setCellValue(String.valueOf(record.getEndtime()));
            userProjectTimeRow.createCell(5).setCellValue(String.valueOf(record.getPausetime()));
            userProjectTimeRow.createCell(6).setCellValue(String.valueOf(record.getOvertime()));
            userProjectTimeRow.createCell(7).setCellValue(String.valueOf(record.getTotaltime()));
            userProjectTimeRow.createCell(8).setCellValue(record.getComment());
        }
    }
}
