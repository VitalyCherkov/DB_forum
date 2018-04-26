package ru.mail.park.cherkov.db.models.db;

import java.sql.Timestamp;

public class ThreadDBModel {

    public String author;
    public String message;
    public String slug;
    public String title;
    public Timestamp created;
    public Integer votes = 0;
    public String forum;
    public Long id;

    public ThreadDBModel(String author, String message, String slug, String title, Timestamp created, Integer votes, String forum, Long id) {
        this.author = author;
        this.message = message;
        this.slug = slug;
        this.title = title;
        this.created = created;
        this.votes = votes;
        this.forum = forum;
        this.id = id;
    }
}
