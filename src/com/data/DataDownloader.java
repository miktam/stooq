package com.data;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.apache.log4j.Logger;

public class DataDownloader {

	private static final Logger logger = Logger.getLogger(DataDownloader.class);

	private final String bossaDataInZip = "http://bossa.pl/pub/metastock/mstock/mstall.zip";

	private final String dataDirectory = "C:\\DEV\\stooq\\data\\";
	private final String filenameStoredLocally = dataDirectory + "stooq.zip";
	private final String extractedFilesDirectory = dataDirectory + "EXTRACTED\\";

	private String contentType;
	private int contentLength;
	private long now;

	// set to true if there is new file from internet
	private boolean filesAreFreshUnzipThem = false;

	public List<String> downloadData() throws Exception {
		logger.trace("start download");

		now = s();
		URLConnection urlConnection = readFromUrl(bossaDataInZip);
		now = f("create url connection", now);

		String filename;
		try {
			filename = writeUrlLocally(urlConnection);
			now = f("wrote file locally", now);
		} catch (java.net.UnknownHostException e) {

			logger.warn(e);
			logger.warn("try to use cached data in :" + filenameStoredLocally);
			filename = filenameStoredLocally;
		}

		List<String> extractedFiles = unzipFile(filename);
		now = f("extracted files", now);

		return extractedFiles;
	}

	private String writeUrlLocally(URLConnection urlConnection) throws IOException, FileNotFoundException,
			UnknownHostException {

		long sizeOfStoredFile = new File(filenameStoredLocally).length();
		if (sizeOfStoredFile == contentLength) {
			logger.warn("USE CACHE! stored file size: " + sizeOfStoredFile + " is equals to urlContentLengh");

		} else {

			filesAreFreshUnzipThem = true;
			InputStream raw = urlConnection.getInputStream();
			InputStream in = new BufferedInputStream(raw);
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
				throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
			}

			logger.trace("Just read bytes from network: " + offset);

			FileOutputStream out = new FileOutputStream(filenameStoredLocally);
			out.write(data);
			out.flush();
			out.close();

			now = f("wrote all bytes", now);

			logger.trace("Read from network: " + out.toString());
		}
		return filenameStoredLocally;
	}

	private List<String> unzipFile(String path) {
		int BUFFER = 1024;
		List<String> extractedFiles = new ArrayList<String>();
		try {
			BufferedOutputStream dest = null;
			BufferedInputStream is = null;
			ZipEntry entry;
			ZipFile zipfile = new ZipFile(path);
			Enumeration<? extends ZipEntry> e = zipfile.entries();

			int howManyEntriesInZip = zipfile.size();
			logger.trace("files in zip: " + howManyEntriesInZip);
			File directoryWhereFilesWillBeCreated = new File(extractedFilesDirectory);
			int alreadyInDir = directoryWhereFilesWillBeCreated.list().length;
			if (alreadyInDir == howManyEntriesInZip && filesAreFreshUnzipThem == false) {
				logger.warn("CACHE unzipping!: already " + howManyEntriesInZip + " files in directory");
				List<File> filesCached = Arrays.asList(directoryWhereFilesWillBeCreated.listFiles());
				for (File f : filesCached)
					extractedFiles.add(f.getAbsolutePath());
			} else {

				while (e.hasMoreElements()) {
					entry = (ZipEntry) e.nextElement();
					is = new BufferedInputStream(zipfile.getInputStream(entry));
					int count;
					byte data[] = new byte[BUFFER];
					String filePath = entry.getName();
					logger.trace("extract file: " + filePath);
					extractedFiles.add(extractedFilesDirectory + filePath);
					FileOutputStream fos = new FileOutputStream(extractedFilesDirectory + filePath);
					dest = new BufferedOutputStream(fos, BUFFER);
					while ((count = is.read(data, 0, BUFFER)) != -1) {
						dest.write(data, 0, count);
					}
					dest.flush();
					dest.close();
					is.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return extractedFiles;

	}

	private URLConnection readFromUrl(String url) throws MalformedURLException, IOException {
		logger.trace("get tickers from net");

		setProxyIfNeeded(url);

		URL u = new URL(url);
		URLConnection uc = u.openConnection();
		contentType = uc.getContentType();
		contentLength = uc.getContentLength();
		logger.debug("content type: " + contentType + " and lenght: " + contentLength);

		return uc;
	}

	private static void setProxyIfNeeded(String url) {
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			InputStream raw = uc.getInputStream();

			logger.info("no need to set proxy");
		} catch (Exception e) {
			logger.info("set proxy!");
			System.setProperty("http.proxyHost", "wwwgate0.mot.com");
			System.setProperty("http.proxyPort", "1080");
		}
	}

	private long s() {
		return System.currentTimeMillis();
	}

	private long f(String operation, long s) {
		long took = System.currentTimeMillis() - s;
		logger.info("[" + took + "] ms for " + operation);

		return System.currentTimeMillis();
	}

}
