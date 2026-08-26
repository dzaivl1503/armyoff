/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import item.Bullet;
import model.CRes;
import player.Boss;
import player.CPlayer;
import screen.GameScr;

public final class OfflineGunPhysics {
    public static final int MUZZLE_W = 24;
    public static final int MUZZLE_H = 24;
    public static final int ST_X = 0;
    public static final int ST_Y = 1;
    public static final int ST_VX = 2;
    public static final int ST_VY = 3;
    public static final int ST_VXT = 4;
    public static final int ST_VYT = 5;
    public static final int ST_VYT2 = 6;

    private OfflineGunPhysics() {
    }

    public static Phys forPlayerGun(byte by) {
        return OfflineGunPhysics.forBulletId(Bullet.setBulletType(by));
    }

    public static Phys forBulletId(byte by) {
        int n = GameScr.windx;
        int n2 = GameScr.windy;
        switch (by) {
            case 0: {
                return OfflineGunPhysics.p(n * 80 / 100, n2 * 80 / 100, 100);
            }
            case 1: {
                return OfflineGunPhysics.p(n * 50 / 100, n2 * 50 / 100, 50);
            }
            case 2: {
                return OfflineGunPhysics.p(n * 80 / 100, n2 * 80 / 100, 60);
            }
            case 4: {
                return OfflineGunPhysics.p(0, 0, 80);
            }
            case 5: {
                return OfflineGunPhysics.p(0, 0, 80);
            }
            case 6: {
                return OfflineGunPhysics.p(n * 70 / 100, n2 * 70 / 100, 90);
            }
            case 7: {
                return OfflineGunPhysics.p(n * 70 / 100, n2 * 70 / 100, 80);
            }
            case 8: {
                return OfflineGunPhysics.p(n * 70 / 100, n2 * 70 / 100, 70);
            }
            case 9: {
                return OfflineGunPhysics.p(n * 40 / 100, n2 * 40 / 100, 90);
            }
            case 10: {
                return OfflineGunPhysics.p(n * 50 / 100, n2 * 50 / 100, 80);
            }
            case 11: {
                return OfflineGunPhysics.p(n * 30 / 100, n2 * 30 / 100, 90);
            }
            case 12: {
                return OfflineGunPhysics.p(0, 0, 100);
            }
            case 13: {
                return OfflineGunPhysics.p(n * 50 / 100, n2 * 50 / 100, 120);
            }
            case 14: {
                return OfflineGunPhysics.p(n * 10 / 100, n2 * 10 / 100, 50);
            }
            case 15: {
                return OfflineGunPhysics.p(n * 10 / 100, n2 * 10 / 100, 50);
            }
            case 16: {
                return OfflineGunPhysics.p(0, 0, 100);
            }
            case 17: {
                return OfflineGunPhysics.p(n * 30 / 100, n2 * 30 / 100, 100);
            }
            case 19: {
                return OfflineGunPhysics.p(n * 20 / 100, n2 * 20 / 100, 50);
            }
            case 21: {
                return OfflineGunPhysics.p(n * 10 / 100, n2 * 10 / 100, 50);
            }
            case 22: {
                return OfflineGunPhysics.p(0, 0, 10);
            }
            case 23: {
                return OfflineGunPhysics.p(n * 20 / 100, n2 * 20 / 100, 100);
            }
            case 24: {
                return OfflineGunPhysics.p(0, 0, 50);
            }
            case 25: {
                return OfflineGunPhysics.p(0, 0, -50);
            }
            case 26: {
                return OfflineGunPhysics.p(n * 30 / 100, n2 * 30 / 100, 60);
            }
            case 27: {
                return OfflineGunPhysics.p(n * 15 / 100, n2 * 15 / 100, 60);
            }
            case 28: {
                return OfflineGunPhysics.p(0, 0, 20);
            }
            case 29: {
                return OfflineGunPhysics.p(n * 15 / 100, n2 * 15 / 100, 60);
            }
            case 30: {
                return OfflineGunPhysics.p(0, 0, 0);
            }
            case 33: {
                return OfflineGunPhysics.p(n * 80 / 100, n2 * 80 / 100, 40);
            }
            case 40: {
                return OfflineGunPhysics.p(n * 10 / 100, n2 * 10 / 100, 50);
            }
            case 49: {
                return OfflineGunPhysics.p(n * 40 / 100, n2 * 40 / 100, 70);
            }
            case 51: {
                return OfflineGunPhysics.p(n * 5 / 100, n2 * 5 / 100, 60);
            }
            case 52: {
                return OfflineGunPhysics.p(n * 10 / 100, n2 * 10 / 100, 100);
            }
            case 54: {
                return OfflineGunPhysics.p(0, 0, 80);
            }
            case 55: {
                return OfflineGunPhysics.p(n * 6 / 100, n2 * 6 / 100, 60);
            }
            case 56: {
                return OfflineGunPhysics.p(n * 70 / 100, n2 * 70 / 100, 70);
            }
            case 57: {
                return OfflineGunPhysics.p(0, 0, 120);
            }
            case 58: {
                return OfflineGunPhysics.p(0, 0, 100);
            }
        }
        return OfflineGunPhysics.p(n * 80 / 100, n2 * 80 / 100, 100);
    }

    public static int[] newState(int n, int n2, int n3, int n4) {
        return new int[]{n, n2, n3, n4, 0, 0, 0};
    }

    public static void muzzle(CPlayer cPlayer, int n, int n2, int[] nArray) {
        int n3 = 24;
        int n4 = 24;
        if (cPlayer instanceof Boss && cPlayer.gun == 13) {
            n3 = 42;
            n4 = 42;
        }
        nArray[0] = cPlayer.x + ((n3 - 4) * CRes.cos(n) >> 10);
        nArray[1] = cPlayer.y - n4 / 2 - ((n4 - 4) * CRes.sin(n) >> 10);
        nArray[2] = n2 * CRes.cos(n) >> 10;
        nArray[3] = -(n2 * CRes.sin(n) >> 10);
    }

    public static void muzzleForcePlus(CPlayer cPlayer, int n, int n2, int n3, int[] nArray) {
        int n4 = n2 + n3;
        OfflineGunPhysics.muzzle(cPlayer, n, n4, nArray);
    }

    public static void integrate(int[] nArray, int n, int n2, int n3) {
        int n4 = nArray[2];
        int n5 = nArray[3];
        int n6 = nArray[4];
        int n7 = nArray[5];
        int n8 = nArray[6];
        n7 += Math.abs(n2);
        n8 += n3;
        if (Math.abs(n6 += Math.abs(n)) >= 100) {
            n4 = n > 0 ? (n4 += n6 / 100) : (n4 -= n6 / 100);
            n6 %= 100;
        }
        if (Math.abs(n7) >= 100) {
            n5 = n2 > 0 ? (n5 += n7 / 100) : (n5 -= n7 / 100);
            n7 %= 100;
        }
        if (Math.abs(n8) >= 100) {
            n5 += n8 / 100;
            n8 %= 100;
        }
        nArray[2] = n4;
        nArray[3] = n5;
        nArray[4] = n6;
        nArray[5] = n7;
        nArray[6] = n8;
    }

    public static void tarzanTurn(int[] nArray, boolean bl, byte[] byArray, boolean[] blArray) {
        int n = nArray[3];
        int n2 = nArray[2];
        if (n > 0 && !blArray[0]) {
            blArray[0] = true;
        }
        if (byArray[0] == 0) {
            nArray[2] = n2 + (bl ? 1 : -1);
            byArray[0] = 1;
        } else if (byArray[0] == 1) {
            nArray[2] = n2 + (bl ? 2 : -2);
        } else if (blArray[0]) {
            byArray[0] = 0;
        }
    }

    private static Phys p(int n, int n2, int n3) {
        return new Phys(n, n2, n3);
    }

    public static final class Phys {
        public final int ax100;
        public final int ay100;
        public final int g100;

        Phys(int n, int n2, int n3) {
            this.ax100 = n;
            this.ay100 = n2;
            this.g100 = n3;
        }
    }
}

