package com.data;

import java.util.List;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hamcrest.core.IsEqual;
import org.junit.BeforeClass;
import org.junit.Test;

import com.data.ticker.Ticker;

import static org.hamcrest.CoreMatchers.*;

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
		
		logger.debug(tickers);
		
		
		
	}

}
