/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineAiMovement;
import com.teamobi.mobiarmy2.OfflineBotUfo;
import com.teamobi.mobiarmy2.OfflineBulletSim;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineGunPhysics;
import com.teamobi.mobiarmy2.OfflineItemLogic;
import com.teamobi.mobiarmy2.OfflinePvpBot;
import com.teamobi.mobiarmy2.OfflineSpecialBullets;
import item.BM;
import model.CRes;
import player.CPlayer;
import player.PM;
import screen.GameScr;

public final class OfflinePvpBotAI {
    private static final int THINK_TICKS = 35;
    private static final int MOVE_TICKS_PER_PIXEL = 2;
    private static final int IDEAL_RANGE_MIN = 90;
    private static final int IDEAL_RANGE_MAX = 380;
    private static boolean turnPending;
    private static int thinkTicks;
    private static boolean shotInFlight;
    private static int actingIndex;
    private static int lastBotIndex;
    private static int shotFlightTicks;
    private static int emptyShotWaitTicks;
    private static int movePixelsLeft;
    private static int moveTickCounter;
    private static boolean movingAwayFromTarget;
    private static final int MAX_FULL_HEAL_USES = 4;
    private static final int[] fullHealUsesLeft;
    private static final int[] botInvisibleTurns;
    private static final int[] botVampireTurns;
    private static final int[] SPLIT_FRACTION_PCT;
    private static final int[] ANGLE_CANDIDATES;
    private static final int[] ATTACK_ITEM_IDS;

    private OfflinePvpBotAI() {
    }

    public static void reset() {
        turnPending = false;
        thinkTicks = 0;
        shotInFlight = false;
        actingIndex = -1;
        lastBotIndex = -1;
        shotFlightTicks = 0;
        emptyShotWaitTicks = 0;
        movePixelsLeft = 0;
        moveTickCounter = 0;
        for (int i = 0; i < fullHealUsesLeft.length; ++i) {
            OfflinePvpBotAI.fullHealUsesLeft[i] = 4;
            OfflinePvpBotAI.botInvisibleTurns[i] = 0;
            OfflinePvpBotAI.botVampireTurns[i] = 0;
        }
        OfflineBotUfo.reset();
    }

    public static boolean isBotInvisible(int n) {
        return n >= 0 && n < botInvisibleTurns.length && botInvisibleTurns[n] > 0;
    }

    public static void onBotDamageDealt(int n, int n2) {
        if (n < 0 || n >= botVampireTurns.length || botVampireTurns[n] <= 0 || n2 <= 0) {
            return;
        }
        CPlayer cPlayer = OfflinePvpBotAI.botAt(n);
        if (cPlayer == null || cPlayer.maxhp <= 0) {
            return;
        }
        int n3 = n2 / 2;
        int n4 = Math.min(cPlayer.maxhp, cPlayer.hp + n3);
        byte by = (byte)(n4 * 25 / cPlayer.maxhp);
        cPlayer.updateHP(n4, by);
    }

    private static CPlayer botAt(int n) {
        if (PM.p == null || n < 0 || n >= PM.p.length) {
            return null;
        }
        int n2 = OfflinePvpBot.activeSquadCount;
        int n3 = n2 + OfflinePvpBot.activeBotCount;
        if (n < n2 || n >= n3) {
            return null;
        }
        CPlayer cPlayer = PM.p[n];
        return cPlayer != null && cPlayer.hp > 0 && cPlayer.getState() != 5 ? cPlayer : null;
    }

    private static int findNextBotIndex() {
        int n = OfflinePvpBot.activeSquadCount;
        int n2 = OfflinePvpBot.activeBotCount;
        if (n2 <= 0) {
            return -1;
        }
        for (int i = 1; i <= n2; ++i) {
            int n3 = n + (lastBotIndex - n + i + n2) % n2;
            if (OfflinePvpBotAI.botAt(n3) == null) continue;
            return n3;
        }
        return -1;
    }

    static CPlayer target() {
        int n;
        int n2 = OfflinePvpBot.activeSquadCount;
        int n3 = 0;
        for (n = 0; n < n2 && n < PM.p.length; ++n) {
            if (PM.p[n] == null || PM.p[n].hp <= 0 || PM.p[n].getState() == 5) continue;
            ++n3;
        }
        if (n3 == 0) {
            return null;
        }
        int n4 = CRes.random(0, n3);
        for (n = 0; n < n2 && n < PM.p.length; ++n) {
            CPlayer cPlayer = PM.p[n];
            if (cPlayer == null || cPlayer.hp <= 0 || cPlayer.getState() == 5) continue;
            if (n4 == 0) {
                return cPlayer;
            }
            --n4;
        }
        return null;
    }

    public static void startBotTurn() {
        try {
            OfflinePvpBotAI.startBotTurnImpl();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            turnPending = false;
            OfflineCombat.ensureLocalPlayerTurn();
        }
    }

    private static void startBotTurnImpl() {
        if (GameScr.res != null && !GameScr.res.equals("")) {
            return;
        }
        actingIndex = OfflinePvpBotAI.findNextBotIndex();
        CPlayer cPlayer = OfflinePvpBotAI.botAt(actingIndex);
        CPlayer cPlayer2 = OfflinePvpBotAI.target();
        if (cPlayer == null || cPlayer2 == null) {
            OfflineCombat.ensureLocalPlayerTurn();
            return;
        }
        lastBotIndex = actingIndex;
        OfflineItemLogic.onBossTurn(cPlayer, actingIndex);
        if (cPlayer.hp <= 0) {
            OfflineCombat.checkBattleEndNow();
            OfflineCombat.ensureLocalPlayerTurn();
            return;
        }
        OfflineCombat.rollWind();
        OfflineCombat.advanceTurnCounter();
        PM.curP = (byte)actingIndex;
        cPlayer.active = true;
        int n = cPlayer.look = cPlayer2.x >= cPlayer.x ? 2 : 0;
        if (GameScr.cam != null) {
            GameScr.cam.setPlayerMode(actingIndex);
        }
        cPlayer.itemUsed = -1;
        cPlayer.isUsedItem = false;
        if (actingIndex >= 0 && actingIndex < botInvisibleTurns.length) {
            if (botInvisibleTurns[actingIndex] > 0) {
                int n2 = actingIndex;
                botInvisibleTurns[n2] = botInvisibleTurns[n2] - 1;
                if (botInvisibleTurns[n2] == 0) {
                    cPlayer.isInvisible = false;
                }
            }
            if (botVampireTurns[actingIndex] > 0) {
                int n3 = actingIndex;
                botVampireTurns[n3] = botVampireTurns[n3] - 1;
                if (botVampireTurns[n3] == 0) {
                    cPlayer.isVampire = false;
                }
            }
        }
        OfflinePvpBotAI.maybeUseItem(cPlayer, actingIndex, OfflinePvpBot.activeDifficulty);
        OfflinePvpBotAI.maybeUseSupportItem(cPlayer, actingIndex, OfflinePvpBot.activeDifficulty);
        OfflinePvpBotAI.maybeUseAttackItem(cPlayer, OfflinePvpBot.activeDifficulty);
        OfflinePvpBotAI.maybeUsePow(cPlayer, OfflinePvpBot.activeDifficulty);
        OfflinePvpBotAI.decideMovement(cPlayer, cPlayer2, OfflinePvpBot.activeDifficulty);
        turnPending = true;
        thinkTicks = 35;
        shotInFlight = false;
        shotFlightTicks = 0;
        emptyShotWaitTicks = 0;
    }

    private static void decideMovement(CPlayer cPlayer, CPlayer cPlayer2, byte by) {
        int n;
        movePixelsLeft = 0;
        moveTickCounter = 0;
        if (OfflineItemLogic.isFrozen(actingIndex)) {
            return;
        }
        int n2 = Math.abs(cPlayer2.x - cPlayer.x);
        n = by == 0 ? 25 : (by == 3 ? 100 : (by == 2 ? 90 : 55));
        if (CRes.random(0, 100) >= n) {
            return;
        }
        if (n2 > 380) {
            movingAwayFromTarget = false;
            movePixelsLeft = Math.min(n2 - 380 + 40, 140);
        } else if (n2 < 90) {
            movingAwayFromTarget = true;
            movePixelsLeft = Math.min(90 - n2 + 40, 140);
        }
    }

    private static void stepBot(CPlayer cPlayer, CPlayer cPlayer2) {
        int n;
        boolean bl;
        if (++moveTickCounter < 2) {
            return;
        }
        moveTickCounter = 0;
        boolean bl2 = bl = cPlayer2.x < cPlayer.x;
        if (movingAwayFromTarget) {
            n = bl ? 2 : 0;
        } else {
            int n2 = n = bl ? 0 : 2;
        }
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
                GameScr.pm.updatePlayerXY(actingIndex, (short)cPlayer.x, (short)cPlayer.y);
            }
        } else {
            movePixelsLeft = 0;
        }
        if (movePixelsLeft <= 0) {
            cPlayer.setState((byte)0);
            cPlayer.checkAngleForSprite();
        }
    }

    public static void update() {
        if (!turnPending || GameScr.res != null && !GameScr.res.equals("")) {
            return;
        }
        CPlayer cPlayer = OfflinePvpBotAI.botAt(actingIndex);
        CPlayer cPlayer2 = OfflinePvpBotAI.target();
        if (cPlayer == null || cPlayer.hp <= 0 || cPlayer2 == null) {
            OfflinePvpBotAI.finishTurn();
            return;
        }
        if (!shotInFlight) {
            if (thinkTicks > 0) {
                --thinkTicks;
                return;
            }
            try {
                if (movePixelsLeft > 0) {
                    OfflinePvpBotAI.stepBot(cPlayer, cPlayer2);
                    return;
                }
                OfflinePvpBotAI.act(cPlayer, cPlayer2);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                OfflinePvpBotAI.finishTurn();
            }
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
        if (OfflinePvpBotAI.isAnyoneFalling() && ++emptyShotWaitTicks < 60) {
            return;
        }
        OfflinePvpBotAI.finishTurn();
    }

    private static boolean isAnyoneFalling() {
        if (PM.p == null) {
            return false;
        }
        for (int i = 0; i < PM.p.length; ++i) {
            if (PM.p[i] == null || !PM.p[i].falling) continue;
            return true;
        }
        return false;
    }

    private static void act(CPlayer cPlayer, CPlayer cPlayer2) {
        byte by = cPlayer.resolveCurrentShotBulletType();
        int n = cPlayer.maxforce > 0 ? cPlayer.maxforce : 30;
        int[] nArray = OfflinePvpBotAI.pickBestAngleForce(cPlayer, cPlayer2, by, n);
        int n2 = nArray[0];
        int n3 = nArray[1];
        byte by2 = (byte)nArray[2];
        cPlayer.angle = n2;
        cPlayer.look = cPlayer2.x >= cPlayer.x ? 2 : 0;
        shotInFlight = true;
        OfflineCombat.onWaitForFire(by, (short)cPlayer.x, (short)cPlayer.y, (short)n2, (byte)n3, by2, (byte)1);
    }

    private static int[] pickBestAngleForce(CPlayer cPlayer, CPlayer cPlayer2, byte by, int n) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId(by);
        boolean bl = cPlayer2.x >= cPlayer.x;
        boolean bl2 = by == 17 || by == 19;
        boolean bl3 = by == 58;
        boolean bl4 = cPlayer.itemUsed == 100;
        int n2 = cPlayer.angle;
        int n3 = bl ? 55 : 125;
        int n4 = Math.max(1, n / 2);
        int n5 = 2;
        long l = Long.MAX_VALUE;
        int n6 = 0;
        while (n6 < ANGLE_CANDIDATES.length) {
            long l2;
            int n7;
            int n8;
            cPlayer.angle = n8 = bl ? ANGLE_CANDIDATES[n6] : 180 - ANGLE_CANDIDATES[n6];
            if (bl3) {
                n7 = OfflinePvpBotAI.refineForceDraby(cPlayer, n8, phys, cPlayer2.x, cPlayer2.y, n, by, bl4);
                l2 = OfflinePvpBotAI.drabyWeightedError(cPlayer, by, n7, bl4, cPlayer2.x, cPlayer2.y);
                if (l2 >= 0L && l2 < l) {
                    l = l2;
                    n3 = n8;
                    n4 = n7;
                    n5 = 2;
                }
                ++n6;
                continue;
            }
            n7 = OfflinePvpBotAI.refineForce(cPlayer, n8, phys, cPlayer2.x, cPlayer2.y, n);
            if (bl2) {
                short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, n8, n7, phys);
                if (sArray != null && sArray.length >= 2 && sArray[0] != null && sArray[0].length >= 3) {
                    int n9 = sArray[0].length;
                    for (int i = 0; i < SPLIT_FRACTION_PCT.length; ++i) {
                        byte by2;
                        long l3;
                        int n10 = 2 + (n9 - 1 - 2) * SPLIT_FRACTION_PCT[i] / 100;
                        if (n10 < 2) {
                            n10 = 2;
                        }
                        if (n10 > 250) {
                            n10 = 250;
                        }
                        if (n10 >= n9) {
                            n10 = n9 - 1;
                        }
                        if ((l3 = OfflinePvpBotAI.averageLandingError(OfflineSpecialBullets.build(cPlayer, by, n7, by2 = (byte)n10), cPlayer2)) < 0L || l3 >= l) continue;
                        l = l3;
                        n3 = n8;
                        n4 = n7;
                        n5 = n10;
                    }
                }
            } else {
                l2 = OfflinePvpBotAI.averageLandingError(OfflineBulletSim.buildAllPaths(cPlayer, by, (byte)n7, (byte)2, bl4), cPlayer2);
                if (l2 >= 0L && l2 < l) {
                    l = l2;
                    n3 = n8;
                    n4 = n7;
                    n5 = 2;
                }
            }
            ++n6;
        }
        cPlayer.angle = n2;
        return new int[]{n3, n4, n5};
    }

    private static long averageLandingError(short[][][] sArray, CPlayer cPlayer) {
        if (sArray == null || sArray.length == 0) {
            return -1L;
        }
        long l = 0L;
        int n = 0;
        for (int i = 0; i < sArray.length; ++i) {
            short[][] sArray2 = sArray[i];
            if (sArray2 == null || sArray2.length < 2 || sArray2[0] == null || sArray2[0].length <= 0) continue;
            int n2 = sArray2[0].length - 1;
            long l2 = sArray2[0][n2] - cPlayer.x;
            long l3 = sArray2[1][n2] - cPlayer.y;
            l += l2 * l2 + l3 * l3;
            ++n;
        }
        return n > 0 ? l / (long)n : -1L;
    }

    private static long drabyWeightedError(CPlayer cPlayer, byte by, int n, boolean bl, int n2, int n3) {
        int n4;
        short[][][] sArray = OfflineBulletSim.buildAllPaths(cPlayer, by, (byte)n, (byte)2, bl);
        if (sArray == null || sArray.length == 0 || sArray[0] == null || sArray[0].length < 2 || sArray[0][0] == null || sArray[0][0].length == 0) {
            return -1L;
        }
        int n5 = sArray[0][0].length - 1;
        long l = sArray[0][0][n5] - n2;
        long l2 = sArray[0][1][n5] - n3;
        long l3 = l * l + l2 * l2;
        int n6 = n4 = OfflineBulletSim.lastBounceIndices != null ? OfflineBulletSim.lastBounceIndices.length : 0;
        long l4 = n4 >= 2 ? 1L : (n4 == 1 ? 6L : 30L);
        return l3 * l4;
    }

    private static int refineForceDraby(CPlayer cPlayer, int n, OfflineGunPhysics.Phys phys, int n2, int n3, int n4, byte by, boolean bl) {
        int n5;
        int n6 = OfflineBulletSim.clampForce(n4);
        int n7 = Math.min(5, n6);
        long l = Long.MAX_VALUE;
        for (n5 = 1; n5 <= n6; n5 += 3) {
            long l2 = OfflinePvpBotAI.drabyWeightedError(cPlayer, by, n5, bl, n2, n3);
            if (l2 < 0L || l2 >= l) continue;
            l = l2;
            n7 = n5;
        }
        int n8 = Math.max(1, n7 - 3);
        int n9 = Math.min(n6, n7 + 3);
        for (n5 = n8; n5 <= n9; ++n5) {
            long l3 = OfflinePvpBotAI.drabyWeightedError(cPlayer, by, n5, bl, n2, n3);
            if (l3 < 0L || l3 >= l) continue;
            l = l3;
            n7 = n5;
        }
        return n7;
    }

    private static int refineForce(CPlayer cPlayer, int n, OfflineGunPhysics.Phys phys, int n2, int n3, int n4) {
        long l;
        int n5;
        long l2;
        int n6;
        int n7 = OfflineBulletSim.clampForce(n4);
        int n8 = Math.min(5, n7);
        long l3 = Long.MAX_VALUE;
        for (n6 = 1; n6 <= n7; n6 += 3) {
            long l4;
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, n, n6, phys);
            if (sArray == null || sArray.length < 2 || sArray[0] == null || sArray[0].length <= 0 || (l2 = (l4 = (long)(sArray[0][n5 = sArray[0].length - 1] - n2)) * l4 + (l = (long)(sArray[1][n5] - n3)) * l) >= l3) continue;
            l3 = l2;
            n8 = n6;
        }
        int n9 = Math.max(1, n8 - 3);
        n5 = Math.min(n7, n8 + 3);
        for (n6 = n9; n6 <= n5; ++n6) {
            int n10;
            long l5;
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, n, n6, phys);
            if (sArray == null || sArray.length < 2 || sArray[0] == null || sArray[0].length <= 0 || (l5 = (l = (long)(sArray[0][n10 = sArray[0].length - 1] - n2)) * l + (l2 = (long)(sArray[1][n10] - n3)) * l2) >= l3) continue;
            l3 = l5;
            n8 = n6;
        }
        return n8;
    }

    private static void finishTurn() {
        turnPending = false;
        shotInFlight = false;
        CPlayer cPlayer = OfflinePvpBotAI.botAt(actingIndex);
        if (cPlayer != null && cPlayer.hp > 0) {
            cPlayer.setState((byte)0);
        }
        OfflineCombat.ensureLocalPlayerTurn();
    }

    private static void maybeUseItem(CPlayer cPlayer, int n, byte by) {
        int n2;
        boolean bl;
        if (cPlayer == null || cPlayer.item == null || cPlayer.maxhp <= 0 || cPlayer.hp >= cPlayer.maxhp) {
            return;
        }
        boolean bl2 = by == 3;
        int n3 = bl2 ? 50 : 30;
        boolean bl3 = bl = cPlayer.hp < cPlayer.maxhp * n3 / 100;
        if ((by == 2 || bl2) && bl && n >= 0 && n < fullHealUsesLeft.length && fullHealUsesLeft[n] > 0) {
            byte by2 = 25;
            cPlayer.updateHP(cPlayer.maxhp, by2);
            int n4 = n;
            fullHealUsesLeft[n4] = fullHealUsesLeft[n4] - 1;
            return;
        }
        n2 = by == 0 ? 5 : (bl2 ? 55 : (by == 2 ? 30 : 15));
        if (bl) {
            n2 += 25;
        }
        if (CRes.random(0, 100) >= n2) {
            return;
        }
        for (int i = 0; i < cPlayer.item.length; ++i) {
            int n6 = cPlayer.item[i];
            if (n6 != 0 && n6 != 32 && n6 != 33) continue;
            int n7 = n6 == 0 ? 350 : (n6 == 32 ? cPlayer.maxhp / 2 : cPlayer.maxhp);
            int n8 = Math.min(cPlayer.maxhp, cPlayer.hp + n7);
            byte by3 = (byte)(n8 * 25 / cPlayer.maxhp);
            cPlayer.updateHP(n8, by3);
            return;
        }
    }

    private static void maybeUseAttackItem(CPlayer cPlayer, byte by) {
        int n;
        boolean bl;
        boolean bl2 = bl = by == 3;
        if (cPlayer == null || cPlayer.item == null || by != 2 && !bl || cPlayer.itemUsed != -1) {
            return;
        }
        int n2 = n = bl ? 70 : 35;
        if (CRes.random(0, 100) >= n) {
            return;
        }
        int n3 = CRes.random(0, ATTACK_ITEM_IDS.length);
        for (int i = 0; i < ATTACK_ITEM_IDS.length; ++i) {
            int n4 = ATTACK_ITEM_IDS[(n3 + i) % ATTACK_ITEM_IDS.length];
            if (!OfflinePvpBotAI.hasItemInLoadout(cPlayer, n4)) continue;
            cPlayer.isUsedItem = true;
            cPlayer.UseItem(n4, true, 0);
            return;
        }
    }

    private static void maybeUseSupportItem(CPlayer cPlayer, int n, byte by) {
        if (cPlayer == null || cPlayer.item == null || by != 3 || cPlayer.itemUsed != -1 || n < 0 || n >= botInvisibleTurns.length) {
            return;
        }
        if (CRes.random(0, 100) >= 40) {
            return;
        }
        boolean bl = CRes.random(0, 2) == 0;
        int n2 = bl ? 34 : 35;
        for (int i = 0; i < 2; ++i) {
            boolean bl2 = n2 == 34 ? (botInvisibleTurns[n] > 0) : (botVampireTurns[n] > 0);
            if (!bl2 && OfflinePvpBotAI.hasItemInLoadout(cPlayer, n2)) {
                cPlayer.isUsedItem = true;
                cPlayer.UseItem(n2, true, 0);
                if (n2 == 34) {
                    OfflinePvpBotAI.botInvisibleTurns[n] = 3;
                } else {
                    OfflinePvpBotAI.botVampireTurns[n] = 3;
                    cPlayer.isVampire = true;
                }
                return;
            }
            n2 = n2 == 34 ? 35 : 34;
        }
    }

    private static boolean hasItemInLoadout(CPlayer cPlayer, int n) {
        for (int i = 0; i < cPlayer.item.length; ++i) {
            if (cPlayer.item[i] != n) continue;
            return true;
        }
        return false;
    }

    private static void maybeUsePow(CPlayer cPlayer, byte by) {
        int n;
        if (cPlayer == null || !cPlayer.isAngry || cPlayer.itemUsed != -1) {
            return;
        }
        n = by == 0 ? 40 : (by == 2 || by == 3 ? 100 : 70);
        if (CRes.random(0, 100) >= n) {
            return;
        }
        cPlayer.isUsedItem = true;
        cPlayer.angryX = 0;
        cPlayer.currAngry = 0;
        cPlayer.is2TurnItem = true;
        cPlayer.UseItem(100, true, 0);
    }

    static {
        actingIndex = -1;
        lastBotIndex = -1;
        fullHealUsesLeft = new int[8];
        botInvisibleTurns = new int[8];
        botVampireTurns = new int[8];
        SPLIT_FRACTION_PCT = new int[]{50, 65, 78, 88, 95, 99};
        ANGLE_CANDIDATES = new int[]{10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85};
        ATTACK_ITEM_IDS = new int[]{6, 7, 8};
    }
}

