package com.sugarsaas.api.loader;

import com.sugarsaas.util.SugarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Order(1)
@Component
public class SugarSchemaLoader
{
    private static final Logger logger = LoggerFactory.getLogger(SugarSchemaLoader.class);

    @Bean(name="SugarSchemaLoader")
    public DataSourceInitializer dataSourceInitializer(@Qualifier("dataSource") final DataSource dataSource) {
        ResourceDatabasePopulator resourceDatabasePopulator = new ResourceDatabasePopulator();
        try {
            // First load the Sugar schema itself
            String createSQLScript = "sugar_create_" + SugarUtils.getDatabaseType(dataSource) + ".sql";
            logger.info("SchemaLoader running " + createSQLScript);
            resourceDatabasePopulator.addScript(new ClassPathResource("/db/" + createSQLScript));
        } catch (Exception ex)  {
            ex.printStackTrace(System.err);
        }
        DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();
        dataSourceInitializer.setDataSource(dataSource);
        dataSourceInitializer.setDatabasePopulator(resourceDatabasePopulator);
        return dataSourceInitializer;
    }
}
