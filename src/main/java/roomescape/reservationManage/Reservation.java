package roomescape.reservationManage;

public class Reservation {
    private Long id;
    private String name;
    private String date;
    private Long timeId; // time_id만 저장

    public Reservation(Long id, String name, String date, Long timeId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.timeId = timeId;
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

    public Long getTimeId() {
        return timeId;
    }
}
