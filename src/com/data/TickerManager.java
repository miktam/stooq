package com.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.data.ta.SMA;
import com.data.ticker.EntryDayTicker;
import com.data.ticker.Ticker;
import com.tools.Tool;

public class TickerManager {

	private static TickerManager INSTANCE = new TickerManager();

	private static final Logger logger = Logger.getLogger(TickerManager.class);

	private List<Ticker> tickers = new ArrayList<Ticker>();
	private static Map<String, Ticker> nameTickerMap = new HashMap<String, Ticker>();

	public static Ticker getTickerFor(String name) {
		return nameTickerMap.get(name);
	}

	public static List<Ticker> getSignalsBasedOnSMAFor(List<String> tickersName)
			throws Exception {
		List<Ticker> tickersWithSignals = new ArrayList<Ticker>();

		for (String ticker : tickersName) {

			Ticker t = getTickerFor(ticker);
			logger.trace("TA: check " + t.ticker);

			t.taSMA(SMA.SMA15);
			t.taSMA(SMA.SMA30);
			t.taSMA(SMA.SMA45);

			List<EntryDayTicker> last = t.getLast(46);

			EntryDayTicker yesterdayEntry = null;

			for (EntryDayTicker entry : last) {
				if (yesterdayEntry == null)
					yesterdayEntry = entry;
				else {
					if ((yesterdayEntry.isCloseBiggerAnySMA() == false)
							&& entry.isCloseBiggerAnySMA() == true) {				
							logger.warn("BUY signal:" + t.ticker + " for " + entry.date.toString(Tool.dm()));
							tickersWithSignals.add(t);
						} else if ((yesterdayEntry.isCloseBiggerAnySMA() == true
							&& entry.isCloseBiggerAnySMA() == false))
						{
							logger.warn("SELL signal:" + t.ticker + " for " + entry.date.toString(Tool.dm()));
						}
						
						yesterdayEntry = entry;
					}
				
			}
		}

		return tickersWithSignals;

	}

	// public static double[] getCloseFor(String tickerName, int howMany) {
	// double[] result = new double[howMany];
	// List<EntryDayTicker> tickers = getLast(tickerName, howMany);
	//
	// int c = 0;
	// for (EntryDayTicker t : tickers) {
	// result[c++] = t.close;
	// }
	//
	// return result;
	// }

	public Map<String, Ticker> getNameTickerMap() {
		return nameTickerMap;
	}

	private TickerManager() {
	};

	public static TickerManager ins() {
		return INSTANCE;
	}

	public void loadTickers(List<Ticker> ti) {
		tickers = ti;

		for (Ticker t : tickers) {
			nameTickerMap.put(t.ticker, t);
		}
	}

}
