package Robin.MariokartBackend.dtos;

import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.model.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
    private String email;
    private List<UserRole> userRoles;
    private ProfileDto profile;
}
