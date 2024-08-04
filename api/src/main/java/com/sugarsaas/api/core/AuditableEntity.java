package com.sugarsaas.api.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.*;

import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Set;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity
{
    @CreatedDate
    @Column(name="time_created", updatable=false)
    @JsonProperty(access=JsonProperty.Access.READ_ONLY)
    @Temporal(TemporalType.TIMESTAMP)
    protected Date createdTime;

    @LastModifiedDate
    @Column(name="time_updated")
    @Temporal(TemporalType.TIMESTAMP)
    protected Date updatedTime;

    public String getKey()   {
       getIdField();
       if (idField != null) {
           try {
               idField.setAccessible(true);
               Object key = idField.get(this);
               return key != null ? key.toString() : null;
           } catch (IllegalAccessException e) {
               throw new RuntimeException(e);
           }
       }
       return null;
    }

    public String getKeyColumnName()   {
        getIdField();
        return idField != null ? idField.getName() : null;
    }

    private void getIdField()   {
        if (idField == null) {
            Reflections reflections = new Reflections(this.getClass(), new FieldAnnotationsScanner());
            Set<Field> fields = reflections.getFieldsAnnotatedWith(Id.class);
            idField = fields.isEmpty() ? null : fields.stream().findFirst().get();
        }
        /*
        System.out.println("class="+getClass());
        Field[] fields = getClass().getFields();
        System.out.println("fields="+fields.length);
        for (Field f : getClass().getFields())   {
            System.out.println("f="+f);
            for (Annotation a : f.getAnnotations())  {
                System.out.println("   a="+a);
                if (a instanceof Id)   {
                    return f;
                }
            }
        }
        return null;
        */
    }

    @Transient
    private Field idField = null;
}