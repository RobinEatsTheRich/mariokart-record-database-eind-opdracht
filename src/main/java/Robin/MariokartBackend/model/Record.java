package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "Records")
public class Record {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "total_time")
    private float totalTime;
    private float lap1;
    private float lap2;
    private float lap3;
    private float lap4;
    private float lap5;
    private float lap6;
    private float lap7;
    @Column(name = "is_200CC")
    private boolean is200CC;
    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;
    @Column(name = "character_id")
    private Long characterId;
    @Column(name = "body_id")
    private Long bodyId;
    @Column(name = "wheels_id")
    private Long wheelsId;
    @Column(name = "glider_id")
    private Long gliderId;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    @OneToOne(mappedBy = "record")
    @JoinColumn(name = "recordingData_id")
    private RecordingData recordingData;

    public Record(float totalTime,
                  float lap1,
                  float lap2,
                  float lap3,
                  boolean is200CC,
                  Course course,
                  Long characterId,
                  Long bodyId,
                  Long wheelsId,
                  Long gliderId
    ) {
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.is200CC = is200CC;
        this.course = course;
        this.characterId = characterId;
        this.bodyId = bodyId;
        this.wheelsId = wheelsId;
        this.gliderId = gliderId;
    }

    public Record(float totalTime,
                  float lap1,
                  float lap2,
                  float lap3,
                  float lap4,
                  float lap5,
                  float lap6,
                  float lap7,
                  boolean is200CC,
                  Course course,
                  Long characterId,
                  Long bodyId,
                  Long wheelsId,
                  Long gliderId
    ) {
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.lap4 = lap4;
        this.lap5 = lap5;
        this.lap6 = lap6;
        this.lap7 = lap7;
        this.is200CC = is200CC;
        this.course = course;
        this.characterId = characterId;
        this.bodyId = bodyId;
        this.wheelsId = wheelsId;
        this.gliderId = gliderId;
    }

    public Record(Long id,      //Constructor for Testing
                  float totalTime,
                  float lap1,
                  float lap2,
                  float lap3,
                  boolean is200CC,
                  Long characterId,
                  Long bodyId,
                  Long wheelsId,
                  Long gliderId
    ) {
        this.id = id;
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.is200CC = is200CC;
        this.characterId = characterId;
        this.bodyId = bodyId;
        this.wheelsId = wheelsId;
        this.gliderId = gliderId;
    }

    public Record() {
    }

    public boolean isIs200CC() {
        return is200CC;
    }

    public void setIs200CC(boolean is200CC) {
        this.is200CC = is200CC;
    }
}



