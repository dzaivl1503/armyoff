/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineBulletSim;
import com.teamobi.mobiarmy2.OfflineGunPhysics;
import item.Bullet;
import java.util.Vector;
import map.MM;
import model.CRes;
import player.CPlayer;

public final class OfflineSpecialBullets {
    private static final int MAX_FRAMES = 900;

    private OfflineSpecialBullets() {
    }

    static boolean usesSpecialBuilder(byte by) {
        return by == 17 || by == 19 || by == 21 || by == 49;
    }

    static short[][][] build(CPlayer cPlayer, byte by, int n, byte by2) {
        switch (by) {
            case 17: {
                return OfflineSpecialBullets.buildApache(cPlayer, n, by2);
            }
            case 19: {
                return OfflineSpecialBullets.buildChicky(cPlayer, n, by2);
            }
            case 21: {
                return new short[][][]{OfflineSpecialBullets.buildTarzan(cPlayer, n)};
            }
            case 49: {
                return new short[][][]{OfflineSpecialBullets.buildMirror(cPlayer, n)};
            }
        }
        return null;
    }

    static byte resolveForce2(byte by, short[][][] sArray) {
        if (by != 17 && by != 19) {
            return 2;
        }
        if (sArray == null || sArray.length == 0 || sArray[0] == null || sArray[0][1] == null) {
            return 2;
        }
        return OfflineSpecialBullets.apexPathIndex(sArray[0]);
    }

    static byte apexPathIndex(short[][] sArray) {
        short[] sArray2 = sArray[1];
        if (sArray2.length < 2) {
            return 2;
        }
        int n = 0;
        for (int i = 1; i < sArray2.length; ++i) {
            if (sArray2[i] >= sArray2[n]) continue;
            n = i;
        }
        int n2 = Math.max(2, n);
        return (byte)(n2 > 250 ? -6 : (byte)n2);
    }

    private static short[][] buildTarzan(CPlayer cPlayer, int n) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)21);
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzle(cPlayer, cPlayer.angle, n, nArray);
        int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
        final boolean bl = nArray2[2] <= 0;
        final byte[] byArray = new byte[]{-1};
        final boolean[] blArray = new boolean[]{false};
        return OfflineBulletSim.simulatePath(nArray2, phys.ax100, phys.ay100, phys.g100, new OfflineBulletSim.FrameHook(){

            public void afterIntegrate(int[] nArray, Vector vector, Vector vector2) {
                byte by = byArray[0];
                int n = nArray[2];
                OfflineGunPhysics.tarzanTurn(nArray, bl, byArray, blArray);
                if (byArray[0] != by || nArray[2] != n) {
                    OfflineBulletSim.addPathPointForce(vector, vector2, nArray[0], nArray[1]);
                }
            }
        }, cPlayer);
    }

    private static short[][] buildMirror(CPlayer cPlayer, int n) {
        int n2;
        int n3;
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)49);
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzleForcePlus(cPlayer, cPlayer.angle, n, 5, nArray);
        int n4 = nArray[0];
        int n5 = nArray[1];
        int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        OfflineBulletSim.addPathPoint(vector, vector2, n4, n5);
        int n6 = 0;
        int n7 = 0;
        int n8 = n4;
        int n9 = n5;
        boolean bl = false;
        for (int i = 0; i < 900; ++i) {
            n3 = nArray2[0];
            n2 = nArray2[1];
            nArray2[0] = nArray2[0] + nArray2[2];
            nArray2[1] = nArray2[1] + nArray2[3];
            int n10 = nArray2[0];
            int n11 = nArray2[1];
            n8 = n10;
            n9 = n11;
            if (OfflineSpecialBullets.outOfPlay(n10, n11)) {
                OfflineBulletSim.addPathPointForce(vector, vector2, n10, n11);
                break;
            }
            int[] nArray3 = OfflineBulletSim.collisionPoint(n3, n2, n10, n11, cPlayer);
            if (nArray3 != null) {
                n8 = nArray3[0];
                n9 = nArray3[1];
                OfflineBulletSim.addPathPointForce(vector, vector2, n8, n9);
                break;
            }
            OfflineBulletSim.addPathPoint(vector, vector2, n10, n11);
            OfflineGunPhysics.integrate(nArray2, phys.ax100, phys.ay100, phys.g100);
            if (nArray2[3] < 0) continue;
            bl = true;
            int n12 = CRes.angle(n8 - n4, n5 - n9);
            int[] nArray4 = OfflineSpecialBullets.scaleLaserVelocity(n * CRes.cos(n12) >> 10, n * CRes.sin(n12) >> 10);
            n6 = nArray4[0];
            n7 = nArray4[1];
            break;
        }
        n3 = n8;
        n2 = n9;
        if (bl && (n6 != 0 || n7 != 0)) {
            int[] nArray5 = OfflineSpecialBullets.simulateLaserImpact(cPlayer, n8, n9, n6, n7);
            n3 = nArray5[0];
            n2 = nArray5[1];
            OfflineBulletSim.addPathPointForce(vector, vector2, n8, n9);
            OfflineBulletSim.addPathPointForce(vector, vector2, n3, n2);
        }
        short[][] object = OfflineBulletSim.compressPath(vector, vector2);
        if (bl && object[0].length < 3) {
            object = new short[][]{{(short)n4, (short)n8, (short)n3}, {(short)n5, (short)n9, (short)n2}};
        }
        OfflineSpecialBullets.applyMirrorLaser(object, n, bl ? n6 : 0, bl ? n7 : 0);
        return object;
    }

    private static int[] scaleLaserVelocity(int n, int n2) {
        if (n != 0) {
            while (Math.abs(n) < 15) {
                n += n;
                n2 += n2;
            }
        }
        return new int[]{n, n2};
    }

    private static int[] simulateLaserImpact(CPlayer cPlayer, int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7 = n;
        int n8 = n2;
        if (n3 == 0 && n4 == 0) {
            n8 = n2 + 60;
        }
        for (int i = 0; i < 900; ++i) {
            n6 = n7;
            n5 = n8;
            if (OfflineSpecialBullets.outOfPlay(n7 += n3, n8 += n4)) break;
            int[] nArray = OfflineBulletSim.collisionPoint(n6, n5, n7, n8, cPlayer);
            if (nArray == null) continue;
            n7 = nArray[0];
            n8 = nArray[1];
            break;
        }
        if ((n6 = (n7 - n) * (n7 - n) + (n8 - n2) * (n8 - n2)) < 2500 && (n3 != 0 || n4 != 0)) {
            n7 = n;
            n8 = n2;
            for (n5 = 0; n5 < 40 && n6 < 2500; ++n5) {
                int n9 = n7;
                int n10 = n8;
                if (OfflineSpecialBullets.outOfPlay(n7 += n3, n8 += n4)) break;
                int[] nArray = OfflineBulletSim.collisionPoint(n9, n10, n7, n8, cPlayer);
                if (nArray != null) {
                    n7 = nArray[0];
                    n8 = nArray[1];
                    break;
                }
                n6 = (n7 - n) * (n7 - n) + (n8 - n2) * (n8 - n2);
            }
        }
        return new int[]{n7, n8};
    }

    private static void applyMirrorLaser(short[][] sArray, int n, int n2, int n3) {
        if (sArray == null || sArray[0].length < 2) {
            Bullet.dXLaser = 0;
            Bullet.dYLaser = 0;
            return;
        }
        if (n2 == 0 && n3 == 0) {
            int n4 = sArray[0].length - 2;
            if (n4 < 0) {
                n4 = 0;
            }
            short s = sArray[0][0];
            short s2 = sArray[1][0];
            short s3 = sArray[0][n4];
            short s4 = sArray[1][n4];
            int n5 = CRes.angle(s3 - s, s2 - s4);
            n2 = n * CRes.cos(n5) >> 10;
            n3 = n * CRes.sin(n5) >> 10;
            if (n2 != 0) {
                while (Math.abs(n2) < 15) {
                    n2 += n2;
                    n3 += n3;
                }
            }
        }
        OfflineSpecialBullets.setMirrorLaserBytes(n2, n3);
    }

    private static void setMirrorLaserBytes(int n, int n2) {
        int n3 = OfflineSpecialBullets.clampLaserStep(n);
        int n4 = OfflineSpecialBullets.clampLaserStep(-n2);
        if (n3 != 0) {
            while (Math.abs(n3) < 15) {
                n3 += n3;
                n4 += n4;
            }
        }
        Bullet.dXLaser = n3;
        Bullet.dYLaser = n4;
    }

    private static int clampLaserStep(int n) {
        if (n > 127) {
            return 127;
        }
        if (n < -128) {
            return -128;
        }
        return n;
    }

    private static short[][][] buildChicky(CPlayer cPlayer, int n, byte by) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)19);
        short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, cPlayer.angle, n, phys);
        int n2 = OfflineSpecialBullets.resolveSplitIndex(sArray, by);
        int n3 = n2 < sArray[0].length ? n2 : sArray[0].length - 1;
        short s = sArray[0][n3];
        short s2 = sArray[1][n3];
        short[][] sArray2 = OfflineBulletSim.buildPathFromPoint(cPlayer, s, s2 + 8, 0, 2, phys.ax100 / 2, phys.ay100 / 2, 30);
        return new short[][][]{sArray, sArray2};
    }

    private static short[][][] buildApache(CPlayer cPlayer, int n, byte by) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)17);
        short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, cPlayer.angle, n, phys);
        int n2 = OfflineSpecialBullets.resolveSplitIndex(sArray, by);
        int n3 = n2 < sArray[0].length ? n2 : sArray[0].length - 1;
        short s = sArray[0][n3];
        short s2 = sArray[1][n3];
        int n4 = cPlayer.angle;
        int n5 = n4 + CRes.fixangle(CRes.angle(cPlayer.x - s, cPlayer.y - 12 - s2));
        if (n4 < 90) {
            n5 = 180 - n5;
        }
        short[][][] sArrayArray = new short[4][][];
        sArrayArray[0] = OfflineSpecialBullets.truncatePath(sArray, n3);
        int n6 = n5 - 15;
        for (int i = 0; i < 3; ++i) {
            int n7 = n6 + i * 15;
            int n8 = s + (20 * CRes.cos(n7) >> 10);
            int n9 = s2 - 12 - (20 * CRes.sin(n7) >> 10);
            int n10 = n * CRes.cos(n7) >> 11;
            int n11 = -(n * CRes.sin(n7) >> 11);
            sArrayArray[i + 1] = OfflineBulletSim.buildPathFromPoint(cPlayer, n8, n9, n10, n11, phys.ax100, phys.ay100, phys.g100);
        }
        return sArrayArray;
    }

    private static int resolveSplitIndex(short[][] sArray, byte by) {
        int n = by & 0xFF;
        if (n < 2) {
            n = 2;
        }
        if (n >= sArray[0].length) {
            n = sArray[0].length - 1;
        }
        return n;
    }

    private static short[][] truncatePath(short[][] sArray, int n) {
        int n2 = n + 1;
        short[] sArray2 = new short[n2];
        short[] sArray3 = new short[n2];
        for (int i = 0; i < n2; ++i) {
            sArray2[i] = sArray[0][i];
            sArray3[i] = sArray[1][i];
        }
        return new short[][]{sArray2, sArray3};
    }

    private static boolean outOfPlay(int n, int n2) {
        return n < -100 || n > MM.mapWidth + 100 || n2 > MM.mapHeight + 100;
    }
}

