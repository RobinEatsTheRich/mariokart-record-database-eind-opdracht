package Robin.MariokartBackend.model;
import Robin.MariokartBackend.enumerations.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class User {
    @Id
    private String username;
    private String password;
    private String email;
    @OneToOne(mappedBy = "user")
    private Profile profile;
    private List<UserRole> userRoles;

    public User(String username, String password, String email, Profile profile, List<UserRole> userRoles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.profile = profile;
        this.userRoles = userRoles;
    }

    public User() {
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
