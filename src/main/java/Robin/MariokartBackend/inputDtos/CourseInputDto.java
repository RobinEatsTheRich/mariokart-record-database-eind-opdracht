package Robin.MariokartBackend.inputDtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class CourseInputDto {
    private Long id;
    @NotNull
    @Size(min=1, max=128)
    private String name;
    private String imgLink;

    public CourseInputDto(String name, String imgLink) {
        this.name = name;
        this.imgLink = imgLink;
    }
}
