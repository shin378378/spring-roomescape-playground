package roomescape.reservationManage.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationManage.Reservation;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReservationRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReservationRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<Reservation> findAll() {
        String sql = "SELECT r.id, r.name, r.date, t.id AS time_id " +
                "FROM reservation AS r " +
                "INNER JOIN time AS t ON r.time_id = t.id";
        return namedParameterJdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation findById(Long id) {
        String sql = "SELECT r.id, r.name, r.date, t.id AS time_id " +
                "FROM reservation AS r " +
                "INNER JOIN time AS t ON r.time_id = t.id " +
                "WHERE r.id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, reservationRowMapper);
    }

    public Reservation insertReservation(String name, String date, Long time) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("date", date);
        parameters.put("time_id", time);

        Number generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters);

        return findById(generatedKey.longValue());
    }

    public void deleteReservation(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public Time findTimeById(Long timeId) {
        String sql = "SELECT time FROM time WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", timeId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Time.class);
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        Long timeId = rs.getLong("time_id");
        Time time = findTimeById(timeId);
        return new Reservation(
                rs.getLong("id"),
                rs.getString("name"),
                rs.getString("date"),
                time
        );
    };
}
