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
        String sql = "SELECT id, name, date, time FROM reservation";
        return namedParameterJdbcTemplate.query(sql, reservationRowMapper);
    }

    public Reservation findById(Long id) {
        String sql = "SELECT id, name, date, time FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, reservationRowMapper);
    }

    public Reservation insertReservation(String name, String date, Time time) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("reservation")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("date", date);
        parameters.put("time", time);

        Number generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters);

        return findById(generatedKey.longValue());
    }


    public void deleteReservation(Long id) {
        String sql = "DELETE FROM reservation WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    private final RowMapper<Reservation> reservationRowMapper = (rs, rowNum) -> new Reservation(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("date"),
            rs.getTime("time")
    );
}
