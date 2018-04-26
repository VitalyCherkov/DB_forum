package ru.mail.park.cherkov.db.models.mappers;

public interface IMapper <FROM, TO> {
    public TO convert(FROM from);
}
