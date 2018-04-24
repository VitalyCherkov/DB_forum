package ru.mail.park.cherkov.db.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.api.Vote;

import java.util.List;

@RestController
public class ThreadController {

    @PostMapping(value = "/api/forum/{slug}/create", produces = "application/json")
    public ResponseEntity<Thread> create(
            @PathVariable String slug,
            @RequestBody Thread thread
    ) {
        return new ResponseEntity<Thread>(
                HttpStatus.CREATED
        );
    }

    @GetMapping(value = "/api/thread/{slug_or_id}/details", produces = "application/json")
    public ResponseEntity<Thread> details(
            @PathVariable String slug_or_id
    ) {
        return new ResponseEntity<Thread>(
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/thread/{slug_or_id}/details", produces = "application/json")
    public ResponseEntity<Thread> update(
            @PathVariable String slug_or_id,
            @RequestBody Thread thread
    ) {
        return new ResponseEntity<Thread>(
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/api/thread/{slug_or_id}/posts", produces = "application/json")
    public ResponseEntity<List<Post>> getPosts(
            @PathVariable String slug_or_id,
            @RequestParam(required = false, defaultValue = "-1") Integer limit,
            @RequestParam(required = false, defaultValue = "-1") Integer since,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "false") Boolean desc
    ) {
        return new ResponseEntity<List<Post>>(
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/thread/{slug_or_id}/vote", produces = "application/json")
    public ResponseEntity<Thread> doVote(
            @PathVariable String slug_or_id,
            @RequestBody Vote vote
    ) {
        return new ResponseEntity<Thread>(
                HttpStatus.OK
        );
    }

}
