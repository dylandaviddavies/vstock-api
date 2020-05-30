package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * IEX quote repository utilizing the IEX API and a cache with a TTL of one
 * hour.
 *
 * @author Dylan Davies
 *
 */
@Repository("iexQuoteRepository")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IexQuoteCacheRepository implements IIexQuoteRepository {

	private LoadingCache<String, Quote> cache;

	private @NonNull IIexQuoteRepository iexQuoteDataRepository;

	@Override
	public Quote get(@NonNull String symbol) {
		try {
			return cache.get(symbol);
		} catch (ExecutionException e) {
			return null;
		}
	}

	@Override
	public Map<String, Quote> getAll(@NonNull List<String> symbols) {
		try {
			return cache.getAll(symbols);
		} catch (ExecutionException e) {
			return cache.getAllPresent(symbols);
		}
	}

	@PostConstruct
	private void postConstruct() {
		cache = CacheBuilder.newBuilder()//
				.expireAfterWrite(1, TimeUnit.HOURS)//
				.build(new CacheLoader<>() {

					@Override
					public Quote load(String key) throws Exception {
						return iexQuoteDataRepository.get(key);
					}

					public Map<String, Quote> loadAll(Iterable<? extends String> keys) throws Exception {
						return iexQuoteDataRepository.getAll(Lists.newArrayList(keys));
					}

				});
	}

}
