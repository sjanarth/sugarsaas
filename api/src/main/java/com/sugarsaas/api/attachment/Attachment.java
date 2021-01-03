package com.sugarsaas.api.attachment;

import com.sugarsaas.api.core.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="sugar_attachments")
public class Attachment extends AuditableEntity
{
    @Id
    private ObjectId id;

    private Integer entityId;

    private String type;

    private String name;

    private Binary data;
}
