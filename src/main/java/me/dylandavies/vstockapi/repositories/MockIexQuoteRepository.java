package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * Mock IEX quote repository.
 *
 * @author Dylan Davies
 *
 */
@RequiredArgsConstructor
public class MockIexQuoteRepository implements IIexQuoteRepository {

	private @NonNull Map<String, Quote> repo;

	@Override
	public Map<String, Quote> getAll(List<String> symbols) {
		return symbols.stream()//
				.map(repo::get)//
				.filter(Objects::nonNull)//
				.collect(Collectors.toMap(Quote::getSymbol, v -> v));
	}

}
