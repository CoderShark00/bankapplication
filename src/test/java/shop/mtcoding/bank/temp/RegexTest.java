package shop.mtcoding.bank.temp;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.springframework.security.config.http.MatcherType.regex;

public class RegexTest {

    @Test
    public void 한글만된다_test(){
        String value = "한글";
        boolean result = Pattern.matches("^[가-힣]+$", value);
        System.out.println("테스트 : " + result);
    }

    @Test // 공백은됨.
    public void 한글은안된다_test(){
        String value = "";
        boolean reulst = Pattern.matches("^[^ㄱ-ㅎ가-힣]*$", value);
        System.out.println("테스트 : " + reulst);
    }
    @Test
    public void 영어만된다_test(){
        String value = "asd";
        boolean result = Pattern.matches("^[a-zA-Z]+$", value);
        System.out.println("테스트 : " + result);
    }


    @Test
    public void 영어는안된다_test(){
        String value = "아아";
        boolean result = Pattern.matches("^[^a-zA-Z]*$", value);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void 영어와숫자만된다_test(){
        String value = "as2d";
        boolean result = Pattern.matches("^[a-zA-Z0-9]+$", value);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void 영어만되고_길이는최소2최대4이다_test(){
        String value = "as";
        boolean result = Pattern.matches("^[a-zA-Z]{2,4}$", value);
        System.out.println("테스트 : " + result);
    }

    // username, email, fullname
    @Test
    public void user_username_test(){
        String username = "ssar";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,20}$", username);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_fullname_test(){
        String fullname = "ssar";
        boolean result = Pattern.matches("^[a-zA-Z가-힣]{1,20}$", fullname);
        System.out.println("테스트 : " + result);
    }

    @Test
    public void user_email_test(){
        String email = "ssar@gmail.com";
        boolean result = Pattern.matches("^[a-zA-Z0-9]{2,6}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", email);
        System.out.println("테스트 : " + result);
    }
}
