package com.study2.spring_study_2.kafka.producer;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class KafkaLogProducer {

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final String topic = "api-log-topic";

  public void sendLog(String log) {
    kafkaTemplate.send(topic, log);
  }
}
