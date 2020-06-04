package me.dylandavies.vstockapi.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

/**
 * IEX quote repository utilizing the IEX API and a cache with a TTL of one
 * hour.
 *
 * @author Dylan Davies
 *
 */
@Repository("iexBatchStocksRepository")
public class IexBatchStocksCacheRepository implements IIexBatchStocksRepository {

	private LoadingCache<String, BatchStocks> cache;

	private IIexBatchStocksRepository iexBatchStocksDataRepository;

	@Autowired
	public IexBatchStocksCacheRepository(IIexBatchStocksRepository iexBatchStocksDataRepository) {
		this.iexBatchStocksDataRepository = iexBatchStocksDataRepository;
	}

	@Override
	public BatchStocks get(String symbol) {
		try {
			return cache.get(symbol);
		} catch (ExecutionException e) {
			return null;
		}
	}

	@Override
	public List<BatchStocks> getAll(List<String> symbols) {
		try {
			return new ArrayList<>(cache.getAll(symbols).values());
		} catch (ExecutionException e) {
			return new ArrayList<>(cache.getAllPresent(symbols).values());
		}
	}

	@PostConstruct
	private void postConstruct() {
		cache = CacheBuilder.newBuilder()//
				.expireAfterWrite(1, TimeUnit.HOURS)//
				.build(new CacheLoader<>() {

					@Override
					public BatchStocks load(String key) throws Exception {
						return iexBatchStocksDataRepository.get(key);
					}

					public Map<String, BatchStocks> loadAll(Iterable<? extends String> keys) throws Exception {
						return iexBatchStocksDataRepository.getAll(Lists.newArrayList(keys)).stream()
								.collect(Collectors.toMap(k -> k.getQuote().getSymbol(), v -> v));
					}

				});
	}

}
