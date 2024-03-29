package Robin.MariokartBackend.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CourseDto {
    private Long id;
    private String name;
    private String imgLink;
    private List<RecordDtoForCourse> records;
}
