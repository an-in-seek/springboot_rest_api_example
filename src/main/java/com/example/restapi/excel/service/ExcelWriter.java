package com.example.restapi.excel.service;

import com.example.restapi.excel.resource.ExcelRenderResource;
import com.example.restapi.excel.resource.ExcelRenderResourceFactory;
import com.example.restapi.excel.util.SuperClassReflectionUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.lang.reflect.Field;

public class ExcelWriter<T> {

    private final Workbook workbook;
    private final Sheet sheet;
    private final ExcelRenderResource resource;
    private final List<T> dataList;
    private int rowIndex = 0;

    public ExcelWriter(List<T> dataList, Class<T> type) {
        // 엑셀 파일 하나를 만듭니다
        this.workbook = new SXSSFWorkbook();
        this.sheet = workbook.createSheet();
        this.resource = ExcelRenderResourceFactory.prepareRenderResource(type);
        this.dataList = dataList;
    }

    public void createExcel(HttpServletResponse response) throws NoSuchFieldException, IllegalAccessException, IOException {
        // 엑셀 헤더 생성
        createHead();
        // 엑셀 바디 생성
        createBody(dataList);
        // 엑셀 쓰기
        writeExcel(response);
    }

    // 헤더를 생성합니다
    private void createHead() {
        Row row = sheet.createRow(rowIndex++);
        int columnIndex = 0;
        for (String dataFieldName : resource.getDataFieldNames()) {
            Cell cell = row.createCell(columnIndex++);
            String value = resource.getExcelHeaderName(dataFieldName);
            cell.setCellValue(value);
        }
    }

    // 바디에 데이터를 넣어줍니다
    private void createBody(List<T> dataList) throws NoSuchFieldException, IllegalAccessException {
        for (T data : dataList) {
            Row row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            for (String dataFieldName : resource.getDataFieldNames()) {
                Cell cell = row.createCell(columnIndex++);
                Field field = SuperClassReflectionUtils.getField(data.getClass(), (dataFieldName));
                field.setAccessible(true);
                Object cellValue = field.get(data);
                field.setAccessible(false);
                renderCellValue(cell, cellValue);
            }
        }
    }

    // 컨텐츠 타입과 파일명 지정
    private void writeExcel(HttpServletResponse response) throws IOException {
        String fileName = new String(resource.getExcelFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setContentType("ms-vnd/excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s.xlsx\"", fileName));
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    //
    private void renderCellValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Number) {
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(cellValue == null ? "" : cellValue.toString());
    }
}
