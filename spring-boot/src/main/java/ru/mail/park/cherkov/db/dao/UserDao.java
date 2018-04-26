package ru.mail.park.cherkov.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.User;
import ru.mail.park.cherkov.db.models.db.UserDBModel;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service
public class UserDao {

    private JdbcTemplate template;
    private RowMapper<UserDBModel> rowMapper;

    public UserDao(JdbcTemplate template) {
        this.template = template;
        this.rowMapper = new RowMapper<UserDBModel>() {
            @Override
            public UserDBModel mapRow(ResultSet resultSet, int i) throws SQLException {
                return new UserDBModel(
                        resultSet.getString("nickname"),
                        resultSet.getString("email"),
                        resultSet.getString("fullname"),
                        resultSet.getString("about")
                );
            }
        };
    }

    public UserDBModel create(User user) {

        String sql =    "INSERT INTO \"User\"(email, nickname, fullname, about)" +
                        "   VALUES (?::CITEXT, ?::CITEXT, ?, ?);";

        template.update( con -> {
            PreparedStatement ps = con.prepareStatement(
                    sql,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.email);
            ps.setString(2, user.nickname);
            ps.setString(3, user.fullname);
            ps.setString(4, user.about);
            return ps;
        });

        return new UserDBModel(user.nickname, user.email, user.fullname, user.about);

    }

    public List<UserDBModel> getByNickname(String nickname) {

        final String sql =    "SELECT email, nickname, fullname, about FROM \"User\"" +
                        "   WHERE nickname = ?::CITEXT";

        return template.query(
                sql,
                ps -> ps.setString(1, nickname),
                rowMapper
        );

    }

    public UserDBModel update(User user) {

        final String sql =    "UPDATE \"User\"\n" +
                        "   SET (email, fullname, about) = (\n" +
                        "       COALESCE(?::CITEXT, email),\n" +
                        "       COALESCE(?, fullname),\n" +
                        "       COALESCE(?, about)\n" +
                        "   )\n" +
                        "   WHERE \"Users\".nickname = ?::CITEXT\n" +
                        "   RETURNING email, nickname, fullname, about;";

        return template.queryForObject(
                sql,
                rowMapper,
                user.email,
                user.fullname,
                user.about,
                user.nickname
        );

    }



}
