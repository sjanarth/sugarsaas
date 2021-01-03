package com.sugarsaas.api.tag;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Set;

public interface TagRepository extends MongoRepository<Tag, ObjectId>
{
    @Query("{$and : [{'entityId': ?0}, {'type': '?1'}]}")
    Set<Tag> findByEntityIdAndType(Integer entityId, String type);
}
