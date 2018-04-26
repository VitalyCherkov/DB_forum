package ru.mail.park.cherkov.db.models.managers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.dao.ForumDao;
import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.errors.ForumAlreadyCreated;
import ru.mail.park.cherkov.db.models.errors.ForumNotFound;
import ru.mail.park.cherkov.db.models.errors.UserNotFound;
import ru.mail.park.cherkov.db.models.mappers.ForumMapper;

@Service
public class ForumManager {

    private ForumDao forumDao;
    private ForumMapper forumMapper;

    public ForumManager(ForumDao forumDao, ForumMapper forumMapper) {
        this.forumDao = forumDao;
        this.forumMapper = forumMapper;
    }

    public Forum create(Forum forum) {
        try {
            return forumMapper.convert(forumDao.create(forum));
        }
        catch (EmptyResultDataAccessException exception) {
            throw new UserNotFound();
        }
        catch (DuplicateKeyException exception) {
            throw new ForumAlreadyCreated(get(forum.slug));
        }
        catch (DataIntegrityViolationException exception) {
            throw new UserNotFound();
        }
    }

    public Forum get(String slug) {
        try {
            return forumMapper.convert(forumDao.getDetails(slug));
        }
        catch (EmptyResultDataAccessException exception) {
            throw new ForumNotFound();
        }
    }

}
