package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    public String about;
    public String email;
    public String fullname;
    public String nickname;

    @JsonCreator
    public User() {
        this("", "", "", "");
    }

    @JsonCreator
    public User(
            @JsonProperty("about")
            String about,
            @JsonProperty("email")
            String email,
            @JsonProperty("fullname")
            String fullname) {
        this(about, email, fullname, null);
    }

    public User(
            String about,
            String email,
            String fullname,
            String nickname
    ) {
        this.about = about;
        this.email = email;
        this.fullname = fullname;
        this.nickname = nickname;
    }
}
