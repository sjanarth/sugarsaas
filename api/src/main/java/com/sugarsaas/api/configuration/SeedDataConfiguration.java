package com.sugarsaas.api.configuration;

import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.net.URL;
import java.util.Collection;

@Configuration
@PropertySource("classpath:application.properties")
public class SeedDataConfiguration
{
    @Value("${com.sugarsaas.seed-data-packages}")
    private String[] seedDataPackages;

    @Bean
    public ConfigurationBuilder seedDataConfigurationBuilder()  {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();
        Collection<URL> allSDPs = ClasspathHelper.forPackage("com.sugarsaas.api");
        for (String sdp : seedDataPackages) {
            allSDPs.addAll(ClasspathHelper.forPackage(sdp));
        }
        configurationBuilder.setUrls(allSDPs);
        configurationBuilder.setScanners(new SubTypesScanner());
        return configurationBuilder;
    }
}