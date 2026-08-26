/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.RMS;
import CLib.mSystem;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.TemCanvas;
import network.Message;

public class TemMidlet {
    public static TemCanvas temCanvas;
    public static TemMidlet instance;
    public static byte DIVICE;
    public static final byte NONE = 0;
    public static final byte NOKIA_STORE = 1;
    public static final byte GOOGLE_STORE = 2;
    public static byte currentIAPStore;
    public static boolean isBlockNOKIAStore;
    public static byte langServer;
    public static final String[] productIds;
    public static String[] listGems;
    public static final String[] google_productIds;
    public static String[] google_listGems;

    public TemMidlet() {
        instance = this;
        temCanvas = new TemCanvas();
        temCanvas.start();
    }

    protected void destroyApp(boolean bl) {
    }

    public static void makePurchase(String string) {
    }

    protected void pauseApp() {
    }

    protected void startApp() {
    }

    public void destroy() {
        GameMidlet.instance.notifyDestroyed();
    }

    public static byte[] encoding(byte[] byArray) {
        if (byArray != null) {
            for (int i = 0; i < byArray.length; ++i) {
                byArray[i] = (byte)~byArray[i];
            }
        }
        return byArray;
    }

    public static byte[] loadRMS(String string) {
        return RMS.loadRMS(string);
    }

    public static void openUrl(String string) {
        mSystem.openUrl(string);
    }

    public static void delRMS() {
    }

    public static String connectHTTP(String string) {
        return mSystem.connectHTTP(string);
    }

    public static void handleMessage(Message message) {
    }

    public void call(String string) {
    }

    public static void submitPurchase() {
    }

    public static void handleAllMessage(Message message) {
    }

    static {
        DIVICE = (byte)2;
        currentIAPStore = 0;
        isBlockNOKIAStore = true;
        langServer = 0;
        productIds = new String[]{"1311457"};
        listGems = new String[]{"24 Gems"};
        google_productIds = new String[]{"hs_gold_10_2", "hs_gold_30_2", "hs_gold_70_2", "hs_gold_180_2", "hs_gold_380_2", "hs_gold_800_2"};
        google_listGems = new String[]{"24 Gems ($0.99)", "84 Gems ($2.99)", "150 Gems ($4.99)", "350 Gems ($9.99)", "1.000 Gems ($24.99)", "2.500 Gems ($49.99)"};
    }
}

