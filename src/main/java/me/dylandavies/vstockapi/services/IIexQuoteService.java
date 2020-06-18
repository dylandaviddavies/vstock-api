package me.dylandavies.vstockapi.services;

import java.util.List;

import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.Quote;

public interface IIexQuoteService {

	List<Quote> getAll(List<String> symbols, String search, ChangeFilter changeFilter, QuoteSort sort,
			SortDirection sortDirection, ChartRange chartRange, Integer limit);

	List<Quote> getTrending();

}
