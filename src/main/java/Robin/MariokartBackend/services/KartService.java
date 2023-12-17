package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.KartDto;
import Robin.MariokartBackend.inputDtos.KartInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.Kart;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.KartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class KartService {


    private final KartRepository kartRepos;


    public KartService(KartRepository kartRepos) {
        this.kartRepos = kartRepos;
    }


    public List<KartDto> getAllKarts(){
        List<Kart> kartList = kartRepos.findAll();
        List<KartDto> kartDtoList = new ArrayList<>();
        for(Kart kart : kartList)
        {
            kartDtoList.add(dtoFromKart(kart));
        }
        return kartDtoList;
    }

    public KartDto getKart(Long id){
        Optional<Kart> kartOptional = kartRepos.findById(id);
        if (kartOptional.isPresent()) {
            Kart kart = kartOptional.get();
            return dtoFromKart(kart);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public KartDto addKart(KartInputDto dto){
        Kart kart = kartFromDto(dto);
        kartRepos.save(kart);
        return dtoFromKart(kart);
    }

    public KartDto editKart(Long id, KartInputDto dto){
        Optional<Kart> kartOptional = kartRepos.findById(id);
        if (kartOptional.isPresent()) {
            Kart ogKart = kartOptional.get();
            Kart kart = kartFromDto(dto);
            kart.setId(ogKart.getId());

            kartRepos.save(kart);

            return dtoFromKart(kart);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteKart(Long id){
        Optional<Kart> kartOptional = kartRepos.findById(id);
        if (kartOptional.isPresent()) {
            kartRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

//    public KartDto assignRemoteControllerToKart(Long id, Long remoteControllerId){
//        Optional<Kart> KartOptional = KartRepos.findById(id);
//        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);
//
//        if(KartOptional.isPresent() && remoteControllerOptional.isPresent()){
//            Kart Kart = KartOptional.get();
//            RemoteController remoteController = remoteControllerOptional.get();
//            Kart.setRemoteController(remoteController);
//            KartRepos.save(Kart);
//            return dtoFromKart(Kart);
//
//        }else if(KartOptional.isPresent() && remoteControllerOptional.isEmpty()){
//            throw new RecordNotFoundException("Remote Controller ID cannot be found");
//
//        }else if(KartOptional.isEmpty() && remoteControllerOptional.isPresent()){
//            throw new RecordNotFoundException("Kart ID cannot be found");
//
//        }else{
//            throw new RecordNotFoundException("Neither ID can be found");
//        }
//    }

    public KartDto dtoFromKart(Kart kart) {
        KartDto dto = new KartDto();
        dto.setId(kart.getId());
        dto.setBody(kart.getBody());
        dto.setWheels(kart.getWheels());
        dto.setGlider(kart.getGlider());

        return dto;
    }

    public Kart kartFromDto (KartInputDto dto) {
        Kart kart = new Kart();
        kart.setId(dto.getId());
        kart.setBody(dto.getBody());
        kart.setWheels(dto.getWheels());
        kart.setGlider(dto.getGlider());
        return kart;
    }
}
