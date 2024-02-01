package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.UserInputDto;
import Robin.MariokartBackend.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/users")
@RestController
public class UserController {

    @Autowired
    private UserService userService;
    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtoList;
        userDtoList = userService.getAllUsers();
        return ResponseEntity.ok(userDtoList);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser (@Valid @RequestBody UserInputDto inputDto){
        UserDto addedUserDto = userService.addUser(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedUserDto.getUsername()).toUriString());

        return ResponseEntity.created(uri).body(addedUserDto);
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable String username, @RequestBody UserInputDto inputDto) {
        UserDto edittedUserDto = userService.editUser(username, inputDto);
        return ResponseEntity.ok(edittedUserDto);
    }

    @PutMapping("/{username}/set_profile")
    public ResponseEntity<UserDto> assignProfile(@Valid @PathVariable String username, @RequestBody IdInputDto profileId) {
        UserDto edittedUserDto = userService.assignProfile(username, profileId);
        return ResponseEntity.ok(edittedUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return new ResponseEntity<>("TV succesfully deleted", HttpStatus.OK);
    }
}
