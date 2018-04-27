package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;

public class Status {

    public Integer forum;
    public Integer post;
    public Integer thread;
    public Integer user = 0;

    public Status(
            Integer forum,
            Integer post,
            Integer thread
    ) {
        this.forum = forum;
        this.post = post;
        this.thread = thread;
    }
}
