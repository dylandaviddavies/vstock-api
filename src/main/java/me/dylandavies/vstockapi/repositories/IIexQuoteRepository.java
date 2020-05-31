package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;

import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * Repository for {@link Quote}s.
 *
 * @author Dylan Davies
 *
 */
public interface IIexQuoteRepository {

	/**
	 * Returns the quotes for the provided stock symbols.
	 *
	 * @param symbols The stock symbols to get the quotes for.
	 * @return Quotes keyed by their stock symbol.
	 */
	Quote get(String symbol);

	/**
	 * Returns the quotes for the provided stock symbols.
	 *
	 * @param symbols The stock symbols to get the quotes for.
	 * @return Quotes keyed by their stock symbol.
	 */
	Map<String, Quote> getAll(List<String> symbols);

}
