package br.com.moraesit.transactionbff.feign;

import br.com.moraesit.transactionbff.dto.TransactionDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "transaction", url = "${transaction.url}")
public interface TransactionClient {

    @GetMapping("/transaction/{agencia}/{conta}")
    List<TransactionDto> buscarTransacoes(@PathVariable final Long agencia, @PathVariable final Long conta);
}
