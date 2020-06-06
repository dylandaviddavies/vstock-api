package me.dylandavies.vstockapi.enums;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;

import pl.zankowski.iextrading4j.api.stocks.Quote;

public enum QuoteSort {

	CHANGE {
		@Override
		public Comparator<Quote> getComparator(SortDirection sortDirection) {
			return (f, s) -> {
				BigDecimal firstValue = f.getChange();
				if (!f.getLatestPrice().equals(BigDecimal.ZERO))
					firstValue = firstValue.divide(f.getLatestPrice(), 2, RoundingMode.FLOOR);
				BigDecimal secondValue = s.getChange();
				if (!s.getLatestPrice().equals(BigDecimal.ZERO))
					secondValue = secondValue.divide(s.getLatestPrice(), 2, RoundingMode.FLOOR);
				return sortDirection.compare(firstValue, secondValue);
			};
		}
	},

	COMPANY_NAME {
		@Override
		public Comparator<Quote> getComparator(SortDirection sortDirection) {
			return (f, s) -> sortDirection.compare(f.getCompanyName(), s.getCompanyName(), String::compareToIgnoreCase);
		}
	},

	LATEST_PRICE {
		@Override
		public Comparator<Quote> getComparator(SortDirection sortDirection) {
			return (f, s) -> sortDirection.compare(f.getLatestPrice(), s.getLatestPrice());
		}
	};

	public abstract Comparator<Quote> getComparator(SortDirection sortDirection);
}
