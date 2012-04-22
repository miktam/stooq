package com.tools;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.data.ticker.EntryDayTicker;
import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Tool {

    private static Logger logger = Logger.getLogger(Tool.class);


    /**
     * extract close prices
     */
    public static List<Double> ePriceClose(List<EntryDayTicker> edt)
    {
        List<Double> result = new ArrayList<Double>();
        
        for (EntryDayTicker e: edt)
        {
            result.add(e.close);
        }

        return result;
    }

    /**
     * print array of doubles
     */
    public static void pa(double[] arr) {
        logger.info("print array with size: " + arr.length);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            sb.append(",");
        }

        logger.info(sb.toString());
    }

    public static DateTimeFormatter dmy() {
        return DateTimeFormat.forPattern("dd-MM-yyyy");
    }

    /**
     * Date.toString(Tool.dm())
     *
     * @return date format: dd-mm
     */
    public static DateTimeFormatter dm() {
        return DateTimeFormat.forPattern("dd-MM");
    }

    /**
     * list decimal as 22.22 in simple mode - avoid stack overflow
     *
     * @return as Tool.df().format(DOUBLE))
     */
    public static String p_s(double d) {
        return new DecimalFormat("##.##").format(d);
    }

    /**
     * print doubles nicely
     */
    public static String p(double d) {
        String result;

        if (d < 1000) {
            result = Tool.p(d);
        } else {
            Double doubleToFormat = d;
            Integer bigInt = doubleToFormat.intValue();
            String tmpRes = bigInt.toString();
            if (tmpRes.length() >= 7) {
                String trailingMillion = tmpRes.substring(0, tmpRes.length() - 6);
                result = trailingMillion + "M";
            } else {
                String trailingK = tmpRes.substring(0, tmpRes.length() - 3);
                result = trailingK + "K";
            }
        }

        return result;
    }


    /**
     * Date.toString(Tool.dm())
     *
     * @return date format: dd
     */
    public static DateTimeFormatter d() {
        return DateTimeFormat.forPattern("dd");
    }

}
