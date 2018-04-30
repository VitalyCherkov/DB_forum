package ru.mail.park.cherkov.db.utils.sqlGenerators;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TreeSort implements IGetSort {

    @Override
    public String getSql(ArrayList<Object> destArgs, Long threadId, Integer limit, Integer since, Boolean desc) {

        StringBuilder sql = new StringBuilder(
                "SELECT * FROM Post WHERE threadid = ?\n"
        );
        destArgs.add(threadId);

        if (since != -1) {
            if (desc != null && desc) {
                sql.append("AND path < (SELECT path FROM Post WHERE id = ?)\n");
            }
            else {
                sql.append("AND path > (SELECT path FROM Post WHERE id = ?)\n");
            }
            destArgs.add(since);
        }

        if (desc != null && desc) {
            sql.append("ORDER BY path DESC, id DESC\n");
        }
        else {
            sql.append("ORDER BY path ASC, id ASC\n");
        }

        if (limit != -1) {
            sql.append("LIMIT ?");
            destArgs.add(limit);
        }

        return sql.toString();
    }
}
