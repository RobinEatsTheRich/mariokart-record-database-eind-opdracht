package Robin.MariokartBackend.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CharacterDto {
    private Long id;
    private String name;
    private byte[] img;
}
