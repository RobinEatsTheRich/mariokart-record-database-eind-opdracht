package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.inputDtos.KartPartInputDto;
import Robin.MariokartBackend.services.KartPartService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/kartParts")
@RestController
public class KartPartController {

    @Autowired
    private KartPartService kartPartService;
    @GetMapping
    public ResponseEntity<List<KartPartDto>> getAllKartParts() {
        List<KartPartDto> kartPartDtoList;
        kartPartDtoList = kartPartService.getAllKartParts();
        return ResponseEntity.ok(kartPartDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<KartPartDto> getKartPart(@PathVariable Long id) {
        return ResponseEntity.ok(kartPartService.getKartPart(id));
    }

    @PostMapping
    public ResponseEntity<KartPartDto> addKartPart (@Valid @RequestBody KartPartInputDto inputDto){
        KartPartDto addedKartPartDto = kartPartService.addKartPart(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedKartPartDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedKartPartDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<KartPartDto> updateKartPart(@Valid @PathVariable Long id, @RequestBody KartPartInputDto inputDto) {
        KartPartDto edittedKartPartDto = kartPartService.editKartPart(id, inputDto);
        return ResponseEntity.ok(edittedKartPartDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteKartPart(@PathVariable Long id){
        kartPartService.deleteKartPart(id);
        return new ResponseEntity<>("KartPart "+id+" succesfully deleted!", HttpStatus.OK);
    }
}
