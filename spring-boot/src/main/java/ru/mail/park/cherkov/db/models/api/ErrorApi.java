package ru.mail.park.cherkov.db.models.api;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ErrorApi {
    @JsonProperty("message")
    public String message;
}
