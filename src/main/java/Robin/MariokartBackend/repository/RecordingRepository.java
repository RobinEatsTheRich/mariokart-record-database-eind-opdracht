package Robin.MariokartBackend.repository;

import Robin.MariokartBackend.model.RecordingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordingRepository extends JpaRepository<RecordingData, Long> {
}