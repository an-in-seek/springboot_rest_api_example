package com.example.restapi.excel.util;

import lombok.NoArgsConstructor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor
public final class SuperClassReflectionUtil {

    // 모든 필드 가져오기
    public static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, true)) {
            fields.addAll(Arrays.asList(clazzInClasses.getDeclaredFields()));
        }
        return fields;
    }

    // 클래스로부터 어노테이션 가져오기
    public static Annotation getAnnotation(Class<?> clazz,
                                           Class<? extends Annotation> targetAnnotation) {
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, false)) {
            if (clazzInClasses.isAnnotationPresent(targetAnnotation))
                return clazzInClasses.getAnnotation(targetAnnotation);
        }
        return null;
    }

    // 클래스로부터 특정 필드 가져오기
    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        for (Class<?> clazzInClasses : getAllClassesIncludingSuperClasses(clazz, false)) {
            for (Field field : clazzInClasses.getDeclaredFields()) {
                if (field.getName().equals(name))
                    return clazzInClasses.getDeclaredField(name);
            }
        }
        throw new NoSuchFieldException();
    }

    // 클래스로부터 부모 클래스를 포함한 모든 클래스 가져오기
    private static List<Class<?>> getAllClassesIncludingSuperClasses(Class<?> clazz, boolean fromSuper) {
        List<Class<?>> classes = new ArrayList<>();
        while (clazz != null) {
            classes.add(clazz);
            clazz = clazz.getSuperclass();
        }
        if (fromSuper) Collections.reverse(classes);
        return classes;
    }
}