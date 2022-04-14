package com.techbank.account.cmd.api.controllers;

import com.techbank.account.cmd.api.commands.OpenAccountCommand;
import com.techbank.account.cmd.api.dto.OpenAccountResponse;
import com.techbank.account.common.dto.BaseResponse;
import com.techbank.cqrs.core.infrastructure.CommandDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequestMapping(path = "/api/v1/openBankAccount")
public class OpenAccountController {
    private  final Logger logger = Logger.getLogger(OpenAccountController.class.getName()); // could use @SLF4j from lombok

    @Autowired  // use constructor injection
    private CommandDispatcher commandDispatcher;

    @PostMapping
    public ResponseEntity<BaseResponse> OpenAccount(@RequestBody OpenAccountCommand command){
        var id = UUID.randomUUID().toString();
        command.setId(id);
        try{
            commandDispatcher.send(command);
            return  new ResponseEntity<>(new OpenAccountResponse("Bank account creation request completed successfully",id), HttpStatus.CREATED);

        }catch (IllegalStateException e){

            // Use Exception Translator Pattern for generic exceptions to make code more clear
            // see https://www.baeldung.com/exception-handling-for-rest-with-spring#controlleradvice

            logger.log(Level.WARNING, MessageFormat.format("Client made a bad request -  {0}.",e.toString()));
            return new ResponseEntity<>(new BaseResponse(e.toString()),HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            var safeErrorMessage = MessageFormat.format("Error while processing request to open a new bank account for id - {0}",id);
            logger.log(Level.SEVERE,safeErrorMessage,e);
            return new ResponseEntity<>(new OpenAccountResponse(safeErrorMessage,id),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
