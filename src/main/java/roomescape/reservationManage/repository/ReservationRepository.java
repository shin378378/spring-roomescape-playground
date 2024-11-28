package roomescape.reservationManage.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.reservationManage.Reservation;
import roomescape.timeManage.Time;

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
        String sql = "SELECT " +
                "    r.id AS reservation_id, " +
                "    r.name, " +
                "    r.date, " +
                "    t.id AS time_id, " +
                "    t.time AS time_value " +
                "FROM reservation AS r " +
                "INNER JOIN time AS t ON r.time_id = t.id";
        return namedParameterJdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation findById(Long id) {
        String sql = "SELECT " +
                "    r.id AS reservation_id, " +
                "    r.name, " +
                "    r.date, " +
                "    t.id AS time_id, " +
                "    t.time AS time_value " +
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

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> {
        // time 객체 생성
        Time time = new Time(
                rs.getLong("time_id"),      // time의 id
                rs.getString("time_value")  // time의 value
        );

        // Reservation 객체 반환
        return new Reservation(
                rs.getLong("reservation_id"), // alias 사용
                rs.getString("name"),
                rs.getString("date"),
                time                           // 생성된 time 객체 전달
        );
    };

}
