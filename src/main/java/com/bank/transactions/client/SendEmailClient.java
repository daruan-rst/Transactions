package com.bank.transactions.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value ="email", url="http://localhost:6080")
public interface SendEmailClient {

    @RequestMapping(value="email/send-email", method= RequestMethod.POST )
    SendEmail sendingTheEmail (SendEmail sendEmail);
}
