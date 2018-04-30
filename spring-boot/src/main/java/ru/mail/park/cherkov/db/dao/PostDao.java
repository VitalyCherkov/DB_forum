package ru.mail.park.cherkov.db.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.Post;
import ru.mail.park.cherkov.db.models.db.PostDBModel;
import ru.mail.park.cherkov.db.models.errors.ThreadNotFound;
import ru.mail.park.cherkov.db.models.errors.UserNotFound;
import ru.mail.park.cherkov.db.utils.sqlGenerators.GetSortSql;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class PostDao {

    JdbcTemplate template;
    GetSortSql getSortSql;
    RowMapper <PostDBModel> rowMapper;
    private final Logger log = LoggerFactory.getLogger(this.getClass());


    public PostDao(JdbcTemplate template, GetSortSql getSortSql) {

        this.template = template;
        this.getSortSql = getSortSql;
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
        String sql = "SELECT * FROM Post WHERE id = ?;";

        return template.queryForObject(
                sql,
                rowMapper,
                id
        );
    }

    public PostDBModel updateMessage(Long id, String message) {
        String sql = "UPDATE Post\n" +
                    "    SET messagetext = COALESCE(?, messagetext)\n" +
                    "    WHERE Post.id = ?\n" +
                    "    RETURNING *;";

        return template.queryForObject(
                sql,
                rowMapper,
                message,
                id
        );
    }

    public List<PostDBModel> create(String slugOrId, List<Post> posts) throws Exception {

        Connection con = template.getDataSource().getConnection();
        try {
            con.setAutoCommit(false);
            return create(con, slugOrId, posts);
        }
        finally {
            con.setAutoCommit(true);
            con.close();
        }

    }

    private List<PostDBModel> create(Connection con, String slugOrId, List<Post> posts) throws Exception {
        Timestamp created = new Timestamp(System.currentTimeMillis());

        ResultSet rs = getThreadForumRS(con, slugOrId);
        Long threadId = rs.getLong("id");
        Long forumId = rs.getLong("forumid");
        String threadSlug = rs.getString("slug");
        String forumSlug = rs.getString("forumslug");

        if (posts == null || posts.isEmpty()) {
            return new ArrayList<PostDBModel>();
        }

        SqlRowSet postsIds = template.queryForRowSet(
                "SELECT nextval('Post_id_seq') FROM generate_series(1, ?);",
                posts.size()
        );


        PreparedStatement ps = con.prepareStatement(
            "INSERT INTO Post (" +
                    "id, " +
                    "userid, " +
                    "usernickname, " +
                    "threadid, " +
                    "threadslug, " +
                    "forumslug, " +
                    "messagetext, " +
                    "created, " +
                    "parentid" +
                    ")\n" +
                "   SELECT\n" +
                "       ?,\n" +
                "       U.id,\n" +
                "       U.nickname,\n" +
                "       ?,\n" +
                "       ?::CITEXT,\n" +
                "       ?::CITEXT,\n" +
                "       ?,\n" +
                "       ?::timestamptz,\n" +
                "       ?\n" +
                "   FROM \"User\" AS U\n" +
                "   WHERE U.nickname = ?::CITEXT;",
                Statement.NO_GENERATED_KEYS
        );

        posts.forEach(post -> {
            postsIds.next();
            post.id = postsIds.getLong(1);
            try {
                ps.setLong(1, post.id);
                ps.setLong(2, threadId);
                ps.setString(3, threadSlug);
                ps.setString(4, forumSlug);
                ps.setString(5, post.message);
                ps.setTimestamp(6, created);
                ps.setLong(7, post.parent);
                ps.setString(8, post.author);
                ps.addBatch();
            }
            catch (SQLException e) {
                log.error("caught");
            }
        });


        PreparedStatement psUpdatePostsCont = con.prepareStatement(
            "UPDATE Forum\n" +
                "    SET posts = posts + ? WHERE id = ?;"
        );

        psUpdatePostsCont.setInt(1, posts.size());
        psUpdatePostsCont.setLong(2, forumId);

        try {
            int[] result = ps.executeBatch();
            psUpdatePostsCont.executeUpdate();
            if (IntStream.of(result).anyMatch(value -> value == 0)) {
                throw new UserNotFound();
            }
            con.commit();
        }
        catch (Exception e) {
            con.rollback();
            throw e;
        }

        return posts
                .stream()
                .map(post -> new PostDBModel(
                        post.id,
                        post.message,
                        false,
                        threadId,
                        post.parent,
                        post.author,
                        forumSlug,
                        created
                ))
                .collect(Collectors.toList());

    }

    private ResultSet getThreadForumRS(Connection con, String slugOrId) throws Exception {

        String sqlId =
                "SELECT id, slug, forumid, forumslug FROM Thread\n" +
                "    WHERE id = ?;";
        String sqlSlug =
                "SELECT id, slug, forumid, forumslug FROM Thread\n" +
                "    WHERE slug = ?::CITEXT;";

        PreparedStatement ps;

        if (slugOrId.matches("[0-9]+")) {
            ps = con.prepareStatement(sqlId);
            ps.setLong(1, Long.parseLong(slugOrId));
        }
        else {
            ps = con.prepareStatement(sqlSlug);
            ps.setString(1, slugOrId);
        }

        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            throw new ThreadNotFound();
        }

        return rs;
    }

    public List<PostDBModel> getSorted(
            String slugOrId,
            String sortType,
            Integer limit,
            Integer since,
            Boolean desc
    ) {
        Long id;
        if (slugOrId.matches("[0-9]+")) {
            id = Long.parseLong(slugOrId);
        }
        else {
            SqlRowSet rowSet = template.queryForRowSet("SELECT id FROM Thread WHERE slug = ?::CITEXT", slugOrId);
            rowSet.next();
            id = rowSet.getLong(1);
        }

        ArrayList<Object> args = new ArrayList<>();
        String sql = getSortSql.get(sortType, args, id, limit, since, desc);

        return template.query(
                sql,
                args.toArray(),
                rowMapper
        );

    }

}
