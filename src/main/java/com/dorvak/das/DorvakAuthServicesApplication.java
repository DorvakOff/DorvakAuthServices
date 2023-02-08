package com.dorvak.das;

import com.dorvak.das.auth.keys.KeyManager;
import com.dorvak.das.utils.MultiThreading;
import com.dorvak.das.utils.cache.CacheUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.logging.Logger;

@SpringBootApplication
public class DorvakAuthServicesApplication implements CommandLineRunner {

    private static final Logger logger = Logger.getLogger(DorvakAuthServicesApplication.class.getName());
    private static DorvakAuthServicesApplication INSTANCE;
    private KeyManager keyManager;

    @Autowired
    private ConfigurableApplicationContext ctx;

    public static void main(String[] args) {
        SpringApplication.run(DorvakAuthServicesApplication.class, args);
    }

    public static DorvakAuthServicesApplication getInstance() {
        return INSTANCE;
    }

    public static Logger getLogger() {
        return logger;
    }

    private void shutdown() {
        logger.info("Shutting down...");
        if (ctx.isRunning()) {
            ctx.stop();
        }
        MultiThreading.shutdown();
    }

    @Override
    public void run(String... args) {
        INSTANCE = this;
        CacheUtils.init();

        keyManager = new KeyManager();
        keyManager.addKeyGenerator("test");

        Thread shutdownHook = new Thread(this::shutdown, "Shutdown Hook");
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public KeyManager getKeyManager() {
        return keyManager;
    }
}
