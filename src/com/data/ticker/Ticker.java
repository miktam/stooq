package com.data.ticker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.data.ta.SMA;
import com.tools.Tool;

public class Ticker {

    private static Logger logger = Logger.getLogger(Ticker.class);
    public final String ticker;
    private EntryDayTicker firstTransaction;
    private String firstLine;
    private Map<DateTime, EntryDayTicker> map = new LinkedHashMap<DateTime, EntryDayTicker>();
    private List<EntryDayTicker> list = new LinkedList<EntryDayTicker>();

    // GETTERS
    public Map<DateTime, EntryDayTicker> getMap() {
        return map;
    }

    public List<EntryDayTicker> getList() {
        return list;
    }

    /**
     * get first transaction (oldest one)
     *
     * @return
     */
    public EntryDayTicker first() {
        return firstTransaction;
    }

    /**
     * get file with // <TICKER>,<DTYYYYMMDD>,<OPEN>,<HIGH>,<LOW>,<CLOSE>,<VOL>,
     * // ACTION, 20060724, 13.90, 13.90, 11.60,11.80,1211035
     *
     * @param absolutePath
     */
    public Ticker(String absolutePath) {
        ticker = absolutePath.substring(
                absolutePath.lastIndexOf("\\") + "\\".length(),
                absolutePath.length() - ".mst".length());
        logger.trace("create ticker for " + ticker);
        Scanner s;
        try {
            s = new Scanner(new File(absolutePath));
            firstLine = s.nextLine();
            logger.trace("first line: " + firstLine);

            String firstline = s.nextLine();
            logger.trace("handle first transaction line - first in the history entry"
                    + firstline);
            EntryDayTicker firstEntry = new EntryDayTicker(firstline);
            this.firstTransaction = firstEntry;
            map.put(firstEntry.date, firstEntry);
            list.add(firstEntry);

            while (s.hasNextLine()) {
                String line = s.nextLine();
                logger.trace(line);
                EntryDayTicker e = new EntryDayTicker(line);
                map.put(e.date, e);

                list.add(e);
            }

        } catch (FileNotFoundException e) {
            logger.error("file not found!!!" + e);
            e.printStackTrace();
        }
    }

    /**
     * First Average Gain = Sum of Gains over the past 14 periods / 14.
     * Next Average Gain = [(previous Average Gain) x 13 + current Gain] / 14.            *
     * First Average Loss = Total of Losses during past 14 periods / 14
     */
    public void taRSI() {
        int lookback = 100;
        int period = 14;

        List<EntryDayTicker> last = getLast(100);
        ListIterator<EntryDayTicker> it = last.listIterator();

        EntryDayTicker prev = it.next();
        while (it.hasNext()) {
            EntryDayTicker current = it.next();
            current.setChange(current.close - prev.close);

            prev = current;
            
            
            logger.debug(current);
        }


        double rs = 0;


        double RSI = 100 - (100 / 1 + rs);
    }

    public void taSMA(SMA sma) {
        int days = sma.value();

        logger.trace("start counting sma for " + ticker);
        EntryDayTicker[] lastEntries = new EntryDayTicker[days * 2];
        Object[] rawEdt = getList().subList(getList().size() - days * 2,
                getList().size()).toArray();

        for (int i = 0; i < rawEdt.length; i++) {
            lastEntries[i] = (EntryDayTicker) rawEdt[i];
        }

        for (int i = days; i < days * 2; i++) {
            // collect appropriate amount of previous values
            double sumForPreviousValues = 0;
            for (int y = i - (days - 1); y <= i; y++) {
                sumForPreviousValues += lastEntries[y].close;
            }
            lastEntries[i].smaMap.put(sma, sumForPreviousValues / days);

        }

        StringBuilder sb = new StringBuilder();
        for (int i = days; i < days * 2; i++) {
            Double value = lastEntries[i].smaMap.get(sma);
            sb.append(Tool.df().format(value) + ", ");
        }

        logger.trace(sma + " for " + ticker + ": " + sb);
    }

    @Override
    public String toString() {
        return ticker;
    }

    public String toString(int lastCloses) {
        StringBuilder sb = new StringBuilder();

        int listSize = getList().size();

        List<EntryDayTicker> sublist = getList().subList(listSize - lastCloses,
                listSize);

        for (EntryDayTicker edt : sublist) {
            sb.append(edt.date.toString(Tool.dm()) + "|close=" + edt.close

                    + edt.getSMA(SMA.SMA45) + edt.getSMA(SMA.SMA30)
                    + edt.getSMA(SMA.SMA15) + "\n");
        }

        return ticker + " last " + lastCloses + ":" + sb;
    }

    public List<EntryDayTicker> getLast(int last) {
        int listSize = getList().size();
        return getList().subList(listSize - last,
                listSize);
    }

}
