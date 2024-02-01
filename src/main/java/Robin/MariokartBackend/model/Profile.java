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
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "profile")
    private List<Record> records;
    @Column(name = "nintendo_code")
    private String nintendoCode;

    public Profile(Long id,User user,List<Record> records, String nintendoCode) {
        this.id = id;
        this.user = user;
        this.records = records;
        this.nintendoCode = nintendoCode;
    }
    public Profile(User user) {
        this.user = user;
    }

    public Profile() {
    }
}
