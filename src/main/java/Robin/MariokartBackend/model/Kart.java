package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Karts")
public class Kart {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    private KartPart body;
    @OneToOne
    private KartPart wheels;
    @OneToOne
    private KartPart glider;

    public Kart(Long id, KartPart body, KartPart wheels, KartPart glider) {
        this.id = id;
        this.body = body;
        this.wheels = wheels;
        this.glider = glider;
    }

    public Kart() {
    }
}
