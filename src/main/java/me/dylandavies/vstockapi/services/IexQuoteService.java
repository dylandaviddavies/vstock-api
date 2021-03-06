package me.dylandavies.vstockapi.services;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import me.dylandavies.vstockapi.utils.QuoteSearchPredicate;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

@Service
public class IexQuoteService implements IIexQuoteService {

	private IIexBatchStocksService iexBatchStocksService;

	@Autowired
	public IexQuoteService(IIexBatchStocksService iexBatchStocksService) {
		this.iexBatchStocksService = iexBatchStocksService;
	}

	private List<Quote> getAll(List<String> symbols, ChartRange chartRange) {
		return iexBatchStocksService.getAll(symbols, chartRange)///
				.stream().map(BatchStocks::getQuote).collect(Collectors.toList());
	}

	@Override
	public List<Quote> getAll(List<String> symbols, String search, ChangeFilter changeFilter, QuoteSort sort,
			SortDirection sortDirection, ChartRange chartRange, Integer limit) {
		Stream<Quote> stream = getAll(symbols, chartRange)//
				.stream()//
				.filter(Optional.ofNullable(search)//
						.filter(s -> !StringUtils.isEmpty(s))//
						.map(s -> (Predicate<Quote>) new QuoteSearchPredicate(s))//
						.orElse(q -> true))
				.filter(Optional.ofNullable(changeFilter)//
						.map(ChangeFilter::getPredicate)//
						.orElse(q -> true));

		if (sort != null && sortDirection != null)
			stream = stream.sorted(sort.getComparator(sortDirection));

		if (limit != null)
			stream = stream.limit(limit);

		return stream.collect(Collectors.toList());
	}

	@Override
	public List<Quote> getTrendingQuotes() {
		return iexBatchStocksService.getTrending().stream().map(BatchStocks::getQuote).collect(Collectors.toList());
	}
}
