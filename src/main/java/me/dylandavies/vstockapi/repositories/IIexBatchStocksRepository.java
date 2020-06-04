package me.dylandavies.vstockapi.repositories;

import java.util.List;

import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

/**
 * Repository for {@link BatchStocks}.
 *
 * @author Dylan Davies
 *
 */
public interface IIexBatchStocksRepository {

	/**
	 * Returns the batch stocks for the provided stock symbols.
	 * @param chartRange TODO
	 * @param symbols The stock symbols to get the batch stocks for.
	 *
	 * @return Batch stocks keyed by their stock symbol.
	 */
	BatchStocks get(String symbol, ChartRange chartRange);

	/**
	 * Returns the batch stocks for the provided stock symbols.
	 *
	 * @param symbols The stock symbols to get the batch stocks for.
	 * @param chartRange TODO
	 * @return Batch stocks keyed by their stock symbol.
	 */
	List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange);

}
