package io.incetutku.kafka.tutorial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.incetutku.kafka.tutorial.config.KafkaConfigProps;
import io.incetutku.kafka.tutorial.domain.CustomerVisitEvent;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootApplication
public class KafkaTutorialApplication {

    private final ObjectMapper objectMapper;

    public KafkaTutorialApplication(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public static void main(String[] args) {
        SpringApplication.run(KafkaTutorialApplication.class, args);
    }

    @Bean
    public ApplicationRunner runner(KafkaTemplate<String, String> kafkaTemplate, KafkaConfigProps kafkaConfigProps) throws JsonProcessingException {
        CustomerVisitEvent event = CustomerVisitEvent.builder()
                .customerId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        String payload = objectMapper.writeValueAsString(event);

        return args -> {
            kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
        };
    }

    @KafkaListener(topics = "customer.visit")
    public String listens(String in) {
        System.out.println();
        return in;
    }
}
