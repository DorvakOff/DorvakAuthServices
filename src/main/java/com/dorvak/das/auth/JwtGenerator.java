package com.dorvak.das.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.dorvak.das.DorvakAuthServices;
import com.dorvak.das.auth.keys.KeyGenerator;
import com.dorvak.das.auth.keys.KeyManager;
import com.dorvak.das.models.User;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.sql.Date;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.UUID;

public class JwtGenerator {

    private final Algorithm algorithm;
    private final JWTVerifier verifier;
    private final TemporalAmount expirationTime;

    public JwtGenerator(KeyManager keyManager) {
        KeyGenerator keyGenerator = keyManager.addKeyGenerator(this.getClass());
        this.algorithm = Algorithm.RSA256((RSAPublicKey) keyGenerator.getPublicKey(), (RSAPrivateKey) keyGenerator.getPrivateKey());
        this.verifier = JWT.require(algorithm).withIssuer(DorvakAuthServices.APPLICATION_NAME).build();
        this.expirationTime = Duration.ofDays(7);
    }

    public String generateToken(User user) throws JWTCreationException {
        return JWT.create()
                .withIssuer(DorvakAuthServices.APPLICATION_NAME)
                .withClaim("uuid", user.getId().toString())
                .withExpiresAt(Date.from(Instant.now().plus(expirationTime)))
                .sign(algorithm);
    }

    public String generateToken(String uuid) {
        return this.generateToken(UUID.fromString(uuid));
    }

    public String generateToken(UUID uuid) {
        return this.generateToken(DorvakAuthServices.getInstance().getUserRepository().findById(uuid).orElseThrow());
    }

    public String verifyToken(String token) throws JWTVerificationException {
        return verifier.verify(token).getClaim("uuid").asString();
    }
}
