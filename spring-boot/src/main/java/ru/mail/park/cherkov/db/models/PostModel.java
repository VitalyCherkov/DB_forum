package ru.mail.park.cherkov.db.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "PostDetails")
public class PostModel {

    @Id private Long id;
    private Long userid;
    private Long threadid;
    private String message;
    private Long parentid;
    private String created;
    private Boolean isedited;

    public PostModel(
            Long id,
            Long userid,
            Long threadid,
            String message,
            Long parentid,
            String created,
            Boolean isedited
    ) {
        this.id = id;
        this.userid = userid;
        this.threadid = threadid;
        this.message = message;
        this.parentid = parentid;
        this.created = created;
        this.isedited = isedited;
    }



}
