package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.KartDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.KartInputDto;
import Robin.MariokartBackend.services.KartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/karts")
@RestController
public class KartController {

    @Autowired
    private KartService kartService;
    @GetMapping
    public ResponseEntity<List<KartDto>> getAllKarts() {
        List<KartDto> kartDtoList;
        kartDtoList = kartService.getAllKarts();
        return ResponseEntity.ok(kartDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KartDto> getKart(@PathVariable Long id) {
        return ResponseEntity.ok(kartService.getKart(id));
    }

    @PostMapping
    public ResponseEntity<KartDto> addKart (@Valid @RequestBody KartInputDto inputDto){
        KartDto addedKartDto = kartService.addKart(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedKartDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedKartDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KartDto> updateKart(@Valid @PathVariable Long id, @RequestBody KartInputDto inputDto) {
        KartDto edittedKartDto = kartService.editKart(id, inputDto);
        return ResponseEntity.ok(edittedKartDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKart(@PathVariable Long id){
        kartService.deleteKart(id);
        return new ResponseEntity<>("TV succesfully deleted", HttpStatus.OK);
    }

//    @PutMapping("/{id}/remote_controller")
//    public ResponseEntity<KartDto> assignRemoteControllerToKart(@PathVariable Long id, @RequestBody IdInputDto inputDto) {
//        KartDto edittedKartDto = kartService.assignRemoteControllerToKart(id, inputDto.id);
//        return ResponseEntity.ok(edittedKartDto);
//    }

}
