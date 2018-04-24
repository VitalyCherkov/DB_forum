package ru.mail.park.cherkov.db.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "User")
public class UserModel {

    @Id private Long id;
    private String nickname;
    private String email;
    private String fullname;
    private String about;

    public UserModel(Long id, String nickname, String email, String fullname, String about) {
        this.id = id;
        this.nickname = nickname;
        this.email = email;
        this.fullname = fullname;
        this.about = about;
    }

    public UserModel() {
        this.id = 0L;
        this.nickname = null;
        this.email = null;
        this.fullname = null;
        this.about = null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return Objects.equals(id, userModel.id) &&
                Objects.equals(nickname, userModel.nickname) &&
                Objects.equals(email, userModel.email) &&
                Objects.equals(fullname, userModel.fullname) &&
                Objects.equals(about, userModel.about);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, nickname, email, fullname, about);
    }
}
