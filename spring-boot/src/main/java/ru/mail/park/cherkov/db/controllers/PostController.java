package ru.mail.park.cherkov.db.controllers;

import com.sun.xml.internal.xsom.impl.scd.Iterators;
import javafx.geometry.Pos;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.api.PostDetails;

import java.util.List;

@RestController
public class PostController {

    @GetMapping(value = "/api/post/{id}/details")
    public ResponseEntity<PostDetails> details(
            @PathVariable Long id,
            @RequestParam(required = false) Iterators.Array<String> related
    ) {
        return new ResponseEntity<PostDetails>(
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/post/{id}/details")
    public ResponseEntity<Post> updateMessage (
            @PathVariable Long id,
            @RequestBody Post message
    ){
        return new ResponseEntity<Post>(
                HttpStatus.OK
        );
    }

    @PostMapping(value = "/api/thread/{slug_or_id}/create", produces = "application/json")
    public ResponseEntity<List<Post>> create(
            @PathVariable String slug_or_id,
            @RequestBody List<Post> posts
    ) {
        return new ResponseEntity<List<Post>>(
                HttpStatus.CREATED
        );
    }

}
