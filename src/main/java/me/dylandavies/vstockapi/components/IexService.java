package me.dylandavies.vstockapi.components;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;

import lombok.NonNull;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.QuoteRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchMarketStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksType;

@Component
public class IexService {
	@Value("${iex.api.publishable}")
	private String publishableToken;

	@Value("${iex.api.secret}")
	private String secretToken;

	private LoadingCache<String, Quote> cache;

	@PostConstruct
	private void postConstruct() {
		cache = CacheBuilder.newBuilder()//
				.expireAfterWrite(5, TimeUnit.MINUTES)//
				.build(new IexQuoteCacheLoader(publishableToken, secretToken));
	}

	public Map<String, Quote> getQuotes(@NonNull List<String> symbols) {
		List<String> uppercasedSymbols = symbols.stream().map(String::toUpperCase).collect(Collectors.toList());
		try {
			return cache.getAll(uppercasedSymbols);
		} catch (ExecutionException e) {
			return cache.getAllPresent(uppercasedSymbols);
		}
	}
}
