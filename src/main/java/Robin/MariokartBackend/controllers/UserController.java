package Robin.MariokartBackend.controllers;

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

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser (@Valid @RequestBody UserInputDto inputDto){
        UserDto addedUserDto = userService.addUser(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedUserDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable Long id, @RequestBody UserInputDto inputDto) {
        UserDto edittedUserDto = userService.editUser(id, inputDto);
        return ResponseEntity.ok(edittedUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return new ResponseEntity<>("TV succesfully deleted", HttpStatus.OK);
    }

//    @PutMapping("/{id}/remote_controller")
//    public ResponseEntity<UserDto> assignRemoteControllerToUser(@PathVariable Long id, @RequestBody IdInputDto inputDto) {
//        UserDto edittedUserDto = userService.assignRemoteControllerToUser(id, inputDto.id);
//        return ResponseEntity.ok(edittedUserDto);
//    }

}
