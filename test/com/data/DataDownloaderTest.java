package com.data;

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
	public void getSMA() throws Exception {

		dd.downloadData();
		String kghm = "KGHM";
		TickerManager.ins();
		Ticker kg = TickerManager.getTickerFor(kghm);

		kg.taSMA(SMA.SMA15);
		kg.taSMA(SMA.SMA30);
		kg.taSMA(SMA.SMA45);

		logger.info(kg.toString(50));
	}

	@Test
	@Ignore
	public void getLastTickers() throws Exception {

		List<Ticker> tickers = dd.downloadData();
		String wig = "WIG20";
		String kghm = "KGHM";
		String MAGNA = "06MAGNA";

		TickerManager.ins();
		logger.info("last 3 Entry for " + MAGNA + " = "
				+ TickerManager.getLast(MAGNA, 3));
		logger.info("last 3 Entry for " + wig + " = "
				+ TickerManager.getLast(wig, 3));
		logger.info("last 2 Entry for " + kghm + " = "
				+ TickerManager.getLast(kghm, 2));

	}

	@Test
	@Ignore
	public void testMACD() throws Exception {

		List<Ticker> tickers = dd.downloadData();
		// assertThat(tickers.size(), equalTo(notNullValue()));

		String kghm = "KGHM";

		TickerManager.ins();
		logger.info("last 3 Entry for " + kghm + " = "
				+ TickerManager.getLast(kghm, 3));

	}

}
