package com.data;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import com.tictactec.ta.lib.Core;
import com.tictactec.ta.lib.MInteger;
import com.tictactec.ta.lib.RetCode;
import com.tools.Tool;

public class TechnicalATest {

	private final static Logger logger = Logger.getLogger(TechnicalATest.class);
	static DataDownloader dd;

	private double input[];
	private int inputInt[];
	private double output[];
	private int outputInt[];
	private MInteger outBegIdx;
	private MInteger outNbElement;
	private RetCode retCode;
	private Core lib;
	private int lookback;

	static public double[] close = new double[252];

//	@Test
//	public void test_MACD_KGHM() throws Exception {
//
//		dd.downloadData();
//		String kghm = "KGHM";
//		TickerManager.ins();
//		close = TickerManager.getCloseFor(kghm, 252);
//		logger.info("lenght of close = " + close.length);
//		double macd[] = new double[close.length];
//		double signal[] = new double[close.length];
//		double hist[] = new double[close.length];
//		lookback = lib.macdLookback(15, 26, 9);
//		retCode = lib.macd(0, close.length - 1, close, 15, 26, 9, outBegIdx,
//				outNbElement, macd, signal, hist);
//
//		double ema15[] = new double[close.length];
//		lookback = lib.emaLookback(15);
//		retCode = lib.ema(0, close.length - 1, close, 15, outBegIdx,
//				outNbElement, ema15);
//
//		double ema26[] = new double[close.length];
//		lookback = lib.emaLookback(26);
//		retCode = lib.ema(0, close.length - 1, close, 26, outBegIdx,
//				outNbElement, ema26);
//
//		logger.info("ema 15");
//		pa(ema15);
//		pa(ema26);
//
//		pa(macd);
//		
//		pa(signal);
//
//		// TODO Add tests of outputs
//	}

	public static void pa(double[] arr) {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		for (int i = 0; i < arr.length; i++) {
			StringBuilder b = new StringBuilder();
			double value = arr[i];
			if (value != 0) {
				b.append(value);
				sb.append(b.subSequence(0, b.indexOf(".") + 3) + ",");
			}
		}
		logger.info(sb.toString());
	}

	@Before
	public void setUp() {
		BasicConfigurator.configure();
		dd = new DataDownloaderImpl();
		lib = new Core();
		input = new double[200];
		inputInt = new int[200];
		output = new double[200];
		outputInt = new int[200];
		outBegIdx = new MInteger();
		outNbElement = new MInteger();
		for (int i = 0; i < input.length; i++) {
			input[i] = (double) i;
			inputInt[i] = i;
		}
		for (int i = 0; i < output.length; i++) {
			output[i] = (double) -999999.0;
			outputInt[i] = -999999;
		}
		outBegIdx.value = -1;
		outNbElement.value = -1;
		retCode = RetCode.InternalError;
		lookback = -1;
	}

}
