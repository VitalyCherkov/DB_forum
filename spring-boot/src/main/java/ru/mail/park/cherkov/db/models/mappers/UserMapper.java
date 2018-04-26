package ru.mail.park.cherkov.db.models.mappers;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.User;
import ru.mail.park.cherkov.db.models.db.UserDBModel;

@Component
public class UserMapper implements IMapper <UserDBModel, User> {

    public User convert(UserDBModel userDBModel) {
        return new User(
                userDBModel.about,
                userDBModel.email,
                userDBModel.fullname,
                userDBModel.nickname
        );
    }
}
