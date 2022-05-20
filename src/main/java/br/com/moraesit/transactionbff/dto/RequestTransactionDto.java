package br.com.moraesit.transactionbff.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString(of = {"uuid", "situacao", "valor"})
@Schema(description = "Objeto de transporte para o envio de uma promessa de transação")
public class RequestTransactionDto extends TransactionDto {
    @JsonIgnore
    private SituacaoEnum situacao;

    @JsonIgnore
    private LocalDateTime data;
}
