package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.RecordInputDto;
import Robin.MariokartBackend.services.RecordService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/records")
@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;
    @GetMapping
    public ResponseEntity<List<RecordDto>> getAllRecords() {
        List<RecordDto> recordDtoList;
        recordDtoList = recordService.getAllRecords();
        return ResponseEntity.ok(recordDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecordDto> getRecord(@PathVariable Long id) {
        return ResponseEntity.ok(recordService.getRecord(id));
    }

    @PostMapping
    public ResponseEntity<RecordDto> addRecord (@Valid @RequestBody RecordInputDto inputDto){
        RecordDto addedRecordDto = recordService.addRecord(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedRecordDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedRecordDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RecordDto> updateRecord(@Valid @PathVariable Long id, @RequestBody RecordInputDto inputDto) {
        RecordDto edittedRecordDto = recordService.editRecord(id, inputDto);
        return ResponseEntity.ok(edittedRecordDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRecord(@PathVariable Long id){
        recordService.deleteRecord(id);
        return new ResponseEntity<>("Record succesfully deleted", HttpStatus.OK);
    }

//    @PutMapping("/{id}/set_character")
//    public ResponseEntity<RecordDto> assignProfile(@Valid @PathVariable Long id, @RequestBody IdInputDto profileId) {
//        RecordDto edittedRecordDto = recordService.assignCharacter(id, profileId);
//        return ResponseEntity.ok(edittedRecordDto);
//    }

}
