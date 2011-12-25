package com.data.ticker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
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

	private EntryDayTicker firstTransaction;
	private EntryDayTicker lastTransaction;

	private String firstLine;
	private Map<DateTime, EntryDayTicker> map = new LinkedHashMap<DateTime, EntryDayTicker>();
	private List<EntryDayTicker> list = new LinkedList<EntryDayTicker>();

	// GETTERS
	public Map<DateTime, EntryDayTicker> getMap() {
		return map;
	}
	
	public List<EntryDayTicker> getList()
	{
		return list;
	}

	/**
	 * get first transaction (oldest one)
	 * 
	 * @return
	 */
	public EntryDayTicker first() {
		return firstTransaction;
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

			String firstline = s.nextLine();
			logger.trace("handle first transaction line - first in the history entry"
					+ firstline);
			EntryDayTicker firstEntry = new EntryDayTicker(firstline);
			this.firstTransaction = firstEntry;
			map.put(firstEntry.date, firstEntry);
			list.add(firstEntry);

			while (s.hasNextLine()) {
				String line = s.nextLine();
				logger.trace(line);
				EntryDayTicker e = new EntryDayTicker(line);
				map.put(e.date, e);

				list.add(e);
			}

		} catch (FileNotFoundException e) {
			logger.error("file not found!!!" + e);
			e.printStackTrace();
		}
	}

}
