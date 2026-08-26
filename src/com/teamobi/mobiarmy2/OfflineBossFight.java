/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineBossAI;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import com.teamobi.mobiarmy2.OfflineGunAngles;
import coreLG.TerrainMidlet;
import map.MM;
import model.CRes;
import model.PlayerInfo;
import player.Boss;
import player.CPlayer;
import player.PM;
import screen.GameScr;

public final class OfflineBossFight {
    private static final byte[] BOSS_GUN = new byte[]{12, 12, 13, 14, 15, 16, 17, 22, 25, 26};
    private static final int[] WIN_XU = new int[]{500, 600, 650, 700, 1000, 1150, 1250, 1350, 1500, 2500};
    private static final int[] WIN_LUONG = new int[]{10, 15, 20, 25, 35, 40, 45, 50, 70, 100};
    private static final short[][] PLAYER_SPAWN_X = new short[][]{{490, 715, 427, 777, 586, 618, 522, 682}, {92, 345, 128, 309, 164, 273, 200, 235}, {681, 809, 553, 941, 425, 1068, 295, 1193}, {436, 524, 368, 592, 310, 650, 265, 695}, {100, 197, 68, 229, 135, 165, 162, 131}, {92, 798, 187, 663, 293, 580, 378, 469}, {48, 413, 490, 848, 511, 952, 565, 87}, {68, 447, 830, 203, 277, 688, 745, 600}, {54, 51, 56, 37, 83, 220, 278, 126}, {452, 681, 56, 589, 125, 220, 278, 512}};
    private static final short[][] PLAYER_SPAWN_Y = new short[][]{{269, 269, 301, 301, 237, 237, 269, 269}, {527, 527, 527, 527, 527, 527, 527, 527}, {432, 433, 398, 399, 369, 369, 336, 333}, {438, 438, 419, 419, 438, 438, 438, 438}, {383, 517, 383, 517, 517, 517, 383, 383}, {375, 316, 294, 391, 294, 275, 335, 277}, {231, 292, 150, 207, 294, 153, 185, 256}, {546, 492, 487, 547, 546, 488, 488, 489}, {65, 138, 220, 345, 345, 175, 174, 345}, {245, 248, 322, 117, 120, 277, 276, 362}};
    public static byte currentRoomIndex = (byte)-1;
    private static final int EXP_MULT_NUM = 2;
    private static final int EXP_MULT_DEN = 1;
    private static final int[] BOSS_HP = new int[]{1500, 1500, 4785, 3700, 15000, 4500, 1, 3800, 1800, 1800};
    private static final int[] BOSS_HP_PER_LEVEL = new int[]{10, 10, 15, 10, 10, 12, 0, 10, 10, 10};

    private OfflineBossFight() {
    }

    public static int bossExpReward(CPlayer cPlayer, byte by) {
        int n;
        if (cPlayer == null) {
            return 0;
        }
        switch (cPlayer.gun) {
            case 12: {
                n = 250;
                break;
            }
            case 11: {
                n = 100;
                break;
            }
            case 14: {
                n = 350;
                break;
            }
            case 13: {
                n = 300;
                break;
            }
            case 15: {
                n = 500;
                break;
            }
            case 16: {
                n = 275;
                break;
            }
            case 18:
            case 19:
            case 20:
            case 21: {
                n = 300;
                break;
            }
            case 22: {
                n = 500;
                break;
            }
            case 25:
            case 26: {
                n = 650;
                break;
            }
            default: {
                n = by >= 0 ? 250 : 0;
            }
        }
        return n * 2 / 1;
    }

    public static int winXuReward(byte by) {
        if (by < 0 || by >= WIN_XU.length) {
            return 0;
        }
        return WIN_XU[by];
    }

    public static int winLuongReward(byte by) {
        if (by < 0 || by >= WIN_LUONG.length) {
            return 0;
        }
        return WIN_LUONG[by];
    }

    public static void prepareBossInfo(byte by, PlayerInfo playerInfo) {
        if (playerInfo == null || by < 0 || by >= BOSS_GUN.length) {
            return;
        }
        playerInfo.gun = BOSS_GUN[by];
        playerInfo.isBoss = true;
        playerInfo.maxHP = OfflineBossFight.bossSpawnHp(by);
    }

    public static void finishSetup(byte by) {
        currentRoomIndex = by;
        GameScr.myIndex = 0;
        GameScr.trainingMode = false;
        GameScr.res = "";
        TerrainMidlet.myInfo.index = 0;
        OfflineGunAngles.applyServerAngleLocks();
        OfflineBossFight.placePlayerAtServerPoint(by);
        OfflineBossFight.placeBoss(by);
        if (by <= 5) {
            OfflineBossFight.createEarlyMapBosses(by);
        } else if (by == 6) {
            OfflineBossFight.createBalloonParts();
        } else if (by >= 7) {
            OfflineBossFight.createLateMapBosses(by);
        }
        CPlayer cPlayer = OfflineBossFight.findBoss();
        int n = 1 + (TerrainMidlet.myInfo == null ? 0 : TerrainMidlet.myInfo.getSquadSize());
        for (int i = 0; i < n && i < PM.p.length; ++i) {
            if (PM.p[i] == null || PM.p[i] instanceof Boss) continue;
            PM.p[i].team = true;
            OfflineBossFight.lockActors(PM.p[i], cPlayer);
        }
        OfflineBossAI.reset();
        OfflineCombat.reset();
        OfflineCombat.ensureLocalPlayerTurn();
        if (PM.p[GameScr.myIndex] != null) {
            OfflineGunAngles.resetAimAngle(PM.p[GameScr.myIndex]);
        }
        if (GameScr.cam != null) {
            GameScr.cam.setPlayerMode(0);
        }
    }

    private static int bossSpawnHp(byte by) {
        if (by < 0 || by >= BOSS_HP.length) {
            return 5000;
        }
        return BOSS_HP[by] + OfflineBossFight.teamLevel() * BOSS_HP_PER_LEVEL[by];
    }

    private static int teamLevel() {
        return TerrainMidlet.myInfo == null ? 0 : Math.max(0, TerrainMidlet.myInfo.level2);
    }

    public static int requiredBossSlots(byte by) {
        switch (by) {
            case 0:
            case 1:
            case 5:
            case 8:
            case 9: {
                return 4;
            }
            case 2:
            case 3: {
                return 2;
            }
            case 4:
            case 6: {
                return 5;
            }
            case 7: {
                return 3;
            }
        }
        return 1;
    }

    private static CPlayer findBoss() {
        for (int i = 0; i < PM.p.length; ++i) {
            if (!(PM.p[i] instanceof Boss)) continue;
            return PM.p[i];
        }
        return null;
    }

    private static void placeBoss(byte by) {
        int n;
        int n2;
        int n3 = OfflineBossFight.findBossIndex();
        if (n3 < 0) {
            return;
        }
        switch (by) {
            case 0: {
                n2 = CRes.random(95, 316);
                n = OfflineBossFight.findGroundBelow(n2, 50 + 40 * CRes.random(0, 3));
                break;
            }
            case 1: {
                n2 = CRes.random(445, 801);
                n = OfflineBossFight.findGroundBelow(n2, 180);
                break;
            }
            case 2: {
                n2 = 505;
                n = OfflineBossFight.findGroundBelow(n2, 221);
                break;
            }
            case 3: {
                n2 = 420;
                n = OfflineBossFight.findGroundBelow(n2, 200);
                break;
            }
            case 4: {
                n2 = 880;
                n = OfflineBossFight.findGroundBelow(n2, 400);
                break;
            }
            case 5: {
                int n4 = CRes.random(300, 801);
                int n5 = CRes.random(-350, 101);
                GameScr.pm.updatePlayerXY(n3, (short)n4, (short)n5);
                return;
            }
            case 6: {
                int n6 = CRes.random(300, 801);
                int n7 = CRes.random(-350, 101);
                GameScr.pm.updatePlayerXY(n3, (short)n6, (short)n7);
                return;
            }
            case 7: {
                int n8 = CRes.random(20, Math.max(21, MM.mapWidth - 19));
                int n9 = 250;
                GameScr.pm.updatePlayerXY(n3, (short)n8, (short)n9);
                return;
            }
            case 8:
            case 9: {
                int n10 = 700;
                int n11 = CRes.random(0, 30);
                GameScr.pm.updatePlayerXY(n3, (short)n10, (short)n11);
                return;
            }
            default: {
                float f = by % 2 == 0 ? 0.8f : 0.2f;
                OfflineBossFight.placeOnGround(n3, f);
                return;
            }
        }
        GameScr.pm.updatePlayerXY(n3, (short)n2, (short)n);
    }

    private static void createEarlyMapBosses(byte by) {
        int n = OfflineBossFight.requiredBossSlots(by);
        for (int i = 1; i < n; ++i) {
            String string;
            int n2;
            int n3;
            byte by2 = BOSS_GUN[by];
            int n4 = OfflineBossFight.bossSpawnHp(by);
            switch (by) {
                case 0: {
                    n3 = i % 2 == 0 ? CRes.random(95, 316) : CRes.random(890, 1071);
                    n2 = OfflineBossFight.findGroundBelow(n3, 50 + 40 * CRes.random(0, 3));
                    string = "BigBoom";
                    break;
                }
                case 1: {
                    n3 = CRes.random(445, 801) + i * 50;
                    n2 = OfflineBossFight.findGroundBelow(n3, 180);
                    string = "SmallBoom";
                    break;
                }
                case 2: {
                    int[] nArray = new int[]{505, 1010};
                    int[] nArray2 = new int[]{221, 221};
                    n3 = nArray[i];
                    n2 = OfflineBossFight.findGroundBelow(n3, nArray2[i]);
                    string = "Spider Robot";
                    break;
                }
                case 3: {
                    int[] nArray = new int[]{420, 580};
                    n3 = nArray[i];
                    n2 = OfflineBossFight.findGroundBelow(n3, 200);
                    string = "Robot";
                    break;
                }
                case 4: {
                    by2 = 12;
                    n4 = 1500 + OfflineBossFight.teamLevel() * 10;
                    n3 = CRes.random(470, 756);
                    n2 = OfflineBossFight.findGroundBelow(n3, 400);
                    string = "BigBoom";
                    break;
                }
                case 5: {
                    n3 = CRes.random(300, 801);
                    n2 = CRes.random(-350, 101);
                    string = "UFO";
                    break;
                }
                default: {
                    return;
                }
            }
            if (OfflineBossFight.createBoss(by2, string, n4, n3, n2, by == 5) != null) continue;
            return;
        }
    }

    private static CPlayer createBoss(byte by, String string, int n, int n2, int n3, boolean bl) {
        int n4 = OfflineBossFight.firstEmptySlot();
        if (n4 < 0) {
            return null;
        }
        Boss boss = new Boss(-2000 - n4, (byte)n4, n2, n3, true, 2, by, n);
        boss.name = string;
        boss.hp = n;
        boss.maxhp = n;
        boss.team = false;
        boss.falling = false;
        boss.active = false;
        boss.setState((byte)0);
        PM.p[n4] = boss;
        return boss;
    }

    private static void createBalloonParts() {
        int n = OfflineBossFight.findBossIndex();
        if (n < 0 || !(PM.p[n] instanceof Boss) || PM.p[n].gun != 17) {
            return;
        }
        CPlayer cPlayer = PM.p[n];
        byte[] byArray = new byte[]{18, 19, 20};
        int[] nArray = new int[]{51, -5, -67};
        int[] nArray2 = new int[]{19, 30, -6};
        int n2 = OfflineBossFight.teamLevel() * 10;
        int[] nArray3 = new int[]{2000 + n2, 2500 + n2, 1000 + n2};
        String[] stringArray = new String[]{"Balloon Gun", "Balloon Gun Big", "Fan Back"};
        for (int i = 0; i < byArray.length; ++i) {
            int n3 = OfflineBossFight.firstEmptySlot();
            if (n3 < 0) {
                return;
            }
            Boss boss = new Boss(-2100 - i, (byte)n3, cPlayer.x + nArray[i], cPlayer.y + nArray2[i], true, 2, byArray[i], nArray3[i]);
            boss.name = stringArray[i];
            boss.hp = nArray3[i];
            boss.maxhp = nArray3[i];
            boss.team = false;
            boss.falling = false;
            boss.active = false;
            boss.setState((byte)0);
            PM.p[n3] = boss;
        }
    }

    private static void createLateMapBosses(byte by) {
        int n = by == 7 ? 3 : 4;
        byte by2 = BOSS_GUN[by];
        int n2 = OfflineBossFight.bossSpawnHp(by);
        for (int i = 1; i < n; ++i) {
            int n3;
            int n4;
            int n5 = OfflineBossFight.firstEmptySlot();
            if (n5 < 0) {
                return;
            }
            if (by == 7) {
                n4 = CRes.random(20, Math.max(21, MM.mapWidth - 19));
                n3 = 250;
            } else {
                n4 = 700 - i * 80;
                n3 = CRes.random(0, 30);
            }
            Boss boss = new Boss(-2200 - i, (byte)n5, n4, n3, true, 2, by2, n2);
            boss.name = by == 7 ? "Spider Poisonous" : (by == 8 ? "Ghost" : "Ghost II");
            boss.hp = n2;
            boss.maxhp = n2;
            boss.team = false;
            boss.falling = false;
            boss.active = false;
            boss.setState((byte)0);
            PM.p[n5] = boss;
        }
    }

    static CPlayer createBalloonEye(CPlayer cPlayer) {
        if (cPlayer == null) {
            return null;
        }
        int n = OfflineBossFight.firstEmptySlot();
        if (n < 0) {
            return null;
        }
        int n2 = 1500 + OfflineBossFight.teamLevel() * 10;
        Boss boss = new Boss(-2300, (byte)n, cPlayer.x + 57, cPlayer.y - 27, true, (byte)2, (byte)21, n2);
        boss.name = "Balloon Eye";
        boss.hp = n2;
        boss.maxhp = n2;
        boss.team = false;
        boss.falling = false;
        boss.active = false;
        boss.setState((byte)0);
        PM.p[n] = boss;
        return boss;
    }

    private static int firstEmptySlot() {
        if (PM.p == null) {
            return -1;
        }
        for (int i = 8; i < PM.p.length; ++i) {
            if (PM.p[i] != null) continue;
            return i;
        }
        return -1;
    }

    private static void placePlayerAtServerPoint(byte by) {
        if (by < 0 || by >= PLAYER_SPAWN_X.length) {
            OfflineBossFight.placeOnGround(0, 0.5f);
        } else {
            short[] sArray = PLAYER_SPAWN_X[by];
            short[] sArray2 = PLAYER_SPAWN_Y[by];
            int n = Math.min(sArray.length, sArray2.length);
            if (n == 0) {
                OfflineBossFight.placeOnGround(0, 0.5f);
            } else {
                int n2 = CRes.random(0, n);
                GameScr.pm.updatePlayerXY(0, sArray[n2], sArray2[n2]);
            }
        }
        OfflineBossFight.placeSquadExtras();
    }

    private static void placeSquadExtras() {
        if (PM.p == null || PM.p[0] == null || TerrainMidlet.myInfo == null) {
            return;
        }
        int n = PM.p[0].x;
        int n2 = TerrainMidlet.myInfo.getSquadSize();
        for (int i = 1; i <= n2 && i < PM.p.length; ++i) {
            if (PM.p[i] == null) continue;
            int n3 = n + i * 26;
            if (n3 < 10) {
                n3 = 10;
            }
            if (MM.mapWidth > 20 && n3 > MM.mapWidth - 10) {
                n3 = MM.mapWidth - 10;
            }
            int n4 = OfflineBossFight.findGroundY(n3);
            GameScr.pm.updatePlayerXY(i, (short)n3, (short)n4);
        }
    }

    private static int findBossIndex() {
        for (int i = 0; i < PM.p.length; ++i) {
            if (!(PM.p[i] instanceof Boss)) continue;
            return i;
        }
        return -1;
    }

    private static void placeOnGround(int n, float f) {
        if (n < 0 || GameScr.pm == null) {
            return;
        }
        short s = (short)((float)MM.mapWidth * f);
        short s2 = (short)OfflineBossFight.findGroundY(s);
        GameScr.pm.updatePlayerXY(n, s, s2);
    }

    static int findGroundY(int n) {
        int n2 = OfflineBossFight.findGroundYStrict(n);
        return n2 >= 0 ? n2 : (int)((float)MM.mapHeight * 0.6f);
    }

    static int findGroundYStrict(int n) {
        if (GameScr.mm != null && MM.mapHeight > 0) {
            for (int i = 0; i < MM.mapHeight; i += 2) {
                if (!GameScr.mm.isLand(n, i)) continue;
                return i;
            }
        }
        return -1;
    }

    private static int findGroundBelow(int n, int n2) {
        if (GameScr.mm == null) {
            return n2;
        }
        int n3 = MM.mapHeight + 200;
        for (int i = n2; i < n3; ++i) {
            if (!GameScr.mm.isLand(n, i)) continue;
            return i;
        }
        return n2;
    }

    private static void lockActors(CPlayer cPlayer, CPlayer cPlayer2) {
        if (cPlayer != null) {
            cPlayer.falling = false;
            cPlayer.active = true;
            cPlayer.isCom = false;
            cPlayer.setState((byte)0);
            if (cPlayer.hp <= 0) {
                cPlayer.maxhp = cPlayer.hp = OfflineEquipmentStats.maxHp(TerrainMidlet.myInfo);
            }
        }
        if (cPlayer2 != null) {
            cPlayer2.falling = false;
            cPlayer2.active = true;
            cPlayer2.setState((byte)0);
            if (cPlayer2.hp <= 0) {
                cPlayer2.hp = cPlayer2.maxhp;
            }
        }
        if (cPlayer != null && cPlayer2 != null) {
            cPlayer.look = cPlayer2.x >= cPlayer.x ? 2 : 0;
            cPlayer2.look = cPlayer.x >= cPlayer2.x ? 2 : 0;
        }
    }
}

