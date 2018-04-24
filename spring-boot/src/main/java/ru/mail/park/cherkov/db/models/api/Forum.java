package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Forum {
    @JsonProperty("slug")
    public String slug;

    @JsonProperty("title")
    public String title;

    @JsonProperty("user")
    public String user;

    @JsonProperty("posts")
    public Integer posts = 0;

    @JsonProperty("threads")
    public Integer threads = 0;

    @JsonCreator
    public Forum(
            @JsonProperty("slug") String slug,
            @JsonProperty("title") String title,
            @JsonProperty("user")  String user,
            @JsonProperty("posts") Integer posts,
            @JsonProperty("threads") Integer threads
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
        this(slug, title, user, 0, 0);
    }
}
