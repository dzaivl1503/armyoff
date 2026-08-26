/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import Equipment.Equip;
import Equipment.PlayerEquip;
import coreLG.TerrainMidlet;
import map.CMap;
import player.CPlayer;
import screen.GameScr;

public final class OfflineBulletAssets {
    private OfflineBulletAssets() {
    }

    public static void ensureHoleMasks() {
        CMap.ensureHoleMasksLoaded();
    }

    public static void prepareCombat(CPlayer cPlayer) {
        int n;
        OfflineBulletAssets.ensureHoleMasks();
        if (cPlayer == null) {
            return;
        }
        cPlayer.idBullet = n = OfflineBulletAssets.resolveBulletFrame(cPlayer);
        if (GameScr.bm != null) {
            GameScr.bm.prepareOfflineBulletFrames(n);
        }
    }

    public static int resolveBulletFrame(CPlayer cPlayer) {
        if (cPlayer == null) {
            return 0;
        }
        try {
            short maskId = -1;
            if (cPlayer.equip != null) {
                maskId = cPlayer.equip.getActiveMaskId();
            } else if (TerrainMidlet.isVip != null && cPlayer.gun >= 0 && cPlayer.gun < TerrainMidlet.isVip.length && TerrainMidlet.isVip[cPlayer.gun]) {
                if (TerrainMidlet.myInfo != null && TerrainMidlet.myInfo.equipVipID != null) {
                    maskId = TerrainMidlet.myInfo.equipVipID[cPlayer.gun][1];
                }
            }
            if (maskId > 0) {
                short[] setData = PlayerEquip.getMaskSetData(cPlayer.gun, maskId);
                if (setData != null && setData[0] > 0) {
                    Equip maskGun = PlayerEquip.getEquip(cPlayer.gun, (byte)0, setData[0]);
                    if (maskGun != null) {
                        return maskGun.bullet & 0xFF;
                    }
                }
            }
            TerrainMidlet.myInfo.ensureCombatEquip();
            if (TerrainMidlet.myInfo.myEquip != null && TerrainMidlet.myInfo.myEquip.equips[0] != null) {
                return TerrainMidlet.myInfo.myEquip.equips[0].bullet & 0xFF;
            }
        }
        catch (Exception exception) {
        }
        return cPlayer.idBullet;
    }

    public static int bulletSheetIndex(byte by) {
        switch (by) {
            case 0:
            case 32: {
                return 0;
            }
            case 1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 9: {
                return 3;
            }
            case 10: {
                return 4;
            }
            case 11: {
                return 5;
            }
            case 19: {
                return 6;
            }
            case 21: {
                return 7;
            }
            case 17: {
                return 8;
            }
            case 49: {
                return 9;
            }
        }
        return 0;
    }
}

