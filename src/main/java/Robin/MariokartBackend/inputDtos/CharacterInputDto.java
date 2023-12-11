package Robin.MariokartBackend.inputDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CharacterInputDto {
    private Long id;
    @NotNull
    @Size(min=1, max=128)
    private String name;
    private byte[] img;

    public CharacterInputDto(String name, byte[] img) {
        this.name = name;
        this.img = img;
    }
}
