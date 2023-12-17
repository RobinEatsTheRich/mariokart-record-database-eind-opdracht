package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "courses")
public class Course {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    @Column(name = "img_link")
    private String imgLink;
    @OneToMany
    private List<Record> records;

    public Course(Long id, String name, String imgLink, List<Record> records) {
        this.id = id;
        this.name = name;
        this.imgLink = getImgLink();
    }

    public Course() {
    }
}
