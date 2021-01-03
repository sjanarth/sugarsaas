package com.sugarsaas.api.core;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Slf4j
public class AbstractNoSQLServiceImpl
{
    protected Optional<Object> getEntityId(Object target, Class cls) {
        Class targetClass = target.getClass();
        if (targetClass.isAnnotationPresent(cls)) {
            SupportsFeature entity = AnnotationUtils.findAnnotation(targetClass, SupportsFeature.class);
            try {
                String idFieldName = entity.id();
                Field f = targetClass.getDeclaredField(idFieldName);
                f.setAccessible(true);
                Object entityId = f.get(target);
                return Optional.ofNullable(entityId);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return Optional.empty();
    }

    protected Optional<String> getEntityType(Object target, Class cls) {
        Class targetClass = target.getClass();
        if (targetClass.isAnnotationPresent(cls)) {
            SupportsFeature entity = AnnotationUtils.findAnnotation(targetClass, SupportsFeature.class);
            String entityType = entity.type();
            if (entityType.equals(""))
                entityType = targetClass.getSimpleName().toUpperCase();
            return Optional.ofNullable(entityType);
        }
        return Optional.empty();
    }
}