package Robin.MariokartBackend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordDto {
    private Long id;
    private String totalTime;
    private String lap1;
    private String lap2;
    private String lap3;
    private String lap4;
    private String lap5;
    private String lap6;
    private String lap7;
    private boolean is200CC;
    private CharacterDto character;
    private CourseDto course;
    private KartPartDto body;
    private KartPartDto wheels;
    private KartPartDto glider;
    private String recordHolder;
    //    private byte[] recording;

    public boolean isIs200CC() {
        return is200CC;
    }

    public void setIs200CC(boolean is200CC) {
        this.is200CC = is200CC;
    }
}
