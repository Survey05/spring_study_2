package com.study2.spring_study_2.router;

import com.study2.spring_study_2.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Configuration
public class UserRouter {

  @Bean
  public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {
    return RouterFunctions
        .route(GET("/users").and(accept(org.springframework.http.MediaType.APPLICATION_JSON)), userHandler::getAllUsers)
        .andRoute(GET("/users/{id}").and(accept(org.springframework.http.MediaType.APPLICATION_JSON)), userHandler::getUserById);
  }
}
