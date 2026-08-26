/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Vector;
import model.ClanItem;
import model.Language;
import model.PlayerInfo;
import shop.ShopBietDoi;

public final class OfflineTeamItems {
    public static final int HP = 0;
    public static final int ATTACK = 1;
    public static final int DEFENSE = 2;
    public static final int LUCK = 3;
    public static final int EXP = 4;
    public static final int COUNT = 5;
    private static final String[] NAMES = new String[]{"Sinh l\u1ef1c \u0111\u1ed9i +10%", "S\u1ee9c m\u1ea1nh \u0111\u1ed9i +10%", "Ph\u00f2ng th\u1ee7 \u0111\u1ed9i +15%", "May m\u1eafn \u0111\u1ed9i +20%", "Kinh nghi\u1ec7m \u0111\u1ed9i x3"};
    private static final int[] XU = new int[]{20000, 30000, 30000, 40000, 50000};
    private static final int[] LUONG = new int[]{10, 15, 15, 20, 25};
    private static final byte[] HOURS = new byte[]{24, 24, 24, 24, 12};
    private static final byte[] LEVEL = new byte[]{1, 5, 5, 10, 10};
    private static long[] expiresAt = new long[5];
    private static final int EXP_TEAM_MULTIPLIER = 3;
    private static final int EXP_CARD_MULTIPLIER = 2;
    private static long expCardExpiresAt;

    private OfflineTeamItems() {
    }

    public static void reset() {
        expiresAt = new long[5];
        expCardExpiresAt = 0L;
    }

    public static boolean isActive(int n) {
        return n >= 0 && n < 5 && expiresAt[n] > System.currentTimeMillis();
    }

    public static int remainingHours(int n) {
        if (!OfflineTeamItems.isActive(n)) {
            return 0;
        }
        long l = expiresAt[n] - System.currentTimeMillis();
        return (int)((l + 3599999L) / 3600000L);
    }

    public static int expMultiplier() {
        int n = 0;
        if (OfflineTeamItems.isActive(4)) {
            n += 3;
        }
        if (OfflineTeamItems.isExpCardActive()) {
            n += 2;
        }
        return n == 0 ? 1 : n;
    }

    public static boolean isExpCardActive() {
        return expCardExpiresAt > System.currentTimeMillis();
    }

    public static int expCardRemainingHours() {
        if (!OfflineTeamItems.isExpCardActive()) {
            return 0;
        }
        long l = expCardExpiresAt - System.currentTimeMillis();
        return (int)((l + 3599999L) / 3600000L);
    }

    static int activateExpCard(int n) {
        long l = System.currentTimeMillis();
        long l2 = expCardExpiresAt > l ? expCardExpiresAt : l;
        expCardExpiresAt = l2 + (long)n * 3600000L;
        return OfflineTeamItems.expCardRemainingHours();
    }

    public static int applyStat(int n, int n2) {
        int n3 = 0;
        if (n == 0 && OfflineTeamItems.isActive(0)) {
            n3 = 10;
        } else if (n == 1 && OfflineTeamItems.isActive(1)) {
            n3 = 10;
        } else if (n == 2 && OfflineTeamItems.isActive(2)) {
            n3 = 15;
        } else if (n == 3 && OfflineTeamItems.isActive(3)) {
            n3 = 20;
        }
        return n2 + n2 * n3 / 100;
    }

    public static void shop(byte by, byte by2, byte by3) {
        if (by == 0) {
            OfflineTeamItems.openShop();
        } else {
            OfflineTeamItems.buy(by2, by3);
        }
    }

    private static void openShop() {
        Vector<ClanItem> vector = new Vector<ClanItem>();
        for (int i = 0; i < 5; ++i) {
            ClanItem clanItem = new ClanItem();
            clanItem.id = (byte)i;
            clanItem.name = NAMES[i] + (OfflineTeamItems.isActive(i) ? " (c\u00f2n " + OfflineTeamItems.remainingHours(i) + "h)" : "");
            clanItem.xu = XU[i];
            clanItem.luong = LUONG[i];
            clanItem.expDate = OfflineTeamItems.isActive(i) ? (byte)OfflineTeamItems.displayHours(i) : HOURS[i];
            clanItem.levelRequire = LEVEL[i];
            vector.addElement(clanItem);
        }
        if (CCanvas.shopBietDoi == null) {
            CCanvas.shopBietDoi = new ShopBietDoi();
        }
        CCanvas.shopBietDoi.setItems(vector);
        CCanvas.shopBietDoi.show();
    }

    private static void buy(byte by, byte by2) {
        ClanItem clanItem;
        int n;
        int n2 = by2 & 0xFF;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null || n2 < 0 || n2 >= 5) {
            return;
        }
        if (playerInfo.level2 < LEVEL[n2]) {
            CCanvas.startOKDlg("C\u1ea7n \u0111\u1ea1t level " + LEVEL[n2] + " \u0111\u1ec3 mua item n\u00e0y.");
            return;
        }
        int n3 = n = by == 1 ? LUONG[n2] : XU[n2];
        if (by == 1 ? playerInfo.luong < n : playerInfo.xu < n) {
            CCanvas.startOKDlg(Language.kocotien());
            return;
        }
        if (by == 1) {
            playerInfo.luong -= n;
        } else {
            playerInfo.xu -= n;
        }
        long l = System.currentTimeMillis();
        long l2 = expiresAt[n2] > l ? expiresAt[n2] : l;
        OfflineTeamItems.expiresAt[n2] = l2 + (long)HOURS[n2] * 3600000L;
        if (CCanvas.shopBietDoi != null && CCanvas.shopBietDoi.items != null && (clanItem = CCanvas.shopBietDoi.getClanItem(by2)) != null) {
            clanItem.name = NAMES[n2] + " (c\u00f2n " + OfflineTeamItems.remainingHours(n2) + "h)";
            clanItem.expDate = (byte)OfflineTeamItems.displayHours(n2);
        }
        playerInfo.maxHP = OfflineEquipmentStats.maxHp(playerInfo);
        OfflineSave.save();
        CCanvas.startOKDlg("\u0110\u00e3 k\u00edch ho\u1ea1t " + NAMES[n2] + " trong " + HOURS[n2] + " gi\u1edd.");
    }

    private static int displayHours(int n) {
        int n2 = OfflineTeamItems.remainingHours(n);
        return n2 > 127 ? 127 : n2;
    }

    static void write(DataOutputStream dataOutputStream) throws IOException {
        for (int i = 0; i < 5; ++i) {
            dataOutputStream.writeLong(expiresAt[i]);
        }
    }

    static void read(DataInputStream dataInputStream) throws IOException {
        for (int i = 0; i < 5; ++i) {
            OfflineTeamItems.expiresAt[i] = dataInputStream.readLong();
        }
    }

    static void writeExpCard(DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeLong(expCardExpiresAt);
    }

    static void readExpCard(DataInputStream dataInputStream) throws IOException {
        expCardExpiresAt = dataInputStream.readLong();
    }

    public static void editorSetRemainingHours(int n, int n2) {
        if (n < 0 || n >= COUNT) {
            throw new IllegalArgumentException("index");
        }
        if (n2 < 0) {
            n2 = 0;
        }
        if (n2 > 8760) {
            n2 = 8760;
        }
        if (n2 == 0) {
            OfflineTeamItems.expiresAt[n] = 0L;
            return;
        }
        OfflineTeamItems.expiresAt[n] = System.currentTimeMillis() + (long)n2 * 3600000L;
    }

    public static void editorSetExpCardRemainingHours(int n) {
        if (n < 0) {
            n = 0;
        }
        if (n > 8760) {
            n = 8760;
        }
        if (n == 0) {
            expCardExpiresAt = 0L;
            return;
        }
        expCardExpiresAt = System.currentTimeMillis() + (long)n * 3600000L;
    }
}

