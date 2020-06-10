package me.dylandavies.vstockapi.iex;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.client.rest.manager.RestRequest;
import pl.zankowski.iextrading4j.client.rest.request.stocks.v1.BatchStocksRequestBuilder;

public class IexBatchStocksRequestBuilder extends BatchStocksRequestBuilder {

	private final Set<String> filters = new HashSet<>();

	@Override
	public RestRequest<BatchStocks> build() {
		processFilters();
		return super.build();
	}

	private void processFilters() {
		queryParameters.put("filter", filters.stream().collect(Collectors.joining(",")));
	}

	public IexBatchStocksRequestBuilder withFilter(String filter) {
		filters.add(filter);
		return this;
	}

}
