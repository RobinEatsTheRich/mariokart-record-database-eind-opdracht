package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.UserInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.User;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepos;


    public UserService(UserRepository userRepos) {
        this.userRepos = userRepos;
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

    public void deleteUser(Long id){
        Optional<User> userOptional = userRepos.findById(id);
        if (userOptional.isPresent()) {
            userRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

//    public UserDto assignRemoteControllerToUser(Long id, Long remoteControllerId){
//        Optional<User> UserOptional = UserRepos.findById(id);
//        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);
//
//        if(UserOptional.isPresent() && remoteControllerOptional.isPresent()){
//            User User = UserOptional.get();
//            RemoteController remoteController = remoteControllerOptional.get();
//            User.setRemoteController(remoteController);
//            UserRepos.save(User);
//            return dtoFromUser(User);
//
//        }else if(UserOptional.isPresent() && remoteControllerOptional.isEmpty()){
//            throw new RecordNotFoundException("Remote Controller ID cannot be found");
//
//        }else if(UserOptional.isEmpty() && remoteControllerOptional.isPresent()){
//            throw new RecordNotFoundException("User ID cannot be found");
//
//        }else{
//            throw new RecordNotFoundException("Neither ID can be found");
//        }
//    }

    public UserDto dtoFromUser(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setPassword(user.getPassword());
        dto.setEmail(user.getEmail());

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
