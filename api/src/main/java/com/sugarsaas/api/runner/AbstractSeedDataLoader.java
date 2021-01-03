package com.sugarsaas.api.runner;

import org.reflections.Reflections;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

public abstract class AbstractSeedDataLoader
{
    @Autowired
    protected ConfigurationBuilder configurationBuilder;

    protected <T> Set<Class<? extends T>> scanProviders(Class<T> type) {
        return new Reflections(configurationBuilder).getSubTypesOf(type);
    }
}
