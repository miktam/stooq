package com.data;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.data.ta.SMA;
import com.data.ticker.Ticker;

public class DataDownloaderTest {

	private final static Logger logger = Logger
			.getLogger(DataDownloaderTest.class);

	static DataDownloader dd;

	@BeforeClass
	public static void beforeClass() {
		dd = new DataDownloaderImpl();
	}

	@Ignore
	@Test
	public void createDate() {
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");
		DateTime dateTime = formatter.parseDateTime("20111230");
		logger.info(dateTime);
	}

	@Ignore
	@Test
	public void getClosedPriceForTickers() throws Exception {

		dd.downloadData();
		String kghm = "KGHM";
		TickerManager.ins();
		Ticker kg = TickerManager.getTickerFor(kghm);
		logger.info(kg.toString(10));
		// Tool.pa(TickerManager.getCloseFor(kghm, 10));
	}
	
	@Test
	public void checkSignals() throws Exception
	{
		DataDownloader dd = new DataDownloaderImpl();
		dd.downloadData();
		
		List<String> tickersName = new ArrayList<String> (){{
			add("KGHM");
			add("CIECH");
			add("PKNORLEN");
			add("LOTOS");
			add("ZYWIEC");
			add("MILLENNIUM");
			add("DEBICA");
			add("KETY");
			add("DEBICA");
			add("BANKBPH");
		}};
			
		
		List<Ticker> interesting = TickerManager.getSignalsBasedOnSMAFor(tickersName);
		logger.info(interesting);
	}

	@Test
	public void getSMA() throws Exception {

		dd.downloadData();
		String kghm = "KGHM";
		TickerManager.ins();
		Ticker kg = TickerManager.getTickerFor(kghm);

		kg.taSMA(SMA.SMA15);
		kg.taSMA(SMA.SMA30);
		kg.taSMA(SMA.SMA45);

		logger.info(kg.toString(45));
	}
}
