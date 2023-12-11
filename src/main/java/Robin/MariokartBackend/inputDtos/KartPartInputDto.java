package Robin.MariokartBackend.inputDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class KartPartInputDto {

    private Long id;
    @NotNull
    @Size(min=1, max=128)
    private String name;
    private byte[] img;

    public KartPartInputDto(Long id, String name, byte[] img) {
        this.id = id;
        this.name = name;
        this.img = img;
    }
}
