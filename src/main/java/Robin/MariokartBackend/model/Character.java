package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "characters")
public class Character {
    @Id
    private Long id;
    private String name;
    @Column(name = "img_link")
    private String imgLink;

    @OneToOne
    private Record record;

    public Character(Long id, String name,  String imgLink) {
        this.id = id;
        this.name = name;
        this.imgLink = imgLink;
    }

    public Character() {
    }
}
