package roomescape.reservation.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservation.Reservation;
import roomescape.reservation.dto.AddReservationRequest;
import roomescape.reservation.exception.MissingRequiredFieldException;
import roomescape.reservation.exception.NotFoundReservationException;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {
    private final AtomicLong atomicLong = new AtomicLong(1);
    private final List<Reservation> reservations = new ArrayList<>();

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations() {
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> addReservation(@RequestBody @Valid AddReservationRequest reservationRequest) {
        Reservation reservation = new Reservation(
                getNextId(),
                reservationRequest.getName(),
                reservationRequest.getDate(),
                reservationRequest.getTime()
        );
        reservations.add(reservation);
        URI location = URI.create("/reservations/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable int id) {
        for (Reservation reservation : reservations) {
            if (reservation.getId() == id) {
                reservations.remove(reservation);
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("예약이 취소되었습니다.");
            }
        }
        throw new NotFoundReservationException("해당 ID의 예약이 존재하지 않습니다.");
    }

    @ExceptionHandler(NotFoundReservationException.class)
    public ResponseEntity<String> handleNotFoundException(NotFoundReservationException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MissingRequiredFieldException.class)
    public ResponseEntity<String> handleMissingRequiredFieldException(MissingRequiredFieldException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    private long getNextId() {
        return atomicLong.getAndIncrement();
    }
}
