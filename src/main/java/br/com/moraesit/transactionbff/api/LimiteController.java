package br.com.moraesit.transactionbff.api;

import br.com.moraesit.transactionbff.feign.LimiteClient;
import br.com.moraesit.transactionbff.feign.dto.LimiteDiario;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/limites")
public class LimiteController {

    private final LimiteClient limiteClient;

    public LimiteController(LimiteClient limiteClient) {
        this.limiteClient = limiteClient;
    }

    @GetMapping("/{agencia}/{conta}")
    public LimiteDiario buscarLimiteDiario(@PathVariable Long agencia, @PathVariable Long conta) {
        return limiteClient.buscarLimiteDiario(agencia, conta);
    }
}
