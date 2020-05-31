package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * Mock IEX quote repository.
 *
 * @author Dylan Davies
 *
 */
public class MockIexQuoteRepository implements IIexQuoteRepository {

	private Map<String, Quote> repo;

	public MockIexQuoteRepository(Map<String, Quote> repo) {
		this.repo = repo;
	}

	@Override
	public Quote get(String symbol) {
		return repo.get(symbol);
	}

	@Override
	public Map<String, Quote> getAll(List<String> symbols) {
		return symbols.stream()//
				.map(repo::get)//
				.filter(Objects::nonNull)//
				.collect(Collectors.toMap(Quote::getSymbol, v -> v));
	}

}
