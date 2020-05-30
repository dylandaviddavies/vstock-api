package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;

import me.dylandavies.vstockapi.cache.IexQuoteCacheLoader;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * IEX quote repository utilizing the IEX API and a cache with a TTL of one
 * hour.
 *
 * @author Dylan Davies
 *
 */
@Repository
public class IexQuoteApiRepository implements IIexQuoteRepository {

	private LoadingCache<String, Quote> cache;

	@Value("${iex.api.publishable}")
	private String publishableToken;

	@Value("${iex.api.secret}")
	private String secretToken;

	@Override
	public Map<String, Quote> getAll(List<String> symbols) {
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
				.build(new IexQuoteCacheLoader(publishableToken, secretToken));
	}

}
