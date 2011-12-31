package com.data.ta;

import org.joda.time.DateTime;
import com.data.ticker.Ticker;
import com.tools.Tool;

public class Signal {

	public final Ticker ticker;
	public final DateTime date;
	public final SignalEnum type;

	public Signal(Ticker ticker, DateTime date, SignalEnum type) {
		super();
		this.ticker = ticker;
		this.date = date;
		this.type = type;
	}

	@Override
	public String toString() {
		return "Signal [ticker=" + ticker + ", date=" + date.toString(Tool.dm()) + ", type=" + type + "]";
	}	
}
