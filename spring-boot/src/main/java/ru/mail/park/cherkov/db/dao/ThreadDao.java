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
import java.util.ArrayList;
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
            Integer limit,
            Timestamp since,
            Boolean desc
    ) {
        ArrayList <Object> args = new ArrayList<Object>();

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
        args.add(forumSlug);

        if (since != null) {
            if (desc) {
                sql.append(" AND T.created <= ?::timestamptz");
            }
            else {
                sql.append(" AND T.created >= ?::timestamptz");
            }
            args.add(since);
        }

        if (desc) {
            sql.append(" ORDER BY DESC");
        }
        else {
            sql.append(" ORDER BY ASC");
        }

        if (limit >= 0) {
            sql.append(" LIMIT ?");
            args.add(limit);
        }

        sql.append(";");

        return template.query(
                sql.toString(),
                args.toArray(),
                rowMapper
        );
    }

    public ThreadDBModel getBySlugOrId(String slug, Long id) {
        ArrayList <Object> args = new ArrayList<Object>();

        String sql = "SELECT \n" +
                "    T.id AS id,\n" +
                "    T.slug AS slug,\n" +
                "    T.messagetext AS \"message\",\n" +
                "    T.nickname AS author,\n" +
                "    T.created AS created,\n" +
                "    T.forumslug AS forum,\n" +
                "    T.title AS title,\n" +
                "    T.votes AS votes\n" +
                "FROM Thread T WHERE";

        if (slug != null) {
            sql += " T.slug = ?::CITEXT;";
            args.add(slug);
        }
        else {
            sql += " T.id = ?";
            args.add(id);
        }

        return template.queryForObject(
                sql,
                rowMapper,
                args.toArray()
        );
    }

    public ThreadDBModel updateBySlugOrId(String message, String title, String slug, Long id) {
        ArrayList <Object> args = new ArrayList<Object>();

        args.add(message);
        args.add(title);

        String sql =    "UPDATE Thread T\n" +
                        "    SET (T.messagetext, T.title) = (?, ?) WHERE\n";

        if (slug != null) {
            sql += " T.slug = ?::CITEXT;";
            args.add(slug);
        }
        else {
            sql += " T.id = ?";
            args.add(id);
        }

        sql +=  "    RETURNING \n" +
                "        T.id AS id,\n" +
                "        T.slug AS slug,\n" +
                "        T.messagetext AS \"message\",\n" +
                "        T.nickname AS author,\n" +
                "        T.created AS created,\n" +
                "        T.forumslug AS forum,\n" +
                "        T.title AS title,\n" +
                "        T.votes AS votes;";

        return template.queryForObject(
                sql,
                rowMapper,
                args.toArray()
        );

    }

}
