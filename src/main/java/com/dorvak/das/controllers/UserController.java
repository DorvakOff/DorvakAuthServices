package com.dorvak.das.controllers;

import com.dorvak.das.DorvakAuthServicesApplication;
import com.dorvak.das.auth.AuthManager;
import com.dorvak.das.dto.LoginDto;
import com.dorvak.das.dto.UserDto;
import com.dorvak.das.exceptions.AuthException;
import com.dorvak.das.models.User;
import com.dorvak.das.utils.Checks;
import com.dorvak.das.utils.FileUtils;
import com.dorvak.das.utils.ImageUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/user")
public class UserController {


    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginDto) {
        User user = AuthManager.login(loginDto.username(), loginDto.email(), loginDto.password());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        return DorvakAuthServicesApplication.getInstance().getJtwGenerator().generateToken(user);
    }

    @PostMapping("/register")
    public String register(@RequestBody LoginDto loginDto) {
        Checks.notNull(loginDto.username(), "username");
        Checks.notNull(loginDto.password(), "password");
        Checks.notNull(loginDto.email(), "email");
        try {
            User user = AuthManager.createUser(loginDto.username(), loginDto.password(), loginDto.email());
            return DorvakAuthServicesApplication.getInstance().getJtwGenerator().generateToken(user);
        } catch (AuthException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/data")
    public UserDto getUserData(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        User user = AuthManager.getUserFromToken(token);
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

    @PostMapping("/avatar")
    public void setAvatar(@RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam("avatar") MultipartFile avatar) {
        User user = AuthManager.getUserFromToken(token);
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(avatar.getOriginalFilename()));
        String oldAvatar = user.getAvatarFile();
        user.setAvatarFile(fileName);

        String uploadDir = FileUtils.USERS_DIRECTORY + "/" + user.getId().toString() + "/avatar";

        try {
            FileUtils.saveFile(uploadDir, fileName, avatar);
            if (!Objects.equals(oldAvatar, fileName)) {
                FileUtils.deleteFile(FileUtils.getAvatarFile(user.getId(), oldAvatar, false));
            }
            user.save();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
