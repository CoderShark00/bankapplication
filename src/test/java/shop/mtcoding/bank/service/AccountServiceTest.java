package shop.mtcoding.bank.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountTransferReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountListRespDto;
import shop.mtcoding.bank.dto.user.UserReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class AccountServiceTest extends DummyObject {

    @InjectMocks
    private AccountService accountService;

    @Spy // 진짜 객체를 InjectMocks에 주입
    private ObjectMapper om;

    @Mock // 모크라는애들이 위의 인젝트 모크로 주입됨.
    private UserRepository userRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Test
    public void 계좌등록_test()throws Exception{
        // given
        Long userId = 1L;

        AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
        accountSaveReqDto.setNumber(1111L);
        accountSaveReqDto.setPassword(1234L);

        // stub1
        User ssar = newMockUser(userId, "ssar", "쌀");
        when(userRepository.findById(any())).thenReturn(Optional.of(ssar));

        // stub2
        when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

        // stub3
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.save(any())).thenReturn(ssarAccount);

        // when
        AccountRespDto.AccountSaveRespDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, userId);
        String responseBody = om.writeValueAsString(accountSaveRespDto);
        System.out.println("테스트 : " + responseBody);
        //then
        assertThat(accountSaveRespDto.getNumber()).isEqualTo(1111L);

    }

    @Test
    public void 계좌목록보기_유저별_test()throws Exception{
        // given
        Long userId = 1L;

        //stub
        User ssar = newMockUser(1l, "ssar", "쌀");
        when(userRepository.findById(userId)).thenReturn(Optional.of(ssar));

        Account ssarAccount1 = newMockAccount(1L, 1111L, 1000L, ssar);
        Account ssarAccount2 = newMockAccount(2L, 2222L, 1000L, ssar);
        List<Account> ssarAccounts = Arrays.asList(ssarAccount1, ssarAccount2);
        when(accountRepository.findByUser_id(userId)).thenReturn(ssarAccounts);
        // when
        AccountListRespDto accountListRespDto = accountService.계좌목록보기_유저별(userId);
        // then
        assertThat(accountListRespDto.getFullname()).isEqualTo("쌀");
        assertThat(accountListRespDto.getAccounts().size()).isEqualTo(2);
    }

    @Test
    public void 계좌삭제_test()throws Exception{
        //given
        Long number = 1111L;
        Long userId = 2L;

        //stub
        User ssar = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount));

        //when
        //then
        assertThatThrownBy(()-> accountService.계좌삭제(number, userId)).isInstanceOf(CustomApiException.class);
    }


    // Account -> balance 변경됐는지
    // Transaction -> balance 잘 기록됐는지 확인
    @Test
    public void 계좌입금_test1()throws Exception{
        //given
        UserReqDto.AccountDepositReqDto accountDepositReqDto = new UserReqDto.AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01088887777");

        //stub1
        User ssar = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount1 = newMockAccount(1L, 1111L, 1000L, ssar);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount1));

        //stub2 (스텁이 진행될 때 마다 연관된 객체는 새로 만들어서 주입하기 - 타임이 때문에 꼬인다.)
        Account ssarAccount2 = newMockAccount(1L, 1111L, 1000L, ssar);
        Transaction transaction = newMockDepositTransaction(1L, ssarAccount2);
        when(transactionRepository.save(any())).thenReturn(transaction);

        //when
        AccountRespDto.AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        System.out.println("테스트 : 계좌쪽 잔액 : " + ssarAccount2.getBalance());

        // then
        assertThat(ssarAccount1.getBalance()).isEqualTo(1100);
    }

    @Test
    public void 계좌입금_test2() throws Exception{
        //given
        UserReqDto.AccountDepositReqDto accountDepositReqDto = new UserReqDto.AccountDepositReqDto();
        accountDepositReqDto.setNumber(1111L);
        accountDepositReqDto.setAmount(100L);
        accountDepositReqDto.setGubun("DEPOSIT");
        accountDepositReqDto.setTel("01088887777");


        // stub 1
        User ssar1 = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount1 = newMockAccount(1L, 1111L, 1000L, ssar1);
        when(accountRepository.findByNumber(any())).thenReturn(Optional.of(ssarAccount1));

        // stub 2
        User ssar2 = newMockUser(1L, "ssar", "쌀");
        Account ssarAccount2 = newMockAccount(1L, 1111L, 1000L, ssar2);
        Transaction transaction = newMockDepositTransaction(1L, ssarAccount2);
        when(transactionRepository.save(any())).thenReturn(transaction);

        //when
        AccountRespDto.AccountDepositRespDto accountDepositRespDto = accountService.계좌입금(accountDepositReqDto);
        String responseBody = om.writeValueAsString(accountDepositRespDto);
        System.out.println("테스트 : " + responseBody);

        //then
        assertThat(ssarAccount1.getBalance()).isEqualTo(1100);
    }


    // 위는 서비스 테스트의 기술적인 테크닉
    // 진짜 서비스를 테스트 하고 싶으면, 내가 지금 무엇을 여기서 테스트해야할지 명확히 구분(책임 분리)
    // DTO를 만드는 책임 -> 서비스 (서비스에서 안할래 -Controller 테스트 할거니까)
    // DB관련된 것도 -> 서비스 것이 아니다.
    // DB관련된 것을 조회할 때, 그 값을 통해서 어떤 비즈니스 로직이 흘러가는 것이 있으면 -> stub으로 정의해서 테스트 해보면된다.

    // DB 스텁, DB 스텁 (가짜로 DB만들어서 deposit 검증)
    @Test
    public void 계좌입금_test3() throws Exception{
        // given
        Account account = newMockAccount(1L, 1111L, 1000L, null);
        Long amount = 100L;
        //when
        if(amount <= 0L){
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }
        account.deposit(100L);

        //then
        assertThat(account.getBalance()).isEqualTo(1100L);
    }

    // 계좌 출금_테스트 (서비스)
    @Test
    public void 계좌출금_test() throws Exception{
        //given
        Long amount = 100L;
        Long password= 1234L;
        Long userId = 1L;

        User ssar = newMockUser(1L, "ssar", "쌀");
        Account account = newMockAccount(1L, 1111L, 1000L, ssar);
        //when
        if(amount <= 0L){
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }
        account.checkOwner(userId);
        account.checkSamePassword(password);
//        account.checkBalance(amount);
        account.withdraw(amount);
        //then
        assertThat(account.getBalance()).isEqualTo(900L);
    }

    // 꼼꼼하게 하자..
    // 완벽한 테스트는 존재할 수 없다.
    // 계좌 이체_테스트 (서비스)
    @Test
    public void 계좌이체_test() throws Exception{
        //given
        Long userId = 1000L;
        AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
        accountTransferReqDto.setWithdrawNumber(1111L);
        accountTransferReqDto.setDepositNumber(2222L);
        accountTransferReqDto.setWithdrawPassword(1234L);
        accountTransferReqDto.setAmount(100L);
        accountTransferReqDto.setGubun("TRANSFER");

        User ssar = newMockUser(1000L, "ssar", "쌀");
        User cos = newMockUser(2L, "cos", "코스");

        Account withdrawAccount = newMockAccount(1L, 1111L, 1000L, ssar);
        Account depositAccount = newMockAccount(2L, 2222L, 1000L, cos);
        //when
        // 출금계좌와 입금계좌가 동일 X
        if(accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()){
            throw new CustomApiException("입출금계좌가 동일할 수 없습니다.");
        }

        // 0원 체크
        if(accountTransferReqDto.getAmount() <= 0L){
            throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
        }

        // 출금 소유자 확인 (로그인한 사람과 동일한지)
        withdrawAccount.checkOwner(userId);

        // 출금계좌 비밀번호 확인
        withdrawAccount.checkSamePassword(accountTransferReqDto.getWithdrawPassword());

        // 출금계좌 잔액 확인
        withdrawAccount.checkBalance(accountTransferReqDto.getAmount());

        // 이체하기
        withdrawAccount.withdraw(accountTransferReqDto.getAmount());
        depositAccount.deposit(accountTransferReqDto.getAmount());

        //then
        assertThat(withdrawAccount.getBalance()).isEqualTo(900L);
        assertThat(depositAccount.getBalance()).isEqualTo(1100L);
    }
    // 계좌목록보기_유저별_테스트 (서비스)

    // 계좌상세보기_테스트 (서비스)


}