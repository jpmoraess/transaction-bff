package br.com.moraesit.transactionbff.api;

import br.com.moraesit.transactionbff.domain.LimiteService;
import br.com.moraesit.transactionbff.feign.dto.LimiteDiario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.function.Supplier;

@RestController
@RequestMapping("/limites")
public class LimiteController {

    private final LimiteService limiteService;

    public LimiteController(LimiteService limiteService) {
        this.limiteService = limiteService;
    }

    @GetMapping("/{agencia}/{conta}")
    public Mono<LimiteDiario> buscarLimiteDiario(@PathVariable Long agencia, @PathVariable Long conta) {
        return limiteService.buscarLimiteDiario(agencia, conta);
    }
}
