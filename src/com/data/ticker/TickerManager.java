package com.data.ticker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.data.ta.SMA;
import com.data.ta.Signal;
import com.data.ta.SignalGenerator;

public class TickerManager {

    private static TickerManager INSTANCE = new TickerManager();

    private static final Logger logger = Logger.getLogger(TickerManager.class);

    private static List<Ticker> tickers = new ArrayList<Ticker>();
    private static Map<String, Ticker> nameTickerMap = new HashMap<String, Ticker>();
    

    public static List<Ticker> parseFiles(List<String> absolutePaths)
	{

		// <TICKER>,<DTYYYYMMDD>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,
		//ACTION,20060724,13.90,13.90,11.60,11.80,1211035
		for (String path:absolutePaths)
		{
			Ticker t = new Ticker(path);
			tickers.add(t);
		}
		
		logger.trace("created list of tickers #: " + tickers.size());	
		
		return tickers;
	}
    
    
    public static Ticker getTickerFor(String name) {
        return nameTickerMap.get(name);
    }

    public static List<Signal> getSignalsOnRsi(List<Ticker> tickers, int days) {

        List<Signal> signals = new ArrayList<Signal>();

        for (Ticker ticker : tickers) {
            logger.trace("TA: check " + ticker);

            try {
                ticker.taRSI();
            } catch (java.lang.IndexOutOfBoundsException e) {
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

    public static List<Signal> getSignalsOnSMA(List<Ticker> tickers, int days) throws Exception {
        List<Signal> signals = new ArrayList<Signal>();

        for (Ticker t : tickers) {
            logger.trace("TA: check " + t.ticker);

            List<EntryDayTicker> last = null;

            try {

                //t.taSMA(SMA.SMA15);
                //t.taSMA(SMA.SMA30);
                t.taSMA(SMA.SMA45);
                t.taSMA(SMA.SMA200);

                last = t.getLast(days + 1);

            } catch (IndexOutOfBoundsException e) {

                logger.trace(e);
                continue;
            }

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

    private TickerManager() {
    }

    ;

    public static TickerManager ins() {
        return INSTANCE;
    }

    public void loadTickers(List<Ticker> ti) {
        tickers = ti;

        for (Ticker t : tickers) {
            nameTickerMap.put(t.ticker, t);
        }
    }


    public static List<Signal> getSignalsOnVolume(List<Ticker> tickers, int days) {

        List<Signal> signals = new ArrayList<Signal>();

        for (Ticker ticker : tickers) {
            logger.trace("TA: check " + ticker);

            try {

                List<EntryDayTicker> last = ticker.getLast(days);
            } catch (IndexOutOfBoundsException e) {
                logger.trace(e);
                continue;
            }

            Signal s = SignalGenerator.checkVolume(ticker, days);

            if (s != null)
                signals.add(s);
        }

        return signals;
    }

	public static List<Signal> getSignalsOnAD(List<Ticker> tickers, int days) {
		  List<Signal> signals = new ArrayList<Signal>();

	        for (Ticker ticker : tickers) {
	            logger.trace("AD: check " + ticker);
	            
	            ticker.taAD(20);

	            try {

	                List<EntryDayTicker> last = ticker.getLast(days);
	            } catch (IndexOutOfBoundsException e) {
	                logger.trace(e);
	                continue;
	            }

	            Signal s = SignalGenerator.checkAD(ticker, days);

	            if (s != null)
	                signals.add(s);
	        }

	        return signals;
	}
}
