package com.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.data.ticker.EntryDayTicker;
import com.data.ticker.Ticker;

public class TickerManager {

	private static TickerManager INSTANCE = new TickerManager();

	private List<Ticker> tickers = new ArrayList<Ticker>();
	private static Map<String, Ticker> nameTickerMap = new HashMap<String, Ticker>();
	
	public static Ticker getTickerFor(String name)
	{
		return nameTickerMap.get(name);
	}
	
	public static List<EntryDayTicker> getLast(String name, int howMany)
	{
		Ticker t = nameTickerMap.get(name);
		return t.getList().subList(t.getList().size() - howMany, t.getList().size());
	}

	public Map<String, Ticker> getNameTickerMap() {
		return nameTickerMap;
	}

	private TickerManager() {};

	public static TickerManager ins() {
		return INSTANCE;
	}

	public void loadTickers(List<Ticker> ti) {
		tickers = ti;
		
		for (Ticker t:tickers)
		{
			nameTickerMap.put(t.ticker, t);
		}
	}

}
