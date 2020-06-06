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

	BatchStocks get(String symbol, ChartRange chartRange) throws Exception;

	List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange) throws Exception;

}
