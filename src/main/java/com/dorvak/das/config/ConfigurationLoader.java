package com.dorvak.das.config;

import com.dorvak.das.DorvakAuthServicesApplication;
import com.google.gson.Gson;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

@org.springframework.context.annotation.Configuration
public class ConfigurationLoader {

    private Configuration configuration;

    public ConfigurationLoader() {
    }

    @Bean
    public Configuration load() {
        File file = new File("DAS/config.json");
        if (file.exists()) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(file)) {
                configuration = gson.fromJson(reader, Configuration.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            configuration = new Configuration();
        }
        updateConfiguration();
        DorvakAuthServicesApplication.getLogger().info("Loaded config file");
        return configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void updateConfiguration() {
        File file = new File("DAS/config.json");
        try (FileWriter writer = new FileWriter(file)) {
            if (file.exists() & file.createNewFile()) {
                DorvakAuthServicesApplication.getLogger().info("Updated config file");
            }
            Gson gson = new Gson();
            String json = gson.toJson(configuration);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
