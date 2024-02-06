package Robin.MariokartBackend.repository;

import Robin.MariokartBackend.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacterRepository extends JpaRepository<Character, Long> {
}
