package me.dylandavies.vstockapi.controllers;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import me.dylandavies.vstockapi.components.IexService;
import pl.zankowski.iextrading4j.api.stocks.Quote;

@RestController
@RequestMapping("/api")
public class ApiController {

	@Autowired
	private IexService iexService;

	@GetMapping("/get")
	public Map<String, Quote> get() {
		return iexService.getQuotes(Arrays.asList("aapl", "tsla", "fb"));
	}

}
