package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

/**
 * Mock IEX quote repository.
 *
 * @author Dylan Davies
 *
 */
public class IexBatchStocksMockRepository implements IIexBatchStocksRepository {

	private Map<String, BatchStocks> repo;

	public IexBatchStocksMockRepository(Map<String, BatchStocks> repo) {
		this.repo = repo;
	}

	@Override
	public BatchStocks get(String symbol, ChartRange chartRange) throws Exception {
		return repo.get(symbol);
	}

	@Override
	public List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange) throws Exception {
		return symbols.stream()//
				.map(repo::get)//
				.filter(Objects::nonNull)//
				.collect(Collectors.toList());
	}

	@Override
	public List<BatchStocks> getTrending() throws Exception {
		throw new UnsupportedOperationException();
	}

}
