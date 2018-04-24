package ru.mail.park.cherkov.db.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "Forum")
public class ForumModel {

    @Id private Long id;
    private String slug;
    private String title;
    private Long userid;
    private Integer posts;
    private Integer threads;

    public ForumModel(Long id, String slug, String title, Long userid, Integer posts, Integer threads) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.userid = userid;
        this.posts = posts;
        this.threads = threads;
    }

    public ForumModel(Long id, String slug, String title, Long userid) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.userid = userid;
        this.posts = 0;
        this.threads = 0;
    }

    public ForumModel() {
        this.id = 0L;
        this.slug = null;
        this.title = null;
        this.userid = null;
        this.posts = 0;
        this.threads = 0;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getUserid() {
        return userid;
    }

    public void setUserid(Long userid) {
        this.userid = userid;
    }

    public Integer getPosts() {
        return posts;
    }

    public void setPosts(Integer posts) {
        this.posts = posts;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ForumModel that = (ForumModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(slug, that.slug) &&
                Objects.equals(title, that.title) &&
                Objects.equals(userid, that.userid) &&
                Objects.equals(posts, that.posts) &&
                Objects.equals(threads, that.threads);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, slug, title, userid, posts, threads);
    }
}
