package com.data.ta;

public enum SMA {

	SMA15(15), SMA30(30), SMA45(45), SMA50(50), SMA200(200);

	private int value;

	SMA(int v) {
		value = v;
	}

	public int value() {
		return this.value;
	}

}
