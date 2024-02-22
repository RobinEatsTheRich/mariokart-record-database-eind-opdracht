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
    private Long id;
    private String name;
    private String alternativeName;
    @Column(name = "img_link")
    private String imgLink;
    @Column(name = "part_type")
    private PartType partType;


    public KartPart(Long id, String name, String alternativeName, String imgLink, PartType partType) {
        this.id = id;
        this.name = name;
        this.alternativeName = alternativeName;
        this.imgLink = imgLink;
        this.partType = partType;
    }

    public KartPart() {
    }
}
