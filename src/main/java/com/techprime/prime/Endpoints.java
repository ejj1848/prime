package com.techprime.prime;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.awt.*;
import java.time.Duration;
import java.util.Date;
import java.util.stream.Stream;

@RestController
@RequestMapping("/rest/employee")
public class Endpoints {

    private final EmployeeRepository employeeRepository;

    public Endpoints(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/")
    public Flux<Employee> getAll() {

        return employeeRepository.findAll();

    }


    @GetMapping("/{id}")
    public Mono<Employee> getId(@PathVariable("id") String id) {
        return employeeRepository.findById(id);
    }

    @GetMapping(value = "/{id}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<EmpEvent> getEvents(@PathVariable("id") String id) {

      return employeeRepository.findById(id)
                .flatMapMany(employee -> {
                    Flux<Long> interval = Flux.interval(Duration.ofSeconds(2L));

                    Flux<EmpEvent> empEventFlux =
                            Flux.fromStream(
                                    Stream.generate(() -> new EmpEvent(employee, new Date()))
                            );
                    return Flux.zip(interval, empEventFlux)
                            .map(Tuple2::getT2);

                });
    }
}
