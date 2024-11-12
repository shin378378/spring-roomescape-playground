package roomescape.reservation.repository;

import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class ReservationRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReservationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findAll() {
        String sql = "SELECT id, name, date, time FROM reservation";
        return jdbcTemplate.queryForList(sql);
    }

    public Map<String, Object> findById(Long id) {
        String sql = "SELECT id, name, date, time FROM reservation WHERE id = ?";
        return jdbcTemplate.queryForMap(sql, id);
    }
}
