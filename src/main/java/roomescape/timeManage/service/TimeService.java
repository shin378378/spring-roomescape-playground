package roomescape.timeManage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.exception.NotFoundReservationException;
import roomescape.timeManage.TimeSchedule;
import roomescape.timeManage.repository.TimeRepository;

import java.util.List;

@Service
public class TimeService {
    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public List<TimeSchedule> getAllTimes() {
        return timeRepository.findAll();
    }

    @Transactional
    public TimeSchedule addTime(String time) {
        return timeRepository.insertTime(time);
    }

    @Transactional
    public void cancelTime(Long id) {
        try {
            timeRepository.findById(id);
        } catch (Exception e) {
            throw new NotFoundReservationException("시간 ID가 존재하지 않습니다: " + id);
        }
        timeRepository.deleteTime(id);
    }
}
