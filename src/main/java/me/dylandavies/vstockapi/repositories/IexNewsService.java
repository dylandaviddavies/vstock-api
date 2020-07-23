package me.dylandavies.vstockapi.repositories;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.dylandavies.vstockapi.services.IIexBatchStocksService;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.v1.News;

@Service
public class IexNewsService implements IIexNewsService {

	private IIexBatchStocksService iexBatchStocksService;

	@Autowired
	public IexNewsService(IIexBatchStocksService iexBatchStocksService) {
		this.iexBatchStocksService = iexBatchStocksService;
	}

	@Override
	public List<News> getAll(List<String> symbols, ChartRange chartRange, Integer limit) {
		List<News> list = iexBatchStocksService.getAll(symbols, chartRange).stream().map(BatchStocks::getNews)
				.flatMap(List::stream).collect(Collectors.toList());

		Collections.shuffle(list);

		if (limit != null)
			list = list.stream().limit(limit).collect(Collectors.toList());

		return list;
	}

}
