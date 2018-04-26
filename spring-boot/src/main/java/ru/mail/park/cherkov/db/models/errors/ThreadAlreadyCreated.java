package ru.mail.park.cherkov.db.models.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.mail.park.cherkov.db.models.api.Thread;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Thread already created")
public class ThreadAlreadyCreated extends RuntimeException{

    private Thread thread;

    public ThreadAlreadyCreated(Thread thread) {
        this.thread = thread;
    }

    public Thread getThread() {
        return thread;
    }

}
