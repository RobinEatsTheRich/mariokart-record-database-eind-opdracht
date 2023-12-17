package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.inputDtos.RecordInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.RecordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {


    private final RecordRepository recordRepos;


    public RecordService(RecordRepository recordRepos) {
        this.recordRepos = recordRepos;
    }


    public List<RecordDto> getAllRecords(){
        List<Record> recordList = recordRepos.findAll();
        List<RecordDto> recordDtoList = new ArrayList<>();
        for(Record record : recordList)
        {
            recordDtoList.add(dtoFromRecord(record));
        }
        return recordDtoList;
    }

    public RecordDto getRecord(Long id){
        Optional<Record> recordOptional = recordRepos.findById(id);
        if (recordOptional.isPresent()) {
            Record record = recordOptional.get();
            return dtoFromRecord(record);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public RecordDto addRecord(RecordInputDto dto){
        Record record = recordFromDto(dto);
        recordRepos.save(record);
        return dtoFromRecord(record);
    }

    public RecordDto editRecord(Long id, RecordInputDto dto){
        Optional<Record> recordOptional = recordRepos.findById(id);
        if (recordOptional.isPresent()) {
            Record ogRecord = recordOptional.get();
            Record record = recordFromDto(dto);
            record.setId(ogRecord.getId());

            recordRepos.save(record);

            return dtoFromRecord(record);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteRecord(Long id){
        Optional<Record> recordOptional = recordRepos.findById(id);
        if (recordOptional.isPresent()) {
            recordRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

//    public RecordDto assignRemoteControllerToRecord(Long id, Long remoteControllerId){
//        Optional<Record> RecordOptional = RecordRepos.findById(id);
//        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);
//
//        if(RecordOptional.isPresent() && remoteControllerOptional.isPresent()){
//            Record Record = RecordOptional.get();
//            RemoteController remoteController = remoteControllerOptional.get();
//            Record.setRemoteController(remoteController);
//            RecordRepos.save(Record);
//            return dtoFromRecord(Record);
//
//        }else if(RecordOptional.isPresent() && remoteControllerOptional.isEmpty()){
//            throw new RecordNotFoundException("Remote Controller ID cannot be found");
//
//        }else if(RecordOptional.isEmpty() && remoteControllerOptional.isPresent()){
//            throw new RecordNotFoundException("Record ID cannot be found");
//
//        }else{
//            throw new RecordNotFoundException("Neither ID can be found");
//        }
//    }

    public RecordDto dtoFromRecord(Record record) {
        RecordDto dto = new RecordDto();
        dto.setId(record.getId());
        dto.setCourse(record.getCourse());
        dto.setTotalTime(record.getTotalTime());
        dto.setLap1(record.getLap1());
        dto.setLap2(record.getLap2());
        dto.setLap3(record.getLap3());
        if (record.getLap4() != null);
        {
            dto.setLap4(record.getLap4());
        }
        if (record.getLap5() != null);
        {
            dto.setLap5(record.getLap5());
        }
        if (record.getLap6() != null);
        {
            dto.setLap6(record.getLap6());
        }
        if (record.getLap7() != null);
        {
            dto.setLap7(record.getLap7());
        }
        dto.setIs200CC(record.isIs200CC());
        dto.setKart(record.getKart());
        dto.setCharacter(record.getCharacter());
        dto.setProfile(record.getProfile());

        return dto;
    }

    public Record recordFromDto (RecordInputDto dto) {
        Record record = new Record();
        record.setId(dto.getId());
        record.setTotalTime(dto.getTotalTime());
        record.setLap1(dto.getLap1());
        record.setLap2(dto.getLap2());
        record.setLap3(dto.getLap3());
        if (dto.getLap4() != null);
        {
            record.setLap4(dto.getLap4());
        }
        if (dto.getLap5() != null);
        {
            record.setLap5(dto.getLap5());
        }
        if (dto.getLap6() != null);
        {
            record.setLap6(dto.getLap6());
        }
        if (dto.getLap7() != null);
        {
            record.setLap7(dto.getLap7());
        }
        record.setIs200CC(dto.isIs200CC());
        return record;
    }
}
