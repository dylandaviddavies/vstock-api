package me.dylandavies.vstockapi.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.ceau.chart.LineChart;
import be.ceau.chart.color.Color;
import be.ceau.chart.data.LineData;
import be.ceau.chart.dataset.LineDataset;
import me.dylandavies.vstockapi.enums.ChangeFilter;
import me.dylandavies.vstockapi.enums.QuoteSort;
import me.dylandavies.vstockapi.enums.SortDirection;
import me.dylandavies.vstockapi.services.IIexBatchStocksService;
import me.dylandavies.vstockapi.services.IIexQuoteService;
import pl.zankowski.iextrading4j.api.stocks.Chart;
import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.Quote;
import pl.zankowski.iextrading4j.api.stocks.v1.BatchStocks;

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
	private IIexQuoteService iexQuoteService;

	@GetMapping("/myStocksLineChart")
	public String myStocksLineChart(@Valid @Size(min = 1, max = 50) @RequestParam List<String> symbols,
			@RequestParam ChartRange chartRange) {
		List<BatchStocks> stocks = iexBatchStocksService.getAll(symbols, chartRange);
		LineData data = new LineData();
		Set<String> labels = new LinkedHashSet<>();
		for (BatchStocks stock : stocks) {
			LineDataset dataset = new LineDataset();
			Quote quote = stock.getQuote();
			dataset.setBackgroundColor(Color.random());
			dataset.setLabel(quote.getCompanyName() + " - " + quote.getSymbol());
			for (Chart chart : stock.getChart()) {
				switch (chartRange) {
				case ONE_DAY:
					String time = chart.getMinute();
					if (time == null)
						continue;
					LocalTime dateTime = LocalTime.parse(time);
					if (dateTime.getMinute() % 15 != 0)
						continue;
					labels.add(dateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)));
					break;
				default:
					if (chart.getDate() == null)
						continue;
					labels.add(LocalDate.parse(chart.getDate())
							.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
					break;
				}
				dataset.addData(chart.getClose());
			}
			data.addDataset(dataset);
		}
		data.setLabels(labels);
		return new LineChart(data).toJson();
	}

	@GetMapping("/quotes")
	public List<Quote> quotes(@Valid @Size(min = 1, max = 50) @RequestParam List<String> symbols,
			@RequestParam(required = false) String search, @RequestParam(required = false) ChangeFilter changeFilter,
			@RequestParam(required = false, defaultValue = "CHANGE") QuoteSort sort,
			@RequestParam(required = false, defaultValue = "DESC") SortDirection sortDirection,
			@RequestParam(required = false, defaultValue = "ONE_DAY") ChartRange chartRange) {
		return iexQuoteService.getAll(symbols, search, changeFilter, sort, sortDirection, chartRange);
	}
}
