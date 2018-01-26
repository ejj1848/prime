package com.techprime.prime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.UUID;
import java.util.stream.Stream;

@SpringBootApplication
public class PrimeApplication {

    public static void main(String[] args) {
        SpringApplication.run(PrimeApplication.class, args);
    }

    @Bean
    CommandLineRunner commandLineRunner(EmployeeRepository employeeRepository) {

        return args -> {
            employeeRepository.deleteAll()
                    .subscribe(null, null, () -> {
                        Stream.of(new Employee(UUID.randomUUID().toString(), "Eric", 100L)
                                , new Employee(UUID.randomUUID().toString(), "Tony", 12L)
                                , new Employee(UUID.randomUUID().toString(), "Bip", 10000L))
                                .forEach(employee -> {
                                    employeeRepository.save(employee)
                                            .subscribe(System.out::print);
                                });

                    });
        };

    }
}
