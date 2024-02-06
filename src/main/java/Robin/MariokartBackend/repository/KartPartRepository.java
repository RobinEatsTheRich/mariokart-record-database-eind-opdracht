package Robin.MariokartBackend.repository;

import Robin.MariokartBackend.model.KartPart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KartPartRepository extends JpaRepository<KartPart, Long> {
}
