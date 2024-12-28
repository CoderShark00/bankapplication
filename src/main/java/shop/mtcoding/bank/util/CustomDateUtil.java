package shop.mtcoding.bank.util;

import org.springframework.format.datetime.DateFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateUtil {

    public static String toStringFormat(LocalDateTime localDateTime){
        // 2023-01-02Tjfkasfkql 이런식으로 리턴되는걸 방지하고자 만든것
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
