package ru.mail.park.cherkov.db.models.mappers;

import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.db.ForumDBModel;

public class ForumMapper implements IMapper<ForumDBModel, Forum> {
    public Forum convert(ForumDBModel forumDBModel) {
        return new Forum(
                forumDBModel.slug,
                forumDBModel.title,
                forumDBModel.author,
                forumDBModel.posts,
                forumDBModel.threads
        );
    }
}
