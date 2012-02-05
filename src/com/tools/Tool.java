package com.tools;

import java.text.DecimalFormat;

import org.apache.log4j.Logger;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Tool {
	
	private static Logger logger = Logger.getLogger(Tool.class);
	
	/**
	 * print array of doubles
	 * @param arr
	 */
	public static void pa(double[] arr)
	{
		logger.info("print array with size: " + arr.length);
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			sb.append(arr[i]);
			sb.append(",");
		}
		
		logger.info(sb.toString());
	}
	
	public static DateTimeFormatter dmy()
	{
		return  DateTimeFormat.forPattern("dd-MM-yyyy");
	}
	
	/**
	 * Date.toString(Tool.dm())
	 * @return date format: dd-mm
	 */
	public static DateTimeFormatter dm()
	{
		return  DateTimeFormat.forPattern("dd-MM");
	}
	
	/**
	 * list decimal as 22.22
	 * @return as Tool.df().format(DOUBLE))
	 */
	public static DecimalFormat df()
	{
		return new DecimalFormat("##.##");
	}
	
	/**
	 * format big numbers
	 * @return
	 */
	public static DecimalFormat dfBig()
	{
		DecimalFormat df = new DecimalFormat("##");
		df.setGroupingSize(3);
		return df;
	}

	
	/**
	 * Date.toString(Tool.dm())
	 * @return date format: dd
	 */
	public static DateTimeFormatter d() {
		return  DateTimeFormat.forPattern("dd");
	}

}
