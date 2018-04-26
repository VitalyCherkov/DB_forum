package ru.mail.park.cherkov.db.controllers;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.api.Thread;

import java.net.URI;
import java.util.List;

@RestController
public class ForumController {

    // private repo
    // private mapper
    // private mapperThread

    @PostMapping(value = "/api/forum/create", produces = "application/json")
    public ResponseEntity<Forum> create(
            @RequestBody Forum forum
    ) {
        ResponseEntity<Forum> entity = new ResponseEntity<Forum>(
                HttpStatus.CREATED
        );
        return entity;
    }

    @GetMapping(value = "/api/{slug}/details", produces = "application/json")
    public ResponseEntity<Forum> details(
            @PathVariable String slug
    ) {

        System.out.println("KEK LOL");

        return new ResponseEntity<Forum>(
                new Forum(
                        slug,
                        "title",
                        "user",
                        5,
                        10
                ),
                new HttpHeaders(),
                HttpStatus.OK
        );

    }

    @GetMapping(value = "/api/forum/{slug}/threads")
    public ResponseEntity<List<Thread>> threads(
            @PathVariable String slug,
            @RequestParam(required = false, defaultValue = "-1") Long limit,
            @RequestParam(required = false, defaultValue = "") String since,
            @RequestParam(required = false, defaultValue = "false") Boolean desc
    ) {
        return new ResponseEntity<List<Thread>>(
                HttpStatus.OK
        );
    }





}
