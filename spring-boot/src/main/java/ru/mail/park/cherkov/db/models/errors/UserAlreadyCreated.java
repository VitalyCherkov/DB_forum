package ru.mail.park.cherkov.db.models.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.mail.park.cherkov.db.models.api.User;

import java.util.List;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "User already created")
public class UserAlreadyCreated extends RuntimeException {

    private List<User> users;

    public UserAlreadyCreated(List<User> users) {
        this.users = users;
    }

    public List<User> getUsers() {
        return users;
    }
}
