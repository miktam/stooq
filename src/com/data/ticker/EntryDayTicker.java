package com.data.ticker;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.data.ta.SMA;
import com.tools.Tool;

public class EntryDayTicker {

    private static Logger logger = Logger.getLogger(EntryDayTicker.class);

    private static DateTimeFormatter formatter = DateTimeFormat
            .forPattern("yyyyMMdd");

    @Override
    public String toString() {
        return "Ticker [name=" + name + ", date=" + date.toString(Tool.dmy())
                + ", open=" + open + ", high=" + high + ", low=" + low
                + ", close=" + close + ", vol=" + vol + ", gain=" + gain + " ,loss=" + loss + "]";
    }

    public final String name;
    public final DateTime date;
    public final double open;
    public final double high;
    public final double low;
    public final double close;
    public final double vol;

    private double gain = -1;
    private double loss = -1;
    private double RSI = -1;

    public void setChange(double change) {
        if (change > 0) {
            gain = change;
            loss = 0;
        } else if (change < 0) {
            loss = change * -1;
            gain = 0;
        } else {
            loss = 0;
            gain = 0;
        }

    }

    public void setRSI(double rsi) {
        this.RSI = rsi;
    }

    public double getRSI() {
        return RSI;
    }

    public Map<SMA, Double> smaMap = new HashMap<SMA, Double>();

    public EntryDayTicker(String entry) {
        super();
        String[] current = entry.split(",");

        logger.trace(Arrays.asList(current));

        this.name = current[0];
        this.date = formatter.parseDateTime(current[1]);
        this.open = Double.parseDouble(current[2]);
        this.high = Double.parseDouble(current[3]);
        this.low = Double.parseDouble(current[4]);
        this.close = Double.parseDouble(current[5]);
        this.vol = Double.parseDouble(current[6]);
    }

    String getSMA(SMA sma) {
        Double result = smaMap.get(sma);
        return (result == null) ? "" : ", " + sma + "="
                + new DecimalFormat("##.##").format(result);
    }

    public boolean anySMAexists() {
        boolean res = false;
        if (smaMap.get(SMA.SMA15) != null || smaMap.get(SMA.SMA30) != null
                || smaMap.get(SMA.SMA45) != null)
            return true;

        return res;
    }

}
