/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import com.teamobi.mobiarmy2.OfflinePvpBot;
import com.teamobi.mobiarmy2.OfflinePvpBotAI;
import coreLG.CCanvas;
import effect.Explosion;
import java.util.Vector;
import model.PlayerInfo;
import player.CPlayer;
import player.PM;
import screen.GameScr;
import screen.PrepareScr;

public final class OfflineBotUfo {
    private static final int SHOTS_PER_UFO = 3;
    private static final int[] TRIGGER_TURNS = new int[]{20, 40};
    private static final int[] TRIGGER_COUNTS = new int[]{2, 3};
    private static final boolean[] triggeredAtTurn = new boolean[TRIGGER_TURNS.length];
    private static final int HITS_TO_KILL = 5;
    private static final int HIT_RADIUS = 40;
    private static final int SPREAD_X = 45;
    private static final Vector active = new Vector();

    private OfflineBotUfo() {
    }

    public static void reset() {
        for (int i = 0; i < triggeredAtTurn.length; ++i) {
            OfflineBotUfo.triggeredAtTurn[i] = false;
        }
        active.removeAllElements();
    }

    public static void onTurnAdvanced(int n) {
        if (OfflinePvpBot.activeDifficulty == 3 && PrepareScr.isPvpBotRoom) {
            for (int i = 0; i < TRIGGER_TURNS.length; ++i) {
                if (triggeredAtTurn[i] || n != TRIGGER_TURNS[i]) continue;
                OfflineBotUfo.triggeredAtTurn[i] = true;
                OfflineBotUfo.trigger(TRIGGER_COUNTS[i]);
            }
        }
    }

    public static void onLocalPlayerTurnStart() {
        if (OfflinePvpBot.activeDifficulty == 3 && PrepareScr.isPvpBotRoom) {
            OfflineBotUfo.tickAll();
        }
    }

    private static void trigger(int n) {
        int n2 = OfflineBotUfo.strongestAliveBotAttack();
        if (n2 <= 0) {
            return;
        }
        CPlayer cPlayer = OfflinePvpBotAI.target();
        if (cPlayer == null) {
            return;
        }
        for (int i = 0; i < n; ++i) {
            int n3 = i * 45 - (n - 1) * 45 / 2;
            Instance instance = new Instance();
            instance.offsetX = n3;
            instance.offsetY = 0;
            instance.x = cPlayer.x + n3;
            instance.y = cPlayer.y - 150;
            instance.shotsLeft = 3;
            instance.attack = n2;
            instance.hitsLeft = 5;
            active.addElement(instance);
        }
    }

    public static boolean checkHit(int n, int n2) {
        if (active.isEmpty()) {
            return false;
        }
        boolean bl = false;
        int n3 = 0;
        while (n3 < active.size()) {
            Instance instance = (Instance)active.elementAt(n3);
            int n4 = n - instance.x;
            int n5 = n2 - instance.y;
            if (n4 * n4 + n5 * n5 <= 1600) {
                bl = true;
                --instance.hitsLeft;
                if (instance.hitsLeft <= 0) {
                    new Explosion(instance.x, instance.y, 0);
                    active.removeElementAt(n3);
                    continue;
                }
                new Explosion(instance.x, instance.y, 12);
            }
            ++n3;
        }
        return bl;
    }

    private static int strongestAliveBotAttack() {
        if (CCanvas.prepareScr == null || PM.p == null) {
            return 0;
        }
        int n = OfflinePvpBot.activeSquadCount;
        int n2 = n + OfflinePvpBot.activeBotCount;
        int n3 = 0;
        for (int i = n; i < n2 && i < PM.p.length && i < CCanvas.prepareScr.playerInfos.size(); ++i) {
            PlayerInfo playerInfo;
            int n4;
            CPlayer cPlayer = PM.p[i];
            if (cPlayer == null || cPlayer.hp <= 0 || cPlayer.getState() == 5 || (n4 = OfflineEquipmentStats.attack(playerInfo = (PlayerInfo)CCanvas.prepareScr.playerInfos.elementAt(i))) <= n3) continue;
            n3 = n4;
        }
        return n3;
    }

    private static void tickAll() {
        if (active.isEmpty()) {
            return;
        }
        int n = 0;
        while (n < active.size()) {
            Instance instance = (Instance)active.elementAt(n);
            CPlayer cPlayer = OfflinePvpBotAI.target();
            if (cPlayer == null) {
                active.removeElementAt(n);
                continue;
            }
            instance.x = cPlayer.x + instance.offsetX;
            instance.y = cPlayer.y - 150 + instance.offsetY;
            --instance.shotsLeft;
            if (GameScr.sm != null) {
                GameScr.sm.addLazer(instance.x, instance.y, cPlayer.x, cPlayer.y - 15, 1);
            }
            new Explosion(cPlayer.x, cPlayer.y - 35, 12);
            OfflineCombat.applyDirectExplosionDamage(cPlayer.x, cPlayer.y - 20, 50, instance.attack, false);
            if (instance.shotsLeft <= 0) {
                active.removeElementAt(n);
                continue;
            }
            ++n;
        }
    }

    public static void paintAll(mGraphics mGraphics2) {
        for (int i = 0; i < active.size(); ++i) {
            Instance instance = (Instance)active.elementAt(i);
            CPlayer.paintUFO(mGraphics2, instance.x, instance.y);
        }
    }

    private static final class Instance {
        int x;
        int y;
        int offsetX;
        int offsetY;
        int shotsLeft;
        int attack;
        int hitsLeft;

        private Instance() {
        }
    }
}

