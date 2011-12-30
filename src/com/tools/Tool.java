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
	
	public static DateTimeFormatter dm()
	{
		return  DateTimeFormat.forPattern("dd-MM");
	}
	
	/**
	 * list decimal as 22.22
	 * @return
	 */
	public static DecimalFormat df()
	{
		return new DecimalFormat("##.##");
	}

}
