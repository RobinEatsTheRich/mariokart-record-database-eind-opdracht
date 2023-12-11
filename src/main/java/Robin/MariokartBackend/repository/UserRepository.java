package Robin.MariokartBackend.repository;

import Robin.MariokartBackend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
