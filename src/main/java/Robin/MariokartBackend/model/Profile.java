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
    private String userName;
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "profile")
    private List<Record> records;
    @Column(name = "nintendo_code")
    private String nintendoCode;

    public Profile(String userName,User user,List<Record> records, String nintendoCode) {
        this.userName = userName;
        this.user = user;
        this.records = records;
        this.nintendoCode = nintendoCode;
    }
    public Profile(String userName, User user) {
        this.userName = userName;
        this.user = user;
    }

    public Profile() {
    }
}
