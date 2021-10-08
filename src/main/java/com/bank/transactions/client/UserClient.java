package com.bank.transactions.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value ="user", url="http://localhost:3080/usuario")
public interface UserClient {

    @RequestMapping(value="/verify-existence/", method= RequestMethod.GET )
    String doesThisUserExist(@RequestParam String cpf);
}
