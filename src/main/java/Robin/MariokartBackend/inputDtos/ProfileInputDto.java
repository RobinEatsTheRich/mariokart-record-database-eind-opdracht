package Robin.MariokartBackend.inputDtos;

import lombok.Getter;
@Getter
public class ProfileInputDto {
    private String nintendoCode;

    public ProfileInputDto(String nintendoCode) {
        this.nintendoCode = nintendoCode;
    }
    public ProfileInputDto(){

    }
}
