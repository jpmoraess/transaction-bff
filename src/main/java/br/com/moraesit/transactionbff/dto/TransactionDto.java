package br.com.moraesit.transactionbff.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@EqualsAndHashCode(of = "uuid")
public class TransactionDto {
    private UUID uuid;
}
