package me.dylandavies.vstockapi.controllers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ceau.chart.LineChart;
import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import be.ceau.chart.options.elements.Fill;
import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import me.dylandavies.vstockapi.repositories.IIexNewsService;
import me.dylandavies.vstockapi.services.IIexBatchStocksService;
import me.dylandavies.vstockapi.services.IIexQuoteService;
import me.dylandavies.vstockapi.utils.TrendingData;
import pl.zankowski.iextrading4j.api.stocks.Chart;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;
import pl.zankowski.iextrading4j.api.stocks.v1.News;

/**
 * Controller for vstock API v1.
 *
 * @author Dylan Davies
 *
 */
@RestController
@Validated
@RequestMapping("/api/v1")
public class V1ApiController {

	@Autowired
	private IIexBatchStocksService iexBatchStocksService;

	@Autowired
	private IIexNewsService iexNewsService;

	@Autowired
	private IIexQuoteService iexQuoteService;

	@GetMapping("/myStocksLineChart")
	public String myStocksLineChart(@Valid @Size(min = 1, max = 8) @RequestParam List<String> symbols,
			@RequestParam(required = false, defaultValue = "ONE_DAY") ChartRange chartRange) {
		List<BatchStocks> stocks = iexBatchStocksService.getAll(symbols, chartRange);
		LineData data = new LineData();
		Set<String> labels = new LinkedHashSet<>();
		for (BatchStocks stock : stocks) {
			LineDataset dataset = new LineDataset();
			Quote quote = stock.getQuote();
			dataset.setBorderColor(Color.random());
			dataset.setFill(new Fill<>(false));
			dataset.setLabel(quote.getCompanyName());
			for (Chart chart : stock.getChart()) {
				String date = chart.getDate();
				switch (chartRange) {
				case ONE_DAY:
					String time = chart.getMinute();
					if (time == null)
						continue;
					labels.add(LocalDateTime.parse(date + "T" + time, DateTimeFormatter.ISO_DATE_TIME)
							.format(DateTimeFormatter.ISO_DATE_TIME));
					break;
				default:
					if (date == null)
						continue;
					labels.add(date);
					break;
				}
				dataset.addData(chart.getClose());
			}
			data.addDataset(dataset);
		}
		data.setLabels(labels);
		return new LineChart(data).toJson();
	}

	@GetMapping("/news")
	public List<News> news(@RequestParam List<String> symbols,
			@RequestParam(required = false, defaultValue = "ONE_DAY") ChartRange chartRange,
			@RequestParam(required = false) Integer limit) {
		return iexNewsService.getAll(symbols, chartRange, limit);
	}

	@GetMapping("/quotes")
	public List<Quote> quotes(@Valid @Size(min = 1, max = 8) @RequestParam List<String> symbols,
			@RequestParam(required = false) String search, @RequestParam(required = false) ChangeFilter changeFilter,
			@RequestParam(required = false, defaultValue = "CHANGE") QuoteSort sort,
			@RequestParam(required = false, defaultValue = "DESC") SortDirection sortDirection,
			@RequestParam(required = false, defaultValue = "ONE_DAY") ChartRange chartRange,
			@RequestParam(required = false) Integer limit) {
		return iexQuoteService.getAll(symbols, search, changeFilter, sort, sortDirection, chartRange, limit);
	}

	@GetMapping("/stock/{symbol}")
	public BatchStocks stock(@PathVariable String symbol,
			@RequestParam(defaultValue = "ONE_DAY") ChartRange chartRange) {
		return iexBatchStocksService.get(symbol, chartRange);
	}

	@GetMapping("/trending/{symbol}")
	public TrendingData trending(@PathVariable String symbol,
			@RequestParam(defaultValue = "ONE_DAY") ChartRange chartRange) {
		return iexBatchStocksService.getTrendingData(symbol, chartRange);
	}

	@GetMapping("/trendingQuotes")
	public List<Quote> trendingQuotes() {
		return iexQuoteService.getTrendingQuotes();
	}
}
