package com.dorvak.das.utils;

import com.dorvak.das.DorvakAuthServicesApplication;
import com.dorvak.das.exceptions.ApplicationException;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUtils {

    public static final String USERS_DIRECTORY = "DAS/users";

    public static void createDirectory(String directoryName) {
        File directory = new File(directoryName);
        if (!directory.exists() && directory.mkdirs()) {
            DorvakAuthServicesApplication.getLogger().info("Created new directory: " + directoryName);
        }
    }

    public static boolean fileExists(String path, String fileName) {
        return new File(path, fileName).exists();
    }

    public static void generateFile(String path, String fileName) throws IOException {
        File file = new File(path, fileName);
        if (!file.exists() && file.createNewFile()) {
            DorvakAuthServicesApplication.getLogger().info("Created new file: " + fileName);
        }
    }

    public static File getAvatarFile(UUID uuid, String fileName, boolean useDefault) throws ApplicationException {
        File file = new File(USERS_DIRECTORY, new File(uuid.toString(), "avatar/" + fileName).toString());
        if (!file.exists()) {
            if (!useDefault) {
                throw new ApplicationException("Avatar file not found: " + fileName);
            }
            file = new File("DAS/resources/users/avatars", "default.jpg");
        }
        return file;
    }

    public static File getAvatarFile(UUID uuid, String fileName) {
        return getAvatarFile(uuid, fileName, true);
    }

    public static String getFileExtension(String file) {
        return file.substring(file.lastIndexOf(".") + 1);
    }

    public static String getFileExtension(File file) {
        return getFileExtension(file.getName());
    }

    public static void saveFile(String uploadDir, String fileName, MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = multipartFile.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }

    public static void deleteFile(File file) {
        if (file.exists() && !file.delete()) {
            DorvakAuthServicesApplication.getLogger().warning("Failed to delete file: " + file.getName());
        }
    }
}
