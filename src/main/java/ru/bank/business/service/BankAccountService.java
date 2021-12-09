package ru.bank.business.service;

import org.springframework.data.domain.Pageable;
import ru.bank.web.dto.BankAccountDto;
import ru.bank.web.dto.DispatchMoneyDto;

import javax.transaction.Transactional;
import java.util.List;

public interface BankAccountService {
    BankAccountDto create(BankAccountDto bankAccountDto);

    @Transactional
    void remove (String numberAccount);

    List<BankAccountDto> getAll(Pageable pageable);

    void dispatchMoney(DispatchMoneyDto dispatchMoneyDto);
}
