package shop.mtcoding.bank.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.bank.config.dummy.DummyObject;
import shop.mtcoding.bank.domain.user.UserRepository;
import shop.mtcoding.bank.dto.user.UserReqDto;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Sql("classpath:db/teardown.sql")
class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManager em;

    @BeforeEach
    public void setUp() throws Exception {
        dataSetting();
        em.clear();
    }

    private void dataSetting() {
        userRepository.save(newUser("ssar", "쌀"));
    }

    @Test
    public void join_success_test() throws Exception {
        //given
        UserReqDto.JoinReqDto joinReqDto = new UserReqDto.JoinReqDto();
        joinReqDto.setUsername("love");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("love@nate.com");
        joinReqDto.setFullname("러브");

        String requestBody = om.writeValueAsString(joinReqDto); // JSON으로 변경
//        System.out.println("테스트 : " + requestBody);
        //when
        ResultActions resultActions = mvc.perform(post("/api/join")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)); // Body 값이 있으면 Body값을 설명하는 MediaType이 꼭 필요
//        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
//        System.out.println("테스트 : " + responseBody);
        //then
        resultActions.andExpect(status().isCreated());
    }


    @Test
    public void join_fail_test() throws Exception {
        //given
        UserReqDto.JoinReqDto joinReqDto = new UserReqDto.JoinReqDto();
        joinReqDto.setUsername("ssar");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("ssar@nate.com");
        joinReqDto.setFullname("쌀");

        String requestBody = om.writeValueAsString(joinReqDto); // JSON으로 변경
//        System.out.println("테스트 : " + requestBody);

        //when
        ResultActions resultActions = mvc.perform(post("/api/join")
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON)); // Body 값이 있으면 Body값을 설명하는 MediaType이 꼭 필요
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println("테스트 : " + responseBody);

        //then
        resultActions.andExpect(status().isBadRequest());


    }
}
