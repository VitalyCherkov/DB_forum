package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class PostDetails {

    @JsonProperty("author")
    public User author;

    @JsonProperty("forum")
    public Forum forum;

    @JsonProperty("post")
    public Post post;

    @JsonProperty("thread")
    public Thread thread;

    @JsonCreator
    public PostDetails(
            @JsonProperty("author") User author,
            @JsonProperty("forum") Forum forum,
            @JsonProperty("post") Post post,
            @JsonProperty("thread") Thread thread
    ) {
        this.author = author;
        this.forum = forum;
        this.post = post;
        this.thread = thread;
    }
}
