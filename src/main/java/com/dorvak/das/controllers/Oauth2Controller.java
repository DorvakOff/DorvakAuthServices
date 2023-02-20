package com.dorvak.das.controllers;

import com.dorvak.das.DorvakAuthServices;
import com.dorvak.das.auth.PasswordManager;
import com.dorvak.das.dto.Oauth2ApplicationDto;
import com.dorvak.das.dto.TokenDto;
import com.dorvak.das.models.Oauth2Application;
import com.dorvak.das.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/oauth2")
public class Oauth2Controller {

    public static final String[] SCOPES = new String[]{
            "identify",
            "email"
    };

    @GetMapping("/authorize")
    @ResponseBody
    public Oauth2ApplicationDto authorize(@RequestParam(value = "client_id", required = false) String clientId, @RequestParam(value = "redirect_uri", required = false) String redirectUri, @RequestParam(value = "scope", required = false, defaultValue = "") String scope) {
        Oauth2Application application = DorvakAuthServices.getInstance().getOauth2ApplicationRepository().findByClientId(clientId);
        if (application == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        if (!application.getRedirectUris().contains(redirectUri)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid redirect URI");
        }
        List<String> scopes = new ArrayList<>(Arrays.stream(scope.split(" ")).map(String::toLowerCase).toList());
        scopes.retainAll(Arrays.asList(SCOPES));

        return new Oauth2ApplicationDto(scopes, clientId, redirectUri, application.getName());
    }

    @PostMapping("/token")
    @ResponseBody
    public String token(@RequestBody TokenDto body) {
        User user = DorvakAuthServices.getInstance().getUserRepository().findByUsername(body.username());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        if (!PasswordManager.checkPassword(body.password(), user.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid password");
        }
        Oauth2Application application = DorvakAuthServices.getInstance().getOauth2ApplicationRepository().findByClientId(body.clientId());
        if (application == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found");
        }
        if (!application.getRedirectUris().contains(body.redirectUri())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid redirect URI");
        }
        List<String> scopes = new ArrayList<>(body.scopes().stream().map(String::toLowerCase).toList());
        scopes.retainAll(Arrays.asList(SCOPES));
        if (scopes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid scopes");
        }
        return DorvakAuthServices.getInstance().getJtwGenerator().generateToken(user, application, scopes);
    }
}
