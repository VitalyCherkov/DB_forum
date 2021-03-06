package ru.mail.park.cherkov.db.models.managers;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.dao.PostDao;
import ru.mail.park.cherkov.db.dao.ThreadDao;
import ru.mail.park.cherkov.db.dao.UserDao;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.errors.PostAlreadyCreated;
import ru.mail.park.cherkov.db.models.errors.ThreadNotFound;
import ru.mail.park.cherkov.db.models.mappers.PostMapper;

import java.sql.BatchUpdateException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostManager {

    private UserManager userManager;
    private ThreadManager threadManager;
    private PostDao postDao;
    private PostMapper postMapper;

    public PostManager(UserManager userManager, ThreadManager threadManager, PostDao postDao, PostMapper postMapper) {
        this.threadManager = threadManager;
        this.postDao = postDao;
        this.postMapper = postMapper;
    }

    public List<Post> create(String slugOrId, List<Post> posts) {

        try {
            return postDao
                    .create(slugOrId, posts)
                    .stream()
                    .map(postDBModel -> postMapper.convert(postDBModel))
                    .collect(Collectors.toList());
        }
        catch (BatchUpdateException e) {
            if (e.getNextException().getMessage().toLowerCase().equals("ERROR: invalid_foreign_key".toLowerCase())) {
                throw new PostAlreadyCreated();
            }
            throw new PostAlreadyCreated();
        }
        catch (Exception e) {
            throw new ThreadNotFound();
        }
    }

    public Post update(Post post) {
        try {
            return postMapper.convert(postDao
                    .updateMessage(post.id, post.message));

        }
        catch (EmptyResultDataAccessException e) {
            throw new ThreadNotFound();
        }
    }

    public Post get(Long id) {
        try {
            return postMapper.convert(postDao.getById(id));
        }
        catch (EmptyResultDataAccessException e) {
            throw new ThreadNotFound();
        }
    }

    public List <Post> getList(
            String slugOrId,
            Integer limit,
            Integer since,
            Boolean desc,
            String sortType
    ) {
        threadManager.get(slugOrId);
        return postDao.getSorted(slugOrId, sortType, limit, since, desc)
                .stream()
                .map(postDBModel -> postMapper.convert(postDBModel))
                .collect(Collectors.toList());
    }
}
