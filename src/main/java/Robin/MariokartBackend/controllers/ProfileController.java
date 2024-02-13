package Robin.MariokartBackend.controllers;
import Robin.MariokartBackend.RecordingUtil;
import Robin.MariokartBackend.dtos.ProfileDto;
import Robin.MariokartBackend.inputDtos.ProfileInputDto;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.model.RecordingData;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.ProfileService;
import Robin.MariokartBackend.services.RecordingDataService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    @PutMapping("/assign_record/{id}")
    public ResponseEntity<ProfileDto> assignRecord(@Valid @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long id) {
        ProfileDto edittedProfileDto = profileService.assignRecord(myUserDetails.getUsername(), id);
        return ResponseEntity.ok(edittedProfileDto);
    }
    @PutMapping("/add_rival/{id}")
    public ResponseEntity<ProfileDto> addRival(@Valid @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable String id) {
        ProfileDto edittedProfileDto = profileService.addRival(myUserDetails.getUsername(), id);
        return ResponseEntity.ok(edittedProfileDto);
    }
    @PutMapping("/remove_rival/{id}")
    public ResponseEntity<ProfileDto> removeRival(@Valid @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable String id) {
        ProfileDto edittedProfileDto = profileService.removeRival(myUserDetails.getUsername(), id);
        return ResponseEntity.ok(edittedProfileDto);
    }

}
