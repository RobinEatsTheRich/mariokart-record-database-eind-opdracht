package Robin.MariokartBackend.services;

import Robin.MariokartBackend.RecordingUtil;
import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.repository.RecordRepository;
import Robin.MariokartBackend.repository.RecordingDataRepository;
import Robin.MariokartBackend.security.MyUserDetails;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class RecordingDataService {
    private final RecordingDataRepository recordingDataRepository;
    private final RecordRepository recordRepository;
    private final RecordService recordService;


    public RecordingDataService(RecordingDataRepository recordingDataRepository, RecordRepository recordRepository, RecordService recordService) {
        this.recordingDataRepository = recordingDataRepository;
        this.recordRepository = recordRepository;
        this.recordService = recordService;
    }

    public String uploadRecording(MyUserDetails myUserDetails, MultipartFile multipartFile, Long recordId) throws IOException {
        Record record = recordService.recordFromId(recordId);
        if (record.getProfile() == null){
            throw new ForbiddenException("This Record is not assigned to a profile yet, so you cannot be the owner.");
        } else if (!record.getProfile().getUsername().equals(myUserDetails.getUsername()) &&
                !myUserDetails.getUserRoles().contains(UserRole.ADMIN)){
            throw new ForbiddenException("You are not the owner of this record, nor logged in as an admin.");
        } else if (multipartFile == null || multipartFile.isEmpty()){
            throw new BadRequestException("No file attached.");

        } else if (record.getRecordingData() != null){
            throw new BadRequestException("This record already has a recording attached.");
        } else {
            RecordingData recordingData = new RecordingData();
            recordingData.setName(multipartFile.getName());
            recordingData.setType(multipartFile.getContentType());
            recordingData.setRecordingData(RecordingUtil.compressRecording(multipartFile.getBytes()));
            recordingData.setRecord(record);
            RecordingData savedRecording = recordingDataRepository.save(recordingData);
            record.setRecordingData(recordingData);
            recordRepository.save(record);
            return savedRecording.getName();
        }
    }

    public void deleteRecording(MyUserDetails myUserDetails, Long recordId){
        Record record = recordService.recordFromId(recordId);
        if (!record.getProfile().getUsername().equals(myUserDetails.getUsername()) &&
                !myUserDetails.getUserRoles().contains(UserRole.ADMIN)){
            throw new ForbiddenException("You are not the owner of this record, nor logged in as an admin.");
        }
        if (record.getRecordingData() == null){
            throw new RecordNotFoundException("The record corresponding to "+recordId+" doesn't have a recording to be deleted.");
        } else {
            RecordingData recordingData = record.getRecordingData();
            recordingDataRepository.deleteById(recordingData.getId());
            record.setRecordingData(null);
        }
    }

    public RecordingData downloadRecording(Long recordId){
        Record record = recordService.recordFromId(recordId);
        RecordingData recordingData = record.getRecordingData();
        if (recordingData == null){
            throw new RecordNotFoundException("The record corresponding to the ID"+recordId+" does not have a recording attached.");
        }
        return  recordingData;
    }
}
