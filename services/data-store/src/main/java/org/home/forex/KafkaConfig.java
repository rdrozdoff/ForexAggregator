package org.home.forex;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Bean
    public NewTopic inputTopic(@Value("${store.topic.input}") String inputTopic) {
        return new NewTopic(inputTopic, 1, (short) 1);
    }
}
