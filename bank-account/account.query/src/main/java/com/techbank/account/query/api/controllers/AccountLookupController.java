package com.techbank.account.query.api.controllers;

import com.techbank.account.query.api.dto.AccountLookupResponse;
import com.techbank.account.query.api.queries.FindAllAccountQuery;
import com.techbank.account.query.domain.BankAccount;
import com.techbank.cqrs.core.infrastructure.QueryDispatcher;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/bankAccountLookup")
public class AccountLookupController {
    private  final Logger logger = Logger.getLogger(AccountLookupController.class.getName());
    @Autowired
    private QueryDispatcher queryDispatcher;

    @GetMapping(path = "/")
    public ResponseEntity<AccountLookupResponse> getAllAccounts(){
        try{
            List<BankAccount> accounts = queryDispatcher.send(new FindAllAccountQuery());
            if(accounts == null || accounts.size() == 0){
                return  new ResponseEntity<>(null,HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(  AccountLookupResponse.builder()
                    .accounts(accounts)
                    .message("").build(), HttpStatus.OK);

        }catch (Exception e){
            return  new ResponseEntity<>(new AccountLookupResponse("Failed to complete query"),
                    HttpStatus.INTERNAL_SERVER_ERROR
                    );
        }
    }
}
