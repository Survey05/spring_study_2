package com.study2.spring_study_2.handler;

import com.study2.spring_study_2.exception.UserNotFoundException;
import com.study2.spring_study_2.model.dto.UserDto;
import com.study2.spring_study_2.service.UserService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class UserHandler {

  private final UserService userService;

  public Mono<ServerResponse> getAllUsers(ServerRequest request) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userService.getAllUsers(), UserDto.class);
  }

  public Mono<ServerResponse> getUserById(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));
    return userService.getUserById(id)
        .flatMap(userDto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(userDto));
  }

  public Mono<ServerResponse> createUser(ServerRequest request) {
    return request.bodyToMono(UserDto.class)
        .flatMap(userService::createUser)
        .flatMap(userDto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(userDto));
  }

  public Mono<ServerResponse> updateUser(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));
    return request.bodyToMono(UserDto.class)
        .flatMap(userDto -> userService.updateUser(id, userDto))
        .flatMap(updatedUserDto -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(updatedUserDto));
  }

  public Mono<ServerResponse> deleteUser(ServerRequest request) {
    Long id = Long.valueOf(request.pathVariable("id"));
    return userService.deleteUser(id)
        .then(ServerResponse.ok().build());
  }

  public Mono<ServerResponse> getUsersByMinAge(ServerRequest request) {
    int minAge = Integer.parseInt(request.pathVariable("minAge"));
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(userService.getUsersByMinAge(minAge), UserDto.class);
  }

  public Mono<ServerResponse> getUsersByName(ServerRequest request) {
    String name = request.pathVariable("name");
    Flux<UserDto> users = userService.getUsersByName(name);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users, UserDto.class);
          } else {
            return Mono.error(new UserNotFoundException("user with name " + name + " not found"));
          }
        });
  }

  public Mono<ServerResponse> getUsersStartingWith(ServerRequest request) {
    String prefix = request.pathVariable("prefix");
    Flux<UserDto> users = userService.getUsersStartingWith(prefix);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users, UserDto.class);
          } else {
            return Mono.error(new UserNotFoundException("user starting with " + prefix + " not found"));
          }
        });
  }

  public Mono<ServerResponse> getUsersContainingWith(ServerRequest request) {
    String keyword = request.pathVariable("keyword");
    Flux<UserDto> users = userService.getUsersContaining(keyword);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users, UserDto.class);
          } else {
            return Mono.error(new UserNotFoundException("user containing keyword " + keyword + " not found"));
          }
        });
  }

  public Mono<ServerResponse> getUsersByBetweenOrderByAge(ServerRequest request) {
    Integer min = Integer.valueOf(request.queryParam("min").orElseThrow(() -> new RuntimeException("min query parameter is required")));
    Integer max = Integer.valueOf(request.queryParam("max").orElseThrow(() -> new RuntimeException("max query parameter is required")));
    String sort = request.queryParam("sort").orElse("asc");
    Flux<UserDto> users = userService.getUsersByAgeRangeSorted(min, max, sort);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users, UserDto.class);
          } else {
            return Mono.error(new UserNotFoundException("user with age " + min + " ~ " + max + " not found"));
          }
        });
  }

  public  Mono<ServerResponse> isNameDuplicate(ServerRequest request) {
    String name = request.pathVariable("name");
    Mono<Boolean> exists = userService.isNameDuplicate(name);

    return exists.flatMap(exist -> {
      if (exist) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(
            Map.of("duplicate", exist));
      } else {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(
            Map.of("duplicate", exist));
      }
    });
  }

  public Mono<ServerResponse> getUsersWithPagination(ServerRequest request) {
    Integer page = Integer.valueOf(request.queryParam("page").orElse("0"));
    Integer size = Integer.valueOf(request.queryParam("size").orElse("10"));
    Flux<UserDto> users = userService.getUsersWithPagination(page, size);

    return users.hasElements()
        .flatMap(exists -> {
          if (exists) {
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users, UserDto.class);
          }
          else {
            return Mono.error(new UserNotFoundException("user with page " + page + " not found"));
          }
        });
  }
}
