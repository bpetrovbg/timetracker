package com.bobi.timetracker.utilities;

import com.bobi.timetracker.models.UserProjectTime;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.servlet.view.document.AbstractXlsView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class ExcelView extends AbstractXlsView {

    @Override
    protected void buildExcelDocument(Map<String, Object> model,
                                      Workbook workbook,
                                      HttpServletRequest request,
                                      HttpServletResponse response) throws Exception {

        // change the file name
        response.setHeader("Content-Disposition", "attachment; filename=\"reportAll.xlsx\"");

        @SuppressWarnings("unchecked")
        List<UserProjectTime> userProjectTimeList = (List<UserProjectTime>) model.get("userprojecttime");

        // create excel xls sheet
        Sheet sheet = workbook.createSheet("All data");
        sheet.setDefaultColumnWidth(30);

        // create style for header cells
        /*CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setFontName("Arial");
        style.setFillForegroundColor(HSSFColor.BLUE.index);
        //style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        //font.setBold(true);
        font.setColor(HSSFColor.BLACK.index);
        style.setFont(font);*/


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
        for(UserProjectTime userProjectTime : userProjectTimeList){
            Row userProjectTimeRow = sheet.createRow(rowCount++);
            userProjectTimeRow.createCell(0).setCellValue(userProjectTime.getUserid().getUsername());
            userProjectTimeRow.createCell(1).setCellValue(userProjectTime.getProjectid().getName());
            userProjectTimeRow.createCell(2).setCellValue(userProjectTime.getProjectid().getLocation() + " / " + userProjectTime.getLocation());
            userProjectTimeRow.createCell(3).setCellValue(String.valueOf(userProjectTime.getStarttime()));
            userProjectTimeRow.createCell(4).setCellValue(String.valueOf(userProjectTime.getEndtime()));
            userProjectTimeRow.createCell(5).setCellValue(String.valueOf(userProjectTime.getPausetime()));
            userProjectTimeRow.createCell(6).setCellValue(String.valueOf(userProjectTime.getOvertime()));
            userProjectTimeRow.createCell(7).setCellValue(String.valueOf(userProjectTime.getTotaltime()));
            userProjectTimeRow.createCell(8).setCellValue(userProjectTime.getComment());
        }
    }
}
