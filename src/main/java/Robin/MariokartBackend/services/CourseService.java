package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.CourseDto;
import Robin.MariokartBackend.inputDtos.CourseInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Course;
import Robin.MariokartBackend.model.Course;
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

    public Long courseIdFromName(String name){
        Long result = 0l;
        List<Course> courseList = courseRepos.findAll();
        if (!courseList.isEmpty()){
            for (Course course : courseList){
                if (name.toLowerCase().equals(course.getName().toLowerCase())){
                    result = course.getId();
                }
            }
        }
        return result;
    }
    public Course courseFromId(Long id){
        Course result = new Course();
        Optional<Course> optionalCourse = courseRepos.findById(id);
        if (optionalCourse.isPresent()){
            result = optionalCourse.get();
        }
        return result;
    }

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
