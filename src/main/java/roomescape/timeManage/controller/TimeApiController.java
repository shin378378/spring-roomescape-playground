package roomescape.timeManage.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.YourController;
import roomescape.timeManage.TimeSchedule;
import roomescape.timeManage.dto.AddTimeRequest;
import roomescape.timeManage.service.TimeService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/times")
public class TimeApiController {
    private final TimeService timeService;

    public TimeApiController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeSchedule>> getTimes() {
        List<TimeSchedule> times = timeService.getAllTimes();
        return ResponseEntity.ok().body(times);
    }

    @PostMapping
    public ResponseEntity<TimeSchedule> addTime(@RequestBody @Valid AddTimeRequest timeRequest) {
        System.out.println("시간 받기");

        String time = timeRequest.getTime();
        TimeSchedule timeSchedule = timeService.addTime(time);

        System.out.println("시간 추가하기");
        URI location = URI.create("/times/" + timeSchedule.getId());
        return ResponseEntity.created(location).body(timeSchedule);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTime(@PathVariable Long id) {
        timeService.cancelTime(id);
        return ResponseEntity.noContent().build();
    }
}
