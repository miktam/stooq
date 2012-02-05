package com.data.ticker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.data.ta.SMA;
import com.tools.Tool;

import static com.tools.Tool.df;

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
        int lookback = 220;
        int period = 14;
        
        logger.trace("count gain/losses for more than lookback");

        List<EntryDayTicker> entriesToCountGainLosses = new LinkedList<EntryDayTicker>(getLast(lookback+period+1));
         ListIterator<EntryDayTicker> it = entriesToCountGainLosses.listIterator();

        EntryDayTicker prev = it.next();
        while (it.hasNext()) {
            EntryDayTicker current = it.next();
            current.setChange(current.close - prev.close);

            prev = current;
            logger.trace(current);
        }

        List<EntryDayTicker> last = new LinkedList<EntryDayTicker>(getLast(lookback));
        logger.trace("start counting RS for first 14 days");

        ListIterator<EntryDayTicker> first14days = last.listIterator(period);

        double gains = 0;
        double losses = 0;
        while (first14days.hasPrevious()) {
            // count ave gains and loss
            EntryDayTicker today = first14days.previous();
            logger.trace("calculate gains/losses for+" + today);
            gains += today.getGain();
            losses += today.getLoss();
        }

        double avgGain = gains / 14;
        double avgLoss = losses / 14;
        
        double RS = avgGain / avgLoss;

        double RSI = 100-(100/(1+RS));
        EntryDayTicker t = last.get(--period);
        t.setRSI(RSI);

        logger.trace("avgGain:" + df().format(avgGain) + ", avgLoss:" + df().format(avgLoss) + ", RS:" + df().format(RS) + ", RSI:" + df().format(RSI) + " for " + t.date.toString(Tool.dm())+ ", close =" + t.close);

        logger.trace("calculate RSI for next days");
        ListIterator<EntryDayTicker> nextDays = last.listIterator(period);

        // skip day which is already calculated
        nextDays.next();

        double previousAvgGain = avgGain;
        double previousAvgLoss = avgLoss;

        while (nextDays.hasNext()) {
            EntryDayTicker entry = nextDays.next();

            // calculate avg gain/loss
            previousAvgGain = (previousAvgGain * 13 + entry.getGain()) / 14;
            previousAvgLoss = (previousAvgLoss * 13 + entry.getLoss()) / 14;

            RS = previousAvgGain / previousAvgLoss;

            RSI = 100-(100/(1+RS));
            entry.setRSI(RSI);
            logger.trace("avgGain:" + df().format(previousAvgGain) + ", avgLoss:" + df().format(previousAvgLoss) + ", RS:" + df().format(RS) + ", RSI:" + df().format(RSI) + " for " + entry.date.toString(Tool.dm()) + ", close =" + entry.close);
        }
    }

    public double[] getRsi(int howMany)
    {
        double[] res = new double[howMany];
        List<EntryDayTicker> lastEntry = getLast(howMany);
        
        StringBuffer sb = new StringBuffer();

        int counter = 0;
        for (EntryDayTicker e : lastEntry) {
            sb.append(e.date.toString(Tool.dm()) + ", rsi = " + e.getRSI());
            res[counter] = e.getRSI();
            counter++;
        }
        
        logger.info(sb);
        return res;
    }

    public void taSMA(SMA sma) {
        int days = sma.value();

        logger.trace("start counting sma for " + ticker);
        EntryDayTicker[] lastEntries = edt2Array(days*2);

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

	public void taAD(int days) {
		logger.trace("calculate Money Flow Multiplier");
		
		double moneyFlowMult = 0;
		double moneyFlowValue = 0;
		double previousAD = 0;
		
		List<EntryDayTicker> lastEdt = getLast(days*2);
		for (EntryDayTicker e : lastEdt) {
			//Money Flow Multiplier = [(Close  -  Low) - (High - Close)] /(High - Low)
            moneyFlowMult = ((e.close-e.low) - (e.high - e.close)) / (e.high - e.low);
			moneyFlowValue = moneyFlowMult * e.vol;
			previousAD = moneyFlowValue + previousAD;
			e.setAD(previousAD);
			
			logger.info(e.date.toString(Tool.d()) + ":" + e.getAD() + " " + Tool.df().format(e.close));
		}
	}

    public void taSTS() {
        List<EntryDayTicker> last = new LinkedList<EntryDayTicker>(getLast(30));
        ListIterator<EntryDayTicker> it = last.listIterator(14);

        while (it.hasNext())
        {
            
            logger.info("index:" + it.nextIndex());


            // get max for last 14
            ListIterator<EntryDayTicker> goBack = it;
            double max = 0;
            double min = 0;

            int counter = 14;
            while (goBack.hasPrevious() && counter > 0)
            {
                EntryDayTicker t = goBack.previous();
                if (t.close > max)
                    max = t.close;
                if (t.close < min)
                    min = t.close;

                counter--;
                
                logger.info(goBack.previousIndex());
            }

            EntryDayTicker next = it.next();

            logger.info(next.date.toString(Tool.d()) + " max = " + Tool.df().format(max) + ", min = " + Tool.df().format(min));

            
            // get min for last 14

        }



    }

    /**
     * get array of ADT for given amount of days
     * @param days
     * @return
     */
    private EntryDayTicker[] edt2Array(int days) {
        EntryDayTicker[] lastEntries = new EntryDayTicker[days];
        Object[] rawEdt = getList().subList(getList().size() - days,
                getList().size()).toArray();

        for (int i = 0; i < rawEdt.length; i++) {
            lastEntries[i] = (EntryDayTicker) rawEdt[i];
        }
        return lastEntries;
    }

    @Override
    public String toString() {
        return ticker;
    }
}
