package com.example.restapi.excel.resource;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class ExcelRenderResource {

    private final String excelFileName;
    private final Map<String, String> excelHeaderNames;
    private final List<String> dataFieldNames;

    public ExcelRenderResource(String excelFileName, Map<String, String> excelHeaderNames, List<String> dataFieldNames) {
        this.excelFileName = excelFileName;
        this.excelHeaderNames = excelHeaderNames;
        this.dataFieldNames = dataFieldNames;
    }

    public String getExcelHeaderName(String dataFieldName) {
        return excelHeaderNames.get(dataFieldName);
    }
}
