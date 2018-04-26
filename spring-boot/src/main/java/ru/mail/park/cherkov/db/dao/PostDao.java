package ru.mail.park.cherkov.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.db.PostDBModel;

import java.util.List;

@Service
public class PostDao {

    JdbcTemplate template;
    RowMapper <PostDBModel> rowMapper;

    public PostDao(JdbcTemplate template) {

        this.template = template;
        this.rowMapper = (resultSet, i) -> new PostDBModel(
                resultSet.getLong("id"),
                resultSet.getString("messagetext"),
                resultSet.getBoolean("isedited"),
                resultSet.getLong("threadid"),
                resultSet.getLong("parentid"),
                resultSet.getString("usernickname"),
                resultSet.getString("forumslug"),
                resultSet.getTimestamp("created")
        );

    }

    public PostDBModel getById(Long id) {
        String sql = "SELECT * FROM Post WHERE Post.id = ?;";

        return template.queryForObject(
                sql,
                rowMapper,
                id
        );
    }

    public PostDBModel updateMessage(Long id, String message) {
        String sql = "UPDATE Post\n" +
                    "    SET Post.messagetext = ?\n" +
                    "    WHERE Post.id = id\n" +
                    "    RETURNING *;";

        return template.queryForObject(
                sql,
                rowMapper,
                message,
                id
        );
    }

}
