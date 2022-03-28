package br.com.moraesit.transactionbff.domain;

import br.com.moraesit.transactionbff.dto.RequestTransactionDto;
import br.com.moraesit.transactionbff.dto.SituacaoEnum;
import br.com.moraesit.transactionbff.dto.TransactionDto;
import br.com.moraesit.transactionbff.exception.ResourceNotFoundException;
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
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

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
    public Mono<RequestTransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        return Mono.fromCallable(() -> {
                    requestTransactionDto.setData(LocalDateTime.now());
                    requestTransactionDto.naoAnalisada();
                    return (transactionRedisRepository.save(requestTransactionDto));
                }).doOnError(throwable -> {
                    log.error(throwable.getMessage(), throwable);
                    throw new ResourceNotFoundException("Recurso não encontrado");
                })
                .doOnSuccess(requestTransactionDto1 -> {
                    log.info("Transação persistida com sucesso: {}", requestTransactionDto1);
                    reactiveKafkaProducerTemplate.send(topic, requestTransactionDto)
                            .doOnSuccess(senderResult -> log.info(senderResult.toString()))
                            .subscribe();
                })
                .doFinally(signalType -> {
                    if (signalType.compareTo(SignalType.ON_COMPLETE) == 0) {
                        log.info("Mensagem enviada para o kafka com sucesso.");
                    }
                });
    }

    public Optional<TransactionDto> findById(final String id) {
        return retryTemplate.execute(ret -> {
            log.info("Consultando Redis!");
            return transactionRedisRepository.findById(id);
        });
    }
}
