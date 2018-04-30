package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Forum {

    public String slug;
    public String title;
    public String user;
    public Integer posts = 0;
    public Integer threads = 0;

    public Forum(
            String slug,
            String user,
            String title,
            Integer posts,
            Integer threads
    ) {
        this.slug = slug;
        this.title = title;
        this.user = user;
        this.posts = posts;
        this.threads = threads;
    }

    @JsonCreator
    public Forum(
            @JsonProperty("slug") String slug,
            @JsonProperty("title") String title,
            @JsonProperty("user")  String user
    ) {
        this.slug = slug;
        this.title = title;
        this.user = user;
        this.posts = 0;
        this.threads = 0;
    }
}
