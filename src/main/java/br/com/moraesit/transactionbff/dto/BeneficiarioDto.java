package br.com.moraesit.transactionbff.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class BeneficiarioDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "CPF do beneficiário")
    @NotNull(message = "Informar o CPF")
    private Long CPF;

    @Schema(description = "Código do banco destino")
    @NotNull(message = "Informar o código do banco de destino")
    private Long codigoBanco;

    @Schema(description = "Agência de destino")
    @NotNull(message = "Informar a agência de destino")
    private String agencia;

    @Schema(description = "Conta de destino")
    @NotNull(message = "Informar a conta de destino")
    private String conta;

    @Schema(description = "Nome do favorecido")
    @NotNull(message = "Informar o nome do favorecido")
    private String nomeFavorecido;
}
