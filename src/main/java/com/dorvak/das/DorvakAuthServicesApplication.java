package com.dorvak.das;

import com.dorvak.das.keys.KeyGenerator;
import com.dorvak.das.keys.KeyManager;
import com.dorvak.das.utils.FileUtils;
import com.dorvak.das.utils.MultiThreading;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

@SpringBootApplication
public class DorvakAuthServicesApplication implements CommandLineRunner {

    private static final String VERSION = "0.0.1";
    private static final Logger logger = Logger.getLogger(DorvakAuthServicesApplication.class.getName());
    private static DorvakAuthServicesApplication INSTANCE;
    private KeyManager keyManager;

    public static void main(String[] args) {
        SpringApplication.run(DorvakAuthServicesApplication.class, args);
    }

    public static DorvakAuthServicesApplication getInstance() {
        return INSTANCE;
    }

    public static String getVersion() {
        return VERSION;
    }

    public static Logger getLogger() {
        return logger;
    }

    private static void shutdown() {
        logger.info("Shutting down...");
        MultiThreading.shutdown();
    }

    @Override
    public void run(String... args) {
        FileUtils.createDirectory("DAS");
        INSTANCE = this;

        keyManager = new KeyManager();
        keyManager.addKeyGenerator("test");

        Thread shutdownHook = new Thread(DorvakAuthServicesApplication::shutdown, "Shutdown Hook");

        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
}
