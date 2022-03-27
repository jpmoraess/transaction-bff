package br.com.moraesit.transactionbff.domain;

import br.com.moraesit.transactionbff.dto.RequestTransactionDto;
import br.com.moraesit.transactionbff.dto.TransactionDto;
import br.com.moraesit.transactionbff.redis.TransactionRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRedisRepository transactionRedisRepository;
    private final RetryTemplate retryTemplate;
    private final ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate;

    @Value("${app.topic}")
    private String topic;

    public TransactionService(TransactionRedisRepository transactionRedisRepository, RetryTemplate retryTemplate,
                              ReactiveKafkaProducerTemplate<String, RequestTransactionDto> reactiveKafkaProducerTemplate) {
        this.transactionRedisRepository = transactionRedisRepository;
        this.retryTemplate = retryTemplate;
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
    }

    @Transactional
    @Retryable(value = QueryTimeoutException.class, maxAttempts = 5, backoff = @Backoff(delay = 100))
    public Optional<TransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        requestTransactionDto.setData(LocalDateTime.now());

        reactiveKafkaProducerTemplate.send(topic, requestTransactionDto)
                .doOnSuccess(senderResult -> log.info(senderResult.toString()))
                .subscribe();

        return Optional.of(transactionRedisRepository.save(requestTransactionDto));
    }

    public Optional<TransactionDto> findById(final String id) {
        return retryTemplate.execute(ret -> {
            log.info("Consultando Redis!");
            return transactionRedisRepository.findById(id);
        });
    }
}
