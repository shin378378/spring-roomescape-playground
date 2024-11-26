package roomescape.timeManage.repository;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import roomescape.timeManage.TimeSchedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TimeRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public TimeRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public List<TimeSchedule> findAll() {
        String sql = "SELECT id, time FROM time";
        return namedParameterJdbcTemplate.query(sql, timeRowMapper);
    }

    public TimeSchedule findById(Long id) {
        String sql = "SELECT id, time FROM time WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, params, timeRowMapper);
    }

    public TimeSchedule insertTime(String time) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("time")
                .usingGeneratedKeyColumns("id");

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("time", time);
        Number generatedKey = simpleJdbcInsert.executeAndReturnKey(parameters);

        return findById(generatedKey.longValue());
    }


    public void deleteTime(Long id) {
        String sql = "DELETE FROM time WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        namedParameterJdbcTemplate.update(sql, params);
    }

    private final RowMapper<TimeSchedule> timeRowMapper = (rs, rowNum) -> new TimeSchedule(
            rs.getLong("id"),
            rs.getString("time")
    );
}
