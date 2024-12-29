package shop.mtcoding.bank.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
@ActiveProfiles("test")
@AutoConfigureMockMvc // Mock(가짜) 환경에 MockMvc가 등록
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SecurityConfigTest {
    // 가짜 환경에 등록된 MockMvc를 DI함.
    @Autowired
    private MockMvc mockMvc;

    // 서버는 일관성있게 에러가 리턴되어야 한다.
    // 내가 모르는 에러가 프론트한테 날라가지 않게, 내가 직접 다 제어하자.
    @Test
    public void authentication_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/s/hello"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();
        System.out.println("테스트 : " + responseBody);
        System.out.println("테스트 : " + httpStatus);

        // then
        assertThat(httpStatus).isEqualTo(401);
    }


    @Test
    public void authorization_test() throws Exception {
        // given

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/admin/hello"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatus = resultActions.andReturn().getResponse().getStatus();
        System.out.println("테스트 : " + responseBody);
        System.out.println("테스트 : " + httpStatus);

        // then
        assertThat(httpStatus).isEqualTo(401);
    }

}