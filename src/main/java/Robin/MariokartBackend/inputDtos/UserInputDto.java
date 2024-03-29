package Robin.MariokartBackend.inputDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserInputDto {
    @NotNull
    @Size(min=1, max=60)
    private String username;
    @NotNull
    @Size(min=1, max=60)
    private String password;
    @Size(max=60)
    private String email;

    private List<String> roles;

    public UserInputDto(String username, String password, String email, List<String> roles) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }
    public UserInputDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public UserInputDto() {
    }
}
