/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineGunPhysics;
import com.teamobi.mobiarmy2.OfflineSpecialBullets;
import effect.Tornado;
import item.BM;
import item.Bullet;
import java.util.Vector;
import map.MM;
import model.CRes;
import player.CPlayer;
import player.PM;
import screen.GameScr;

public final class OfflineBulletSim {
    private static final int MAX_FRAMES = 800;
    public static int[] lastBounceIndices = new int[0];

    private OfflineBulletSim() {
    }

    private static short at(Vector vector, int n) {
        return (Short)vector.elementAt(n);
    }

    public static short[][][] buildAllPaths(CPlayer cPlayer, byte by, byte by2, byte by3) {
        return OfflineBulletSim.buildAllPaths(cPlayer, by, by2, by3, cPlayer != null && cPlayer.isDoublePower);
    }

    public static short[][][] buildAllPaths(CPlayer cPlayer, byte by, byte by2, byte by3, boolean bl) {
        Object object;
        int n = OfflineBulletSim.clampForce(by2);
        if (by == 58) {
            try {
                return new short[][][]{OfflineBulletSim.buildBounceBombPath(cPlayer, n)};
            }
            catch (Exception exception) {
                exception.printStackTrace();
                return null;
            }
        }
        if (by == 49) {
            return new short[][][]{OfflineBulletSim.buildMagentaPath(cPlayer, n)};
        }
        if (by == 5) {
            int[] nArray = new int[4];
            OfflineGunPhysics.muzzle(cPlayer, cPlayer.angle, n, nArray);
            OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId(by);
            int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
            short[][] sArray = OfflineBulletSim.simulatePrimitivePath(nArray2, phys.ax100, phys.ay100, phys.g100, cPlayer, false);
            return new short[][][]{sArray};
        }
        short[][][] sArray = OfflineBulletSim.buildClientItemPaths(cPlayer, by, n);
        if (sArray != null) {
            return sArray;
        }
        if (OfflineSpecialBullets.usesSpecialBuilder(by) && (object = OfflineSpecialBullets.build(cPlayer, by, n, by3)) != null) {
            return (short[][][])object;
        }
        object = OfflineBulletSim.listShots(cPlayer, by, by2, by3, bl);
        short[][][] sArrayArray = new short[((Vector)object).size()][][];
        for (int i = 0; i < ((Vector)object).size(); ++i) {
            sArrayArray[i] = OfflineBulletSim.buildOnePath((Shot)((Vector)object).elementAt(i), cPlayer);
        }
        return sArrayArray;
    }

    private static short[][] buildMagentaPath(CPlayer cPlayer, int n) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)49);
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzleForcePlus(cPlayer, cPlayer.angle, n, 5, nArray);
        int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
        short[] sArray = new short[803];
        short[] sArray2 = new short[803];
        int n2 = 0;
        n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, nArray[0], nArray[1]);
        Bullet.dXLaser = 0;
        Bullet.dYLaser = 0;
        for (int i = 0; i < 800; ++i) {
            int n3 = nArray2[0];
            int n4 = nArray2[1];
            int n5 = n3 + nArray2[2];
            int n6 = n4 + nArray2[3];
            nArray2[0] = n5;
            nArray2[1] = n6;
            if (n5 < -100 || n5 > MM.mapWidth + 100 || n6 > MM.mapHeight + 100 || n6 < -9999999) {
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n5, n6);
                break;
            }
            int[] nArray3 = OfflineBulletSim.collisionPoint(n3, n4, n5, n6, cPlayer);
            if (nArray3 != null) {
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, nArray3[0], nArray3[1]);
                break;
            }
            OfflineGunPhysics.integrate(nArray2, phys.ax100, phys.ay100, phys.g100);
            OfflineBulletSim.applyTornadoLift(nArray2);
            if (nArray2[3] >= 0) {
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n5, n6);
                int n7 = CRes.angle(n5 - nArray[0], nArray[1] - n6);
                int n8 = n + 10;
                int n9 = n8 * CRes.cos(n7) >> 10;
                int n10 = n8 * CRes.sin(n7) >> 10;
                int n11 = n9;
                int n12 = -n10;
                if (n11 != 0) {
                    while (Math.abs(n11) < 15) {
                        n11 += n11;
                        n12 += n12;
                    }
                }
                Bullet.dXLaser = n11;
                Bullet.dYLaser = n12;
                int n13 = n9;
                int n14 = n10;
                if (n13 != 0) {
                    while (Math.abs(n13) < 15) {
                        n13 += n13;
                        n14 += n14;
                    }
                }
                int n15 = n3;
                int n16 = n4;
                int n17 = n5;
                int n18 = n6;
                for (int j = 0; j < 800 && n17 >= -100 && n17 <= MM.mapWidth + 100 && n18 <= MM.mapHeight + 100; n17 += n13, n18 += n14, ++j) {
                    nArray3 = OfflineBulletSim.collisionPoint(n15, n16, n17, n18, cPlayer);
                    if (nArray3 != null) {
                        n17 = nArray3[0];
                        n18 = nArray3[1];
                        break;
                    }
                    n15 = n17;
                    n16 = n18;
                }
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n17, n18);
                break;
            }
            n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n5, n6);
        }
        short[] sArray3 = new short[n2];
        short[] sArray4 = new short[n2];
        System.arraycopy(sArray, 0, sArray3, 0, n2);
        System.arraycopy(sArray2, 0, sArray4, 0, n2);
        return new short[][]{sArray3, sArray4};
    }

    public static short[][] buildMagentaPreviewPath(CPlayer cPlayer, int n) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)49);
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzleForcePlus(cPlayer, cPlayer.angle, n, 5, nArray);
        int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
        short[] sArray = new short[803];
        short[] sArray2 = new short[803];
        int n2 = 0;
        n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, nArray[0], nArray[1]);
        for (int i = 0; i < 800; ++i) {
            int n3 = nArray2[0];
            int n4 = nArray2[1];
            int n5 = n3 + nArray2[2];
            int n6 = n4 + nArray2[3];
            nArray2[0] = n5;
            nArray2[1] = n6;
            if (n5 < -100 || n5 > MM.mapWidth + 100 || n6 > MM.mapHeight + 100 || n6 < -9999999) {
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n5, n6);
                break;
            }
            OfflineGunPhysics.integrate(nArray2, phys.ax100, phys.ay100, phys.g100);
            OfflineBulletSim.applyTornadoLift(nArray2);
            if (nArray2[3] >= 0) {
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n5, n6);
                int n7 = CRes.angle(n5 - nArray[0], nArray[1] - n6);
                int n8 = n + 10;
                int n9 = n8 * CRes.cos(n7) >> 10;
                int n10 = n8 * CRes.sin(n7) >> 10;
                int n11 = n9;
                int n12 = n10;
                if (n11 != 0) {
                    while (Math.abs(n11) < 15) {
                        n11 += n11;
                        n12 += n12;
                    }
                }
                int n13 = n5;
                int n14 = n6;
                for (int j = 0; j < 800 && n13 >= -100 && n13 <= MM.mapWidth + 100 && n14 <= MM.mapHeight + 100; n13 += n11, n14 += n12, ++j) {
                }
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n13, n14);
                break;
            }
            n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n5, n6);
        }
        short[] sArray3 = new short[n2];
        short[] sArray4 = new short[n2];
        System.arraycopy(sArray, 0, sArray3, 0, n2);
        System.arraycopy(sArray2, 0, sArray4, 0, n2);
        return new short[][]{sArray3, sArray4};
    }

    private static short[][] buildBounceBombPath(CPlayer cPlayer, int n) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)58);
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzle(cPlayer, cPlayer.angle, n, nArray);
        int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
        short[] sArray = new short[803];
        short[] sArray2 = new short[803];
        int n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, 0, nArray[0], nArray[1]);
        int n3 = 0;
        int[] nArray3 = new int[2];
        int n4 = nArray2[1];
        double d = (double)Math.max(phys.g100, 1) / 100.0;
        int n5 = 0;
        while (n5 < 800) {
            int n6 = nArray2[0];
            int n7 = nArray2[1];
            int n8 = n6 + nArray2[2];
            int n9 = n7 + nArray2[3];
            nArray2[0] = n8;
            nArray2[1] = n9;
            if (n9 < n4) {
                n4 = n9;
            }
            if (n8 < -100 || n8 > MM.mapWidth + 100 || n9 > MM.mapHeight + 100) {
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n8, n9);
                break;
            }
            int[] nArray4 = OfflineBulletSim.collisionPoint(n6, n7, n8, n9, cPlayer);
            if (nArray4 != null) {
                int n10;
                int n11;
                n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, nArray4[0], nArray4[1]);
                if (OfflineCombat.isProjectileCollision(nArray4[0], nArray4[1], cPlayer)) break;
                int n12 = nArray4[1] - n4;
                if (n3 >= 2 || n12 <= 1 || (n11 = -((int)(Math.sqrt(2.0 * d * (double)(n10 = n12 / 2)) + 0.5))) >= 0) break;
                nArray2[0] = nArray4[0];
                nArray2[1] = nArray4[1] - 3;
                nArray2[3] = n11;
                nArray2[4] = 0;
                nArray2[5] = 0;
                nArray2[6] = 0;
                n4 = nArray4[1] - 3;
                nArray3[n3] = n2 - 1;
                ++n3;
                ++n5;
                continue;
            }
            n2 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n2, n8, n9);
            OfflineGunPhysics.integrate(nArray2, phys.ax100, phys.ay100, phys.g100);
            OfflineBulletSim.applyTornadoLift(nArray2);
            ++n5;
        }
        short[] sArray3 = new short[n2];
        short[] sArray4 = new short[n2];
        System.arraycopy(sArray, 0, sArray3, 0, n2);
        System.arraycopy(sArray2, 0, sArray4, 0, n2);
        int[] nArray5 = new int[n3];
        System.arraycopy(nArray3, 0, nArray5, 0, n3);
        lastBounceIndices = nArray5;
        return new short[][]{sArray3, sArray4};
    }

    public static short[][][] buildBossPaths(CPlayer cPlayer, byte by, byte by2) {
        if (by == 36) {
            int[] nArray = new int[4];
            OfflineGunPhysics.muzzle(cPlayer, cPlayer.angle, OfflineBulletSim.clampForce(by2), nArray);
            OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId(by);
            int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
            short[][] sArray = OfflineBulletSim.simulatePrimitivePath(nArray2, phys.ax100, phys.ay100, phys.g100, cPlayer, false);
            return new short[][][]{sArray};
        }
        Vector vector = OfflineBulletSim.listShots(cPlayer, by, by2, (byte)0, false, true);
        short[][][] sArrayArray = new short[vector.size()][][];
        for (int i = 0; i < vector.size(); ++i) {
            sArrayArray[i] = OfflineBulletSim.buildOnePath((Shot)vector.elementAt(i), cPlayer);
        }
        return sArrayArray;
    }

    public static short[][] buildBossBigRocketPath(CPlayer cPlayer, int n) {
        Object[] objectArray;
        int n2;
        short[] sArray = new short[160];
        short[] sArray2 = new short[160];
        int n3 = cPlayer.x;
        int n4 = cPlayer.y - 70;
        for (n2 = 0; n4 > -1000 && n2 < sArray.length - 2; ++n2) {
            sArray[n2] = (short)n3;
            sArray2[n2] = (short)(n4 -= 30);
        }
        n3 = n;
        sArray[n2] = (short)n3;
        sArray2[n2] = (short)n4;
        ++n2;
        while (n4 < MM.mapHeight + 100 && n2 < sArray.length) {
            int n5 = n4 + 30;
            int[] collP = OfflineBulletSim.collisionPoint(n3, n4, n3, n5, cPlayer);
            if (collP != null) {
                sArray[n2] = (short)collP[0];
                sArray2[n2] = (short)collP[1];
                ++n2;
                break;
            }
            n4 = n5;
            sArray[n2] = (short)n3;
            sArray2[n2] = (short)n4;
            ++n2;
        }
        short[] sArray3 = new short[n2];
        short[] sArray4 = new short[n2];
        System.arraycopy(sArray, 0, sArray3, 0, n2);
        System.arraycopy(sArray2, 0, sArray4, 0, n2);
        return new short[][]{sArray3, sArray4};
    }

    public static int[] findBossTrajectoryAim(CPlayer cPlayer, CPlayer cPlayer2, byte by, int n, int n2) {
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId(by);
        boolean bl = by == 8;
        int n3 = bl ? 5 : 1;
        int n4 = n;
        int n5 = OfflineBulletSim.clampForce(n2);
        int n6 = Integer.MAX_VALUE;
        for (int i = n5; i <= 30; ++i) {
            int n7 = n;
            for (int j = 180 - n; n7 <= 90 && j >= 90; n7 += n3, j -= n3) {
                int n8 = OfflineBulletSim.trajectoryAimScore(cPlayer, cPlayer2, n7, i, phys, bl);
                if (n8 < 0) {
                    return new int[]{n7, i};
                }
                if (n8 < n6) {
                    n6 = n8;
                    n4 = n7;
                    n5 = i;
                }
                if (j == n7) continue;
                n8 = OfflineBulletSim.trajectoryAimScore(cPlayer, cPlayer2, j, i, phys, bl);
                if (n8 < 0) {
                    return new int[]{j, i};
                }
                if (n8 >= n6) continue;
                n6 = n8;
                n4 = j;
                n5 = i;
            }
        }
        return new int[]{n4, n5};
    }

    private static int trajectoryAimScore(CPlayer cPlayer, CPlayer cPlayer2, int n, int n2, OfflineGunPhysics.Phys phys, boolean bl) {
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzle(cPlayer, n, n2, nArray);
        int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
        int n3 = cPlayer2.x;
        int n4 = cPlayer2.y - 12;
        int n5 = OfflineBulletSim.distanceSq(nArray[0], nArray[1], n3, n4);
        for (int i = 0; i < 800; ++i) {
            int[] nArray3;
            int n6 = nArray2[0];
            int n7 = nArray2[1];
            int n8 = n6 + nArray2[2];
            int n9 = n7 + nArray2[3];
            if (OfflineBulletSim.segmentTouchesTarget(n6, n7, n8, n9, cPlayer2)) {
                return -1;
            }
            if (bl && (nArray3 = OfflineBulletSim.collisionPoint(n6, n7, n8, n9, cPlayer, true)) != null) {
                return n5;
            }
            nArray2[0] = n8;
            nArray2[1] = n9;
            int n10 = OfflineBulletSim.distanceSq(n8, n9, n3, n4);
            if (n10 < n5) {
                n5 = n10;
            }
            if (n8 < -100 || n8 > MM.mapWidth + 100 || n9 > MM.mapHeight + 100) break;
            OfflineGunPhysics.integrate(nArray2, phys.ax100, phys.ay100, phys.g100);
        }
        return n5;
    }

    private static boolean segmentTouchesTarget(int n, int n2, int n3, int n4, CPlayer cPlayer) {
        int n5 = n3 - n;
        int n6 = n4 - n2;
        int n7 = Math.max(Math.abs(n5), Math.abs(n6));
        if (n7 == 0) {
            return n >= cPlayer.x - 12 && n <= cPlayer.x + 12 && n2 >= cPlayer.y - 24 && n2 <= cPlayer.y;
        }
        for (int i = 0; i <= n7; ++i) {
            int n8 = n + n5 * i / n7;
            int n9 = n2 + n6 * i / n7;
            if (n8 < cPlayer.x - 12 || n8 > cPlayer.x + 12 || n9 < cPlayer.y - 24 || n9 > cPlayer.y) continue;
            return true;
        }
        return false;
    }

    private static int distanceSq(int n, int n2, int n3, int n4) {
        int n5 = n - n3;
        int n6 = n2 - n4;
        return n5 * n5 + n6 * n6;
    }

    public static int trajectorySlotCount(CPlayer cPlayer, byte by) {
        return OfflineBulletSim.trajectorySlotCount(cPlayer, by, cPlayer != null && cPlayer.isDoublePower);
    }

    public static int trajectorySlotCount(CPlayer cPlayer, byte by, boolean bl) {
        short[][][] sArray;
        switch (by) {
            case 4:
            case 14: {
                return 2;
            }
            case 16: {
                return 7;
            }
            case 23: {
                return 8;
            }
            case 26: {
                return 5;
            }
            case 28: {
                return 14;
            }
        }
        if (by == 17) {
            return 4;
        }
        if (by == 19) {
            return 2;
        }
        if (OfflineSpecialBullets.usesSpecialBuilder(by) && (sArray = OfflineSpecialBullets.build(cPlayer, by, 1, (byte)2)) != null) {
            return sArray.length;
        }
        return OfflineBulletSim.listShots(cPlayer, by, (byte)1, (byte)0, bl).size();
    }

    private static short[][][] buildClientItemPaths(CPlayer cPlayer, byte by, int n) {
        if (cPlayer == null) {
            return null;
        }
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId(by);
        if (by == 4) {
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, cPlayer.angle, n, phys);
            int n2 = sArray[0].length - 1;
            short s = sArray[0][n2];
            short s2 = sArray[1][n2];
            return new short[][][]{sArray, OfflineBulletSim.buildDropToTarget(s - 140, s2 - 320, 5, 0, s, s2, 80)};
        }
        if (by == 14) {
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, cPlayer.angle, n, phys);
            int n3 = sArray[0].length - 1;
            short s = sArray[0][n3];
            short s3 = sArray[1][n3];
            return new short[][][]{sArray, new short[][]{{s}, {s3}}};
        }
        if (by == 16) {
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, cPlayer.angle, n, phys);
            int n4 = sArray[0].length - 1;
            short s = sArray[0][n4];
            short s4 = sArray[1][n4];
            int[] nArray = new int[]{-8, 12, -19, 18, 20, -20};
            int[] nArray2 = new int[]{-493, -496, -505, -505, -512, -512};
            int[] nArray3 = new int[]{-1, 0, -2, 1, 2, -3};
            int[] nArray4 = new int[]{2, 1, 1, 1, 0, 0};
            short[][][] sArrayArray = new short[7][][];
            sArrayArray[0] = sArray;
            OfflineGunPhysics.Phys phys2 = OfflineGunPhysics.forBulletId((byte)12);
            for (int i = 0; i < 6; ++i) {
                sArrayArray[i + 1] = OfflineBulletSim.buildPathFromPoint(cPlayer, s + nArray[i], s4 + nArray2[i], nArray3[i], nArray4[i], phys2.ax100, phys2.ay100, phys2.g100);
            }
            return sArrayArray;
        }
        if (by == 23) {
            short[][] sArray = OfflineBulletSim.buildPathFromMuzzle(cPlayer, cPlayer.angle, n, phys);
            int n5 = sArray[0].length - 1;
            short s = sArray[0][n5];
            short s5 = sArray[1][n5];
            int[] nArray = new int[]{-90, -60, -30, 0, 30, 60, 90};
            int[] nArray5 = new int[]{0, 1, 2, 3, 2, 1, 0};
            short[][][] sArrayArray = new short[8][][];
            sArrayArray[0] = sArray;
            OfflineGunPhysics.Phys phys3 = OfflineGunPhysics.forBulletId((byte)24);
            for (int i = 0; i < 7; ++i) {
                sArrayArray[i + 1] = OfflineBulletSim.buildPathFromPoint(cPlayer, s + nArray[i], s5 - 187, 0, nArray5[i], phys3.ax100, phys3.ay100, phys3.g100);
            }
            return sArrayArray;
        }
        if (by == 22) {
            boolean bl = cPlayer.angle < 89;
            int n6 = cPlayer.x + (bl ? 1 : -1);
            int n7 = cPlayer.y;
            int n8 = n * 3;
            short[] sArray = new short[n8 + 2];
            short[] sArray2 = new short[n8 + 2];
            int n9 = 0;
            sArray[n9] = (short)n6;
            sArray2[n9] = (short)n7;
            ++n9;
            int n10 = 0;
            for (int i = 0; i < n8; ++i) {
                ++n10;
                for (int j = 0; j < n10; ++j) {
                    if (GameScr.mm != null && GameScr.mm.isLand(n6, n7)) {
                        n10 = 0;
                        break;
                    }
                    ++n7;
                }
                int n11 = bl ? 4 : -4;
                if (GameScr.mm != null && GameScr.mm.isLand(n6 += n11, n7 - 5)) {
                    n6 -= n11;
                } else {
                    for (int j = 4; j >= 0; --j) {
                        if (GameScr.mm == null || !GameScr.mm.isLand(n6, n7 - j)) continue;
                        n7 -= j;
                        break;
                    }
                }
                if (n7 > MM.mapHeight + 100) {
                    sArray[n9] = (short)n6;
                    sArray2[n9] = (short)n7;
                    ++n9;
                    break;
                }
                sArray[n9] = (short)n6;
                sArray2[n9] = (short)n7;
                ++n9;
            }
            short[] sArray3 = new short[n9];
            short[] sArray4 = new short[n9];
            System.arraycopy(sArray, 0, sArray3, 0, n9);
            System.arraycopy(sArray2, 0, sArray4, 0, n9);
            return new short[][][]{new short[][]{sArray3, sArray4}};
        }
        if (by == 26) {
            Object[] objectArray;
            int n12;
            int n13;
            int n14;
            int n15;
            int[] nArray = new int[4];
            OfflineGunPhysics.muzzle(cPlayer, cPlayer.angle, n, nArray);
            int[] nArray6 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
            Vector vector = new Vector();
            Vector vector2 = new Vector();
            OfflineBulletSim.addPathPoint(vector, vector2, nArray[0], nArray[1]);
            for (int i = 0; i < n; ++i) {
                int n16 = nArray6[0];
                n15 = nArray6[1];
                nArray6[0] = nArray6[0] + nArray6[2];
                nArray6[1] = nArray6[1] + nArray6[3];
                n14 = nArray6[0];
                n13 = nArray6[1];
                if (n14 < -100 || n14 > MM.mapWidth + 100 || n13 > MM.mapHeight + 100 || n13 < -(MM.mapHeight + 100)) {
                    OfflineBulletSim.addPathPointForce(vector, vector2, n14, n13);
                    break;
                }
                int[] nArray7 = OfflineBulletSim.collisionPoint(n16, n15, n14, n13, cPlayer);
                if (nArray7 != null) {
                    OfflineBulletSim.addPathPointForce(vector, vector2, nArray7[0], nArray7[1]);
                    break;
                }
                OfflineBulletSim.addPathPoint(vector, vector2, n14, n13);
                OfflineGunPhysics.integrate(nArray6, phys.ax100, phys.ay100, phys.g100);
                OfflineBulletSim.applyTornadoLift(nArray6);
            }
            short[][] sArray = OfflineBulletSim.compressPathInternal(vector, vector2);
            int n17 = sArray[0].length - 1;
            int n18 = sArray[0][n17];
            int n19 = sArray[1][n17];
            int[] nArray8 = OfflineGunPhysics.newState(n18, n19, nArray6[2], nArray6[3]);
            n15 = n18;
            n14 = n19;
            n13 = 1;
            for (n12 = 0; n12 < 800; ++n12) {
                int n20 = nArray8[0];
                int n21 = nArray8[1];
                nArray8[0] = nArray8[0] + nArray8[2];
                nArray8[1] = nArray8[1] + nArray8[3];
                n15 = nArray8[0];
                n14 = nArray8[1];
                if (n15 < -100 || n15 > MM.mapWidth + 100 || n14 > MM.mapHeight + 100 || n14 < -(MM.mapHeight + 100)) break;
                int[] collP2 = OfflineBulletSim.collisionPoint(n20, n21, n15, n14, cPlayer);
                if (collP2 != null) {
                    n15 = collP2[0];
                    n14 = collP2[1];
                    break;
                }
                OfflineGunPhysics.integrate(nArray8, phys.ax100, phys.ay100, phys.g100);
                OfflineBulletSim.applyTornadoLift(nArray8);
            }
            n13 = Math.max(4, n12);
            OfflineGunPhysics.Phys phys4 = OfflineGunPhysics.forBulletId((byte)27);
            short[][][] sArrayArray = new short[5][][];
            sArrayArray[0] = sArray;
            double[] weights = new double[]{0.55, 0.8, 1.0, 1.3};
            for (int i = 0; i < 4; ++i) {
                int n22 = (int)((double)n13 * weights[i] + 0.5);
                if (n22 < 3) {
                    n22 = 3;
                }
                int[] nArray9 = OfflineBulletSim.solveVelocityToTarget(n15 - n18, n14 - n19, n22, phys4.g100);
                sArrayArray[i + 1] = OfflineBulletSim.buildPathFromPoint(cPlayer, n18, n19, nArray9[0], nArray9[1], phys4.ax100, phys4.ay100, phys4.g100);
            }
            return sArrayArray;
        }
        if (by == 28) {
            short[][] sArray = OfflineBulletSim.buildPathToApex(cPlayer, n, phys);
            int n23 = sArray[0].length - 1;
            short s = sArray[0][n23];
            short s6 = sArray[1][n23];
            int[] nArray = new int[]{18, -19, 16, -17, 14, -15, 11, -12, 8, -9, 5, -6, -2};
            int[] nArray10 = new int[]{-20, -20, -23, -23, -26, -26, -28, -28, -30, -30, -31, -31, -31};
            int[] nArray11 = new int[]{2, -3, 3, 4, 3, -4, 3, -4, 2, -3, 1, -2, 1};
            int[] nArray12 = new int[]{-1, -1, -2, -2, -3, -3, -4, -4, -5, -5, -6, -6, -7};
            short[][][] sArrayArray = new short[14][][];
            sArrayArray[0] = sArray;
            OfflineGunPhysics.Phys phys5 = OfflineGunPhysics.forBulletId((byte)29);
            for (int i = 0; i < 13; ++i) {
                sArrayArray[i + 1] = OfflineBulletSim.buildPathFromPoint(cPlayer, s + nArray[i], s6 + nArray10[i], nArray11[i], nArray12[i], phys5.ax100, phys5.ay100, phys5.g100);
            }
            return sArrayArray;
        }
        if (by == 30) {
            int n24 = n * 2;
            short[] sArray = new short[n24 + 1];
            short[] sArray5 = new short[n24 + 1];
            int n25 = cPlayer.x;
            int n26 = cPlayer.y;
            sArray[0] = (short)n25;
            sArray5[0] = (short)n26;
            for (int i = 1; i <= n24; ++i) {
                sArray[i] = (short)n25;
                sArray5[i] = (short)(n26 += 2);
            }
            return new short[][][]{new short[][]{sArray, sArray5}};
        }
        return null;
    }

    private static short[][] buildDropToTarget(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        OfflineBulletSim.addPathPoint(vector, vector2, n, n2);
        int n8 = 0;
        for (int i = 0; i < 800 && n2 < n6; ++i) {
            n += n3;
            n2 += n4;
            if ((n8 += n7) >= 100) {
                n4 += n8 / 100;
                n8 %= 100;
            }
            OfflineBulletSim.addPathPoint(vector, vector2, n, n2);
        }
        OfflineBulletSim.addPathPointForce(vector, vector2, n5, n6);
        return OfflineBulletSim.compressPath(vector, vector2);
    }

    private static short[][] buildPathToApex(CPlayer cPlayer, int n, OfflineGunPhysics.Phys phys) {
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzle(cPlayer, cPlayer.angle, n, nArray);
        int[] nArray2 = OfflineGunPhysics.newState(nArray[0], nArray[1], nArray[2], nArray[3]);
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        OfflineBulletSim.addPathPoint(vector, vector2, nArray[0], nArray[1]);
        for (int i = 0; i < 800; ++i) {
            nArray2[0] = nArray2[0] + nArray2[2];
            nArray2[1] = nArray2[1] + nArray2[3];
            OfflineBulletSim.addPathPoint(vector, vector2, nArray2[0], nArray2[1]);
            OfflineGunPhysics.integrate(nArray2, phys.ax100, phys.ay100, phys.g100);
            OfflineBulletSim.applyTornadoLift(nArray2);
            if (nArray2[3] >= 0) break;
        }
        return OfflineBulletSim.compressPath(vector, vector2);
    }

    static short[][] compressPath(Vector vector, Vector vector2) {
        return OfflineBulletSim.compressPathInternal(vector, vector2);
    }

    static short[][] buildPathFromMuzzle(CPlayer cPlayer, int n, int n2, OfflineGunPhysics.Phys phys) {
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzle(cPlayer, n, n2, nArray);
        return OfflineBulletSim.buildOnePath(new Shot(nArray[0], nArray[1], nArray[2], nArray[3], phys.ax100, phys.ay100, phys.g100), cPlayer);
    }

    static short[][] buildPathFromMuzzle(CPlayer cPlayer, int n, int n2, int n3, int n4, int n5) {
        return OfflineBulletSim.buildOnePath(new Shot(cPlayer.x + (20 * CRes.cos(n) >> 10), cPlayer.y - 12 - (20 * CRes.sin(n) >> 10), n2 * CRes.cos(n) >> 10, -(n2 * CRes.sin(n) >> 10), n3, n4, n5), cPlayer);
    }

    static short[][] buildPathFromPoint(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        return OfflineBulletSim.buildOnePath(new Shot(n, n2, n3, n4, n5, n6, n7), null);
    }

    static short[][] buildPathFromPoint(CPlayer cPlayer, int n, int n2, int n3, int n4, int n5, int n6, int n7) {
        return OfflineBulletSim.buildOnePath(new Shot(n, n2, n3, n4, n5, n6, n7), cPlayer);
    }

    private static int[] solveVelocityToTarget(int n, int n2, int n3, int n4) {
        if (n3 < 1) {
            n3 = 1;
        }
        int n5 = n / n3;
        int n6 = n4 * n3 * (n3 - 1) / 200;
        int n7 = (n2 - n6) / n3;
        return new int[]{n5, n7};
    }

    public static int trajectorySlotCount(byte by, byte by2) {
        CPlayer cPlayer;
        CPlayer cPlayer2 = cPlayer = GameScr.pm != null ? PM.getMyPlayer() : null;
        if (cPlayer != null) {
            return OfflineBulletSim.trajectorySlotCount(cPlayer, by);
        }
        if (by == 1) {
            return 2;
        }
        if (by == 2) {
            return 3;
        }
        if (by == 9) {
            return 4;
        }
        if (by == 10) {
            return 3;
        }
        if (by == 11 || by == 33) {
            return 5;
        }
        if (by == 56) {
            return 3;
        }
        if (by == 17) {
            return 4;
        }
        if (by == 19) {
            return 2;
        }
        return 1;
    }

    public static int clampForce(int n) {
        if (n < 1) {
            return 1;
        }
        return n > 30 ? 30 : n;
    }

    private static short[][] buildOnePath(Shot shot, CPlayer cPlayer) {
        if (shot.penetrateTerrain || shot.maxFrames < 800) {
            return OfflineBulletSim.simulatePrimitivePath(OfflineGunPhysics.newState(shot.x, shot.y, shot.vx, shot.vy), shot.ax100, shot.ay100, shot.g100, cPlayer, true, shot.penetrateTerrain, shot.maxFrames);
        }
        return OfflineBulletSim.simulatePath(OfflineGunPhysics.newState(shot.x, shot.y, shot.vx, shot.vy), shot.ax100, shot.ay100, shot.g100, null, cPlayer);
    }

    static short[][] simulatePath(int[] nArray, int n, int n2, int n3, FrameHook frameHook) {
        return OfflineBulletSim.simulatePath(nArray, n, n2, n3, frameHook, null);
    }

    static short[][] simulatePath(int[] nArray, int n, int n2, int n3, FrameHook frameHook, CPlayer cPlayer) {
        if (frameHook == null) {
            return OfflineBulletSim.simulatePrimitivePath(nArray, n, n2, n3, cPlayer);
        }
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        int n4 = nArray[0];
        int n5 = nArray[1];
        OfflineBulletSim.addPathPoint(vector, vector2, n4, n5);
        for (int i = 0; i < 800; ++i) {
            int n6 = nArray[0];
            int n7 = nArray[1];
            nArray[0] = nArray[0] + nArray[2];
            nArray[1] = nArray[1] + nArray[3];
            int n8 = nArray[0];
            int n9 = nArray[1];
            if (n8 < -100 || n8 > MM.mapWidth + 100 || n9 > MM.mapHeight + 100) {
                OfflineBulletSim.addPathPointForce(vector, vector2, n8, n9);
                break;
            }
            int[] nArray2 = OfflineBulletSim.collisionPoint(n6, n7, n8, n9, cPlayer);
            if (nArray2 != null) {
                nArray[0] = nArray2[0];
                nArray[1] = nArray2[1];
                OfflineBulletSim.addPathPointForce(vector, vector2, nArray2[0], nArray2[1]);
                break;
            }
            OfflineBulletSim.addPathPoint(vector, vector2, n8, n9);
            OfflineGunPhysics.integrate(nArray, n, n2, n3);
            OfflineBulletSim.applyTornadoLift(nArray);
            if (frameHook == null) continue;
            frameHook.afterIntegrate(nArray, vector, vector2);
        }
        return OfflineBulletSim.compressPathInternal(vector, vector2);
    }

    private static short[][] simulatePrimitivePath(int[] nArray, int n, int n2, int n3, CPlayer cPlayer) {
        return OfflineBulletSim.simulatePrimitivePath(nArray, n, n2, n3, cPlayer, true);
    }

    private static short[][] simulatePrimitivePath(int[] nArray, int n, int n2, int n3, CPlayer cPlayer, boolean bl) {
        return OfflineBulletSim.simulatePrimitivePath(nArray, n, n2, n3, cPlayer, bl, false, 800);
    }

    private static short[][] simulatePrimitivePath(int[] nArray, int n, int n2, int n3, CPlayer cPlayer, boolean bl, boolean bl2) {
        return OfflineBulletSim.simulatePrimitivePath(nArray, n, n2, n3, cPlayer, bl, bl2, 800);
    }

    private static short[][] simulatePrimitivePath(int[] nArray, int n, int n2, int n3, CPlayer cPlayer, boolean bl, boolean bl2, int n4) {
        short[] sArray = new short[802];
        short[] sArray2 = new short[802];
        int n5 = 0;
        sArray[n5] = (short)nArray[0];
        sArray2[n5] = (short)nArray[1];
        ++n5;
        int n6 = n4 < 800 ? n4 : 800;
        for (int i = 0; i < n6; ++i) {
            int n7 = nArray[0];
            int n8 = nArray[1];
            nArray[0] = nArray[0] + nArray[2];
            nArray[1] = nArray[1] + nArray[3];
            int n9 = nArray[0];
            int n10 = nArray[1];
            if (n9 < -1500 || n9 > MM.mapWidth + 1500 || n10 > MM.mapHeight + 100 || n10 < -(MM.mapHeight + 2000)) {
                n5 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n5, n9, n10);
                break;
            }
            int[] nArray2 = OfflineBulletSim.collisionPoint(n7, n8, n9, n10, cPlayer, bl, bl2);
            if (nArray2 != null) {
                nArray[0] = nArray2[0];
                nArray[1] = nArray2[1];
                n5 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n5, nArray2[0], nArray2[1]);
                break;
            }
            n5 = OfflineBulletSim.appendPrimitivePoint(sArray, sArray2, n5, n9, n10);
            OfflineGunPhysics.integrate(nArray, n, n2, n3);
            OfflineBulletSim.applyTornadoLift(nArray);
        }
        short[] sArray3 = new short[n5];
        short[] sArray4 = new short[n5];
        System.arraycopy(sArray, 0, sArray3, 0, n5);
        System.arraycopy(sArray2, 0, sArray4, 0, n5);
        return new short[][]{sArray3, sArray4};
    }

    private static int appendPrimitivePoint(short[] sArray, short[] sArray2, int n, int n2, int n3) {
        short s = (short)n2;
        short s2 = (short)n3;
        if (n == 0 || sArray[n - 1] != s || sArray2[n - 1] != s2) {
            sArray[n] = s;
            sArray2[n] = s2;
            return n + 1;
        }
        return n;
    }

    static short[][] simulateStraight(int n, int n2, int n3, int n4) {
        return OfflineBulletSim.simulateStraight(n, n2, n3, n4, null);
    }

    static short[][] simulateStraight(int n, int n2, int n3, int n4, CPlayer cPlayer) {
        Vector vector = new Vector();
        Vector vector2 = new Vector();
        OfflineBulletSim.addPathPoint(vector, vector2, n, n2);
        for (int i = 0; i < 800; ++i) {
            int n5 = n;
            int n6 = n2;
            if ((n += n3) < -100 || n > MM.mapWidth + 100 || (n2 += n4) > MM.mapHeight + 100) {
                OfflineBulletSim.addPathPointForce(vector, vector2, n, n2);
                break;
            }
            int[] nArray = OfflineBulletSim.collisionPoint(n5, n6, n, n2, cPlayer);
            if (nArray != null) {
                OfflineBulletSim.addPathPointForce(vector, vector2, nArray[0], nArray[1]);
                break;
            }
            OfflineBulletSim.addPathPoint(vector, vector2, n, n2);
            if (!OfflineBulletSim.isInsideTornado(n, n2)) continue;
            n4 -= 2;
        }
        return OfflineBulletSim.compressPathInternal(vector, vector2);
    }

    private static void applyTornadoLift(int[] nArray) {
        if (OfflineBulletSim.isInsideTornado(nArray[0], nArray[1])) {
            nArray[3] = nArray[3] - 2;
        }
    }

    private static boolean isInsideTornado(int n, int n2) {
        if (BM.vTornado == null || BM.vTornado.isEmpty()) {
            return false;
        }
        for (int i = 0; i < BM.vTornado.size(); ++i) {
            Tornado tornado = (Tornado)BM.vTornado.elementAt(i);
            if (tornado == null || tornado.nturn < 0 || Math.abs(tornado.x + 10 - n) > 32) continue;
            return true;
        }
        return false;
    }

    static int[] collisionPoint(int n, int n2, int n3, int n4, CPlayer cPlayer) {
        return OfflineBulletSim.collisionPoint(n, n2, n3, n4, cPlayer, true);
    }

    private static int[] collisionPoint(int n, int n2, int n3, int n4, CPlayer cPlayer, boolean bl) {
        return OfflineBulletSim.collisionPoint(n, n2, n3, n4, cPlayer, bl, false);
    }

    private static int[] collisionPoint(int n, int n2, int n3, int n4, CPlayer cPlayer, boolean bl, boolean bl2) {
        int n5;
        int n6;
        int n7 = n3 - n;
        int n8 = n4 - n2;
        int n9 = n7 < 0 ? -1 : (n7 > 0 ? 1 : 0);
        int n10 = n8 < 0 ? -1 : (n8 > 0 ? 1 : 0);
        int n11 = n9;
        int n12 = n10;
        int n13 = Math.abs(n7);
        if (n13 > (n5 = Math.abs(n8))) {
            n12 = 0;
        } else {
            n13 = Math.abs(n8);
            n5 = Math.abs(n7);
            n11 = 0;
        }
        int n14 = n13 >> 1;
        int n15 = n;
        int n16 = n2;
        for (int i = 0; i <= n13; ++i) {
            if (!bl2 && GameScr.mm != null && GameScr.mm.isLand(n15, n16) || bl && OfflineCombat.isProjectileCollision(n15, n16, cPlayer)) {
                return new int[]{n15, n16};
            }
            if ((n14 += n5) >= n13 && n13 != 0) {
                n14 -= n13;
                n15 += n9;
                n16 += n10;
                continue;
            }
            n15 += n11;
            n16 += n12;
        }
        return null;
    }

    private static void addPoint(Vector vector, Vector vector2, int n, int n2) {
        OfflineBulletSim.addPoint(vector, vector2, n, n2, false);
    }

    static void addPathPoint(Vector vector, Vector vector2, int n, int n2) {
        OfflineBulletSim.addPoint(vector, vector2, n, n2, false);
    }

    static void addPathPointForce(Vector vector, Vector vector2, int n, int n2) {
        OfflineBulletSim.addPoint(vector, vector2, n, n2, true);
    }

    private static void addPoint(Vector vector, Vector vector2, int n, int n2, boolean bl) {
        if (vector.isEmpty()) {
            vector.addElement(new Short((short)n));
            vector2.addElement(new Short((short)n2));
            return;
        }
        if (OfflineBulletSim.at(vector, vector.size() - 1) != (short)n || OfflineBulletSim.at(vector2, vector2.size() - 1) != (short)n2) {
            vector.addElement(new Short((short)n));
            vector2.addElement(new Short((short)n2));
        }
    }

    private static short[][] compressPathInternal(Vector vector, Vector vector2) {
        int n;
        if (vector.isEmpty()) {
            return new short[][]{new short[0], new short[0]};
        }
        Vector<Short> vector3 = new Vector<Short>();
        Vector<Short> vector4 = new Vector<Short>();
        vector3.addElement(new Short(OfflineBulletSim.at(vector, 0)));
        vector4.addElement(new Short(OfflineBulletSim.at(vector2, 0)));
        for (int i = 1; i < vector.size(); ++i) {
            short s = OfflineBulletSim.at(vector, i);
            short s2 = OfflineBulletSim.at(vector2, i);
            n = OfflineBulletSim.at(vector3, vector3.size() - 1);
            int n2 = OfflineBulletSim.at(vector4, vector4.size() - 1);
            int n3 = s - n;
            int n4 = s2 - n2;
            while (n3 < -127 || n3 > 127 || n4 < -127 || n4 > 127) {
                int n5 = OfflineBulletSim.clamp(n3, -127, 127);
                int n6 = OfflineBulletSim.clamp(n4, -127, 127);
                if (n5 == 0 && n3 != 0) {
                    int n7 = n5 = n3 > 0 ? 1 : -1;
                }
                if (n6 == 0 && n4 != 0) {
                    n6 = n4 > 0 ? 1 : -1;
                }
                vector3.addElement(new Short((short)(n += n5)));
                vector4.addElement(new Short((short)(n2 += n6)));
                n3 = s - n;
                n4 = s2 - n2;
            }
            vector3.addElement(new Short(s));
            vector4.addElement(new Short(s2));
        }
        short[] sArray = new short[vector3.size()];
        short[] sArray2 = new short[vector4.size()];
        for (n = 0; n < vector3.size(); ++n) {
            sArray[n] = OfflineBulletSim.at(vector3, n);
            sArray2[n] = OfflineBulletSim.at(vector4, n);
        }
        return new short[][]{sArray, sArray2};
    }

    private static int clamp(int n, int n2, int n3) {
        if (n < n2) {
            return n2;
        }
        return n > n3 ? n3 : n;
    }

    private static Vector listShots(CPlayer cPlayer, byte by, byte by2, byte by3, boolean bl) {
        return OfflineBulletSim.listShots(cPlayer, by, by2, by3, bl, false);
    }

    private static Vector listShots(CPlayer cPlayer, byte by, byte by2, byte by3, boolean bl, boolean bl2) {
        int n = OfflineBulletSim.clampForce(by2);
        int n2 = cPlayer.angle;
        byte by4 = Bullet.setBulletType(cPlayer.gun);
        boolean bl3 = !bl2 && by != by4;
        Vector<Shot> vector = new Vector<Shot>();
        if (bl3) {
            OfflineBulletSim.appendItemShots(vector, cPlayer, by, n2, n);
            return vector;
        }
        OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId(by);
        switch (by) {
            case 1: {
                Shot shot = OfflineBulletSim.muzzleShot(cPlayer, n2, n, phys);
                int n3 = bl ? 6 : 2;
                OfflineBulletSim.addDuplicates(vector, shot, n3);
                break;
            }
            case 2: {
                int n4 = bl ? 6 : 3;
                int n5 = n2 - n4 / 2 * 5;
                for (int i = 0; i < n4; ++i) {
                    vector.addElement(OfflineBulletSim.muzzleShot(cPlayer, n5 + i * 5, n, phys));
                }
                break;
            }
            case 6: {
                Shot shot = OfflineBulletSim.muzzleShot(cPlayer, n2, n, phys);
                OfflineBulletSim.addDuplicates(vector, shot, 3);
                break;
            }
            case 9: {
                int n6 = n2 - 6;
                for (int i = 0; i < 4; ++i) {
                    vector.addElement(OfflineBulletSim.muzzleShot(cPlayer, n6, n, phys));
                    n6 += 4;
                }
                break;
            }
            case 10: {
                Shot shot = OfflineBulletSim.muzzleShot(cPlayer, n2, n, phys);
                OfflineBulletSim.addDuplicates(vector, shot, 3);
                break;
            }
            case 11: {
                Shot shot = OfflineBulletSim.muzzleShot(cPlayer, n2, n, phys);
                OfflineBulletSim.addDuplicates(vector, shot, 5);
                break;
            }
            case 33: {
                Shot shot = OfflineBulletSim.muzzleShot(cPlayer, n2, n, phys);
                OfflineBulletSim.addDuplicates(vector, shot, 5);
                break;
            }
            case 49: {
                vector.addElement(OfflineBulletSim.magentaMuzzleShot(cPlayer, n2, n, phys));
                break;
            }
            case 56: {
                int n7 = n2 - 5;
                OfflineGunPhysics.Phys phys2 = OfflineGunPhysics.forBulletId((byte)56);
                for (int i = 0; i < 3; ++i) {
                    vector.addElement(OfflineBulletSim.muzzleShot(cPlayer, n7, n, phys2));
                    n7 += 5;
                }
                break;
            }
            default: {
                vector.addElement(OfflineBulletSim.muzzleShot(cPlayer, n2, n, phys));
            }
        }
        return vector;
    }

    private static void appendItemShots(Vector vector, CPlayer cPlayer, byte by, int n, int n2) {
        switch (by) {
            case 6: {
                Shot shot = OfflineBulletSim.muzzleShot(cPlayer, n, n2, OfflineGunPhysics.forBulletId((byte)6)).withPenetrateTerrain();
                OfflineBulletSim.addDuplicates(vector, shot, 3);
                break;
            }
            case 25:
            case 52: {
                vector.addElement(OfflineBulletSim.muzzleShot(cPlayer, n, n2, OfflineGunPhysics.forBulletId(by)).withPenetrateTerrain());
                break;
            }
            case 56: {
                int n3 = n - 5;
                OfflineGunPhysics.Phys phys = OfflineGunPhysics.forBulletId((byte)56);
                for (int i = 0; i < 3; ++i) {
                    vector.addElement(OfflineBulletSim.muzzleShot(cPlayer, n3, n2, phys));
                    n3 += 5;
                }
                break;
            }
            default: {
                vector.addElement(OfflineBulletSim.muzzleShot(cPlayer, n, n2, OfflineGunPhysics.forBulletId(by)));
            }
        }
    }

    private static void addDuplicates(Vector vector, Shot shot, int n) {
        for (int i = 0; i < n; ++i) {
            vector.addElement(shot.duplicate());
        }
    }

    private static Shot muzzleShot(CPlayer cPlayer, int n, int n2, OfflineGunPhysics.Phys phys) {
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzle(cPlayer, n, n2, nArray);
        return new Shot(nArray[0], nArray[1], nArray[2], nArray[3], phys.ax100, phys.ay100, phys.g100);
    }

    private static Shot magentaMuzzleShot(CPlayer cPlayer, int n, int n2, OfflineGunPhysics.Phys phys) {
        int[] nArray = new int[4];
        OfflineGunPhysics.muzzleForcePlus(cPlayer, n, n2, 5, nArray);
        return new Shot(nArray[0], nArray[1], nArray[2], nArray[3], phys.ax100, phys.ay100, phys.g100);
    }

    private static final class Shot {
        final int x;
        final int y;
        final int vx;
        final int vy;
        final int ax100;
        final int ay100;
        final int g100;
        final boolean penetrateTerrain;
        final int maxFrames;

        Shot(int n, int n2, int n3, int n4, int n5, int n6, int n7) {
            this(n, n2, n3, n4, n5, n6, n7, false, 800);
        }

        Shot(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl) {
            this(n, n2, n3, n4, n5, n6, n7, bl, 800);
        }

        Shot(int n, int n2, int n3, int n4, int n5, int n6, int n7, boolean bl, int n8) {
            this.x = n;
            this.y = n2;
            this.vx = n3;
            this.vy = n4;
            this.ax100 = n5;
            this.ay100 = n6;
            this.g100 = n7;
            this.penetrateTerrain = bl;
            this.maxFrames = n8;
        }

        Shot duplicate() {
            return new Shot(this.x, this.y, this.vx, this.vy, this.ax100, this.ay100, this.g100, this.penetrateTerrain, this.maxFrames);
        }

        Shot withPenetrateTerrain() {
            return new Shot(this.x, this.y, this.vx, this.vy, this.ax100, this.ay100, this.g100, true, this.maxFrames);
        }

        Shot withMaxFrames(int n) {
            return new Shot(this.x, this.y, this.vx, this.vy, this.ax100, this.ay100, this.g100, this.penetrateTerrain, n);
        }
    }

    static interface FrameHook {
        public void afterIntegrate(int[] var1, Vector var2, Vector var3);
    }
}

