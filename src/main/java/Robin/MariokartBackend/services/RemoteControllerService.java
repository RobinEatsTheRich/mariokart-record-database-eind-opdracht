package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.RemoteControllerDto;
import Robin.MariokartBackend.inputDtos.RemoteControllerInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RemoteControllerService {

    private RemoteControllerRepository remoteControllerRepos;

    public RemoteControllerService(RemoteControllerRepository remoteControllerRepos) {
        this.remoteControllerRepos = remoteControllerRepos;
    }

    public List<RemoteControllerDto> getAllRemoteControllers(){
        List<RemoteController> remoteControllerList = remoteControllerRepos.findAll();
        List<RemoteControllerDto> remoteControllerDtoList = new ArrayList<>();
        for(RemoteController remoteController : remoteControllerList)
        {
            remoteControllerDtoList.add(dtoFromRemoteController(remoteController));
        }
        return remoteControllerDtoList;
    }

    public RemoteControllerDto getRemoteController(Long id){
        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(id);
        if (remoteControllerOptional.isPresent()) {
            RemoteController remoteController = remoteControllerOptional.get();
            return dtoFromRemoteController(remoteController);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public RemoteControllerDto addRemoteController(RemoteControllerInputDto dto){
        RemoteController remoteController = remoteControllerFromDto(dto);
        remoteControllerRepos.save(remoteController);
        return dtoFromRemoteController(remoteController);
    }

    public RemoteControllerDto editRemoteController(Long id, RemoteControllerInputDto dto){
        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(id);
        if (remoteControllerOptional.isPresent()) {
            RemoteController OgRemoteController = remoteControllerOptional.get();
            RemoteController remoteController = remoteControllerFromDto(dto);
            remoteController.setId(OgRemoteController.getId());

            RemoteController newRemoteController = remoteControllerRepos.save(remoteController);

            return dtoFromRemoteController(remoteController);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteRemoteController(Long id){
        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(id);
        if (remoteControllerOptional.isPresent()) {
            remoteControllerRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public RemoteControllerDto dtoFromRemoteController(RemoteController remoteController) {
        RemoteControllerDto dto = new RemoteControllerDto();
        dto.setId(remoteController.getId());
        dto.setCompatibleWith(remoteController.getCompatibleWith());
        dto.setName(remoteController.getName());
        dto.setBrand(remoteController.getBrand());
        dto.setPrice(remoteController.getPrice());
        dto.setOriginalStock(remoteController.getOriginalStock());
        dto.setSold(remoteController.getSold());
        return dto;
    }

    public RemoteController remoteControllerFromDto (RemoteControllerInputDto dto) {
        RemoteController remoteController = new RemoteController();
        remoteController.setId(dto.getId());
        remoteController.setCompatibleWith(dto.getCompatibleWith());
        remoteController.setName(dto.getName());
        remoteController.setBrand(dto.getBrand());
        remoteController.setPrice(dto.getPrice());
        remoteController.setOriginalStock(dto.getOriginalStock());
        remoteController.setSold(dto.getSold());
        return remoteController;
    }
}

