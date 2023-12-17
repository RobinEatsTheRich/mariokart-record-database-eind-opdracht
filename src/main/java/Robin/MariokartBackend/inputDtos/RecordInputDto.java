package Robin.MariokartBackend.inputDtos;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;


@Getter
public class RecordInputDto {

    private Long id;
    @PositiveOrZero
    private Integer totalTime;
    @PositiveOrZero
    private Integer lap1;
    @PositiveOrZero
    private Integer lap2;
    @PositiveOrZero
    private Integer lap3;
    @PositiveOrZero
    private Integer lap4;
    @PositiveOrZero
    private Integer lap5;
    @PositiveOrZero
    private Integer lap6;
    @PositiveOrZero
    private Integer lap7;
    private boolean is200CC;
    private byte[] recording;

    public RecordInputDto(Integer totalTime,
                          Integer lap1,
                          Integer lap2,
                          Integer lap3,
                          boolean is200CC,
                          byte[] recording) {
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.is200CC = is200CC;
        this.recording = recording;
    }

    public RecordInputDto(Integer totalTime,
                          Integer lap1,
                          Integer lap2,
                          Integer lap3,
                          Integer lap4,
                          Integer lap5,
                          Integer lap6,
                          Integer lap7,
                          boolean is200CC,
                          byte[] recording) {
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.lap4 = lap4;
        this.lap5 = lap5;
        this.lap6 = lap6;
        this.lap7 = lap7;
        this.is200CC = is200CC;
        this.recording = recording;
    }

    public boolean isIs200CC() {
        return is200CC;
    }

    public void setIs200CC(boolean is200CC) {
        this.is200CC = is200CC;
    }
}
