package br.com.moraesit.transactionbff.api;

import br.com.moraesit.transactionbff.domain.TransactionService;
import br.com.moraesit.transactionbff.dto.RequestTransactionDto;
import br.com.moraesit.transactionbff.dto.TransactionDto;
import br.com.moraesit.transactionbff.exception.ResourceNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Operation(description = "API para criar uma transação financeira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Retorno OK da criação da transação."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    @PostMapping
    public Mono<TransactionDto> enviarTransacao(@RequestBody final RequestTransactionDto requestTransactionDto) {
        var save = transactionService.save(requestTransactionDto);
        return save.map(Mono::just).orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada."));
    }

    @Operation(description = "API para buscar uma transação financeira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno OK da busca da transação."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    @Parameters(value = {
            @Parameter(name = "id", in = ParameterIn.PATH)
    })
    @GetMapping("/{id}")
    public Mono<TransactionDto> buscarTransacao(@PathVariable("id") final String uuid) {
        var transacao = transactionService.findById(uuid);
        return transacao.map(Mono::just).orElseThrow(() -> new ResourceNotFoundException("Transação não encontrada."));
    }

    @Operation(description = "API para remover uma transação financeira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Retorno OK da remoção da transação."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    @Parameters(value = {
            @Parameter(name = "id", in = ParameterIn.PATH)
    })
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> removerTransacao(@PathVariable("id") final String uuid) {
        return Mono.empty();
    }

    @Operation(description = "API para autorizar a transação financeira")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Retorno OK da confirmação da transação."),
            @ApiResponse(responseCode = "401", description = "Erro de autenticação."),
            @ApiResponse(responseCode = "403", description = "Erro de autorização."),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado.")
    })
    @Parameters(value = {
            @Parameter(name = "id", in = ParameterIn.PATH)
    })
    @PatchMapping(value = "/{id}/confirmar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<TransactionDto> confirmarTransacao(@PathVariable("id") final String uuid) {
        return Mono.empty();
    }
}
