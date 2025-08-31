package com.teachermanagement.service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.teachermanagement.dto.TeacherDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Service for exporting teacher data to PDF and Excel formats
 */
@Service
public class ExportService {
    
    /**
     * Export teachers data to PDF format
     */
    public byte[] exportToPDF(List<TeacherDTO> teachers) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            
            // Add title
            Paragraph title = new Paragraph("Teacher Management System - Teachers Report")
                    .setFontSize(18)
                    .setBold()
                    .setTextAlignment(TextAlignment.CENTER);
            document.add(title);
            
            // Add some space
            document.add(new Paragraph(" "));
            
            // Create table
            Table table = new Table(new float[]{1, 3, 2, 2, 2});
            table.setWidth(100);
            
            // Add headers
            table.addHeaderCell(new Cell().add(new Paragraph("ID").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Full Name").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Age").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Date of Birth").setBold()));
            table.addHeaderCell(new Cell().add(new Paragraph("Number of Classes").setBold()));
            
            // Add data rows
            for (TeacherDTO teacher : teachers) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(teacher.getId()))));
                table.addCell(new Cell().add(new Paragraph(teacher.getFullName())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(teacher.getAge()))));
                table.addCell(new Cell().add(new Paragraph(teacher.getDateOfBirth().toString())));
                table.addCell(new Cell().add(new Paragraph(String.valueOf(teacher.getNumberOfClasses()))));
            }
            
            document.add(table);
            
            // Add statistics
            document.add(new Paragraph(" "));
            document.add(new Paragraph("Total Teachers: " + teachers.size()).setBold());
            
            if (!teachers.isEmpty()) {
                double avgClasses = teachers.stream()
                        .mapToInt(TeacherDTO::getNumberOfClasses)
                        .average()
                        .orElse(0.0);
                document.add(new Paragraph("Average Classes per Teacher: " + 
                        String.format("%.2f", avgClasses)).setBold());
            }
            
            document.close();
            
        } catch (Exception e) {
            throw new Exception("Error generating PDF: " + e.getMessage(), e);
        }
        
        return baos.toByteArray();
    }
    
    /**
     * Export teachers data to Excel format
     */
    public byte[] exportToExcel(List<TeacherDTO> teachers) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Teachers");
            
            // Create header style
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            
            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Full Name", "Age", "Date of Birth", "Number of Classes"};
            
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            
            // Create data rows
            int rowNum = 1;
            for (TeacherDTO teacher : teachers) {
                Row row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(teacher.getId());
                row.createCell(1).setCellValue(teacher.getFullName());
                row.createCell(2).setCellValue(teacher.getAge());
                row.createCell(3).setCellValue(teacher.getDateOfBirth().toString());
                row.createCell(4).setCellValue(teacher.getNumberOfClasses());
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }
            
            // Add statistics sheet
            Sheet statsSheet = workbook.createSheet("Statistics");
            Row statsRow1 = statsSheet.createRow(0);
            statsRow1.createCell(0).setCellValue("Total Teachers:");
            statsRow1.createCell(1).setCellValue(teachers.size());
            
            if (!teachers.isEmpty()) {
                double avgClasses = teachers.stream()
                        .mapToInt(TeacherDTO::getNumberOfClasses)
                        .average()
                        .orElse(0.0);
                
                Row statsRow2 = statsSheet.createRow(1);
                statsRow2.createCell(0).setCellValue("Average Classes per Teacher:");
                statsRow2.createCell(1).setCellValue(avgClasses);
            }
            
            statsSheet.autoSizeColumn(0);
            statsSheet.autoSizeColumn(1);
            
            workbook.write(baos);
        }
        
        return baos.toByteArray();
    }
}
