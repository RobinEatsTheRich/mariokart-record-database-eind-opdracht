package Robin.MariokartBackend.inputDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class KartPartInputDto {
    @NotNull
    private Long id;
    @NotNull
    @Size(min=1, max=128)
    private String name;
    @Size(min=1, max=360)
    private String imgLink;
    @NotNull
    private String partType;

    public KartPartInputDto(Long id, String name, String imgLink, String partType) {
        this.id = id;
        this.name = name;
        this.imgLink = imgLink;
        this.partType = partType;
    }
}
