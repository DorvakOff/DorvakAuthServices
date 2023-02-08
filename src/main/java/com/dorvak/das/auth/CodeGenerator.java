package com.dorvak.das.auth;

import com.dorvak.das.utils.cache.ExpirableConcurrentHashMap;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CodeGenerator {

    public static final int VERIFICATION_CODE_LENGTH = 128;

    private static final Map<String, String> verificationCodes = new ExpirableConcurrentHashMap<>(15, TimeUnit.MINUTES);

    public static String generateVerificationCode(String id) {
        StringBuilder code = new StringBuilder();
        Random r = new Random();
        char ch;

        if (verificationCodes.containsValue(id)) {
            verificationCodes.entrySet().removeIf(entry -> entry.getValue().equals(id));
        }

        for (int i = 0; i < VERIFICATION_CODE_LENGTH; i++) {
            int type = r.nextInt(3);
            switch (type) {
                case 0 -> {
                    // 65 = A, 90 = Z
                    ch = (char) (r.nextInt(26) + 65);
                    code.append(ch);
                }
                case 1 -> {
                    // 97 = a, 122 = z
                    ch = (char) (r.nextInt(26) + 97);
                    code.append(ch);
                }
                // 48 = 0, 57 = 9
                case 2 -> code.append(r.nextInt(10));
            }
        }
        String result = code.toString();
        if (verificationCodes.containsKey(result)) {
            return generateVerificationCode(id);
        }
        verificationCodes.put(result, id);
        return result;
    }

    public static boolean verifyCode(String code, String id) {
        if (!verificationCodes.containsKey(code)) {
            return false;
        }
        if (verificationCodes.get(code).equals(id)) {
            verificationCodes.remove(code);
            return true;
        } else {
            return false;
        }
    }
}
