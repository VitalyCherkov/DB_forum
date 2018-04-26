package ru.mail.park.cherkov.db.models.db;

public class UserDBModel {

    public String nickname;
    public String email;
    public String fullname;
    public String about;

    public UserDBModel(String nickname, String email, String fullname, String about) {
        this.nickname = nickname;
        this.email = email;
        this.fullname = fullname;
        this.about = about;
    }
}
