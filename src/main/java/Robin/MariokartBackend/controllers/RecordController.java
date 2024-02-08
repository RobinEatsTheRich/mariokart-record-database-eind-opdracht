package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.RecordingUtil;
import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.RecordInputDto;
import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.services.RecordService;
import Robin.MariokartBackend.services.RecordingDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RequestMapping("/records")
@RestController
public class RecordController {

    @Autowired
    private RecordService recordService;
    @Autowired
    private RecordingDataService recordingDataService;
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
    public ResponseEntity<RecordDto> createRecord (@Valid @RequestBody RecordInputDto inputDto){
        RecordDto addedRecordDto = recordService.createRecord(inputDto);
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
        return new ResponseEntity<>("Record "+id+" succesfully deleted!", HttpStatus.OK);
    }
    @PutMapping("/{id}/recording")
    public ResponseEntity<Object> uploadRecordingData(@RequestParam("file") MultipartFile multipartFile, @PathVariable("id") Long recordId) throws IOException {
        String recording = recordingDataService.uploadRecording(multipartFile,recordId);
        return ResponseEntity.ok("file has been uploaded: "+ recording);
    }

    @GetMapping("/{id}/recording")
    public ResponseEntity<Object> downloadRecording(@PathVariable("id") Long recordId){
        RecordingData recordingData = recordingDataService.downloadRecording(recordId);
        byte[] recording = RecordingUtil.decompressRecording(recordingData.getRecordingData());
        return ResponseEntity.ok().contentType(MediaType.valueOf(recordingData.getType())).body(recording);
    }
}
