package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.inputDtos.KartPartInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.KartPart;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.KartPartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KartPartService {


    private final KartPartRepository kartPartRepos;


    public KartPartService(KartPartRepository kartPartRepos){
        this.kartPartRepos = kartPartRepos;
    }


    public List<KartPartDto> getAllKartParts(){
        List<KartPart> kartPartList = kartPartRepos.findAll();
        List<KartPartDto> kartPartDtoList = new ArrayList<>();
        for(KartPart kartPart : kartPartList)
        {
            kartPartDtoList.add(dtoFromKartPart(kartPart));
        }
        return kartPartDtoList;
    }

    public KartPartDto getKartPart(Long id){
        Optional<KartPart> kartPartOptional = kartPartRepos.findById(id);
        if (kartPartOptional.isPresent()) {
            KartPart kartPart = kartPartOptional.get();
            return dtoFromKartPart(kartPart);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public KartPartDto addKartPart(KartPartInputDto dto){
        KartPart kartPart = KartPartFromDto(dto);
        kartPartRepos.save(kartPart);
        return dtoFromKartPart(kartPart);
    }

    public KartPartDto editKartPart(Long id, KartPartInputDto dto){
        Optional<KartPart> kartPartOptional = kartPartRepos.findById(id);
        if (kartPartOptional.isPresent()) {
            KartPart ogKartPart = kartPartOptional.get();
            KartPart kartPart = KartPartFromDto(dto);
            kartPart.setId(ogKartPart.getId());

            kartPartRepos.save(kartPart);

            return dtoFromKartPart(kartPart);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteKartPart(Long id){
        Optional<KartPart> kartPartOptional = kartPartRepos.findById(id);
        if (kartPartOptional.isPresent()) {
            kartPartRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

//    public KartPartDto assignRemoteControllerToKartPart(Long id, Long remoteControllerId){
//        Optional<KartPart> KartPartOptional = KartPartRepos.findById(id);
//        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);
//
//        if(KartPartOptional.isPresent() && remoteControllerOptional.isPresent()){
//            KartPart KartPart = KartPartOptional.get();
//            RemoteController remoteController = remoteControllerOptional.get();
//            KartPart.setRemoteController(remoteController);
//            KartPartRepos.save(KartPart);
//            return dtoFromKartPart(KartPart);
//
//        }else if(KartPartOptional.isPresent() && remoteControllerOptional.isEmpty()){
//            throw new RecordNotFoundException("Remote Controller ID cannot be found");
//
//        }else if(KartPartOptional.isEmpty() && remoteControllerOptional.isPresent()){
//            throw new RecordNotFoundException("KartPart ID cannot be found");
//
//        }else{
//            throw new RecordNotFoundException("Neither ID can be found");
//        }
//    }

    public KartPartDto dtoFromKartPart(KartPart kartPart) {
        KartPartDto dto = new KartPartDto();
        dto.setId(kartPart.getId());
        dto.setName(kartPart.getName());
        dto.setImgLink(kartPart.getImgLink());

        return dto;
    }

    public KartPart KartPartFromDto (KartPartInputDto dto) {
        KartPart kartPart = new KartPart();
        kartPart.setId(dto.getId());
        kartPart.setName(dto.getName());
        kartPart.setImgLink(dto.getImgLink());
        return kartPart;
    }
}
