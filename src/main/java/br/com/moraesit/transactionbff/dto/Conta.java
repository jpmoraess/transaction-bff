package br.com.moraesit.transactionbff.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class Conta implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "Código da agência")
    @NotNull(message = "Informar o código da agência.")
    private Long codigoAgencia;

    @Schema(description = "Código da conta")
    @NotNull(message = "Informar o código da conta.")
    private Long codigoConta;
}
