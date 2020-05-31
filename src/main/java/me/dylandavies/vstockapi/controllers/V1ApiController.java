package me.dylandavies.vstockapi.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import me.dylandavies.vstockapi.services.IIexService;
import pl.zankowski.iextrading4j.api.stocks.Quote;

/**
 * Controller for vstock API v1.
 *
 * @author Dylan Davies
 *
 */
@RestController
@RequestMapping("/api/v1")
public class V1ApiController {

	@Autowired
	private IIexService iexService;

	/**
	 * Returns the quotes for the provided stock symbols.
	 *
	 * @param symbols
	 * @return Quotes keyed by their stock symbol.
	 */
	@GetMapping("/quotes")
	public List<Quote> quotes(@RequestParam List<String> symbols,
			@RequestParam(required = false) ChangeFilter changeFilter,
			@RequestParam(required = false, defaultValue = "CHANGE") QuoteSort sort,
			@RequestParam(required = false, defaultValue = "DESC") SortDirection sortDirection) {
		List<Quote> quotes = new ArrayList<>(iexService.getQuotes(symbols, changeFilter, sort, sortDirection).values());
		return quotes.stream()//
				.sorted(sort.getComparator(sortDirection))//
				.filter(Optional.ofNullable(changeFilter)//
						.map(ChangeFilter::getPredicate)//
						.orElse(q -> true))//
				.collect(Collectors.toList());
	}
}
