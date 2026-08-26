/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineAiMovement;
import com.teamobi.mobiarmy2.OfflineBossFight;
import com.teamobi.mobiarmy2.OfflineBulletSim;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineGunPhysics;
import com.teamobi.mobiarmy2.OfflineItemLogic;
import effect.Explosion;
import item.BM;
import map.MM;
import model.CRes;
import player.Boss;
import player.CPlayer;
import player.PM;
import screen.GameScr;

public final class OfflineBossAI {
    private static final int THINK_TICKS = 35;
    private static final int MOVE_TICKS_PER_PIXEL = 2;
    private static final int SERVER_MOVE_LIMIT = 100;
    private static boolean turnPending;
    private static int thinkTicks;
    private static boolean shotInFlight;
    private static int actingBossIndex;
    private static int lastBossIndex;
    private static int movePixelsLeft;
    private static int moveTickCounter;
    private static int emptyShotWaitTicks;
    private static int shotFlightTicks;
    private static boolean secondShotPending;
    private static byte secondBulletId;
    private static int secondDamage;
    private static int secondRadius;
    private static int secondForceMin;
    private static int secondForceMax;
    private static int secondAngleMin;
    private static int secondAngleMax;
    private static boolean spawnSmallBombPending;
    private static int spawnSmallBombX;
    private static int spawnSmallBombY;
    private static boolean bigBoomDropTurn;
    private static boolean[] ufoLoaded;
    private static boolean[] ufoMoveTurn;
    private static boolean[] spiderActionTurn;
    private static boolean[] spiderCaptureTurn;
    private static boolean[] spiderAcidTurn;
    private static boolean[] spiderWaitPlayerLand;
    private static byte[] spiderTurnCooldown;
    private static int[] spiderReturnX;
    private static boolean[] ghostActionTurn;
    private static byte[] ghostActionTicks;
    private static int balloonTurnPhase;
    private static boolean balloonEyeSpawned;

    private OfflineBossAI() {
    }

    public static void reset() {
        turnPending = false;
        thinkTicks = 0;
        shotInFlight = false;
        actingBossIndex = -1;
        lastBossIndex = -1;
        movePixelsLeft = 0;
        moveTickCounter = 0;
        emptyShotWaitTicks = 0;
        shotFlightTicks = 0;
        secondShotPending = false;
        spawnSmallBombPending = false;
        bigBoomDropTurn = false;
        ufoLoaded = new boolean[PM.p == null ? 0 : PM.p.length];
        ufoMoveTurn = new boolean[PM.p == null ? 0 : PM.p.length];
        spiderActionTurn = new boolean[PM.p == null ? 0 : PM.p.length];
        spiderCaptureTurn = new boolean[PM.p == null ? 0 : PM.p.length];
        spiderAcidTurn = new boolean[PM.p == null ? 0 : PM.p.length];
        spiderWaitPlayerLand = new boolean[PM.p == null ? 0 : PM.p.length];
        spiderTurnCooldown = new byte[PM.p == null ? 0 : PM.p.length];
        spiderReturnX = new int[PM.p == null ? 0 : PM.p.length];
        ghostActionTurn = new boolean[PM.p == null ? 0 : PM.p.length];
        ghostActionTicks = new byte[PM.p == null ? 0 : PM.p.length];
        balloonTurnPhase = -1;
        balloonEyeSpawned = false;
        OfflineCombat.clearBossShotProfile();
    }

    public static void startBossTurn() {
        if (GameScr.res != null && !GameScr.res.equals("")) {
            return;
        }
        CPlayer cPlayer = OfflineBossAI.localPlayer();
        actingBossIndex = OfflineBossAI.findNextBossIndex();
        CPlayer cPlayer2 = OfflineBossAI.bossAt(actingBossIndex);
        if (cPlayer2 == null || cPlayer == null || cPlayer.hp <= 0) {
            OfflineCombat.ensureLocalPlayerTurn();
            return;
        }
        lastBossIndex = actingBossIndex;
        OfflineItemLogic.onBossTurn(cPlayer2, actingBossIndex);
        if (cPlayer2.hp <= 0) {
            OfflineCombat.checkBattleEndNow();
            OfflineCombat.ensureLocalPlayerTurn();
            return;
        }
        if (OfflineBossAI.isOutOfMapBounds(cPlayer2)) {
            OfflineBossAI.explodeAndDie(cPlayer2, 0, 0);
            return;
        }
        OfflineCombat.rollWind();
        OfflineCombat.advanceTurnCounter();
        PM.curP = (byte)actingBossIndex;
        cPlayer2.active = true;
        if (GameScr.cam != null) {
            GameScr.cam.setPlayerMode(actingBossIndex);
        }
        turnPending = true;
        thinkTicks = 35;
        shotInFlight = false;
        secondShotPending = false;
        spawnSmallBombPending = false;
        bigBoomDropTurn = cPlayer2.gun == 12 && !OfflineBossAI.isBigBoomMelee(cPlayer2, cPlayer) && OfflineBossAI.firstEmptyBossSlot() >= 0 && CRes.random(0, 100) <= 45;
        moveTickCounter = 0;
        emptyShotWaitTicks = 0;
        shotFlightTicks = 0;
        movePixelsLeft = !bigBoomDropTurn && !OfflineItemLogic.isFrozen(actingBossIndex) && OfflineBossAI.shouldMove(cPlayer2, cPlayer) ? 100 : 0;
    }

    public static void update() {
        if (!turnPending || GameScr.res != null && !GameScr.res.equals("")) {
            return;
        }
        CPlayer cPlayer = OfflineBossAI.bossAt(actingBossIndex);
        CPlayer cPlayer2 = OfflineBossAI.localPlayer();
        if (cPlayer == null || cPlayer.hp <= 0 || cPlayer2 == null || cPlayer2.hp <= 0) {
            OfflineBossAI.finishTurn();
            return;
        }
        if (actingBossIndex >= 0 && actingBossIndex < ufoMoveTurn.length && ufoMoveTurn[actingBossIndex]) {
            if (cPlayer.isFlyingToPoint()) {
                return;
            }
            OfflineBossAI.ufoMoveTurn[OfflineBossAI.actingBossIndex] = false;
            OfflineBossAI.finishTurn();
            return;
        }
        if (actingBossIndex >= 0 && actingBossIndex < ghostActionTurn.length && ghostActionTurn[actingBossIndex]) {
            int n = actingBossIndex;
            ghostActionTicks[n] = (byte)(ghostActionTicks[n] + 1);
            if (ghostActionTicks[n] < 18) {
                return;
            }
            OfflineBossAI.ghostActionTurn[OfflineBossAI.actingBossIndex] = false;
            OfflineBossAI.finishGhostAttack(cPlayer, cPlayer2);
            return;
        }
        if (actingBossIndex >= 0 && actingBossIndex < spiderActionTurn.length && spiderActionTurn[actingBossIndex]) {
            if (cPlayer.isFlyingToPoint() || cPlayer.isCapturingPlayer()) {
                return;
            }
            if (spiderCaptureTurn[actingBossIndex]) {
                OfflineBossAI.spiderCaptureTurn[OfflineBossAI.actingBossIndex] = false;
                OfflineBossAI.spiderActionTurn[OfflineBossAI.actingBossIndex] = false;
                OfflineBossAI.dropSpiderSilk(cPlayer, cPlayer2);
                return;
            }
            if (spiderAcidTurn[actingBossIndex]) {
                OfflineBossAI.spiderAcidTurn[OfflineBossAI.actingBossIndex] = false;
                OfflineBossAI.spiderActionTurn[OfflineBossAI.actingBossIndex] = false;
                OfflineBossAI.fireSpiderAcid(cPlayer, cPlayer2);
                return;
            }
            if (spiderWaitPlayerLand[actingBossIndex]) {
                if (cPlayer2.falling) {
                    return;
                }
                OfflineBossAI.spiderWaitPlayerLand[OfflineBossAI.actingBossIndex] = false;
            }
            OfflineBossAI.spiderActionTurn[OfflineBossAI.actingBossIndex] = false;
            OfflineBossAI.finishTurn();
            return;
        }
        if (!shotInFlight) {
            if (thinkTicks > 0) {
                --thinkTicks;
                return;
            }
            if (movePixelsLeft > 0) {
                OfflineBossAI.stepTowardPlayer(cPlayer, cPlayer2);
                return;
            }
            OfflineBossAI.act(cPlayer, cPlayer2);
            return;
        }
        ++shotFlightTicks;
        if (GameScr.bm == null) {
            return;
        }
        if (BM.active || GameScr.bm.bullets.size() != 0) {
            emptyShotWaitTicks = 0;
            if (shotFlightTicks < 180) {
                return;
            }
            GameScr.bm.bullets.removeAllElements();
            GameScr.bm.endShoot();
        }
        if (OfflineBossAI.isAnyoneFalling() && ++emptyShotWaitTicks < 60) {
            return;
        }
        if (secondShotPending) {
            secondShotPending = false;
            OfflineBossAI.fire(cPlayer, cPlayer2, secondBulletId, OfflineBossAI.randomArcAngle(cPlayer, cPlayer2, secondAngleMin, secondAngleMax), CRes.random(secondForceMin, secondForceMax + 1), secondDamage, secondRadius);
            return;
        }
        if (spawnSmallBombPending) {
            spawnSmallBombPending = false;
            OfflineBossAI.spawnSmallBomb(spawnSmallBombX, spawnSmallBombY, cPlayer2);
        }
        OfflineBossAI.finishTurn();
    }

    private static void act(CPlayer cPlayer, CPlayer cPlayer2) {
        switch (cPlayer.gun) {
            case 11: {
                OfflineBossAI.actSmallBoom(cPlayer, cPlayer2);
                return;
            }
            case 12: {
                OfflineBossAI.actBigBoom(cPlayer, cPlayer2);
                return;
            }
            case 13: {
                OfflineBossAI.actRobotSpider(cPlayer, cPlayer2);
                return;
            }
            case 14: {
                OfflineBossAI.actRobot(cPlayer, cPlayer2);
                return;
            }
            case 15: {
                OfflineBossAI.actTrex(cPlayer, cPlayer2);
                return;
            }
            case 16: {
                OfflineBossAI.actUfo(cPlayer, cPlayer2);
                return;
            }
            case 17: {
                OfflineBossAI.actBalloon(cPlayer, cPlayer2);
                return;
            }
            case 22: {
                OfflineBossAI.actCaveSpider(cPlayer, cPlayer2);
                return;
            }
            case 25:
            case 26: {
                OfflineBossAI.actGhost(cPlayer, cPlayer2);
                return;
            }
        }
        OfflineBossAI.fireAimed(cPlayer, cPlayer2, (byte)0, 60, 10, 450, 18);
    }

    private static void actBigBoom(CPlayer cPlayer, CPlayer cPlayer2) {
        if (OfflineBossAI.isBigBoomMelee(cPlayer, cPlayer2)) {
            OfflineBossAI.explodeAndDie(cPlayer, 40, cPlayer.hp);
            return;
        }
        if (bigBoomDropTurn) {
            bigBoomDropTurn = false;
            int n = cPlayer2.x > cPlayer.x ? CRes.random(70, 75) : CRes.random(110, 115);
            int n2 = Math.abs(cPlayer.x - cPlayer2.x) / 20;
            n2 = Math.max(8, Math.min(30, n2));
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, n, n2, OfflineGunPhysics.forBulletId((byte)34));
            OfflineBossAI.rememberSmallBombSpawn(sArray);
            OfflineBossAI.firePath(cPlayer, (byte)34, sArray, n, 0, 0);
            return;
        }
        int n = CRes.angle(cPlayer2.x - cPlayer.x, cPlayer.y - cPlayer2.y);
        OfflineBossAI.fire(cPlayer, cPlayer2, (byte)7, n, 30, 250, 30);
    }

    private static void actSmallBoom(CPlayer cPlayer, CPlayer cPlayer2) {
        if (OfflineBossAI.isSmallBoomMelee(cPlayer, cPlayer2)) {
            OfflineBossAI.explodeAndDie(cPlayer, 30, 600);
            return;
        }
        int n = cPlayer.x > cPlayer2.x ? 70 : 110;
        OfflineBossAI.fire(cPlayer, cPlayer2, (byte)5, n, 5, 0, 0);
    }

    private static void actRobot(CPlayer cPlayer, CPlayer cPlayer2) {
        if (Math.abs(cPlayer.x - cPlayer2.x) <= 40) {
            if (Math.abs(cPlayer.y - cPlayer2.y) <= 40) {
                OfflineBossAI.fire(cPlayer, cPlayer2, (byte)35, OfflineBossAI.horizontalAngle(cPlayer, cPlayer2), 1, 1200, 50);
                OfflineBossAI.queueSecondShot((byte)36, 0, 0, 15, 29, 80, 99);
            } else {
                OfflineBossAI.fire(cPlayer, cPlayer2, (byte)36, CRes.random(80, 100), CRes.random(15, 30), 0, 0);
            }
            return;
        }
        byte[] byArray = new byte[]{0, 2, 10, 6, 7, 36, 36, 36, 36};
        byte by = byArray[CRes.random(0, byArray.length)];
        OfflineBossAI.fireAimed(cPlayer, cPlayer2, by, 60, 30, OfflineBossAI.robotBulletProfile(by), OfflineBossAI.defaultRadius(by));
    }

    private static void actRobotSpider(CPlayer cPlayer, CPlayer cPlayer2) {
        int n;
        byte by;
        if (Math.abs(cPlayer.x - cPlayer2.x) <= 40 && Math.abs(cPlayer.y - cPlayer2.y) <= 30) {
            OfflineBossAI.fire(cPlayer, cPlayer2, (byte)8, CRes.random(180, 360), CRes.random(1, 5), 300, 22);
            OfflineBossAI.queueSecondShot((byte)36, 0, 0, 15, 29, 80, 99);
            return;
        }
        byte[] byArray = new byte[]{8, 14, 33};
        by = byArray[CRes.random(0, byArray.length)];
        int[] nArray = OfflineBulletSim.findBossTrajectoryAim(cPlayer, cPlayer2, by, by == 8 ? -45 : 60, by == 8 ? 1 : 10);
        n = by == 8 ? 300 : (by == 14 ? 500 : 600);
        int n3 = by == 8 ? 22 : (by == 14 ? 30 : 25);
        OfflineBossAI.fire(cPlayer, cPlayer2, by, nArray[0], nArray[1], n, n3);
        if (by == 8) {
            OfflineBossAI.queueSecondShot((byte)36, 0, 0, 15, 29, 80, 99);
        }
    }

    private static void actTrex(CPlayer cPlayer, CPlayer cPlayer2) {
        if (Math.abs(cPlayer.x - cPlayer2.x) <= 90 && Math.abs(cPlayer.y - cPlayer2.y) <= 250) {
            OfflineBossAI.fire(cPlayer, cPlayer2, (byte)35, OfflineBossAI.horizontalAngle(cPlayer, cPlayer2), 1, 1200, 250);
            return;
        }
        int n = CRes.random(0, 3);
        if (n == 0) {
            short[][] sArray = OfflineBulletSim.buildBossBigRocketPath(cPlayer, cPlayer2.x);
            OfflineBossAI.firePath(cPlayer, (byte)37, sArray, 110, 570, 150);
        } else {
            byte by = (byte)(n == 1 ? 40 : 41);
            int n2 = cPlayer2.x + CRes.random(-50, 51);
            short[][] sArray = OfflineBossAI.aimedPath(cPlayer, by, n2, cPlayer2.y, 60, 15);
            OfflineBossAI.firePath(cPlayer, by, sArray, 60, by == 40 ? 220 : 200, 30);
        }
    }

    private static void actUfo(CPlayer cPlayer, CPlayer cPlayer2) {
        boolean bl;
        boolean bl2 = bl = actingBossIndex >= 0 && actingBossIndex < ufoLoaded.length && ufoLoaded[actingBossIndex];
        if (bl && !OfflineBossAI.hasLandBelow(cPlayer.x, cPlayer.y)) {
            OfflineBossAI.ufoLoaded[OfflineBossAI.actingBossIndex] = false;
            bl = false;
        }
        if (!bl) {
            int n = cPlayer2.x;
            int n2 = cPlayer2.y - CRes.random(150, 500);
            if (actingBossIndex >= 0 && actingBossIndex < ufoLoaded.length) {
                OfflineBossAI.ufoLoaded[OfflineBossAI.actingBossIndex] = true;
                OfflineBossAI.ufoMoveTurn[OfflineBossAI.actingBossIndex] = true;
            }
            if (GameScr.pm != null) {
                GameScr.pm.flyTo(actingBossIndex, (short)n, (short)n2);
            }
            return;
        }
        OfflineBossAI.ufoLoaded[OfflineBossAI.actingBossIndex] = false;
        short[][] sArray = OfflineBulletSim.simulateStraight(cPlayer.x, cPlayer.y, 0, 30, cPlayer);
        OfflineBossAI.firePath(cPlayer, (byte)42, sArray, 270, 1000, 32);
    }

    private static void actBalloon(CPlayer cPlayer, CPlayer cPlayer2) {
        CPlayer cPlayer3 = OfflineBossAI.liveBossByGun((byte)18);
        CPlayer cPlayer4 = OfflineBossAI.liveBossByGun((byte)19);
        CPlayer cPlayer5 = OfflineBossAI.liveBossByGun((byte)20);
        CPlayer cPlayer6 = OfflineBossAI.liveBossByGun((byte)21);
        if (!balloonEyeSpawned && (cPlayer3 == null && cPlayer4 == null || cPlayer5 == null)) {
            cPlayer6 = OfflineBossFight.createBalloonEye(cPlayer);
            boolean bl = balloonEyeSpawned = cPlayer6 != null;
        }
        if (cPlayer3 == null && cPlayer4 == null && cPlayer6 == null) {
            OfflineBossAI.killBossPart(cPlayer);
            OfflineBossAI.killBossPart(cPlayer5);
            OfflineCombat.checkBattleEndNow();
            OfflineBossAI.finishTurn();
            return;
        }
        int n = 0;
        while (n++ < 3) {
            if ((balloonTurnPhase = (balloonTurnPhase + 1) % 3) == 0 && cPlayer3 != null) {
                OfflineBossAI.relocateBalloon(cPlayer);
                OfflineBossAI.actBalloonGun(cPlayer, cPlayer2);
                return;
            }
            if (balloonTurnPhase == 1 && cPlayer4 != null && cPlayer5 != null) {
                OfflineBossAI.actBalloonBombRun(cPlayer);
                return;
            }
            if (balloonTurnPhase != 2 || cPlayer6 == null) continue;
            OfflineBossAI.actBalloonEye(cPlayer, cPlayer2);
            return;
        }
        OfflineBossAI.finishTurn();
    }

    private static void relocateBalloon(CPlayer cPlayer) {
        int n = cPlayer.x;
        int n2 = cPlayer.y;
        int n3 = CRes.random(100, Math.max(101, MM.mapWidth - 99));
        int n4 = CRes.random(-150, 51);
        GameScr.pm.updatePlayerXY(actingBossIndex, (short)n3, (short)n4);
        OfflineBossAI.relocateBalloonPart((byte)18, n, n2, n3, n4);
        OfflineBossAI.relocateBalloonPart((byte)19, n, n2, n3, n4);
        OfflineBossAI.relocateBalloonPart((byte)20, n, n2, n3, n4);
        OfflineBossAI.relocateBalloonPart((byte)21, n, n2, n3, n4);
    }

    private static void relocateBalloonPart(byte by, int n, int n2, int n3, int n4) {
        CPlayer cPlayer = OfflineBossAI.liveBossByGun(by);
        if (cPlayer == null) {
            return;
        }
        int n5 = OfflineBossAI.indexOf(cPlayer);
        if (n5 < 0) {
            return;
        }
        int n6 = cPlayer.x - n;
        int n7 = cPlayer.y - n2;
        GameScr.pm.updatePlayerXY(n5, (short)(n3 + n6), (short)(n4 + n7));
    }

    private static int indexOf(CPlayer cPlayer) {
        if (PM.p == null || cPlayer == null) {
            return -1;
        }
        for (int i = 0; i < PM.p.length; ++i) {
            if (PM.p[i] != cPlayer) continue;
            return i;
        }
        return -1;
    }

    private static void actBalloonBombRun(CPlayer cPlayer) {
        short[][][] sArrayArray = new short[10][][];
        int n = cPlayer.x;
        int n2 = cPlayer.y + 50;
        for (int i = 0; i < 10; ++i) {
            if (i > 0) {
                n += 105;
            }
            if (n > MM.mapWidth) {
                n = 105 - (n - MM.mapWidth);
            }
            sArrayArray[i] = OfflineBulletSim.simulateStraight(n, n2, 0, 20, cPlayer);
        }
        OfflineBossAI.firePaths(cPlayer, (byte)43, sArrayArray, 270, 300, OfflineBossAI.defaultRadius((byte)43));
    }

    private static void actBalloonGun(CPlayer cPlayer, CPlayer cPlayer2) {
        short[][][] sArrayArray = new short[15][][];
        int n = OfflineBossAI.aimedAngle(cPlayer, cPlayer2, 35);
        for (int i = 0; i < 15; ++i) {
            int n2 = (10 * CRes.cos(n) >> 10) + CRes.random(-10, 11);
            int n3 = -(10 * CRes.sin(n) >> 10);
            OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)44);
            sArrayArray[i] = OfflineBulletSim.buildPathFromPoint(cPlayer, cPlayer.x + 51, cPlayer.y + 40, n2, n3, phys.ax100, phys.ay100, phys.g100);
        }
        cPlayer.look = cPlayer2.x >= cPlayer.x ? 2 : 0;
        OfflineBossAI.firePaths(cPlayer, (byte)44, sArrayArray, n, 100, OfflineBossAI.defaultRadius((byte)44));
    }

    private static void actBalloonEye(CPlayer cPlayer, CPlayer cPlayer2) {
        short[][] sArray = OfflineBossAI.buildLinePath(cPlayer.x + 65, cPlayer.y - 27, cPlayer2.x, cPlayer2.y - 12, 12);
        OfflineBossAI.firePath(cPlayer, (byte)45, sArray, OfflineBossAI.aimedAngle(cPlayer, cPlayer2, 35), 500, OfflineBossAI.defaultRadius((byte)45));
    }

    private static void actCaveSpider(CPlayer cPlayer, CPlayer cPlayer2) {
        if (actingBossIndex < 0 || actingBossIndex >= spiderTurnCooldown.length) {
            OfflineBossAI.finishTurn();
            return;
        }
        int n = cPlayer.x;
        if (cPlayer2.y - 150 > cPlayer.y && spiderTurnCooldown[actingBossIndex] == 0) {
            OfflineBossAI.spiderTurnCooldown[OfflineBossAI.actingBossIndex] = 3;
            OfflineBossAI.spiderReturnX[OfflineBossAI.actingBossIndex] = n;
            OfflineBossAI.spiderActionTurn[OfflineBossAI.actingBossIndex] = true;
            OfflineBossAI.spiderCaptureTurn[OfflineBossAI.actingBossIndex] = true;
            GameScr.pm.updatePlayerXY(actingBossIndex, (short)cPlayer2.x, (short)cPlayer.y);
            cPlayer.capture(GameScr.myIndex);
            return;
        }
        if (spiderTurnCooldown[actingBossIndex] > 0) {
            int n2 = actingBossIndex;
            spiderTurnCooldown[n2] = (byte)(spiderTurnCooldown[n2] - 1);
        }
        if (!cPlayer2.isPoison) {
            GameScr.pm.updatePlayerXY(actingBossIndex, (short)cPlayer2.x, (short)cPlayer.y);
            cPlayer2.isPoison = true;
            cPlayer2.poisonEff = true;
            GameScr.pm.updatePlayerXY(actingBossIndex, (short)n, (short)cPlayer.y);
            OfflineBossAI.finishTurn();
            return;
        }
        int n3 = CRes.random(50, Math.max(51, MM.mapWidth - 49));
        GameScr.pm.flyTo(actingBossIndex, (short)n3, (short)cPlayer.y);
        OfflineBossAI.spiderActionTurn[OfflineBossAI.actingBossIndex] = true;
        OfflineBossAI.spiderAcidTurn[OfflineBossAI.actingBossIndex] = true;
    }

    private static void fireSpiderAcid(CPlayer cPlayer, CPlayer cPlayer2) {
        short[][][] sArrayArray = new short[5][][];
        int n = OfflineBossAI.aimedAngle(cPlayer, cPlayer2, 55);
        for (int i = 0; i < 5; ++i) {
            int n2 = n + i * 5;
            int n3 = cPlayer.x + (30 * CRes.cos(n2) >> 10);
            int n4 = cPlayer.y - 12 - (30 * CRes.sin(n2) >> 10);
            int n5 = 10 * CRes.cos(n2) >> 10;
            int n6 = -(10 * CRes.sin(n2) >> 10);
            OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)47);
            sArrayArray[i] = OfflineBulletSim.buildPathFromPoint(cPlayer, n3, n4, n5, n6, phys.ax100, phys.ay100, phys.g100);
        }
        cPlayer.look = cPlayer2.x >= cPlayer.x ? 2 : 0;
        OfflineBossAI.firePaths(cPlayer, (byte)47, sArrayArray, n, 400, OfflineBossAI.defaultRadius((byte)47));
    }

    private static void dropSpiderSilk(CPlayer cPlayer, CPlayer cPlayer2) {
        short[] sArray = new short[]{(short)cPlayer.x, (short)cPlayer.x};
        short[] sArray2 = new short[]{(short)(cPlayer.y - 12), (short)(cPlayer.y + 26)};
        GameScr.pm.updatePlayerXY(GameScr.myIndex, (short)cPlayer.x, (short)(cPlayer.y + 23));
        cPlayer2.falling = false;
        OfflineBossAI.firePath(cPlayer, (byte)8, new short[][]{sArray, sArray2}, 270, 0, 0);
        if (actingBossIndex >= 0 && actingBossIndex < spiderReturnX.length) {
            GameScr.pm.updatePlayerXY(actingBossIndex, (short)spiderReturnX[actingBossIndex], (short)cPlayer.y);
        }
    }

    private static void actGhost(CPlayer cPlayer, CPlayer cPlayer2) {
        int n = cPlayer.x > cPlayer2.x ? cPlayer2.x + 30 : cPlayer2.x - 30;
        int n2 = cPlayer2.y - 15;
        GameScr.pm.flyTo(actingBossIndex, (short)n, (short)n2);
        cPlayer.checkGhostLook(cPlayer2.x, cPlayer.x);
        cPlayer.ghostHit(GameScr.myIndex);
        OfflineBossAI.ghostActionTicks[OfflineBossAI.actingBossIndex] = 0;
        OfflineBossAI.ghostActionTurn[OfflineBossAI.actingBossIndex] = true;
    }

    private static void finishGhostAttack(CPlayer cPlayer, CPlayer cPlayer2) {
        int n = Math.max(1, MM.mapHeight - 199);
        int n2 = cPlayer.gun == 25 ? CRes.random(100, Math.max(101, MM.mapWidth - 99)) : (cPlayer.x > cPlayer2.x && cPlayer2.x < MM.mapWidth - 80 ? cPlayer2.x + 80 : (cPlayer2.x > 80 ? cPlayer2.x - 80 : cPlayer2.x + 80));
        int n3 = CRes.random(0, n);
        GameScr.pm.flyTo(actingBossIndex, (short)n2, (short)n3);
        int n4 = cPlayer.gun == 25 ? CRes.random(300, 601) : CRes.random(200, 501);
        OfflineCombat.applyBossDirectDamage(cPlayer2, n4);
        OfflineBossAI.finishTurn();
    }

    private static short[][] buildLinePath(int n, int n2, int n3, int n4, int n5) {
        int n6 = Math.max(Math.abs(n3 - n), Math.abs(n4 - n2));
        int n7 = Math.max(2, n6 / Math.max(1, n5) + 2);
        short[] sArray = new short[n7];
        short[] sArray2 = new short[n7];
        for (int i = 0; i < n7; ++i) {
            sArray[i] = (short)(n + (n3 - n) * i / (n7 - 1));
            sArray2[i] = (short)(n2 + (n4 - n2) * i / (n7 - 1));
        }
        return new short[][]{sArray, sArray2};
    }

    private static CPlayer liveBossByGun(byte by) {
        if (PM.p == null) {
            return null;
        }
        for (int i = 0; i < PM.p.length; ++i) {
            CPlayer cPlayer = PM.p[i];
            if (!(cPlayer instanceof Boss) || cPlayer.gun != by || cPlayer.hp <= 0 || cPlayer.getState() == 5) continue;
            return cPlayer;
        }
        return null;
    }

    private static void killBossPart(CPlayer cPlayer) {
        if (cPlayer != null && cPlayer.hp > 0) {
            cPlayer.updateHP(0, (byte)0);
        }
    }

    private static boolean hasLandBelow(int n, int n2) {
        if (GameScr.mm == null) {
            return true;
        }
        for (int i = n2; i <= MM.mapHeight + 100; ++i) {
            if (!GameScr.mm.isLand(n, i)) continue;
            return true;
        }
        return false;
    }

    private static void queueSecondShot(byte by, int n, int n2, int n3, int n4, int n5, int n6) {
        secondShotPending = true;
        secondBulletId = by;
        secondDamage = n;
        secondRadius = n2;
        secondForceMin = n3;
        secondForceMax = n4;
        secondAngleMin = n5;
        secondAngleMax = n6;
    }

    private static int robotBulletProfile(byte by) {
        switch (by) {
            case 0: {
                return 280;
            }
            case 2: {
                return 300;
            }
            case 10: {
                return 321;
            }
            case 6: {
                return 600;
            }
            case 7: {
                return 250;
            }
        }
        return 0;
    }

    private static void fireAimed(CPlayer cPlayer, CPlayer cPlayer2, byte by, int n, int n2, int n3, int n4) {
        int n5 = OfflineBossAI.aimedAngle(cPlayer, cPlayer2, n);
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId(by);
        int n6 = OfflineBossAI.bestForce(cPlayer, n5, phys, cPlayer2.x, n2);
        OfflineBossAI.fire(cPlayer, cPlayer2, by, n5, n6, n3, n4);
    }

    private static void fire(CPlayer cPlayer, CPlayer cPlayer2, byte by, int n, int n2, int n3, int n4) {
        cPlayer.look = cPlayer2.x >= cPlayer.x ? 2 : 0;
        cPlayer.angle = n;
        if (by == 35) {
            short[][] sArray = OfflineBulletSim.simulateStraight(cPlayer.x, cPlayer.y - 12, cPlayer2.x >= cPlayer.x ? 8 : -8, 0);
            OfflineBossAI.firePath(cPlayer, by, sArray, n, n3, n4);
        } else {
            short[][][] sArray = OfflineBulletSim.buildBossPaths(cPlayer, by, (byte)OfflineBulletSim.clampForce(n2));
            OfflineBossAI.firePaths(cPlayer, by, sArray, n, n3, n4);
        }
    }

    private static void firePath(CPlayer cPlayer, byte by, short[][] sArray, int n, int n2, int n3) {
        if (sArray == null) {
            OfflineBossAI.finishTurn();
            return;
        }
        OfflineBossAI.firePaths(cPlayer, by, new short[][][]{sArray}, n, n2, n3);
    }

    private static void firePaths(CPlayer cPlayer, byte by, short[][][] sArray, int n, int n2, int n3) {
        byte by2 = (byte)actingBossIndex;
        if (by2 < 0 || sArray == null || sArray.length == 0) {
            OfflineBossAI.finishTurn();
            return;
        }
        short[][] sArrayArray = new short[sArray.length][];
        short[][] sArrayArray2 = new short[sArray.length][];
        for (int i = 0; i < sArray.length; ++i) {
            if (sArray[i] == null || sArray[i].length < 2 || sArray[i][0] == null || sArray[i][1] == null || sArray[i][0].length == 0 || sArray[i][1].length == 0) {
                OfflineBossAI.finishTurn();
                return;
            }
            sArrayArray[i] = sArray[i][0];
            sArrayArray2[i] = sArray[i][1];
        }
        cPlayer.angle = n;
        OfflineCombat.setBossShotProfile(by2, by, n2, n3);
        OfflineCombat.rollShotLucky();
        cPlayer.shoot((byte)0, by2, (short)cPlayer.x, (short)cPlayer.y, by, sArrayArray, sArrayArray2, (byte)1, (byte)0, (short)n, null, null, -1, -1);
        BM.force2 = 0;
        shotInFlight = true;
    }

    private static short[][] aimedPath(CPlayer cPlayer, byte by, int n, int n2, int n3, int n4) {
        int n5 = n >= cPlayer.x ? n3 : 180 - n3;
        int n6 = OfflineBossAI.bestForce(cPlayer, n5, OfflineGunPhysics.forBulletId(by), n, n4);
        return OfflineBulletSim.buildPathFromMuzzle(cPlayer, n5, n6, OfflineGunPhysics.forBulletId(by));
    }

    static int bestForce(CPlayer cPlayer, int n, OfflineGunPhysics.Phys phys, int n2, int n3) {
        int n4 = OfflineBulletSim.clampForce(n3);
        int n5 = Math.min(5, n4);
        int n6 = Integer.MAX_VALUE;
        for (int i = 1; i <= n4; i += 2) {
            int n7;
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, n, i, phys);
            if (sArray[0].length <= 0 || (n7 = Math.abs(sArray[0][sArray[0].length - 1] - n2)) >= n6) continue;
            n6 = n7;
            n5 = i;
        }
        return n5;
    }

    private static int defaultRadius(byte by) {
        switch (by) {
            case 0: {
                return 21;
            }
            case 1: {
                return 11;
            }
            case 2: {
                return 18;
            }
            case 6: {
                return 22;
            }
            case 7: {
                return 30;
            }
            case 8: {
                return 22;
            }
            case 10: {
                return 19;
            }
            case 14: {
                return 30;
            }
            case 11: {
                return 11;
            }
            case 33: {
                return 25;
            }
            case 37: {
                return 150;
            }
            case 40:
            case 41: {
                return 30;
            }
            case 42: {
                return 32;
            }
            case 43: {
                return 32;
            }
            case 44: {
                return 11;
            }
            case 45: {
                return 28;
            }
            case 47: {
                return 7;
            }
            case 48: {
                return 18;
            }
        }
        return 0;
    }

    private static int bossAttack(CPlayer cPlayer) {
        switch (cPlayer.gun) {
            case 13: {
                return 300;
            }
            case 14: {
                return 300;
            }
            case 15: {
                return 750;
            }
            case 16: {
                return 600;
            }
            case 17:
            case 18:
            case 21: {
                return 350;
            }
            case 22: {
                return 300;
            }
            case 12: {
                return 300;
            }
        }
        return 300;
    }

    static int aimedAngle(CPlayer cPlayer, CPlayer cPlayer2, int n) {
        int n2 = cPlayer2.x >= cPlayer.x ? n : 180 - n;
        return n2 + CRes.random(-5, 6);
    }

    private static int randomArcAngle(CPlayer cPlayer, CPlayer cPlayer2, int n, int n2) {
        int n3 = CRes.random(n, n2 + 1);
        return cPlayer2.x >= cPlayer.x ? n3 : 180 - n3;
    }

    private static int horizontalAngle(CPlayer cPlayer, CPlayer cPlayer2) {
        return cPlayer2.x >= cPlayer.x ? 0 : 180;
    }

    private static boolean shouldMove(CPlayer cPlayer, CPlayer cPlayer2) {
        if (cPlayer.gun == 12) {
            return !OfflineBossAI.isBigBoomMelee(cPlayer, cPlayer2);
        }
        return cPlayer.gun == 11 && !OfflineBossAI.isSmallBoomMelee(cPlayer, cPlayer2);
    }

    private static void stepTowardPlayer(CPlayer cPlayer, CPlayer cPlayer2) {
        int n;
        cPlayer.isCom = true;
        if (++moveTickCounter < 2) {
            return;
        }
        moveTickCounter = 0;
        int n2 = n = cPlayer2.x < cPlayer.x ? 0 : 2;
        if (OfflineAiMovement.wouldStepOffMap(cPlayer, n)) {
            movePixelsLeft = 0;
            cPlayer.setState((byte)0);
            cPlayer.checkAngleForSprite();
            return;
        }
        int n3 = cPlayer.x;
        cPlayer.move(n);
        int n4 = Math.abs(cPlayer.x - n3);
        if (n4 > 0) {
            movePixelsLeft -= n4;
            if (GameScr.pm != null) {
                GameScr.pm.updatePlayerXY(actingBossIndex, (short)cPlayer.x, (short)cPlayer.y);
            }
        } else {
            movePixelsLeft = 0;
        }
        if (cPlayer.gun == 12 && OfflineBossAI.isBigBoomMelee(cPlayer, cPlayer2) || cPlayer.gun == 11 && OfflineBossAI.isSmallBoomMelee(cPlayer, cPlayer2)) {
            movePixelsLeft = 0;
        }
        if (movePixelsLeft <= 0) {
            cPlayer.setState((byte)0);
            cPlayer.checkAngleForSprite();
        }
    }

    private static void explodeAndDie(CPlayer cPlayer, int n, int n2) {
        new Explosion(cPlayer.x, cPlayer.y - 12, 1);
        OfflineCombat.applyDirectExplosionDamage(cPlayer.x, cPlayer.y - 12, n, n2, false);
        if (cPlayer.hp > 0) {
            cPlayer.updateHP(0, (byte)0);
        }
        turnPending = false;
        shotInFlight = false;
        emptyShotWaitTicks = 0;
        shotFlightTicks = 0;
        OfflineCombat.checkBattleEndNow();
        OfflineCombat.ensureLocalPlayerTurn();
    }

    private static void rememberSmallBombSpawn(short[][] sArray) {
        if (sArray == null || sArray[0].length == 0) {
            return;
        }
        int n = sArray[0].length - 1;
        spawnSmallBombX = sArray[0][n];
        spawnSmallBombY = sArray[1][n];
        spawnSmallBombPending = true;
    }

    private static void spawnSmallBomb(int n, int n2, CPlayer cPlayer) {
        int n3 = OfflineBossAI.firstEmptyBossSlot();
        if (n3 < 0) {
            return;
        }
        int n4 = 1000;
        Boss boss = new Boss(-1000 - n3, (byte)n3, n, n2, true, (byte)(cPlayer.x < n ? 0 : 2), (byte)11, n4);
        boss.name = "Bomb nh\u1ecf";
        boss.hp = n4;
        boss.maxhp = n4;
        boss.team = false;
        boss.falling = true;
        PM.p[n3] = boss;
        new Explosion(n, n2 - 12, 0);
    }

    private static boolean isOutOfMapBounds(CPlayer cPlayer) {
        return cPlayer.y > MM.mapHeight + 200 || cPlayer.x < -100 || cPlayer.x > MM.mapWidth + 100;
    }

    private static boolean isBigBoomMelee(CPlayer cPlayer, CPlayer cPlayer2) {
        return Math.abs(cPlayer.x - cPlayer2.x) <= 35 && Math.abs(cPlayer.y - cPlayer2.y) <= 35;
    }

    private static boolean isSmallBoomMelee(CPlayer cPlayer, CPlayer cPlayer2) {
        return Math.abs(cPlayer.x - cPlayer2.x) <= 25 && Math.abs(cPlayer.y - cPlayer2.y) <= 25;
    }

    private static boolean isRobotMelee(CPlayer cPlayer, CPlayer cPlayer2) {
        return Math.abs(cPlayer.x - cPlayer2.x) <= 40 && Math.abs(cPlayer.y - cPlayer2.y) <= 40;
    }

    private static void finishTurn() {
        turnPending = false;
        shotInFlight = false;
        secondShotPending = false;
        OfflineCombat.clearBossShotProfile();
        CPlayer cPlayer = OfflineBossAI.bossAt(actingBossIndex);
        if (cPlayer != null && cPlayer.hp > 0) {
            if (cPlayer.gun == 17) {
                cPlayer.stopServerFlight();
            }
            cPlayer.setState((byte)0);
        }
        OfflineCombat.ensureLocalPlayerTurn();
    }

    private static CPlayer localPlayer() {
        if (PM.p == null || GameScr.myIndex < 0 || GameScr.myIndex >= PM.p.length) {
            return null;
        }
        return PM.p[GameScr.myIndex];
    }

    private static CPlayer bossAt(int n) {
        if (PM.p == null || n < 0 || n >= PM.p.length) {
            return null;
        }
        CPlayer cPlayer = PM.p[n];
        return cPlayer instanceof Boss && cPlayer.hp > 0 ? cPlayer : null;
    }

    private static int findNextBossIndex() {
        if (PM.p == null || PM.p.length == 0) {
            return -1;
        }
        for (int i = 1; i <= PM.p.length; ++i) {
            int n = (lastBossIndex + i + PM.p.length) % PM.p.length;
            if (OfflineBossAI.bossAt(n) == null || OfflineBossAI.isPassiveBalloonPart(PM.p[n])) continue;
            return n;
        }
        return -1;
    }

    private static boolean isPassiveBalloonPart(CPlayer cPlayer) {
        return cPlayer != null && (cPlayer.gun == 18 || cPlayer.gun == 19 || cPlayer.gun == 20 || cPlayer.gun == 21);
    }

    private static CPlayer findMainBoss() {
        if (PM.p == null) {
            return null;
        }
        for (int i = 0; i < PM.p.length; ++i) {
            if (!(PM.p[i] instanceof Boss) || PM.p[i].hp <= 0) continue;
            return PM.p[i];
        }
        return null;
    }

    private static int firstEmptyBossSlot() {
        if (PM.p == null) {
            return -1;
        }
        for (int i = 8; i < PM.p.length; ++i) {
            if (PM.p[i] != null) continue;
            return i;
        }
        return -1;
    }

    private static boolean isAnyoneFalling() {
        if (PM.p == null) {
            return false;
        }
        for (int i = 0; i < PM.p.length; ++i) {
            if (PM.p[i] == null || !PM.p[i].falling || PM.p[i].getState() == 5) continue;
            return true;
        }
        return false;
    }

    static {
        actingBossIndex = -1;
        lastBossIndex = -1;
        ufoLoaded = new boolean[0];
        ufoMoveTurn = new boolean[0];
        spiderActionTurn = new boolean[0];
        spiderCaptureTurn = new boolean[0];
        spiderAcidTurn = new boolean[0];
        spiderWaitPlayerLand = new boolean[0];
        spiderTurnCooldown = new byte[0];
        spiderReturnX = new int[0];
        ghostActionTurn = new boolean[0];
        ghostActionTicks = new byte[0];
    }
}

