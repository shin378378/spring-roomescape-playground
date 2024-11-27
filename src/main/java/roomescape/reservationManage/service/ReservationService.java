package roomescape.reservationManage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationManage.Reservation;
import roomescape.exception.NotFoundReservationException;
import roomescape.reservationManage.repository.ReservationRepository;

import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @Transactional
    public Reservation addReservation(String name, String date, Long timeId) {
        return reservationRepository.insertReservation(name, date, timeId);
    }

    @Transactional
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id);
        if (reservation == null) {
            throw new NotFoundReservationException("예약 ID가 존재하지 않습니다: " + id);
        }
        reservationRepository.deleteReservation(id);
    }
}

