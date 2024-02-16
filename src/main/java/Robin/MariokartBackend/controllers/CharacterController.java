package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.CharacterDto;
import Robin.MariokartBackend.inputDtos.CharacterInputDto;
import Robin.MariokartBackend.services.CharacterService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/characters")
@RestController
public class CharacterController {

    @Autowired
    private CharacterService characterService;
    @GetMapping
    public ResponseEntity<List<CharacterDto>> getAllCharacters() {
        List<CharacterDto> characterDtoList;
        characterDtoList = characterService.getAllCharacters();
        return ResponseEntity.ok(characterDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CharacterDto> getCharacter(@PathVariable Long id) {
        return ResponseEntity.ok(characterService.getCharacter(id));
    }

    @PostMapping
    public ResponseEntity<CharacterDto> addCharacter (@Valid @RequestBody CharacterInputDto inputDto){
        CharacterDto addedCharacterDto = characterService.addCharacter(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedCharacterDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedCharacterDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CharacterDto> updateCharacter(@Valid @PathVariable Long id, @RequestBody CharacterInputDto inputDto) {
        CharacterDto edittedCharacterDto = characterService.editCharacter(id, inputDto);
        return ResponseEntity.ok(edittedCharacterDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCharacter(@PathVariable Long id){
        characterService.deleteCharacter(id);
        return new ResponseEntity<>("Character "+id+" succesfully deleted!", HttpStatus.OK);
    }
}
