package br.com.moraesit.transactionbff.redis;

import br.com.moraesit.transactionbff.dto.TransactionDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRedisRepository extends CrudRepository<TransactionDto, String> {
}
