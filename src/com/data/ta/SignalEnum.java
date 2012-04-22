package com.data.ta;

public enum SignalEnum {
	
	B_SMA15, B_SMA30, B_SMA45, B_SMA200,
	S_SMA15, S_SMA30, S_SMA45, S_SMA200,

    /**
     * broke 30 rsi lower
     */
    B_RSI_30,
    /**
     * broke 20 rsi lower
     */
    B_RSI_20,
    /**
     * lower then 30 rsi and changed trend from lowering to up
     */
    B_RSI_30_CHANGED_TREND,

    B_RSI_20_CHANGED_TREND,
    S_RSI_70, S_RSI_80,
    S_RSI_80_CHANGED_TREND,
    S_RSI_70_CHANGED_TREND,
    B_OR_S_VOL_5,
    B_OR_S_VOL_10,


    B_BREAK_CHANNEL_UP_26_WEEKS,
    S_BREAK_CHANNEL_DOWN_26_WEEKS,

    B_BREAK_CHANNEL_UP_13_WEEKS,
    S_BREAK_CHANNEL_DOWN_13_WEEKS;

}
