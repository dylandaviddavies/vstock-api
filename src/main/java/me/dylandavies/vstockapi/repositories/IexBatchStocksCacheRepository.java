package me.dylandavies.vstockapi.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

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

	private LoadingCache<String, BatchStocks> biYearlyCache;

	private LoadingCache<String, BatchStocks> dailyCache;

	private IIexBatchStocksRepository iexBatchStocksDataRepository;

	private LoadingCache<String, BatchStocks> maxCache;

	private LoadingCache<String, BatchStocks> monthlyCache;

	private LoadingCache<String, BatchStocks> weeklyCache;

	private LoadingCache<String, BatchStocks> yearlyCache;

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
		}, () -> iexBatchStocksDataRepository.get(symbol, chartRange));
	}

	@Override
	public List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange) {
		return withCache(chartRange, c -> {
			try {
				return new ArrayList<>(c.getAll(symbols).values());
			} catch (ExecutionException e) {
				return new ArrayList<>(c.getAllPresent(symbols).values());
			}
		}, () -> iexBatchStocksDataRepository.getAll(symbols, chartRange));
	}

	@PostConstruct
	private void postConstruct() {
		dailyCache = CacheBuilder.newBuilder()//
				.maximumSize(100)//
				.weakValues()//
				.expireAfterWrite(30, TimeUnit.MINUTES)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.ONE_DAY));
		weeklyCache = CacheBuilder.newBuilder()//
				.maximumSize(100)//
				.weakValues()//
				.expireAfterWrite(30, TimeUnit.MINUTES)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.FIVE_DAYS));
		monthlyCache = CacheBuilder.newBuilder()//
				.maximumSize(10)//
				.weakValues()//
				.expireAfterWrite(30, TimeUnit.MINUTES)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.ONE_MONTH));
		biYearlyCache = CacheBuilder.newBuilder()//
				.maximumSize(10)//
				.weakValues()//
				.expireAfterWrite(30, TimeUnit.MINUTES)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.SIX_MONTHS));
		yearlyCache = CacheBuilder.newBuilder()//
				.maximumSize(10)//
				.weakValues()//
				.expireAfterWrite(30, TimeUnit.MINUTES)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.ONE_YEAR));
		maxCache = CacheBuilder.newBuilder()//
				.maximumSize(10)//
				.weakValues()//
				.expireAfterWrite(30, TimeUnit.MINUTES)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.MAX));
	}

	public <T> T withCache(ChartRange chartRange, Function<LoadingCache<String, BatchStocks>, T> func,
			Supplier<T> fallback) {
		switch (chartRange) {
		case ONE_DAY:
			return func.apply(dailyCache);
		case FIVE_DAYS:
			return func.apply(weeklyCache);
		case ONE_MONTH:
			return func.apply(monthlyCache);
		case SIX_MONTHS:
			return func.apply(biYearlyCache);
		case ONE_YEAR:
			return func.apply(yearlyCache);
		case MAX:
			return func.apply(maxCache);
		default:
			return fallback.get();
		}
	}

}
