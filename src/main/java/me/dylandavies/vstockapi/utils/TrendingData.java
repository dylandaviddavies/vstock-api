package me.dylandavies.vstockapi.utils;

import java.io.Serializable;

public class TrendingData implements Serializable {
	private static final long serialVersionUID = 1931047866303100104L;

	private final int rank;
	private final String symbol;

	public TrendingData(String symbol, int rank) {
		this.symbol = symbol;
		this.rank = rank;
	}

	public int getRank() {
		return rank;
	}

	public String getSymbol() {
		return symbol;
	}
}
