package ru.mail.park.cherkov.db.models.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.mail.park.cherkov.db.models.api.Forum;

@ResponseStatus(code = HttpStatus.CONFLICT/*, reason = "Forum already created"*/)
public class ForumAlreadyCreated extends RuntimeException implements IMessageContainerError {

    private Forum forum;

    @Override
    public Object getCustomMessage() {
        return forum;
    }

    public ForumAlreadyCreated(Forum forum) {
        this.forum = forum;
    }

}
