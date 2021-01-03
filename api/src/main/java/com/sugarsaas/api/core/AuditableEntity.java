package com.sugarsaas.api.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

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
}