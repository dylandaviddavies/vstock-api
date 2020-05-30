package me.dylandavies.vstockapi.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import me.dylandavies.vstockapi.components.IIexService;
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
	public Map<String, Quote> quotes(@RequestParam(required = true) List<String> symbols) {
		return iexService.getQuotes(symbols);
	}

}
