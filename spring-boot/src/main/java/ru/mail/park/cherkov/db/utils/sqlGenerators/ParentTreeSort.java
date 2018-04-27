package ru.mail.park.cherkov.db.utils.sqlGenerators;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ParentTreeSort implements IGetSort {

    @Override
    public String getSql(ArrayList<Object> destArgs, Long threadId, Integer limit, Integer since, Boolean desc) {

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM Post WHERE path[1] IN (\n" +
            "   SELECT id FROM Post WHERE threadid = ? AND parentid = 0\n"
        );
        destArgs.add(threadId);

        if (since != null) {
            if (desc != null && desc) {
                sql.append("AND path < (SELECT path FROM messages WHERE id = ?)\n");
            }
            else {
                sql.append("AND path > (SELECT path FROM messages WHERE id = ?)\n");
            }
            destArgs.add(since);
        }

        if (limit != null) {
            sql.append("LIMIT ? )\n");
            destArgs.add(limit);
        }

        if (desc != null && desc) {
            sql.append("ORDER BY path DESC, id DESC");
        }
        else {
            sql.append("ORDER BY path ASC, id ASC");
        }

        return sql.toString();

    }
}

