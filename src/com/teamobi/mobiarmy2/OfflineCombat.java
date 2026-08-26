/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineBossAI;
import com.teamobi.mobiarmy2.OfflineBossFight;
import com.teamobi.mobiarmy2.OfflineBossReward;
import com.teamobi.mobiarmy2.OfflineBotUfo;
import com.teamobi.mobiarmy2.OfflineBulletAssets;
import com.teamobi.mobiarmy2.OfflineBulletSim;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import com.teamobi.mobiarmy2.OfflineGunAngles;
import com.teamobi.mobiarmy2.OfflineItemLogic;
import com.teamobi.mobiarmy2.OfflineLuckyGift;
import com.teamobi.mobiarmy2.OfflineMission;
import com.teamobi.mobiarmy2.OfflinePvpBot;
import com.teamobi.mobiarmy2.OfflinePvpBotAI;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineSpecialBullets;
import com.teamobi.mobiarmy2.OfflineSpecialShop;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Explosion;
import item.BM;
import item.Bullet;
import map.MM;
import model.CRes;
import model.PlayerInfo;
import player.Boss;
import player.CPlayer;
import player.PM;
import screen.GameScr;
import screen.PrepareScr;

public final class OfflineCombat {
    private static int turnCounter;
    private static int totalExpGained;
    private static byte bossShotWho;
    private static byte bossShotType;
    private static int bossShotDamage;
    private static int bossShotRadius;
    private static boolean[] bossExpGranted;
    private static int[] localSuperX;
    private static int[] localSuperY;
    private static byte[] localSuperType;
    private static final int[] BONUS_GEM_IDS;
    private static boolean localPlayerPowActive;
    private static boolean localShotPowActive;
    private static boolean[] localShotLucky;
    private static int lastSquadIndex;

    private OfflineCombat() {
    }

    public static void reset() {
        localPlayerPowActive = false;
        localShotPowActive = false;
        localShotLucky = new boolean[0];
        OfflineCombat.clearLocalSuperShot();
        OfflineCombat.clearBossShotProfile();
        OfflineCombat.resetBossExpTracking();
        OfflineItemLogic.reset();
        lastSquadIndex = -1;
        totalExpGained = 0;
        turnCounter = 0;
    }

    public static int turnCounter() {
        return turnCounter;
    }

    public static void advanceTurnCounter() {
        OfflineBotUfo.onTurnAdvanced(++turnCounter);
    }

    public static int totalExpGained() {
        return totalExpGained;
    }

    public static boolean isActiveBattle() {
        return GameScr.pm != null && PM.p != null && CCanvas.curScr == CCanvas.gameScr;
    }

    public static void onWaitForFire(byte by, short s, short s2, short s3, byte by2, byte by3, byte by4) {
        if (!OfflineCombat.isActiveBattle()) {
            return;
        }
        CPlayer cPlayer = PM.getCurPlayer();
        if (cPlayer == null) {
            return;
        }
        OfflineCombat.clearBossShotProfile();
        byte by5 = OfflineCombat.playerSlot(cPlayer);
        if (by5 < 0) {
            return;
        }
        OfflineCombat.addRage(cPlayer, 15);
        OfflineBulletAssets.prepareCombat(cPlayer);
        byte by6 = cPlayer.gun;
        byte by7 = by;
        if (by7 < 0) {
            by7 = Bullet.setBulletType(by6);
        }
        OfflineItemLogic.beginLocalShot(by7);
        byte by8 = (byte)OfflineBulletSim.clampForce(by2);
        boolean bl = localPlayerPowActive || cPlayer.itemUsed == 100;
        short[][][] sArray = OfflineBulletSim.buildAllPaths(cPlayer, by7, by8, by3, bl);
        if (!OfflineCombat.hasUsablePaths(sArray)) {
            cPlayer.active = true;
            cPlayer.shootFrame = false;
            CPlayer.isShooting = false;
            CPlayer.isStopFire = false;
            return;
        }
        byte by9 = by7 == 17 || by7 == 19 ? (by3 > 0 ? (byte)Math.max(2, OfflineBulletSim.clampForce(by3)) : OfflineSpecialBullets.resolveForce2(by7, sArray)) : (by3 > 0 ? (byte)((byte)OfflineBulletSim.clampForce(by3)) : (byte)2);
        short[][] sArrayArray = new short[sArray.length][];
        short[][] sArrayArray2 = new short[sArray.length][];
        for (int i = 0; i < sArray.length; ++i) {
            sArrayArray[i] = sArray[i][0];
            sArrayArray2[i] = sArray[i][1];
        }
        short s4 = (short)cPlayer.angle;
        byte by10 = bl ? (byte)1 : 0;
        int[] nArray = OfflineCombat.prepareLocalSuperShot(sArray, by7, bl, cPlayer);
        if (by5 == GameScr.myIndex && OfflineCombat.hasSuperShot()) {
            OfflineMission.onSuperShot();
        }
        localShotPowActive = bl;
        OfflineCombat.rollShotLucky();
        cPlayer.shoot(by10, by5, (short)cPlayer.x, (short)cPlayer.y, by7, sArrayArray, sArrayArray2, by4, by9, s4, null, null, nArray[0], nArray[1]);
    }

    private static boolean hasUsablePaths(short[][][] sArray) {
        if (sArray == null || sArray.length == 0) {
            return false;
        }
        for (int i = 0; i < sArray.length; ++i) {
            if (sArray[i] != null && sArray[i].length >= 2 && sArray[i][0] != null && sArray[i][1] != null && sArray[i][0].length != 0 && sArray[i][1].length != 0) continue;
            return false;
        }
        return true;
    }

    public static void applyExplosionDamage(byte by, int[] nArray, int[] nArray2, byte by2) {
        boolean bl;
        if (!OfflineCombat.isActiveBattle() || by <= 0) {
            return;
        }
        boolean bl2 = by2 == bossShotWho;
        boolean bl3 = bl = !bl2 && localShotPowActive;
        if (!bl2) {
            localShotPowActive = false;
            localPlayerPowActive = false;
        }
        boolean bl4 = by2 == GameScr.myIndex;
        OfflineCombat.applyAggregatedDamage(by, nArray, nArray2, bl, bl4, bl2, by2);
        if (bl2) {
            OfflineCombat.clearBossShotProfile();
        } else if (bl4) {
            OfflineItemLogic.finishLocalShot();
            OfflineCombat.clearLocalSuperShot();
        }
        BM.nBum = 0;
        OfflineCombat.checkBattleEnd(bl4);
    }

    private static void applyAggregatedDamage(byte n, int[] nArray, int[] nArray2, boolean bl, boolean bl2, boolean bl3, byte by) {
        int n2;
        byte by2 = OfflineItemLogic.localShotType();
        PlayerInfo playerInfo = bl3 ? null : OfflineCombat.luckInfoForSlot(by, by >= 0 && PM.p != null && by < PM.p.length ? PM.p[by] : null);
        for (n2 = 0; n2 < n; ++n2) {
            if (nArray[n2] == -1 && nArray2[n2] == -1) continue;
            OfflineItemLogic.onExplosionPoint(nArray[n2], nArray2[n2], by);
            if (!bl || by2 == 49) continue;
            new Explosion(nArray[n2], nArray2[n2], 7);
        }
        boolean bl4 = !bl3 && by >= 0 && by < localShotLucky.length && localShotLucky[by];
        for (int i = 0; i < PM.p.length; ++i) {
            int n3;
            int n4;
            int n5;
            int n6;
            CPlayer cPlayer = PM.p[i];
            if (cPlayer == null || !OfflineCombat.isDamageableTarget(cPlayer) || cPlayer.getState() == 5 || cPlayer.hp <= 0 || !bl2 && !OfflineCombat.isSquadUnit(cPlayer) || OfflineItemLogic.blocksDamage(cPlayer, i) || OfflinePvpBotAI.isBotInvisible(i)) continue;
            int n7 = 0;
            for (n2 = 0; n2 < n; ++n2) {
                int n8;
                int n9 = n6 = bl3 ? bossShotRadius : OfflineItemLogic.shotRadius(bl, n2);
                if (n6 <= 0 || nArray[n2] == -1 && nArray2[n2] == -1) continue;
                n5 = n6 * n6;
                n4 = OfflineCombat.distanceSqToHitBox(cPlayer, nArray[n2], nArray2[n2]);
                if (n4 > n5) continue;
                int n10 = n3 = bl3 ? OfflineCombat.bossProjectileDamage(bossShotType, bossShotDamage, n2) : OfflineItemLogic.shotDamage(playerInfo, bl, n2);
                if (bl2) {
                    n8 = OfflineCombat.localSuperBonusAt(nArray[n2], nArray2[n2]);
                    n3 += n3 * n8 / 100;
                }
                n7 += (n8 = n3 * (n5 - n4) / n5) < 1 ? 1 : n8;
            }
            if (n7 <= 0) continue;
            int n11 = n6 = i < localShotLucky.length && localShotLucky[i] ? 1 : 0;
            if (bl4) {
                n7 *= 2;
            }
            if (OfflineCombat.isSquadUnit(cPlayer)) {
                n7 = OfflineCombat.reduceDamageForTarget(cPlayer, n7, n6 != 0);
            }
            n5 = n7 > cPlayer.hp ? cPlayer.hp : n7;
            n4 = cPlayer.hp - n7;
            if (n4 < 0) {
                n4 = 0;
            }
            if (n4 <= 0 && n6 != 0) {
                n4 = 10;
            }
            n3 = cPlayer.maxhp > 0 ? (int)(n4 * 25 / cPlayer.maxhp) : 0;
            cPlayer.updateHP(n4, (byte)n3);
            OfflineCombat.addRage(cPlayer, 12);
            if (bl2) {
                OfflineItemLogic.applyStatus(cPlayer, i);
                OfflineItemLogic.recordVampireDamage(n5);
                OfflineMission.addProgress(1, n5);
                if (n4 > 0 || !OfflineCombat.isPvpBotSlot(i)) continue;
                OfflineMission.onKillPvpBot(cPlayer.gun);
                continue;
            }
            if (!OfflineCombat.isPvpBotSlot(by)) continue;
            OfflinePvpBotAI.onBotDamageDealt(by, n5);
        }
    }

    private static int[] prepareLocalSuperShot(short[][][] sArray, byte by, boolean bl, CPlayer cPlayer) {
        OfflineCombat.clearLocalSuperShot();
        int n = -1;
        int n2 = -1;
        if (bl || !OfflineCombat.supportsSuperShot(by) || sArray == null) {
            return new int[]{n, n2};
        }
        int n3 = by == 19 ? Math.min(1, sArray.length) : sArray.length;
        localSuperX = new int[n3];
        localSuperY = new int[n3];
        localSuperType = new byte[n3];
        for (int i = 0; i < n3; ++i) {
            int n4 = OfflineItemLogic.shotRadius(false, i);
            short[][] sArray2 = sArray[i];
            if (sArray2 == null || sArray2.length < 2 || sArray2[0] == null || sArray2[1] == null || sArray2[0].length <= 2 || sArray2[1].length != sArray2[0].length) continue;
            int n5 = 0;
            for (int j = 1; j < sArray2[1].length; ++j) {
                if (sArray2[1][j] >= sArray2[1][n5]) continue;
                n5 = j;
            }
            int n6 = sArray2[0].length - 1;
            int n7 = 0;
            if (n5 < n6 && sArray2[1][n6] - sArray2[1][n5] > 350) {
                n7 = 2;
            } else if (n5 < n6 && Math.abs(sArray2[0][n6] - sArray2[0][0]) > 375) {
                n7 = 1;
            }
            OfflineCombat.localSuperX[i] = sArray2[0][n6];
            OfflineCombat.localSuperY[i] = sArray2[1][n6];
            OfflineCombat.localSuperType[i] = (byte)n7;
            if (n7 != 2 || n >= 0 || !OfflineCombat.hasDamageTargetAt(sArray2[0][n6], sArray2[1][n6], n4, cPlayer)) continue;
            n = sArray2[0][n5];
            n2 = sArray2[1][n5];
        }
        return new int[]{n, n2};
    }

    private static boolean supportsSuperShot(byte by) {
        return by == 0 || by == 1 || by == 2 || by == 9 || by == 10 || by == 11 || by == 19;
    }

    private static boolean hasDamageTargetAt(int n, int n2, int n3, CPlayer cPlayer) {
        if (PM.p == null) {
            return false;
        }
        int n4 = n3 * n3;
        for (int i = 0; i < PM.p.length; ++i) {
            CPlayer cPlayer2 = PM.p[i];
            if (cPlayer2 == null || cPlayer2 == cPlayer || !OfflineCombat.isDamageableTarget(cPlayer2) || cPlayer2.hp <= 0 || cPlayer2.getState() == 5 || OfflineCombat.distanceSqToHitBox(cPlayer2, n, n2) > n4) continue;
            return true;
        }
        return false;
    }

    private static int localSuperBonusAt(int n, int n2) {
        int n3 = 0;
        for (int i = 0; i < localSuperType.length; ++i) {
            int n4;
            if (localSuperType[i] == 0 || Math.abs(localSuperX[i] - n) > 2 || Math.abs(localSuperY[i] - n2) > 2) continue;
            int n5 = n4 = localSuperType[i] == 2 ? 10 : 5;
            if (n4 <= n3) continue;
            n3 = n4;
        }
        return n3;
    }

    private static boolean hasSuperShot() {
        for (int i = 0; i < localSuperType.length; ++i) {
            if (localSuperType[i] == 0) continue;
            return true;
        }
        return false;
    }

    private static void clearLocalSuperShot() {
        localSuperX = new int[0];
        localSuperY = new int[0];
        localSuperType = new byte[0];
    }

    private static void resetBossExpTracking() {
        int n = PM.p == null ? 0 : PM.p.length;
        bossExpGranted = new boolean[n];
    }

    public static void setBossShotProfile(byte by, byte by2, int n, int n2) {
        bossShotWho = by;
        bossShotType = by2;
        bossShotDamage = Math.max(0, n);
        bossShotRadius = Math.max(0, n2);
    }

    public static void clearBossShotProfile() {
        bossShotWho = (byte)-1;
        bossShotType = (byte)-1;
        bossShotDamage = 0;
        bossShotRadius = 0;
    }

    private static int bossProjectileDamage(byte by, int n, int n2) {
        switch (by) {
            case 1: {
                return n / 2;
            }
            case 2:
            case 10: {
                return n / 3;
            }
            case 6: {
                return n / 3;
            }
            case 7: {
                return n * 2;
            }
            case 11:
            case 33: {
                return n / 5;
            }
            case 36: {
                return 0;
            }
        }
        return n;
    }

    public static void applyDirectExplosionDamage(int n, int n2, int n3, int n4, boolean bl) {
        if (!OfflineCombat.isActiveBattle()) {
            return;
        }
        OfflineCombat.damageAt(n, n2, n3, n4, bl);
        if (bl) {
            OfflineItemLogic.finishDirectDamage();
        }
        OfflineCombat.checkBattleEnd(bl);
    }

    public static void checkBattleEndNow() {
        if (OfflineCombat.isActiveBattle()) {
            OfflineCombat.checkBattleEnd();
        }
    }

    public static void applyBossDirectDamage(CPlayer cPlayer, int n) {
        if (!OfflineCombat.isActiveBattle() || cPlayer == null || cPlayer.hp <= 0 || n <= 0 || OfflineItemLogic.blocksDamage(cPlayer, GameScr.myIndex)) {
            return;
        }
        if (OfflineCombat.isSquadUnit(cPlayer)) {
            n = OfflineCombat.reduceDamageForTarget(cPlayer, n);
        }
        int n2 = Math.max(0, cPlayer.hp - n);
        byte by = cPlayer.maxhp > 0 ? (byte)(n2 * 25 / cPlayer.maxhp) : (byte)0;
        cPlayer.updateHP(n2, by);
        OfflineCombat.addRage(cPlayer, 12);
        OfflineCombat.checkBattleEnd();
    }

    private static void checkBattleEnd() {
        OfflineCombat.checkBattleEnd(false);
    }

    private static void checkBattleEnd(boolean bl) {
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer == null || CCanvas.gameScr == null || CCanvas.gameScr.isShowingResult()) {
            return;
        }
        if (PrepareScr.isPvpBotRoom) {
            boolean bl2 = OfflineCombat.hasSquadSurvivor();
            boolean bl3 = OfflineCombat.hasBotSurvivor();
            if (bl && !bl2 && !bl3) {
                int[] nArray = OfflineCombat.grantPvpBotWinRewards();
                CCanvas.gameScr.setWin((byte)1, (byte)0, nArray[0], nArray[1]);
            } else if (!bl2) {
                int[] nArray = OfflineCombat.applyPvpBotLosePenalty();
                CCanvas.gameScr.setWin((byte)2, (byte)0, -nArray[0], -nArray[1]);
            } else if (!bl3) {
                int[] nArray = OfflineCombat.grantPvpBotWinRewards();
                CCanvas.gameScr.setWin((byte)1, (byte)0, nArray[0], nArray[1]);
            }
            return;
        }
        OfflineCombat.grantDefeatedBossExp();
        CPlayer cPlayer2 = null;
        boolean bl4 = false;
        boolean bl5 = false;
        for (int i = 0; i < PM.p.length; ++i) {
            if (!(PM.p[i] instanceof Boss)) continue;
            bl4 = true;
            if (cPlayer2 == null) {
                cPlayer2 = PM.p[i];
            }
            if (PM.p[i].hp <= 0 || PM.p[i].getState() == 5) continue;
            bl5 = true;
        }
        if (bl4 && !bl5) {
            int[] nArray = OfflineCombat.grantWinRewards();
            OfflineMission.onBossWin(cPlayer2);
            CCanvas.gameScr.setWin((byte)1, (byte)0, nArray[0], nArray[1], OfflineBossReward.lastRewardText());
            CCanvas.gameScr.setExpBonus(totalExpGained);
        } else if (!OfflineCombat.hasSquadSurvivor()) {
            CCanvas.gameScr.setWin((byte)2, (byte)0, 0);
        }
    }

    private static boolean hasSquadSurvivor() {
        if (PM.p == null || TerrainMidlet.myInfo == null) {
            return false;
        }
        int n = 1 + TerrainMidlet.myInfo.getSquadSize();
        for (int i = 0; i < n && i < PM.p.length; ++i) {
            CPlayer cPlayer = PM.p[i];
            if (cPlayer == null || cPlayer.hp <= 0 || cPlayer.getState() == 5) continue;
            return true;
        }
        return false;
    }

    private static boolean isPvpBotSlot(int n) {
        int n2 = OfflinePvpBot.activeSquadCount;
        int n3 = n2 + OfflinePvpBot.activeBotCount;
        return n >= n2 && n < n3;
    }

    private static PlayerInfo luckInfoForSlot(int n, CPlayer cPlayer) {
        if (cPlayer == null) {
            return null;
        }
        if (OfflineCombat.isSquadUnit(cPlayer)) {
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            return cPlayer.gun == playerInfo.gun ? playerInfo : playerInfo.createSquadSnapshot(cPlayer.gun);
        }
        if (OfflineCombat.isPvpBotSlot(n) && CCanvas.prepareScr != null && n >= 0 && n < CCanvas.prepareScr.playerInfos.size()) {
            return (PlayerInfo)CCanvas.prepareScr.playerInfos.elementAt(n);
        }
        return null;
    }

    private static boolean hasBotSurvivor() {
        if (PM.p == null) {
            return false;
        }
        int n = OfflinePvpBot.activeSquadCount;
        int n2 = n + OfflinePvpBot.activeBotCount;
        for (int i = n; i < n2 && i < PM.p.length; ++i) {
            CPlayer cPlayer = PM.p[i];
            if (cPlayer == null || cPlayer.hp <= 0 || cPlayer.getState() == 5) continue;
            return true;
        }
        return false;
    }

    public static int bonusGemCount(int n, byte by) {
        if (by != 3) {
            return 0;
        }
        return Math.max(0, n - 1);
    }

    private static void grantBonusGems(int n, byte by, int n2) {
        int n3 = OfflineCombat.bonusGemCount(n, by) * n2 / 100;
        for (int i = 0; i < n3; ++i) {
            int n4 = BONUS_GEM_IDS[CRes.random(0, BONUS_GEM_IDS.length)];
            OfflineSpecialShop.addMaterial((byte)n4, 1);
        }
    }

    private static int pvpBotRewardPercent() {
        int n = OfflinePvpBot.activeBotCount;
        if (n <= 0 || PM.p == null) {
            return 100;
        }
        int n2 = OfflinePvpBot.activeSquadCount;
        int n3 = n2 + n;
        int n4 = 0;
        for (int i = n2; i < n3 && i < PM.p.length; ++i) {
            CPlayer cPlayer = PM.p[i];
            if (cPlayer == null || !cPlayer.diedFromFall) continue;
            ++n4;
        }
        int n5 = (n - n4) * 100 / n;
        return n5 < 10 ? 10 : n5;
    }

    private static int[] grantPvpBotWinRewards() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return new int[]{0, 0};
        }
        int n = OfflineCombat.pvpBotRewardPercent();
        int n2 = OfflinePvpBot.winXuReward(OfflinePvpBot.activeDifficulty) * n / 100;
        int n3 = OfflinePvpBot.winLuongReward(OfflinePvpBot.activeDifficulty) * n / 100;
        int n4 = OfflinePvpBot.winExpReward(OfflinePvpBot.activeBotCount, OfflinePvpBot.activeDifficulty) * n / 100;
        playerInfo.xu += n2;
        playerInfo.luong += n3;
        ++playerInfo.win;
        OfflineCombat.grantExpDirect(n4);
        OfflineCombat.grantBonusGems(OfflinePvpBot.activeBotCount, OfflinePvpBot.activeDifficulty, n);
        OfflineMission.onPvpBotWin(OfflinePvpBot.activeSquadCount + OfflinePvpBot.activeBotCount);
        OfflineLuckyGift.arm();
        OfflineSave.save();
        return new int[]{n2, n3};
    }

    private static int[] applyPvpBotLosePenalty() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return new int[]{0, 0};
        }
        int n = OfflinePvpBot.winXuReward(OfflinePvpBot.activeDifficulty);
        int n2 = OfflinePvpBot.winLuongReward(OfflinePvpBot.activeDifficulty);
        int n3 = playerInfo.xu;
        int n4 = playerInfo.luong;
        playerInfo.xu = Math.max(0, playerInfo.xu - n);
        playerInfo.luong = Math.max(0, playerInfo.luong - n2);
        OfflineSave.save();
        return new int[]{n3 - playerInfo.xu, n4 - playerInfo.luong};
    }

    public static int expThresholdForLevel(int n) {
        if (n < 1) {
            n = 1;
        }
        return 500 * n * (n + 1);
    }

    private static void grantDefeatedBossExp() {
        if (PM.p == null) {
            return;
        }
        if (bossExpGranted == null || bossExpGranted.length != PM.p.length) {
            OfflineCombat.resetBossExpTracking();
        }
        for (int i = 0; i < PM.p.length; ++i) {
            CPlayer cPlayer = PM.p[i];
            if (!(cPlayer instanceof Boss) || bossExpGranted[i] || cPlayer.hp > 0 && cPlayer.getState() != 5) continue;
            OfflineCombat.bossExpGranted[i] = true;
            int n = OfflineBossFight.bossExpReward(cPlayer, OfflineBossFight.currentRoomIndex);
            if (n <= 0) continue;
            OfflineCombat.grantSquadExp(n);
        }
    }

    private static void grantSquadExp(int n) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null || n <= 0) {
            return;
        }
        totalExpGained += (n *= OfflineTeamItems.expMultiplier());
        CPlayer cPlayer = PM.getMyPlayer();
        byte by = cPlayer != null ? cPlayer.gun : playerInfo.gun;
        boolean bl = playerInfo.grantClassExp(by, n);
        if (cPlayer != null) {
            cPlayer.updateExp(n);
        }
        OfflineSave.save();
        if (bl) {
            int n2 = by == playerInfo.gun ? playerInfo.level2 : playerInfo.classLevel2[by];
            CCanvas.startOKDlg("Ch\u00fac m\u1eebng! " + PrepareScr.GUN_NAME[by] + " \u0111\u00e3 l\u00ean c\u1ea5p " + n2 + "!");
        }
    }

    private static int[] grantWinRewards() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return new int[]{0, 0};
        }
        byte by = OfflineBossFight.currentRoomIndex;
        int n = OfflineBossFight.winXuReward(by);
        int n2 = OfflineBossFight.winLuongReward(by);
        if (n <= 0 && n2 <= 0) {
            return new int[]{0, 0};
        }
        playerInfo.xu += n;
        playerInfo.luong += n2;
        ++playerInfo.win;
        OfflineBossReward.grantExtraRewards(by);
        OfflineSave.save();
        return new int[]{n, n2};
    }

    public static void grantExpDirect(int n) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null || n <= 0) {
            return;
        }
        n *= OfflineTeamItems.expMultiplier();
        if (playerInfo.nextExp <= 0) {
            playerInfo.nextExp = OfflineCombat.expThresholdForLevel(playerInfo.level2);
        }
        playerInfo.exp += n;
        boolean bl = false;
        while (playerInfo.nextExp > 0 && playerInfo.exp >= playerInfo.nextExp) {
            playerInfo.exp -= playerInfo.nextExp;
            ++playerInfo.level2;
            playerInfo.point = (short)(playerInfo.point + 2);
            playerInfo.nextExp = OfflineCombat.expThresholdForLevel(playerInfo.level2);
            bl = true;
        }
        playerInfo.level2Percen = playerInfo.nextExp > 0 ? (int)(playerInfo.exp * 100 / playerInfo.nextExp) : 0;
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer != null) {
            cPlayer.updateExp(n);
        }
        playerInfo.saveCurrentClassProgress();
        OfflineSave.save();
        if (bl) {
            CCanvas.startOKDlg("Ch\u00fac m\u1eebng! B\u1ea1n \u0111\u00e3 l\u00ean c\u1ea5p " + playerInfo.level2 + "!");
        }
    }

    private static void damageAt(int n, int n2, int n3, int n4, boolean bl) {
        int n5 = n3 * n3;
        int n6 = 0;
        while (n6 < PM.p.length) {
            CPlayer cPlayer = PM.p[n6];
            if (cPlayer != null && OfflineCombat.isDamageableTarget(cPlayer) && cPlayer.getState() != 5 && cPlayer.hp > 0) {
                if (!bl && !OfflineCombat.isSquadUnit(cPlayer)) {
                    ++n6;
                    continue;
                }
                if (OfflineItemLogic.blocksDamage(cPlayer, n6) || OfflinePvpBotAI.isBotInvisible(n6)) {
                    ++n6;
                    continue;
                }
                int n7 = OfflineCombat.distanceSqToHitBox(cPlayer, n, n2);
                if (n7 <= n5) {
                    int n8 = n4 * (n5 - n7) / n5;
                    if (n8 < 1) {
                        n8 = 1;
                    }
                    if (OfflineCombat.isSquadUnit(cPlayer)) {
                        n8 = OfflineCombat.reduceDamageForTarget(cPlayer, n8);
                    }
                    int n9 = n8 > cPlayer.hp ? cPlayer.hp : n8;
                    int n10 = cPlayer.hp - n8;
                    if (n10 < 0) {
                        n10 = 0;
                    }
                    byte by = cPlayer.maxhp > 0 ? (byte)(n10 * 25 / cPlayer.maxhp) : (byte)0;
                    cPlayer.updateHP(n10, by);
                    OfflineCombat.addRage(cPlayer, 12);
                    if (bl) {
                        OfflineItemLogic.applyStatus(cPlayer, n6);
                        OfflineItemLogic.recordVampireDamage(n9);
                    }
                    if (bl && cPlayer instanceof Boss) {
                        OfflineMission.addProgress(1, n9);
                    }
                }
            }
            ++n6;
        }
    }

    private static int distanceSqToHitBox(CPlayer cPlayer, int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        if (cPlayer instanceof Boss) {
            switch (cPlayer.gun) {
                case 13: {
                    n6 = cPlayer.x - 24;
                    n5 = cPlayer.y - 42;
                    n4 = cPlayer.x + 24;
                    n3 = cPlayer.y + 2;
                    break;
                }
                case 14: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 34;
                    n4 = cPlayer.x + 18;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 15: {
                    n6 = cPlayer.x - 30;
                    n5 = cPlayer.y - 64;
                    n4 = cPlayer.x + 30;
                    n3 = cPlayer.y + 5;
                    break;
                }
                case 16: {
                    n6 = cPlayer.x - 28;
                    n5 = cPlayer.y - 48;
                    n4 = cPlayer.x + 28;
                    n3 = cPlayer.y + 5;
                    break;
                }
                case 17: {
                    n6 = cPlayer.x - 40;
                    n5 = cPlayer.y - 60;
                    n4 = cPlayer.x + 42;
                    n3 = cPlayer.y + 6;
                    break;
                }
                case 18: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 30;
                    n4 = cPlayer.x + 20;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 19: {
                    n6 = cPlayer.x - 20;
                    n5 = cPlayer.y - 28;
                    n4 = cPlayer.x + 20;
                    n3 = cPlayer.y + 5;
                    break;
                }
                case 20: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 36;
                    n4 = cPlayer.x + 18;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 21: {
                    n6 = cPlayer.x - 14;
                    n5 = cPlayer.y - 22;
                    n4 = cPlayer.x + 16;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 22: {
                    n6 = cPlayer.x - 25;
                    n5 = cPlayer.y - 46;
                    n4 = cPlayer.x + 25;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 23:
                case 24: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 24;
                    n4 = cPlayer.x + 18;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 25:
                case 26: {
                    n6 = cPlayer.x - 20;
                    n5 = cPlayer.y - 36;
                    n4 = cPlayer.x + 20;
                    n3 = cPlayer.y + 4;
                    break;
                }
                default: {
                    n6 = cPlayer.x - 12;
                    n5 = cPlayer.y - 26;
                    n4 = cPlayer.x + 12;
                    n3 = cPlayer.y + 2;
                    break;
                }
            }
        } else {
            n6 = cPlayer.x - 12;
            n5 = cPlayer.y - 26;
            n4 = cPlayer.x + 12;
            n3 = cPlayer.y + 2;
        }
        int n7 = OfflineCombat.clampInt(n, n6, n4);
        int n8 = OfflineCombat.clampInt(n2, n5, n3);
        int n9 = n - n7;
        int n10 = n2 - n8;
        return n9 * n9 + n10 * n10;
    }

    static boolean isProjectileCollision(int n, int n2, CPlayer cPlayer) {
        if (PM.p == null) {
            return false;
        }
        for (int i = 0; i < PM.p.length; ++i) {
            CPlayer cPlayer2 = PM.p[i];
            if (cPlayer2 == null || cPlayer2 == cPlayer || !OfflineCombat.isDamageableTarget(cPlayer2) || cPlayer != null && cPlayer2.team == cPlayer.team || cPlayer2.hp <= 0 || cPlayer2.getState() == 5 || cPlayer2.isInvisible || !OfflineCombat.isInsideProjectileHitBox(cPlayer2, n, n2)) continue;
            return true;
        }
        return false;
    }

    private static boolean isDamageableTarget(CPlayer cPlayer) {
        return !(cPlayer instanceof Boss) || cPlayer.gun != 17;
    }

    private static boolean isSquadUnit(CPlayer cPlayer) {
        if (cPlayer == null || PM.p == null || TerrainMidlet.myInfo == null) {
            return false;
        }
        int n = 1 + TerrainMidlet.myInfo.getSquadSize();
        for (int i = 0; i < n && i < PM.p.length; ++i) {
            if (PM.p[i] != cPlayer) continue;
            return true;
        }
        return false;
    }

    private static int reduceDamageForTarget(CPlayer cPlayer, int n) {
        return OfflineCombat.reduceDamageForTarget(cPlayer, n, false);
    }

    private static int reduceDamageForTarget(CPlayer cPlayer, int n, boolean bl) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null || cPlayer == null) {
            return n;
        }
        PlayerInfo playerInfo2 = cPlayer.gun == playerInfo.gun ? playerInfo : playerInfo.createSquadSnapshot(cPlayer.gun);
        int n2 = OfflineEquipmentStats.defense(playerInfo2);
        if (bl) {
            n2 *= 2;
        }
        return OfflineEquipmentStats.reduceDamage(n2, n);
    }

    private static boolean isInsideProjectileHitBox(CPlayer cPlayer, int n, int n2) {
        int n3;
        int n4;
        int n5;
        int n6;
        if (cPlayer instanceof Boss) {
            switch (cPlayer.gun) {
                case 13: {
                    n6 = cPlayer.x - 24;
                    n5 = cPlayer.y - 42;
                    n4 = cPlayer.x + 24;
                    n3 = cPlayer.y + 2;
                    break;
                }
                case 14: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 34;
                    n4 = cPlayer.x + 18;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 15: {
                    n6 = cPlayer.x - 30;
                    n5 = cPlayer.y - 64;
                    n4 = cPlayer.x + 30;
                    n3 = cPlayer.y + 5;
                    break;
                }
                case 16: {
                    n6 = cPlayer.x - 28;
                    n5 = cPlayer.y - 48;
                    n4 = cPlayer.x + 28;
                    n3 = cPlayer.y + 5;
                    break;
                }
                case 17: {
                    n6 = cPlayer.x - 40;
                    n5 = cPlayer.y - 60;
                    n4 = cPlayer.x + 42;
                    n3 = cPlayer.y + 6;
                    break;
                }
                case 18: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 30;
                    n4 = cPlayer.x + 20;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 19: {
                    n6 = cPlayer.x - 20;
                    n5 = cPlayer.y - 28;
                    n4 = cPlayer.x + 20;
                    n3 = cPlayer.y + 5;
                    break;
                }
                case 20: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 36;
                    n4 = cPlayer.x + 18;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 21: {
                    n6 = cPlayer.x - 14;
                    n5 = cPlayer.y - 22;
                    n4 = cPlayer.x + 16;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 22: {
                    n6 = cPlayer.x - 25;
                    n5 = cPlayer.y - 46;
                    n4 = cPlayer.x + 25;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 23:
                case 24: {
                    n6 = cPlayer.x - 18;
                    n5 = cPlayer.y - 24;
                    n4 = cPlayer.x + 18;
                    n3 = cPlayer.y + 4;
                    break;
                }
                case 25:
                case 26: {
                    n6 = cPlayer.x - 20;
                    n5 = cPlayer.y - 36;
                    n4 = cPlayer.x + 20;
                    n3 = cPlayer.y + 4;
                    break;
                }
                default: {
                    n6 = cPlayer.x - 12;
                    n5 = cPlayer.y - 26;
                    n4 = cPlayer.x + 12;
                    n3 = cPlayer.y + 2;
                    break;
                }
            }
        } else {
            n6 = cPlayer.x - 12;
            n5 = cPlayer.y - 26;
            n4 = cPlayer.x + 12;
            n3 = cPlayer.y + 2;
        }
        return n >= n6 && n <= n4 && n2 >= n5 && n2 <= n3;
    }

    private static int clampInt(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        if (n > n3) {
            return n3;
        }
        return n;
    }

    public static void applyItemEffect(int n) {
        int n2;
        if (!OfflineCombat.isActiveBattle()) {
            return;
        }
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer == null || cPlayer.maxhp <= 0) {
            return;
        }
        OfflineItemLogic.onUseItem(n);
        if (n == 24) {
            new Explosion(cPlayer.x, cPlayer.y - 12, 10);
            OfflineCombat.applyDirectExplosionDamage(cPlayer.x, cPlayer.y - 12, 70, 1500, true);
            if (cPlayer.hp > 0 && !CCanvas.gameScr.isShowingResult()) {
                cPlayer.active = false;
                OfflineBossAI.startBossTurn();
            }
            return;
        }
        if (n == 27) {
            cPlayer.itemUsed = -1;
            cPlayer.bulletType = Bullet.setBulletType(cPlayer.gun);
            cPlayer.force = 0;
            cPlayer.force_2 = 0;
            cPlayer.active = false;
            OfflineBossAI.startBossTurn();
            return;
        }
        if (n == 10) {
            int n3 = 1 + (TerrainMidlet.myInfo == null ? 0 : TerrainMidlet.myInfo.getSquadSize());
            for (int i = 0; i < n3 && i < PM.p.length; ++i) {
                CPlayer cPlayer2 = PM.p[i];
                if (cPlayer2 == null || cPlayer2.hp <= 0 || cPlayer2.getState() == 5 || cPlayer2.maxhp <= 0) continue;
                int n4 = cPlayer2.hp * 25 / cPlayer2.maxhp;
                int n5 = Math.min(cPlayer2.maxhp, cPlayer2.hp + 300);
                byte by = (byte)(n5 * 25 / cPlayer2.maxhp);
                cPlayer2.updateHP(n5, by);
                OfflineCombat.addRage(cPlayer2, Math.abs(n4 - by) * 4);
            }
            return;
        }
        if (n == 0) {
            n2 = 350;
        } else if (n == 32) {
            n2 = cPlayer.maxhp / 2;
        } else if (n == 33) {
            n2 = cPlayer.maxhp;
        } else {
            return;
        }
        int n6 = cPlayer.hp * 25 / cPlayer.maxhp;
        int n7 = cPlayer.hp + n2;
        if (n7 > cPlayer.maxhp) {
            n7 = cPlayer.maxhp;
        }
        byte by = (byte)(n7 * 25 / cPlayer.maxhp);
        cPlayer.updateHP(n7, by);
        OfflineCombat.addRage(cPlayer, Math.abs(n6 - by) * 4);
    }

    private static void addRage(CPlayer cPlayer, int n) {
        if (cPlayer == null) {
            return;
        }
        int n2 = (cPlayer.currAngry & 0xFF) + n;
        if (n2 > 100) {
            n2 = 100;
        }
        cPlayer.updateAngry((byte)n2);
    }

    public static void rollShotLucky() {
        if (PM.p == null) {
            return;
        }
        localShotLucky = new boolean[PM.p.length];
        for (int i = 0; i < PM.p.length; ++i) {
            boolean bl;
            CPlayer cPlayer = PM.p[i];
            if (cPlayer == null) continue;
            OfflineCombat.localShotLucky[i] = bl = OfflineEquipmentStats.rollLucky(OfflineCombat.luckInfoForSlot(i, cPlayer));
            if (!bl) continue;
            cPlayer.lucky();
        }
    }

    public static void useSpecialSkill() {
        if (!OfflineCombat.isActiveBattle()) {
            return;
        }
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer == null || !cPlayer.isAngry || cPlayer.itemUsed != -1) {
            return;
        }
        cPlayer.isUsedItem = true;
        cPlayer.angryX = 0;
        cPlayer.currAngry = 0;
        cPlayer.is2TurnItem = true;
        cPlayer.UseItem(100, true, 0);
        localPlayerPowActive = true;
    }

    public static void onShootResult() {
        if (!OfflineCombat.isActiveBattle()) {
            return;
        }
        if (MM.vHoleInfo != null) {
            MM.vHoleInfo.removeAllElements();
        }
        if (PrepareScr.isPvpBotRoom) {
            OfflinePvpBotAI.startBotTurn();
        } else {
            OfflineBossAI.startBossTurn();
        }
    }

    public static void rollWind() {
        GameScr.changeWind(CRes.random(-70, 71), CRes.random(-70, 71));
        OfflineItemLogic.applyWindLock();
    }

    private static int findNextSquadIndex() {
        if (PM.p == null || TerrainMidlet.myInfo == null) {
            return 0;
        }
        int n = 1 + TerrainMidlet.myInfo.getSquadSize();
        if (n > PM.p.length) {
            n = PM.p.length;
        }
        if (n <= 1) {
            return 0;
        }
        for (int i = 1; i <= n; ++i) {
            int n2 = (lastSquadIndex + i + n) % n;
            CPlayer cPlayer = PM.p[n2];
            if (cPlayer == null || cPlayer.hp <= 0 || cPlayer.getState() == 5) continue;
            return n2;
        }
        return 0;
    }

    public static void ensureLocalPlayerTurn() {
        int n;
        if (PM.p == null) {
            return;
        }
        lastSquadIndex = n = OfflineCombat.findNextSquadIndex();
        GameScr.myIndex = (byte)n;
        if (GameScr.myIndex < 0 || GameScr.myIndex >= PM.p.length) {
            return;
        }
        PM.curP = GameScr.myIndex;
        CPlayer cPlayer = PM.p[GameScr.myIndex];
        if (cPlayer == null) {
            return;
        }
        OfflineCombat.rollWind();
        OfflineItemLogic.onPlayerTurn();
        if (cPlayer.isPoison && cPlayer.hp > 0) {
            int n2 = Math.max(0, cPlayer.hp - 150);
            byte by = cPlayer.maxhp > 0 ? (byte)(n2 * 25 / cPlayer.maxhp) : (byte)0;
            cPlayer.updateHP(n2, by);
            OfflineCombat.checkBattleEnd();
            if (cPlayer.hp <= 0) {
                OfflineCombat.ensureLocalPlayerTurn();
                return;
            }
        }
        OfflineCombat.advanceTurnCounter();
        OfflineBotUfo.onLocalPlayerTurnStart();
        cPlayer.active = true;
        cPlayer.falling = false;
        cPlayer.shootFrame = false;
        cPlayer.isCom = false;
        CPlayer.isShooting = false;
        cPlayer.itemUsed = -1;
        cPlayer.isUsedItem = false;
        cPlayer.movePoint = 0;
        if (cPlayer.getState() == 4) {
            cPlayer.setState((byte)0);
            cPlayer.checkAngleForSprite();
        }
        if (GameScr.cam != null) {
            GameScr.cam.setPlayerMode(GameScr.myIndex);
        }
        if (cPlayer.savedAngle != Integer.MIN_VALUE) {
            cPlayer.onStartTurnAngle();
        } else {
            OfflineGunAngles.resetAimAngle(cPlayer);
        }
    }

    public static void moveLocal(byte by, short s, short s2) {
        if (!OfflineCombat.isActiveBattle() || by < 0 || by >= PM.p.length || PM.p[by] == null) {
            return;
        }
        PM.p[by].xToNow = s;
        PM.p[by].yToNow = s2;
        if (PM.p[by].x != s || PM.p[by].y != s2) {
            GameScr.pm.movePlayer(by, s, s2);
        }
    }

    private static byte playerSlot(CPlayer cPlayer) {
        for (int i = 0; i < PM.p.length; ++i) {
            if (PM.p[i] != cPlayer) continue;
            return (byte)i;
        }
        return -1;
    }

    static {
        bossShotWho = (byte)-1;
        bossShotType = (byte)-1;
        bossExpGranted = new boolean[0];
        localSuperX = new int[0];
        localSuperY = new int[0];
        localSuperType = new byte[0];
        BONUS_GEM_IDS = new int[]{8, 18, 28, 38};
        localShotLucky = new boolean[0];
        lastSquadIndex = -1;
    }
}

