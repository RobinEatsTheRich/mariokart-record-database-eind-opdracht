package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.RecordingUtil;
import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.services.RecordingDataService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/recordingData")
public class RecordingDataController {

    private final RecordingDataService recordingDataService;

    public RecordingDataController(RecordingDataService recordingDataService) {
        this.recordingDataService = recordingDataService;
    }


    @PostMapping
    public ResponseEntity<Object> uploadRecordingData(@RequestParam("file")MultipartFile multipartFile,
                                                      @RequestParam Long recordId) throws IOException {
        String recording = recordingDataService.uploadRecording(multipartFile,recordId);
        return ResponseEntity.ok("file has been uploaded: "+ recording);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> downloadRecording(@PathVariable("id") Long recordId){
        RecordingData recordingData = recordingDataService.downloadRecording(recordId);
        byte[] recording = RecordingUtil.decompressRecording(recordingData.getRecordingData());
        return ResponseEntity.ok().contentType(MediaType.valueOf(recordingData.getType())).body(recording);
    }
}
