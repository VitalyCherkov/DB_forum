package ru.mail.park.cherkov.db.models.mappers;

import org.springframework.stereotype.Component;

public interface IMapper <FROM, TO> {
    public TO convert(FROM from);
}
