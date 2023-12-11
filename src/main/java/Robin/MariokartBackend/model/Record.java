package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Records")
public class Record {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "total_time")
    private Integer totalTime;
    private Integer lap1;
    private Integer lap2;
    private Integer lap3;
    private Integer lap4;
    private Integer lap5;
    private Integer lap6;
    private Integer lap7;
    @Column(name = "is_200CC")
    private boolean is200CC;
    private byte[] recording;

    @ManyToOne
    private Course course;
    @ManyToOne
    private Kart kart;
    @ManyToOne
    private Character character;
    @OneToOne
    private Profile profile;

    public Record(Integer totalTime,
                  Integer lap1,
                  Integer lap2,
                  Integer lap3,
                  boolean is200CC,
                  byte[] recording,
                  Course course,
                  Kart kart,
                  Character character,
                  Profile profile) {
        this.totalTime = totalTime;
        this.lap1 = lap1;
        this.lap2 = lap2;
        this.lap3 = lap3;
        this.is200CC = is200CC;
        this.recording = recording;
        this.course = course;
        this.kart = kart;
        this.character = character;
        this.profile = profile;
    }

    public Record(Long id,
                  Integer totalTime,
                  Integer lap1,
                  Integer lap2,
                  Integer lap3,
                  Integer lap4,
                  Integer lap5,
                  Integer lap6,
                  Integer lap7,
                  boolean is200CC,
                  byte[] recording,
                  Course course,
                  Kart kart,
                  Character character,
                  Profile profile) {
        this.id = id;
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
        this.course = course;
        this.kart = kart;
        this.character = character;
        this.profile = profile;
    }

    public Record() {
    }
}
