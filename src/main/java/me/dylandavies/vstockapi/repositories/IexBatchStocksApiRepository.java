package me.dylandavies.vstockapi.repositories;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import me.dylandavies.vstockapi.iex.IexBatchMarketStocksRequestBuilder;
import me.dylandavies.vstockapi.iex.IexBatchStocksRequestBuilder;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksType;

@Repository("iexBatchStocksDataRepository")
public class IexBatchStocksApiRepository implements IIexBatchStocksRepository {

	private static final List<String> FILTERS = Arrays.asList("datetime", "image", "quote", "change", "latestPrice",
			"primaryExchange", "symbol", "source", "summary", "headline", "related", "url", "companyName", "minute",
			"date", "chart", "news", "close");

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
	public BatchStocks get(String key, ChartRange chartRange) throws Exception {
		IEXCloudClient client = createClient();
		return client.executeRequest(new IexBatchStocksRequestBuilder()//
				.withFilters(FILTERS)//
				.withSymbol(key)//
				.addType(BatchStocksType.QUOTE)//
				.addType(BatchStocksType.CHART)//
				.addType(BatchStocksType.NEWS)//
				.withChartRange(chartRange)//
				.build());
	}

	@Override
	public List<BatchStocks> getAll(List<String> keys, ChartRange chartRange) throws Exception {
		IEXCloudClient client = createClient();
		Map<String, BatchStocks> stocks = client.executeRequest(new IexBatchMarketStocksRequestBuilder()//
				.withFilters(FILTERS)//
				.withSymbols(keys)//
				.addType(BatchStocksType.QUOTE)//
				.addType(BatchStocksType.CHART)//
				.addType(BatchStocksType.NEWS)//
				.withChartRange(chartRange)//
				.build());
		return new ArrayList<>(stocks.values());
	}

	@Override
	public List<BatchStocks> getTrending() throws Exception {
		throw new UnsupportedOperationException();
	}
}
