package ru.mail.park.cherkov.db.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.mail.park.cherkov.db.models.api.ErrorApi;
import ru.mail.park.cherkov.db.models.errors.IMessageContainerError;

@ControllerAdvice
public class ExceptionsAdviceController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleError(Exception e) {


        ResponseStatus responceStatus = AnnotatedElementUtils.findMergedAnnotation(e.getClass(), ResponseStatus.class);
        if (responceStatus != null) {
            if (e instanceof IMessageContainerError) {
                return ResponseEntity
                        .status(responceStatus.code())
                        .body(((IMessageContainerError) e).getCustomMessage());
            }
            else {
                return ResponseEntity
                        .status(responceStatus.code())
                        .body(new ErrorApi(responceStatus.reason()));
            }
        }

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorApi(e.getMessage()));

    }

}
