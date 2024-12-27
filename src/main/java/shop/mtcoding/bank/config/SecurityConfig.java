package shop.mtcoding.bank.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import shop.mtcoding.bank.domain.user.UserEnum;
import shop.mtcoding.bank.dto.ResponseDto;
import shop.mtcoding.bank.util.CustomResponseUtil;


@Configuration
public class SecurityConfig {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Bean // Ioc 컨테이너에 BCruptPasswordEncoder() 객체가 등록됨.
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨.");
        return new BCryptPasswordEncoder();
    }
    // JWT 필터 등록이 필요함.


    // JWT 서버를 만들거라 Session은 사용 안할거임
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨.");
        http.headers((h)->h.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)); // iframe 허용안함.
        http.csrf(AbstractHttpConfigurer::disable); // enable이면 postman이 잘 작동안함.
        http.cors((co)->co.configurationSource(configurationSource()));

        // jSessionId를 서버쪽에서 관리하겠다는 뜻
        http.sessionManagement((sm)-> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        // react, app에서 요청하는것
        http.formLogin(AbstractHttpConfigurer::disable);
        // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다.
        http.httpBasic(AbstractHttpConfigurer::disable);

        // Exception 가로채기
        http.exceptionHandling(e->e.authenticationEntryPoint(
                (request, response, exception) -> {
                    CustomResponseUtil.unAuthentication(response, "로그인을 진행해 주세요");
                }));

        http.authorizeHttpRequests(c-> c.requestMatchers("/api/s/**").authenticated()
                .requestMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN) // 최근 공식문서에서 ""로하면됨.
                .anyRequest().permitAll()); // 나머지 요청은 다 허용

        return http.build();
    }


    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨.");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용(프론트 엔트 IP만 허용 react)
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization"); // 옛날에는 디폴트 지금은 아님.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
