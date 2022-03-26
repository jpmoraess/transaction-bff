package br.com.moraesit.transactionbff.domain;

import br.com.moraesit.transactionbff.feign.LimiteClient;
import br.com.moraesit.transactionbff.feign.dto.LimiteDiario;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.decorators.Decorators;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Supplier;

@Service
public class LimiteService {

    private final LimiteClient limiteClient;

    private final CircuitBreaker countCircuitBreaker;

    public LimiteService(LimiteClient limiteClient, CircuitBreaker countCircuitBreaker) {
        this.limiteClient = limiteClient;
        this.countCircuitBreaker = countCircuitBreaker;
    }

    public LimiteDiario buscarLimiteDiario(Long agencia, Long conta) {
        final var limiteDiario = buscarLimiteDiarioFallback(agencia, conta);
        return limiteDiario.get();
    }

    private Supplier<LimiteDiario> buscarLimiteDiarioFallback(Long agencia, Long conta) {
        final var limiteDiarioSupplier = countCircuitBreaker
                .decorateSupplier(() -> limiteClient.buscarLimiteDiario(agencia, conta));
        return Decorators
                .ofSupplier(limiteDiarioSupplier)
                .withCircuitBreaker(countCircuitBreaker)
                .withFallback(List.of(CallNotPermittedException.class), ex -> getStaticLimit(agencia, conta))
                .decorate();
    }

    private LimiteDiario getStaticLimit(Long agencia, Long conta) {
        return LimiteDiario.builder()
                .agencia(agencia)
                .conta(conta)
                .valor(BigDecimal.ZERO)
                .build();
    }
}
