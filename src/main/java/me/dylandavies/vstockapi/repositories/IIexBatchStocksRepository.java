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
	 *
	 * @param chartRange
	 * @param symbols    The stock symbols to get the batch stocks for.
	 *
	 * @return Batch stocks keyed by their stock symbol.
	 * @throws Exception
	 */
	BatchStocks get(String symbol, ChartRange chartRange) throws Exception;

	/**
	 * Returns the batch stocks for the provided stock symbols.
	 *
	 * @param symbols    The stock symbols to get the batch stocks for.
	 * @param chartRange
	 * @return Batch stocks keyed by their stock symbol.
	 * @throws Exception
	 */
	List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange) throws Exception;

}
