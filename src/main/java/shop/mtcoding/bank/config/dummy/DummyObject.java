package shop.mtcoding.bank.config.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.mtcoding.bank.domain.user.User;

import java.time.LocalDateTime;

public class DummyObject {


    protected User newUser(String username, String fullname){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encPassword = encoder.encode("1234");

        return User.builder()
                .username(username)
                .password(encPassword)
                .email(username+"@nate.com")
                .fullname(fullname)
                .build();
    }

    protected User newMockUser(Long id, String username, String fullname){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encPassword = encoder.encode("1234");

       return User.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username+"@nate.com")
                .fullname(fullname)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}