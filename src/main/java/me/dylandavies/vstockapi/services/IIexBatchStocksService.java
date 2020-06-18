package me.dylandavies.vstockapi.services;

import java.util.List;

import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

/**
 * Service for handling IEX data.
 *
 * @author Dylan Davies
 *
 */
public interface IIexBatchStocksService {

	BatchStocks get(String symbol, ChartRange chartRange);

	List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange);

	List<BatchStocks> getTrending();

}
