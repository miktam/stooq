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

import com.data.ta.SMA;
import com.tools.Tool;

public class Ticker {

	@Override
	public String toString() {
		return "Ticker [ticker=" + ticker + ", firstLine=" + firstLine
				+ ", map size=" + map.size() + "]";
	}

	public String toString(int lastCloses) {
		StringBuilder sb = new StringBuilder();

		int listSize = getList().size();
		
		List<EntryDayTicker> sublist = getList().subList(listSize - lastCloses,	listSize);

		for (EntryDayTicker edt : sublist ) {
			sb.append(edt.date.toString(Tool.dm()) + ":" + edt.close + ", ");
		}

		return ticker + " last " + lastCloses + ":" + sb;
	}

	private static Logger logger = Logger.getLogger(Ticker.class);

	public final String ticker;

	private EntryDayTicker firstTransaction;

	private String firstLine;
	private Map<DateTime, EntryDayTicker> map = new LinkedHashMap<DateTime, EntryDayTicker>();
	private List<EntryDayTicker> list = new LinkedList<EntryDayTicker>();

	// GETTERS
	public Map<DateTime, EntryDayTicker> getMap() {
		return map;
	}

	public List<EntryDayTicker> getList() {
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
	
	public void taSMA(SMA sma)
	{
		int days = sma.value();
		
		logger.info("start counting sma for " + ticker);
		EntryDayTicker[] last100 = new EntryDayTicker[days*2];
		Object []rawEdt = getList().subList(getList().size() - days*2 , getList().size()).toArray();
		
		logger.info("raw edit length:" + rawEdt.length);
		
		for (int i = 0; i < rawEdt.length; i++) {
			last100[i] = (EntryDayTicker) rawEdt[i];
		}				
	
		for (int i = days; i < days*2; i++)
		{
			// collect 50 previous values
			double all50 = 0;
			for (int y = i-(days-1); y <= i ; y++)
			{
				all50+=last100[y].close;
			}
			last100[i].sma50 = all50 / days;
			
			last100[i].smaMap.put(sma, all50/days);
			
			logger.info("sma50 for " + last100[i].date.toString(Tool.dm()) + "=" + last100[i].sma50);
		}		
	}
	
}
