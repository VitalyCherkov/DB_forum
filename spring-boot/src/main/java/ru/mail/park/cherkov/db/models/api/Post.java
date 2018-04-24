package ru.mail.park.cherkov.db.models.api;

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
    public Long parent;

    @JsonProperty("thread")
    public Long thread;



}
