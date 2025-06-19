package com.study2.spring_study_2.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaLogConsumer {

  @KafkaListener(topics = "api-log-topic", groupId = "og-group")
  public void listen(ConsumerRecord<String, String> record) {
    String message = record.value();
    System.out.println("[KafkaConsumer] Consumed message: " + message);
  }
}
