package com.sugarsaas.api.preference;

import com.sugarsaas.api.core.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Id;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="sugar_preferences")
public class Preference extends AuditableEntity
{
    @Id
    private ObjectId id;

    private Integer entityId;

    private String type;

    private String name;

    private String description;

    private String value;

    private boolean seeded = true;
}
