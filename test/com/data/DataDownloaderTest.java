package com.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.data.ta.Signal;
import com.data.ticker.EntryDayTicker;
import com.data.ticker.Ticker;
import com.data.ticker.TickerManager;
import com.tools.Tool;

public class DataDownloaderTest {

	private final static Logger logger = Logger.getLogger(DataDownloaderTest.class);

	static DataDownloader dd;
	static List<String> wig30tickersList;
	static List<Ticker> tickers;

	@Test
	public void checkAllSignals() throws Exception {

		List<Signal> totalSignals = new ArrayList<Signal>();

		totalSignals.addAll(TickerManager.getSignalsOnVolume(tickers, 1));
		totalSignals.addAll(TickerManager.getSignalsOnRsi(tickers, 3));
		totalSignals.addAll(TickerManager.getSignalsOnSMA(tickers, 1));

        totalSignals.addAll(TickerManager.getSignalsOnHighestLowestPrice(tickers, 3));

		totalSignals.removeAll(extractCompositeEntries(totalSignals));

		displaySignals(totalSignals);
	}

	@Test
	public void testDoublePrinting() {
		double price = 15.3498;
		double bigVolume = 313131313.1232;

		String pricePrinted = Tool.p(bigVolume);
		logger.info(bigVolume + " nicer " + pricePrinted);

		logger.info(price + " nicer " + Tool.p(price));

		logger.info(1000000 + " nicer " + Tool.p(1000000.23));
		logger.info(100000 + " nicer " + Tool.p(100000.23));
	}

	@Test
	public void checkSignalsSTS() throws Exception {
		// List<Signal> interesting = TickerManager.getSignalsOnAD(tickers, 3);
		// displaySignals(interesting);
		Ticker kgh = TickerManager.getTickerFor("KGHM");
		kgh.taSTS();
	}

	@Ignore("not finished")
	@Test
	public void checkSignalsAD() throws Exception {
		// List<Signal> interesting = TickerManager.getSignalsOnAD(tickers, 3);
		// displaySignals(interesting);
		Ticker kgh = TickerManager.getTickerFor("KGHM");
		kgh.taAD(10);
	}

	@Test
	public void checkSignalsVolumeIncreaseForAll() throws Exception {
		List<Signal> interesting = TickerManager.getSignalsOnVolume(tickers, 3);
		displaySignals(interesting);
	}

	@Test
	public void checkSignalsRsiForAll() throws Exception {
		List<Signal> interesting = TickerManager.getSignalsOnRsi(tickers, 4);
		displaySignals(interesting);
	}

	@Test
	public void checkSignalsSMA() throws Exception {
		List<Signal> interesting = TickerManager.getSignalsOnSMA(tickers, 2);
		displaySignals(interesting);
	}

	private static void displaySignals(List<Signal> interesting) {
		List<Signal> composites = extractCompositeEntries(interesting);
		logger.info("COMPOSITES");
		listSignals(composites);
		interesting.removeAll(composites);

		logger.info("BUY");
		List<Signal> tobuy = extractBuySignals(interesting);
		listSignals(tobuy);

		interesting.removeAll(tobuy);
		logger.info("TO SELL");
		listSignals(interesting);
	}

	/**
	 * for WIG * and Futures composites
	 * 
	 * @param interesting
	 * @return
	 */
	private static List<Signal> extractCompositeEntries(List<Signal> interesting) {
		List<Signal> composite = new ArrayList<Signal>();
		for (Signal i : interesting) {
			if (i.ticker.ticker.startsWith("WIG") || i.ticker.ticker.startsWith("FW")
					|| i.ticker.ticker.contains("WIG") || i.ticker.ticker.contains("RESPECT")
					|| i.ticker.ticker.contains("INVESTORMS"))
				composite.add(i);
		}
		return composite;
	}

	private static List<Signal> extractBuySignals(List<Signal> interesting) {
		List<Signal> tobuy = new ArrayList<Signal>();
		for (Signal i : interesting) {
			if (i.type.name().startsWith("B"))
				tobuy.add(i);
		}
		return tobuy;
	}

	private static void listSignals(List<Signal> s) {

		logger.trace("unsorted list: " + s);

		// sort signals by tickers

		int LIMIT = 500000;
		logger.info("show sorted Signals (by volume) for volume (obroty) > " + Tool.p(LIMIT));

		Collections.sort(s, new ComapatorBasedOnTickerName());
		Collections.sort(s, new ComapatorBasedOnVolume());

		for (Signal sig : s) {

			double obroty = (sig.ticker.getLast(1)).get(0).close * (sig.ticker.getLast(1)).get(0).vol;

			if (obroty > LIMIT)
				logger.warn(sig + ", obroty=" + Tool.p(obroty) + "\t" + getLastPrices(sig.ticker.getLast(5)));
		}
	}

	private static String getLastPrices(List<EntryDayTicker> entries) {
		StringBuilder res = new StringBuilder();
		for (EntryDayTicker e : entries) {
			res.append(e.close + ", ");
		}

		return res.toString().substring(0, res.length() - 2);
	}

	static class ComapatorBasedOnVolume implements Comparator<Signal> {
		@Override
		public int compare(Signal o1, Signal o2) {
			double vol1 = o1.ticker.getLast(1).get(0).close * (o1.ticker.getLast(1)).get(0).vol;
			double vol2 = o2.ticker.getLast(1).get(0).close * (o2.ticker.getLast(1)).get(0).vol;
			return (int) (vol2 - vol1);
		}
	}

	static class ComapatorBasedOnTickerName implements Comparator<Signal> {
		@Override
		public int compare(Signal o1, Signal o2) {
			return o1.ticker.ticker.compareTo(o2.ticker.ticker);
		}
	}

	@BeforeClass
	public static void beforeClass() throws Exception {

		Thread.sleep(5000);
		wig30tickersList = new ArrayList<String>() {
			{
				add("ASSECOPOL");
				add("HANDLOWY");
				add("BRE");
				add("GTC");
				add("GETIN");
				add("JSW");
				add("KERNEL");
				add("KGHM");
				add("LOTOS");
				add("BOGDANKA");
				add("PBG");
				add("PEKAO");
				add("PGE");
				add("PGNIG");
				add("PKNORLEN");
				add("PKOBP");
				add("PZU");
				add("TAURONPE");
				add("TPSA");
				add("TVN");

				add("PETROLINV");
				add("HAWE");
				add("BORYSZEW");
				add("PEP");
				add("POLIMEXMS");
				add("CYFRPLSAT");
				add("CIECH");
				add("SYNTHOS");
			}
		};

		dd = new DataDownloader();
		tickers = TickerManager.parseFiles(dd.downloadData());
	}

}
