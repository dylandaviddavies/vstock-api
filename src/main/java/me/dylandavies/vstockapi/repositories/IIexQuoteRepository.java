package me.dylandavies.vstockapi.repositories;

import java.util.List;
import java.util.Map;

import pl.zankowski.iextrading4j.api.stocks.Quote;

public interface IIexQuoteRepository {

	Map<String, Quote> getAll(List<String> symbols);

}
