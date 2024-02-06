package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.ProfileInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Profile;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.ProfileRepository;
import Robin.MariokartBackend.repository.RecordRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {


    private final ProfileRepository profileRepos;
    private final RecordRepository recordRepos;
    private final RecordService recordService;

    public ProfileService(ProfileRepository profileRepos, RecordRepository recordRepos, RecordService recordService) {
        this.profileRepos = profileRepos;
        this.recordRepos = recordRepos;
        this.recordService = recordService;
    }


    public List<ProfileDto> getAllProfiles(){
        List<Profile> profileList = profileRepos.findAll();
        List<ProfileDto> profileDtoList = new ArrayList<>();
        for(Profile Profile : profileList)
        {
            profileDtoList.add(dtoFromProfile(Profile));
        }
        return profileDtoList;
    }

    public ProfileDto getProfile(String username){
        return dtoFromProfile(profileFromName(username));
    }

    public ProfileDto createProfile(String username, User user){
        Profile profile = new Profile(username, user);
        profileRepos.save(profile);
        return dtoFromProfile(profileFromName(username));
    }

    public ProfileDto editProfile(String username, ProfileInputDto dto){
        ProfileDto result;
        Profile profile = profileFromDto(dto);
        profileRepos.save(profile);
        result = dtoFromProfile(profileFromName(username));
        return result;
    }
    public void deleteProfile(String username){
        Optional<Profile> profileOptional = profileRepos.findById(username);
        if (profileOptional.isPresent()) {
            profileRepos.deleteById(username);
        } else {
            throw new RecordNotFoundException("The profile belonging to "+username+" could not be found in the database");
        }
    }

    public ProfileDto assignRecord(String username, Long recordId){
        Record record = recordService.recordFromId(recordId);
        Profile profile = profileFromName(username);
        record.setProfile(profile);
        recordRepos.save(record);
        record = recordService.recordFromId(recordId);
        List<Record> recordList = profile.getRecords();
        recordList.add(record);
        profile.setRecords(recordList);
        profileRepos.save(profile);
        return dtoFromProfile(profileFromName(username));
    }

    public Profile profileFromName(String username){
        Profile result;
        Optional<Profile> profileOptional = profileRepos.findById(username);
        if (profileOptional.isPresent())
        {
            result = profileOptional.get();
        } else{
            throw new RecordNotFoundException("The profile belonging to "+username+" could not be found in the database");
        }
        return result;
    }

    public List<RecordDto> dtoListfromRecordList(List<Record> recordList){
        List<RecordDto> recordDtoList = new ArrayList<>();
        if (recordList != null && !recordList.isEmpty()) {
            for (int i = 0; i == recordList.size(); i++) {
                    RecordDto recordDto = recordService.dtoFromRecord(recordList.get(i));
                    recordDtoList.add(recordDto);
            }
        }
        return recordDtoList;
    }

    public ProfileDto dtoFromProfile(Profile profile) {
        ProfileDto dto = new ProfileDto();
        dto.setUserName(profile.getUserName());
        if (profile.getRecords() != null){
            dto.setRecords(dtoListfromRecordList(profile.getRecords()));
        }
        dto.setNintendoCode(profile.getNintendoCode());
        return dto;
    }

    public Profile profileFromDto (ProfileInputDto dto) {
        Profile profile = new Profile();
        profile.setNintendoCode(dto.getNintendoCode());
        return profile;
    }
}
