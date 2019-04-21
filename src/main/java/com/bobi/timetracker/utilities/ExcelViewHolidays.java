package com.bobi.timetracker.utilities;

import com.bobi.timetracker.models.Holiday;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

public class ExcelViewHolidays extends ExcelView {
    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {
        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"reportAllHolidays.xls\"");

        @SuppressWarnings("unchecked")
        List<Holiday> holidayList = (List<Holiday>) model.get("holidays");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("All holidays");
        sheet.setDefaultColumnWidth(30);

        // create header row
        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("Username");
        //  header.getCell(0).setCellStyle(style);
        header.createCell(1).setCellValue("Startdate");
        // header.getCell(1).setCellStyle(style);
        header.createCell(2).setCellValue("Enddate");
        //  header.getCell(2).setCellStyle(style);
        header.createCell(3).setCellValue("Description");
        // header.getCell(3).setCellStyle(style);
        header.createCell(4).setCellValue("Approved?");

        int rowCount = 1;
        for (Holiday holiday : holidayList ) {
            Row userProjectTimeRow = sheet.createRow(rowCount++);
            userProjectTimeRow.createCell(0).setCellValue(holiday.getUser().getUsername());
            userProjectTimeRow.createCell(1).setCellValue(holiday.getStartdate());
            userProjectTimeRow.createCell(2).setCellValue(holiday.getEnddate());
            userProjectTimeRow.createCell(3).setCellValue(holiday.getDescription());
            if(holiday.getApproved() == Boolean.TRUE) {
                userProjectTimeRow.createCell(4).setCellValue("YES");
            } else {
                userProjectTimeRow.createCell(4).setCellValue("NO");
            }
        }
    }
}
