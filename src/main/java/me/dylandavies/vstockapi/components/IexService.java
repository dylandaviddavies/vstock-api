package me.dylandavies.vstockapi.components;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.dylandavies.vstockapi.repositories.IIexQuoteRepository;
import pl.zankowski.iextrading4j.api.stocks.Quote;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IexService implements IIexService {

	private @NonNull IIexQuoteRepository iexQuoteRepository;

	@Override
	public Map<String, Quote> getQuotes(@NonNull List<String> symbols) {
		List<String> uppercasedSymbols = symbols.stream()//
				.map(String::toUpperCase)//
				.collect(Collectors.toList());
		return iexQuoteRepository.getAll(uppercasedSymbols);
	}

}
