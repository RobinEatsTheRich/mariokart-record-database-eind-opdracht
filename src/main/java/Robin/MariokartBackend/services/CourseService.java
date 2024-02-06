package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.CourseDto;
import Robin.MariokartBackend.dtos.KartPartDto;
import Robin.MariokartBackend.inputDtos.CourseInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Course;
import Robin.MariokartBackend.model.Course;
import Robin.MariokartBackend.model.KartPart;
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
        return dtoFromCourse(courseFromId(id));
    }

    public CourseDto addCourse(CourseInputDto dto){
        Course Course = courseFromDto(dto);
        courseRepos.save(Course);
        return dtoFromCourse(Course);
    }

    public CourseDto editCourse(Long id, CourseInputDto dto){
        CourseDto result;
        Course course = courseFromDto(dto);
        courseRepos.save(course);
        result = dtoFromCourse(courseFromId(id));
        return result;
    }

    public void deleteCourse(Long id){
        Optional<Course> CourseOptional = courseRepos.findById(id);
        if (CourseOptional.isPresent()) {
            courseRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("The course corresponding to ID:"+id+" could not be found in the database");
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
        } else {
            throw new RecordNotFoundException("The course corresponding to ID:"+id+" could not be found in the database");
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

    public Course courseFromDto (CourseInputDto dto) {
        Course course = new Course();
        course.setId(dto.getId());
        course.setName(dto.getName());
        course.setImgLink(dto.getImgLink());
        return course;
    }
}
