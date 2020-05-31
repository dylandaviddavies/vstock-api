package me.dylandavies.vstockapi.enums;

import java.math.BigDecimal;
import java.util.function.Predicate;

import pl.zankowski.iextrading4j.api.stocks.Quote;

public enum ChangeFilter {
	NEGATIVE {
		@Override
		public Predicate<Quote> getPredicate() {
			return q -> q.getChange().compareTo(BigDecimal.ZERO) < 0;
		}
	},
	NONE {
		@Override
		public Predicate<Quote> getPredicate() {
			return q -> q.getChange().compareTo(BigDecimal.ZERO) == 0;
		}
	},
	POSITIVE {
		@Override
		public Predicate<Quote> getPredicate() {
			return q -> q.getChange().compareTo(BigDecimal.ZERO) > 0;
		}
	};

	public abstract Predicate<Quote> getPredicate();
}
