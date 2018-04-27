package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class PostDetails {

    public User author = null;
    public Forum forum = null;
    public Post post = null;
    public Thread thread = null;

    public PostDetails() { }

    public PostDetails(
            User author,
            Forum forum,
            Post post,
            Thread thread
    ) {
        this.author = author;
        this.forum = forum;
        this.post = post;
        this.thread = thread;
    }
}
