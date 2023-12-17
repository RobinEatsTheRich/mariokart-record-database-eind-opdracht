package Robin.MariokartBackend.dtos;

import Robin.MariokartBackend.model.Character;
import Robin.MariokartBackend.model.Course;
import Robin.MariokartBackend.model.Kart;
import Robin.MariokartBackend.model.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecordDto {
    private Long id;
    private Integer totalTime;
    private Integer lap1;
    private Integer lap2;
    private Integer lap3;
    private Integer lap4;
    private Integer lap5;
    private Integer lap6;
    private Integer lap7;
    private boolean is200CC;
    private byte[] recording;
    private Course course;
    private Kart kart;
    private Character character;
    private Profile profile;

    public boolean isIs200CC() {
        return is200CC;
    }

    public void setIs200CC(boolean is200CC) {
        this.is200CC = is200CC;
    }
}
