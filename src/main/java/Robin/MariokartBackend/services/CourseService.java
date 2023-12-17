package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.CourseDto;
import Robin.MariokartBackend.inputDtos.CourseInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.RemoteController;
import Robin.MariokartBackend.model.Course;
import Robin.MariokartBackend.repository.RemoteControllerRepository;
import Robin.MariokartBackend.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {


    private final CourseRepository courseRepos;


    public CourseService(CourseRepository courseRepos) {
        this.courseRepos = courseRepos;
    }


    public List<CourseDto> getAllCourses(){
        List<Course> CourseList = courseRepos.findAll();
        List<CourseDto> CourseDtoList = new ArrayList<>();
        for(Course Course : CourseList)
        {
            CourseDtoList.add(dtoFromCourse(Course));
        }
        return CourseDtoList;
    }

    public CourseDto getCourse(Long id){
        Optional<Course> CourseOptional = courseRepos.findById(id);
        if (CourseOptional.isPresent()) {
            Course Course = CourseOptional.get();
            return dtoFromCourse(Course);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public CourseDto addCourse(CourseInputDto dto){
        Course Course = CourseFromDto(dto);
        courseRepos.save(Course);
        return dtoFromCourse(Course);
    }

    public CourseDto editCourse(Long id, CourseInputDto dto){
        Optional<Course> CourseOptional = courseRepos.findById(id);
        if (CourseOptional.isPresent()) {
            Course OgCourse = CourseOptional.get();
            Course Course = CourseFromDto(dto);
            Course.setId(OgCourse.getId());

            courseRepos.save(Course);

            return dtoFromCourse(Course);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

    public void deleteCourse(Long id){
        Optional<Course> CourseOptional = courseRepos.findById(id);
        if (CourseOptional.isPresent()) {
            courseRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("ID cannot be found");
        }
    }

//    public CourseDto assignRemoteControllerToCourse(Long id, Long remoteControllerId){
//        Optional<Course> CourseOptional = CourseRepos.findById(id);
//        Optional<RemoteController> remoteControllerOptional = remoteControllerRepos.findById(remoteControllerId);
//
//        if(CourseOptional.isPresent() && remoteControllerOptional.isPresent()){
//            Course Course = CourseOptional.get();
//            RemoteController remoteController = remoteControllerOptional.get();
//            Course.setRemoteController(remoteController);
//            CourseRepos.save(Course);
//            return dtoFromCourse(Course);
//
//        }else if(CourseOptional.isPresent() && remoteControllerOptional.isEmpty()){
//            throw new RecordNotFoundException("Remote Controller ID cannot be found");
//
//        }else if(CourseOptional.isEmpty() && remoteControllerOptional.isPresent()){
//            throw new RecordNotFoundException("Course ID cannot be found");
//
//        }else{
//            throw new RecordNotFoundException("Neither ID can be found");
//        }
//    }

    public CourseDto dtoFromCourse(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setImgLink(course.getImgLink());

        return dto;
    }

    public Course CourseFromDto (CourseInputDto dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setImgLink(dto.getImgLink());
        return course;
    }
}
