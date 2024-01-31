package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.UserInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Profile;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.ProfileRepository;
import Robin.MariokartBackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepos;
    private final ProfileRepository profileRepos;
    private final ProfileService profileService;

    public UserService(UserRepository userRepos, ProfileRepository profileRepos, ProfileService profileService) {
        this.userRepos = userRepos;
        this.profileRepos = profileRepos;
        this.profileService = profileService;
    }

    public List<UserDto> getAllUsers(){
        List<User> userList = userRepos.findAll();
        List<UserDto> userDtoList = new ArrayList<>();
        for(User User : userList)
        {
            userDtoList.add(dtoFromUser(User));
        }
        return userDtoList;
    }

    public UserDto getUser(Long id){
        Optional<User> userOptional = userRepos.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            return dtoFromUser(user);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public UserDto addUser(UserInputDto dto){
        User user = userFromDto(dto);
        userRepos.save(user);
        return dtoFromUser(user);
    }

    public UserDto editUser(Long id, UserInputDto dto){
        Optional<User> userOptional = userRepos.findById(id);
        if (userOptional.isPresent()) {
            User ogUser = userOptional.get();
            User user = userFromDto(dto);
            user.setId(ogUser.getId());

            userRepos.save(user);

            return dtoFromUser(user);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }
    public UserDto assignProfile(Long userId, IdInputDto profileId){
        Optional<User> userOptional = userRepos.findById(userId);
        Optional<Profile> profileOptional = profileRepos.findById(profileId.id);
        if (userOptional.isPresent() && profileOptional.isPresent()) {
            User user = userOptional.get();
            Profile profile = profileOptional.get();
            user.setProfile(profile);
            userRepos.save(user);
            profile.setUser(user);
            profileRepos.save(profile);
            return dtoFromUser(user);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteUser(Long id){
        Optional<User> userOptional = userRepos.findById(id);
        if (userOptional.isPresent()) {
            userRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public UserDto dtoFromUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        if (user.getProfile() != null) {dto.setProfileDto(profileService.dtoFromProfile(user.getProfile()));}
        return dto;
    }

    public User userFromDto (UserInputDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setEmail(dto.getEmail());
        return user;
    }
}
