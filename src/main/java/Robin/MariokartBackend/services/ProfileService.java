package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.inputDtos.ProfileInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.Profile;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.ProfileRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {


    private final ProfileRepository profileRepos;


    public ProfileService(ProfileRepository profileRepos) {
        this.profileRepos = profileRepos;
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

//    public ProfileDto assignRemoteControllerToProfile(Long id, Long remoteControllerId){
//        Optional<Profile> ProfileOptional = ProfileRepos.findById(id);
//        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);
//
//        if(ProfileOptional.isPresent() && remoteControllerOptional.isPresent()){
//            Profile Profile = ProfileOptional.get();
//            RemoteController remoteController = remoteControllerOptional.get();
//            Profile.setRemoteController(remoteController);
//            ProfileRepos.save(Profile);
//            return dtoFromProfile(Profile);
//
//        }else if(ProfileOptional.isPresent() && remoteControllerOptional.isEmpty()){
//            throw new RecordNotFoundException("Remote Controller ID cannot be found");
//
//        }else if(ProfileOptional.isEmpty() && remoteControllerOptional.isPresent()){
//            throw new RecordNotFoundException("Profile ID cannot be found");
//
//        }else{
//            throw new RecordNotFoundException("Neither ID can be found");
//        }
//    }

    public ProfileDto dtoFromProfile(Profile profile) {
        ProfileDto dto = new ProfileDto();
        dto.setId(profile.getId());
        dto.setUser(profile.getUser());
        dto.setRecords(profile.getRecords());
        dto.setNintendoCode(profile.getNintendoCode());
        dto.setFavKart(profile.getFavKart());
        dto.setFavCharacter(profile.getFavCharacter());

        return dto;
    }

    public Profile profileFromDto (ProfileInputDto dto) {
        Profile profile = new Profile();
        profile.setId(dto.getId());
        profile.setNintendoCode(dto.getNintendoCode());
        return profile;
    }
}
