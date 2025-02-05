package Robin.MariokartBackend.controllers;
import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.inputDtos.ProfileInputDto;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<ProfileDto> updateProfile(@Valid @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable String id, @RequestBody ProfileInputDto inputDto) {
        ProfileDto edittedProfileDto = profileService.editProfile(myUserDetails, id, inputDto);
        return ResponseEntity.ok(edittedProfileDto);
    }
    @PutMapping("/record/{id}")
    public ResponseEntity<ProfileDto> assignRecord(@Valid @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long id) {
        ProfileDto edittedProfileDto = profileService.assignRecord(myUserDetails, id);
        return ResponseEntity.ok(edittedProfileDto);
    }
    @PutMapping("/rival/{id}")
    public ResponseEntity<ProfileDto> addRival(@Valid @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable String id) {
        ProfileDto edittedProfileDto = profileService.addRival(myUserDetails, id);
        return ResponseEntity.ok(edittedProfileDto);
    }
    @DeleteMapping("/rival/{id}")
    public ResponseEntity<ProfileDto> removeRival(@Valid @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable String id) {
        ProfileDto edittedProfileDto = profileService.removeRival(myUserDetails, id);
        return ResponseEntity.ok(edittedProfileDto);
    }

}
