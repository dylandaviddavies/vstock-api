package me.dylandavies.vstockapi.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ceau.chart.LineChart;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import me.dylandavies.vstockapi.services.IIexBatchStocksService;
import me.dylandavies.vstockapi.services.IIexQuoteService;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.v1.Intraday;

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
	private IIexBatchStocksService iexBatchStocksService;

	@Autowired
	private IIexQuoteService iexQuoteService;

	@GetMapping("/myStocksLineChart")
	public String myStocksLineChart(@RequestParam List<String> symbols) {
		List<BatchStocks> stocks = iexBatchStocksService.getBatchStocks(symbols);
		LineData data = new LineData();
		for (BatchStocks stock : stocks) {
			LineDataset dataset = new LineDataset();
			Quote quote = stock.getQuote();
			dataset.setLabel(quote.getSymbol() + " - " + quote.getCompanyName());
			for (Intraday day : stock.getIntradayPrices()) {
				dataset.addData(day.getClose());
				data.addLabel(day.getDate() + " " + day.getMinute());
			}
			data.addDataset(dataset);
		}
		return new LineChart(data).toJson();
	}

	@GetMapping("/quotes")
	public List<Quote> quotes(@RequestParam List<String> symbols, @RequestParam(required = false) String search,
			@RequestParam(required = false) ChangeFilter changeFilter,
			@RequestParam(required = false, defaultValue = "CHANGE") QuoteSort sort,
			@RequestParam(required = false, defaultValue = "DESC") SortDirection sortDirection) {
		return iexQuoteService.getQuotes(symbols, search, changeFilter, sort, sortDirection);
	}
}
