package roomescape.reservation;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.dto.addReservationRequest;
import roomescape.reservation.exception.MissingRequiredFieldException;
import roomescape.reservation.exception.NotFoundReservationException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ReservationController {

    private final List<Reservation> reservations = new ArrayList<>();

    public ReservationController() {
        reservations.add(new Reservation(1L, "브라운", "2023-01-01", "10:00"));
        reservations.add(new Reservation(2L, "브라운", "2023-01-02", "11:00"));
        reservations.add(new Reservation(3L, "브라운", "2023-01-03", "12:00"));
    }

    @GetMapping("/reservation")
    public String readPage(Model model) {
        model.addAttribute("reservation", reservations);
        return "reservation";
    }

    @RequestMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> addReservation(@RequestBody addReservationRequest reservationRequest) {
        String name = reservationRequest.getName();
        String data = reservationRequest.getDate();
        String time = reservationRequest.getTime();

        if (name == null || name.isEmpty() || data == null || data.isEmpty() || time == null || time.isEmpty()) {
            throw new MissingRequiredFieldException("필수 인자가 누락되었습니다.");
        }

        Reservation reservation = new Reservation(
                getNextId(),
                name, data, time
        );
        reservations.add(reservation);
        URI location = URI.create("/reservations/" + reservation.getId());

        return ResponseEntity.created(location).body(reservation);
    }

    private Long getNextId() {
        if (reservations.isEmpty()) {
            return 1L;
        } else {
            return reservations.get(reservations.size() - 1).getId() + 1;
        }
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<String> handleException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable int id) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == id) {
                reservations.remove(reservation);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("예약이 취소되었습니다.");
            }
        }
        throw new NotFoundReservationException("해당 ID의 예약이 존재하지 않습니다.");
    }

    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<String> handleMissingRequiredFieldException(MissingRequiredFieldException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
