package org.home.forex.repository;

import org.home.forex.entity.QuoteEntity;
import org.springframework.data.repository.CrudRepository;

public interface QuoteRepository extends CrudRepository<QuoteEntity, Long> {
}
