package me.dylandavies.vstockapi.enums;

import java.util.Comparator;

import pl.zankowski.iextrading4j.api.stocks.Quote;

public enum QuoteSort {

	CHANGE {
		@Override
		public Comparator<Quote> getComparator(SortDirection sortDirection) {
			return (f, s) -> sortDirection.compare(f.getChange(), s.getChange());
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
