package roomescape.reservationManage;

import java.sql.Time;

public class Reservation {
    private Long id;
    private String name;
    private String date;
    private Time time; // time_id만 저장

    public Reservation(Long id, String name, String date, Time time) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
    }

    public Long getId() {
        return id;
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
