package com.nextodev.javaopenapirest.controller;

import com.nextodev.javaopenapirest.api.HelloApi;
import com.nextodev.javaopenapirest.model.HelloObject1;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController implements HelloApi {

    @Override
    public ResponseEntity<Resource> sayHello(HelloObject1 helloObject1, String format) {
        System.out.println("Hello");
        System.out.println(helloObject1 + " " + format);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/sayHello")
    public String sayHello() {
        return  "Hello";
    }
}
