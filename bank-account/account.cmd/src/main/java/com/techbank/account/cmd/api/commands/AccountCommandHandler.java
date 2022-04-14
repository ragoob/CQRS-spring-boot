package com.techbank.account.cmd.api.commands;

import com.techbank.account.cmd.domain.AccountAggregate;
import com.techbank.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements  CommandHandler{

    @Autowired // use constructor injection
    private EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    /*

    I can see a pattern here in all the methods, here's the code in this class:

        var aggregate = eventSourcingHandler.getById(command.getId());
        // do some logic related to the function
        eventSourcingHandler.save(aggregate);


    so can we have some kind of AOP, so to have an aspect for the non-functional code here

     */

    @Override
    public void handle(OpenAccountCommand command) {
      var aggregate = new AccountAggregate(command);
      eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFundsCommand command) {
      var aggregate = eventSourcingHandler.getById(command.getId());
      aggregate.depositFunds(command.getAmount());
      eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithdrawFundsCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        if(command.getAmount() > aggregate.getBalance()){
            throw  new IllegalStateException("Withdrawal declined, insufficient funds!");
        }
        aggregate.withdrawFunds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);
    }
}
