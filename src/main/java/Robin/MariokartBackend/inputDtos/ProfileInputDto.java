package Robin.MariokartBackend.inputDtos;

import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.User;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;

import java.util.List;

@Getter
public class ProfileInputDto {
    private Long id;
    private String nintendoCode;

    public ProfileInputDto(String nintendoCode) {
        this.nintendoCode = nintendoCode;
    }
}
