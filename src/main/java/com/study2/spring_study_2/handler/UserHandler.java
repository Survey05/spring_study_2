package com.study2.spring_study_2.handler;

import com.study2.spring_study_2.exception.UserNotFoundException;
import com.study2.spring_study_2.model.User;
import com.study2.spring_study_2.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
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
        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + id + " not found")));
  }

  public Mono<ServerResponse> createUser(ServerRequest request) {
    return request.bodyToMono(User.class)
        .flatMap(userRepository::save)
        .flatMap(saved -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(saved));
  }

  public Mono<ServerResponse> updateUser(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));

    return request.bodyToMono(User.class)
        .flatMap(user -> userRepository.findById(Long.valueOf(id))
            .flatMap(existingUser -> {
              existingUser.setName(user.getName());
              existingUser.setAge(user.getAge());
              return userRepository.save(existingUser);
            })
        )
        .flatMap(updateUser -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updateUser))
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> deleteUser(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));
    return userRepository.findById(id)
        .flatMap(existingUser -> userRepository.delete(existingUser)
          .then(ServerResponse.ok().build())
        )
        .switchIfEmpty(ServerResponse.notFound().build());
  }

  public Mono<ServerResponse> getUsersByMinAge(ServerRequest request) {
    int minAge = Integer.valueOf(request.pathVariable("minAge"));
    Flux<User> users = userRepository.findByAgeGreaterThan(minAge);

    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(users, User.class);
  }

  public Mono<ServerResponse> getUsersNameUppercase(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userRepository.findAll()
            .map(user -> {user.setName(user.getName().toUpperCase()); return user;}),
        User.class);
  }

  public Mono<ServerResponse> getUsersByName(ServerRequest request) {
    String name = request.pathVariable("name");

    Flux<User> users = userRepository.findByName(name);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
          }
          else {
            return Mono.error(new UserNotFoundException("user with name " + name + " not found"));
          }
        });
    }

  public Mono<ServerResponse> getUsersByRange(ServerRequest request) {
    int min = Integer.valueOf(request.pathVariable("min"));
    int max = Integer.valueOf(request.pathVariable("max"));

    Flux<User> users = userRepository.findByAgeBetween(min, max);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
          }
          else {
            return Mono.error(new UserNotFoundException("user with age " + min + " ~ " + max + " not found"));
          }
        });
  }

  public Mono<ServerResponse> getUsersStartingWith(ServerRequest request) {

    String prefix = request.pathVariable("prefix");

    Flux<User> users = userRepository.findByNameStartingWith(prefix);

    return users.hasElements()
        .flatMap(exisits -> {
          if (exisits) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
          }
          else {
            return Mono.error(new UserNotFoundException("user starting with " + prefix + " not found"));
          }
        });
  }

  public Mono<ServerResponse> getUsersContainingWith(ServerRequest request) {
    String keyword = request.pathVariable("keyword");

    Flux<User> users = userRepository.findByNameContaining(keyword);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
          }
          else {
            return Mono.error(new UserNotFoundException("user containing keyword " + keyword + " not found"));
          }
        });
  }

  public Mono<ServerResponse> getUsersByBetweenOrderByAge(ServerRequest request) {
    Integer min = Integer.valueOf(request.queryParam("min").orElseThrow(() -> new RuntimeException("min query parameter is required")));
    Integer max = Integer.valueOf(request.queryParam("max").orElseThrow(() -> new RuntimeException("max query parameter is required")));
    String sort = request.queryParam("sort").orElse("asc");

    Flux<User> users;

    switch (sort.toLowerCase()) {
      case "desc":
        users = userRepository.findByAgeBetweenOrderByAgeDesc(min, max);
        break;
      case "asc":
        users = userRepository.findByAgeBetweenOrderByAgeAsc(min, max);
        break;
      default: return Mono.error(new IllegalArgumentException("sort " + sort + " is not supported"));
    }

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(users, User.class);
          }
          else {
            return Mono.error(new UserNotFoundException("user with age " + min + " ~ " + max + " not found"));
          }
        });
  }

}

