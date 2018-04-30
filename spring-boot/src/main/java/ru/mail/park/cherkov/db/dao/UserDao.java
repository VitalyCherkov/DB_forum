package ru.mail.park.cherkov.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.User;
import ru.mail.park.cherkov.db.models.db.UserDBModel;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserDao {

    private JdbcTemplate template;
    private RowMapper<UserDBModel> rowMapper;

    public UserDao(JdbcTemplate template) {

        this.template = template;

        this.rowMapper = (resultSet, i) ->
                new UserDBModel(
                        resultSet.getString("nickname"),
                        resultSet.getString("email"),
                        resultSet.getString("fullname"),
                        resultSet.getString("about")
                );

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

        return getByNicknameOrEmail(nickname, null);

    }

    public UserDBModel update(User user) {

        final String sql =    "UPDATE \"User\"\n" +
                        "   SET (email, fullname, about) = (\n" +
                        "       COALESCE(?::CITEXT, email),\n" +
                        "       COALESCE(?, fullname),\n" +
                        "       COALESCE(?, about)\n" +
                        "   )\n" +
                        "   WHERE \"User\".nickname = ?::CITEXT\n" +
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

    public List<UserDBModel> getByForum (
            String forumSlug,
            Integer limit,
            String sinceNickname,
            Boolean desc
    ) {

        ArrayList<Object> args = new ArrayList<Object>();

        StringBuilder sql = new StringBuilder(
                "SELECT \n" +
                "    U.about AS about, \n" +
                "    U.email AS email, \n" +
                "    U.fullname AS fullname, \n" +
                "    U.nickname AS nickname\n" +
                "    FROM (\n" +
                "        SELECT FU.userid AS uid FROM ForumUser FU\n" +
                "            WHERE FU.forumslug = ?::CITEXT "
        );
        args.add(forumSlug);

        if (!sinceNickname.equals("")) {
            if (desc) {
                sql.append("AND FU.nickname < ?::CITEXT\n");
            }
            else {
                sql.append("AND FU.nickname > ?::CITEXT\n");
            }
            args.add(sinceNickname);
        }

        sql.append(") AS F JOIN \"User\" AS U ON U.id = F.uid");

        if (desc) {
            sql.append(" ORDER BY U.nickname DESC ");
        }
        else {
            sql.append(" ORDER BY U.nickname ASC ");
        }

        if (limit != -1) {
            sql.append("LIMIT ?");
            args.add(limit);
        }

        sql.append(";");

        return template.query(
                sql.toString(),
                args.toArray(),
                rowMapper
        );

    }

    public List<UserDBModel> getByNicknameOrEmail(String nickname, String email){

        String sql =    "SELECT email, nickname, fullname, about FROM \"User\"" +
                        "   WHERE nickname = ?::CITEXT";

        if (email != null) {
            sql += " OR email = ?::CITEXT;";
        }
        else {
            sql += ";";
        }

        return template.query(
                sql,
                ps -> {
                    ps.setString(1, nickname);
                    if (email != null) {
                        ps.setString(2, email);
                    }
                },
                rowMapper
        );

    }

}
