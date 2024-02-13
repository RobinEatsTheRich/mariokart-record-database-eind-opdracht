package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.dtos.RecordDtoForCourse;
import Robin.MariokartBackend.inputDtos.RecordInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {


    private final RecordRepository recordRepos;
    private final CharacterRepository characterRepos;
    private final CharacterService characterService;
    private final CourseRepository courseRepos;
    private final CourseService courseService;
    private final KartPartRepository kartPartRepos;
    private final KartPartService kartpartService;

    @Autowired
    public RecordService(RecordRepository recordRepos,
                         CharacterRepository characterRepos,
                         CharacterService characterService,
                         CourseRepository courseRepos,
                         CourseService courseService,
                         KartPartRepository kartPartRepos,
                         KartPartService kartpartService) {
        this.recordRepos = recordRepos;
        this.characterRepos = characterRepos;
        this.characterService = characterService;
        this.courseRepos = courseRepos;
        this.courseService = courseService;
        this.kartPartRepos = kartPartRepos;
        this.kartpartService = kartpartService;
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
        return dtoFromRecord(recordFromId(id));
    }

    public RecordDto createRecord(RecordInputDto dto){
        Record record = recordFromDto(dto);
        recordRepos.save(record);
        courseService.assignRecord(record.getCourse(),record));
        return dtoFromRecord(record);
    }

    public RecordDto editRecord(Long id, RecordInputDto dto){
        Record oldRecord = recordFromId(id);
        Record newRecord = recordFromDto(dto);
        if (newRecord.getId() != null){
            newRecord.setId(oldRecord.getId());
        }
        recordRepos.save(newRecord);
        return dtoFromRecord(newRecord);
    }

    public void deleteRecord(Long id){
        Optional<Record> recordOptional = recordRepos.findById(id);
        if (recordOptional.isPresent()) {
            recordRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("The record corresponding to ID:"+id+" could not be found in the database");
        }
    }

    public String stringFromTimefloat(float f){
        String result = Float.toString(f);
        String substring1 = result.substring(0,1);
        String substring2 = result.substring(2,4);
        String substring3 = result.substring(4);
        result = substring1+":"+substring2+"."+substring3;

        return result;
    }

    public List<RecordDto> dtoListfromRecordList(List<Record> recordList){
        List<RecordDto> recordDtoList = new ArrayList<>();
        if (recordList != null && !recordList.isEmpty()) {
            for (Record record : recordList) {
                RecordDto recordDto = dtoFromRecord(record);
                recordDtoList.add(recordDto);
            }
        }
        return recordDtoList;
    }
    public List<RecordDtoForCourse> dtoForCoursesListfromRecordList(List<Record> recordList){
        List<RecordDtoForCourse> recordDtoList = new ArrayList<>();
        if (recordList != null && !recordList.isEmpty()) {
            for (Record record : recordList) {
                RecordDtoForCourse recordDto = dtoForCoursesFromRecord(record);
                recordDtoList.add(recordDto);
            }
        }
        return recordDtoList;
    }

    public Record recordFromId(Long id){
        Record result;
        Optional<Record> recordOptional = recordRepos.findById(id);
        if (recordOptional.isPresent())
        {
            result = recordOptional.get();
        } else{
            throw new RecordNotFoundException("The record corresponding to ID:"+id+" could not be found in the database");
        }
        return result;
    }

    public RecordDtoForCourse dtoForCoursesFromRecord(Record record) {
        RecordDtoForCourse dto = new RecordDtoForCourse();
        dto.setId(record.getId());
        dto.setTotalTime(stringFromTimefloat(record.getTotalTime()));
        dto.setLap1(stringFromTimefloat(record.getLap1()));
        dto.setLap2(stringFromTimefloat(record.getLap2()));
        dto.setLap3(stringFromTimefloat(record.getLap3()));
        if (record.getLap4() > 0){
            dto.setLap4(stringFromTimefloat(record.getLap4()));
        }
        if (record.getLap5() > 0){
            dto.setLap5(stringFromTimefloat(record.getLap5()));
        }
        if (record.getLap6() > 0){
            dto.setLap6(stringFromTimefloat(record.getLap6()));
        }
        if (record.getLap7() > 0){
            dto.setLap7(stringFromTimefloat(record.getLap7()));
        }
        dto.setIs200CC(record.isIs200CC());
        if (record.getCharacterId() != null){
            dto.setCharacter(
                    characterService.dtoFromCharacter(characterService.characterFromId(record.getCharacterId()))
            );
        }
        if (record.getBodyId() != null){
            dto.setBody(
                    kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getBodyId()))
            );
        }
        if (record.getWheelsId() != null){
            dto.setWheels(
                    kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getWheelsId()))
            );
        }
        if (record.getGliderId() != null){
            dto.setGlider(
                    kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getGliderId()))
            );
        }
        if (record.getProfile() != null && record.getProfile().getUser() != null){
            dto.setRecordHolder(record.getProfile().getUser().getUsername());
        }

        return dto;
    }

    public RecordDto dtoFromRecord(Record record) {
        RecordDto dto = new RecordDto();
        dto.setId(record.getId());
        dto.setTotalTime(stringFromTimefloat(record.getTotalTime()));
        dto.setLap1(stringFromTimefloat(record.getLap1()));
        dto.setLap2(stringFromTimefloat(record.getLap2()));
        dto.setLap3(stringFromTimefloat(record.getLap3()));
        if (record.getLap4() > 0){
            dto.setLap4(stringFromTimefloat(record.getLap4()));
        }
        if (record.getLap5() > 0){
            dto.setLap5(stringFromTimefloat(record.getLap5()));
        }
        if (record.getLap6() > 0){
            dto.setLap6(stringFromTimefloat(record.getLap6()));
        }
        if (record.getLap7() > 0){
            dto.setLap7(stringFromTimefloat(record.getLap7()));
        }
        dto.setIs200CC(record.isIs200CC());
        if (record.getCharacterId() != null){
            dto.setCharacter(
                    characterService.dtoFromCharacter(characterService.characterFromId(record.getCharacterId()))
            );
        }
        if (record.getCourse() != null){
            dto.setCourse(
                    courseService.dtoForRecordFromCourse(courseService.courseFromId(record.getCourse().getId()))

            );
        }
        if (record.getBodyId() != null){
            dto.setBody(
                    kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getBodyId()))
            );
        }
        if (record.getWheelsId() != null){
            dto.setWheels(
                    kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getWheelsId()))
            );
        }
        if (record.getGliderId() != null){
            dto.setGlider(
                    kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getGliderId()))
            );
        }
        if (record.getProfile() != null && record.getProfile().getUser() != null){
            dto.setRecordHolder(record.getProfile().getUser().getUsername());
        }

        return dto;
    }

    public Record recordFromDto (RecordInputDto dto) {
        Record record = new Record();
        record.setId(dto.getId());
        record.setTotalTime(dto.getTotalTime());
        record.setLap1(dto.getLap1());
        record.setLap2(dto.getLap2());
        record.setLap3(dto.getLap3());
        if (dto.getLap4() > 0){
            record.setLap4(dto.getLap4());
        }
        if (dto.getLap5() > 0){
            record.setLap5(dto.getLap5());
        }
        if (dto.getLap6() > 0){
            record.setLap6(dto.getLap6());
        }
        if (dto.getLap7() > 0){
            record.setLap7(dto.getLap7());
        }
        record.setIs200CC(dto.isIs200CC());
        record.setCharacterId(characterService.characterIdFromName(dto.getCharacterName()));
        record.setCourse(courseRepos.getReferenceById(courseService.courseIdFromName(dto.getCourseName())));
        record.setBodyId(kartpartService.kartPartIdFromName(dto.getBodyName()));
        record.setWheelsId(kartpartService.kartPartIdFromName(dto.getWheelsName()));
        record.setGliderId(kartpartService.kartPartIdFromName(dto.getGliderName()));
        return record;
    }
}
