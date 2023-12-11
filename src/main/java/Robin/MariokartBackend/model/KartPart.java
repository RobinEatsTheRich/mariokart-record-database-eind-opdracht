package Robin.MariokartBackend.model;

import Robin.MariokartBackend.enumerations.PartType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Kart_Parts")
public class KartPart {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private byte[] img;
    @Column(name = "part_type")
    private PartType partType;

    public KartPart(Long id, String name, byte[] img, PartType partType) {
        this.id = id;
        this.name = name;
        this.img = img;
        this.partType = partType;
    }

    public KartPart() {
    }
}
