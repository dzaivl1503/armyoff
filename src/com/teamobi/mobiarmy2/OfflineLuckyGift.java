/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.mImage;
import Equipment.Equip;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineSpecialShop;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.CRes;
import model.CTime;
import model.Font;
import model.LuckyGift;
import model.PlayerInfo;
import screen.LuckyGifrScreen;
import screen.PrepareScr;
import shop.OfflineShopEquip;

public final class OfflineLuckyGift {
    private static final int TIME_SECONDS = 15;
    private static final int MAX_OPENS = 2;
    private static final int AUTO_OPEN_INTERVAL = 20;
    private static boolean pending;
    private static boolean autoOpening;
    private static int autoTick;
    private static int opensUsed;
    private static boolean[] realOpen;
    private static final int XU_MIN = 1000;
    private static final int XU_MAX = 50000;
    private static final int XU_STEP = 1000;
    private static Equip xuTemplate;

    private OfflineLuckyGift() {
    }

    public static void arm() {
        pending = true;
    }

    public static boolean consumePending() {
        boolean bl = pending;
        pending = false;
        return bl;
    }

    public static boolean isRealOpen(int n) {
        return n >= 0 && n < realOpen.length && realOpen[n];
    }

    public static void startAfterWin() {
        autoOpening = false;
        autoTick = 0;
        opensUsed = 0;
        realOpen = new boolean[12];
        LuckyGifrScreen.info = Font.normalFont.splitFontBStrInLine("B\u1ea1n c\u00f3 2 l\u01b0\u1ee3t ch\u1ecdn qu\u00e0 mi\u1ec5n ph\u00ed!", CCanvas.width - 80);
        LuckyGifrScreen.time = new CTime();
        LuckyGifrScreen.time.initTimeInterval((byte)15);
        LuckyGifrScreen.time.resetTime();
        if (CCanvas.luckyGifrScreen == null) {
            CCanvas.luckyGifrScreen = new LuckyGifrScreen();
        }
        CCanvas.luckyGifrScreen.isShow = false;
        CCanvas.luckyGifrScreen.show();
    }

    public static void onTimeExpired() {
        autoOpening = true;
    }

    public static void tick(LuckyGifrScreen luckyGifrScreen) {
        if (!autoOpening || luckyGifrScreen.isShow) {
            return;
        }
        if (++autoTick < 20) {
            return;
        }
        autoTick = 0;
        int n = OfflineLuckyGift.nextUnopened(luckyGifrScreen);
        if (n < 0) {
            autoOpening = false;
            return;
        }
        OfflineLuckyGift.revealBox(luckyGifrScreen, (byte)n, opensUsed < 2);
    }

    private static int nextUnopened(LuckyGifrScreen luckyGifrScreen) {
        for (int i = 0; i < luckyGifrScreen.num; ++i) {
            if (luckyGifrScreen.giftDelete[i] == -1) continue;
            return i;
        }
        return -1;
    }

    public static void openBox(LuckyGifrScreen luckyGifrScreen, byte by) {
        if (by < 0 || by >= luckyGifrScreen.num || luckyGifrScreen.giftDelete[by] == -1 || opensUsed >= 2) {
            return;
        }
        OfflineLuckyGift.revealBox(luckyGifrScreen, by, true);
        if (opensUsed >= 2) {
            autoOpening = true;
            autoTick = 0;
        }
    }

    private static void revealBox(LuckyGifrScreen luckyGifrScreen, byte by, boolean bl) {
        luckyGifrScreen.giftDelete[by] = -1;
        LuckyGift luckyGift = new LuckyGift();
        luckyGift.id = by;
        luckyGift.isWait = true;
        luckyGift.isServerSend = true;
        OfflineLuckyGift.rollReward(luckyGift, bl);
        if (bl) {
            ++opensUsed;
            OfflineLuckyGift.realOpen[by] = true;
        }
        luckyGifrScreen.setGiftByItemID(luckyGift);
    }

    private static Equip xuTemplate() {
        if (xuTemplate == null) {
            xuTemplate = new Equip();
            OfflineLuckyGift.xuTemplate.isMaterial = true;
            OfflineLuckyGift.xuTemplate.materialIcon = mImage.createImage("/icon/xu_icon.png");
        }
        return xuTemplate;
    }

    private static void rollReward(LuckyGift luckyGift, boolean bl) {
        Vector vector;
        int n = CRes.random(0, 3);
        if (n == 0) {
            OfflineLuckyGift.rollXu(luckyGift, bl);
            return;
        }
        Vector vector2 = vector = n == 1 ? OfflineShopEquip.buildShopItems() : OfflineSpecialShop.buildCatalogItems();
        if (vector.size() == 0) {
            OfflineLuckyGift.rollXu(luckyGift, bl);
            return;
        }
        Equip equip = (Equip)vector.elementAt(CRes.random(0, vector.size()));
        equip.num = 1;
        luckyGift.rewardIcon = equip;
        luckyGift.info = equip.name;
        if (!bl) {
            return;
        }
        if (equip.isMaterial) {
            OfflineSpecialShop.addMaterial((byte)equip.id, 1);
        } else {
            Equip equip2 = PlayerEquip.createEquip(equip.glass, equip.type, equip.id);
            if (equip2 != null) {
                equip2.glass = equip.glass;
                equip2.type = equip.type;
                equip2.name = equip.name;
                equip2.dbKey = OfflineChest.nextDbKey();
                equip2.date = (byte)-1;
                equip2.slot = (byte)equip2.socketCount();
                OfflineChest.add(equip2);
            }
        }
        OfflineSave.save();
    }

    private static void rollXu(LuckyGift luckyGift, boolean bl) {
        int n = 50;
        int n2 = 1000 + CRes.random(0, n) * 1000;
        luckyGift.rewardIcon = OfflineLuckyGift.xuTemplate();
        luckyGift.info = OfflineLuckyGift.formatXu(n2) + " xu";
        if (!bl) {
            return;
        }
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo != null) {
            playerInfo.xu += n2;
        }
        OfflineSave.save();
    }

    private static String formatXu(int n) {
        if (n >= 1000) {
            return n / 1000 + "K";
        }
        return String.valueOf(n);
    }

    public static void finish() {
        PrepareScr.isPvpBotRoom = false;
        GameMidlet.openPvpBotSetup();
    }

    static {
        realOpen = new boolean[12];
    }
}

