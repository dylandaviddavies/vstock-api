package me.dylandavies.vstockapi.services;

import java.util.List;
import java.util.Map;

import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * Service for handling IEX data.
 *
 * @author Dylan Davies
 *
 */
public interface IIexService {

	/**
	 * Returns the quotes for the provided stock symbols.
	 *
	 * @param symbols       Stock symbols
	 * @param changeFilter  TODO
	 * @param sort          TODO
	 * @param sortDirection TODO
	 * @return Quotes keyed by their stock symbol.
	 */
	Map<String, Quote> getQuotes(List<String> symbols, ChangeFilter changeFilter, QuoteSort sort,
			SortDirection sortDirection);

}
