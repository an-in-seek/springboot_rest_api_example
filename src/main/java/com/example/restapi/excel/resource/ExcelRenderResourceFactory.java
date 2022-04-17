package com.example.restapi.excel.resource;

import com.example.restapi.excel.annotation.ExcelColumnName;
import com.example.restapi.excel.annotation.ExcelFileName;
import com.example.restapi.excel.util.SuperClassReflectionUtil;
import org.springframework.util.StringUtils;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.lang.reflect.Field;

public class ExcelRenderResourceFactory {

    public static ExcelRenderResource prepareRenderResource(Class<?> type) {
        String fileName = getFileName(type);
        Map<String, String> headerNamesMap = new LinkedHashMap<>();
        List<String> fieldNames = new ArrayList<>();
        for (Field field : SuperClassReflectionUtil.getAllFields(type)) {
            if (field.isAnnotationPresent(ExcelColumnName.class)) {
                ExcelColumnName annotation = field.getAnnotation(ExcelColumnName.class);
                fieldNames.add(field.getName());
                String headerName = annotation.headerName();
                headerName = StringUtils.hasText(headerName) ? headerName : field.getName();
                headerNamesMap.put(field.getName(), headerName);
            }
        }
        return new ExcelRenderResource(fileName, headerNamesMap, fieldNames);
    }

    private static String getFileName(Class<?> type) {
        String fileName = type.getSimpleName();
        if (type.isAnnotationPresent(ExcelFileName.class)) {
            fileName = type.getAnnotation(ExcelFileName.class).fileName();
            if(!StringUtils.hasText(fileName)) fileName = type.getSimpleName();
        }
        return fileName;
    }
}
