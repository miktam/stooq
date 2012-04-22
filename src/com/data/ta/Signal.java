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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Signal signal = (Signal) o;

        if (date != null ? !date.equals(signal.date) : signal.date != null) return false;
        if (ticker != null ? !ticker.equals(signal.ticker) : signal.ticker != null) return false;
        return type == signal.type;

    }

    @Override
    public int hashCode() {
        int result = ticker != null ? ticker.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
