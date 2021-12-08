package ru.bank.business.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Currency;
import java.util.concurrent.ThreadLocalRandom;

@Getter
@Setter
@Entity
@Table(name = "bank_account")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class BankAccountEntity {
    @Id
    String numberAccount;

    Currency currency;

    BigDecimal balance;

    public static String generationNumberAccount() {
        int sizeNumber = 20;
        StringBuilder sb = new StringBuilder(sizeNumber);
        for (int i = 0; i < sizeNumber; i++) {
            int index = ThreadLocalRandom.current().nextInt(10);
            sb.append(index);
        }
        return sb.toString();
    }

}
