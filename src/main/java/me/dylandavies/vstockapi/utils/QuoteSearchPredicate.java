package me.dylandavies.vstockapi.utils;

import java.util.function.Predicate;

import pl.zankowski.iextrading4j.api.stocks.Quote;

public class QuoteSearchPredicate implements Predicate<Quote> {

	private final String str;

	public QuoteSearchPredicate(String str) {
		this.str = str;
	}

	@Override
	public boolean test(Quote q) {
		return q.getSymbol().toUpperCase().contains(str.toUpperCase())
				|| q.getCompanyName().toUpperCase().contains(str.toUpperCase());
	}

}
