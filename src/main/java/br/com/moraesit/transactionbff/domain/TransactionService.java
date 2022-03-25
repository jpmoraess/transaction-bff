package br.com.moraesit.transactionbff.domain;

import br.com.moraesit.transactionbff.dto.RequestTransactionDto;
import br.com.moraesit.transactionbff.dto.TransactionDto;
import br.com.moraesit.transactionbff.redis.TransactionRedisRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRedisRepository transactionRedisRepository;

    public TransactionService(TransactionRedisRepository transactionRedisRepository) {
        this.transactionRedisRepository = transactionRedisRepository;
    }

    @Transactional
    public Optional<TransactionDto> save(final RequestTransactionDto requestTransactionDto) {
        requestTransactionDto.setData(LocalDateTime.now());
        return Optional.of(transactionRedisRepository.save(requestTransactionDto));
    }

    public Optional<TransactionDto> findById(final String id) {
        return transactionRedisRepository.findById(id);
    }
}
