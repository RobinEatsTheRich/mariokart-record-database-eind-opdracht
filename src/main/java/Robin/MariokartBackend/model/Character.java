package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Characters")
public class Character {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private byte[] img;

    public Character(Long id, String name, byte[] img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }

    public Character() {
    }
}
