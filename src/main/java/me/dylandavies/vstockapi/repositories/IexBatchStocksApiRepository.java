package me.dylandavies.vstockapi.repositories;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.client.IEXCloudClient;
import pl.zankowski.iextrading4j.client.IEXCloudTokenBuilder;
import pl.zankowski.iextrading4j.client.IEXTradingApiVersion;
import pl.zankowski.iextrading4j.client.IEXTradingClient;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchMarketStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksRequestBuilder;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksType;

@Repository("iexBatchStocksDataRepository")
public class IexBatchStocksApiRepository implements IIexBatchStocksRepository {

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
		return client.executeRequest(new BatchStocksRequestBuilder()//
				.withSymbol(key)//
				.addType(BatchStocksType.QUOTE)//
				.addType(BatchStocksType.CHART)//
				.withChartRange(chartRange)//
				.build());
	}

	@Override
	public List<BatchStocks> getAll(List<String> keys, ChartRange chartRange) throws Exception {
		IEXCloudClient client = createClient();
		Map<String, BatchStocks> stocks = client.executeRequest(new BatchMarketStocksRequestBuilder()//
				.withSymbols(keys)//
				.addType(BatchStocksType.QUOTE)//
				.addType(BatchStocksType.CHART)//
				.addType(BatchStocksType.NEWS)//
				.withChartRange(chartRange)//
				.build());
		return new ArrayList<>(stocks.values());
	}
}
