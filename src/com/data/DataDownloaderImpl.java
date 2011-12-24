package com.data;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;

public class DataDownloaderImpl implements DataDownloader {

	private final Logger logger = Logger.getLogger(DataDownloaderImpl.class);

	private String contentType;
	private int contentLength;
	private long now;

	@Override
	public boolean downloadData() throws Exception {
		logger.debug("start download");

		now = s();
		URLConnection urlConnection = readFromUrl("http://bossa.pl/pub/metastock/mstock/mstall.zip");
		now = f("create url connection", now);

		String filename = writeUrlLocally(urlConnection);
		
		logger.info("got filename: time to unzip: " + filename);

		return false;
	}

	private String writeUrlLocally(URLConnection urlConnection)
			throws IOException, FileNotFoundException {
		InputStream raw = urlConnection.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		logger.info("create buffer with size: " + contentLength);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (-1 == bytesRead)
				break;
			offset += bytesRead;
		}
		in.close();

		now = f("read all bytes", now);

		if (offset != contentLength) {
			throw new IOException("Only read " + offset + " bytes; Expected "
					+ contentLength + " bytes");
		}

		logger.info("Just read bytes from network: " + offset);

		String filename = "C:\\DEV\\stooq\\data\\stooq.zip";
		FileOutputStream out = new FileOutputStream(filename);
		out.write(data);
		out.flush();
		out.close();

		now = f("wrote all bytes", now);

		logger.info("Read from network: " + out.toString());
		return filename;
	}

	private long s() {
		return System.currentTimeMillis();
	}

	private long f(String operation, long s) {
		long took = System.currentTimeMillis() - s;
		logger.info(operation + " took [" + took + "]ms");

		return System.currentTimeMillis();
	}

	private URLConnection readFromUrl(String url) throws MalformedURLException,
			IOException {
		logger.info("get tickers from net");

		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		contentType = uc.getContentType();
		contentLength = uc.getContentLength();
		logger.info("content type: " + contentType + " and lenght: "
				+ contentLength);

		return uc;
	}

}
