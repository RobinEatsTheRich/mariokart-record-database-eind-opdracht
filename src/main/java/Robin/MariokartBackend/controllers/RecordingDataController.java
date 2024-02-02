package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.RecordingRepository;
import Robin.MariokartBackend.repository.UserRepository;
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
    private final RecordingRepository recordingRepository;
    private final UserRepository userRepository;

    public RecordingDataController(RecordingDataService recordingDataService, RecordingRepository recordingRepository, UserRepository userRepository) {
        this.recordingDataService = recordingDataService;
        this.recordingRepository = recordingRepository;
        this.userRepository = userRepository;
    }


    @PostMapping
    public ResponseEntity<Object> uploadRecordingData(@RequestParam("file")MultipartFile multipartFile,
                                                      @RequestParam String username) throws IOException {
        String recording = recordingDataService.uploadRecording(multipartFile,username);
        return ResponseEntity.ok("file has been uploaded, "+ recording);
    }

    @GetMapping("/{username}")
    public ResponseEntity<Object> downloadRecording(@PathVariable("username") String username) throws IOException {
        byte[] recording = recordingDataService.downloadRecording(username);

        Optional<User> optionalUser = userRepository.findById(username);
        User user = new User();
        if (optionalUser.isPresent()){
            user = optionalUser.get();
        }
        Optional<RecordingData> optionalRecordingData = recordingRepository.findById(user.getRecordingData().getId());
        MediaType mediaType = MediaType.valueOf(optionalRecordingData.get().getType());
        return ResponseEntity.ok().contentType(mediaType).body(recording);


    }
}
