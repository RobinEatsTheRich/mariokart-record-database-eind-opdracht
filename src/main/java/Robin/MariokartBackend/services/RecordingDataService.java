package Robin.MariokartBackend.services;

import Robin.MariokartBackend.RecordingUtil;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.repository.RecordRepository;
import Robin.MariokartBackend.repository.RecordingDataRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class RecordingDataService {
    private final RecordingDataRepository recordingDataRepository;
    private final RecordRepository recordRepository;


    public RecordingDataService(RecordingDataRepository recordingDataRepository, RecordRepository recordRepository) {
        this.recordingDataRepository = recordingDataRepository;
        this.recordRepository = recordRepository;
    }

    public String uploadRecording(MultipartFile multipartFile, Long recordId) throws IOException {
        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        Record record = new Record();
        if (optionalRecord.isPresent()){
            record = optionalRecord.get();
        }

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

    public byte[] downloadRecording(Long recordId) throws IOException {
        Optional<Record> optionalRecord = recordRepository.findById(recordId);
        Record record = new Record();
        if (optionalRecord.isPresent()){
            record = optionalRecord.get();
        }

        RecordingData recordingData = record.getRecordingData();
        return RecordingUtil.decompressRecording(recordingData.getRecordingData());
    }
}
