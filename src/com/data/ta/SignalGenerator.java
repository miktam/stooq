package com.data.ta;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.data.ticker.EntryDayTicker;
import com.data.ticker.Ticker;
import com.tools.Tool;

public class SignalGenerator {

	private static final Logger logger = Logger.getLogger(SignalGenerator.class);

	public static Signal checkSMA(Ticker t, EntryDayTicker yesterday, EntryDayTicker today) {

		Signal s = null;

		List<SMA> smas = new ArrayList<SMA>() {
			{
				add(SMA.SMA15);
				add(SMA.SMA30);
				add(SMA.SMA45);
			}
		};

		// logger.info(today.close + "|sma15=" +
		// Tool.df().format(today.smaMap.get(SMA.SMA15)) + " "
		// + today.date.toString(Tool.dm()));

		for (SMA sma : smas) {
			if ((isCloseBiggerForSMA(yesterday, sma) == false) && isCloseBiggerForSMA(today, sma) == true) {
				s = new Signal(t, today.date, sma.SMA2Signal(BuySell.BUY));
			} else if ((isCloseBiggerForSMA(yesterday, sma) == true && isCloseBiggerForSMA(today, sma) == false)) {
				s = new Signal(t, today.date, sma.SMA2Signal(BuySell.SELL));
			}
		}

		if (s != null)
			logger.warn(s);

		return s;
	}

	@Deprecated
	private static boolean isCloseBiggerAnySMA(EntryDayTicker e) {
		boolean signal = false;
		if (e.anySMAexists()) {
			if (e.smaMap.get(SMA.SMA15) != null && e.close > e.smaMap.get(SMA.SMA15) || e.smaMap.get(SMA.SMA30) != null
					&& e.close > e.smaMap.get(SMA.SMA30) || e.smaMap.get(SMA.SMA45) != null
					&& e.close > e.smaMap.get(SMA.SMA45)) {
				// date.toString(Tool.dm()));
				signal = true;
			}
		}
		return signal;
	}

	private static boolean isCloseBiggerForSMA(EntryDayTicker e, SMA sma) {
		boolean signal = false;
		if (e.anySMAexists()) {
			if (e.smaMap.get(sma) != null && e.close > e.smaMap.get(sma)) {
				signal = true;
			}
		}
		return signal;
	}

}
