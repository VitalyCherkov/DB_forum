package ru.mail.park.cherkov.db.utils.sqlGenerators;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class FlatSort implements IGetSort {

    @Override
    public String getSql(ArrayList<Object> destArgs, Long threadId, Integer limit, Integer since, Boolean desc) {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM Post\n" +
                "    WHERE threadid = ?\n"
        );
        destArgs.add(threadId);

        if (since != -1) {
            if (desc != null && desc) {
                sql.append(" AND id < ?");
            }
            else {
                sql.append(" AND id > ?");
            }
            destArgs.add(since);
        }

        if (desc != null && desc) {
            sql.append(" ORDER BY id DESC");
        }
        else {
            sql.append(" ORDER BY id ASC");
        }

        if (limit != -1) {
            sql.append(" LIMIT ?");
            destArgs.add(limit);
        }

        return sql.toString();
    }
}
