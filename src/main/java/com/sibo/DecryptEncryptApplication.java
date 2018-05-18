package com.sibo;

import com.sibo.decryptEncrypt.annotation.EncryptDecrypt;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication(scanBasePackages = { "com.sibo" })
@EncryptDecrypt
@RestController
@EnableAutoConfiguration
public class DecryptEncryptApplication {

    @RequestMapping("/test")
    String test(@RequestBody String str) {
        System.out.println(str);
        return str;
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(DecryptEncryptApplication.class, args);
    }

}