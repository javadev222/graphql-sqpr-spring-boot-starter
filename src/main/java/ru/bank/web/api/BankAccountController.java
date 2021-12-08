//package ru.testing.web.api;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import ru.testing.business.service.BankAccountService;
//import ru.testing.web.dto.BankAccountCreateDto;
//import ru.testing.web.dto.BankAccountGetDto;
//import ru.testing.web.dto.DispatchMoneyDto;
//
//import javax.validation.Valid;
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("api/v1/accounts")
//public class BankAccountController {
//    private final BankAccountService bankAccountService;
//
////    @PostMapping
////    public ResponseEntity<BankAccountGetDto> createBankAccount(@RequestBody @Valid BankAccountCreateDto bankAccountDto) {
////        return ResponseEntity.ok().body(bankAccountService.create(bankAccountDto));
////    }
////
////    @GetMapping
////    public ResponseEntity<List<BankAccountGetDto>> getBankAccounts() {
////        return ResponseEntity.ok().body(bankAccountService.getAll());
////    }
////
////    @PutMapping("/dispatch")
////    public HttpStatus dispatchBankAccount(@RequestBody @Valid DispatchMoneyDto dispatchMoneyDto) {
////        bankAccountService.dispatchMoney(dispatchMoneyDto);
////        return HttpStatus.OK;
////    }
//}
