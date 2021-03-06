package ru.mail.park.cherkov.db.controllers;

import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.User;
import ru.mail.park.cherkov.db.models.managers.UserManager;
import ru.mail.park.cherkov.db.models.mappers.UserMapper;

import java.util.List;

@RestController
public class UserController {

    private UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @GetMapping(value = "/api/forum/{slug}/users", produces = "application/json")
    public ResponseEntity<List<User>> getForumUsers(
            @PathVariable String slug,
            @RequestParam(required = false, defaultValue = "-1") Integer limit,
            @RequestParam(required = false, defaultValue = "") String since,
            @RequestParam(required = false, defaultValue = "false") Boolean desc
    ) {
        return ResponseEntity
                .ok(userManager.getList(slug, limit, since, desc));
    }

    @PostMapping(value = "/api/user/{nickname}/create", produces = "application/json")
    public ResponseEntity<User> createUser(
            @PathVariable String nickname,
            @RequestBody(required = false) User user
    ) {
        if (user == null) {
            user = new User();
        }
        user.nickname = nickname;
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userManager.create(user));
    }

    @GetMapping(value = "/api/user/{nickname}/profile", produces = "application/json")
    public ResponseEntity<User> getUser(
            @PathVariable String nickname
    ) {
        return ResponseEntity
                .ok(userManager.get(nickname));
    }

    @PostMapping(value = "/api/user/{nickname}/profile", produces = "application/json")
    public ResponseEntity<User> updateUser(
            @PathVariable String nickname,
            @RequestBody User user
    ) {
        user.nickname = nickname;
        return ResponseEntity
                .ok(userManager.update(user));
    }

}
