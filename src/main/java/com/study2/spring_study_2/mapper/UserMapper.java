package com.study2.spring_study_2.mapper;

import com.study2.spring_study_2.model.dto.UserDto;
import com.study2.spring_study_2.model.entity.UserEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserDto toDto(UserEntity entity);
  UserEntity toEntity(UserDto dto);
}