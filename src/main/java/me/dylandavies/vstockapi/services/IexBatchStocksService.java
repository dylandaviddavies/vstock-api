package me.dylandavies.vstockapi.services;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.dylandavies.vstockapi.repositories.IIexBatchStocksRepository;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

@Service
public class IexBatchStocksService implements IIexBatchStocksService {

	private static final Logger LOGGER = Logger.getLogger(IexBatchStocksService.class.getName());

	private IIexBatchStocksRepository iexBatchStocksRepository;

	@Autowired
	public IexBatchStocksService(IIexBatchStocksRepository iexBatchStocksRepository) {
		this.iexBatchStocksRepository = iexBatchStocksRepository;
	}

	@Override
	public BatchStocks get(String symbol, ChartRange chartRange) {
		return Optional.ofNullable(symbol)//
				.map(String::toUpperCase)//
				.map(s -> {
					try {
						return iexBatchStocksRepository.get(s, chartRange);
					} catch (Exception e) {
						LOGGER.log(Level.FINER, "Failed attempt at getting stock: " + s);
						return null;
					}
				})//
				.orElse(null);
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
			LOGGER.log(Level.FINER,
					"Failed attempt at getting stocks: " + symbols.stream().collect(Collectors.joining(", ")));
			return Collections.emptyList();
		}
	}
}
