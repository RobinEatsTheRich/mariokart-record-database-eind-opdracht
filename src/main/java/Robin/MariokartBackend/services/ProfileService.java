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

    public ProfileDto getProfile(Long id){
        Optional<Profile> profileOptional = profileRepos.findById(id);
        if (profileOptional.isPresent()) {
            Profile profile = profileOptional.get();
            return dtoFromProfile(profile);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public ProfileDto addProfile(ProfileInputDto dto){
        Profile profile = profileFromDto(dto);
        profileRepos.save(profile);
        return dtoFromProfile(profile);
    }

    public ProfileDto editProfile(Long id, ProfileInputDto dto){
        Optional<Profile> profileOptional = profileRepos.findById(id);
        if (profileOptional.isPresent()) {
            Profile ogProfile = profileOptional.get();
            Profile profile = profileFromDto(dto);
            profile.setId(ogProfile.getId());

            profileRepos.save(profile);

            return dtoFromProfile(profile);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }
    public void deleteProfile(Long id){
        Optional<Profile> profileOptional = profileRepos.findById(id);
        if (profileOptional.isPresent()) {
            profileRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public ProfileDto assignRecord(Long userId, IdInputDto profileId){
        Optional<Profile> profileOptional = profileRepos.findById(userId);
        Optional<Record> recordOptional = recordRepos.findById(profileId.id);
        if (recordOptional.isPresent() && profileOptional.isPresent()) {
            Record record = recordOptional.get();
            Profile profile = profileOptional.get();
            record.setProfile(profile);
            recordRepos.save(record);

            List<Record> recordList = profile.getRecords();
            recordList.add(record);
            profile.setRecords(recordList);
            profileRepos.save(profile);
            return dtoFromProfile(profile);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
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
        dto.setId(profile.getId());
        if (profile.getUser() != null) {
            dto.setUserName(profile.getUser().getUsername());
        }
        if (profile.getRecords() != null){
            dto.setRecords(dtoListfromRecordList(profile.getRecords()));
        }
        dto.setNintendoCode(profile.getNintendoCode());
        return dto;
    }

    public Profile profileFromDto (ProfileInputDto dto) {
        Profile profile = new Profile();
        profile.setId(dto.getId());
        profile.setNintendoCode(dto.getNintendoCode());
        return profile;
    }
}
