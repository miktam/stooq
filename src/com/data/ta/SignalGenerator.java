package com.data.ta;

import org.apache.log4j.Logger;

import com.data.ticker.EntryDayTicker;
import com.data.ticker.Ticker;
import com.tools.Tool;

public class SignalGenerator {

	private static final Logger logger = Logger.getLogger(SignalGenerator.class);

	public static Signal checkSMA(Ticker t, EntryDayTicker yesterday, EntryDayTicker today) {

		Signal s = null;

		if ((isCloseBiggerAnySMA(yesterday) == false) && isCloseBiggerAnySMA(today) == true) {
			logger.warn("BUY signal:" + t.ticker + " for " + today.date.toString(Tool.dm()));
			// TODO differentiate signal enums
			s = new Signal(t, today.date, SignalEnum.B_SMA15);
		} else if ((isCloseBiggerAnySMA(yesterday) == true && isCloseBiggerAnySMA(today) == false)) {
			logger.warn("SELL signal:" + t.ticker + " for " + today.date.toString(Tool.dm()));
			s = new Signal(t, today.date, SignalEnum.S_SMA15);
		}

		return s;
	}

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

}
