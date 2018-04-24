package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("about")
    public String about;

    @JsonProperty("email")
    public String email;

    @JsonProperty("fullname")
    public String fullname;

    @JsonProperty("nickname")
    public String nickname;

    public User(
            @JsonProperty("about") String about,
            @JsonProperty("email") String email,
            @JsonProperty("fullname") String fullname,
            @JsonProperty("nickname") String nickname
    ) {
        this.about = about;
        this.email = email;
        this.fullname = fullname;
        this.nickname = nickname;
    }
}
