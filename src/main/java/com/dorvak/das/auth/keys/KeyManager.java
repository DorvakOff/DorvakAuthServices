package com.dorvak.das.auth.keys;

import com.dorvak.das.DorvakAuthServices;

import java.util.HashMap;
import java.util.Map;

public class KeyManager {

    private final Map<String, KeyGenerator> keyGenerators;

    public KeyManager() {
        keyGenerators = new HashMap<>();
    }

    public KeyGenerator addKeyGenerator(String name) {
        try {
            keyGenerators.put(name, new KeyGenerator(name));
        } catch (Exception e) {
            e.printStackTrace();
            DorvakAuthServices.getLogger().severe("Failed to create key generator for " + name);
        }
        return keyGenerators.get(name);
    }

    public KeyGenerator addKeyGenerator(Class<?> clazz) {
        return this.addKeyGenerator(clazz.getSimpleName());
    }
}
