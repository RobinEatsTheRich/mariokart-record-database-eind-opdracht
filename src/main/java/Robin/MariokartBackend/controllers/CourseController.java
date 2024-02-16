package Robin.MariokartBackend.controllers;

import Robin.MariokartBackend.dtos.CourseDto;
import Robin.MariokartBackend.inputDtos.CourseInputDto;
import Robin.MariokartBackend.security.MyUserDetails;
import Robin.MariokartBackend.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RequestMapping("/courses")
@RestController
public class CourseController {


    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }


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
    public ResponseEntity<String> deleteCourse( @AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long id){
        courseService.deleteCourse(id);
        return new ResponseEntity<>("Course "+id+" succesfully deleted!", HttpStatus.OK);
    }
}
