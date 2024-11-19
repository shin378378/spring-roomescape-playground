package roomescape.reservation.dto;

import jakarta.validation.constraints.NotEmpty;

public class AddReservationRequest {
    @NotEmpty(message = "이름은 필수 값입니다.")
    final private String name;
    @NotEmpty(message = "날짜은 필수 값입니다.")
    final private String date;
    @NotEmpty(message = "시간은 필수 값입니다.")
    final private String time;

    public AddReservationRequest(String name, String date, String time) {
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

    public String getTime() {
        return time;
    }
}
