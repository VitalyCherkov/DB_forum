package ru.mail.park.cherkov.db.models.mappers;

import ru.mail.park.cherkov.db.models.api.User;
import ru.mail.park.cherkov.db.models.db.UserDBModel;

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
