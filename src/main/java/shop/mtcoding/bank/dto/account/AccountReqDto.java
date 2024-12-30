package shop.mtcoding.bank.dto.account;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.user.User;

public class AccountReqDto {

    // DTO가 똑같아도 재사용하지 않기 (유지보수)
    @Getter
    @Setter
    public static class AccountSaveReqDto{
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;

        public Account toEntity(User user) {
            return Account.builder()
                    .number(number)
                    .password(password)
                    .balance(1000L)
                    .user(user)
                    .build();

        }
    }
    @Getter
    @Setter
    public static class AccountWithdrawReqDto{
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long number;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long password;
        @NotNull
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "WITHDRAW")
        private String gubun;
    }


    @Getter
    @Setter
    public static class AccountTransferReqDto{
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawNumber;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long depositNumber;
        @NotNull
        @Digits(integer = 4, fraction = 4)
        private Long withdrawPassword;
        @NotNull
        private Long amount;
        @NotEmpty
        @Pattern(regexp = "TRANSFER")
        private String gubun;
    }
}
