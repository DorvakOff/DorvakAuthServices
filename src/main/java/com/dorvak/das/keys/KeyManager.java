package com.dorvak.das.keys;

import com.dorvak.das.DorvakAuthServicesApplication;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

public class KeyManager {

    private final Map<String, KeyGenerator> keyGenerators;

    public KeyManager() {
        keyGenerators = new HashMap<>();
    }

    public void addKeyGenerator(String name) {
        try {
            keyGenerators.put(name, new KeyGenerator(name));
        } catch (Exception e) {
            e.printStackTrace();
            DorvakAuthServicesApplication.getLogger().severe("Failed to create key generator for " + name);
        }
    }

    public PublicKey getPublicKey(String name) {
        if (!keyGenerators.containsKey(name)) {
            return null;
        }
        return keyGenerators.get(name).getPublicKey();
    }

    public PrivateKey getPrivateKey(String name) {
        if (!keyGenerators.containsKey(name)) {
            return null;
        }
        return keyGenerators.get(name).getPrivateKey();
    }

}
