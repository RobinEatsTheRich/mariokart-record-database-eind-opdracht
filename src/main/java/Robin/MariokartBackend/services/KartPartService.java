package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.enumerations.PartType;
import Robin.MariokartBackend.exceptions.BadRequestException;
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
        if (kartPartRepos.findById(dto.getId()).isPresent()){
            throw new BadRequestException("A kartPart by this ID already exists, please either edit that part using the 'PUT' method, or pick a different ID");
        }
        KartPart kartPart = kartPartFromDto(dto);
        kartPartRepos.save(kartPart);
        return dtoFromKartPart(kartPart);
    }

    public KartPartDto editKartPart(Long id, KartPartInputDto dto){
        KartPart oldKartPart = kartPartFromId(id);  //This is to efficiently check whether the ID exists
        KartPart newKartPart = kartPartFromDto(dto);
        newKartPart.setId(oldKartPart.getId());
        kartPartRepos.save(newKartPart);
        return dtoFromKartPart(newKartPart);
    }

    public void deleteKartPart(Long id){
        Optional<KartPart> kartPartOptional = kartPartRepos.findById(id);
        if (kartPartOptional.isPresent()) {
            kartPartRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("The KartPart corresponding to ID "+id+" could not be found in the database.");
        }
    }

    public Long kartPartIdFromName(String name){
        Long result = 0l;
        List<KartPart> kartPartList = kartPartRepos.findAll();
        if (!kartPartList.isEmpty()){
            for (KartPart kartPart : kartPartList){
                if (name.equalsIgnoreCase(kartPart.getName())){
                    result = kartPart.getId();
                } else if (kartPart.getAlternativeName() != null && (name.equalsIgnoreCase(kartPart.getAlternativeName()))){
                    result = kartPart.getId();
                }
            }
        }
        if(result == 0l){
            throw new RecordNotFoundException("The KartPart '"+name+"' could not be found in the database.");
        }
        return result;
    }
    public KartPart kartPartFromId(Long id){
        KartPart result;
        Optional<KartPart> optionalKartPart = kartPartRepos.findById(id);
        if (optionalKartPart.isPresent()){
            result = optionalKartPart.get();
        } else {
            throw new RecordNotFoundException("The KartPart corresponding to ID "+id+" could not be found in the database.");
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
        if (dto.getPartType().equalsIgnoreCase("BODY") || dto.getPartType().equalsIgnoreCase("WHEELS") || dto.getPartType().equalsIgnoreCase("GLIDER")){
            kartPart.setPartType(PartType.valueOf(dto.getPartType()));
        } else {
            throw new BadRequestException("The kartpart needs to be one of the valid part types 'BODY', 'WHEELS', or 'GLIDER'.");
        }
        return kartPart;
    }
}
