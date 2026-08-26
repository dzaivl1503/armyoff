/*
 * Decompiled with CFR 0.152.
 */
package item;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineBulletAssets;
import coreLG.CCanvas;
import effect.Explosion;
import effect.Tornado;
import item.Bullet;
import java.util.Vector;
import map.MM;
import model.CRes;
import model.FrameImage;
import model.Position;
import network.GameService;
import player.CPlayer;
import player.PM;
import screen.GameScr;

public class BM {
    public static mImage airFighter = GameScr.airFighter;
    public Vector bullets = new Vector();
    public byte numShoot = 0;
    int x;
    int y;
    public static int angle;
    public static byte force;
    byte type;
    byte whoShot;
    byte delayBullCound = (byte)-1;
    byte delayBullType = (byte)-1;
    int nDelayBull = 1;
    int nLazerDelay = 0;
    int nMeteorDelay = 0;
    int timedelay = 0;
    boolean isEndDelayBull = true;
    public static boolean active;
    static boolean isActiveAirFly;
    static boolean isActiveBomBay;
    static boolean isActiveLazer;
    public static boolean isActiveTornado;
    static boolean isAciveExplore;
    static boolean isActiveEgg;
    static boolean isActiveMeteor;
    static boolean isActive4Missile;
    static boolean isActiveMissileRain;
    static boolean pendingTenLuaFragments;
    static byte pendingTenLuaWhoShot;
    public int nBull;
    static int nArray;
    public static int nBum;
    public static int[] bumX;
    public static int[] bumY;
    public static int[] bumX_Last;
    public static int[] bumY_Last;
    static final int bombangle = 0;
    public static int airPlaneStartVx;
    static final byte bombForce = 5;
    public static final int rangeActive = 165;
    public static final int airPlaneStartX = 400;
    public static final int airPlaneStartY = 320;
    public static int airPlaneX;
    public static int airPlaneY;
    int airPlaneVx;
    static int lazerX;
    static int lazerY;
    static int tonardoX;
    static int tonardoY;
    public static Vector vTornado;
    static int exploreX;
    static int exploreY;
    static byte exploreForce;
    static int exploreAngel;
    static int exploreVx;
    static int exploreVy;
    public static boolean allSendENDSHOOT;
    public static boolean shootNextStep;
    static int eggX;
    static int eggY;
    static int meteorX;
    static int meteorY;
    static int meteorDesX;
    static int meteorDesY;
    static int missileXS;
    static int missileYS;
    static int missileXD;
    static int missileYD;
    static int missileP;
    static int missleAngle;
    static int mRainX;
    static int mRainY;
    int nWasShoot;
    static int xChicken;
    static int yChicken;
    public static int nOrbit;
    public static boolean activeUFOLazer;
    public static Vector lazerPosition;
    public FrameImage[][] frameBulletSpecial = new FrameImage[58][2];
    public mImage bulletChicken;
    short[][] xPaint;
    short[][] yPaint;
    short[][] xHit;
    short[][] yHit;
    int bIndex = 0;
    public static byte force2;
    public static boolean isBombBalloon;
    public byte critical;
    private int idBullet;
    boolean isDouble = false;
    boolean endShoot;
    int activeTicks;
    private byte endTurnDelay;

    public BM() {
        active = false;
        this.nBull = 0;
        this.numShoot = 0;
    }

    private int getPaintSlotCount() {
        return this.xPaint != null ? this.xPaint.length : 0;
    }

    private boolean paintIndexValid(int n) {
        if (this.xPaint == null || this.yPaint == null) {
            return false;
        }
        if (n < 0 || n >= this.xPaint.length || n >= this.yPaint.length) {
            return false;
        }
        return this.xPaint[n] != null && this.yPaint[n] != null;
    }

    public void spawnTenLuaFragments(byte by) {
        for (int i = 1; i < 5 && this.paintIndexValid(i); ++i) {
            this.bullets.addElement(new Bullet(27, this.xPaint[i], this.yPaint[i], by, this.idBullet));
        }
    }

    private void clampBulletCountToPaintSlots() {
        int n = this.getPaintSlotCount();
        if (n <= 0) {
            this.nBull = 0;
            this.nDelayBull = 0;
            this.isEndDelayBull = true;
        } else if (this.nBull > n) {
            this.nBull = n;
        }
    }

    public void setBullType(byte by, byte by2, byte by3, short[][] sArray, short[][] sArray2, byte by4, byte by5, short[][] sArray3, short[][] sArray4, int n) {
        int n2;
        this.type = by3;
        this.xPaint = sArray;
        this.yPaint = sArray2;
        this.xHit = sArray3;
        this.yHit = sArray4;
        this.critical = by;
        this.idBullet = n;
        xChicken = this.x;
        yChicken = this.y;
        this.whoShot = by2;
        force2 = by5;
        if (Bullet.isDoubleBull(by3) && by5 > 0 && sArray != null && sArray.length > 0 && sArray[0] != null && (n2 = sArray[0].length) > 2) {
            int n3 = n2 - 2;
            if (force2 > n3) {
                force2 = (byte)Math.max(2, n3);
            }
            if (force2 < 2) {
                force2 = (byte)2;
            }
        }
        active = true;
        this.activeTicks = 0;
        this.numShoot = by4;
        if (this.numShoot == 2) {
            this.isDouble = true;
        }
        this.nWasShoot = 1;
        this.bIndex = 0;
        allSendENDSHOOT = false;
        shootNextStep = true;
        this.endShoot = false;
        this.endTurnDelay = 0;
        isBombBalloon = false;
        if (by3 == 43) {
            isBombBalloon = true;
        }
        nBum = 0;
        for (n2 = 0; n2 < nArray; ++n2) {
            BM.bumX[n2] = -1;
            BM.bumY[n2] = -1;
            BM.bumX_Last[n2] = -1;
            BM.bumY_Last[n2] = -1;
        }
    }

    private void createShootInfo() {
        this.bIndex = 0;
        this.isEndDelayBull = true;
        this.createBullet(this.type);
        boolean bl = true;
        int n = 0;
        switch (this.type) {
            case 0:
            case 32:
            case 40:
            case 41:
            case 48:
            case 49:
            case 58: {
                this.nBull = 1;
                break;
            }
            case 1: {
                this.delayBullType = (byte)5;
                this.isEndDelayBull = false;
                this.nBull = this.critical == 0 ? 2 : 6;
                break;
            }
            case 2: {
                n = 1;
                if (this.critical == 0) {
                    this.nBull = 3;
                }
                if (this.critical == 1) {
                    this.nBull = 6;
                }
            }
            default: {
                break;
            }
            case 4: {
                isActiveAirFly = false;
                isActiveBomBay = false;
                airPlaneX = 400;
                airPlaneY = airPlaneStartVx;
                this.nBull = 2;
                break;
            }
            case 5:
            case 36: {
                this.nBull = 1;
                break;
            }
            case 6: {
                n = 1;
                this.nBull = 3;
                break;
            }
            case 7:
            case 31: {
                this.nBull = 1;
                break;
            }
            case 8: {
                this.nBull = 1;
                break;
            }
            case 9: {
                this.nBull = 4;
                break;
            }
            case 10: {
                if (this.critical != 0 && this.critical != 1) break;
                this.delayBullType = (byte)3;
                this.isEndDelayBull = false;
                this.nBull = 3;
                break;
            }
            case 11: {
                this.delayBullType = (byte)5;
                this.isEndDelayBull = false;
                this.nBull = 5;
                break;
            }
            case 13: {
                this.nBull = 1;
                break;
            }
            case 14: {
                this.nBull = 2;
                break;
            }
            case 16: {
                this.nBull = 7;
                break;
            }
            case 17: {
                this.nBull = 1;
                break;
            }
            case 19: {
                this.nBull = 1;
                break;
            }
            case 21: {
                this.nBull = 1;
                break;
            }
            case 22: {
                this.nBull = 1;
                break;
            }
            case 23: {
                this.nBull = 8;
                break;
            }
            case 25: {
                this.nBull = 1;
                break;
            }
            case 26: {
                this.nBull = 5;
                break;
            }
            case 28: {
                this.nBull = 14;
                break;
            }
            case 30: {
                this.nBull = 1;
                break;
            }
            case 33: {
                this.delayBullType = (byte)5;
                this.isEndDelayBull = false;
                this.nBull = 5;
                break;
            }
            case 34: {
                this.nBull = 1;
            }
            case 35: {
                this.nBull = 1;
                break;
            }
            case 37: {
                this.nBull = 1;
                break;
            }
            case 42: {
                this.nBull = 1;
                break;
            }
            case 43: {
                this.delayBullType = (byte)11;
                this.isEndDelayBull = false;
                this.nBull = 10;
                break;
            }
            case 44: {
                this.delayBullType = (byte)3;
                this.isEndDelayBull = false;
                this.nBull = 15;
                break;
            }
            case 45: {
                this.nBull = 1;
                break;
            }
            case 47: {
                this.delayBullType = (byte)2;
                this.isEndDelayBull = false;
                this.nBull = 5;
                break;
            }
            case 50: {
                this.nBull = 1;
                break;
            }
            case 51: {
                this.nBull = 1;
                break;
            }
            case 52: {
                this.nBull = 1;
                break;
            }
            case 54: {
                this.nBull = 1;
                break;
            }
            case 55: {
                this.nBull = 1;
                break;
            }
            case 56: {
                this.nBull = 3;
                break;
            }
            case 57: {
                this.nBull = 1;
            }
        }
        if (bl && this.type != 43) {
            if (n >= 0 && n < this.bullets.size()) {
                GameScr.cam.setBulletMode((Bullet)this.bullets.elementAt(n));
            } else {
                this.abortBrokenShot();
                return;
            }
        }
        this.clampBulletCountToPaintSlots();
        this.nDelayBull = this.nBull;
        this.delayBullCound = this.delayBullType;
    }

    private void createBullet(byte by) {
        switch (by) {
            case 0: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                Bullet bullet = new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet);
                this.bullets.addElement(bullet);
                ++this.bIndex;
                break;
            }
            case 1: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                Bullet bullet = new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet);
                bullet.pelletIndex = this.bIndex++;
                this.bullets.addElement(bullet);
                break;
            }
            case 58: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet));
                ++this.bIndex;
                break;
            }
            case 2: {
                int n;
                int n2 = n = this.critical == 0 ? 3 : 6;
                if (!this.paintIndexValid(this.bIndex + n - 1)) {
                    return;
                }
                int n3 = 0;
                while (n3 < n) {
                    Bullet bullet = new Bullet(by, this.xPaint[n3 + this.bIndex], this.yPaint[n3 + this.bIndex], this.whoShot, this.idBullet);
                    bullet.pelletIndex = n3++;
                    this.bullets.addElement(bullet);
                }
                this.bIndex += n;
                break;
            }
            case 3: {
                if (!this.paintIndexValid(1)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[1], this.yPaint[1], this.whoShot));
                GameScr.isDarkEffect = false;
                break;
            }
            case 4: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 5:
            case 36: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 6: {
                for (int i = 0; i < 3; ++i) {
                    this.bullets.addElement(new Bullet(by, this.xPaint[i], this.yPaint[i], this.whoShot, this.idBullet));
                }
                return;
            }
            case 7:
            case 31: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 8: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 9: {
                if (!this.paintIndexValid(this.bIndex + 3)) {
                    return;
                }
                for (int i = 0; i < 4; ++i) {
                    this.bullets.addElement(new Bullet(by, this.xPaint[i + this.bIndex], this.yPaint[i + this.bIndex], this.whoShot, this.idBullet));
                }
                this.bIndex += 4;
                break;
            }
            case 10: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                Bullet bullet = new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet);
                bullet.pelletIndex = this.bIndex++;
                this.bullets.addElement(bullet);
                break;
            }
            case 11: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                CRes.out("ID BULLET= " + this.idBullet);
                Bullet bullet = new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet);
                bullet.pelletIndex = this.bIndex++;
                this.bullets.addElement(bullet);
                break;
            }
            case 12: {
                for (int i = 1; i <= 6; ++i) {
                    this.bullets.addElement(new Bullet(by, this.xPaint[i], this.yPaint[i], this.whoShot));
                }
                return;
            }
            case 13:
            case 17: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet));
                ++this.bIndex;
                break;
            }
            case 14:
            case 40: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 15: {
                short[] sArray = new short[]{(short)lazerX, (short)lazerX};
                short[] sArray2 = new short[]{(short)Math.max(0, lazerY - 300), (short)lazerY};
                this.bullets.addElement(new Bullet(by, sArray, sArray2, this.whoShot));
                break;
            }
            case 16: {
                this.bullets.addElement(new Bullet(16, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 18: {
                if (this.endShoot) break;
                for (int i = 0; i < 3; ++i) {
                    if (!this.paintIndexValid(this.bIndex)) {
                        return;
                    }
                    this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet));
                    ++this.bIndex;
                }
                break;
            }
            case 19: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet));
                ++this.bIndex;
                break;
            }
            case 20: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot));
                ++this.nBull;
                ++this.bIndex;
                break;
            }
            case 21: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet));
                ++this.bIndex;
                break;
            }
            case 22: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot));
                break;
            }
            case 23: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 24: {
                for (int i = 1; i <= 7; ++i) {
                    this.bullets.addElement(new Bullet(by, this.xPaint[i], this.yPaint[i], this.whoShot));
                }
                return;
            }
            case 25: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 26: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 27: {
                for (int i = 1; i <= 4; ++i) {
                    this.bullets.addElement(new Bullet(by, this.xPaint[i], this.yPaint[i], this.whoShot));
                }
                return;
            }
            case 28: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 29: {
                for (int i = 1; i <= 13; ++i) {
                    this.bullets.addElement(new Bullet(by, this.xPaint[i], this.yPaint[i], this.whoShot));
                }
                return;
            }
            case 30: {
                this.bullets.addElement(new Bullet(by, this.xPaint[0], this.yPaint[0], this.whoShot));
                break;
            }
            case 32:
            case 34:
            case 35:
            case 37:
            case 41:
            case 42:
            case 45:
            case 50:
            case 51:
            case 52:
            case 54:
            case 55:
            case 57: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot));
                ++this.bIndex;
                break;
            }
            case 33:
            case 44:
            case 47: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot));
                ++this.bIndex;
                break;
            }
            default: {
                break;
            }
            case 43: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot));
                ++this.bIndex;
                break;
            }
            case 48: {
                if (!this.paintIndexValid(this.bIndex) || this.xHit == null || this.yHit == null || this.bIndex >= this.xHit.length || this.bIndex >= this.yHit.length || this.xHit[this.bIndex] == null || this.yHit[this.bIndex] == null) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.xHit[this.bIndex], this.yHit[this.bIndex]));
                ++this.bIndex;
                break;
            }
            case 49: {
                if (!this.paintIndexValid(this.bIndex)) {
                    return;
                }
                this.bullets.addElement(new Bullet(by, this.xPaint[this.bIndex], this.yPaint[this.bIndex], this.whoShot, this.idBullet));
                ++this.bIndex;
                break;
            }
            case 56: {
                if (!this.paintIndexValid(this.bIndex + 2)) {
                    return;
                }
                for (int i = 0; i < 3; ++i) {
                    this.bullets.addElement(new Bullet(by, this.xPaint[i + this.bIndex], this.yPaint[i + this.bIndex], this.whoShot));
                }
                this.bIndex += 3;
            }
        }
    }

    public void prepareOfflineBulletFrames(int n) {
        this.idBullet = n;
        this.onInitSpecialBullet();
    }

    public void onInitSpecialBullet() {
        int n = this.idBullet;
        CPlayer cPlayer = PM.getCurPlayer();
        if (cPlayer != null) {
            this.idBullet = n = OfflineBulletAssets.resolveBulletFrame(cPlayer);
        }
        int n2 = n;
        this.initOfflineSpecialBullets(n2);
        this.frameBulletSpecial[7][0] = new FrameImage(Bullet.grenadeImg.image, 8);
        this.frameBulletSpecial[9][1] = new FrameImage(Bullet.chuoiImg2.image, 4);
        this.frameBulletSpecial[21][1] = new FrameImage(Bullet.boomerangBig.image, 4);
        this.frameBulletSpecial[2][0] = new FrameImage(PlayerEquip.bullets[2].image, 20, 20);
        this.frameBulletSpecial[2][1] = new FrameImage(PlayerEquip.bullets[2].image, 20, 20);
    }

    private void initOfflineSpecialBullets(int n) {
        if (PlayerEquip.bullets[2] != null && PlayerEquip.bullets[2].image != null && PlayerEquip.bullets[2].image.midpImage != null) {
            this.frameBulletSpecial[2][0] = new FrameImage(PlayerEquip.bullets[2].image, 20, 20);
            this.frameBulletSpecial[2][1] = new FrameImage(PlayerEquip.bullets[2].image, 20, 20);
        }
        if (PlayerEquip.bullets[3] != null && PlayerEquip.bullets[3].image != null && PlayerEquip.bullets[3].image.midpImage != null) {
            this.frameBulletSpecial[9][0] = BM.offlineRotatedFrame(PlayerEquip.bullets[3], n, 4);
        }
        if (PlayerEquip.bullets[6] != null && PlayerEquip.bullets[6].image != null) {
            mImage mImage2 = CCanvas.cutBulletFrame(PlayerEquip.bullets[6], n);
            this.bulletChicken = mImage2 != null ? mImage2 : PlayerEquip.bullets[6];
        }
        if (PlayerEquip.bullets[7] != null && PlayerEquip.bullets[7].image != null && PlayerEquip.bullets[7].image.midpImage != null) {
            this.frameBulletSpecial[21][0] = BM.offlineRotatedFrame(PlayerEquip.bullets[7], 0, 4);
        }
        if (PlayerEquip.bullets[8] != null && PlayerEquip.bullets[8].image != null && PlayerEquip.bullets[8].image.midpImage != null) {
            this.frameBulletSpecial[17][0] = BM.offlineRotatedFrame(PlayerEquip.bullets[8], n, 8);
        }
    }

    private static FrameImage offlineRotatedFrame(mImage mImage2, int n, int n2) {
        mImage mImage3 = CCanvas.cutBulletFrame(mImage2, n);
        Image image = CCanvas.rasterizeFrame(mImage3);
        if (image == null) {
            return null;
        }
        return new FrameImage(image, n2);
    }

    public void update() {
        try {
            Object object;
            int n;
            if (active) {
                ++this.activeTicks;
                if (this.activeTicks > 1500) {
                    this.abortBrokenShot();
                    return;
                }
            }
            for (n = 0; n < this.bullets.size(); ++n) {
                ((Bullet)this.bullets.elementAt(n)).fixedUpdate();
            }
            if (!this.isEndDelayBull) {
                if (!isBombBalloon) {
                    this.delayBullCound = (byte)(this.delayBullCound - 1);
                    if (this.delayBullCound == 0 && this.nDelayBull > 0) {
                        if (!this.paintIndexValid(this.bIndex)) {
                            this.isEndDelayBull = true;
                            this.nDelayBull = 0;
                        } else {
                            this.createBullet(this.type);
                            this.delayBullCound = this.delayBullType;
                            --this.nDelayBull;
                            if (this.nDelayBull <= 1) {
                                this.isEndDelayBull = true;
                            }
                        }
                    }
                } else {
                    object = PM.p[this.whoShot];
                    if (this.paintIndexValid(this.bIndex) && this.xPaint[this.bIndex][0] <= ((CPlayer)object).x + 10 && this.xPaint[this.bIndex][0] >= ((CPlayer)object).x - 10) {
                        this.createBullet(this.type);
                        --this.nDelayBull;
                        if (this.nDelayBull <= 1) {
                            this.isEndDelayBull = true;
                        }
                    }
                }
            } else if (this.bullets.size() == 0 && this.nBull <= 0 && this.numShoot > 0 && !allSendENDSHOOT) {
                this.createShootInfo();
                this.numShoot = (byte)(this.numShoot - 1);
                ++this.nWasShoot;
                if (this.nWasShoot == this.numShoot) {
                    this.nWasShoot = 0;
                }
            }
            if (isActiveAirFly) {
                if (!isActiveBomBay && (airPlaneX += airPlaneStartVx) >= this.x - 165) {
                    this.createBullet((byte)3);
                    GameScr.cam.setBulletMode((Bullet)this.bullets.elementAt(0));
                    isActiveBomBay = true;
                }
                if (airPlaneX >= MM.mapWidth) {
                    isActiveAirFly = false;
                }
            }
            if (isActiveLazer) {
                ++this.nLazerDelay;
                if (this.nLazerDelay == 10) {
                    GameScr.cam.setTargetPointMode(lazerX, 100);
                }
                if (this.nLazerDelay == 20) {
                    this.nLazerDelay = 0;
                    isActiveLazer = false;
                    CRes.out("======> CReate Bullet Lazer ");
                    this.createBullet((byte)15);
                    GameScr.cam.setLazerMode((Bullet)this.bullets.elementAt(0));
                }
            }
            if (vTornado.size() != 0) {
                isActiveTornado = true;
                for (n = 0; n < vTornado.size(); ++n) {
                    ((Tornado)vTornado.elementAt(n)).update();
                }
            } else {
                isActiveTornado = false;
            }
            if (isAciveExplore) {
                isAciveExplore = false;
                this.createBullet((byte)18);
                GameScr.cam.setBulletMode((Bullet)this.bullets.elementAt(0));
                ((Bullet)this.bullets.elementAt((int)0)).vx = exploreVx;
                ((Bullet)this.bullets.elementAt((int)0)).vy = exploreVy;
            }
            if (isActiveEgg) {
                isActiveEgg = false;
                this.createBullet((byte)20);
                if (eggX > -500 && eggY > -500) {
                    GameScr.cam.setBulletMode((Bullet)this.bullets.elementAt(this.bullets.size() - 1));
                }
            }
            if (isActiveMeteor) {
                ++this.nMeteorDelay;
                if (this.nMeteorDelay == 20) {
                    this.nMeteorDelay = 0;
                    isActiveMeteor = false;
                    this.createBullet((byte)24);
                    GameScr.cam.setMeteorMode((Bullet)this.bullets.elementAt(0));
                }
            }
            if (isActive4Missile) {
                isActive4Missile = false;
                this.createBullet((byte)27);
                GameScr.cam.setBulletMode((Bullet)this.bullets.elementAt(0));
            }
            if (pendingTenLuaFragments) {
                pendingTenLuaFragments = false;
                this.spawnTenLuaFragments(pendingTenLuaWhoShot);
            }
            if (isActiveMissileRain) {
                isActiveMissileRain = false;
                this.createBullet((byte)29);
                GameScr.cam.setMRainMode((Bullet)this.bullets.elementAt(0));
            }
            if (activeUFOLazer && CCanvas.gameTick % 3 == 0) {
                if (lazerPosition.size() != 0) {
                    object = (Position)lazerPosition.elementAt(0);
                    GameScr.sm.addLazer(((Position)object).xF, ((Position)object).yF, ((Position)object).xT, ((Position)object).yT, 1);
                    int n2 = ((Position)object).xT + CRes.random(-35, 35);
                    int n3 = ((Position)object).yT + CRes.random(-10, 10);
                    new Explosion(((Position)object).xT, ((Position)object).yT, 9);
                    new Explosion(n2, n3, 9);
                    new Explosion(n2 + CRes.random(-30, 30), n3 + CRes.random(-10, 10), 9);
                    lazerPosition.removeElement(object);
                } else {
                    activeUFOLazer = false;
                }
            }
            if (this.endTurnDelay > 0) {
                this.endTurnDelay = (byte)(this.endTurnDelay - 1);
                if (this.endTurnDelay == 0 && this.whoShot == GameScr.myIndex) {
                    allSendENDSHOOT = true;
                    shootNextStep = true;
                }
            }
        }
        catch (Exception exception) {
            exception.printStackTrace();
            this.abortBrokenShot();
        }
    }

    public void removeAll(int n, int n2, int n3, int n4, byte by) {
        for (int i = 0; i < this.bullets.size(); ++i) {
            Bullet bullet = (Bullet)this.bullets.elementAt(i);
            this.removeBullet(bullet, true, n, n2, n3, n4, by);
        }
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        for (n = 0; n < this.bullets.size(); ++n) {
            ((Bullet)this.bullets.elementAt(n)).paint(mGraphics2);
        }
        if (isActiveAirFly) {
            mGraphics2.drawImage(airFighter, airPlaneX, airPlaneY + CCanvas.gameTick % 3, mGraphics.VCENTER | mGraphics.HCENTER, false);
        }
        if (isActiveLazer && this.nLazerDelay > 5 && this.nLazerDelay < 20) {
            mGraphics2.setColor(16771821);
            mGraphics2.fillRect(lazerX - 2, -100, 4, lazerY + 100, false);
        }
        if (isActiveTornado) {
            for (n = 0; n < vTornado.size(); ++n) {
                ((Tornado)vTornado.elementAt(n)).paint(mGraphics2);
            }
        }
    }

    public boolean isHaveEgg() {
        for (int i = 0; i < this.bullets.size(); ++i) {
            if (((Bullet)this.bullets.elementAt((int)i)).type != 20) continue;
            return true;
        }
        return false;
    }

    public void removeBullet(Bullet bullet, boolean bl, int n, int n2, int n3, int n4, byte by) {
        this.bullets.removeElement(bullet);
        if (bullet.type == 45) {
            this.endShoot();
        } else {
            if (this.bullets.size() > 0) {
                Bullet bullet2 = (Bullet)this.bullets.elementAt(0);
                if (bullet2.type != 19 && bullet2.type != 43) {
                    if (bullet2.type == 29) {
                        GameScr.cam.setMRainMode(bullet2);
                    } else {
                        GameScr.cam.setBulletMode(bullet2);
                    }
                }
            }
            if (!Bullet.isFlagBull(bullet.type) || this.type == 14) {
                int n5 = ++nBum - 1;
                if (bl) {
                    BM.bumX[n5] = n;
                    BM.bumY[n5] = n2;
                    BM.bumX_Last[n5] = n3;
                    BM.bumY_Last[n5] = n4;
                } else {
                    BM.bumX[n5] = -1;
                    BM.bumY[n5] = -1;
                    BM.bumX_Last[n5] = -1;
                    BM.bumY_Last[n5] = -1;
                }
            }
            if (Bullet.isFlagBull(bullet.type)) {
                if (bl) {
                    this.x = n;
                    this.y = n2;
                } else {
                    this.nBull = 1;
                }
            }
            if (this.type == 19) {
                this.x = xChicken;
                this.y = yChicken;
            }
            --this.nBull;
            if (this.nBull == 0 && this.numShoot == 0) {
                if (by == GameScr.myIndex && bullet.type == 49) {
                    this.endTurnDelay = (byte)8;
                } else if (by == GameScr.myIndex) {
                    allSendENDSHOOT = true;
                    shootNextStep = true;
                }
                if (this.numShoot == 0) {
                    this.endShoot();
                }
                GameService.gI().check_cross((byte)nBum, bumX_Last, bumY_Last, by);
            }
        }
    }

    public static void removeTornado() {
        vTornado.removeAllElements();
    }

    public void tornadoTurnUpd() {
        if (isActiveTornado) {
            for (int i = 0; i < vTornado.size(); ++i) {
                Tornado tornado = (Tornado)vTornado.elementAt(i);
                --tornado.nturn;
            }
        }
    }

    public void endShoot() {
        active = false;
        this.activeTicks = 0;
        this.nBull = 0;
        this.numShoot = 0;
        this.endShoot = true;
        CPlayer.isStopFire = false;
        this.tornadoTurnUpd();
        CRes.out("END SHOOT");
        CCanvas.lockNotify = true;
        CCanvas.tNotify = 0;
        CPlayer.closeMirror = true;
    }

    private void abortBrokenShot() {
        this.bullets.removeAllElements();
        this.isEndDelayBull = true;
        this.nDelayBull = 0;
        this.endShoot();
        if (this.whoShot == GameScr.myIndex) {
            allSendENDSHOOT = true;
            shootNextStep = true;
        }
    }

    public void activeAirplane(int n, int n2) {
        isActiveAirFly = true;
        airPlaneX = n - 400;
        airPlaneY = n2 - 320;
        GameScr.cam.setTargetPointMode(n - 180, n2 - 320);
        GameScr.isDarkEffect = false;
    }

    public void activeLazer(int n, int n2) {
        isActiveLazer = true;
        lazerX = n;
        lazerY = n2;
        GameScr.cam.setTargetPointMode(n, n2);
    }

    public void activeTornado(int n, int n2) {
        vTornado.addElement(new Tornado(n, n2, 3));
        tonardoX = n;
        tonardoY = n2;
    }

    public void activeExplore(int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        isAciveExplore = true;
        exploreX = n3;
        exploreY = n4;
        exploreVx = 0;
        exploreVy = 0;
        exploreForce = (byte)n7;
        int n9 = this.x - n3;
        int n10 = this.y - n4;
        int n11 = CRes.angle(n9, n10);
        exploreForce = (byte)(n7 / 2);
        exploreAngel = 180 - (n8 + n11);
    }

    public void activeEgg(int n, int n2) {
        isActiveEgg = true;
        eggX = n;
        eggY = n2;
    }

    public void activeMortarBum(int n, int n2) {
        this.x = n;
        this.y = n2 - 500;
        this.createBullet((byte)12);
    }

    public void active4Missle(int n, int n2, int n3, int n4) {
        isActive4Missile = true;
        missileXS = n;
        missileYS = n2;
        missileXD = n3;
        missileYD = n4;
        missileP = angle < 90 && angle > -90 ? 1 : -1;
        missleAngle = missileP > 0 ? angle : 180 - angle;
    }

    public void activeMeteor(int n, int n2, int n3) {
        isActiveMeteor = true;
        meteorX = n;
        meteorY = -30;
        meteorDesX = n;
        meteorDesY = n2;
    }

    public void activeMissleRain(int n, int n2) {
        isActiveMissileRain = true;
        mRainX = n;
        mRainY = n2;
    }

    public void onClear() {
    }

    static {
        active = false;
        nArray = 20;
        nBum = 0;
        bumX = new int[nArray];
        bumY = new int[nArray];
        bumX_Last = new int[nArray];
        bumY_Last = new int[nArray];
        airPlaneStartVx = 20;
        vTornado = new Vector();
        allSendENDSHOOT = false;
        shootNextStep = true;
        lazerPosition = new Vector();
    }
}

