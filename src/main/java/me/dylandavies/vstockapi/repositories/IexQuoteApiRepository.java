package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

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

@Repository("iexQuoteDataRepository")
public class IexQuoteApiRepository implements IIexQuoteRepository {

	@Value("${iex.api.publishable}")
	private String publishableToken;

	@Value("${iex.api.secret}")
	private String secretToken;

	private IEXCloudClient createClient() {
		return IEXTradingClient.create(IEXTradingApiVersion.IEX_CLOUD_V1, new IEXCloudTokenBuilder()//
				.withPublishableToken(publishableToken)//
				.withSecretToken(secretToken)//
				.build());
	}

	@Override
	public Quote get(@NonNull String key) {
		IEXCloudClient client = createClient();
		return client.executeRequest(new QuoteRequestBuilder()//
				.withSymbol(key.toUpperCase())//
				.build());
	}

	@Override
	public Map<String, Quote> getAll(@NonNull List<String> keys) {
		IEXCloudClient client = createClient();
		Map<String, BatchStocks> stocks = client.executeRequest(new BatchMarketStocksRequestBuilder()//
				.withSymbols(Lists.newArrayList(keys))//
				.addType(BatchStocksType.QUOTE)//
				.build());
		return stocks.entrySet().stream().collect(Collectors.toMap(Entry::getKey, v -> v.getValue().getQuote()));
	}

}
