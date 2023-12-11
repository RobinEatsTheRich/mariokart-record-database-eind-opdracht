package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.RemoteControllerDto;
import Robin.MariokartBackend.inputDtos.RemoteControllerInputDto;
import Robin.MariokartBackend.services.RemoteControllerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/remote_controllers")
@RestController
public class RemoteControllerController {

    @Autowired
    private RemoteControllerService remoteControllerService;
    @GetMapping
    public ResponseEntity<List<RemoteControllerDto>> getAllRemoteControllers() {
        List<RemoteControllerDto> remoteControllerDtoList;
        remoteControllerDtoList = remoteControllerService.getAllRemoteControllers();
        return ResponseEntity.ok(remoteControllerDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RemoteControllerDto> getRemoteController(@PathVariable Long id) {
        return ResponseEntity.ok(remoteControllerService.getRemoteController(id));
    }

    @PostMapping
    public ResponseEntity<RemoteControllerDto> addRemoteController (@Valid @RequestBody RemoteControllerInputDto inputDto){
        RemoteControllerDto addedRemoteControllerDto = remoteControllerService.addRemoteController(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedRemoteControllerDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedRemoteControllerDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RemoteControllerDto> updateRemoteController(@Valid @PathVariable Long id, @RequestBody RemoteControllerInputDto inputDto) {
        RemoteControllerDto edittedRemoteControllerDto = remoteControllerService.editRemoteController(id, inputDto);
        return ResponseEntity.ok(edittedRemoteControllerDto);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteRemoteController(@PathVariable Long id){
        remoteControllerService.deleteRemoteController(id);
        return new ResponseEntity<>("Remote controller succesfully deleted", HttpStatus.OK);
    }

}