package ru.bank.web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.bank.web.dto.BankAccountDto;
import ru.bank.business.entity.BankAccountEntity;

import java.math.RoundingMode;
import java.util.List;

@Mapper(imports = {RoundingMode.class})
public interface BankAccountMapper {

    @Mapping(target = "numberAccount", expression = "java(BankAccountEntity.generationNumberAccount())")
    @Mapping(target = "balance",
            expression = "java(bankAccountDto.getBalance().setScale(bankAccountDto.getCurrency().getDefaultFractionDigits()," +
                    " RoundingMode.HALF_UP))")
    BankAccountEntity toEntity(BankAccountDto bankAccountDto);

    BankAccountDto toDto(BankAccountEntity bankAccountEntity);

    List<BankAccountDto> toDtos(List<BankAccountEntity> bankAccountDtos);

}