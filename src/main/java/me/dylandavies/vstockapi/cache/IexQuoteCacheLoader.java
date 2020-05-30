package me.dylandavies.vstockapi.cache;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import com.google.common.cache.CacheLoader;
import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;
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

@AllArgsConstructor
public class IexQuoteCacheLoader extends CacheLoader<String, Quote> {

	private @NonNull String publishableToken;

	private @NonNull String secretToken;

	private IEXCloudClient createClient() {
		return IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1, new IEXCloudTokenBuilder()//
				.withPublishableToken(publishableToken)//
				.withSecretToken(secretToken)//
				.build());
	}

	@Override
	public Quote load(@NonNull String key) throws Exception {
		IEXCloudClient client = createClient();
		return client.executeRequest(new QuoteRequestBuilder()//
				.withSymbol(key.toUpperCase())//
				.build());
	}

	@Override
	public Map<String, Quote> loadAll(@NonNull Iterable<? extends String> keys) throws Exception {
		IEXCloudClient client = createClient();
		Map<String, BatchStocks> stocks = client.executeRequest(new BatchMarketStocksRequestBuilder()//
				.withSymbols(Lists.newArrayList(keys))//
				.addType(BatchStocksType.QUOTE)//
				.build());
		return stocks.entrySet().stream().collect(Collectors.toMap(Entry::getKey, v -> v.getValue().getQuote()));
	}
}
