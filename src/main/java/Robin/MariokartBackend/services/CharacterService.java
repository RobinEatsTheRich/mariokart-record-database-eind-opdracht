package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.CharacterDto;
import Robin.MariokartBackend.inputDtos.CharacterInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.Character;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.CharacterRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {


    private final CharacterRepository characterRepos;


    public CharacterService(CharacterRepository characterRepos){
        this.characterRepos = characterRepos;
    }


    public List<CharacterDto> getAllCharacters(){
        List<Character> characterList = characterRepos.findAll();
        List<CharacterDto> characterDtoList = new ArrayList<>();
        for(Character character : characterList)
        {
            characterDtoList.add(dtoFromCharacter(character));
        }
        return characterDtoList;
    }

    public CharacterDto getCharacter(Long id){
        Optional<Character> characterOptional = characterRepos.findById(id);
        if (characterOptional.isPresent()) {
            Character character = characterOptional.get();
            return dtoFromCharacter(character);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public CharacterDto addCharacter(CharacterInputDto dto){
        Character character = characterFromDto(dto);
        characterRepos.save(character);
        return dtoFromCharacter(character);
    }

    public CharacterDto editCharacter(Long id, CharacterInputDto dto){
        Optional<Character> characterOptional = characterRepos.findById(id);
        if (characterOptional.isPresent()) {
            Character ogCharacter = characterOptional.get();
            Character character = characterFromDto(dto);
            character.setId(ogCharacter.getId());

            characterRepos.save(character);

            return dtoFromCharacter(character);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteCharacter(Long id){
        Optional<Character> characterOptional = characterRepos.findById(id);
        if (characterOptional.isPresent()) {
            characterRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

//    public CharacterDto assignRemoteControllerToCharacter(Long id, Long remoteControllerId){
//        Optional<Character> CharacterOptional = CharacterRepos.findById(id);
//        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);
//
//        if(CharacterOptional.isPresent() && remoteControllerOptional.isPresent()){
//            Character Character = CharacterOptional.get();
//            RemoteController remoteController = remoteControllerOptional.get();
//            Character.setRemoteController(remoteController);
//            CharacterRepos.save(Character);
//            return dtoFromCharacter(Character);
//
//        }else if(CharacterOptional.isPresent() && remoteControllerOptional.isEmpty()){
//            throw new RecordNotFoundException("Remote Controller ID cannot be found");
//
//        }else if(CharacterOptional.isEmpty() && remoteControllerOptional.isPresent()){
//            throw new RecordNotFoundException("Character ID cannot be found");
//
//        }else{
//            throw new RecordNotFoundException("Neither ID can be found");
//        }
//    }

    public CharacterDto dtoFromCharacter(Character character) {
        CharacterDto dto = new CharacterDto();
        dto.setId(character.getId());
        dto.setName(character.getName());
        dto.setImgLink(character.getImgLink());

        return dto;
    }

    public Character characterFromDto (CharacterInputDto dto) {
        Character character = new Character();
        character.setId(dto.getId());
        character.setName(dto.getName());
        character.setImgLink(dto.getImgLink());
        return character;
    }
}
