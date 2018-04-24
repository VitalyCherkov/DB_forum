package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Thread {

    @JsonProperty("author")
    public String author;

    @JsonProperty("created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    public String created;

    @JsonProperty("forum")
    public String forum;

    @JsonProperty("id")
    public Long id = -1L;

    @JsonProperty("message")
    public String message;

    @JsonProperty("slug")
    public String slug = "";

    @JsonProperty("title")
    public String title;

    @JsonProperty("votes")
    public Integer votes = 0;

    @JsonCreator
    public Thread(
            @JsonProperty("author") String author,
            @JsonProperty("created") String created,
            @JsonProperty("forum") String forum,
            @JsonProperty("id") Long id,
            @JsonProperty("message") String message,
            @JsonProperty("slug") String slug,
            @JsonProperty("title") String title,
            @JsonProperty("votes") Integer votes
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
