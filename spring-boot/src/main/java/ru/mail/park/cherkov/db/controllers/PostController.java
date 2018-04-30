package ru.mail.park.cherkov.db.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.api.PostDetails;
import ru.mail.park.cherkov.db.models.managers.ForumManager;
import ru.mail.park.cherkov.db.models.managers.PostManager;
import ru.mail.park.cherkov.db.models.managers.ThreadManager;
import ru.mail.park.cherkov.db.models.managers.UserManager;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@RestController
public class PostController {

    private PostManager postManager;
    private UserManager userManager;
    private ForumManager forumManager;
    private ThreadManager threadManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public PostController(PostManager postManager, UserManager userManager, ForumManager forumManager, ThreadManager threadManager) {
        this.postManager = postManager;
        this.userManager = userManager;
        this.forumManager = forumManager;
        this.threadManager = threadManager;
    }

    @GetMapping(value = "/api/thread/{slug_or_id}/posts", produces = "application/json")
    public ResponseEntity<List<Post>> getPosts(
            @PathVariable String slug_or_id,
            @RequestParam(required = false, defaultValue = "-1") Integer limit,
            @RequestParam(required = false, defaultValue = "-1") Integer since,
            @RequestParam(required = false, defaultValue = "flat") String sort,
            @RequestParam(required = false, defaultValue = "false") Boolean desc
    ) {
        return ResponseEntity
                .ok(postManager.getList(slug_or_id, limit, since, desc, sort));
    }

    @GetMapping(value = "/api/post/{id}/details")
    public ResponseEntity<PostDetails> details(
            @PathVariable Long id,
            @RequestParam(required = false) ArrayList<String> related
    ) {
        PostDetails postDetails = new PostDetails();
        postDetails.post = postManager.get(id);
        if (related == null) {
            return ResponseEntity.ok(postDetails);
        }
        if (related.contains("user")) {
            postDetails.author = userManager.get(postDetails.post.author);
        }
        if (related.contains("forum")) {
            postDetails.forum = forumManager.get(postDetails.post.forum);
        }
        if (related.contains("thread")) {
            postDetails.thread = threadManager.get(postDetails.post.thread.toString());
        }
        return ResponseEntity
                .ok(postDetails);
    }

    @PostMapping(value = "/api/post/{id}/details")
    public ResponseEntity<Post> updateMessage (
            @PathVariable Long id,
            @RequestBody Post message
    ){
        if (message == null || message.message == null) {

            return ResponseEntity
                    .ok(details(id, null).getBody().post);
        }
        message.id = id;
        return ResponseEntity
                .ok(postManager.update(message));
    }

    @PostMapping(value = "/api/thread/{slug_or_id}/create", produces = "application/json")
    public ResponseEntity<List<Post>> create(
            @PathVariable String slug_or_id,
            @RequestBody List<Post> posts
    ) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postManager.create(slug_or_id, posts));
    }

}
