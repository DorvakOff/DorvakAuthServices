package com.dorvak.das.auth;

import org.jasypt.util.password.StrongPasswordEncryptor;

public class PasswordManager {

    private static final StrongPasswordEncryptor passwordEncryptor = new StrongPasswordEncryptor();

    public static String encryptPassword(String password) {
        return passwordEncryptor.encryptPassword(password);
    }

    public static boolean checkPassword(String password, String encryptedPassword) {
        return passwordEncryptor.checkPassword(password, encryptedPassword);
    }
}
