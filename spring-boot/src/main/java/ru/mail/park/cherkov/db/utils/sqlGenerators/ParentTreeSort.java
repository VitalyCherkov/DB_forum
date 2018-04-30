package ru.mail.park.cherkov.db.utils.sqlGenerators;

import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ParentTreeSort implements IGetSort {

    @Override
    public String getSql(ArrayList<Object> destArgs, Long threadId, Integer limit, Integer since, Boolean desc) {

        StringBuilder sql = new StringBuilder(
            "SELECT * FROM Post WHERE path[1] IN (\n" +
            "   SELECT id FROM Post WHERE threadid = ? AND parentid = 0 "
        );
        destArgs.add(threadId);

        if (since != -1) {
            if (desc) {
                sql.append("AND path[1] < (SELECT path[1] FROM Post WHERE id = ?) ");
            }
            else {
                sql.append("AND path > (SELECT path FROM Post WHERE id = ?) ");
            }
            destArgs.add(since);
        }

        if (desc) {
            sql.append("ORDER BY path[1] DESC, path ASC, id ASC ");
        }
        else {
            sql.append("ORDER BY path ASC, id ASC ");
        }

        if (limit != -1) {
            sql.append("LIMIT ?\n");
            destArgs.add(limit);
        }

        sql.append(") ");

        if (desc) {
            sql.append("ORDER BY path[1] DESC, path ASC, id ASC;");
        }
        else {
            sql.append("ORDER BY path ASC, id ASC;");
        }

        return sql.toString();

    }
}

