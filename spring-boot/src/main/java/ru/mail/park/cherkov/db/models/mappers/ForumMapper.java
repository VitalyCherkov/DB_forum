package ru.mail.park.cherkov.db.models.mappers;

import org.springframework.stereotype.Component;
import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.db.ForumDBModel;

@Component
public class ForumMapper implements IMapper<ForumDBModel, Forum> {
    public Forum convert(ForumDBModel forumDBModel) {
        return new Forum(
                forumDBModel.slug,
                forumDBModel.author,
                forumDBModel.title,
                forumDBModel.posts,
                forumDBModel.threads
        );
    }
}
