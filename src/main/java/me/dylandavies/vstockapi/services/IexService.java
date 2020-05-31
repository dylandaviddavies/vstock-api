package me.dylandavies.vstockapi.services;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import me.dylandavies.vstockapi.repositories.IIexQuoteRepository;
import pl.zankowski.iextrading4j.api.stocks.Quote;

@Service
public class IexService implements IIexService {

	private IIexQuoteRepository iexQuoteRepository;

	@Autowired
	public IexService(IIexQuoteRepository iexQuoteRepository) {
		this.iexQuoteRepository = iexQuoteRepository;
	}

	@Override
	public Map<String, Quote> getQuotes(@NonNull List<String> symbols, ChangeFilter changeFilter, QuoteSort sort,
			SortDirection sortDirection) {
		List<String> uppercasedSymbols = symbols.stream()//
				.map(String::toUpperCase)//
				.collect(Collectors.toList());
		Stream<Quote> stream = iexQuoteRepository.getAll(uppercasedSymbols)//
				.values()//
				.stream()//
				.filter(Optional.ofNullable(changeFilter)//
						.map(ChangeFilter::getPredicate)//
						.orElse(q -> true));
		if (sort != null && sortDirection != null)
			stream = stream.sorted(sort.getComparator(sortDirection));
		return stream.collect(Collectors.toMap(Quote::getSymbol, q -> q));
	}

}
