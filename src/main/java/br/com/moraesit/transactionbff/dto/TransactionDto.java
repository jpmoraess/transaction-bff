package br.com.moraesit.transactionbff.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@RedisHash(value = "TransactionDto", timeToLive = 300)
@EqualsAndHashCode(of = "uuid")
public class TransactionDto {

    @Id
    @Schema(description = "Código de identificação da transação")
    private UUID uuid;

    @Schema(description = "Valor da transação")
    @NotNull(message = "Informar o valor da transação")
    private BigDecimal valor;

    @Schema(description = "Data, hora, minuto e segundo da transação")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime data;

    @Schema(description = "Conta de origem da transação")
    @NotNull(message = "Informar a conta de origem da transação")
    @Valid
    private Conta conta;

    @Schema(description = "Beneficiário da transação")
    @NotNull(message = "Informar o beneficiário da transação")
    @Valid
    private BeneficiarioDto beneficiario;

    @Schema(description = "Tipo da transação")
    @NotNull(message = "Informar o tipo da transação")
    private TipoTransacao tipoTransacao;

    @Schema(description = "Situação da transação")
    private SituacaoEnum situacao;
}
