package com.study2.spring_study_2.controller;

import com.study2.spring_study_2.model.User;
import java.util.List;
import java.util.Objects;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping({"/users"})
public class UserController {
  private final List<User> users = List.of(new User(1L, "홍길동", 25), new User(2L, "김철수", 30));

  @GetMapping
  public Flux<User> getAllUsers() {
    return Flux.fromIterable(this.users);
  }

  @GetMapping({"/{id}"})
  public Mono<User> getUserById(@PathVariable Long id) {
    return Flux.fromIterable(this.users).filter((user) -> Objects.equals(user.getId(), id)).next();
  }
}

