package com.dorvak.das.config;

public class Configuration {

    private String databaseHost;
    private String driverClassName;
    private String databaseUsername;
    private String databasePassword;

    public Configuration() {
        this.databaseHost = "jdbc:h2:file:~/DemoDB";
        this.driverClassName = "org.h2.Driver";
        this.databaseUsername = "SA";
        this.databasePassword = "";
    }

    public String getDatabaseHost() {
        return databaseHost;
    }

    public void setDatabaseHost(String databaseHost) {
        this.databaseHost = databaseHost;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public void setDatabaseUsername(String databaseUsername) {
        this.databaseUsername = databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public void setDatabasePassword(String databasePassword) {
        this.databasePassword = databasePassword;
    }
}
