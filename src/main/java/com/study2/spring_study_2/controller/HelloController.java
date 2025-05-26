package com.study2.spring_study_2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class HelloController {
  @GetMapping({"/hello"})
  public Mono<String> sayHello() {
    return Mono.just("Hello WebFlux!");
  }
}
