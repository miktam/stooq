package com.data.ticker;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EntryDayTicker {
	
	private static Logger logger = Logger.getLogger(EntryDayTicker.class);
	
	private static DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
	
	@Override
	public String toString() {
		return "Ticker [name=" + name + ", date=" + date.toString(DateTimeFormat.forPattern("dd-MM-yyyy")) + ", open=" + open
				+ ", high=" + high + ", low=" + low + ", close=" + close
				+ ", vol=" + vol + "]";
	}

	public final String name;
	public final DateTime date;
	public final float open;
	public final float high;
	public final float low;
	public final float close;
	public final float vol;

	public EntryDayTicker(String entry) {
		super();
		String[] current = entry.split(",");

		logger.trace(Arrays.asList(current));

		this.name = current[0];
		this.date = formatter.parseDateTime(current[1]);
		this.open = Float.parseFloat(current[2]);
		this.high = Float.parseFloat(current[3]);
		this.low = Float.parseFloat(current[4]);
		this.close = Float.parseFloat(current[5]);
		this.vol = Float.parseFloat(current[6]);
	}

}
