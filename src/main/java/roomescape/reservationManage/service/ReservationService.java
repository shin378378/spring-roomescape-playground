package roomescape.reservationManage.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.reservationManage.Reservation;
import roomescape.exception.NotFoundReservationException;
import roomescape.reservationManage.repository.ReservationRepository;

import java.sql.Time;
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
    public Reservation addReservation(String name, String date, Time time) {
        return reservationRepository.insertReservation(name, date, time);
    }

    @Transactional
    public void cancelReservation(Long id) {
        try {
            reservationRepository.findById(id);
        } catch (Exception e) {
            throw new NotFoundReservationException("예약 ID가 존재하지 않습니다: " + id);
        }
        reservationRepository.deleteReservation(id);
    }
}