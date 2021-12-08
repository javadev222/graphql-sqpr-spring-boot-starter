package ru.bank.business.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.bank.business.dao.BankAccountRepository;
import ru.bank.business.entity.BankAccountEntity;
import ru.bank.business.service.BankAccountService;
import ru.bank.web.dto.BankAccountDto;
import ru.bank.web.dto.DispatchMoneyDto;
import ru.bank.web.error.BankAccountException;
import ru.bank.web.error.ResourceNotFoundException;
import ru.bank.web.mapper.BankAccountMapper;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {
    public static final String YOUR_ACCOUNT_HAS_INSUFFICIENT_FUNDS = "Your account has insufficient funds";
    private final BankAccountRepository repository;
    private final BankAccountMapper bankAccountMapper;

    @Transactional
    @Override
    public BankAccountDto create(BankAccountDto bankAccountDto) {
        return bankAccountMapper.toDto(repository.saveAndFlush(bankAccountMapper.toEntity(bankAccountDto)));
    }

    @Transactional
    @Override
    public void remove (String numberAccount) {
        repository.deleteById(numberAccount);
    }

    @Override
    public List<BankAccountDto> getAll() {
        return bankAccountMapper.toDtos(repository.findAll());
    }

    private BankAccountEntity getById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    @Transactional
    @Override
    public void dispatchMoney(DispatchMoneyDto dispatchMoneyDto) {
        BankAccountEntity sourceBankAccount = getById(dispatchMoneyDto.getSourceId());
        BigDecimal money = dispatchMoneyDto.getMoney();
        BigDecimal sourceBalance = sourceBankAccount.getBalance();
        if (sourceBalance.compareTo(money) < 0) {
            log.error(YOUR_ACCOUNT_HAS_INSUFFICIENT_FUNDS);
            throw new BankAccountException(YOUR_ACCOUNT_HAS_INSUFFICIENT_FUNDS);
        }
        BigDecimal newBalanceSource = sourceBalance.subtract(money);
        sourceBankAccount.setBalance(newBalanceSource);
        BankAccountEntity targetBankAccount = getById(dispatchMoneyDto.getTargetId());
        BigDecimal newBalanceTarget = targetBankAccount.getBalance().add(money);
        targetBankAccount.setBalance(newBalanceTarget);
    }
}
