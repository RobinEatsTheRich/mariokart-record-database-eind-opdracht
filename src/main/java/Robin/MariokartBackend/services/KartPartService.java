package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.enumerations.PartType;
import Robin.MariokartBackend.inputDtos.KartPartInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.KartPart;
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
        return dtoFromKartPart(kartPartFromId(id));
    }

    public KartPartDto addKartPart(KartPartInputDto dto){
        KartPart kartPart = kartPartFromDto(dto);
        kartPartRepos.save(kartPart);
        return dtoFromKartPart(kartPart);
    }

    public KartPartDto editKartPart(Long id, KartPartInputDto dto){
        KartPart newKartPart = kartPartFromDto(dto);
        newKartPart.setId(id);
        kartPartRepos.save(newKartPart);
        return dtoFromKartPart(newKartPart);
    }

    public void deleteKartPart(Long id){
        Optional<KartPart> kartPartOptional = kartPartRepos.findById(id);
        if (kartPartOptional.isPresent()) {
            kartPartRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("The KartPart corresponding to ID "+id+" could not be found in the database");
        }
    }

    public Long kartPartIdFromName(String name){
        Long result = 0l;
        List<KartPart> kartPartList = kartPartRepos.findAll();
        if (!kartPartList.isEmpty()){
            for (KartPart kartPart : kartPartList){
                if (name.toLowerCase().equals(kartPart.getName().toLowerCase())){
                    result = kartPart.getId();
                } else if (kartPart.getAlternativeName() != null && (name.toLowerCase().equals(kartPart.getAlternativeName().toLowerCase()))){
                    result = kartPart.getId();
                }
            }
        }
        if(result == 0l){
            throw new RecordNotFoundException("The KartPart '"+name+"' could not be found in the database");
        }
        return result;
    }
    public KartPart kartPartFromId(Long id){
        KartPart result;
        Optional<KartPart> optionalKartPart = kartPartRepos.findById(id);
        if (optionalKartPart.isPresent()){
            result = optionalKartPart.get();
        } else {
            throw new RecordNotFoundException("The KartPart corresponding to ID "+id+" could not be found in the database");
        }
        return result;
    }

    public KartPartDto dtoFromKartPart(KartPart kartPart) {
        KartPartDto dto = new KartPartDto();
        dto.setId(kartPart.getId());
        dto.setName(kartPart.getName());
        dto.setAlternativeName(kartPart.getAlternativeName());
        dto.setImgLink(kartPart.getImgLink());
        dto.setPartType(kartPart.getPartType());

        return dto;
    }

    public KartPart kartPartFromDto (KartPartInputDto dto) {
        KartPart kartPart = new KartPart();
        kartPart.setId(dto.getId());
        kartPart.setName(dto.getName());
        kartPart.setAlternativeName(dto.getAlternativeName());
        kartPart.setImgLink(dto.getImgLink());
        if (dto.getPartType().toUpperCase().equals("BODY") || dto.getPartType().toUpperCase().equals("WHEELS") || dto.getPartType().toUpperCase().equals("GLIDER")){
            kartPart.setPartType(PartType.valueOf(dto.getPartType()));
        }
        return kartPart;
    }
}
