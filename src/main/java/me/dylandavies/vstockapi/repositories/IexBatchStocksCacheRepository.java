package me.dylandavies.vstockapi.repositories;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

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

	private ConcurrentHashMap<String, Short> symbolsByRequestCount = new ConcurrentHashMap<>();

	private LoadingCache<String, BatchStocks> weeklyCache;

	@Autowired
	public IexBatchStocksCacheRepository(IIexBatchStocksRepository iexBatchStocksDataRepository) {
		this.iexBatchStocksDataRepository = iexBatchStocksDataRepository;
	}

	@Override
	public BatchStocks get(String symbol, ChartRange chartRange) throws Exception {
		return withCache(chartRange, c -> {
			try {
				BatchStocks stocks = c.get(symbol);
				recordSymbolRequest(symbol);
				return stocks;
			} catch (ExecutionException e) {
				return null;
			}
		});
	}

	@Override
	public List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange) throws Exception {
		return withCache(chartRange, c -> {
			try {
				ArrayList<BatchStocks> stocks = new ArrayList<>(c.getAll(symbols).values());
				symbols.forEach(this::recordSymbolRequest);
				return stocks;
			} catch (ExecutionException e) {
				return new ArrayList<>(c.getAllPresent(symbols).values());
			}
		});
	}

	@Override
	public List<BatchStocks> getTrending() throws Exception {
		return dailyCache.asMap().keySet().stream()//
				.filter(symbolsByRequestCount::containsKey)
				.sorted((f, s) -> symbolsByRequestCount.get(s).compareTo(symbolsByRequestCount.get(f))).limit(8)
				.map(k -> {
					try {
						return dailyCache.get(k);
					} catch (Exception e) {
						return null;
					}
				}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@PostConstruct
	private void postConstruct() {
		dailyCache = CacheBuilder.newBuilder()//
				.maximumSize(1000)//
				.weakValues()//
				.expireAfterWrite(12, TimeUnit.HOURS)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.ONE_DAY));
		weeklyCache = CacheBuilder.newBuilder()//
				.maximumSize(1000)//
				.weakValues()//
				.expireAfterWrite(12, TimeUnit.HOURS)//
				.build(new BatchStocksCacheLoader(iexBatchStocksDataRepository, ChartRange.FIVE_DAYS));

		// refresh symbolsByRequestCount every midnight
		OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
		Executors.newSingleThreadScheduledExecutor()//
				.scheduleWithFixedDelay(//
						() -> symbolsByRequestCount.clear(), //
						Duration.between(now, now.toLocalDate().plusDays(1).atStartOfDay(ZoneOffset.UTC)).toMillis(), //
						TimeUnit.DAYS.toMillis(1), //
						TimeUnit.MILLISECONDS);
	}

	private void recordSymbolRequest(String symbol) {
		symbolsByRequestCount.merge(symbol, (short) 1, (a, b) -> {
			int sum = a + b;
			if (sum > Short.MAX_VALUE)
				return Short.MAX_VALUE;
			return (short) sum;
		});
	}

	public <T> T withCache(ChartRange chartRange, Function<LoadingCache<String, BatchStocks>, T> func) {
		switch (chartRange) {
		case ONE_DAY:
			return func.apply(dailyCache);
		case FIVE_DAYS:
			return func.apply(weeklyCache);
		default:
			throw new IllegalArgumentException("Unsupported chartRange: " + chartRange);
		}
	}
}
