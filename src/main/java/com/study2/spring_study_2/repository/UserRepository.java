package com.study2.spring_study_2.repository;

import com.study2.spring_study_2.model.entity.UserEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserEntity, Long> {

  Flux<UserEntity> findByAgeGreaterThan(int age);
  Flux<UserEntity> findByName(String name);
  Flux<UserEntity> findByAgeBetween(int min, int max);
  Flux<UserEntity> findByNameStartingWith(String prefix);
  Flux<UserEntity> findByNameContaining(String keyword);
  Flux<UserEntity> findByAgeBetweenOrderByAgeAsc(int min, int max);
  Flux<UserEntity> findByAgeBetweenOrderByAgeDesc(int min, int max);
}
