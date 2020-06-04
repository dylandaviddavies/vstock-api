package me.dylandavies.vstockapi.cache;

import java.util.Map;
import java.util.stream.Collectors;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;

import me.dylandavies.vstockapi.repositories.IIexBatchStocksRepository;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

public class BatchStocksCacheLoader extends CacheLoader<String, BatchStocks> {
	private final ChartRange chartRange;
	private final IIexBatchStocksRepository iexBatchStocksRepository;

	public BatchStocksCacheLoader(IIexBatchStocksRepository iexBatchStocksRepository, ChartRange chartRange) {
		this.iexBatchStocksRepository = iexBatchStocksRepository;
		this.chartRange = chartRange;
	}

	@Override
	public BatchStocks load(String key) throws Exception {
		return iexBatchStocksRepository.get(key, chartRange);
	}

	@Override
	public Map<String, BatchStocks> loadAll(Iterable<? extends String> keys) throws Exception {
		return iexBatchStocksRepository.getAll(Lists.newArrayList(keys), chartRange).stream()
				.collect(Collectors.toMap(k -> k.getQuote().getSymbol(), v -> v));
	}
}
