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


    @Test
    public void create_test()throws Exception{
        //given
        User user = User.builder().id(1L).role(UserEnum.ADMIN).build();
        LoginUser loginUser = new LoginUser(user);

        //when
        String jwtToken = JwtProcess.create(loginUser);
        System.out.println("테스트 : " + jwtToken);

        //then
        assertTrue(jwtToken.startsWith(JwtV0.TOKEN_PREFIX));
    }

    @Test
    public void verify_test()throws Exception{
        //given
        String jwtToken = "eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJiYW5rIiwiZXhwIjoxNzM1OTc2OTk5LCJpZCI6MSwicm9sZSI6IkNVU1RPTUVSIn0.JO1_rb5ASt0MXiTU6Wcyc7H4rUvYrJcojbWSjG1i7HOgvabOH0WDxknZR4hNglN3iC923BnuCv_kpqPvr4EcoQ";

        //when
        LoginUser loginUser = JwtProcess.verify(jwtToken);
        System.out.println("테스트 : " + loginUser.getUser().getId());

        //then
        assertThat(loginUser.getUser().getId()).isEqualTo(1L);
        assertThat(loginUser.getUser().getRole()).isEqualTo(UserEnum.CUSTOMER);
    }
}