/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import player.CPlayer;

public final class OfflineGunAngles {
    static final short[] SERVER_GLASS_ANGLE = new short[]{-45, -45, -45, 30, 20, 30, -45, 1, -45, 20};
    private static final int LOCK_SIZE = 30;

    private OfflineGunAngles() {
    }

    public static void applyServerAngleLocks() {
        int n;
        short[] sArray = new short[30];
        for (n = 0; n < SERVER_GLASS_ANGLE.length; ++n) {
            sArray[n] = SERVER_GLASS_ANGLE[n];
        }
        while (n < 30) {
            sArray[n] = -45;
            ++n;
        }
        CPlayer.angleLock = sArray;
        CPlayer.angleLockMain = sArray;
    }

    public static short angleLockForGun(byte by) {
        if (CPlayer.angleLock != null && by >= 0 && by < CPlayer.angleLock.length) {
            return CPlayer.angleLock[by];
        }
        if (by >= 0 && by < SERVER_GLASS_ANGLE.length) {
            return SERVER_GLASS_ANGLE[by];
        }
        return -45;
    }

    public static void resetAimAngle(CPlayer cPlayer) {
        if (cPlayer == null || cPlayer.gun >= 11) {
            return;
        }
        if (cPlayer.savedAngle != Integer.MIN_VALUE) {
            cPlayer.angle = cPlayer.savedAngle;
            cPlayer.angleUpdate();
            cPlayer.checkAngleForSprite();
            return;
        }
        short s = OfflineGunAngles.angleLockForGun(cPlayer.gun);
        cPlayer.angle = cPlayer.look == 2 ? (int)s : (int)((short)(180 - s));
        cPlayer.saveCurrentAngle();
        cPlayer.checkAngleForSprite();
    }
}

