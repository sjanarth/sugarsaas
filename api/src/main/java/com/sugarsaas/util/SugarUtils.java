package com.sugarsaas.util;

import javax.sql.DataSource;

public class SugarUtils {
    public static String getDatabaseType (DataSource dataSource) {
        String driverClassName = dataSource.getClass().getCanonicalName();
        if (driverClassName == null)    {
            return null;
        } else if (driverClassName.toLowerCase().contains("mysql")) {
            return "mysql";
        } else if (driverClassName.toLowerCase().contains("postgres"))  {
            return "postgres";
        } else if (driverClassName.toLowerCase().contains("oracle"))    {
            return "oracle";
        } else {
            //throw new Exception ("Unknown database type "+driverClassName);
            return "postgres";
        }
    }
}
