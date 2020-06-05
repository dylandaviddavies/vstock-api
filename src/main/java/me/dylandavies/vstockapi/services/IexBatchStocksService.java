package me.dylandavies.vstockapi.services;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
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
	public List<BatchStocks> getAll(List<String> symbols, ChartRange chartRange) {
		List<String> readiedSymbols = symbols.stream().filter(Objects::nonNull).map(String::toUpperCase)
				.collect(Collectors.toList());

		if (readiedSymbols.isEmpty())
			return Collections.emptyList();

		try {
			return iexBatchStocksRepository.getAll(readiedSymbols, chartRange);
		} catch (Exception e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
	}
}
