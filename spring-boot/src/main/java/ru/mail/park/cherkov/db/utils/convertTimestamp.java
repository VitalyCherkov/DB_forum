package ru.mail.park.cherkov.db.utils;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

@Service
public class convertTimestamp {

    Timestamp convert(String from) {
        return Timestamp.valueOf(from);
    }

}
