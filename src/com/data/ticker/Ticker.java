package com.data.ticker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

public class Ticker {

	@Override
	public String toString() {
		return "Ticker [ticker=" + ticker + ", firstLine=" + firstLine
				+ ", map size=" + map.size() + "]";
	}



	private static Logger logger = Logger.getLogger(Ticker.class);

	public final String ticker;
	private String firstLine;

	private Map<DateTime, Entry> map = new HashMap<DateTime, Entry>();

	public Map<DateTime, Entry> getMap() {
		return map;
	}

	/**
	 * get file with // <TICKER>,<DTYYYYMMDD>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,
	 * // ACTION, 20060724, 13.90, 13.90, 11.60,11.80,1211035
	 * 
	 * @param absolutePath
	 */
	public Ticker(String absolutePath) {
		ticker = absolutePath.substring(
				absolutePath.lastIndexOf("\\") + "\\".length(),
				absolutePath.length() - ".mst".length());
		logger.trace("create ticker for " + ticker);
		Scanner s;
		try {
			s = new Scanner(new File(absolutePath));
			firstLine = s.nextLine();
			logger.trace("first line: " + firstLine);
			// List<Entry> listOFEntries = new ArrayList<Entry>();

			while (s.hasNextLine()) {
				String line = s.nextLine();
				Entry e = new Entry(line);
				map.put(e.date, e);

			}

		} catch (FileNotFoundException e) {
			logger.error("file not found!!!" + e);
			e.printStackTrace();
		}
	}
	
	

	private class Entry {
		@Override
		public String toString() {
			return "Entry [name=" + name + ", date=" + date + ", open=" + open
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

		public Entry(String entry) {
			super();
			String[] current = entry.split(",");

			logger.trace(Arrays.asList(current));

			this.name = current[0];
			this.date = new DateTime(current[1]);
			this.open = Float.parseFloat(current[2]);
			this.high = Float.parseFloat(current[2]);
			this.low = Float.parseFloat(current[2]);
			this.close = Float.parseFloat(current[2]);
			this.vol = Float.parseFloat(current[6]);
		}

	}

}
