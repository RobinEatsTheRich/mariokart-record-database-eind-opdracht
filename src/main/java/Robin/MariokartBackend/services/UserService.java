package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.enumerations.UserRole;
import Robin.MariokartBackend.exceptions.ForbiddenException;
import Robin.MariokartBackend.inputDtos.UserInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.ProfileRepository;
import Robin.MariokartBackend.repository.UserRepository;
import Robin.MariokartBackend.security.MyUserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepos;
    private final ProfileRepository profileRepos;
    private final ProfileService profileService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepos, ProfileRepository profileRepos, ProfileService profileService, PasswordEncoder passwordEncoder) {
        this.userRepos = userRepos;
        this.profileRepos = profileRepos;
        this.profileService = profileService;
        this.passwordEncoder = passwordEncoder;
    }

    public List<String> getAllUsers(){
        List<User> userList = userRepos.findAll();
        List<String> usernamesList = new ArrayList<>();
        for(User user : userList)
        {
            usernamesList.add(user.getUsername());
        }
        return usernamesList;
    }

    public UserDto getUser(MyUserDetails myUserDetails, String username){
        UserDto result;
        if (!username.equals(myUserDetails.getUsername())){
            throw new ForbiddenException("You are logged in as "+myUserDetails.getUsername()+", not as "+username+".");
        }
        result= dtoFromUser(userFromName(username));

        return result;
    }

    public UserDto createUser(UserInputDto dto){
        Optional<User> userOptional = userRepos.findById(dto.getUsername());
        if (userOptional.isPresent())
        {
            throw new ForbiddenException("The username "+dto.getUsername()+" is no longer available, pick a different username.");
        }
        User user = userFromDto(dto);
        userRepos.save(user);
        profileService.createProfile(user.getUsername(),user);
        user.setProfile(profileService.profileFromName(user.getUsername()));
        userRepos.save(user);

        return dtoFromUser(user);
    }

    public UserDto editUser(String username, UserInputDto dto){
        User oldUser = userFromName(username);
        User newUser = userFromDto(dto);
        if (newUser.getUsername() != oldUser.getUsername()){
            throw new RecordNotFoundException("Username cannot be changed, this is your ID");
        }
        userRepos.save(newUser);
        return dtoFromUser(newUser);
    }

    public void deleteUser(String username){
        profileService.deleteProfile(username);
        Optional<User> userOptional = userRepos.findById(username);
        if (userOptional.isPresent()) {
            userRepos.deleteById(username);
        } else {
            throw new RecordNotFoundException("User "+username+" could not be found in the database");
        }
    }

    public static List<UserRole> userRoleFromName(List<String> stringList){
        List<UserRole>  result = new ArrayList<UserRole>();
        for (String string : stringList){
            result.add(UserRole.valueOf(string));
        }
        return result;
    }


    public User userFromName(String username){
        User result;
        Optional<User> userOptional = userRepos.findById(username);
        if (userOptional.isPresent())
        {
            result = userOptional.get();
        } else{
            throw new RecordNotFoundException("User "+username+" could not be found in the database");
        }
        return result;
    }

    public UserDto dtoFromUser(User user) {
        UserDto dto = new UserDto();
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());
        dto.setUserRoles(user.getUserRoles());
        if (user.getProfile() != null){
            dto.setProfile(profileService.dtoFromProfile(user.getProfile()));
        }
        return dto;
    }

    public User userFromDto (UserInputDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setUserRoles(userRoleFromName(dto.getRoles()));
        return user;
    }
}
