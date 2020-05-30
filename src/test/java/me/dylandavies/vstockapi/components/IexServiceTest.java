package me.dylandavies.vstockapi.components;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import me.dylandavies.vstockapi.repositories.MockIexQuoteRepository;
import pl.zankowski.iextrading4j.api.stocks.Quote;

class IexServiceTest {

	private static final String APPLE_SYMBOL = "AAPL";

	private static final String FACEBOOK_SYMBOL = "FB";

	private static final String PARADOX_SYMBOL = "PRXXF";

	private static final String TESLA_SYMBOL = "TSLA";

	private IexService service;

	private Quote createMockQuote(String symbol) {
		return new Quote(symbol, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	@Test
	void getMultipleQuotes() {
		Map<String, Quote> expected = Map.of(APPLE_SYMBOL, createMockQuote(APPLE_SYMBOL), TESLA_SYMBOL,
				createMockQuote(TESLA_SYMBOL));

		Map<String, Quote> results = service.getQuotes(Arrays.asList(APPLE_SYMBOL, TESLA_SYMBOL));

		Assertions.assertEquals(results, expected);
	}

	@Test
	void getOneQuote() {
		Map<String, Quote> expected = Map.of(APPLE_SYMBOL, createMockQuote(APPLE_SYMBOL));

		Map<String, Quote> results = service.getQuotes(Arrays.asList(APPLE_SYMBOL));

		Assertions.assertEquals(results, expected);
	}

	/**
	 * Symbols are stored as uppercase, but the service should allow lowercase
	 * symbols
	 */
	@Test
	void getQuoteWithLowerCaseSymbol() {
		String lowerCaseSymbol = APPLE_SYMBOL.toLowerCase();

		Map<String, Quote> expected = Map.of(APPLE_SYMBOL, createMockQuote(APPLE_SYMBOL));

		Map<String, Quote> results = service.getQuotes(Arrays.asList(lowerCaseSymbol));

		Assertions.assertEquals(results, expected);
	}

	@BeforeEach
	void setUp() {
		service = new IexService(
				new MockIexQuoteRepository(Arrays.asList(APPLE_SYMBOL, TESLA_SYMBOL, FACEBOOK_SYMBOL, PARADOX_SYMBOL)
						.stream().collect(Collectors.toMap(k -> k, this::createMockQuote))));
	}

}
