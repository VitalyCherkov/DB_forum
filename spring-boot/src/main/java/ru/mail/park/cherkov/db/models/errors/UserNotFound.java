package ru.mail.park.cherkov.db.models.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.mail.park.cherkov.db.models.api.ErrorApi;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found")
public class UserNotFound extends RuntimeException implements IMessageContainerError {

    public ErrorApi error;

    public UserNotFound() {
        error = new ErrorApi("User not found");
    }

    @Override
    public Object getCustomMessage() {
        return error;
    }
}
