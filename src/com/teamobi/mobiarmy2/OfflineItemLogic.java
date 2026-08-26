/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.OfflineBulletSim;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import coreLG.TerrainMidlet;
import effect.Explosion;
import java.util.Vector;
import model.PlayerInfo;
import model.TimeBomb;
import player.Boss;
import player.CPlayer;
import player.PM;
import screen.GameScr;

public final class OfflineItemLogic {
    private static byte localShotType = (byte)-1;
    private static boolean specialHandled;
    private static int vampireHeal;
    private static int[] invisibleTurns;
    private static int[] invisible2Turns;
    private static int[] stopWindTurns;
    private static int[] vampireTurns;
    private static int ufoShots;
    private static int ufoX;
    private static int ufoY;
    private static int[] blindTurns;
    private static int[] freezeTurns;
    private static boolean[] poisoned;
    private static final Vector timeBombs;
    private static int nextBombId;

    private OfflineItemLogic() {
    }

    public static void reset() {
        localShotType = (byte)-1;
        specialHandled = false;
        vampireHeal = 0;
        ufoShots = 0;
        ufoX = 0;
        ufoY = 0;
        int n = PM.p == null ? 0 : PM.p.length;
        invisibleTurns = new int[n];
        invisible2Turns = new int[n];
        stopWindTurns = new int[n];
        vampireTurns = new int[n];
        blindTurns = new int[n];
        freezeTurns = new int[n];
        poisoned = new boolean[n];
        timeBombs.removeAllElements();
        nextBombId = 0;
        if (GameScr.timeBombs != null) {
            GameScr.timeBombs.removeAllElements();
        }
    }

    public static void beginLocalShot(byte by) {
        localShotType = by;
        specialHandled = false;
        vampireHeal = 0;
    }

    public static byte localShotType() {
        return localShotType;
    }

    public static int shotDamage(PlayerInfo playerInfo, boolean bl, int n) {
        int n2;
        int n3 = OfflineEquipmentStats.attack(playerInfo);
        switch (localShotType) {
            case 1: {
                n2 = n3 / (bl ? 3 : 2);
                break;
            }
            case 2: {
                n2 = n3 / 3;
                break;
            }
            case 9: {
                n2 = n3 / 4 * (bl ? 2 : 1);
                break;
            }
            case 10: {
                n2 = OfflineItemLogic.splitVolleyDamage(n3 * (bl ? 2 : 1), 3, n);
                break;
            }
            case 11: {
                n2 = OfflineItemLogic.splitVolleyDamage(n3 * (bl ? 2 : 1), 5, n);
                break;
            }
            case 17: {
                n2 = n == 0 ? 10 : n3 / 4 * (bl ? 2 : 1);
                break;
            }
            case 19: {
                n2 = n3 / 2 * (bl ? 2 : 1);
                break;
            }
            case 33: {
                n2 = n3 / 5 * (bl ? 2 : 1);
                break;
            }
            case 4: {
                n2 = 100 + n3 * 2;
                break;
            }
            case 5:
            case 8:
            case 13:
            case 30:
            case 53:
            case 56:
            case 57: {
                n2 = 0;
                break;
            }
            case 6:
            case 28: {
                n2 = n3 / 3;
                break;
            }
            case 7: {
                n2 = n3 * 2;
                break;
            }
            case 14:
            case 22:
            case 52: {
                n2 = 100 + n3 + n3 / 2;
                break;
            }
            case 25: {
                n2 = n3 + n3 / 2;
                break;
            }
            case 26: {
                n2 = n == 0 ? 0 : n3 * (bl ? 2 : 1);
                break;
            }
            case 50: {
                n2 = 1500;
                break;
            }
            case 58: {
                int n4;
                int n5 = n4 = OfflineBulletSim.lastBounceIndices != null ? OfflineBulletSim.lastBounceIndices.length : 2;
                if (n4 > 2) {
                    n4 = 2;
                }
                int n6 = n3 * (bl ? 2 : 1);
                n2 = n4 >= 2 ? n6 * 3 / 2 : n6 * (n4 + 1) / 3;
                break;
            }
            default: {
                n2 = n3 * (bl ? 2 : 1);
            }
        }
        return n2;
    }

    private static int splitVolleyDamage(int n, int n2, int n3) {
        if (n2 <= 1) {
            return n;
        }
        int n4 = n / n2;
        int n5 = n % n2;
        int n6 = n3 % n2;
        if (n6 < 0) {
            n6 += n2;
        }
        return n4 + (n6 < n5 ? 1 : 0);
    }

    public static int shotRadius(boolean bl) {
        return OfflineItemLogic.shotRadius(bl, 0);
    }

    public static int shotRadius(boolean bl, int n) {
        int n2;
        switch (localShotType) {
            case 0: {
                n2 = 18;
                break;
            }
            case 1: {
                n2 = 11;
                break;
            }
            case 2:
            case 9:
            case 49: {
                n2 = 16;
                break;
            }
            case 10: {
                n2 = 17;
                break;
            }
            case 11:
            case 17:
            case 21:
            case 33: {
                n2 = 11;
                break;
            }
            case 19: {
                n2 = n == 0 ? 11 : 16;
                break;
            }
            case 4: {
                n2 = 110;
                break;
            }
            case 6: {
                n2 = 90;
                break;
            }
            case 7: {
                n2 = 80;
                break;
            }
            case 14: {
                n2 = 28;
                break;
            }
            case 16: {
                n2 = 20;
                break;
            }
            case 22: {
                n2 = 10;
                break;
            }
            case 23: {
                n2 = 18;
                break;
            }
            case 25: {
                n2 = 100;
                break;
            }
            case 26: {
                n2 = n == 0 ? 0 : 100;
                break;
            }
            case 28: {
                n2 = 11;
                break;
            }
            case 50: {
                n2 = 70;
                break;
            }
            case 51: {
                n2 = 60;
                break;
            }
            case 55: {
                n2 = 30;
                break;
            }
            case 52: {
                n2 = 100;
                break;
            }
            case 54: {
                n2 = 80;
                break;
            }
            default: {
                n2 = 55;
            }
        }
        return n2;
    }

    public static void onExplosionPoint(int n, int n2, byte by) {
        if (by != GameScr.myIndex || specialHandled) {
            return;
        }
        if (localShotType == 5) {
            specialHandled = true;
        } else if (localShotType == 57) {
            OfflineItemLogic.addTimeBomb(n, n2);
            specialHandled = true;
        }
    }

    public static void applyStatus(CPlayer cPlayer, int n) {
        if (n < 0 || n >= blindTurns.length) {
            return;
        }
        if (localShotType == 51) {
            OfflineItemLogic.blindTurns[n] = 5;
            cPlayer.cantSee = true;
        } else if (localShotType == 54) {
            OfflineItemLogic.freezeTurns[n] = 5;
            cPlayer.isFreeze = true;
        } else if (localShotType == 55) {
            OfflineItemLogic.poisoned[n] = true;
            cPlayer.isPoison = true;
            cPlayer.poisonEff = true;
        }
    }

    public static boolean blocksDamage(CPlayer cPlayer, int n) {
        if (cPlayer == null || n < 0 || n >= invisible2Turns.length) {
            return false;
        }
        return invisible2Turns[n] > 0;
    }

    public static boolean isFrozen(int n) {
        return n >= 0 && n < freezeTurns.length && freezeTurns[n] > 0;
    }

    public static void recordVampireDamage(int n) {
        byte by = GameScr.myIndex;
        if (by >= 0 && by < vampireTurns.length && vampireTurns[by] > 0 && n > 0) {
            vampireHeal += n / 2;
        }
    }

    public static void finishLocalShot() {
        OfflineItemLogic.finishDirectDamage();
        localShotType = (byte)-1;
    }

    public static void finishDirectDamage() {
        if (vampireHeal > 0) {
            OfflineItemLogic.heal(PM.getMyPlayer(), vampireHeal);
        }
        vampireHeal = 0;
    }

    public static void onUseItem(int n) {
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer == null) {
            return;
        }
        byte by = GameScr.myIndex;
        boolean bl = by >= 0 && by < invisibleTurns.length;
        switch (n) {
            case 4: {
                if (bl) {
                    OfflineItemLogic.invisibleTurns[by] = 5;
                }
                cPlayer.isInvisible = true;
                break;
            }
            case 5: {
                if (bl) {
                    OfflineItemLogic.stopWindTurns[by] = 5;
                }
                cPlayer.isStopWind = true;
                GameScr.changeWind(0, 0);
                break;
            }
            case 27: {
                ufoShots = 3;
                ufoX = cPlayer.x;
                ufoY = cPlayer.y - 70;
                break;
            }
            case 34: {
                if (bl) {
                    OfflineItemLogic.invisible2Turns[by] = 3;
                }
                cPlayer.isInvisible = true;
                break;
            }
            case 35: {
                if (bl) {
                    OfflineItemLogic.vampireTurns[by] = 3;
                }
                cPlayer.isVampire = true;
                break;
            }
        }
    }

    public static void onBossTurn(CPlayer cPlayer, int n) {
        if (cPlayer == null || n < 0 || n >= freezeTurns.length) {
            return;
        }
        if (poisoned[n] && cPlayer.hp > 0) {
            new Explosion(cPlayer.x, cPlayer.y - 20, 15);
            OfflineItemLogic.damageOne(cPlayer, 150);
            OfflineCombat.checkBattleEndNow();
        }
        if (blindTurns[n] > 0) {
            int n2 = n;
            blindTurns[n2] = blindTurns[n2] - 1;
            if (blindTurns[n] == 0) {
                cPlayer.cantSee = false;
            }
        }
        if (freezeTurns[n] > 0) {
            int n3 = n;
            freezeTurns[n3] = freezeTurns[n3] - 1;
            if (freezeTurns[n] == 0) {
                cPlayer.isFreeze = false;
            }
        }
        OfflineItemLogic.tickTimeBombs();
    }

    public static void onPlayerTurn() {
        CPlayer cPlayer = PM.getMyPlayer();
        if (cPlayer == null) {
            return;
        }
        byte by = GameScr.myIndex;
        if (by < 0 || by >= invisibleTurns.length) {
            OfflineItemLogic.tickUfoAssist();
            OfflineItemLogic.tickTimeBombs();
            return;
        }
        if (invisibleTurns[by] > 0) {
            byte by2 = by;
            invisibleTurns[by2] = invisibleTurns[by2] - 1;
            if (invisibleTurns[by2] == 0 && invisible2Turns[by] == 0) {
                cPlayer.isInvisible = false;
            }
        }
        if (invisible2Turns[by] > 0) {
            byte by3 = by;
            invisible2Turns[by3] = invisible2Turns[by3] - 1;
            if (invisible2Turns[by3] == 0 && invisibleTurns[by] == 0) {
                cPlayer.isInvisible = false;
            }
        }
        if (stopWindTurns[by] > 0) {
            byte by4 = by;
            stopWindTurns[by4] = stopWindTurns[by4] - 1;
            GameScr.changeWind(0, 0);
            if (stopWindTurns[by] == 0) {
                cPlayer.isStopWind = false;
            }
        }
        if (vampireTurns[by] > 0) {
            byte by5 = by;
            vampireTurns[by5] = vampireTurns[by5] - 1;
            if (vampireTurns[by5] == 0) {
                cPlayer.isVampire = false;
            }
        }
        OfflineItemLogic.tickUfoAssist();
        OfflineItemLogic.tickTimeBombs();
    }

    public static void applyWindLock() {
        for (int i = 0; i < stopWindTurns.length; ++i) {
            if (stopWindTurns[i] <= 0) continue;
            GameScr.changeWind(0, 0);
            return;
        }
    }

    private static void tickUfoAssist() {
        if (ufoShots <= 0) {
            return;
        }
        CPlayer cPlayer = OfflineItemLogic.mainBoss();
        if (cPlayer == null || cPlayer.hp <= 0) {
            ufoShots = 0;
            return;
        }
        --ufoShots;
        ufoX = cPlayer.x;
        ufoY = cPlayer.y - 150;
        if (GameScr.sm != null) {
            GameScr.sm.addLazer(ufoX, ufoY, cPlayer.x, cPlayer.y - 15, 1);
        }
        new Explosion(cPlayer.x, cPlayer.y - 35, 12);
        int n = OfflineEquipmentStats.attack(TerrainMidlet.myInfo);
        OfflineCombat.applyDirectExplosionDamage(cPlayer.x, cPlayer.y - 20, 50, n, true);
    }

    public static void paintUfoAssist(mGraphics mGraphics2) {
        if (ufoShots > 0 && mGraphics2 != null) {
            CPlayer.paintUFO(mGraphics2, ufoX, ufoY);
        }
    }

    private static void addTimeBomb(int n, int n2) {
        TimeBomb timeBomb = new TimeBomb(nextBombId++, n, n2);
        int n3 = OfflineEquipmentStats.attack(TerrainMidlet.myInfo);
        PendingBomb pendingBomb = new PendingBomb(timeBomb, 5, n3 * 2, 120);
        timeBombs.addElement(pendingBomb);
        if (GameScr.timeBombs != null) {
            GameScr.timeBombs.addElement(timeBomb);
        }
    }

    private static void tickTimeBombs() {
        for (int i = timeBombs.size() - 1; i >= 0; --i) {
            PendingBomb pendingBomb = (PendingBomb)timeBombs.elementAt(i);
            --pendingBomb.turns;
            if (pendingBomb.turns > 0) continue;
            pendingBomb.visual.isExplore = true;
            OfflineCombat.applyDirectExplosionDamage(pendingBomb.visual.x, pendingBomb.visual.y, pendingBomb.radius, pendingBomb.damage, true);
            timeBombs.removeElementAt(i);
        }
    }

    private static CPlayer mainBoss() {
        if (PM.p == null) {
            return null;
        }
        for (int i = 0; i < PM.p.length; ++i) {
            if (!(PM.p[i] instanceof Boss) || PM.p[i].gun == 11) continue;
            return PM.p[i];
        }
        return null;
    }

    private static void heal(CPlayer cPlayer, int n) {
        if (cPlayer == null || n <= 0 || cPlayer.hp <= 0) {
            return;
        }
        int n2 = Math.min(cPlayer.maxhp, cPlayer.hp + n);
        byte by = cPlayer.maxhp > 0 ? (byte)(n2 * 25 / cPlayer.maxhp) : (byte)0;
        cPlayer.updateHP(n2, by);
    }

    private static void damageOne(CPlayer cPlayer, int n) {
        int n2 = Math.max(0, cPlayer.hp - n);
        byte by = cPlayer.maxhp > 0 ? (byte)(n2 * 25 / cPlayer.maxhp) : (byte)0;
        cPlayer.updateHP(n2, by);
    }

    static {
        invisibleTurns = new int[0];
        invisible2Turns = new int[0];
        stopWindTurns = new int[0];
        vampireTurns = new int[0];
        blindTurns = new int[0];
        freezeTurns = new int[0];
        poisoned = new boolean[0];
        timeBombs = new Vector();
    }

    private static final class PendingBomb {
        final TimeBomb visual;
        int turns;
        final int damage;
        final int radius;

        PendingBomb(TimeBomb timeBomb, int n, int n2, int n3) {
            this.visual = timeBomb;
            this.turns = n;
            this.damage = n2;
            this.radius = n3;
        }
    }
}

