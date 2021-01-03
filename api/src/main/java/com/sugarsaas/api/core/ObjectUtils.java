package com.sugarsaas.api.core;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@Slf4j
public class ObjectUtils
{
    private static final List<Class> EXCLUDED_FIELD_ANNOTATIONS =
            Arrays.asList(new Class[] {
                    Id.class, EmbeddedId.class, GeneratedValue.class
            });

    public static void shallowCopy (Object from, Object to)  {
        if (from == null || to == null) {
            throw new NullPointerException("Source and destination objects must be non-null");
        }
        Class fromClass = from.getClass();
        Class toClass = to.getClass();
        List<String> copiedFields = new ArrayList<>();
        for (Field f : fromClass.getDeclaredFields()) {
            try {
                if (isExcluded(f)) continue;
                Field t = toClass.getDeclaredField(f.getName());
                if (t.getType() == f.getType()) {
                    if (isSupportedType(f.getType())) {
                        copiedFields.add(f.getName());
                        f.setAccessible(true);
                        t.setAccessible(true);
                        t.set(to, f.get(from));
                    } else if (t.getType() == Date.class) {
                        // dates are not immutable, so clone non-null dates into the destination object
                        copiedFields.add(f.getName());
                        Date d = (Date) f.get(from);
                        f.setAccessible(true);
                        t.setAccessible(true);
                        t.set(to, d != null ? d.clone() : null);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        String fromClassName = fromClass.getCanonicalName();
        if (!typesCopied.contains(fromClassName))    {
            log.info("shallowCopy called for "+fromClassName);
            for(String fieldName : copiedFields)
                log.info("  -> "+fieldName);
            typesCopied.add(fromClassName);
        }
    }

    private static boolean isExcluded(Field field)  {
        for (Class cls : EXCLUDED_FIELD_ANNOTATIONS)    {
            if (field.isAnnotationPresent(cls)) return true;
        }
        return Modifier.isFinal(field.getModifiers());
    }

    private static final List<Class> BOXED_CLASSES =
            Arrays.asList(new Class[] {
                    Byte.class, Character.class,
                    Short.class, Integer.class, Long.class, Float.class, Double.class
            });

    private static final List<Class> BASE_CLASSES =
            Arrays.asList(new Class[] {
                    Number.class, Collection.class,
                    Entity.class
            });

    private static boolean isSupportedType(Class cls) {
        if (cls.isPrimitive()) return true;
        if (cls.isSynthetic()) return false;
        if (String.class.equals(cls)) return true;
        if (BOXED_CLASSES.contains(cls)) return true;
        if (BASE_CLASSES.contains(cls.getSuperclass())) return true;
        Class componentType = cls.getComponentType();
        for (Class bc : BASE_CLASSES) {
            if (cls.isAnnotationPresent(bc) || Arrays.asList(cls.getInterfaces()).contains(bc))
                return true;
            if ((componentType != null) &&
                (componentType.isAnnotationPresent(bc) || (Arrays.asList(componentType.getInterfaces()).contains(bc))))
                return true;
        }
        return false;
    }

    private static Set<String> typesCopied = new HashSet<>();
}