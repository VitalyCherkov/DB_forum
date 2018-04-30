package ru.mail.park.cherkov.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.Thread;
import ru.mail.park.cherkov.db.models.api.Vote;
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

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ThreadDao(JdbcTemplate template, UserDao userDao) {

        this.template = template;
        this.userDao = userDao;

        this.rowMapper = (resultSet, i) ->
            new ThreadDBModel(
                    resultSet.getString("nickname"),
                    resultSet.getString("messagetext"),
                    resultSet.getString("slug"),
                    resultSet.getString("title"),
                    resultSet.getTimestamp("created"),
                    resultSet.getInt("votes"),
                    resultSet.getString("forumslug"),
                    resultSet.getLong("id")
            );

    }

    public ThreadDBModel create(Thread thread) {

        String sql =
                "INSERT INTO Thread (userid, nickname, forumid, forumslug, messagetext, slug, title, created)\n" +
                "SELECT\n" +
                "    U.id, U.nickname, F.id, F.slug::CITEXT, ?, ?, ?, ?::TIMESTAMPTZ\n" +
                "    FROM Forum AS F, (\n" +
                "        SELECT id, nickname FROM \"User\"\n" +
                "        WHERE \"User\".nickname = ?::CITEXT\n" +
                "        ) AS U\n" +
                "        WHERE F.slug = ?::CITEXT\n" +
                "RETURNING *;\n";

        if (thread.created == null) {
            thread.created = new Timestamp(System.currentTimeMillis());
        }

        return template.queryForObject(
                sql,
                rowMapper,
                thread.message,
                thread.slug,
                thread.title,
                thread.created,
                thread.author,
                thread.forum
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
                "SELECT * \n" +
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
            sql.append(" ORDER BY created DESC");
        }
        else {
            sql.append(" ORDER BY created ASC");
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

        String sql = "SELECT * FROM Thread T WHERE";

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

    public ThreadDBModel updateBySlugOrId(Thread thread) {
        String sql =    "UPDATE Thread T\n" +
                        "    SET (messagetext, title) = " +
                        "   (" +
                        "       COALESCE(?, messagetext), " +
                        "       COALESCE(?, title)" +
                        ") WHERE slug = ?::CITEXT OR id = ? RETURNING *;\n";

        return template.queryForObject(
                sql,
                rowMapper,
                thread.message,
                thread.title,
                thread.slug,
                thread.id
        );
    }

    public ThreadDBModel doVoteBySlug(Vote vote, String slug) {

        String sql =
                "INSERT INTO Vote (nickname, voice, threadid)\n" +
                "    SELECT ?::CITEXT, ?, T.id FROM Thread T\n" +
                "        WHERE T.slug = ?::CITEXT\n" +
                "    ON CONFLICT (nickname, threadid) \n" +
                "    DO UPDATE\n" +
                "        SET voice = ?;";

        template.update(
                sql,
                vote.nickname,
                vote.voice,
                slug,
                vote.voice
        );

        return getBySlugOrId(slug, null);

    }

    public ThreadDBModel doVoteById(Vote vote, Long id) {

        String sql =
                "INSERT INTO Vote (nickname, voice, threadid)\n" +
                "    VALUES(?::CITEXT, ?, ?) \n" +
                "    ON CONFLICT (nickname, threadid)\n" +
                "    DO UPDATE\n" +
                "        SET voice = ?;";

        template.update(
                sql,
                vote.nickname,
                vote.voice,
                id,
                vote.voice
        );

        return getBySlugOrId(null, id);

    }

}
