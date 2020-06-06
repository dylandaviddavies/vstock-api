package me.dylandavies.vstockapi.repositories;

import java.util.List;

import pl.zankowski.iextrading4j.api.stocks.ChartRange;
import pl.zankowski.iextrading4j.api.stocks.v1.News;

public interface IIexNewsService {
	List<News> getAll(List<String> symbols, ChartRange chartRange, Integer limit);
}
