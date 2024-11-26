package roomescape.reservationManage.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.sql.Time;

public class AddReservationRequest {
    @NotBlank(message = "이름은 필수 값입니다.")
    final private String name;
    @NotBlank(message = "날짜은 필수 값입니다.")
    final private String date;
    @NotBlank(message = "시간은 필수 값입니다.")
    //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
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
