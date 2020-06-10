package me.dylandavies.vstockapi.iex;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.client.rest.manager.RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchMarketStocksRequestBuilder;

public class IexBatchMarketStocksRequestBuilder extends BatchMarketStocksRequestBuilder {

	private final Set<String> filters = new HashSet<>();

	@Override
	public RestRequest<Map<String, BatchStocks>> build() {

		processFilters();
		return super.build();
	}

	private void processFilters() {
		queryParameters.put("filter", filters.stream().collect(Collectors.joining(",")));
	}

	public IexBatchMarketStocksRequestBuilder withFilter(String filter) {
		filters.add(filter);
		return this;
	}

	public IexBatchMarketStocksRequestBuilder withFilters(List<String> filters) {
		this.filters.addAll(filters);
		return this;
	}

}
