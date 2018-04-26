package ru.mail.park.cherkov.db.models.mappers;

import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.db.ThreadDBModel;

public class ThreadMapper implements IMapper<ThreadDBModel, Thread> {

    public Thread convert(ThreadDBModel threadDBModel) {
        return new Thread(
                threadDBModel.author,
                threadDBModel.created,
                threadDBModel.forum,
                threadDBModel.id,
                threadDBModel.message,
                threadDBModel.slug,
                threadDBModel.title,
                threadDBModel.votes
        );
    }
}
