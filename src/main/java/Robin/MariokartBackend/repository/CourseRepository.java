package Robin.MariokartBackend.repository;

import Robin.MariokartBackend.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
