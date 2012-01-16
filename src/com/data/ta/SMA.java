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

    public SignalEnum SMA2Signal(BuySell bs) {
        SignalEnum s = null;
        switch (this) {
            case SMA15:
                if (bs.equals(BuySell.BUY))
                    s = SignalEnum.B_SMA15;
                else
                    s = SignalEnum.S_SMA15;
                break;
            case SMA30:
                if (bs.equals(BuySell.BUY))
                    s = SignalEnum.B_SMA30;
                else
                    s = SignalEnum.S_SMA30;
                break;
            case SMA45:
                if (bs.equals(BuySell.BUY))
                    s = SignalEnum.B_SMA45;
                else
                    s = SignalEnum.S_SMA45;
                break;
            case SMA200:
                if (bs.equals(BuySell.BUY))
                    s = SignalEnum.B_SMA200;
                else
                    s = SignalEnum.S_SMA200;
                break;
        }

        return s;
    }
}
