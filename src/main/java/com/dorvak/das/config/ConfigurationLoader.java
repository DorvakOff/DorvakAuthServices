package com.dorvak.das.config;

import com.dorvak.das.DorvakAuthServices;
import com.dorvak.das.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    public Configuration load() throws IOException {
        FileUtils.createDirectory("DAS");
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
            if (file.createNewFile()) {
                DorvakAuthServices.getLogger().info("Created new config file");
            }
        }
        updateConfiguration();
        DorvakAuthServices.getLogger().info("Loaded config file");
        return configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void updateConfiguration() {
        File file = new File("DAS/config.json");
        try (FileWriter writer = new FileWriter(file)) {
            if (file.exists() & file.createNewFile()) {
                DorvakAuthServices.getLogger().info("Updated config file");
            }
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(configuration);
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
