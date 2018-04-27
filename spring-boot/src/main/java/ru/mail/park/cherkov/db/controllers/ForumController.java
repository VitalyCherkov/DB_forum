package ru.mail.park.cherkov.db.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.managers.ForumManager;

import java.net.URI;
import java.util.List;

@RestController
public class ForumController {

    private static final Logger logger = LoggerFactory.getLogger(ForumController.class);

    private ForumManager forumManager;

    public ForumController(ForumManager forumManager) {
        this.forumManager = forumManager;
    }

    @PostMapping(value = "/api/forum/create", produces = "application/json")
    public ResponseEntity<Forum> create(
            @RequestBody Forum forum
    ) {
        logger.debug(forum.toString());
        return new ResponseEntity<>(
                forumManager.create(forum),
                HttpStatus.CREATED
        );
    }

    @GetMapping(value = "/api/forum/{slug}/details", produces = "application/json")
    public ResponseEntity<Forum> details(
            @PathVariable String slug
    ) {
        logger.debug(slug);

        return new ResponseEntity<>(
                forumManager.get(slug),
                HttpStatus.OK
        );

    }

}
