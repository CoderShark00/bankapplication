package shop.mtcoding.bank.service;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.handler.ex.CustomApiException;

import java.util.Optional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    @Transactional
    public AccountSaveRespDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId) {
        // User가 DB에 있는지 검증 겸 유저 엔티티 가져오기
        User userPS = userRepository.findById(userId).orElseThrow(
                ()-> new CustomApiException("유저를 찾을 수 없습니다.")
        );
        // 해당 계좌가 DB에 있는 중복 여부 체크
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if (accountOP.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다.");
        }
        // 계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));
        // DTO 응답
        return new AccountSaveRespDto(accountPS);
    }

    @Getter
    @Setter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }


    @Getter
    @Setter
    public static class AccountSaveReqDto{
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();

        }
    }
}