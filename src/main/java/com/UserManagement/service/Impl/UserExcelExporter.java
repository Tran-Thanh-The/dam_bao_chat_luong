package com.UserManagement.service.Impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.UserManagement.Dto.UserDto;
 
public class UserExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<UserDto> listUsers;
     
    public UserExcelExporter(List<UserDto> users) {
        this.listUsers = users;
        workbook = new XSSFWorkbook();
    }
 
 
    private void writeHeaderLine() {
        sheet = workbook.createSheet("Users");
         
        Row row = sheet.createRow(0);
         
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        
        createCell(row, 0, "User ID", style);
        createCell(row, 1, "Họ và tên đệm", style);
        createCell(row, 2, "Tên", style);
        createCell(row, 3, "Email", style);
        createCell(row, 4, "Tuổi", style);
        createCell(row, 5, "Số điện thoại", style);
        createCell(row, 6, "Giới tính", style);
        createCell(row, 7, "Địa chỉ", style);
         
    }
     
    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }
     
    private void writeDataLines() {
        int rowCount = 1;
 
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
                 
        for (UserDto user : listUsers) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, String.valueOf(user.getId()), style);
            createCell(row, columnCount++, String.valueOf(user.getFirstName()), style);
            createCell(row, columnCount++, String.valueOf(user.getLastName()), style);
            createCell(row, columnCount++, String.valueOf(user.getEmail()), style);
            createCell(row, columnCount++, String.valueOf(user.getAge()), style);
            createCell(row, columnCount++, String.valueOf(user.getPhone()), style);
            createCell(row, columnCount++, String.valueOf(user.getGender()), style);
            createCell(row, columnCount++, String.valueOf(user.getAddress()), style);
           
             
        }
    }
     
    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();
         
        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
//        workbook.close();
        outputStream.close();
         
    }
}