package me.dylandavies.vstockapi.services;

import java.util.List;
import java.util.Map;

import lombok.NonNull;
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
	 * @param symbols Stock symbols
	 * @return Quotes keyed by their stock symbol.
	 */
	Map<String, Quote> getQuotes(@NonNull List<String> symbols);

}
