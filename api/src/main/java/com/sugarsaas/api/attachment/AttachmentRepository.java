package com.sugarsaas.api.attachment;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Set;

public interface AttachmentRepository extends MongoRepository<Attachment, ObjectId>
{
    @Query("{$and : [{'entityId': ?0}, {'type': '?1'}]}")
    Set<Attachment> findByEntityIdAndType(Integer entityId, String type);
}
