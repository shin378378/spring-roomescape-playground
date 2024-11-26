package roomescape.reservationManage.dto;

import jakarta.validation.constraints.NotBlank;

import java.sql.Time;

public class AddReservationRequest {
    @NotBlank(message = "이름은 필수 값입니다.")
    final private String name;
    @NotBlank(message = "날짜은 필수 값입니다.")
    final private String date;
    @NotBlank(message = "시간은 필수 값입니다.")
    final private Time time;

    public AddReservationRequest(String name, String date, Time time) {
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }
}
