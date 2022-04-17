package com.example.restapi.excel.service;

import com.example.restapi.excel.resource.ExcelRenderResource;
import com.example.restapi.excel.resource.ExcelRenderResourceFactory;
import com.example.restapi.excel.util.SuperClassReflectionUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.util.ObjectUtils;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.lang.reflect.Field;

public class ExcelService<T> {

    private final Workbook workbook;
    private final Sheet sheet;
    private final ExcelRenderResource resource;
    private final List<T> dataList;
    private int rowIndex = 0;

    // 생성자
    public ExcelService(List<T> dataList, Class<T> type) {
        this.workbook = new SXSSFWorkbook();
        this.sheet = workbook.createSheet();
        this.resource = ExcelRenderResourceFactory.prepareRenderResource(type);
        this.dataList = dataList;
    }

    // 엑셀 파일을 다운로드합니다.
    public void downloadExcel(HttpServletResponse response) throws NoSuchFieldException, IllegalAccessException, IOException {
        createHead();
        createBody();
        writeExcel(response);
    }

    // 헤더에 데이터를 넣어줍니다.
    private void createHead() {
        Row row = sheet.createRow(rowIndex++);
        int columnIndex = 0;
        for (String dataFieldName : resource.getDataFieldNames()) {
            Cell cell = row.createCell(columnIndex++);
            String value = resource.getExcelHeaderName(dataFieldName);
            cell.setCellValue(value);
        }
    }

    // 바디에 데이터를 넣어줍니다.
    private void createBody() throws NoSuchFieldException, IllegalAccessException {
        for (T data : dataList) {
            Row row = sheet.createRow(rowIndex++);
            int columnIndex = 0;
            for (String dataFieldName : resource.getDataFieldNames()) {
                Cell cell = row.createCell(columnIndex++);
                Field field = SuperClassReflectionUtil.getField(data.getClass(), (dataFieldName));
                field.setAccessible(true);
                Object cellValue = field.get(data);
                field.setAccessible(false);
                setCellValue(cell, cellValue);
            }
        }
    }

    // 컨텐츠 타입과 파일명을 지정하고 엑셀 파일을 출력합니다.
    private void writeExcel(HttpServletResponse response) throws IOException {
        String fileName = new String(resource.getExcelFileName().getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        response.setContentType("ms-vnd/excel");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s.xlsx\"", fileName));
        workbook.write(response.getOutputStream());
        workbook.close();
    }

    // 셀에 값을 넣어줍니다.
    private void setCellValue(Cell cell, Object cellValue) {
        if (cellValue instanceof Number) {
            Number numberValue = (Number) cellValue;
            cell.setCellValue(numberValue.doubleValue());
            return;
        }
        cell.setCellValue(ObjectUtils.isEmpty(cellValue) ? "" : String.valueOf(cellValue));
    }
}
