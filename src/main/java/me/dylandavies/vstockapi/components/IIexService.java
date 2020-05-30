package me.dylandavies.vstockapi.components;

import java.util.List;
import java.util.Map;

import lombok.NonNull;
import pl.zankowski.iextrading4j.api.stocks.Quote;

public interface IIexService {

	Map<String, Quote> getQuotes(@NonNull List<String> symbols);

}
