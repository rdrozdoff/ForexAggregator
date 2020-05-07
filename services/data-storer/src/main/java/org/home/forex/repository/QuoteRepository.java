package org.home.forex.repository;

import org.home.forex.entity.QuoteEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface QuoteRepository extends PagingAndSortingRepository<QuoteEntity, Long> {
}
