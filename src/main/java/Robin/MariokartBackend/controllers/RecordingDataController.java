package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.RecordRepository;
import Robin.MariokartBackend.repository.RecordingDataRepository;
import Robin.MariokartBackend.services.RecordingDataService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/recordingData")
public class RecordingDataController {

    private final RecordingDataService recordingDataService;
    private final RecordingDataRepository recordingDataRepository;
    private final RecordRepository recordRepository;

    public RecordingDataController(RecordingDataService recordingDataService, RecordingDataRepository recordingDataRepository, RecordRepository recordRepository) {
        this.recordingDataService = recordingDataService;
        this.recordingDataRepository = recordingDataRepository;
        this.recordRepository = recordRepository;
    }


    @PostMapping
    public ResponseEntity<Object> uploadRecordingData(@RequestParam("file")MultipartFile multipartFile,
                                                      @RequestParam Long recordId) throws IOException {
        String recording = recordingDataService.uploadRecording(multipartFile,recordId);
        return ResponseEntity.ok("file has been uploaded: "+ recording);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> downloadRecording(@PathVariable("id") Long recordId) throws IOException {
        byte[] recording = recordingDataService.downloadRecording(recordId);

        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        Record record = new Record();
        if (optionalRecord.isPresent()){
            record = optionalRecord.get();
        }
        Optional<RecordingData> optionalRecordingData = recordingDataRepository.findById(record.getRecordingData().getId());
        MediaType mediaType = MediaType.valueOf(optionalRecordingData.get().getType());
        return ResponseEntity.ok().contentType(mediaType).body(recording);


    }
}
