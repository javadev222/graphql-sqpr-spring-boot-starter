package ru.bank.web.dataFetcher;

import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLNonNull;
import io.leangen.graphql.annotations.GraphQLQuery;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.bank.business.service.BankAccountService;
import ru.bank.web.dto.BankAccountDto;
import ru.bank.web.dto.DispatchMoneyDto;

import java.util.List;

@RequiredArgsConstructor
@Component
@GraphQLApi
public class BankAccountDataFetcher {
    private final BankAccountService service;

    @GraphQLQuery(name = "getAllBalance")
    public List<BankAccountDto> getAll(@GraphQLArgument(name = "page")Pageable pageable) {
        return service.getAll(pageable);
    }

    @GraphQLMutation(name = "createBankAccount", description = "create a new bank account")
    public BankAccountDto createBankAccount(@GraphQLArgument(name = "createBankAccountInput") @GraphQLNonNull BankAccountDto bankAccount) {
        return service.create(bankAccount);
    }

    @GraphQLMutation(name = "dispatchMoney", description = "dispatch money")
    public void dispatchMoney(@GraphQLArgument(name = "createDispatchMoneyInput") @GraphQLNonNull DispatchMoneyDto dispatchMoneyDto) {
        service.dispatchMoney(dispatchMoneyDto);
    }

    @GraphQLMutation(name = "removeBankAccount", description = "remove bank account")
    public void removeBankAccount(@GraphQLNonNull String numberAccount) {
        service.remove(numberAccount);
    }
}
