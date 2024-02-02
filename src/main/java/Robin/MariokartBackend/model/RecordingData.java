package Robin.MariokartBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name= "recording_data")
public class RecordingData {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String type;
    @Lob
    private byte[] recordingData;

    @OneToOne
    @JoinColumn(name = "user",referencedColumnName = "username")
    private  User user;

    public RecordingData(Long id, String name, String type, byte[] recordingData, User user) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.recordingData = recordingData;
        this.user = user;
    }

    public RecordingData() {
    }
}
