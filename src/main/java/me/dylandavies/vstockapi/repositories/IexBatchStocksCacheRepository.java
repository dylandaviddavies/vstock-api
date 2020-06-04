package me.dylandavies.vstockapi.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import me.dylandavies.vstockapi.cache.BatchStocksCacheLoader;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
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

	private LoadingCache<String, BatchStocks> dailyCache;

	private IIexBatchStocksRepository iexBatchStocksDataRepository;

	private LoadingCache<String, BatchStocks> monthlyCache;

	private LoadingCache<String, BatchStocks> weeklyCache;

	@Autowired
	public IexBatchStocksCacheRepository(IIexBatchStocksRepository iexBatchStocksDataRepository) {
		this.iexBatchStocksDataRepository = iexBatchStocksDataRepository;
	}

	@Override
	public BatchStocks get(String symbol, ChartRange chartRange) {
		return withCache(chartRange, c -> {
			try {
				return c.get(symbol);
			} catch (ExecutionException e) {
				return null;
			}
		});
	}

	@Override
	public List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange) {
		return withCache(chartRange, c -> {
			try {
				return new ArrayList<>(c.getAll(symbols).values());
			} catch (ExecutionException e) {
				return new ArrayList<>(c.getAllPresent(symbols).values());
			}
		});
	}

	@PostConstruct
	private void postConstruct() {
		dailyCache = CacheBuilder.newBuilder()//
				.maximumSize(100)//
				.weakValues()//
				.expireAfterWrite(1, TimeUnit.HOURS)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.ONE_DAY));
		weeklyCache = CacheBuilder.newBuilder()//
				.maximumSize(100)//
				.weakValues()//
				.expireAfterWrite(1, TimeUnit.HOURS)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.FIVE_DAYS));
		monthlyCache = CacheBuilder.newBuilder()//
				.maximumSize(100)//
				.weakValues()//
				.expireAfterWrite(1, TimeUnit.HOURS)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.ONE_MONTH));
	}

	public <T> T withCache(ChartRange chartRange, Function<LoadingCache<String, BatchStocks>, T> func) {
		switch (chartRange) {
		case ONE_DAY:
			return func.apply(dailyCache);
		case FIVE_DAYS:
			return func.apply(weeklyCache);
		case ONE_MONTH:
			return func.apply(monthlyCache);
		default:
			throw new IllegalArgumentException("Unsupported chartRange: " + chartRange);
		}
	}

}
