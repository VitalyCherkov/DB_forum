package ru.mail.park.cherkov.db.models.db;

public class ForumDBModel {

    public String slug;
    public String title;
    public String author;
    public Integer posts = 0;
    public Integer threads = 0;
    public Long forumId = 0L;

    public ForumDBModel(
            String slug,
            String title,
            String author,
            Integer posts,
            Integer threads
    ) {
        this.slug = slug;
        this.title = title;
        this.author = author;
        this.posts = posts;
        this.threads = threads;
    }
}
