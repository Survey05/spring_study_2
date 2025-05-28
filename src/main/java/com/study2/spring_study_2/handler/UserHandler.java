package com.study2.spring_study_2.handler;

import com.study2.spring_study_2.model.User;
import com.study2.spring_study_2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class UserHandler {

  private final UserRepository userRepository;

  public Mono<ServerResponse> getAllUsers(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userRepository.findAll(), User.class);
  }

  public Mono<ServerResponse> getUserById(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));
    return userRepository.findById(id)
        .flatMap(user -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> createUser(ServerRequest request) {
    return request.bodyToMono(User.class)
        .flatMap(userRepository::save)
        .flatMap(saved -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(saved));
  }
}

