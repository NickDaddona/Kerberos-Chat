package edu.ccsu.cs492.kerberoschat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;

@Controller
@SpringBootApplication
public class KerberosChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(KerberosChatApplication.class, args);
    }
}
