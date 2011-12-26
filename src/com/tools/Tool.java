package com.tools;

import org.apache.log4j.Logger;

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

}
