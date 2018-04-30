package ru.mail.park.cherkov.db.models.managers;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.dao.ForumDao;
import ru.mail.park.cherkov.db.dao.ThreadDao;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.api.Vote;
import ru.mail.park.cherkov.db.models.db.ThreadDBModel;
import ru.mail.park.cherkov.db.models.errors.ForumNotFound;
import ru.mail.park.cherkov.db.models.errors.ThreadAlreadyCreated;
import ru.mail.park.cherkov.db.models.errors.ThreadNotFound;
import ru.mail.park.cherkov.db.models.errors.UserNotFound;
import ru.mail.park.cherkov.db.models.mappers.ThreadMapper;

import java.sql.Timestamp;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ThreadManager {

    private ThreadDao threadDao;
    private ThreadMapper threadMapper;
    private ForumManager forumManager;

    public ThreadManager(ThreadDao threadDao, ForumManager forumManager, ThreadMapper threadMapper) {
        this.threadDao = threadDao;
        this.forumManager = forumManager;
        this.threadMapper = threadMapper;
    }

    public Thread create(
        Thread thread
    ) {
        try {
            return threadMapper.convert(threadDao.create(thread));
        }
        catch (DuplicateKeyException e) {
            throw new ThreadAlreadyCreated(get(thread.slug));
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

        Timestamp sinceTime = null;
        if (since != null) {
            final OffsetDateTime offsetDateTime = OffsetDateTime.parse(since);
            sinceTime = Timestamp.valueOf(offsetDateTime.atZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime());
        }

        List<Thread> threads = threadDao.getByForum(slug, limit, sinceTime, desc)
                .stream()
                .map(el -> threadMapper.convert(el))
                .collect(Collectors.toList());

        if (threads.isEmpty()) {
            forumManager.get(slug);
            return threads;
            //throw new ThreadNotFound();
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
        catch (EmptyResultDataAccessException e) {
            throw  new ThreadNotFound();
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
        catch (EmptyResultDataAccessException e) {
            throw new ThreadNotFound();
        }
        catch (DataIntegrityViolationException e) {
            throw new ThreadNotFound();
        }
    }
}
