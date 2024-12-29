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
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.account.AccountReqDto;
import shop.mtcoding.bank.dto.account.AccountReqDto.AccountSaveReqDto;
import shop.mtcoding.bank.dto.account.AccountRespDto;
import shop.mtcoding.bank.dto.account.AccountRespDto.AccountListRespDto;
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
}