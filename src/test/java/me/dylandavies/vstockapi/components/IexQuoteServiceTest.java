package me.dylandavies.vstockapi.components;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.dylandavies.vstockapi.repositories.MockIexBatchStocksRepository;
import me.dylandavies.vstockapi.services.IexBatchStocksService;
import me.dylandavies.vstockapi.services.IexQuoteService;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

class IexQuoteServiceTest {

	private static final String APPLE_SYMBOL = "AAPL";

	private static final String FACEBOOK_SYMBOL = "FB";

	private static final String PARADOX_SYMBOL = "PRXXF";

	private static final String TESLA_SYMBOL = "TSLA";

	private static BatchStocks createMockBatchStocks(String symbol) {
		return new BatchStocks(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				createMockQuote(symbol), null, null, null);
	}

	private static Quote createMockQuote(String symbol) {
		return new Quote(symbol, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	private IexQuoteService service;

	@Test
	void getMultipleQuotes() {
		List<Quote> expected = Arrays.asList(createMockQuote(APPLE_SYMBOL), createMockQuote(TESLA_SYMBOL));

		List<Quote> results = service.getQuotes(Arrays.asList(APPLE_SYMBOL, TESLA_SYMBOL), null, null, null, null, null);

		Assertions.assertEquals(results, expected);
	}

	@Test
	void getNoQuotes() {
		List<Quote> expected = Collections.emptyList();

		List<Quote> results = service.getQuotes(Arrays.asList("fakesymbol"), null, null, null, null, null);

		Assertions.assertEquals(results, expected);
	}

	@Test
	void getOneQuote() {
		List<Quote> expected = Arrays.asList(createMockQuote(APPLE_SYMBOL));

		List<Quote> results = service.getQuotes(Arrays.asList(APPLE_SYMBOL), null, null, null, null, null);

		Assertions.assertEquals(results, expected);
	}

	/**
	 * Symbols are stored as uppercase, but the service should allow lowercase
	 * symbols
	 */
	@Test
	void getQuoteWithLowerCaseSymbol() {
		String lowerCaseSymbol = APPLE_SYMBOL.toLowerCase();

		List<Quote> expected = Arrays.asList(createMockQuote(APPLE_SYMBOL));

		List<Quote> results = service.getQuotes(Arrays.asList(lowerCaseSymbol), null, null, null, null, null);

		Assertions.assertEquals(results, expected);
	}

	@Test
	void getSomeQuotes() {
		List<Quote> expected = Arrays.asList(createMockQuote(APPLE_SYMBOL), createMockQuote(FACEBOOK_SYMBOL));

		List<Quote> results = service.getQuotes(Arrays.asList(APPLE_SYMBOL, FACEBOOK_SYMBOL, "fakesymbol"), null, null,
				null, null, null);

		Assertions.assertEquals(results, expected);
	}

	@BeforeEach
	void setUp() {
		Map<String, BatchStocks> repo = Arrays.asList(APPLE_SYMBOL, TESLA_SYMBOL, FACEBOOK_SYMBOL, PARADOX_SYMBOL)
				.stream().collect(Collectors.toMap(k -> k, IexQuoteServiceTest::createMockBatchStocks));
		service = new IexQuoteService(new IexBatchStocksService(new MockIexBatchStocksRepository(repo)));
	}

}
