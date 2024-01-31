package Robin.MariokartBackend.controllers;
import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.ProfileInputDto;
import Robin.MariokartBackend.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/profiles")
@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;
    @GetMapping
    public ResponseEntity<List<ProfileDto>> getAllProfiles() {
        List<ProfileDto> profileDtoList;
        profileDtoList = profileService.getAllProfiles();
        return ResponseEntity.ok(profileDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileDto> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }

    @PostMapping
    public ResponseEntity<ProfileDto> addProfile (@Valid @RequestBody ProfileInputDto inputDto){
        ProfileDto addedProfileDto = profileService.addProfile(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedProfileDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedProfileDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> updateProfile(@Valid @PathVariable Long id, @RequestBody ProfileInputDto inputDto) {
        ProfileDto edittedProfileDto = profileService.editProfile(id, inputDto);
        return ResponseEntity.ok(edittedProfileDto);
    }
    @PutMapping("/{id}/set_record")
    public ResponseEntity<ProfileDto> assignProfile(@Valid @PathVariable Long id, @RequestBody IdInputDto profileId) {
        ProfileDto edittedProfileDto = profileService.assignRecord(id, profileId);
        return ResponseEntity.ok(edittedProfileDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProfile(@PathVariable Long id){
        profileService.deleteProfile(id);
        return new ResponseEntity<>("TV succesfully deleted", HttpStatus.OK);
    }

}
