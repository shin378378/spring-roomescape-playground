package roomescape.reservation.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.reservation.Reservation;

import java.util.List;

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

    public Reservation insertReservation(String name, String date, String time) {
        String sql = "INSERT INTO reservation (`name`, `date`, `time`) VALUES (:name, :date, :time)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", name)
                .addValue("date", date)
                .addValue("time", time);

        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});

        Long generatedId = keyHolder.getKey().longValue();
        return findById(generatedId);
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
            rs.getString("time")
    );
}
