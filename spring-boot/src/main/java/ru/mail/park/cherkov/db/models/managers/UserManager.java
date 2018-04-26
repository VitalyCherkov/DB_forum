package ru.mail.park.cherkov.db.models.managers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.dao.ForumDao;
import ru.mail.park.cherkov.db.dao.UserDao;
import ru.mail.park.cherkov.db.models.api.User;
import ru.mail.park.cherkov.db.models.db.UserDBModel;
import ru.mail.park.cherkov.db.models.errors.ForumNotFound;
import ru.mail.park.cherkov.db.models.errors.UserAlreadyCreated;
import ru.mail.park.cherkov.db.models.errors.UserNotFound;
import ru.mail.park.cherkov.db.models.mappers.UserMapper;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserManager {

    private static final Logger logger = LoggerFactory.getLogger(UserManager.class);

    private UserDao userDao;
    private ForumDao forumDao;
    private UserMapper userMapper;

    public UserManager(UserDao userDao, ForumDao forumDao, UserMapper userMapper) {
        this.userDao = userDao;
        this.forumDao = forumDao;
        this.userMapper = userMapper;
    }

    public User create(User user) {
        logger.debug("KEK");
        try {
            return userMapper.convert(userDao.create(user));
        }
        catch (DuplicateKeyException exception) {
            System.out.println("KEK");
            List<UserDBModel> foundUsers = userDao.getByNicknameOrEmail(user.nickname, user.email);

            throw new UserAlreadyCreated(
                    foundUsers
                            .stream()
                            .map(el -> userMapper.convert(el))
                            .collect(Collectors.toList())
            );
        }
    }

    public List <User> getList(
            String forumSlug,
            Integer limit,
            String sinceNickname,
            Boolean desc
    ) {
        // TODO: check if Forum exists
        try {
            forumDao.getDetails(forumSlug);
        }
        catch (EmptyResultDataAccessException exception) {
            throw  new ForumNotFound();
        }

        return userDao
                .getByForum(forumSlug, limit, sinceNickname, desc)
                .stream()
                .map(el -> userMapper.convert(el))
                .collect(Collectors.toList());
    }

    public User get(String nickname) {
        List<UserDBModel> users = userDao.getByNickname(nickname);
        if (users.isEmpty()) {
            throw new UserNotFound();
        }
        return userMapper.convert(users.get(0));
    }

    public User update(User user) {
        try {
            return userMapper.convert(userDao.update(user));
        }
        catch (DuplicateKeyException exception) {
            throw new UserAlreadyCreated(null);
        }
        catch (EmptyResultDataAccessException exception) {
            throw new UserNotFound();
        }
    }

}
