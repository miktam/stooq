package com.data;

import java.util.ArrayList;
import java.util.List;

import com.tools.Tool;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.data.ta.SMA;
import com.data.ta.Signal;
import com.data.ticker.Ticker;

public class DataDownloaderTest {

	private final static Logger logger = Logger.getLogger(DataDownloaderTest.class);

	static DataDownloader dd;
    static List<String> wig30tickersList;

	@BeforeClass
	public static void beforeClass() {
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

		dd = new DataDownloaderImpl();
	}

	@Test
    public void checkSignalsRsi() throws Exception {

        dd.downloadData();
        List<Signal> interesting = TickerManager.getSignalsBasedOnRsiFor(wig30tickersList);
       listSignals(interesting);
    }

	@Test
	public void checkSignalsWIG20() throws Exception {
		DataDownloader dd = new DataDownloaderImpl();
		dd.downloadData();
		List<Signal> interesting = TickerManager.getSignalsBasedOnSMAFor(wig30tickersList, 100);
		logger.info(interesting);
	}
    
    private void listSignals(List<Signal> s)
    {
        for (Signal sig:s)
        {
            logger.warn(sig);
        }
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
	@Ignore
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
