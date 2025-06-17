package com.study2.spring_study_2.handler;

import com.study2.spring_study_2.exception.UserNotFoundException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@Order(-2)
public class GlobalErrorWebExceptionHandler implements ErrorWebExceptionHandler {

  @Override
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    String path = exchange.getRequest().getURI().getPath();

    if (path.startsWith("/swagger-ui") || path.startsWith("/v3/api-docs") || path.startsWith("/swagger-resources")) {
      return Mono.error(ex);
    }

    Map<String, Object> errorAttributes = new HashMap<>();
    HttpStatus status;

    if (ex instanceof UserNotFoundException) {
      status = HttpStatus.NOT_FOUND;
      errorAttributes.put("error", "User Not Found");
    }
    else {
      status = HttpStatus.INTERNAL_SERVER_ERROR;
      errorAttributes.put("error", "Internal Server Error");
    }

    errorAttributes.put("message", ex.getMessage());
    errorAttributes.put("status", status.value());

    exchange.getResponse().setStatusCode(status);
    exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

    DataBufferFactory bufferFactory = exchange.getResponse().bufferFactory();
    DataBuffer buffer = bufferFactory.wrap(new org.json.JSONObject(errorAttributes).toString().getBytes());

    return exchange.getResponse().writeWith(Mono.just(buffer));
  }
}
