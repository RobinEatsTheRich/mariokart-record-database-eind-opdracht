package Robin.MariokartBackend.services;

import Robin.MariokartBackend.dtos.CourseDto;
import Robin.MariokartBackend.dtos.CourseDtoForRecord;
import Robin.MariokartBackend.exceptions.BadRequestException;
import Robin.MariokartBackend.inputDtos.CourseInputDto;
import Robin.MariokartBackend.exceptions.RecordNotFoundException;
import Robin.MariokartBackend.model.Course;
import Robin.MariokartBackend.model.KartPart;
import Robin.MariokartBackend.model.Record;
import Robin.MariokartBackend.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CourseService {


    private final CourseRepository courseRepos;
    private final RecordService recordService;

    @Autowired
    public CourseService(@Lazy RecordService recordService, CourseRepository courseRepos) {
        this.courseRepos = courseRepos;
        this.recordService = recordService;
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
        if (courseRepos.findById(dto.getId()).isPresent()){
            throw new BadRequestException("A course by this ID already exists, please either edit that course using the 'PUT' method, or pick a different ID");
        }
        Course Course = courseFromDto(dto);
        courseRepos.save(Course);
        return dtoFromCourse(Course);
    }

    public CourseDto editCourse(Long id, CourseInputDto dto){
        Course oldCourse = courseFromId(id);  //This is to efficiently check whether the ID exists
        Course newCourse = courseFromDto(dto);
        newCourse.setId(oldCourse.getId());
        courseRepos.save(newCourse);
        return dtoFromCourse(newCourse);
    }

    public void deleteCourse(Long id){
        Optional<Course> CourseOptional = courseRepos.findById(id);
        if (CourseOptional.isPresent()) {
            courseRepos.deleteById(id);
        } else {
            throw new RecordNotFoundException("The course corresponding to ID:"+id+" could not be found in the database.");
        }
    }

    public CourseDto assignRecord(Course course, Record record){
        List<Record> recordList = course.getRecords();
        recordList.add(record);
        course.setRecords(recordList);
        courseRepos.save(course);
        return dtoFromCourse(course);
    }

    public Long courseIdFromName(String name){
        Long result = 0l;
        List<Course> courseList = courseRepos.findAll();
        if (!courseList.isEmpty()){
            for (Course course : courseList){
                if (name.equalsIgnoreCase(course.getName())){
                    return course.getId();
                }
            }
        }
        if(result == 0l){
            throw new RecordNotFoundException("The Course '"+name+"' could not be found in the database.");
        }
        return result;
    }
    public Course courseFromId(Long id){
        Course result;
        Optional<Course> optionalCourse = courseRepos.findById(id);
        if (optionalCourse.isPresent()){
            result = optionalCourse.get();
        } else {
            throw new RecordNotFoundException("The course corresponding to ID:"+id+" could not be found in the database.");
        }
        return result;
    }

    public CourseDtoForRecord dtoForRecordFromCourse(Course course) {
        CourseDtoForRecord dto = new CourseDtoForRecord();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setImgLink(course.getImgLink());
        return dto;
    }

    public CourseDto dtoFromCourse(Course course) {
        CourseDto dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setImgLink(course.getImgLink());
        if (course.getRecords() != null && !course.getRecords().isEmpty()){
            dto.setRecords(recordService.dtoForCoursesListFromRecordList(course.getRecords()));
        }
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
