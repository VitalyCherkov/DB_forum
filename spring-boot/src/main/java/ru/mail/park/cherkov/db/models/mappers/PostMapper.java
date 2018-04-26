package ru.mail.park.cherkov.db.models.mappers;

import org.springframework.stereotype.Component;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.db.PostDBModel;

@Component
public class PostMapper implements IMapper <PostDBModel, Post> {
    public Post convert(PostDBModel postDBModel) {
        return new Post(
                postDBModel.author,
                postDBModel.created,
                postDBModel.forum,
                postDBModel.id,
                postDBModel.isEdited,
                postDBModel.message,
                postDBModel.parentId,
                postDBModel.threadId
        );
    }
}
