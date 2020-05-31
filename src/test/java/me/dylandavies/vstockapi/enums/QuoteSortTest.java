package me.dylandavies.vstockapi.enums;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pl.zankowski.iextrading4j.api.stocks.Quote;

class QuoteSortTest {
	private static final Quote HIGH_CHANGE_QUOTE = createMockQuoteWithChange(BigDecimal.TEN);
	private static final Quote HIGH_COMPANY_NAME_QUOTE = createMockQuoteWithCompanyName("Zenimax");
	private static final Quote HIGH_LATEST_PRICE_QUOTE = createMockQuoteWithLatestPrice(BigDecimal.TEN);

	private static final Quote LOW_CHANGE_QUOTE = createMockQuoteWithChange(BigDecimal.ONE);
	private static final Quote LOW_COMPANY_NAME_QUOTE = createMockQuoteWithCompanyName("Alphabet");
	private static final Quote LOW_LATEST_PRICE_QUOTE = createMockQuoteWithLatestPrice(BigDecimal.ONE);

	private static Quote createMockQuoteWithChange(BigDecimal change) {
		return new Quote(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, change, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	private static Quote createMockQuoteWithCompanyName(String companyName) {
		return new Quote(null, companyName, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	private static Quote createMockQuoteWithLatestPrice(BigDecimal latestPrice) {
		return new Quote(null, null, null, null, null, null, null, null, null, null, null, null, latestPrice, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	@Test
	void sortByChangeAsc() {
		List<Quote> expected = Arrays.asList(LOW_CHANGE_QUOTE, HIGH_CHANGE_QUOTE);

		List<Quote> results = Arrays.asList(HIGH_CHANGE_QUOTE, LOW_CHANGE_QUOTE)//
				.stream()//
				.sorted(QuoteSort.CHANGE.getComparator(SortDirection.ASC))//
				.collect(Collectors.toList());

		Assertions.assertEquals(expected, results);
	}

	@Test
	void sortByChangeDesc() {
		List<Quote> expected = Arrays.asList(HIGH_CHANGE_QUOTE, LOW_CHANGE_QUOTE);

		List<Quote> results = Arrays.asList(LOW_CHANGE_QUOTE, HIGH_CHANGE_QUOTE)//
				.stream()//
				.sorted(QuoteSort.CHANGE.getComparator(SortDirection.DESC))//
				.collect(Collectors.toList());

		Assertions.assertEquals(expected, results);
	}

	@Test
	void sortByCompanyNameAsc() {
		List<Quote> expected = Arrays.asList(LOW_COMPANY_NAME_QUOTE, HIGH_COMPANY_NAME_QUOTE);

		List<Quote> results = Arrays.asList(HIGH_COMPANY_NAME_QUOTE, LOW_COMPANY_NAME_QUOTE)//
				.stream()//
				.sorted(QuoteSort.COMPANY_NAME.getComparator(SortDirection.ASC))//
				.collect(Collectors.toList());

		Assertions.assertEquals(expected, results);
	}

	@Test
	void sortByCompanyNameDesc() {
		List<Quote> expected = Arrays.asList(HIGH_COMPANY_NAME_QUOTE, LOW_COMPANY_NAME_QUOTE);

		List<Quote> results = Arrays.asList(LOW_COMPANY_NAME_QUOTE, HIGH_COMPANY_NAME_QUOTE)//
				.stream()//
				.sorted(QuoteSort.COMPANY_NAME.getComparator(SortDirection.DESC))//
				.collect(Collectors.toList());

		Assertions.assertEquals(expected, results);
	}

	@Test
	void sortByLatestPriceAsc() {
		List<Quote> expected = Arrays.asList(LOW_LATEST_PRICE_QUOTE, HIGH_LATEST_PRICE_QUOTE);

		List<Quote> results = Arrays.asList(HIGH_LATEST_PRICE_QUOTE, LOW_LATEST_PRICE_QUOTE)//
				.stream()//
				.sorted(QuoteSort.LATEST_PRICE.getComparator(SortDirection.ASC))//
				.collect(Collectors.toList());

		Assertions.assertEquals(expected, results);
	}

	@Test
	void sortByLatestPriceDesc() {
		List<Quote> expected = Arrays.asList(HIGH_LATEST_PRICE_QUOTE, LOW_LATEST_PRICE_QUOTE);

		List<Quote> results = Arrays.asList(LOW_LATEST_PRICE_QUOTE, HIGH_LATEST_PRICE_QUOTE)//
				.stream()//
				.sorted(QuoteSort.LATEST_PRICE.getComparator(SortDirection.DESC))//
				.collect(Collectors.toList());

		Assertions.assertEquals(expected, results);
	}

}
