package Robin.MariokartBackend.inputDtos;

import Robin.MariokartBackend.model.KartPart;
import jakarta.persistence.OneToOne;
import lombok.Getter;

@Getter
public class KartInputDto {
    private Long id;

    public KartInputDto() {
    }
}
