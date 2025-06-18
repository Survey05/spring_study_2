package com.study2.spring_study_2.service;

import com.study2.spring_study_2.model.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
  Flux<UserDto> getAllUsers();
  Mono<UserDto> getUserById(Long id);
  Mono<UserDto> createUser(UserDto userDto);
  Mono<UserDto> updateUser(Long id, UserDto userDto);
  Mono<Void> deleteUser(Long id);
  Flux<UserDto> getUsersByMinAge(int minAge);
  Flux<UserDto> getUsersByName(String name);
  Flux<UserDto> getUsersStartingWith(String prefix);
  Flux<UserDto> getUsersContaining(String keyword);
  Flux<UserDto> getUsersByAgeRangeSorted(int min, int max, String sort);
  Mono<Boolean> isNameDuplicated(String name);
  Flux<UserDto> getUsersWithPagination(int page, int size);
}

