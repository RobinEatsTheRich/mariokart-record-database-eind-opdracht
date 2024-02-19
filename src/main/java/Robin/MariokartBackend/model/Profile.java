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
    private String username;
    @OneToOne
    private User user;
    @OneToMany(mappedBy = "profile")
    private List<Record> records;
    @Column(name = "nintendo_code")
    private String nintendoCode;

    @ManyToMany
    @JoinTable(
            name = "rival1_rival2",
            joinColumns = @JoinColumn(name = "rival1_id"),
            inverseJoinColumns = @JoinColumn(name = "rival2_id")
    )
    private List<Profile> rivals;

    public Profile(String username,User user,List<Record> records, String nintendoCode) {
        this.username = username;
        this.user = user;
        this.records = records;
        this.nintendoCode = nintendoCode;
    }
    public Profile(String username, User user) {
        this.username = username;
        this.user = user;
    }

    public Profile() {
    }
}
