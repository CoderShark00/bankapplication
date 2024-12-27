package shop.mtcoding.bank.dto;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class ResponseDto<T> {
    private final Integer code; // 1 성공, -1 실패
    private final String msg;
    private final T data; // 한번만들면 수정할 일이없음 final


}
