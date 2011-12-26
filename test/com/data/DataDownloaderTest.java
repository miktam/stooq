package com.data;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.BeforeClass;
import org.junit.Test;

import com.data.ticker.Ticker;

public class DataDownloaderTest {
	
	private final static Logger logger = Logger.getLogger(DataDownloaderTest.class);
	
	
	static DataDownloader dd;
	
	@BeforeClass
	public static void beforeClass()
	{
		BasicConfigurator.configure();
		dd = new DataDownloaderImpl();
		logger.info("instantiated dd: " + dd);
	}
	
	@Test
	public void createDate()
	{
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime dateTime = formatter.parseDateTime("20111230");
		logger.info(dateTime);
	}

	@Test
	public void test() throws Exception {
		
		
		List<Ticker> tickers = dd.downloadData();
		//assertThat(tickers.size(), equalTo(notNullValue()));
		
		String wig = "WIG20";
		String kghm = "KGHM";		
		String MAGNA = "06MAGNA";		
		
		logger.info("last 3 Entry for " + MAGNA + " = "+ TickerManager.ins().getLast(MAGNA, 3));		
		logger.info("last 3 Entry for " + wig + " = "+ TickerManager.ins().getLast(wig, 3));		
		logger.info("last 2 Entry for " + kghm + " = "+ TickerManager.ins().getLast(kghm, 2));
		
	}

}
