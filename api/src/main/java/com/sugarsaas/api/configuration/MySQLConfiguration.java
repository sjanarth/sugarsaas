package com.sugarsaas.api.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories({
    "com.sugarsaas.api.identity",
    "com.sugarsaas.api.tenancy",
    "com.sugarsaas.api.refdata"
})
public class MySQLConfiguration
{
}
