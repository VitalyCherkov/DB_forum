package ru.mail.park.cherkov.db.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import ru.mail.park.cherkov.db.models.api.Status;

@Service
public class ServiceDao {

    JdbcTemplate template;
    RowMapper <Status> rowMapper;

    public ServiceDao(JdbcTemplate template) {
        this.template = template;

        this.rowMapper = (resultSet, i) -> new Status(
                resultSet.getInt("frm"),
                resultSet.getInt("post"),
                resultSet.getInt("thread")
        );
    }

    public void clear() {
        template.update(
                "DELETE FROM ForumUser; " +
                    "DELETE FROM Vote; " +
                    "DELETE FROM Post; " +
                    "DELETE FROM Thread; " +
                    "DELETE FROM Forum; " +
                    "DELETE FROM \"User\";"
        );
    }

    public Status getStatus() {
        String sql =
                "SELECT COUNT(*) AS frm, SUM(threads) AS thread, SUM(posts) AS post\n" +
                "    FROM Forum F;";

        String usrCount = "SELECT COUNT(*) AS usr FROM \"User\";";

        Status status = template.queryForObject(sql, rowMapper);
        status.user = template.queryForObject(usrCount,
                (resultSet, i) -> resultSet.getInt("usr"));

        return status;
    }

}
