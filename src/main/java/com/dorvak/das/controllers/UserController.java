package com.dorvak.das.controllers;

import com.dorvak.das.DorvakAuthServices;
import com.dorvak.das.auth.AuthManager;
import com.dorvak.das.auth.PasswordManager;
import com.dorvak.das.dto.LoginDto;
import com.dorvak.das.dto.UserDto;
import com.dorvak.das.exceptions.AuthException;
import com.dorvak.das.models.User;
import com.dorvak.das.repositories.UserRepository;
import com.dorvak.das.utils.FileUtils;
import com.dorvak.das.utils.ImageUtils;
import com.dorvak.das.utils.IpUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/data")
    public UserDto getUserData(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        User user = AuthManager.getUserFromToken(token, true);
        return new UserDto(user);
    }

    @GetMapping(value = "/avatar/{uuid}/{avatar}", produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getAvatar(@PathVariable UUID uuid, @PathVariable String avatar, @RequestParam(required = false, defaultValue = "128") String size) {
        File avatarFile = FileUtils.getAvatarFile(uuid, avatar);
        try {
            return ImageUtils.getImageAsBytes(avatarFile, size);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/avatar")
    public void setAvatar(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("avatar") MultipartFile avatar) {
        User user = AuthManager.getUserFromToken(token, true);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(avatar.getOriginalFilename()));
        String oldAvatar = user.getAvatarFile();
        user.setAvatarFile(fileName);

        String uploadDir = FileUtils.USERS_DIRECTORY + "/" + user.getId().toString() + "/avatar";

        try {
            FileUtils.saveFile(uploadDir, fileName, avatar);
            if (!Objects.equals(oldAvatar, fileName)) {
                try {
                    FileUtils.deleteFile(FileUtils.getAvatarFile(user.getId(), oldAvatar, false));
                } catch (Exception ignored) {
                }
            }
            user.save();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        User user = userRepository.findUserByEmail(loginDto.email());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        if (!PasswordManager.checkPassword(loginDto.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String ip = IpUtils.getIp(request);
        user.setLastLogin(Instant.now());
        user.setLastLoginIp(ip);
        user.save();
        return DorvakAuthServices.getInstance().getJtwGenerator().generateToken(user);
    }

    @PostMapping("/create-account")
    public String createAccount(@RequestBody LoginDto loginDto, HttpServletRequest request) {
        String ip = IpUtils.getIp(request);
        try {
            User user = AuthManager.createUser(loginDto.username(), loginDto.password(), loginDto.email(), ip, loginDto.language());
            return DorvakAuthServices.getInstance().getJtwGenerator().generateToken(user);
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PatchMapping("/change-password")
    @ResponseBody
    public ResponseEntity<Void> changePassword(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody LoginDto loginDto) {
        User user = AuthManager.getUserFromToken(token, true);
        AuthManager.changePassword(user, loginDto.password(), loginDto.oldPassword());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/update-language")
    @ResponseBody
    public ResponseEntity<Void> changeLanguage(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody LoginDto loginDto) {
        User user = AuthManager.getUserFromToken(token, true);
        user.setLanguage(loginDto.language());
        user.save();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
