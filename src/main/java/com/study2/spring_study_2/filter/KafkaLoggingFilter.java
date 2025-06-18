package com.study2.spring_study_2.filter;

import com.study2.spring_study_2.kafka.producer.KafkaLogProducer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Slf4j
@Component
public class KafkaLoggingFilter implements WebFilter {

  private final KafkaLogProducer kafkaLogProducer;

  public KafkaLoggingFilter(KafkaLogProducer kafkaLogProducer) {
    this.kafkaLogProducer = kafkaLogProducer;
  }

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String method = exchange.getRequest().getMethod() != null
        ? exchange.getRequest().getMethod().name()
        : "UNKNOWN";
    String path = exchange.getRequest().getURI().getPath();
    String clientIp = Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-Forwarded-For"))
        .orElseGet(() -> Optional.ofNullable(exchange.getRequest().getRemoteAddress())
            .map(addr -> addr.getAddress().getHostAddress())
            .orElse("unknown"));

    String logMsg = String.format("Request - method: %s, path: %s, clientIp: %s", method, path, clientIp);

    return chain.filter(exchange)
        .doOnSuccess(unused -> {
          int statusCode = exchange.getResponse().getStatusCode() != null ?
              exchange.getResponse().getStatusCode().value() : 0;

          String extendedLogMsg = logMsg + ", status: " + statusCode;
          log.info(extendedLogMsg);
          kafkaLogProducer.sendLog(extendedLogMsg);
        });
  }
}
