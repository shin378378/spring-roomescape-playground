package roomescape.reservation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.Reservation;
import roomescape.reservation.dto.AddReservationRequest;
import roomescape.reservation.exception.MissingRequiredFieldException;
import roomescape.reservation.exception.NotFoundReservationException;
import roomescape.reservation.ReservationService;

import java.net.URI;
import java.util.List;

@Controller
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping("/reservation")
    public String readPage(Model model) {
        List<Reservation> reservations = reservationService.getAllReservations();
        model.addAttribute("reservation", reservations);
        return "reservation";
    }

    @RequestMapping("/reservations")
    @ResponseBody
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping("/reservations")
    public ResponseEntity<?> addReservation(@RequestBody AddReservationRequest reservationRequest) {
        String name = reservationRequest.getName();
        String date = reservationRequest.getDate();
        String time = reservationRequest.getTime();

        if (name == null || name.isEmpty() || date == null || date.isEmpty() || time == null || time.isEmpty()) {
            throw new MissingRequiredFieldException("필수 인자가 누락되었습니다.");
        }

        Reservation reservation = reservationService.addReservation(name, date, time);
        URI location = URI.create("/reservations/" + reservation.getId());

        return ResponseEntity.created(location).body(reservation);
    }

    @DeleteMapping("/reservations/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("예약이 취소되었습니다.");
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<String> handleException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<String> handleMissingRequiredFieldException(MissingRequiredFieldException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
