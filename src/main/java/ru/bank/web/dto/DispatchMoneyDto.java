package ru.bank.web.dto;

import io.leangen.graphql.annotations.GraphQLInputField;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DispatchMoneyDto {
    @NotBlank
    @GraphQLInputField
    String sourceId;
    @NotBlank
    @GraphQLInputField
    String targetId;
    @NotNull
    @GraphQLInputField
    BigDecimal money;
}
