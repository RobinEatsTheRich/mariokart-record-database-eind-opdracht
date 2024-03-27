package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.CharacterDto;
import Robin.MariokartBackend.exceptions.BadRequestException;
import Robin.MariokartBackend.inputDtos.CharacterInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Character;
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
        return dtoFromCharacter(characterFromId(id));
    }

    public CharacterDto addCharacter(CharacterInputDto dto){
        if (characterRepos.findById(dto.getId()).isPresent()){
            throw new BadRequestException("A character by this ID already exists, please either edit that character using the 'PUT' method, or pick a different ID");
        }
        Character character = characterFromDto(dto);
        characterRepos.save(character);
        return dtoFromCharacter(character);
    }

    public CharacterDto editCharacter(Long id, CharacterInputDto dto){
        Character oldCharacter = characterFromId(id);   //This is to efficiently check whether the ID exists
        Character newCharacter = characterFromDto(dto);
        newCharacter.setId(oldCharacter.getId());
        characterRepos.save(newCharacter);
        return dtoFromCharacter(newCharacter);
    }

    public void deleteCharacter(Long id){
        Optional<Character> characterOptional = characterRepos.findById(id);
        if (characterOptional.isPresent()) {
            characterRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("The character corresponding to ID:"+id+" could not be found in the database.");
        }
    }

    public Long characterIdFromName(String name){
        Long result = 0l;
        List<Character> characterList = characterRepos.findAll();
        if (!characterList.isEmpty()){
            for (Character character : characterList){
                if (name.equalsIgnoreCase(character.getName())){
                    result = character.getId();
                }
            }
        }
        if(result == 0l){
            throw new RecordNotFoundException("The Character '"+name+"' could not be found in the database.");
        }
        return result;
    }
    public Character characterFromId(Long id){
        Character result;
        Optional<Character> optionalCharacter = characterRepos.findById(id);
        if (optionalCharacter.isPresent()){
            result = optionalCharacter.get();
        } else {
            throw new RecordNotFoundException("The character corresponding to ID:"+id+" could not be found in the database.");
        }
        return result;
    }

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
