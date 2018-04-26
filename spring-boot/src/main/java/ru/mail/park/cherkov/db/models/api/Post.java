package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Post {

    @JsonProperty("author")
    public String author;

    @JsonProperty("created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public Timestamp created;

    @JsonProperty("forum")
    public String forum;

    @JsonProperty("id")
    public Long id;

    @JsonProperty("isEdited")
    public Boolean isEdited;

    @JsonProperty("message")
    public String message;

    @JsonProperty("parent")
    public Long parentId;

    @JsonProperty("thread")
    public Long threadId;

    @JsonCreator
    public Post(
            @JsonProperty("author") String author,
            @JsonProperty("created") Timestamp created,
            @JsonProperty("forum") String forum,
            @JsonProperty("id") Long id,
            @JsonProperty("isEdited") Boolean isEdited,
            @JsonProperty("message") String message,
            @JsonProperty("parent") Long parentId,
            @JsonProperty("thread") Long threadId
    ) {
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.id = id;
        this.isEdited = isEdited;
        this.message = message;
        this.parentId = parentId;
        this.threadId = threadId;
    }
}
