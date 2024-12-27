package shop.mtcoding.bank.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // select * from user where userename = ?
    Optional<User> findByUsername(String username);
}
