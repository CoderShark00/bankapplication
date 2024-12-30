package shop.mtcoding.bank.temp;

import org.junit.jupiter.api.Test;

public class LongTest {
    @Test
    public void long_test2() throws Exception {
        //given (2의 8승 -- 256 범위 (-126 + 127))
        Long v1 = 10000L;
        Long v2 = 10000L;

        //when
        if(v1 == v2){
            System.out.println("테스트 : 같습니다..");
        }
    }

    @Test
    public void long_test() throws Exception {
        //given
        Long number1 = 1111L;
        Long number2 = 1111L;

        //when
        if(number1.equals(number2)) {
            System.out.println("테스트 : 동일합니다.");
        }else {
            System.out.println("테스트 : 동일하지 않습니다.");
        }
    }


}
