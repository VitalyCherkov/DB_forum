package ru.mail.park.cherkov.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.Forum;
import ru.mail.park.cherkov.db.models.db.ForumDBModel;
import ru.mail.park.cherkov.db.models.errors.ForumNotFound;

import java.beans.Expression;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class ForumDao {

    private JdbcTemplate template;
    private RowMapper<ForumDBModel> rowMapper;

    public ForumDao(JdbcTemplate template) {

        this.template = template;

        this.rowMapper = (resultSet, i) ->
                new ForumDBModel(
                        resultSet.getString("slug"),
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getInt("posts"),
                        resultSet.getInt("threads")
                );
    }

    public ForumDBModel create(Forum forum) {

        String sql =    "INSERT INTO Forum (userid, slug, title, nickname)\n" +
                        "   SELECT U.id, ?, ?, U.nickname FROM \"User\" U\n" +
                        "       WHERE U.nickname = ? :: CITEXT RETURNING *;";

        return template.queryForObject(
                sql,
                rowMapper,
                forum.slug,
                forum.title,
                forum.user
        );

    }

    public ForumDBModel getDetails(String slug) {
        String sql =
                "SELECT F.posts AS posts,\n" +
                "       F.threads AS threads,\n" +
                "       F.title AS title,\n" +
                "       U.nickname AS nickname FROM\n" +
                "   (\n" +
                "       SELECT posts, threads, title, userid FROM Forum\n" +
                "           WHERE Forum.slug = ?::CITEXT\n" +
                "   ) AS F\n" +
                "   JOIN (\n" +
                "       SELECT nickname, id FROM \"User\"\n" +
                "   ) AS U\n" +
                "   ON U.id = F.userid;\n";

        return template.queryForObject(
                sql,
                rowMapper,
                slug
        );
    };


}
