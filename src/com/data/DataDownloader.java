package com.data;

import java.util.List;

import com.data.ticker.Ticker;

public interface DataDownloader {

	List<Ticker> downloadData() throws Exception;
	
}
