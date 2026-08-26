/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import player.CPlayer;
import screen.GameScr;

final class OfflineAiMovement {
    private static final int FALL_LOOKAHEAD = 80;

    private OfflineAiMovement() {
    }

    static boolean wouldStepOffMap(CPlayer cPlayer, int n) {
        if (GameScr.mm == null) {
            return false;
        }
        int n2 = cPlayer.isRunSpeed ? 2 : (int)cPlayer.runSpeed;
        int n3 = n == 0 ? cPlayer.x - n2 : cPlayer.x + n2;
        for (int i = -4; i <= 80; ++i) {
            if (!GameScr.mm.isLand(n3, cPlayer.y + i)) continue;
            return false;
        }
        return true;
    }
}

