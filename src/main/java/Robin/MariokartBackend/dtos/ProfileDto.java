package Robin.MariokartBackend.dtos;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileDto {
    private String userName;
    private List<RecordDto> records;
    private String nintendoCode;


    public ProfileDto() {
    }
}
