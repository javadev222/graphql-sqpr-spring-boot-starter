package ru.bank.web.dto;

import io.leangen.graphql.annotations.GraphQLId;
import io.leangen.graphql.annotations.GraphQLInputField;
import io.leangen.graphql.annotations.GraphQLScalar;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Currency;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankAccountDto {
    @GraphQLId
    String numberAccount;

    @NotNull
    @GraphQLScalar
    @GraphQLInputField
    Currency currency;

    @NotNull
    @GraphQLInputField
    BigDecimal balance;
}
