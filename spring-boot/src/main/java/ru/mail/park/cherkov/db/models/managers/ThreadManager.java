package ru.mail.park.cherkov.db.models.managers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.dao.ForumDao;
import ru.mail.park.cherkov.db.dao.ThreadDao;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.api.Vote;
import ru.mail.park.cherkov.db.models.errors.ForumNotFound;
import ru.mail.park.cherkov.db.models.errors.ThreadAlreadyCreated;
import ru.mail.park.cherkov.db.models.errors.ThreadNotFound;
import ru.mail.park.cherkov.db.models.errors.UserNotFound;
import ru.mail.park.cherkov.db.models.mappers.ThreadMapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreadManager {

    private ThreadDao threadDao;
    private ForumDao forumDao;
    private ThreadMapper threadMapper;

    public ThreadManager(ThreadDao threadDao, ForumDao forumDao, ThreadMapper threadMapper) {
        this.threadDao = threadDao;
        this.forumDao = forumDao;
        this.threadMapper = threadMapper;
    }

    public Thread create(
        Thread thread
    ) {
        try {
            return threadMapper.convert(threadDao.create(thread));
        }
        catch (DuplicateKeyException e) {
            throw new ThreadAlreadyCreated(
                    get(thread.slug)
            );
        }
        catch (DataIntegrityViolationException e) {
            throw new UserNotFound();
        }
        catch (EmptyResultDataAccessException e) {
            throw new UserNotFound();
        }
    }

    public Thread get(String slugOrId) {
        try {
            if (!slugOrId.matches("[0-9]+")) {
                return threadMapper.convert(
                        threadDao.getBySlugOrId(slugOrId, null)
                );
            }
            else {
                return threadMapper.convert(
                        threadDao.getBySlugOrId(null, Long.parseLong(slugOrId))
                );
            }
        }
        catch (EmptyResultDataAccessException e) {
            throw new ThreadNotFound();
        }
    }

    public List<Thread> getByForum(
            String slug,
            Integer limit,
            String since,
            Boolean desc
    ) {
        List<Thread> threads = threadDao.getByForum(slug, limit, Timestamp.valueOf(since), desc)
                .stream()
                .map(el -> threadMapper.convert(el))
                .collect(Collectors.toList());

        if (threads.isEmpty()) {
            // Check if forum exists
            forumDao.getDetails(slug);
        }

        return threads;
    }

    public Thread update(String slugOrId, Thread thread) {
        if (!slugOrId.matches("[0-9]+")) {
            thread.slug = slugOrId;
        }
        else {
            thread.id = Long.parseLong(slugOrId);
        }

        try {
            return threadMapper.convert(
                    threadDao.updateBySlugOrId(thread)
            );
        }
        catch (DataIntegrityViolationException e) {
            throw new ThreadNotFound();
        }
    }

    public Thread doVote(String slugOrId, Vote vote) {
        try {
            if (slugOrId.matches("[0-9]+")) {
                return threadMapper.convert(
                        threadDao.doVoteById(vote, Long.parseLong(slugOrId))
                );
            }
            else {
                return threadMapper.convert(
                        threadDao.doVoteBySlug(vote, slugOrId)
                );
            }
        }
        catch (DataIntegrityViolationException e) {
            throw new ThreadNotFound();
        }
    }
}
