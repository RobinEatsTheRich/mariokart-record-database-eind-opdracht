package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.dtos.RecordDtoForCourse;
import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.inputDtos.RecordInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.repository.*;
import Robin.MariokartBackend.security.MyUserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordService {


    private final RecordRepository recordRepos;
    private final CharacterService characterService;
    private final CourseRepository courseRepos;
    private final CourseService courseService;
    private final KartPartService kartpartService;

    public RecordService(RecordRepository recordRepos,
                         CharacterService characterService,
                         CourseRepository courseRepos,
                         CourseService courseService,
                         KartPartService kartpartService) {
        this.recordRepos = recordRepos;
        this.characterService = characterService;
        this.courseRepos = courseRepos;
        this.courseService = courseService;
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

    public List<RecordDto> getAllRivalRecords(MyUserDetails myUserDetails){
        List<Record> recordList = recordRepos.findAll();
        List<RecordDto> recordDtoList = new ArrayList<>();
        for(Record record : recordList)
        {
            if(myUserDetails.getRivals().contains(record.getProfile())){
                recordDtoList.add(dtoFromRecord(record));
            }
        }
        return recordDtoList;
    }

    public RecordDto getRecord(Long id){
        return dtoFromRecord(recordFromId(id));
    }

    public RecordDto createRecord(RecordInputDto dto){
        Record record = recordFromDto(dto);
        recordRepos.save(record);
        courseService.assignRecord(record.getCourse(),record);
        return dtoFromRecord(record);
    }

    public RecordDto editRecord(MyUserDetails myUserDetails, Long id, RecordInputDto dto){
        Record oldRecord = recordFromId(id);
        String recordOwner = oldRecord.getProfile().getUsername();
        if (!myUserDetails.getUsername().equals(recordOwner) &&
                !myUserDetails.getUserRoles().contains(UserRole.ADMIN)){
            throw new ForbiddenException("You are logged in as "+myUserDetails.getUsername()+", not as "+recordOwner+".");
        }
        Record newRecord = recordFromDto(dto);
        newRecord.setId(oldRecord.getId());
        recordRepos.save(newRecord);
        return dtoFromRecord(newRecord);
    }

    public void deleteRecord(MyUserDetails myUserDetails, Long id){
        Record record = recordFromId(id);
        String recordOwner = record.getProfile().getUsername();
        if (!myUserDetails.getUsername().equals(recordOwner) &&
                !myUserDetails.getUserRoles().contains(UserRole.ADMIN)){
            throw new ForbiddenException("You are logged in as "+myUserDetails.getUsername()+", not as "+recordOwner+".");
        }
        recordRepos.deleteById(id);
    }

    public String stringFromTimeFloat(float time){
        String result = Float.toString(time);
        for (int i = result.length(); i < 7; i++){
            result = result+"0";
        }
        String substring1 = result.substring(0,1);
        String substring2 = result.substring(2,4);
        String substring3 = result.substring(4,7);
        result = substring1+":"+substring2+"."+substring3;

        return result;
    }

    public boolean checkTimeForValid(float time){
        String stringTime = Float.toString(time);
        if (    stringTime.length() > 7 ||
                (time >= 0.6 && time < 1) ||
                (time >= 1.6 && time < 2) ||
                (time >= 2.6 && time < 3) ||
                (time >= 3.6 && time < 4) ||
                (time >= 4.6 && time < 5) ||
                (time >= 5.6 && time < 6) ||
                (time >= 6.6 && time < 7) ||
                (time >= 7.6 && time < 8) ||
                (time >= 8.6 && time < 9) ||
                (time >= 9.6)){
            return false;
        }
        return true;
    }

    public List<RecordDto> dtoListFromRecordList(List<Record> recordList){
        List<RecordDto> recordDtoList = new ArrayList<>();
        if (recordList != null && !recordList.isEmpty()) {
            for (Record record : recordList) {
                RecordDto recordDto = dtoFromRecord(record);
                recordDtoList.add(recordDto);
            }
        } else {
            throw new RecordNotFoundException("The list of records to be converted was empty");
        }
        return recordDtoList;
    }
    public List<RecordDtoForCourse> dtoForCoursesListFromRecordList(List<Record> recordList){
        List<RecordDtoForCourse> recordDtoList = new ArrayList<>();
        if (recordList != null && !recordList.isEmpty()) {
            for (Record record : recordList) {
                RecordDtoForCourse recordDto = dtoForCoursesFromRecord(record);
                recordDtoList.add(recordDto);
            }
        } else {
            throw new RecordNotFoundException("The list of records to be converted was empty");
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
        dto.setTotalTime(stringFromTimeFloat(record.getTotalTime()));
        dto.setLap1(stringFromTimeFloat(record.getLap1()));
        dto.setLap2(stringFromTimeFloat(record.getLap2()));
        dto.setLap3(stringFromTimeFloat(record.getLap3()));
        if (record.getLap4() > 0 && record.getLap5() > 0 && record.getLap6() > 0 && record.getLap7() > 0){
            dto.setLap4(stringFromTimeFloat(record.getLap4()));
            dto.setLap5(stringFromTimeFloat(record.getLap5()));
            dto.setLap6(stringFromTimeFloat(record.getLap6()));
            dto.setLap7(stringFromTimeFloat(record.getLap7()));
        }
        dto.setIs200CC(record.isIs200CC());
        dto.setCharacter(
                characterService.dtoFromCharacter(characterService.characterFromId(record.getCharacterId()))
        );
        dto.setBody(
                kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getBodyId()))
        );
        dto.setWheels(
                kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getWheelsId()))
        );
        dto.setGlider(
                kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getGliderId()))
        );
        if (record.getProfile() != null && record.getProfile().getUser() != null){
            dto.setRecordHolder(record.getProfile().getUser().getUsername());
        }
        return dto;
    }

    public RecordDto dtoFromRecord(Record record) {
        RecordDto dto = new RecordDto();
        dto.setId(record.getId());
        dto.setTotalTime(stringFromTimeFloat(record.getTotalTime()));
        dto.setLap1(stringFromTimeFloat(record.getLap1()));
        dto.setLap2(stringFromTimeFloat(record.getLap2()));
        dto.setLap3(stringFromTimeFloat(record.getLap3()));
        if (record.getLap4() > 0 && record.getLap5() > 0 && record.getLap6() > 0 && record.getLap7() > 0){
            dto.setLap4(stringFromTimeFloat(record.getLap4()));
            dto.setLap5(stringFromTimeFloat(record.getLap5()));
            dto.setLap6(stringFromTimeFloat(record.getLap6()));
            dto.setLap7(stringFromTimeFloat(record.getLap7()));
        }
        dto.setIs200CC(record.isIs200CC());
        dto.setCharacter(characterService.dtoFromCharacter(characterService.characterFromId(record.getCharacterId())));
        dto.setCourse(courseService.dtoForRecordFromCourse(courseService.courseFromId(record.getCourse().getId())));
        dto.setBody(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getBodyId())));
        dto.setWheels(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getWheelsId())));
        dto.setGlider(kartpartService.dtoFromKartPart(kartpartService.kartPartFromId(record.getGliderId())));
        if (record.getProfile() != null && record.getProfile().getUser() != null){
            dto.setRecordHolder(record.getProfile().getUser().getUsername());
        }
        return dto;
    }

    public Record recordFromDto (RecordInputDto dto) {

        //Some extra validation before setting the values
        boolean isBabyPark = (dto.getCourseName().equalsIgnoreCase("baby park"));
        checkTimeForValid(dto.getTotalTime());
        if ((isBabyPark &&
                !checkTimeForValid(dto.getLap1()) ||
                !checkTimeForValid(dto.getLap2()) ||
                !checkTimeForValid(dto.getLap3()) ||
                !checkTimeForValid(dto.getLap4()) ||
                !checkTimeForValid(dto.getLap5()) ||
                !checkTimeForValid(dto.getLap6()) ||
                !checkTimeForValid(dto.getLap7()))
                || (!isBabyPark &&
                !checkTimeForValid(dto.getLap1()) ||
                !checkTimeForValid(dto.getLap2()) ||
                !checkTimeForValid(dto.getLap3()))){
            throw new ForbiddenException("some of the total- or lap-times are not in proper format.");
        }

        Record record = new Record();
        record.setId(dto.getId());
        record.setTotalTime(dto.getTotalTime());
        record.setLap1(dto.getLap1());
        record.setLap2(dto.getLap2());
        record.setLap3(dto.getLap3());
        if (isBabyPark && (dto.getLap4() > 0 || dto.getLap5() > 0 || dto.getLap6() > 0 || dto.getLap7() > 0)){
            record.setLap4(dto.getLap4());
            record.setLap5(dto.getLap5());
            record.setLap6(dto.getLap6());
            record.setLap7(dto.getLap7());
        } else if (isBabyPark && (dto.getLap4() == 0 || dto.getLap5() == 0 || dto.getLap6() == 0 || dto.getLap7() == 0)){
            throw new ForbiddenException("The course 'Baby Park' has 7 Laps instead of 3, please adjust request appropriately.");
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
