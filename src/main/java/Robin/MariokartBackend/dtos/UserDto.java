package Robin.MariokartBackend.dtos;

import Robin.MariokartBackend.enumerations.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserDto {
    private String username;
    private String email;
    private List<UserRole> userRoles;
    private ProfileDto profile;
}
