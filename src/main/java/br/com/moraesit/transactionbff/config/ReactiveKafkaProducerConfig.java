package br.com.moraesit.transactionbff.config;

import br.com.moraesit.transactionbff.dto.RequestTransactionDto;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

@Configuration
public class ReactiveKafkaProducerConfig {

    @Bean
    public ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate(final KafkaProperties kafkaProperties) {
        final var properties = kafkaProperties.buildProducerProperties();
        return new ReactiveKafkaProducerTemplate<>(SenderOptions.create(properties));
    }
}
