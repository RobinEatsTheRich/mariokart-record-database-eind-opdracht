package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.CharacterDto;
import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.dtos.RecordDto;
import Robin.MariokartBackend.enumerations.PartType;
import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.inputDtos.ProfileInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Profile;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.ProfileRepository;
import Robin.MariokartBackend.repository.RecordRepository;
import Robin.MariokartBackend.security.MyUserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BinaryOperator;

@Service
public class ProfileService {


    private final ProfileRepository profileRepos;
    private final RecordRepository recordRepos;
    private final RecordService recordService;
    private final CharacterService characterService;
    private final KartPartService kartPartService;

    public ProfileService(ProfileRepository profileRepos, RecordRepository recordRepos, RecordService recordService, CharacterService characterService, KartPartService kartPartService) {
        this.profileRepos = profileRepos;
        this.recordRepos = recordRepos;
        this.recordService = recordService;
        this.characterService = characterService;
        this.kartPartService = kartPartService;
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
        return dtoFromProfile(profile);
    }

    public ProfileDto editProfile(MyUserDetails myUserDetails, String username, ProfileInputDto dto){
        if (!myUserDetails.getUsername().equals(username) &&
                (!myUserDetails.getUserRoles().contains(UserRole.ADMIN)) && !myUserDetails.getUsername().equals(username)){
            throw new ForbiddenException("You are logged in as "+myUserDetails.getUsername()+", not as "+username+".");
        }
        Profile oldProfile = profileFromName(username);
        Profile newProfile = profileFromDto(dto);
        newProfile.setUserName(username);
        oldProfile.setNintendoCode(newProfile.getNintendoCode());
        return dtoFromProfile(newProfile);
    }
    public void deleteProfile(MyUserDetails myUserDetails, String username){
        if (!myUserDetails.getUsername().equals(username) &&
                (!myUserDetails.getUserRoles().contains(UserRole.ADMIN)) && !myUserDetails.getUsername().equals(username)){
            throw new ForbiddenException("You are logged in as "+myUserDetails.getUsername()+", not as "+username+".");
        }
        profileRepos.deleteById(username);
    }

    public ProfileDto assignRecord(MyUserDetails myUserDetails, Long recordId){
        Record record = recordService.recordFromId(recordId);
        Profile profile = profileFromName(myUserDetails.getUsername());
        record.setProfile(profile);
        recordRepos.save(record);
        record = recordService.recordFromId(recordId);
        List<Record> recordList = profile.getRecords();
        recordList.add(record);
        profile.setRecords(recordList);
        profileRepos.save(profile);
        return dtoFromProfile(profile);
    }

    public ProfileDto addRival(MyUserDetails myUserDetails, String theirName){
        Profile me = profileFromName(myUserDetails.getUsername());
        Profile them = profileFromName(theirName);
        List<Profile> myRivals = me.getRivals();
        List<Profile> theirRivals = them.getRivals();
        myRivals.add(them);
        theirRivals.add(me);
        me.setRivals(myRivals);
        them.setRivals(theirRivals);
        profileRepos.save(me);
        profileRepos.save(them);
        return dtoFromProfile(me);
    }
    public ProfileDto removeRival(MyUserDetails myUserDetails, String theirName){
        Profile me = profileFromName(myUserDetails.getUsername());
        Profile them = profileFromName(theirName);
        List<Profile> myRivals = me.getRivals();
        List<Profile> theirRivals = them.getRivals();
        myRivals.remove(them);
        theirRivals.remove(me);
        me.setRivals(myRivals);
        them.setRivals(theirRivals);
        profileRepos.save(me);
        profileRepos.save(them);
        return dtoFromProfile(me);
    }
    public CharacterDto getFavoriteCharacter(List<RecordDto> recordList){
        List<Long> allCharacterId = new ArrayList<Long>();
        for (RecordDto recordDto : recordList){
            allCharacterId.add(recordDto.getCharacter().getId());
        }
        Long favoriteCharacterId = allCharacterId.stream().reduce(BinaryOperator.maxBy(Comparator.comparingInt(o -> Collections.frequency(allCharacterId, o)))).orElse(null);
        CharacterDto result = characterService.dtoFromCharacter(characterService.characterFromId(favoriteCharacterId));
        return result;
    }
    public KartPartDto getFavoriteKartPart (List<RecordDto> recordList, PartType partType){
        List<Long> allPartId = new ArrayList<Long>();
        switch(partType){
            case BODY -> {
                for (RecordDto recordDto : recordList){
                allPartId.add(recordDto.getBody().getId());
                }
            }
            case WHEELS -> {
                for (RecordDto recordDto : recordList){
                    allPartId.add(recordDto.getWheels().getId());
                }
            }
            case GLIDER -> {
                for (RecordDto recordDto : recordList){
                    allPartId.add(recordDto.getGlider().getId());
                }
            }
        }

        for (RecordDto recordDto : recordList){
            allPartId.add(recordDto.getCharacter().getId());
        }
        Long favoriteCharacterId = allPartId.stream().reduce(BinaryOperator.maxBy(Comparator.comparingInt(o -> Collections.frequency(allPartId, o)))).orElse(null);
        KartPartDto result = kartPartService.dtoFromKartPart(kartPartService.kartPartFromId(favoriteCharacterId));
        return result;
    }

    public List<String> nameListfromProfiles(List<Profile> profileList){
        List<String> result = new ArrayList<>();
        for (Profile profile : profileList){
            result.add(profile.getUserName());
        }
        return result;
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

    public ProfileDto dtoFromProfile(Profile profile) {
        ProfileDto dto = new ProfileDto();
        dto.setUserName(profile.getUserName());
        if (profile.getRecords() != null && !profile.getRecords().isEmpty()){
            dto.setRecords(recordService.dtoListFromRecordList(profile.getRecords()));
            dto.setFavoriteCharacter(getFavoriteCharacter(dto.getRecords()));
            dto.setFavoriteBody(getFavoriteKartPart(dto.getRecords(),PartType.BODY));
            dto.setFavoriteWheels(getFavoriteKartPart(dto.getRecords(),PartType.WHEELS));
            dto.setFavoriteGlider(getFavoriteKartPart(dto.getRecords(),PartType.GLIDER));
        }
        if (profile.getRivals() != null){
            dto.setRivals(nameListfromProfiles(profile.getRivals()));
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
