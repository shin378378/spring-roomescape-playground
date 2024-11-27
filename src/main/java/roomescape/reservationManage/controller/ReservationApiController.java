package roomescape.reservationManage.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import roomescape.reservationManage.Reservation;
import roomescape.reservationManage.dto.AddReservationRequest;
import roomescape.reservationManage.service.ReservationService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationApiController {
    private final ReservationService reservationService;

    public ReservationApiController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @GetMapping
    public ResponseEntity<List<Reservation>> getReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok().body(reservations);
    }

    @PostMapping
    public ResponseEntity<Reservation> addReservation(@RequestBody @Valid AddReservationRequest reservationRequest) {
        if (reservationRequest.getTime() == null) {
            throw new IllegalArgumentException("time은 숫자여야 합니다.");
        }

        Reservation reservation = reservationService.addReservation(
                reservationRequest.getName(),
                reservationRequest.getDate(),
                reservationRequest.getTime()
        );
        URI location = URI.create("/reservations/" + reservation.getId());
        return ResponseEntity.created(location).body(reservation);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Long id) {
        reservationService.cancelReservation(id);
        return ResponseEntity.noContent().build();
    }
}
