/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import coreLG.TerrainMidlet;
import model.CRes;
import model.PlayerInfo;
import screen.PrepareScr;

public final class OfflinePvpBot {
    public static final byte DIFFICULTY_EASY = 0;
    public static final byte DIFFICULTY_NORMAL = 1;
    public static final byte DIFFICULTY_HARD = 2;
    public static final byte DIFFICULTY_SUPER_HARD = 3;
    public static final int MAX_BOT_COUNT = 4;
    public static int pendingMapId = -1;
    public static int pendingBotCount = 1;
    public static byte pendingDifficulty = 1;
    public static int pendingSquadCount = 1;
    public static int activeSquadCount = 1;
    public static int activeBotCount;
    public static byte activeDifficulty;
    private static final int BOT_IDDB_BASE = -3000;
    private static final byte[] SUPER_HARD_GUN_POOL;
    private static final int[] WIN_XU;
    private static final int[] WIN_LUONG;
    private static final int[] WIN_EXP_PER_BOT;

    private OfflinePvpBot() {
    }

    public static void resetPending() {
        pendingMapId = -1;
        pendingBotCount = 1;
        pendingDifficulty = 1;
    }

    public static String difficultyName(byte by) {
        if (by == 0) {
            return "D\u1ec5";
        }
        if (by == 2) {
            return "Kh\u00f3";
        }
        if (by == 3) {
            return "Si\u00eau kh\u00f3";
        }
        return "Th\u01b0\u1eddng";
    }

    public static PlayerInfo generateBot(int n, byte by) {
        int n2;
        int n3;
        int n4;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        PlayerInfo playerInfo2 = new PlayerInfo();
        playerInfo2.IDDB = -3000 - n;
        byte by2 = by == 3 ? SUPER_HARD_GUN_POOL[CRes.random(0, SUPER_HARD_GUN_POOL.length)] : (byte)CRes.random(0, 11);
        playerInfo2.gun = by2;
        if (by == 3 && playerInfo != null) {
            n4 = OfflinePvpBot.strongestTeamLevel(playerInfo);
            n3 = 40 + CRes.random(0, 21);
            n2 = n4 + Math.max(1, n4 * n3 / 100);
        } else if (by == 2 && playerInfo != null) {
            n4 = OfflinePvpBot.strongestTeamLevel(playerInfo);
            n3 = 10 + CRes.random(0, 6);
            n2 = n4 + Math.max(1, n4 * n3 / 100);
        } else {
            n4 = 1;
            if (playerInfo != null) {
                int n5 = n4 = playerInfo.classLevel2[by2] > 0 ? playerInfo.classLevel2[by2] : playerInfo.level2;
            }
            if (n4 < 1) {
                n4 = 1;
            }
            n2 = n4 + OfflinePvpBot.levelOffset(by);
        }
        if (n2 < 1) {
            n2 = 1;
        }
        playerInfo2.level2 = n2;
        playerInfo2.name = "Bot " + OfflinePvpBot.botScreenName(by2);
        playerInfo2.isReady = true;
        n4 = n2 * 10;
        n3 = by == 3 ? 50 : 35;
        int n6 = n4 * n3 / 100;
        int n7 = n4 * 25 / 100;
        int n8 = n4 - n6 - n7;
        int n9 = n8 > 0 ? CRes.random(0, n8 + 1) : 0;
        int n10 = (n8 -= n9) > 0 ? CRes.random(0, n8 + 1) : 0;
        int n11 = n8 -= n10;
        playerInfo2.ability[0] = (short)n6;
        playerInfo2.ability[1] = (short)n7;
        playerInfo2.ability[2] = (short)n9;
        playerInfo2.ability[3] = (short)n10;
        playerInfo2.ability[4] = (short)n11;
        playerInfo2.ensureCombatEquip();
        playerInfo2.maxHP = OfflineEquipmentStats.maxHp(playerInfo2);
        return playerInfo2;
    }

    private static int strongestTeamLevel(PlayerInfo playerInfo) {
        int n;
        int n2 = n = playerInfo.classLevel2[playerInfo.gun] > 0 ? playerInfo.classLevel2[playerInfo.gun] : playerInfo.level2;
        if (n < 1) {
            n = 1;
        }
        for (int i = 0; i < playerInfo.squadExtra.length; ++i) {
            int n3;
            byte by = playerInfo.squadExtra[i];
            if (by < 0 || by >= playerInfo.classLevel2.length) continue;
            int n4 = n3 = playerInfo.classLevel2[by] > 0 ? playerInfo.classLevel2[by] : playerInfo.level2;
            if (n3 <= n) continue;
            n = n3;
        }
        return n;
    }

    private static int levelOffset(byte by) {
        if (by == 0) {
            return CRes.random(-4, 0);
        }
        if (by == 2) {
            return CRes.random(0, 7);
        }
        if (by == 3) {
            return CRes.random(5, 15);
        }
        return CRes.random(-2, 4);
    }

    private static String botScreenName(byte by) {
        if (by >= 0 && by < PrepareScr.GUN_NAME.length) {
            return PrepareScr.GUN_NAME[by];
        }
        return "?";
    }

    public static int winXuReward(byte by) {
        return by >= 0 && by < WIN_XU.length ? WIN_XU[by] : 0;
    }

    public static int winLuongReward(byte by) {
        return by >= 0 && by < WIN_LUONG.length ? WIN_LUONG[by] : 0;
    }

    public static int winExpReward(int n, byte by) {
        int n2 = by >= 0 && by < WIN_EXP_PER_BOT.length ? WIN_EXP_PER_BOT[by] : 0;
        return n2 * n;
    }

    static {
        activeDifficulty = 1;
        SUPER_HARD_GUN_POOL = new byte[]{6, 9, 10};
        WIN_XU = new int[]{100, 1000, 10000, 1000000};
        WIN_LUONG = new int[]{10, 20, 30, 1000};
        WIN_EXP_PER_BOT = new int[]{100, 200, 300, 1000};
    }
}

