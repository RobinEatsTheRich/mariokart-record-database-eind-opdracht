package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.TelevisionDto;
import Robin.MariokartBackend.inputDtos.TelevisionInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.Television;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.TelevisionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TelevisionService {


    private final TelevisionRepository televisionRepos;
    private final RemoteControllerRepository remoteControllerRepos;
    private final RemoteControllerService remoteControllerService;


    public TelevisionService(TelevisionRepository televisionRepos, RemoteControllerRepository remoteControllerRepos, RemoteControllerService remoteControllerService) {
        this.televisionRepos = televisionRepos;
        this.remoteControllerRepos = remoteControllerRepos;
        this.remoteControllerService = remoteControllerService;
    }


    public List<TelevisionDto> getAllTelevisions(){
        List<Television> televisionList = televisionRepos.findAll();
        List<TelevisionDto> televisionDtoList = new ArrayList<>();
        for(Television television : televisionList)
        {
            televisionDtoList.add(dtoFromTelevision(television));
        }
        return televisionDtoList;
    }

    public TelevisionDto getTelevision(Long id){
        Optional<Television> televisionOptional = televisionRepos.findById(id);
        if (televisionOptional.isPresent()) {
            Television television = televisionOptional.get();
            return dtoFromTelevision(television);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public TelevisionDto addTelevision(TelevisionInputDto dto){
        Television television = televisionFromDto(dto);
        televisionRepos.save(television);
        return dtoFromTelevision(television);
    }

    public TelevisionDto editTelevision(Long id, TelevisionInputDto dto){
        Optional<Television> televisionOptional = televisionRepos.findById(id);
        if (televisionOptional.isPresent()) {
            Television OgTelevision = televisionOptional.get();
            Television television = televisionFromDto(dto);
            television.setId(OgTelevision.getId());

            televisionRepos.save(television);

            return dtoFromTelevision(television);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteTelevision(Long id){
        Optional<Television> televisionOptional = televisionRepos.findById(id);
        if (televisionOptional.isPresent()) {
            televisionRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public TelevisionDto assignRemoteControllerToTelevision(Long id, Long remoteControllerId){
        Optional<Television> televisionOptional = televisionRepos.findById(id);
        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);

        if(televisionOptional.isPresent() && remoteControllerOptional.isPresent()){
            Television television = televisionOptional.get();
            RemoteController remoteController = remoteControllerOptional.get();
            television.setRemoteController(remoteController);
            televisionRepos.save(television);
            return dtoFromTelevision(television);

        }else if(televisionOptional.isPresent() && remoteControllerOptional.isEmpty()){
            throw new RecordNotFoundException("Remote Controller ID cannot be found");

        }else if(televisionOptional.isEmpty() && remoteControllerOptional.isPresent()){
            throw new RecordNotFoundException("Television ID cannot be found");

        }else{
            throw new RecordNotFoundException("Neither ID can be found");
        }
    }

    public TelevisionDto dtoFromTelevision(Television television) {
        TelevisionDto dto = new TelevisionDto();
        dto.setId(television.getId());
        dto.setType(television.getType());
        dto.setBrand(television.getBrand());
        dto.setName(television.getName());
        dto.setPrice(television.getPrice());
        dto.setAvailableSize(television.getAvailableSize());
        dto.setRefreshRate(television.getRefreshRate());
        dto.setScreenType(television.getScreenType());
        dto.setScreenQuality(television.getScreenQuality());
        dto.setSmartTv(television.getSmartTv());
        dto.setWifi(television.getWifi());
        dto.setVoiceControl(television.getVoiceControl());
        dto.setHdr(television.getHdr());
        dto.setBluetooth(television.getBluetooth());
        dto.setAmbiLight(television.getAmbiLight());
        dto.setOriginalStock(television.getOriginalStock());
        dto.setSold(television.getSold());
        if(television.getRemoteController() != null) {
            dto.setRemoteControllerDto(
                    remoteControllerService.dtoFromRemoteController(
                            television.getRemoteController()
                    ));
        }
        return dto;
    }

    public Television televisionFromDto (TelevisionInputDto dto) {
        Television television = new Television();
        television.setId(dto.getId());
        television.setType(dto.getType());
        television.setBrand(dto.getBrand());
        television.setName(dto.getName());
        television.setPrice(dto.getPrice());
        television.setAvailableSize(dto.getAvailableSize());
        television.setRefreshRate(dto.getRefreshRate());
        television.setScreenType(dto.getScreenType());
        television.setScreenQuality(dto.getScreenQuality());
        television.setSmartTv(dto.getSmartTv());
        television.setWifi(dto.getWifi());
        television.setVoiceControl(dto.getVoiceControl());
        television.setHdr(dto.getHdr());
        television.setBluetooth(dto.getBluetooth());
        television.setAmbiLight(dto.getAmbiLight());
        television.setOriginalStock(dto.getOriginalStock());
        television.setSold(dto.getSold());
        return television;
    }
}
