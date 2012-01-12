package com.data.ta;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.data.ticker.EntryDayTicker;
import com.data.ticker.Ticker;
import com.tools.Tool;

public class SignalGenerator {

    private static final Logger logger = Logger.getLogger(SignalGenerator.class);

    public static Signal checkSMA(Ticker t, EntryDayTicker yesterday, EntryDayTicker today) {

        Signal s = null;

        List<SMA> smas = new ArrayList<SMA>() {
            {
                add(SMA.SMA15);
                add(SMA.SMA30);
                add(SMA.SMA45);
            }
        };

        for (SMA sma : smas) {
            if ((isCloseBiggerForSMA(yesterday, sma) == false) && isCloseBiggerForSMA(today, sma) == true) {
                s = new Signal(t, today.date, sma.SMA2Signal(BuySell.BUY));
            } else if ((isCloseBiggerForSMA(yesterday, sma) == true && isCloseBiggerForSMA(today, sma) == false)) {
                s = new Signal(t, today.date, sma.SMA2Signal(BuySell.SELL));
            }
        }

        if (s != null)
            logger.trace(s);

        return s;
    }

    private static boolean isCloseBiggerForSMA(EntryDayTicker e, SMA sma) {
        boolean signal = false;
        if (e.anySMAexists()) {
            if (e.smaMap.get(sma) != null && e.close > e.smaMap.get(sma)) {
                signal = true;
            }
        }
        return signal;
    }

    public static Signal checkRSI(Ticker t, EntryDayTicker yesterdayEntry, EntryDayTicker todayEntry) {

        Signal res = null;

        logger.trace("Buy signals: check if previous RSI was bigger or lower then important levels");

        double prevRSI = yesterdayEntry.getRSI();
        double todayRSI = todayEntry.getRSI();

        if (prevRSI < 40 && todayRSI < 40) {
            if (prevRSI > 30 && todayRSI < 30) {
                res = new Signal(t, todayEntry.date, SignalEnum.B_RSI_30);
            } else if (prevRSI > 20 && todayRSI < 20) {
                res = new Signal(t, todayEntry.date, SignalEnum.B_RSI_20);
            } else if  (prevRSI < 20 && todayRSI < 20)
            {
                if (todayRSI > prevRSI)
                {
                    res = new Signal(t, todayEntry.date, SignalEnum.B_RSI_20_CHANGED_TREND);
                }
            }
            else if  (prevRSI < 30 && todayRSI < 30)
            {
                if (todayRSI > prevRSI)
                {
                    res = new Signal(t, todayEntry.date, SignalEnum.B_RSI_30_CHANGED_TREND);
                }
            }

        }   else   if (prevRSI > 60 && todayRSI > 60) {
            if (prevRSI > 70 && todayRSI < 70) {
                res = new Signal(t, todayEntry.date, SignalEnum.S_RSI_70);
            } else if (prevRSI > 80 && todayRSI < 80) {
                res = new Signal(t, todayEntry.date, SignalEnum.S_RSI_80);
            } else if  (prevRSI > 80 && todayRSI > 80)
            {
                if (todayRSI < prevRSI)
                {
                    res = new Signal(t, todayEntry.date, SignalEnum.S_RSI_80_CHANGED_TREND);
                }
            }
            else if  (prevRSI > 70 && todayRSI > 70)
            {
                if (todayRSI < prevRSI)
                {
                    res = new Signal(t, todayEntry.date, SignalEnum.S_RSI_70_CHANGED_TREND);
                }
            }

        }


        return res;
    }
}
