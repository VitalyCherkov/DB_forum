package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Status {

    @JsonProperty("forum")
    public Integer forum;

    @JsonProperty("post")
    public Integer post;

    @JsonProperty("threadId")
    public Integer thread;

    @JsonProperty("user")
    public Integer user;

}
