package com.dorvak.das.utils;

import org.imgscalr.Scalr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static byte[] toByteArray(BufferedImage bi, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        return baos.toByteArray();

    }

    public static BufferedImage toBufferedImage(byte[] bytes) throws IOException {
        InputStream is = new ByteArrayInputStream(bytes);
        return ImageIO.read(is);
    }

    public static byte[] getImageAsBytes(File file, String size) throws IOException {
        int sizeInt = Integer.parseInt(size.replaceAll("[^0-9]", ""));
        return getImageAsBytes(file, sizeInt);
    }

    public static byte[] getImageAsBytes(File file, int size) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return ImageUtils.toByteArray(Scalr.resize(image, size), FileUtils.getFileExtension(file));
    }

    public static byte[] getImageAsBytes(File file) throws IOException {
        BufferedImage image = ImageIO.read(file);
        return ImageUtils.toByteArray(image, FileUtils.getFileExtension(file));
    }
}