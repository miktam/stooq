package com.data;

import java.util.Arrays;
import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.data.ticker.Ticker;
import com.tools.Tool;

public class DataDownloaderTest {
	
	private final static Logger logger = Logger.getLogger(DataDownloaderTest.class);
	
	
	static DataDownloader dd;
	
	@BeforeClass
	public static void beforeClass()
	{
		BasicConfigurator.configure();
		dd = new DataDownloaderImpl();
	}
	
	@Ignore
	@Test
	public void createDate()
	{
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime dateTime = formatter.parseDateTime("20111230");
		logger.info(dateTime);
	}

	
	@Test
	public void getClosedPriceForTickers() throws Exception {		
		
		dd.downloadData();
		String kghm = "KGHM";				
		TickerManager.ins();	
		Tool.pa(TickerManager.getCloseFor(kghm, 10));
	}
	
	@Test
	@Ignore
	public void getLastTickers() throws Exception {
		
		
		List<Ticker> tickers = dd.downloadData();
		String wig = "WIG20";
		String kghm = "KGHM";		
		String MAGNA = "06MAGNA";		
		
		TickerManager.ins();
		logger.info("last 3 Entry for " + MAGNA + " = "+ TickerManager.getLast(MAGNA, 3));		
		logger.info("last 3 Entry for " + wig + " = "+ TickerManager.getLast(wig, 3));		
		logger.info("last 2 Entry for " + kghm + " = "+ TickerManager.getLast(kghm, 2));
		
	}
	
	@Test
	@Ignore
	public void testMACD() throws Exception {
		
		
		List<Ticker> tickers = dd.downloadData();
		//assertThat(tickers.size(), equalTo(notNullValue()));

		String kghm = "KGHM";		
	
		
		TickerManager.ins();
		logger.info("last 3 Entry for " + kghm + " = "+ TickerManager.getLast(kghm, 3));		
		
		
	}

}
