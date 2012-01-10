package com.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.data.ta.SMA;
import com.data.ta.Signal;
import com.data.ta.SignalGenerator;
import com.data.ticker.EntryDayTicker;
import com.data.ticker.Ticker;

public class TickerManager {

	private static TickerManager INSTANCE = new TickerManager();

	private static final Logger logger = Logger.getLogger(TickerManager.class);

	private List<Ticker> tickers = new ArrayList<Ticker>();
	private static Map<String, Ticker> nameTickerMap = new HashMap<String, Ticker>();

	public static Ticker getTickerFor(String name) {
		return nameTickerMap.get(name);
	}

    public static List<Signal> getSignalsBasedOnRsiForAll(List<Ticker> tickers, int days) {

        List<Signal> signals = new ArrayList<Signal>();

        for (Ticker ticker : tickers) {
            logger.trace("TA: check " + ticker);
            
            try {
                ticker.taRSI();
            }
            catch (java.lang.IndexOutOfBoundsException e)
            {
                logger.trace(ticker + " " + e);
                continue;
            }
            List<EntryDayTicker> last = ticker.getLast(days);

            EntryDayTicker yesterdayEntry = null;

            for (EntryDayTicker entry : last) {
                if (yesterdayEntry == null)
                    yesterdayEntry = entry;
                else {
                    Signal s = SignalGenerator.checkRSI(ticker, yesterdayEntry, entry);
                    if (s != null)
                        signals.add(s);
                    yesterdayEntry = entry;
                }
            }
        }

        return signals;
    }

    public static List<Signal> getSignalsBasedOnRsiFor(List<String> tickersName) throws Exception {
        List<Signal> signals = new ArrayList<Signal>();
        int days = 14;

        for (String ticker : tickersName) {

            Ticker t = getTickerFor(ticker);
            logger.info("TA: check " + ticker + "=" + t.ticker);

            t.taRSI();
            List<EntryDayTicker> last = t.getLast(days);

            EntryDayTicker yesterdayEntry = null;

            for (EntryDayTicker entry : last) {
                if (yesterdayEntry == null)
                    yesterdayEntry = entry;
                else {
                    Signal s = SignalGenerator.checkRSI(t, yesterdayEntry, entry);
                    if (s != null)
                        signals.add(s);
                    yesterdayEntry = entry;
                }
            }
        }

        return signals;
    }

	public static List<Signal> getSignalsBasedOnSMAFor(List<String> tickersName, int days) throws Exception {
		List<Signal> signals = new ArrayList<Signal>();

		for (String ticker : tickersName) {

			Ticker t = getTickerFor(ticker);
			logger.info("TA: check " + ticker + "=" + t.ticker);

			t.taSMA(SMA.SMA15);
			t.taSMA(SMA.SMA30);
			t.taSMA(SMA.SMA45);

			List<EntryDayTicker> last = t.getLast(days + 1);

			EntryDayTicker yesterdayEntry = null;

			for (EntryDayTicker entry : last) {
				if (yesterdayEntry == null)
					yesterdayEntry = entry;
				else {
					Signal s = SignalGenerator.checkSMA(t, yesterdayEntry, entry);
					if (s != null)
						signals.add(s);
					yesterdayEntry = entry;
				}

			}
		}

		return signals;

	}

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
