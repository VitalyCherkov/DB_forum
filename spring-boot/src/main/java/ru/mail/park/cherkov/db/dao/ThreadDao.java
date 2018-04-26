package ru.mail.park.cherkov.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.db.ThreadDBModel;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class ThreadDao {

    JdbcTemplate template;
    RowMapper<ThreadDBModel> rowMapper;
    UserDao userDao;

    public ThreadDao(JdbcTemplate template, UserDao userDao) {

        this.template = template;
        this.userDao = userDao;

        this.rowMapper = (resultSet, i) ->
            new ThreadDBModel(
                    resultSet.getString("author"),
                    resultSet.getString("message"),
                    resultSet.getString("slug"),
                    resultSet.getString("title"),
                    resultSet.getTimestamp("created"),
                    resultSet.getInt("votes"),
                    resultSet.getString("forum"),
                    resultSet.getLong("id")
            );

    }

    public List<ThreadDBModel> getByForum(
            String forumSlug,
            Timestamp since,
            Boolean desc,
            Integer limit
    ) {
        StringBuilder sql = new StringBuilder(
                "SELECT T.nickname AS author,\n" +
                "       T.created AS created,\n" +
                "       T.id AS id,\n" +
                "       T.forumslug AS forum,\n" +
                "       T.messagetext AS \"message\",\n" +
                "       T.slug AS slug,\n" +
                "       T.title AS title,\n" +
                "       T.votes AS votes \n" +
                "FROM Thread T\n" +
                "WHERE ?::CITEXT = T.forumslug\n"
        );

        if (since != null) {
            if (desc) {
                sql.append(" AND T.created <= ?::timestamptz");
            }
            else {
                sql.append(" AND T.created >= ?::timestamptz");
            }
        }

        if (desc) {
            sql.append(" ORDER BY DESC");
        }
        else {
            sql.append(" ORDER BY ASC");
        }

        if (limit >= 0) {
            sql.append(" LIMIT ").append(limit.toString());
        }

        sql.append(";");

        return template.query(
                sql.toString(),
                ps -> {
                    ps.setString(1, forumSlug);
                    if (since != null) {
                        ps.setTimestamp(2, since);
                    }
                },
                rowMapper
        );
    }

}
