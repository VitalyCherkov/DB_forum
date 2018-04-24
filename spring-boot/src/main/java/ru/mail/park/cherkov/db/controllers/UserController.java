package ru.mail.park.cherkov.db.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.User;

import java.util.List;

@RestController
public class UserController {

    @GetMapping(value = "/api/forum/{slug}/users", produces = "application/json")
    public ResponseEntity<List<User>> getForumUsers(
            @PathVariable String slug,
            @RequestParam(required = false, defaultValue = "-1") Long limit,
            @RequestParam(required = false, defaultValue = "") String since,
            @RequestParam(required = false, defaultValue = "false") Boolean desc
    ) {
        return new ResponseEntity<List<User>>(
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/user/{nickname}/create", produces = "application/json")
    public ResponseEntity<User> createUser(
            @PathVariable String nickname,
            @RequestBody(required = false) User user
    ) {
        return new ResponseEntity<User>(
                HttpStatus.CREATED
        );
    }

    @GetMapping(value = "/api/user/{nickname}/profile", produces = "application/json")
    public ResponseEntity<User> getUser(
            @PathVariable String nickname
    ) {
        return new ResponseEntity<User>(
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/user/{nickname}/profile", produces = "application/json")
    public ResponseEntity<User> updateUser(
            @PathVariable String nickname,
            @RequestBody User user
    ) {
        return new ResponseEntity<User>(
                HttpStatus.OK
        );
    }
    
}
