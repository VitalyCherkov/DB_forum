package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Post {

    public String author = "";

    public Timestamp created = null;

    public String forum = "";

    public Long id = 0L;

    public Boolean isEdited = false;

    public String message = "";

    public Long parent = 0L;

    public Long thread = 0L;

    @JsonCreator
    public Post(
            @JsonProperty("author") String author,
            @JsonProperty("created")
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX") Timestamp created,
            @JsonProperty("forum") String forum,
            @JsonProperty("id") Long id,
            @JsonProperty("isEdited") Boolean isEdited,
            @JsonProperty("message") String message,
            @JsonProperty("parent") Long parent,
            @JsonProperty("thread") Long thread
    ) {
        this.author = author != null ? author : "";
        this.created = created;
        this.forum = forum != null ? forum : "";
        this.id = id != null ? id : 0L;
        this.isEdited = isEdited != null ? isEdited : false;
        this.message = message;
        this.parent = parent != null ? parent : 0L;
        this.thread = thread != null ? thread : 0L;
    }
}
