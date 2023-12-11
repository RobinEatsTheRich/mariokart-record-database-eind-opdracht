package Robin.MariokartBackend.dtos;

import Robin.MariokartBackend.model.KartPart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KartDto {
    private Long id;
    private KartPart body;
    private KartPart wheels;
    private KartPart glider;
}
