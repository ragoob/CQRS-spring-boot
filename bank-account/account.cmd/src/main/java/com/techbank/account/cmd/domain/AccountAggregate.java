package com.techbank.account.cmd.domain;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.common.events.AccountClosedEvent;
import com.techbank.account.common.events.AccountOpenedEvent;
import com.techbank.account.common.events.FundsDepositedEvent;
import com.techbank.account.common.events.FundsWithdrawEvent;
import com.techbank.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;
@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private  Boolean active;
    private  double balance;

    /*

    I Like the idea in this class ....

    I think there's a pattern here too..

    1. validate
    2. fire event

    can we have some kind of generalization here so to reduce the plumping code?

     */

    public  AccountAggregate(OpenAccountCommand command){
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .createdDate(new Date()).build());
    }

    public  void  apply(AccountOpenedEvent event){
        this.id = event.getId();
        this.active= true;
        this.balance = event.getOpeningBalance();
    }

    public  void depositFunds(double amount){
        if(!this.active){
            throw  new IllegalStateException("Funds cannot be deposited into a closed account!");
        }
        if(amount <=0){
            throw  new IllegalStateException("The deposit amount must be greater than 0!");
        }
        raiseEvent(FundsDepositedEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public  void  apply(FundsDepositedEvent event){
        this.id = event.getId();
        this.balance = this.getBalance() + event.getAmount();
    }

    public  void withdrawFunds(double amount){
        if(!this.active){
            throw  new IllegalStateException("Funds cannot be deposited into a closed account!");
        }
        raiseEvent(FundsWithdrawEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public  void  apply(FundsWithdrawEvent event){
        this.id = event.getId();
        this.balance = this.getBalance() + event.getAmount();
    }
    public  void  closeAccount(){
        if(!this.active){
            throw  new IllegalStateException("The bank account has already been closed!");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id)
                .build());
    }

    public void apply(AccountClosedEvent event){
        this.id = event.getId();
        this.active = false;
    }

    public double getBalance() {
        return balance;
    }
}
