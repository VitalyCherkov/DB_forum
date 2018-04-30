package ru.mail.park.cherkov.db.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.api.Vote;
import ru.mail.park.cherkov.db.models.managers.ThreadManager;

import java.util.List;

@RestController
public class ThreadController {

    private ThreadManager threadManager;

    public ThreadController(ThreadManager threadManager) {
        this.threadManager = threadManager;
    }

    @GetMapping(value = "/api/forum/{slug}/threads")
    public ResponseEntity<List<Thread>> threads(
            @PathVariable String slug,
            @RequestParam(required = false, defaultValue = "-1") Integer limit,
            @RequestParam(required = false) String since,
            @RequestParam(required = false, defaultValue = "false") Boolean desc
    ) {
        return ResponseEntity
                .ok(threadManager.getByForum(slug, limit, since, desc));
    }

    @PostMapping(value = "/api/forum/{slug}/create", produces = "application/json")
    public ResponseEntity<Thread> create(
            @PathVariable String slug,
            @RequestBody Thread thread
    ) {
        thread.forum = slug;
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(threadManager.create(thread));
    }

    @GetMapping(value = "/api/thread/{slug_or_id}/details", produces = "application/json")
    public ResponseEntity<Thread> details(
            @PathVariable String slug_or_id
    ) {
        return ResponseEntity
                .ok(threadManager.get(slug_or_id));
    }

    @PostMapping(value = "/api/thread/{slug_or_id}/details", produces = "application/json")
    public ResponseEntity<Thread> update(
            @PathVariable String slug_or_id,
            @RequestBody Thread thread
    ) {
        return ResponseEntity
                .ok(threadManager.update(slug_or_id, thread));
    }

    @PostMapping(value = "/api/thread/{slug_or_id}/vote", produces = "application/json")
    public ResponseEntity<Thread> doVote(
            @PathVariable String slug_or_id,
            @RequestBody Vote vote
    ) {
        return ResponseEntity
                .ok(threadManager.doVote(slug_or_id, vote));
    }

}
