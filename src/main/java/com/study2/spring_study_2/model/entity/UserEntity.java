package com.study2.spring_study_2.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

  @Id
  private Long id;
  private String name;
  private int age;
}
