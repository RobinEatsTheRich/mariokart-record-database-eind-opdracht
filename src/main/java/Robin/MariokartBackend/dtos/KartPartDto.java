package Robin.MariokartBackend.dtos;

import Robin.MariokartBackend.enumerations.PartType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KartPartDto {
    private Long id;
    private String name;
    private byte[] img;
    private PartType partType;
}
