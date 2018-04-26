package ru.mail.park.cherkov.db.models.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Post already created")
public class PostAlreadyCreated extends RuntimeException { }
