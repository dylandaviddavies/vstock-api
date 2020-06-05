package me.dylandavies.vstockapi.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.dylandavies.vstockapi.repositories.IexBatchStocksMockRepository;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

class IexBatchStocksServiceTest {

	private static final String APPLE_SYMBOL = "AAPL";

	private static final String FACEBOOK_SYMBOL = "FB";

	private static final String PARADOX_SYMBOL = "PRXXF";

	private static final String TESLA_SYMBOL = "TSLA";

	private static BatchStocks createMockBatchStocks(String symbol) {
		return new BatchStocks(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null);
	}

	private IexBatchStocksService service;

	@Test
	void getBatchStocksWithLowerCaseSymbol() {
		List<BatchStocks> expected = Arrays.asList(createMockBatchStocks(APPLE_SYMBOL));

		List<BatchStocks> results = service.getAll(Arrays.asList(APPLE_SYMBOL.toLowerCase()), null);

		Assertions.assertEquals(expected, results);
	}

	@BeforeEach
	void setUp() {
		Map<String, BatchStocks> repo = Arrays.asList(APPLE_SYMBOL, TESLA_SYMBOL, FACEBOOK_SYMBOL, PARADOX_SYMBOL)
				.stream().collect(Collectors.toMap(k -> k, IexBatchStocksServiceTest::createMockBatchStocks));
		service = new IexBatchStocksService(new IexBatchStocksMockRepository(repo));
	}

}
