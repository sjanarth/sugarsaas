package com.sugarsaas.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories({
    "com.sugarsaas.api.attachment",
    "com.sugarsaas.api.preference",
    "com.sugarsaas.api.tag"})
public class MongoConfiguration
{
}
