package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Profiles")
public class Profile {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne(mappedBy = "")
    private User user;
    @OneToMany
    private List<Record> records;
    @Column(name = "nintendo_code")
    private String nintendoCode;
    @OneToOne
    private Kart favKart;
    @OneToOne
    private Character favCharacter;

    public Profile(Long id, User user, List<Record> records, String nintendoCode, Kart favKart, Character favCharacter) {
        this.id = id;
        this.user = user;
        this.records = records;
        this.nintendoCode = nintendoCode;
        this.favKart = favKart;
        this.favCharacter = favCharacter;
    }

    public Profile() {
    }
}
