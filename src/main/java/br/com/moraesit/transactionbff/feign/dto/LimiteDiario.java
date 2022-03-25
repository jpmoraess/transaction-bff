package br.com.moraesit.transactionbff.feign.dto;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class LimiteDiario {

    private Long id;
    private Long agencia;
    private Long conta;
    private BigDecimal valor;
}
