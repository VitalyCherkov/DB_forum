package ru.mail.park.cherkov.db.models.db;

import java.sql.Timestamp;

public class PostDBModel {

    public Long id;
    public String message;
    public Boolean isEdited;
    public Long threadId = 0L;
    public Long parentId = 0L;
    public String author;
    public String forum;
    public Timestamp created;

    public PostDBModel(
            Long id,
            String message,
            Boolean isEdited,
            Long threadId,
            Long parentId,
            String author,
            String forum,
            Timestamp created
    ) {
        this.id = id;
        this.message = message;
        this.isEdited = isEdited;
        this.threadId = threadId;
        this.parentId = parentId;
        this.author = author;
        this.forum = forum;
        this.created = created;
    }
}
