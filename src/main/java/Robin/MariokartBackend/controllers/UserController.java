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

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(id));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser (@Valid @RequestBody UserInputDto inputDto){
        UserDto addedUserDto = userService.createUser(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedUserDto.getUsername()).toUriString());

        return ResponseEntity.created(uri).body(addedUserDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@Valid @PathVariable String id, @RequestBody UserInputDto inputDto) {
        UserDto edittedUserDto = userService.editUser(id, inputDto);
        return ResponseEntity.ok(edittedUserDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable String id){
        userService.deleteUser(id);
        return new ResponseEntity<>("User "+id+" succesfully deleted!", HttpStatus.OK);
    }
}
