/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineBossFight;
import com.teamobi.mobiarmy2.OfflineSpecialShop;
import coreLG.TerrainMidlet;
import model.CRes;
import model.PlayerInfo;

public final class OfflineBossReward {
    public static final String[] TIER_NAMES = new String[]{"D", "C", "B", "A", "S"};
    private static final byte MAT_NHOM = 62;
    private static final byte MAT_SAT = 63;
    private static final byte MAT_DONG = 64;
    private static final byte MAT_BAC = 65;
    private static final byte MAT_VANG = 66;
    private static final byte MAT_LONGVU = 67;
    private static final byte MAT_GO = 68;
    private static final byte ITEM_X2_EXP_CARD = 54;
    private static String lastRewardText = "";

    private OfflineBossReward() {
    }

    public static int tierOf(byte by) {
        int n = by / 2;
        if (n < 0) {
            return 0;
        }
        if (n > 4) {
            return 4;
        }
        return n;
    }

    public static String tierName(byte by) {
        return TIER_NAMES[OfflineBossReward.tierOf(by)];
    }

    public static String describeRewards(byte by) {
        int n = OfflineBossReward.tierOf(by);
        int n2 = OfflineBossFight.winXuReward(by);
        int n3 = OfflineBossFight.winLuongReward(by);
        String string = "Tier " + TIER_NAMES[n] + ": " + n2 + " xu, " + n3 + " l\u01b0\u1ee3ng\n";
        switch (n) {
            case 0: {
                return string + "1x Nh\u00f4m, 90% ng\u1ecdc c\u1ea5p 1-2";
            }
            case 1: {
                return string + "1x S\u1eaft (+20% Nh\u00f4m), 80% ng\u1ecdc c\u1ea5p 2-3";
            }
            case 2: {
                return string + "1x \u0110\u1ed3ng (+25% S\u1eaft), 75% ng\u1ecdc c\u1ea5p 3-4";
            }
            case 3: {
                return string + "1x B\u1ea1c (+35% \u0110\u1ed3ng), 50% ng\u1ecdc c\u1ea5p 5-6\n15% CT b\u1ea1c";
            }
        }
        return string + "1x V\u00e0ng, 1x L\u00f4ng v\u0169/G\u1ed7 (+40% B\u1ea1c)\n30% ng\u1ecdc c\u1ea5p 7-10, 25%/5% CT b\u1ea1c/v\u00e0ng\n10% B\u00ecnh KN/Th\u1ebb x2EXP";
    }

    public static String lastRewardText() {
        return lastRewardText;
    }

    public static void grantExtraRewards(byte by) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        StringBuffer stringBuffer = new StringBuffer();
        if (playerInfo == null) {
            lastRewardText = "";
            return;
        }
        int n = OfflineBossReward.tierOf(by);
        switch (n) {
            case 0: {
                OfflineBossReward.grant(stringBuffer, (byte)62, 1);
                if (!OfflineBossReward.roll(90)) break;
                OfflineBossReward.grantRandomGem(stringBuffer, 0, 2);
                break;
            }
            case 1: {
                OfflineBossReward.grant(stringBuffer, (byte)63, 1);
                if (OfflineBossReward.roll(20)) {
                    OfflineBossReward.grant(stringBuffer, (byte)62, 1);
                }
                if (!OfflineBossReward.roll(80)) break;
                OfflineBossReward.grantRandomGem(stringBuffer, 1, 3);
                break;
            }
            case 2: {
                OfflineBossReward.grant(stringBuffer, (byte)64, 1);
                if (OfflineBossReward.roll(25)) {
                    OfflineBossReward.grant(stringBuffer, (byte)63, 1);
                }
                if (!OfflineBossReward.roll(75)) break;
                OfflineBossReward.grantRandomGem(stringBuffer, 2, 4);
                break;
            }
            case 3: {
                OfflineBossReward.grant(stringBuffer, (byte)65, 1);
                if (OfflineBossReward.roll(35)) {
                    OfflineBossReward.grant(stringBuffer, (byte)64, 1);
                }
                if (OfflineBossReward.roll(50)) {
                    OfflineBossReward.grantRandomGem(stringBuffer, 4, 6);
                }
                if (!OfflineBossReward.roll(15)) break;
                OfflineBossReward.grantRandomFormula(stringBuffer, false);
                break;
            }
            default: {
                OfflineBossReward.grant(stringBuffer, (byte)66, 1);
                OfflineBossReward.grant(stringBuffer, CRes.random(0, 2) == 0 ? (byte)67 : 68, 1);
                if (OfflineBossReward.roll(40)) {
                    OfflineBossReward.grant(stringBuffer, (byte)65, 1);
                }
                if (OfflineBossReward.roll(30)) {
                    OfflineBossReward.grantRandomGem(stringBuffer, 6, 10);
                }
                if (OfflineBossReward.roll(25)) {
                    OfflineBossReward.grantRandomFormula(stringBuffer, false);
                }
                if (OfflineBossReward.roll(5)) {
                    OfflineBossReward.grantRandomFormula(stringBuffer, true);
                }
                if (!OfflineBossReward.roll(10)) break;
                if (CRes.random(0, 2) == 0) {
                    OfflineBossReward.grant(stringBuffer, (byte)90, 1);
                    break;
                }
                OfflineBossReward.grant(stringBuffer, (byte)54, 1);
            }
        }
        lastRewardText = stringBuffer.toString();
    }

    private static boolean roll(int n) {
        return CRes.random(0, 100) < n;
    }

    private static void grant(StringBuffer stringBuffer, byte by, int n) {
        OfflineSpecialShop.addMaterial(by, n);
        if (stringBuffer.length() > 0) {
            stringBuffer.append(", ");
        }
        stringBuffer.append(n).append("x ").append(OfflineSpecialShop.itemName(by));
    }

    private static void grantRandomGem(StringBuffer stringBuffer, int n, int n2) {
        int n3 = CRes.random(0, 5);
        int n4 = CRes.random(n, n2);
        OfflineBossReward.grant(stringBuffer, (byte)(n3 * 10 + n4), 1);
    }

    private static void grantRandomFormula(StringBuffer stringBuffer, boolean bl) {
        int n = CRes.random(0, 5);
        OfflineBossReward.grant(stringBuffer, (byte)((bl ? 69 : 57) + n), 1);
    }
}

