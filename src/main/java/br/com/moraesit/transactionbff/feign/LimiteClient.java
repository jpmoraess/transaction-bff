package br.com.moraesit.transactionbff.feign;

import br.com.moraesit.transactionbff.feign.dto.LimiteDiario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "limites", url = "${limites.url}")
public interface LimiteClient {

    @GetMapping(path = "/limite-diario/{agencia}/{conta}")
    LimiteDiario buscarLimiteDiario(@PathVariable final Long agencia, @PathVariable final Long conta);
}
