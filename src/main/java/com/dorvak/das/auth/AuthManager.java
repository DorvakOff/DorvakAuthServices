package com.dorvak.das.auth;

import com.dorvak.das.DorvakAuthServices;
import com.dorvak.das.exceptions.AuthException;
import com.dorvak.das.mails.MailManager;
import com.dorvak.das.models.User;
import com.dorvak.das.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

public class AuthManager {

    public static User createUser(String username, String password, String email, String ip) throws AuthException {
        UserRepository userRepository = DorvakAuthServices.getInstance().getUserRepository();
        if (userRepository.existsByUsername(username)) {
            throw new AuthException("Username '%s' already exists", username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new AuthException("Email '%s' already exists", email);
        }

        User user = new User(username, PasswordManager.encryptPassword(password), email, ip);
        user = user.save();

        DorvakAuthServices.getLogger().info("Created user " + username);

        sendVerificationEmail(user);
        return user;
    }

    public static void sendVerificationEmail(User user) {
        if (user.isVerified()) {
            DorvakAuthServices.getLogger().info("User " + user.getUsername() + " is already verified");
            return;
        }
        MailManager mailManager = DorvakAuthServices.getInstance().getMailManager();

        String code = CodeGenerator.generateVerificationCode(user.getId());

        String url = "http://localhost:8080/verify/" + code;

        String subject = "Dorvak Auth Services - Email verification";

        Map<String, Object> variables = Map.of("username", user.getUsername(), "codeUrl", url);

        mailManager.sendMailWithTemplate(subject, "verification-email", variables, user.getEmail());
    }

    public static User login(String username, String email, String password) {
        User user = DorvakAuthServices.getInstance().getUserRepository().findByUsernameOrEmail(username, email);

        return user != null && PasswordManager.checkPassword(password, user.getPassword()) ? user : null;
    }

    public static User getUserFromToken(String token) {
        JwtGenerator jtwGenerator = DorvakAuthServices.getInstance().getJtwGenerator();
        try {
            String uuid = jtwGenerator.verifyToken(token.replace("Bearer ", ""));
            return DorvakAuthServices.getInstance().getUserRepository().findById(UUID.fromString(uuid)).orElse(null);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }
}
