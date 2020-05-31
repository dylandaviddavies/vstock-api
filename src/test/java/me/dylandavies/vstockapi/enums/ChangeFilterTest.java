package me.dylandavies.vstockapi.enums;

import java.math.BigDecimal;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import pl.zankowski.iextrading4j.api.stocks.Quote;

class ChangeFilterTest {

	private static final Quote NEGATIVE_QUOTE = createMockQuote(new BigDecimal(-1));
	private static final Quote POSITIVE_QUOTE = createMockQuote(BigDecimal.ONE);
	private static final Quote ZERO_QUOTE = createMockQuote(BigDecimal.ZERO);

	private static Quote createMockQuote(BigDecimal change) {
		return new Quote(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, change, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null);
	}

	@Test
	void negativeFilterOnNegativeQuote() {
		Assertions.assertTrue(ChangeFilter.NEGATIVE.getPredicate().test(NEGATIVE_QUOTE));
	}

	@Test
	void negativeFilterOnPositiveQuote() {
		Assertions.assertFalse(ChangeFilter.NEGATIVE.getPredicate().test(POSITIVE_QUOTE));
	}

	@Test
	void negativeFilterOnZeroQuote() {
		Assertions.assertFalse(ChangeFilter.NEGATIVE.getPredicate().test(ZERO_QUOTE));
	}

	@Test
	void noneFilterOnNegativeQuote() {
		Assertions.assertFalse(ChangeFilter.NONE.getPredicate().test(NEGATIVE_QUOTE));
	}

	@Test
	void noneFilterOnPositiveQuote() {
		Assertions.assertFalse(ChangeFilter.NONE.getPredicate().test(POSITIVE_QUOTE));
	}

	@Test
	void noneFilterOnZeroQuote() {
		Assertions.assertTrue(ChangeFilter.NONE.getPredicate().test(ZERO_QUOTE));
	}

	@Test
	void positiveFilterOnNegativeQuote() {
		Assertions.assertFalse(ChangeFilter.NEGATIVE.getPredicate().test(POSITIVE_QUOTE));
	}

	@Test
	void positiveFilterOnPositiveQuote() {
		Assertions.assertTrue(ChangeFilter.POSITIVE.getPredicate().test(POSITIVE_QUOTE));
	}

	@Test
	void positiveFilterOnZeroQuote() {
		Assertions.assertFalse(ChangeFilter.NEGATIVE.getPredicate().test(ZERO_QUOTE));
	}
}
