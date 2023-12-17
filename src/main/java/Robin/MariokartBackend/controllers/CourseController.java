package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.CourseDto;
import Robin.MariokartBackend.inputDtos.IdInputDto;
import Robin.MariokartBackend.inputDtos.CourseInputDto;
import Robin.MariokartBackend.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/courses")
@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;
    @GetMapping
    public ResponseEntity<List<CourseDto>> getAllCourses() {
        List<CourseDto> courseDtoList;
        courseDtoList = courseService.getAllCourses();
        return ResponseEntity.ok(courseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourse(id));
    }

    @PostMapping
    public ResponseEntity<CourseDto> addCourse (@Valid @RequestBody CourseInputDto inputDto){
        CourseDto addedCourseDto = courseService.addCourse(inputDto);
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentRequest()
                        .path("/"+addedCourseDto.getId()).toUriString());

        return ResponseEntity.created(uri).body(addedCourseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourse(@Valid @PathVariable Long id, @RequestBody CourseInputDto inputDto) {
        CourseDto edittedCourseDto = courseService.editCourse(id, inputDto);
        return ResponseEntity.ok(edittedCourseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourse(@PathVariable Long id){
        courseService.deleteCourse(id);
        return new ResponseEntity<>("TV succesfully deleted", HttpStatus.OK);
    }

//    @PutMapping("/{id}/remote_controller")
//    public ResponseEntity<CourseDto> assignRemoteControllerToCourse(@PathVariable Long id, @RequestBody IdInputDto inputDto) {
//        CourseDto edittedCourseDto = courseService.assignRemoteControllerToCourse(id, inputDto.id);
//        return ResponseEntity.ok(edittedCourseDto);
//    }

}
