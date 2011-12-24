package com.data;

import static org.junit.Assert.*;


import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class DataDownloaderTest {
	
	private final static Logger logger = Logger.getLogger(DataDownloaderTest.class);
	
	
	static DataDownloader dd;
	
	@BeforeClass
	public static void beforeClass()
	{
		BasicConfigurator.configure();
		dd = new DataDownloaderImpl();
		logger.info("instantiated dd: " + dd);
	}

	@Test
	public void test() throws Exception {
		
		
		assertThat(dd.downloadData(), CoreMatchers.is(false));
		
	}

}
