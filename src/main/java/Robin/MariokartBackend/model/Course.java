package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Courses")
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private byte[] img;
    @OneToMany
    private List<Record> records;

    public Course(Long id, String name, byte[] img, List<Record> records) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.records = records;
    }

    public Course() {
    }
}
