package Robin.MariokartBackend.inputDtos;

import Robin.MariokartBackend.model.Profile;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserInputDto {
    private Long id;
    @NotNull
    @Size(min=1, max=128)
    private String username;
    @NotNull
    @Size(min=1, max=128)
    private String password;
    @Size(min=1, max=128)
    private String email;

    public UserInputDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
