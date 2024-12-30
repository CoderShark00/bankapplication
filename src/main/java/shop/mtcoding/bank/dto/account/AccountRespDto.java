package shop.mtcoding.bank.dto.account;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.user.User;
import shop.mtcoding.bank.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AccountRespDto {

    @Getter
    @Setter
    public static class AccountSaveRespDto {
        private Long id;
        private Long number;
        private Long balance;

        public AccountSaveRespDto(Account account) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
        }
    }
    @Getter @Setter
    public static class AccountListRespDto{
        private String fullname;
        private List<AccountDto> accounts = new ArrayList<>();

        public AccountListRespDto(User user, List<Account> accounts) {
            this.fullname = user.getFullname();
            this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
        }

        @Getter @Setter
        public class AccountDto{
            private Long id;
            private Long number;
            private Long balance;


            public AccountDto(Account account) {
                this.id = account.getId();
                this.number = account.getNumber();
                this.balance = account.getBalance();
            }
        }
    }
    @Setter
    @Getter
    public static class AccountDepositRespDto{
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private TransactionDto transaction;

        public AccountDepositRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter @Setter
        public class TransactionDto{
            private Long id;
            private String gubun;
            private String reciver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction){
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.reciver = transaction.getSender();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
    @Setter
    @Getter
    public static class AccountWithdrawRespDto{
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private Long balance;
        private TransactionDto transaction;

        public AccountWithdrawRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

        @Getter @Setter
        public class TransactionDto{
            private Long id;
            private String gubun;
            private String receiver;
            private Long amount;
            private String createdAt;

            public TransactionDto(Transaction transaction){
                this.id = transaction.getId();
                this.gubun = transaction.getGubun().getValue();
                this.receiver = transaction.getSender();
                this.amount = transaction.getAmount();
                this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
            }
        }
    }
    @Setter @Getter
    public static class AccountTransferRespDto{
        private Long id; // 계좌 ID
        private Long number; // 계좌번호
        private Long balance; // 출금 계좌 잔액
        private TransactionDto transaction;

        public AccountTransferRespDto(Account account, Transaction transaction) {
            this.id = account.getId();
            this.number = account.getNumber();
            this.balance = account.getBalance();
            this.transaction = new TransactionDto(transaction);
        }

    @Getter @Setter
    public class TransactionDto{
        private Long id;
        private String gubun;
        private String sender;
        private String receiver;
        private Long amount;
//        @JsonIgnore
        private Long depositAccountBalance;
        private String createdAt;

        public TransactionDto(Transaction transaction){
            this.id = transaction.getId();
            this.gubun = transaction.getGubun().getValue();
            this.sender = transaction.getSender();
            this.receiver = transaction.getReceiver();
            this.amount = transaction.getAmount();
            this.depositAccountBalance = transaction.getDepositAccountBalance();
            this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
        }
    }
    }
}
