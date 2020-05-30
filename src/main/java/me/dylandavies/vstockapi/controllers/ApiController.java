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

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private IIexService iexService;

	@GetMapping("/quotes")
	public Map<String, Quote> quotes(@RequestParam List<String> symbols) {
		return iexService.getQuotes(symbols);
	}

}
