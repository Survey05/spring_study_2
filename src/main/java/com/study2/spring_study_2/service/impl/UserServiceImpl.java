package com.study2.spring_study_2.service.impl;

import com.study2.spring_study_2.exception.UserNotFoundException;
import com.study2.spring_study_2.mapper.UserMapper;
import com.study2.spring_study_2.model.dto.UserDto;
import com.study2.spring_study_2.model.entity.UserEntity;
import com.study2.spring_study_2.repository.UserRepository;
import com.study2.spring_study_2.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Override
  public Flux<UserDto> getAllUsers() {
    return userRepository.findAll()
        .map(userMapper::toDto);
  }

  @Override
  public Mono<UserDto> getUserById(Long id) {
    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + id + " not found")))
        .map(userMapper::toDto);
  }

  @Override
  public Mono<UserDto> createUser(UserDto userDto) {
    UserEntity entity = userMapper.toEntity(userDto);
    return userRepository.save(entity)
        .map(userMapper::toDto);
  }

  @Override
  public Mono<UserDto> updateUser(Long id, UserDto userDto) {
    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + id + " not found")))
        .flatMap(existingUser -> {
          existingUser.setName(userDto.getName());
          existingUser.setAge(userDto.getAge());
          return userRepository.save(existingUser);
        })
        .map(userMapper::toDto);
  }

  @Override
  public Mono<Void> deleteUser(Long id) {
    return userRepository.findById(id)
        .switchIfEmpty(Mono.error(new UserNotFoundException("User with id " + id + " not found")))
        .flatMap(userRepository::delete);
  }

  @Override
  public Flux<UserDto> getUsersByMinAge(int minAge) {
    return userRepository.findByAgeGreaterThan(minAge)
        .map(userMapper::toDto);
  }

  @Override
  public Flux<UserDto> getUsersByName(String name) {
    return userRepository.findByName(name)
        .switchIfEmpty(
            Flux.error(new UserNotFoundException("User with name " + name + " not found")))
        .map(userMapper::toDto);
  }

  @Override
  public Flux<UserDto> getUsersStartingWith(String prefix) {
    return userRepository.findByNameStartingWith(prefix)
        .switchIfEmpty(
            Flux.error(new UserNotFoundException("No user found starting with " + prefix)))
        .map(userMapper::toDto);
  }

  @Override
  public Flux<UserDto> getUsersContaining(String keyword) {
    return userRepository.findByNameContaining(keyword)
        .switchIfEmpty(
            Flux.error(new UserNotFoundException("No user found containing " + keyword)))
        .map(userMapper::toDto);
  }

  @Override
  public Flux<UserDto> getUsersByAgeRangeSorted(int min, int max, String sort) {
    switch (sort.toLowerCase()) {
      case "asc":
        return userRepository.findByAgeBetweenOrderByAgeAsc(min, max)
            .map(userMapper::toDto);
      case "desc":
        return userRepository.findByAgeBetweenOrderByAgeDesc(min, max)
            .map(userMapper::toDto);
      default:
        return Flux.error(new IllegalArgumentException("Invalid sort order: " + sort));
    }
  }

  @Override
  public Mono<Boolean> isNameDuplicated(String name) {
    return userRepository.existsByName(name);
  }

  @Override
  public Flux<UserDto> getUsersWithPagination(int page, int size) {
    return userRepository.findAll()
        .skip((long) (page-1) * size)
        .take(size)
        .map(userMapper::toDto);
  }
}
