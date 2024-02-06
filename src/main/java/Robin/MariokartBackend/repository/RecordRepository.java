package Robin.MariokartBackend.repository;

import Robin.MariokartBackend.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
