package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorApi {
    public String message;

    @JsonCreator
    public ErrorApi(
            @JsonProperty(value = "message") String message
    ) {
        this.message = message;
    }
}
