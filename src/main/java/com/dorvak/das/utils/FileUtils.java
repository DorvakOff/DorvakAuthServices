package com.dorvak.das.utils;

import com.dorvak.das.DorvakAuthServicesApplication;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists() && directory.mkdirs()) {
            DorvakAuthServicesApplication.getLogger().info("Created new directory: " + directoryName);
        }
    }

    public static boolean fileExists(String path, String fileName) {
        return new File(path, fileName).exists();
    }

    public static void generateOrOverwriteFile(String path, String fileName) throws IOException {
        File file = new File(path, fileName);
        if (file.exists() && file.delete()) {
            DorvakAuthServicesApplication.getLogger().info("Deleted file: " + fileName);
        }
        if (file.createNewFile()) {
            DorvakAuthServicesApplication.getLogger().info("Created new file: " + fileName);
        }
    }

    public static void generateFile(String path, String fileName) throws IOException {
        File file = new File(path, fileName);
        if (!file.exists() && file.createNewFile()) {
            DorvakAuthServicesApplication.getLogger().info("Created new file: " + fileName);
        }
    }
}
