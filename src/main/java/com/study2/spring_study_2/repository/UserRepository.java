package com.study2.spring_study_2.repository;

import com.study2.spring_study_2.model.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, Long> {

  Flux<User> findByAgeGreaterThan(int age);
  Flux<User> findByName(String name);
  Flux<User> findByAgeBetween(int min, int max);
  Flux<User> findByNameStartingWith(String prefix);
  Flux<User> findByNameContaining(String keyword);

}
