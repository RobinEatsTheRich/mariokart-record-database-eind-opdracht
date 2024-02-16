package Robin.MariokartBackend.inputDtos;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import org.springframework.boot.context.properties.bind.DefaultValue;


@Getter
public class RecordInputDto {

    private Long id;
    @Positive
    private float totalTime;
    @Positive
    private float lap1;
    @Positive
    private float lap2;
    @Positive
    private float lap3;
    @PositiveOrZero
    private float lap4;
    @PositiveOrZero
    private float lap5;
    @PositiveOrZero
    private float lap6;
    @PositiveOrZero
    private float lap7;
    private boolean is200CC;
    @NotNull
    private String courseName;
    @NotNull
    private String characterName;
    @NotNull
    private String bodyName;
    @NotNull
    private String wheelsName;
    @NotNull
    private String gliderName;

    public RecordInputDto(float totalTime,
                          float lap1,
                          float lap2,
                          float lap3,
                          boolean is200CC,
                          String courseName,
                          String characterName,
                          String bodyName,
                          String wheelsName,
                          String gliderName) {
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.is200CC = is200CC;
        this.courseName = courseName;
        this.characterName = characterName;
        this.bodyName = bodyName;
        this.wheelsName = wheelsName;
        this.gliderName = gliderName;
    }
    public RecordInputDto(float totalTime,
                          float lap1,
                          float lap2,
                          float lap3,
                          float lap4,
                          float lap5,
                          float lap6,
                          float lap7,
                          boolean is200CC,
                          String characterName,
                          String bodyName,
                          String wheelsName,
                          String gliderName) {
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.lap4 = lap4;
        this.lap5 = lap5;
        this.lap6 = lap6;
        this.lap7 = lap7;
        this.is200CC = is200CC;
        this.courseName = "Baby Park";
        this.characterName = characterName;
        this.bodyName = bodyName;
        this.wheelsName = wheelsName;
        this.gliderName = gliderName;
    }

    public RecordInputDto() {
    }

    public boolean isIs200CC() {
        return is200CC;
    }

    public void setIs200CC(boolean is200CC) {
        this.is200CC = is200CC;
    }
}
