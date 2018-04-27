package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Vote {
    public String  nickname;
    public Integer voice;

    @JsonCreator
    public Vote(
            @JsonProperty("nickname") String nickname,
            @JsonProperty("voice") Integer voice
    ) {
        this.nickname = nickname;
        this.voice = voice;
    }
}
