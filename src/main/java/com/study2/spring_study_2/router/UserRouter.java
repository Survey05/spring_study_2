package com.study2.spring_study_2.router;

import com.study2.spring_study_2.handler.UserHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.*;

@Configuration
public class UserRouter {

  @Bean
  public RouterFunction<ServerResponse> userRoutes(UserHandler userHandler) {

    return RouterFunctions.nest(RequestPredicates.path("/users"), RouterFunctions.route()
        .GET("", userHandler::getAllUsers)
        .GET("/page", userHandler::getUsersWithPagination)
        .GET("/filter", userHandler::getUsersByBetweenOrderByAge)
        .GET("/{id}", userHandler::getUserById)
        .POST("", userHandler::createUser)
        .PUT("/{id}", userHandler::updateUser)
        .DELETE("/{id}", userHandler::deleteUser)
        .GET("/age/{minAge}", userHandler::getUsersByMinAge)
        .GET("/name/{name}", userHandler::getUsersByName)
        .GET("/startwith/{prefix}", userHandler::getUsersStartingWith)
        .GET("/contains/{keyword}", userHandler::getUsersContainingWith)
        .GET("/check-name/{name}", userHandler::isNameDuplicated)
        .build()
    );
  }
}
