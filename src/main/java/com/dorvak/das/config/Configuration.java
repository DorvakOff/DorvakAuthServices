package com.dorvak.das.config;

public class Configuration {

    // Database
    private final String databaseUrl;
    private final String driverClassName;
    private final String databaseUsername;
    private final String databasePassword;

    // Mail
    private final String mailHost;
    private final String mailPort;
    private final String mailUsername;
    private final String mailAddress;
    private final String mailPassword;

    public Configuration() {
        this.databaseUrl = "jdbc:h2:file:./DAS/Data/database.db";
        this.driverClassName = "org.h2.Driver";
        this.databaseUsername = "SA";
        this.databasePassword = "";
        this.mailHost = "example.com";
        this.mailPort = "587";
        this.mailAddress = "no-reply@example.com";
        this.mailUsername = "DorvakAuthServices";
        this.mailPassword = "password";
    }

    public String getDatabaseHost() {
        return databaseUrl;
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

    public String getMailHost() {
        return mailHost;
    }

    public String getMailPort() {
        return mailPort;
    }

    public String getMailUsername() {
        return mailUsername;
    }

    public String getMailPassword() {
        return mailPassword;
    }

    public String getMailAddress() {
        return mailAddress;
    }
}