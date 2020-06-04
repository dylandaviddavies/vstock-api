package me.dylandavies.vstockapi.services;

import java.util.List;

import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

/**
 * Service for handling IEX data.
 *
 * @author Dylan Davies
 *
 */
public interface IIexBatchStocksService {

	List<BatchStocks> getBatchStocks(List<String> symbols);

}
