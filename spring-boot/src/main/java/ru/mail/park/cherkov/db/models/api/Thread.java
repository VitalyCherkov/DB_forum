package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.sql.Timestamp;

public class Thread {

    public String author;
    public Timestamp created;
    public String forum;
    public Long id = -1L;
    public String message;
    public String slug = "";
    public String title;
    public Integer votes = 0;

    public Thread(
            @JsonProperty("author")
            String author,
            @JsonProperty("created")
            @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            Timestamp created,
            @JsonProperty("message")
            String message,
            @JsonProperty("title")
            String title
    ) {
        this.author = author;
        this.created = created;
        this.message = message;
        this.title = title;
    }

    @JsonCreator
    public Thread(
            @JsonProperty("message") String message,
            @JsonProperty("title") String title
    ) {
        this.message = message;
        this.title = title;
    }

    public Thread(
            String author,
            Timestamp created,
            String forum,
            Long id,
            String message,
            String slug,
            String title,
            Integer votes
    ) {
        this.author = author;
        this.created = created;
        this.forum = forum;
        this.id = id;
        this.message = message;
        this.slug = slug;
        this.title = title;
        this.votes = votes;
    }
}
