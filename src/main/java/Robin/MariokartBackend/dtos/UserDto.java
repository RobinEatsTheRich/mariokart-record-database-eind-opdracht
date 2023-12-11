package Robin.MariokartBackend.dtos;

import Robin.MariokartBackend.model.Profile;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private String email;
    private Profile profile;
}
