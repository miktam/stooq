package com.data;

import static com.data.TickerManager.getTickerFor;

import java.util.List;
import java.util.Map.Entry;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.junit.BeforeClass;
import org.junit.Test;

import sun.security.action.GetLongAction;

import com.data.ticker.EntryDayTicker;
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
	public void test() throws Exception {
		
		
		List<Ticker> tickers = dd.downloadData();
		//assertThat(tickers.size(), equalTo(notNullValue()));
		
		String wig = "WIG20";
		String kghm = "KGHM";		
		
		Ticker t = getTickerFor(wig);
		logger.info("first Entry for " + kghm + " = "+ getTickerFor(kghm).first());
		
		logger.info("last Entry for " + kghm + " = "+ TickerManager.ins().getLast(kghm, 2));
		
		Ticker kghmTicker = getTickerFor(kghm);		
	}

}
