package roomescape.timeManage;

public class TimeSchedule {
    private Long id;
    private String time; // 필드 이름 수정

    public TimeSchedule(Long id, String time) { // 생성자 수정
        this.id = id;
        this.time = time;
    }

    public Long getId() {
        return id;
    }

    public String getTime() {
        return time;
    }
}

