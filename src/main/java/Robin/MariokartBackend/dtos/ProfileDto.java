package Robin.MariokartBackend.dtos;

import Robin.MariokartBackend.model.Character;
import Robin.MariokartBackend.model.Kart;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.User;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private Long id;
    private User user;
    private List<Record> records;
    private String nintendoCode;
    private Kart favKart;
    private Character favCharacter;
}
