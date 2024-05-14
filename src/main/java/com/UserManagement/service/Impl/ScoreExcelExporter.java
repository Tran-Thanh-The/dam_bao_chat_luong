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

import com.UserManagement.Dto.ScoreDto;
import com.UserManagement.Dto.UserDto;
 
public class ScoreExcelExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<ScoreDto> listScoreDto;
     
    public ScoreExcelExporter(List<ScoreDto> scores) {
        this.listScoreDto = scores;
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
        
        createCell(row, 0, "Mã sinh viên", style);
        createCell(row, 1, "Tên sinh viên", style);
        createCell(row, 2, "Khoa", style);
        createCell(row, 3, "Khóa", style);
        createCell(row, 4, "Chuyên cần", style);
        createCell(row, 5, "Bài tập lớn", style);
        createCell(row, 6, "Bài giữa kỳ", style);
        createCell(row, 7, "Bài cuối kỳ", style);
        createCell(row, 8, "Trung bình (10)", style);
        createCell(row, 9, "Trung bình (4)", style);
        createCell(row, 10, "Điểm dạng chữ", style);         
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
                 
        for (ScoreDto score : this.listScoreDto) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
             
            createCell(row, columnCount++, String.valueOf(score.getMaSinhVien()), style);
            createCell(row, columnCount++, String.valueOf(score.getTenSinhVien()), style);
            createCell(row, columnCount++, String.valueOf(score.getKhoa()), style);
            createCell(row, columnCount++, String.valueOf(score.getPhanKhoa()), style);
            createCell(row, columnCount++, String.valueOf(score.getDiemChuyenCan()), style);
            createCell(row, columnCount++, String.valueOf(score.getDiemBaiTapLon()), style);
            createCell(row, columnCount++, String.valueOf(score.getDiemKiemTraGiuaKy()), style);
            createCell(row, columnCount++, String.valueOf(score.getDiemKiemTraCuoiKy()), style);
            createCell(row, columnCount++, String.valueOf(score.getDiemTrungBinh()), style);
            createCell(row, columnCount++, String.valueOf(score.getDiemTrungBinhHe4()), style);
            createCell(row, columnCount++, String.valueOf(score.getDiemDangChu()), style);          
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