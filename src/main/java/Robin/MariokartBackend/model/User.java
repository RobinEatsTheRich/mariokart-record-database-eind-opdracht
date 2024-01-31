package Robin.MariokartBackend.model;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private String email;
    @OneToOne(mappedBy = "user")
    private Profile profile;

    public User(Long id, String username, String password, String email, Profile profile) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
    }

    public User() {
    }
}
