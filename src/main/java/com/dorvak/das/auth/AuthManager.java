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

    public static User createUser(String username, String password, String email, String ip, String language) throws AuthException {
        UserRepository userRepository = DorvakAuthServices.getInstance().getUserRepository();
        if (userRepository.existsByUsername(username)) {
            throw new AuthException("Username '%s' already exists", username);
        }

        if (userRepository.existsByEmail(email)) {
            throw new AuthException("Email '%s' already exists", email);
        }

        User user = new User(username, PasswordManager.encryptPassword(password), email, ip, language);
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

        String url = "https://auth.dorvak.com/verify/" + code;

        String subject = "Dorvak Auth Services - Email verification";

        Map<String, Object> variables = Map.of("username", user.getUsername(), "codeUrl", url);

        mailManager.sendMailWithTemplate(subject, "verification-email", variables, user.getEmail());
    }

    public static User login(String username, String email, String password) {
        User user = DorvakAuthServices.getInstance().getUserRepository().findByUsernameOrEmail(username, email);

        return user != null && PasswordManager.checkPassword(password, user.getPassword()) ? user : null;
    }

    public static User getUserFromToken(String token, boolean onlyUserToken) {
        JwtGenerator jtwGenerator = DorvakAuthServices.getInstance().getJtwGenerator();
        try {
            String uuid = jtwGenerator.verifyToken(token.replace("Bearer ", ""), onlyUserToken);
            return DorvakAuthServices.getInstance().getUserRepository().findById(UUID.fromString(uuid)).orElseThrow();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }

    public static void changePassword(User user, String password, String s) {
        if (!PasswordManager.checkPassword(s, user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        user.setPassword(PasswordManager.encryptPassword(password));
        user.save();
    }
}
