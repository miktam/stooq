package com.data.parser;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.data.ticker.Ticker;

public class FileParser {
	
	private static Logger logger = Logger.getLogger(FileParser.class);
	
	private static List<Ticker> tickers = new ArrayList<Ticker>();
	
	public static List<Ticker> parseFiles(List<String> absolutePaths)
	{
		logger.debug("start parsing files: " + absolutePaths);
		
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

}
