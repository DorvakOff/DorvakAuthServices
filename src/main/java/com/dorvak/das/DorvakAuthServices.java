package com.dorvak.das;

import com.dorvak.das.auth.JwtGenerator;
import com.dorvak.das.auth.keys.KeyManager;
import com.dorvak.das.config.Configuration;
import com.dorvak.das.config.ConfigurationLoader;
import com.dorvak.das.mails.MailManager;
import com.dorvak.das.repositories.UserRepository;
import com.dorvak.das.utils.MultiThreading;
import com.dorvak.das.utils.cache.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.thymeleaf.spring5.SpringTemplateEngine;

import java.util.logging.Logger;

@SpringBootApplication
public class DorvakAuthServices implements CommandLineRunner {

    public static final String APPLICATION_NAME = "DorvakAuthServices";
    private static DorvakAuthServices INSTANCE;

    @Autowired
    private ConfigurableApplicationContext ctx;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfigurationLoader configurationLoader;

    @Autowired
    private SpringTemplateEngine thymeleafTemplateEngine;

    private MailManager mailManager;
    private JwtGenerator jtwGenerator;

    public static void main(String[] args) {
        SpringApplication.run(DorvakAuthServices.class, args);
    }

    public static DorvakAuthServices getInstance() {
        return INSTANCE;
    }

    public static Logger getLogger() {
        String clazz = Thread.currentThread().getStackTrace()[2].getClassName();
        return Logger.getLogger(clazz);
    }

    private void shutdown() {
        getLogger().info("Shutting down...");
        if (ctx.isRunning()) {
            ctx.stop();
        }
        MultiThreading.shutdown();
    }

    @Override
    public void run(String... args) {
        INSTANCE = this;
        initManagers();

        getLogger().info("Started up successfully!");

        Thread shutdownHook = new Thread(this::shutdown, "Shutdown Hook");
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    private void initManagers() {
        CacheUtils.init();
        mailManager = new MailManager(thymeleafTemplateEngine);
        KeyManager keyManager = new KeyManager();
        jtwGenerator = new JwtGenerator(keyManager);
    }

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public Configuration getConfiguration() {
        return configurationLoader.getConfiguration();
    }

    public MailManager getMailManager() {
        return mailManager;
    }

    public JwtGenerator getJtwGenerator() {
        return jtwGenerator;
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*").allowedMethods("*");
            }
        };
    }
}
