package shop.mtcoding.bank.dto.user;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.domain.user.UserEnum;

public class UserReqDto {


    @Getter
    @Setter
    public static class LoginReqDto {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class JoinReqDto{
        // 영문, 숫자는 되고, 길이는 최소 2~20자 이내 한글안됨.
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}", message = "영문/숫자 2~20자 이내로 작성해주세요.")
        @NotEmpty // null || 공백일 수 없다.
        private String username;
        // 길이 4~20
        @NotEmpty
        @Size(min = 4, max = 20)
        private String password;

        // 이메일 형식
        @Pattern(regexp = "^[a-zA-Z0-9]{2,6}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "이메일 형식으로 작성해주세요.")
        @NotEmpty
        private String email;

        // 영어, 한글, 1~20
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1~20자 이내로 작성해주세요")
        @NotEmpty
        private String fullname;

        public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
            return User.builder()
                    .username(username)
                    .password(bCryptPasswordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }
}
