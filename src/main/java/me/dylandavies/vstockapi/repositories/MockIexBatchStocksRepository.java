package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

/**
 * Mock IEX quote repository.
 *
 * @author Dylan Davies
 *
 */
public class MockIexBatchStocksRepository implements IIexBatchStocksRepository {

	private Map<String, BatchStocks> repo;

	public MockIexBatchStocksRepository(Map<String, BatchStocks> repo) {
		this.repo = repo;
	}

	@Override
	public BatchStocks get(String symbol) {
		return repo.get(symbol);
	}

	@Override
	public List<BatchStocks> getAll(List<String> symbols) {
		return symbols.stream()//
				.map(repo::get)//
				.filter(Objects::nonNull)//
				.collect(Collectors.toList());
	}

}
