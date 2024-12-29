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
import shop.mtcoding.bank.dto.account.AccountReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountListRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountSaveRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountListRespDto 계좌목록보기_유저별(Long userId){
        User userPS = userRepository.findById(userId).orElseThrow(
                ()-> new CustomApiException("유저를 찾을 수 없습니다."));

        // 유저가 들고 있는 모든 계좌 목록
        List<Account> accountListPS = accountRepository.findByUser_id(userId);
        return new AccountListRespDto(userPS, accountListPS);
    }




    @Transactional
    public AccountSaveRespDto 계좌등록(AccountReqDto.AccountSaveReqDto accountSaveReqDto, Long userId) {
        // User가 DB에 있는지 검증 겸 유저 엔티티 가져오기
        User userPS = userRepository.findById(userId).orElseThrow(
                ()-> new CustomApiException("유저를 찾을 수 없습니다.")
        );
        // 해당 계좌가 DB에 있는지 중복 여부 체크
        Optional<Account> accountOP = accountRepository.findByNumber(accountSaveReqDto.getNumber());
        if (accountOP.isPresent()) {
            throw new CustomApiException("해당 계좌가 이미 존재합니다.");
        }
        // 계좌 등록
        Account accountPS = accountRepository.save(accountSaveReqDto.toEntity(userPS));
        // DTO 응답
        return new AccountSaveRespDto(accountPS);
    }

    @Transactional
    public void 계좌삭제(Long number, Long userId) {
        // 1. 계좌 확인
        Account accountPS = accountRepository.findByNumber(number).orElseThrow(
                () -> new CustomApiException("계좌를 찾을 수 없습니다.")
        );
        // 2. 계좌 소유자 확인
        accountPS.checkOwner(userId);

        // 3. 계좌 삭제
        accountRepository.deleteById(accountPS.getId());
    }
}
