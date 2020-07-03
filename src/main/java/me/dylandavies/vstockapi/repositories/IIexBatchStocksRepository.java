package me.dylandavies.vstockapi.repositories;

import java.util.List;

import me.dylandavies.vstockapi.utils.TrendingData;
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

	List<BatchStocks> getTrending() throws Exception;

	TrendingData getTrendingData(String symbol, ChartRange chartRange) throws Exception;

}
