package com.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

import com.data.ta.Signal;
import com.data.ticker.Ticker;
import com.tools.Tool;

public class DataDownloaderTest {

    private final static Logger logger = Logger.getLogger(DataDownloaderTest.class);

    static DataDownloader dd;
    static List<String> wig30tickersList;
    static List<Ticker> tickers;

    @BeforeClass
    public static void beforeClass() throws Exception {
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
        tickers = dd.downloadData();
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
    public void checkSignalsWIG30BasedOnSMA() throws Exception {
        List<Signal> interesting = TickerManager.getSignalsOnSMA(tickers, 4);
        displaySignals(interesting);
    }


    private void displaySignals(List<Signal> interesting) {
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
     * @param interesting
     * @return
     */
    private List<Signal> extractCompositeEntries(List<Signal> interesting) {
        List<Signal> composite = new ArrayList<Signal>();
        for (Signal i : interesting) {
            if (i.ticker.ticker.startsWith("WIG") ||i.ticker.ticker.startsWith("FW") || i.ticker.ticker.contains("WIG")
                    || i.ticker.ticker.contains("RESPECT")  || i.ticker.ticker.contains("INVESTORMS"))
                composite.add(i);
        }
        return composite;
    }


    private List<Signal> extractBuySignals(List<Signal> interesting) {
        List<Signal> tobuy = new ArrayList<Signal>();
        for (Signal i : interesting) {
            if (i.type.name().startsWith("B"))
                tobuy.add(i);
        }
        return tobuy;
    }

    private void listSignals(List<Signal> s) {

        logger.trace("unsorted list: " + s);

        int LIMIT = 50000;
        logger.info("show sorted Signals (by volume) for volume (obroty) > " + LIMIT);

        Collections.sort(s, new Comparator<Signal>() {
            @Override
            public int compare(Signal o1, Signal o2) {
                double vol1 = o1.ticker.getLast(1).get(0).close * (o1.ticker.getLast(1)).get(0).vol;
                double vol2 = o2.ticker.getLast(1).get(0).close * (o2.ticker.getLast(1)).get(0).vol;
                return (int) (vol2 - vol1);
            }
        });

        for (Signal sig : s) {

            double obroty = (sig.ticker.getLast(1)).get(0).close * (sig.ticker.getLast(1)).get(0).vol;

            if (obroty > LIMIT)
                logger.warn(sig + ", obroty=" + Tool.df().format(obroty));
        }
    }
}
