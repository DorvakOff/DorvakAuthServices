package com.dorvak.das.auth;

import com.dorvak.das.DorvakAuthServicesApplication;
import com.dorvak.das.exceptions.AuthException;
import com.dorvak.das.mails.MailManager;
import com.dorvak.das.models.User;
import com.dorvak.das.repositories.UserRepository;

import java.util.Map;

public class AuthManager {

    public static User createUser(String username, String password, String email) throws AuthException {
        UserRepository userRepository = DorvakAuthServicesApplication.getInstance().getUserRepository();
        if (userRepository.existsByUsername(username)) {
            throw new AuthException("Username '%s' already exists", username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new AuthException("Email '%s' already exists", email);
        }

        User user = new User(username, PasswordManager.encryptPassword(password), email, "127.0.0.1");
        user = user.save();

        DorvakAuthServicesApplication.getLogger().info("Created user " + username);

        sendVerificationEmail(user);
        return user;
    }

    public static void sendVerificationEmail(User user) {
        if (user.isVerified()) {
            DorvakAuthServicesApplication.getLogger().info("User " + user.getUsername() + " is already verified");
            return;
        }
        MailManager mailManager = DorvakAuthServicesApplication.getInstance().getMailManager();

        String code = CodeGenerator.generateVerificationCode(user.getId());

        String url = "http://localhost:8080/verify/" + code;

        String subject = "Dorvak Auth Services - Email verification";

        Map<String, Object> variables = Map.of("username", user.getUsername(), "codeUrl", url);

        mailManager.sendMailWithTemplate(subject, "verification-email", variables, user.getEmail());
    }
}
