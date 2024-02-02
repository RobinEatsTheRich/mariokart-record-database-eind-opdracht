package Robin.MariokartBackend.services;

import Robin.MariokartBackend.RecordingUtil;
import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.RecordingRepository;
import Robin.MariokartBackend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class RecordingDataService {
    private final RecordingRepository recordingRepository;
    private final UserRepository userRepository;


    public RecordingDataService(RecordingRepository recordingRepository, UserRepository userRepository) {
        this.recordingRepository = recordingRepository;
        this.userRepository = userRepository;
    }

    public String uploadRecording(MultipartFile multipartFile, String username) throws IOException {
        Optional<User> optionalUser = userRepository.findById(username);
        User user = new User();
        if (optionalUser.isPresent()){
            user = optionalUser.get();
        }

        RecordingData recordingData = new RecordingData();
        recordingData.setName(multipartFile.getName());
        recordingData.setType(multipartFile.getContentType());
        recordingData.setRecordingData(RecordingUtil.compressRecording(multipartFile.getBytes()));
        recordingData.setUser(user);

        RecordingData savedRecording = recordingRepository.save(recordingData);
        user.setRecordingData(recordingData);
        userRepository.save(user);
        return savedRecording.getName();
    }

    public byte[] downloadRecording(String username) throws IOException {
        Optional<User> optionalUser = userRepository.findById(username);
        User user = new User();
        if (optionalUser.isPresent()){
            user = optionalUser.get();
        }

        RecordingData recordingData = user.getRecordingData();
        return RecordingUtil.decompressRecording(recordingData.getRecordingData());
    }
}
