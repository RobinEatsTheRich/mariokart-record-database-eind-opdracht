package Robin.MariokartBackend.controllers;
import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.dtos.UserDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.ProfileInputDto;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<ProfileDto> getProfile(@PathVariable String id) {
        return ResponseEntity.ok(profileService.getProfile(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDto> updateProfile(@Valid @PathVariable String id, @RequestBody ProfileInputDto inputDto) {
        ProfileDto edittedProfileDto = profileService.editProfile(id, inputDto);
        return ResponseEntity.ok(edittedProfileDto);
    }
//    @AuthenticationPrincipal MyUserDetails myUserDetails
    @PutMapping("/assign_record/{id}")
    public ResponseEntity<ProfileDto> assignProfile(@Valid @PathVariable Long id) {
        ProfileDto edittedProfileDto = profileService.assignRecord("user", id);
        return ResponseEntity.ok(edittedProfileDto);
    }

}
