package shop.mtcoding.bank.service;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.mtcoding.bank.domain.account.Account;
import shop.mtcoding.bank.domain.account.AccountRepository;
import shop.mtcoding.bank.domain.transaction.Transaction;
import shop.mtcoding.bank.domain.transaction.TransactionRepository;
import shop.mtcoding.bank.dto.transaction.TransactionRespDto;
import shop.mtcoding.bank.handler.ex.CustomApiException;
import shop.mtcoding.bank.util.CustomDateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionRespDto.TransactionListRespDto 입출금목록보기(Long userId, Long accountNumber, String gubun, Integer page){
        Account accountPS = accountRepository.findByNumber(accountNumber)
                .orElseThrow(()-> new CustomApiException("해당 계좌를 찾을 수 없습니다."));
        accountPS.checkOwner(userId);
        List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountPS.getId(),gubun,page);
        return new TransactionRespDto.TransactionListRespDto(transactionListPS, accountPS);
    }
 }
