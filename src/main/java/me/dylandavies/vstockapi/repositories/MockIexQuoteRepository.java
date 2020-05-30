package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.zankowski.iextrading4j.api.stocks.Quote;

@RequiredArgsConstructor
public class MockIexQuoteRepository implements IIexQuoteRepository {

	private @NonNull Map<String, Quote> repo;

	@Override
	public Map<String, Quote> getAll(List<String> symbols) {
		return symbols.stream()//
				.map(repo::get)//
				.collect(Collectors.toMap(Quote::getSymbol, v -> v));
	}

}
