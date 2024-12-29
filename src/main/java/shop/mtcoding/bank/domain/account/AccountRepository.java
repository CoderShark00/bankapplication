package shop.mtcoding.bank.domain.account;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    // jpa query method
    // select * from account where number = :number
    // 페치조인하면 조인해서 객체에 값을 미리 가져올 수 있음.
//    @Query("SELECT ac FROM Account ac JOIN FETCH ac.user WHERE ac.number = :number")
    Optional<Account> findByNumber(Long number);

    // jpa query method
    // select * from account where userId = :id
    List<Account> findByUser_id(Long id);
}
