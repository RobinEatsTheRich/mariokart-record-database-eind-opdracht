package Robin.MariokartBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name= "recording_data")
@Getter
@Setter
public class RecordingData {
    @Id
    @GeneratedValue
    private Long id;
    @Column
    private String name;
    @Column
    private String type;
    @Lob
    private byte[] recordingData;

    @OneToOne
    private  Record record;

    public RecordingData(Long id, String name, String type, byte[] recordingData, Record  record) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.recordingData = recordingData;
        this.record = record;
    }

    public RecordingData() {
    }
}
