package com.dorvak.das.config;

public class Configuration {

    private final String databaseHost;
    private final String driverClassName;
    private final String databaseUsername;
    private final String databasePassword;

    public Configuration() {
        this.databaseHost = "jdbc:h2:file:./DAS/Data/database.db";
        this.driverClassName = "org.h2.Driver";
        this.databaseUsername = "SA";
        this.databasePassword = "";
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

}