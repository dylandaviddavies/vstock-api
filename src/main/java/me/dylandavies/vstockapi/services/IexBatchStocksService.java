package me.dylandavies.vstockapi.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.dylandavies.vstockapi.repositories.IIexBatchStocksRepository;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

@Service
public class IexBatchStocksService implements IIexBatchStocksService {

	private IIexBatchStocksRepository iexBatchStocksRepository;

	@Autowired
	public IexBatchStocksService(IIexBatchStocksRepository iexBatchStocksRepository) {
		this.iexBatchStocksRepository = iexBatchStocksRepository;
	}

	@Override
	public List<BatchStocks> getBatchStocks(List<String> symbols, ChartRange chartRange) {
		List<String> uppercasedSymbols = symbols.stream()//
				.map(String::toUpperCase)//
				.collect(Collectors.toList());
		return iexBatchStocksRepository.getAll(uppercasedSymbols, chartRange);
	}
}
