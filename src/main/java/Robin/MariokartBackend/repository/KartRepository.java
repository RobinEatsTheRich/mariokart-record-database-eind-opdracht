package Robin.MariokartBackend.repository;

import Robin.MariokartBackend.model.Kart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KartRepository extends JpaRepository<Kart, Long> {
}
