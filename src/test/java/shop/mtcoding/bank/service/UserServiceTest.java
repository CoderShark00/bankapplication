package shop.mtcoding.bank.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto;
import shop.mtcoding.bank.dto.user.UserRespDto;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Spring 관련 Bean들이 하나도 없는 환경
@ExtendWith(MockitoExtension.class) // 모키토환경
class UserServiceTest extends DummyObject {

    @InjectMocks // 가짜환경에 주입
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Spy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    public void 회원가입_test() throws Exception{
        // given
        UserReqDto.JoinReqDto joinReqDto = new UserReqDto.JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ssar@nate.com");
        joinReqDto.setFullname("쌀");

        // stub 1
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        // stub2
        User ssar = newMockUser(1L, "ssar", "쌀");
        when(userRepository.save(any())).thenReturn(ssar);

        // when
        UserRespDto.JoinRespDto joinRespDto = userService.회원가입(joinReqDto);
        System.out.println("테스트 : " + joinRespDto);

        // then
        assertThat(joinRespDto.getId()).isEqualTo(1L);
        assertThat(joinRespDto.getUsername()).isEqualTo("ssar");
    }
}