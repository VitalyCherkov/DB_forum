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

}
