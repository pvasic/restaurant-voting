package ru.pvasic.restaurantvoting.web;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.pvasic.restaurantvoting.AuthUser;
import ru.pvasic.restaurantvoting.model.User;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public User get(@AuthenticationPrincipal AuthUser authUser) {
        return authUser.getUser();
    }
}
