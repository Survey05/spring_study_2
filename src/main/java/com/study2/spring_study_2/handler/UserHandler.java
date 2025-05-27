package com.study2.spring_study_2.handler;

import com.study2.spring_study_2.model.User;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Component
public class UserHandler {

  private final List<User> users = List.of(
      new User(1L, "홍길동", 25),
      new User(2L, "김철수", 30)
  );

  public Mono<ServerResponse> getAllUsers(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(Flux.fromIterable(users), User.class);
  }

  public Mono<ServerResponse> getUserById(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));
    return Flux.fromIterable(users)
        .filter(user -> Objects.equals(user.getId(), id))
        .next()
        .flatMap(user -> ServerResponse.ok()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(user))
        .switchIfEmpty(ServerResponse.notFound().build());
  }
}
