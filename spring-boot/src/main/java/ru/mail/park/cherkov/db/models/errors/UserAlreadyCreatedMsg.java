package ru.mail.park.cherkov.db.models.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.mail.park.cherkov.db.models.api.ErrorApi;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User already created")
public class UserAlreadyCreatedMsg extends RuntimeException { }
