package com.study2.spring_study_2.exception;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(String message) {
    super(message);
  }

}
