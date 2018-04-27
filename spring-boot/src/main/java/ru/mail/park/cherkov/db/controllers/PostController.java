package ru.mail.park.cherkov.db.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.api.PostDetails;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.managers.ForumManager;
import ru.mail.park.cherkov.db.models.managers.PostManager;
import ru.mail.park.cherkov.db.models.managers.ThreadManager;
import ru.mail.park.cherkov.db.models.managers.UserManager;

import java.util.List;

@RestController
public class PostController {

    private PostManager postManager;
    private UserManager userManager;
    private ForumManager forumManager;
    private ThreadManager threadManager;

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
            @RequestParam(required = false) String sort,
            @RequestParam(required = false, defaultValue = "false") Boolean desc
    ) {
        return new ResponseEntity<List<Post>>(
                postManager.getList(slug_or_id, limit, since, desc, sort),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/api/post/{id}/details")
    public ResponseEntity<PostDetails> details(
            @PathVariable Long id,
            @RequestParam(required = false) List<String> intrestedIn
    ) {

        PostDetails postDetails = new PostDetails();
        postDetails.post = postManager.get(id);

        if (intrestedIn.contains("user")) {
            postDetails.author = userManager.get(postDetails.post.author);
        }

        if (intrestedIn.contains("forum")) {
            postDetails.forum = forumManager.get(postDetails.post.forum);
        }

        if (intrestedIn.contains("thread")) {
            postDetails.thread = threadManager.get(postDetails.post.threadId.toString());
        }

        return new ResponseEntity<PostDetails>(
                postDetails,
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/post/{id}/details")
    public ResponseEntity<Post> updateMessage (
            @PathVariable Long id,
            @RequestBody Post message
    ){
        if (message == null || message.message == null) {
            return new ResponseEntity<Post>(
                    details(id, null).getBody().post,
                    HttpStatus.OK
                    );
        }

        message.id = id;
        return new ResponseEntity<Post>(
                postManager.update(message),
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/thread/{slug_or_id}/create", produces = "application/json")
    public ResponseEntity<List<Post>> create(
            @PathVariable String slug_or_id,
            @RequestBody List<Post> posts
    ) {
        return new ResponseEntity<List<Post>>(
                postManager.create(slug_or_id, posts),
                HttpStatus.CREATED
        );
    }

}
