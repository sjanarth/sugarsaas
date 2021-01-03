package com.sugarsaas.api.preference;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface PreferenceRepository extends MongoRepository<Preference, ObjectId>
{
    @Query("{$and : [{'type': '?0'},{'seeded': ?1}]}")
    Collection<Preference> findByTypeAndSeeded(String type, Boolean Seeded);

    @Query("{$and : [{'entityId': ?0}, {'type': '?1'}]}")
    Collection<Preference> findByEntityIdAndType(Integer entityId, String type);
}
