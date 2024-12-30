package shop.mtcoding.bank.config.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import shop.mtcoding.bank.config.auth.LoginUser;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@ActiveProfiles("test")
class JwtProcessTest {

    private String createToken(){
        //given
        User user = User.builder().id(1L).role(UserEnum.ADMIN).build();
        LoginUser loginUser = new LoginUser(user);

        //when
        String jwtToken = JwtProcess.create(loginUser);
        return jwtToken;
    }

    @Test
    public void create_test()throws Exception{
        //given

        //when
        String jwtToken = createToken();
        System.out.println("테스트 : " + jwtToken);

        //then
        assertTrue(jwtToken.startsWith(JwtV0.TOKEN_PREFIX));
    }

    @Test
    public void verify_test()throws Exception{
        //given
        String token = createToken(); // Bearer 제거
        String jwtToken = token.replace(JwtV0.TOKEN_PREFIX,"");
        //when
        LoginUser loginUser = JwtProcess.verify(jwtToken);
        System.out.println("테스트 : " + loginUser.getUser().getId());

        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.ADMIN);
    }
}