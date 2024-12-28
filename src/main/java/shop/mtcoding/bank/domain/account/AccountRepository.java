package shop.mtcoding.bank.domain.account;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
    // select * from account where number = :number
    // checkpoint : 리펙토링
    Optional<Account> findByNumber(Long number);
}
