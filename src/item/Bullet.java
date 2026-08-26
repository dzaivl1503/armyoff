/*
 * Decompiled with CFR 0.152.
 */
package item;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import CLib.mSound;
import CLib.mSystem;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineBotUfo;
import com.teamobi.mobiarmy2.OfflineBulletAssets;
import com.teamobi.mobiarmy2.OfflineBulletSim;
import com.teamobi.mobiarmy2.OfflinePvpBot;
import coreLG.CCanvas;
import coreLG.CONFIG;
import effect.Camera;
import effect.Explosion;
import effect.Smoke;
import effect.Tornado;
import item.BM;
import java.util.Vector;
import map.CMap;
import map.MM;
import model.CRes;
import model.FilePack;
import model.FrameImage;
import model.IAction2;
import model.Position;
import player.CPlayer;
import player.PM;
import screen.GameScr;

public class Bullet {
    public Bullet(int by, short[] sArray, short[] sArray2, byte by2) {
        this((byte)by, sArray, sArray2, by2);
    }
    public Bullet(int by, short[] sArray, short[] sArray2, int by2) {
        this((byte)by, sArray, sArray2, (byte)by2);
    }
    public Bullet(int by, short[] sArray, short[] sArray2, byte by2, int n) {
        this((byte)by, sArray, sArray2, by2, n);
    }
    public Bullet(int by, short[] sArray, short[] sArray2, int by2, int n) {
        this((byte)by, sArray, sArray2, (byte)by2, n);
    }
    public Bullet(int by, short[] sArray, short[] sArray2, byte by2, short[] sArray3, short[] sArray4) {
        this((byte)by, sArray, sArray2, by2, sArray3, sArray4);
    }

    int x;
    int y;
    int xDestination;
    int yDestination;
    int x_Last;
    int y_Last;
    int _xStart;
    int _yStart;
    int x_1;
    int x_2;
    int y_1;
    int y_2;
    int _w;
    int _h;
    int vx;
    int vy;
    int x_hammer;
    int y_hammer;
    int g = 8;
    int g1 = 0;
    int wind1 = 0;
    int explDelay;
    int vyLazer;
    int lazerColor;
    int subVx = 0;
    int subVy = 0;
    int subBmrVx = 0;
    int mouseP = 0;
    int trans = 0;
    int xS;
    int yS;
    int xD;
    int yD;
    boolean isActiveMissile = false;
    boolean isActiveHammer = false;
    boolean isActiveEgg = false;
    boolean isActiveRain = false;
    byte whoShot;
    private int color;
    boolean rotate = false;
    boolean translate = false;
    mImage img = null;
    FrameImage frmImg = null;
    int curFrame = 0;
    int smokeDelay = 0;
    boolean smoke = false;
    boolean longSmoke = false;
    boolean fireSmoke = false;
    boolean blackSmoke = false;
    boolean changeFrame = false;
    boolean lazerBullet = false;
    boolean changeFrameLeft = false;
    boolean isLightBlink = false;
    boolean isWaterBum;
    boolean lazerShoot = false;
    boolean lazerStop = true;
    boolean tonardo = false;
    boolean beginTimeCount = false;
    boolean chickenBull = false;
    boolean chickenHair = false;
    public static int dXLaser;
    public static int dYLaser;
    public int angle;
    int force;
    public int wind;
    public static mImage missile;
    public static mImage stone;
    public static mImage electric;
    public static mImage bomb;
    public static mImage bomb_flag;
    public static mImage rangCuaImg;
    public static mImage grenadeImg;
    public static mImage webImg;
    public static mImage chuoiImg;
    public static mImage chuoiImg2;
    public static mImage rocket;
    public static mImage superShoot;
    public boolean isSuper;
    int sFrame = 0;
    int ts = 0;
    public static mImage rocket2;
    static mImage rocket3;
    static mImage smallStar;
    static mImage chicken;
    static mImage chicken2;
    static mImage egg;
    static mImage egg2;
    static mImage boomerang;
    static mImage mouse;
    static mImage meteor;
    static mImage hammer;
    static mImage hammer2;
    static mImage rainmissile;
    static mImage khoang;
    static mImage daodat;
    static mImage daodat2;
    static mImage saobang;
    static mImage chatlong;
    static mImage mortar;
    static mImage boomerangBig;
    public static mImage imgGun;
    static mImage medic;
    static mImage nuke;
    static mImage lazer;
    static mImage ak2;
    static mImage axit;
    static mImage cannon2;
    static mImage balloonBull;
    static mImage drabyFireballSheet;
    public static FilePack filePack;
    public byte type;
    public static final byte BULL_CANNON = 0;
    public static final byte BULL_AK = 1;
    public static final byte BULL_PROTON = 2;
    public static final byte BULL_BOMBAY = 3;
    public static final byte BULL_BOMB_FLAG = 4;
    public static final byte BULL_TELEPORT = 5;
    public static final byte BULL_RANG_CUA = 6;
    public static final byte BULL_GENERADE = 7;
    public static final byte BULL_WEB = 8;
    public static final byte BULL_CHUOI = 9;
    public static final byte BULL_ROCKET = 10;
    public static final byte BULL_MORTAR = 11;
    public static final byte BULL_TORNADO = 13;
    public static final byte BULL_TRAI_PHA = 12;
    public static final byte BULL_LAZER_FLAG = 14;
    public static final byte BULL_LAZER = 15;
    public static final byte BULL_TRAI_PHA_FLAG = 16;
    public static final byte BULL_EXPLORE_FLAG = 17;
    public static final byte BULL_EXPLORE = 18;
    public static final byte BULL_CHICKEN = 19;
    public static final byte BULL_EGG = 20;
    public static final byte BULL_BOOMERANG = 21;
    public static final byte BULL_MOUSE = 22;
    public static final byte BULL_METEOR_FLAG = 23;
    public static final byte BULL_METEOR = 24;
    public static final byte BULL_UNDERGROUND = 25;
    public static final byte BULL_4MISSILE = 26;
    public static final byte BULL_1MISSILE = 27;
    public static final byte BULL_MISSILE_RAIN_FLAG = 28;
    public static final byte BULL_MISSILE_RAIN = 29;
    public static final byte BULL_HOLE = 30;
    public static final byte BULL_BOMB_BOSS = 31;
    public static final byte BULL_BOMB_BOSS_SMALL = 32;
    public static final byte BULL_BOSS_MISSILE = 33;
    public static final byte BULL_MINI_BOMB = 34;
    public static final byte BULL_ROBOT_ATTACK = 35;
    public static final byte BULL_TELEPORT_2 = 36;
    public static final byte BULL_NUKE = 37;
    public static final byte BULL_4LAZER = 38;
    public static final byte BULL_1LAZER = 39;
    public static final byte BULL_LAZER2 = 40;
    public static final byte BULL_SPEACIAL = 41;
    public static final byte BULL_UFO_LAZER = 42;
    public static final byte BULL_10_BOMB = 43;
    public static final byte BULL_BALLOON_GUN = 44;
    public static final byte BULL_BALLOON_LAZER = 45;
    public static final byte BULL_AXIT = 47;
    public static final byte BULL_PINGPONG = 48;
    public static final byte BULL_SUICIDE = 50;
    public static final byte BULL_SMOKE = 51;
    public static final byte BULL_BIG_HOLE = 52;
    public static final byte BULL_UFO = 53;
    public static final byte BULL_FREEZE = 54;
    public static final byte BULL_POISION = 55;
    public static final byte BULL_WEB3 = 56;
    public static final byte BULL_TIME_BOMB = 57;
    public static final byte BULL_LAZER_GIRL = 49;
    public static byte[] BULLset_WIND_AFFECT;
    public static byte[] BULLset_WEIGHT;
    public short[] xPaint;
    public short[] yPaint;
    public int paintCount = 0;
    public Vector postion;
    public mImage dan = null;
    private int[] bounceFrameSwitchAt;
    private int bounceFrameIndex = 0;
    private int idBullet;
    int pelletIndex = 0;
    int xLG;
    int yLG;
    int n1 = 6;
    int n2 = 8;
    boolean isMirror;
    int tL;
    int xLaser1;
    int yLaser1;
    int xLaser2;
    int yLaser2;
    long timeDelayPaint;
    long offlineLaserStartMs;
    int lx;
    int ly;
    int pw = 1;
    int ph = 1;
    int px;
    int py;
    int nCheck;
    boolean changePositon = false;
    int dis = 0;
    int count = 0;
    int dis_ChPosition = 4;
    int dis_delay = 0;
    int x_3;
    int y_3;
    int x_4;
    int y_4;
    int timecount = 0;
    boolean isfall = false;
    int dym = 0;
    boolean beginUdgr = false;
    int bx;
    int missileTime = 0;
    int pingFrame;
    int pingColor;
    int t;
    int lastAngle = 0;
    boolean isPaintLazer;
    boolean pingChange = false;
    int mainFrame;
    int pos;
    public static int webId;
    public boolean notPaint = false;
    int angleRotate;
    Image tam;

    public Bullet(byte by, short[] sArray, short[] sArray2, byte by2) {
        this.init(by, sArray, sArray2, by2);
    }

    public Bullet(byte by, short[] sArray, short[] sArray2, byte by2, int n) {
        this.idBullet = n;
        this.init(by, sArray, sArray2, by2);
    }

    public Bullet(byte by, short[] sArray, short[] sArray2, byte by2, short[] sArray3, short[] sArray4) {
        this.init(by, sArray, sArray2, by2);
        this.getCollisionPingPong(sArray3, sArray4);
    }

    public void init(byte by, short[] sArray, short[] sArray2, byte by2) {
        this.type = by;
        this.whoShot = by2;
        this.xPaint = sArray;
        this.yPaint = sArray2;
        this.x = this.xPaint[0];
        this.y = this.yPaint[0];
        this.setInitialFlightDirection();
        this.dan = null;
        this.bounceFrameIndex = 0;
        this.bounceFrameSwitchAt = (int[])(by == 58 ? OfflineBulletSim.lastBounceIndices : null);
        this.initBulletType(by);
        if (by == 49) {
            int[] nArray = this.getXYMirror(sArray, sArray2);
            if (nArray != null) {
                this.xLG = nArray[0];
                this.yLG = nArray[1];
            }
            this.xLaser1 = 0;
            this.yLaser1 = 0;
            this.xLaser2 = 0;
            this.yLaser2 = 0;
            this.isMirror = false;
            this.isPaintLazer = false;
            this.tL = 0;
            this.offlineLaserStartMs = 0L;
            CPlayer.xM = this.xLG;
            CPlayer.yM = this.yLG;
            CPlayer.isMirror = true;
        }
        Camera.shaking = 0;
        this.getPlayerEffect();
        mSound.playSound(2, mSound.volumeSound, 1);
    }

    private void setInitialFlightDirection() {
        if (this.xPaint == null || this.yPaint == null || this.xPaint.length < 2) {
            this.angleRotate = 0;
            return;
        }
        for (int i = 1; i < this.xPaint.length; ++i) {
            int n = this.xPaint[i] - this.xPaint[0];
            int n2 = this.yPaint[i] - this.yPaint[0];
            if (n == 0 && n2 == 0) continue;
            this.angleRotate = CRes.angle(n, n2);
            return;
        }
        this.angleRotate = 0;
    }

    public void getCollisionPingPong(short[] sArray, short[] sArray2) {
        this.postion = new Vector();
        if (this.type == 48) {
            for (int i = 0; i < sArray.length; ++i) {
                this.postion.addElement(new Position(sArray[i], sArray2[i]));
            }
        }
    }

    public int[] getXYMirror(short[] sArray, short[] sArray2) {
        short s = Bullet.min(sArray2);
        for (int i = 0; i < sArray2.length; ++i) {
            if (sArray2[i] != s) continue;
            return new int[]{sArray[i], sArray2[i]};
        }
        return null;
    }

    public void getPlayerEffect() {
        if (this.type == 31 || this.type == 32 || this.type == 35) {
            this.explode(1);
            if (PM.getCurPlayer().gun != 15) {
                Camera.shaking = 2;
            }
        }
        this.checkBackBody();
        if (GameScr.bm.critical == 1) {
            if (this.type != 11 && this.type != 18) {
                new Explosion(this.xPaint[0], this.yPaint[0], 0);
            }
        } else if (this.type != 42 && this.type != 47) {
            GameScr.sm.addSmoke(this.xPaint[0], this.yPaint[0], (byte)0);
        }
        if (this.type == 45) {
            CCanvas.lockNotify = true;
            CCanvas.tNotify = 0;
        }
    }

    public void paintLazerGirl(mGraphics mGraphics2) {
        int n = GameScr.bm.critical == 1 ? 718162 : this.color;
        boolean bl = true;
        long l = mSystem.currentTimeMillis();
        long l2 = 20L;
        if (bl && l - this.timeDelayPaint < l2) {
            this.drawLazerGirlLines(mGraphics2, n);
            return;
        }
        this.timeDelayPaint = l;
        int n2 = Math.max(Math.abs(dXLaser), Math.abs(dYLaser));
        if (n2 < 6) {
            n2 = 6;
        }
        if (!this.isMirror) {
            int n3 = this.xLG - this.xPaint[0];
            int n4 = this.yLG - this.yPaint[0];
            int n5 = this.xLaser1;
            int n6 = this.yLaser1;
            this.xLaser1 = Bullet.advanceLaserComponent(n5, n6, n3, n4, n2, true);
            this.yLaser1 = Bullet.advanceLaserComponent(n5, n6, n3, n4, n2, false);
            GameScr.cam.setTargetPointMode(this.xPaint[0] + this.xLaser1, this.yPaint[0] + this.yLaser1);
            if (this.xLaser1 == n3 && this.yLaser1 == n4) {
                this.isMirror = true;
                this.tL = 0;
                this.xLaser2 = 0;
                this.yLaser2 = 0;
            }
        } else {
            GameScr.cam.setTargetPointMode(this.xLG + this.xLaser2, this.yLG - this.yLaser2);
            short s = this.xPaint[this.xPaint.length - 1];
            short s2 = this.yPaint[this.yPaint.length - 1];
            int n7 = s - this.xLG;
            int n8 = this.yLG - s2;
            int n9 = this.xLaser2;
            int n10 = this.yLaser2;
            this.xLaser2 = Bullet.advanceLaserComponent(n9, n10, n7, n8, n2, true);
            this.yLaser2 = Bullet.advanceLaserComponent(n9, n10, n7, n8, n2, false);
            if (this.xLaser2 == n7 && this.yLaser2 == n8) {
                this.x = this.xPaint[this.xPaint.length - 1];
                this.y = this.yPaint[this.yPaint.length - 1];
                this.explode(2);
                return;
            }
        }
        if (bl) {
            ++this.tL;
            if (this.tL > 500) {
                this.tL = 0;
                this.x = this.xPaint[this.xPaint.length - 1];
                this.y = this.yPaint[this.yPaint.length - 1];
                this.explode(2);
            }
        }
        this.drawLazerGirlLines(mGraphics2, n);
    }

    private static int advanceLaserComponent(int n, int n2, int n3, int n4, int n5, boolean bl) {
        int n6 = n3 - n;
        int n7 = n4 - n2;
        int n8 = Math.max(Math.abs(n6), Math.abs(n7));
        if (n8 <= n5) {
            return bl ? n3 : n4;
        }
        int n9 = n + n6 * n5 / n8;
        int n10 = n2 + n7 * n5 / n8;
        if (n9 == n && n6 != 0) {
            n9 += n6 > 0 ? 1 : -1;
        }
        if (n10 == n2 && n7 != 0) {
            n10 += n7 > 0 ? 1 : -1;
        }
        return bl ? n9 : n10;
    }

    private void drawLazerGirlLines(mGraphics mGraphics2, int n) {
        if (GameScr.curGRAPHIC_LEVEL != 2) {
            this.drawThickLaser(mGraphics2, this.xPaint[0], this.yPaint[0], this.xPaint[0] + this.xLaser1, this.yPaint[0] + this.yLaser1, n, false);
            this.drawThickLaser(mGraphics2, this.xLG, this.yLG, this.xLG + this.xLaser2, this.yLG - this.yLaser2, n, true);
        } else {
            mGraphics2.setColor(n);
            mGraphics2.drawLine(this.xPaint[0], this.yPaint[0], this.xPaint[0] + this.xLaser1, this.yPaint[0] + this.yLaser1, true);
            mGraphics2.setColor(n);
            mGraphics2.drawLine(this.xLG, this.yLG, this.xLG + this.xLaser2, this.yLG - this.yLaser2, true);
        }
    }

    private void drawThickLaser(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5, boolean bl) {
        int n6 = n3 - n;
        int n7 = n4 - n2;
        int n8 = Math.max(Math.abs(n6), Math.abs(n7));
        if (n8 == 0) {
            return;
        }
        for (int i = 0; i < this.n1; ++i) {
            int n9 = i - this.n1 / 2;
            int n10 = -n7 * n9 / n8;
            int n11 = n6 * n9 / n8;
            if (i < this.n1 / 2 + this.n1 / 4 && i > this.n1 / 2 - this.n1 / 4) {
                mGraphics2.setColor(0xFFFFFF);
            } else {
                mGraphics2.setColor(n5);
            }
            mGraphics2.drawLine(n + n10, n2 + n11, n3 + n10, n4 + n11, bl);
        }
    }

    public static short min(short[] sArray) {
        short s = sArray[0];
        for (int i = 1; i < sArray.length; ++i) {
            if (sArray[i] >= s) continue;
            s = sArray[i];
        }
        return s;
    }

    public void checkBackBody() {
        if (PM.getCurPlayer().gun == 13) {
            PM.getCurPlayer().xBugBack = -5;
            PM.getCurPlayer().yBugBack = 5;
            if (this.type == 36) {
                PM.getCurPlayer().frameleg_1 = 1;
                PM.getCurPlayer().xBug = this.xPaint[this.xPaint.length - 1];
                this.notPaint = true;
            }
        }
        if (PM.getCurPlayer().gun == 14 && this.type == 36) {
            PM.getCurPlayer().xBug = this.xPaint[this.xPaint.length - 1];
            PM.getCurPlayer().isFly = true;
            this.notPaint = true;
        }
    }

    public void checkFlyPlayer() {
        if (this.type == 36 && PM.getCurPlayer().isJump) {
            PM.getCurPlayer().x = this.x;
            PM.getCurPlayer().y = this.y;
        }
    }

    private boolean useOfflineBulletFrame(int n) {
        mImage mImage2;
        if (n < 0 || n >= PlayerEquip.bullets.length) {
            return false;
        }
        mImage mImage3 = PlayerEquip.bullets[n];
        if (mImage3 == null || mImage3.image == null) {
            return false;
        }
        int n2 = this.idBullet;
        n2 = OfflineBulletAssets.resolveBulletFrame(PM.getCurPlayer());
        if (n2 < 0) {
            n2 = 0;
        }
        if ((mImage2 = CCanvas.cutBulletFrame(mImage3, n2)) == null) {
            return false;
        }
        this.dan = mImage2;
        this.img = null;
        this.rotate = true;
        return true;
    }

    private void initBulletType(int n) {
        byte by = GameScr.bm.critical;
        switch (n) {
            case 0: {
                if (by == 0) {
                    if (this.useOfflineBulletFrame(0)) {
                        this.longSmoke = true;
                        this.isLightBlink = true;
                        this.frmImg = null;
                    } else {
                        this.img = PlayerEquip.bullets[0];
                        this.longSmoke = true;
                        this.isLightBlink = true;
                        this.frmImg = null;
                    }
                }
                if (by != 1) break;
                this.fireSmoke = true;
                this.frmImg = new FrameImage(Bullet.cannon2.image, 14, 14);
                this.changeFrame = true;
                this.img = null;
                break;
            }
            case 1: {
                if (this.useOfflineBulletFrame(1)) {
                    this.rotate = true;
                    this.smoke = true;
                    break;
                }
                CCanvas.cutImage(PlayerEquip.bullets[1], this.idBullet, new IAction2(){

                    public void perform(Object object) {
                        Bullet.this.dan = new mImage((Image)object);
                    }
                });
                this.rotate = true;
                this.smoke = true;
                break;
            }
            case 2: {
                if (by == 0) {
                    this.frmImg = GameScr.bm.frameBulletSpecial[2][0];
                    this.frmImg.nFrame = 2;
                    this.changeFrame = true;
                }
                if (by != 1) break;
                this.frmImg = GameScr.bm.frameBulletSpecial[2][1];
                this.frmImg.nFrame = 2;
                this.changeFrame = true;
                break;
            }
            case 3: {
                this.frmImg = new FrameImage(Bullet.bomb.image, 24, 24);
                this.changeFrame = true;
                this.blackSmoke = true;
                break;
            }
            case 4:
            case 16: {
                this.frmImg = new FrameImage(Bullet.bomb_flag.image, 32, 32);
                this.smoke = true;
                this.changeFrame = true;
                break;
            }
            case 5:
            case 36: {
                this.frmImg = new FrameImage(Bullet.bomb_flag.image, 32, 32);
                if (PM.getCurPlayer().isJump) break;
                this.smoke = true;
                break;
            }
            case 6: {
                this.frmImg = new FrameImage(PlayerEquip.bullets[5].image, 20, 20);
                this.frmImg.nFrame = 4;
                this.changeFrame = true;
                break;
            }
            case 7:
            case 31: {
                this.frmImg = GameScr.bm.frameBulletSpecial[7][0];
                this.checkFrameDirect();
                this.smoke = true;
                break;
            }
            case 8:
            case 56: {
                this.frmImg = new FrameImage(Bullet.webImg.image, 12, 12);
                this.checkFrameDirect();
                this.smoke = true;
                break;
            }
            case 9: {
                if (by == 0) {
                    this.frmImg = GameScr.bm.frameBulletSpecial[9][0];
                }
                if (by == 1) {
                    this.frmImg = GameScr.bm.frameBulletSpecial[9][1];
                }
                this.checkTranslate();
                this.changeFrame = true;
                break;
            }
            case 10: {
                if (by == 0) {
                    CCanvas.cutImage(PlayerEquip.bullets[4], this.idBullet, new IAction2(){

                        public void perform(Object object) {
                            Bullet.this.dan = new mImage((Image)object);
                            Bullet.this.rotate = true;
                        }
                    });
                }
                if (by != 1) break;
                CCanvas.cutImage(PlayerEquip.bullets[4], this.idBullet, new IAction2(){

                    public void perform(Object object) {
                        Bullet.this.dan = new mImage((Image)object);
                        Bullet.this.rotate = true;
                        Bullet.this.fireSmoke = true;
                    }
                });
                break;
            }
            case 11: {
                if (by == 0) {
                    this.frmImg = new FrameImage(PlayerEquip.bullets[5].image, 20, 20);
                    this.frmImg.nFrame = 4;
                    this.blackSmoke = true;
                    this.changeFrame = true;
                }
                if (by != 1) break;
                this.idBullet = 0;
                this.frmImg = new FrameImage(Bullet.mortar.image, 18, 18);
                this.smoke = true;
                this.changeFrame = true;
                break;
            }
            case 12: {
                this.frmImg = new FrameImage(PlayerEquip.bullets[5].image, 20, 20);
                this.frmImg.nFrame = 4;
                this.blackSmoke = true;
                this.changeFrame = true;
                break;
            }
            case 13: {
                this.tonardo = true;
                break;
            }
            case 14:
            case 40: {
                this.lazerBullet = true;
                break;
            }
            case 15: {
                this.frmImg = new FrameImage(Bullet.lazer.image, 19, 19);
                this.rotate = true;
                this.lazerShoot = true;
                this.lazerStop = false;
                break;
            }
            case 17:
            case 18: {
                this.frmImg = GameScr.bm.frameBulletSpecial[17][0];
                if (by == 1) {
                    this.fireSmoke = true;
                }
                this.changeFrame = true;
                this.translate = true;
                break;
            }
            case 19: {
                if (by == 0) {
                    this.dan = GameScr.bm.bulletChicken;
                }
                if (by == 1) {
                    this.frmImg = new FrameImage(Bullet.chicken2.image, 25, 25);
                    this.dan = chicken2;
                    this.chickenHair = true;
                    this.longSmoke = true;
                }
                this.chickenBull = true;
                this.rotate = true;
                break;
            }
            case 20: {
                if (by == 0) {
                    this.frmImg = new FrameImage(Bullet.egg.image, 12, 11);
                }
                if (by == 1) {
                    this.frmImg = new FrameImage(Bullet.egg2.image, 20, 20);
                }
                this.changeFrame = true;
                break;
            }
            case 21: {
                if (by == 0) {
                    this.frmImg = GameScr.bm.frameBulletSpecial[21][0];
                    this.changeFrame = true;
                }
                if (by != 1) break;
                this.frmImg = GameScr.bm.frameBulletSpecial[21][1];
                this.changeFrame = true;
                this.fireSmoke = true;
                break;
            }
            case 22: {
                this.frmImg = new FrameImage(Bullet.mouse.image, 25, 13);
                this.changeFrame = true;
                this.translate = true;
                break;
            }
            case 23: {
                this.frmImg = new FrameImage(Bullet.saobang.image, 15, 15);
                this.changeFrame = true;
                this.rotate = true;
                break;
            }
            case 24: {
                this.frmImg = new FrameImage(Bullet.meteor.image, 27, 28);
                this.changeFrame = true;
                break;
            }
            case 25: {
                this.frmImg = new FrameImage(Bullet.daodat.image, 16, 16);
                this.changeFrame = true;
                break;
            }
            case 26:
            case 27:
            case 33: {
                this.dan = new mImage(Bullet.rocket2.image);
                this.rotate = true;
                this.smoke = true;
                break;
            }
            case 28: {
                this.frmImg = new FrameImage(Bullet.rainmissile.image, 23, 23);
                this.changeFrame = false;
                break;
            }
            case 29: {
                this.dan = new mImage(Bullet.rocket2.image);
                this.rotate = true;
                break;
            }
            case 30: {
                this.frmImg = new FrameImage(Bullet.khoang.image, 32, 32);
                this.changeFrame = true;
                break;
            }
            case 32:
            case 41: {
                this.img = stone;
                this.smoke = true;
                this.isLightBlink = true;
                break;
            }
            case 34: {
                this.tonardo = true;
            }
            default: {
                break;
            }
            case 37: {
                this.frmImg = new FrameImage(Bullet.nuke.image, 18, 39);
                this.changeFrame = true;
                break;
            }
            case 43: {
                this.img = CPlayer.bomb;
                break;
            }
            case 44: {
                this.frmImg = new FrameImage(Bullet.lazer.image, 19, 19);
                this.rotate = true;
                break;
            }
            case 47: {
                this.frmImg = new FrameImage(Bullet.axit.image, 15, 15);
                this.rotate = true;
                break;
            }
            case 48: {
                this.frmImg = new FrameImage(Bullet.balloonBull.image, 14, 14);
                break;
            }
            case 49: {
                this.img = PlayerEquip.bullets[9];
                if (GameScr.curGRAPHIC_LEVEL == 0) {
                    this.longSmoke = true;
                }
                this.color = this.getColor(CCanvas.cutImage(this.img, this.idBullet));
                break;
            }
            case 51:
            case 53: {
                this.longSmoke = true;
                break;
            }
            case 52: {
                this.frmImg = new FrameImage(Bullet.daodat2.image, 24, 24);
                this.changeFrame = true;
                break;
            }
            case 54: {
                this.frmImg = new FrameImage(Bullet.saobang.image, 15, 15);
                this.changeFrame = true;
                this.rotate = true;
                break;
            }
            case 55: {
                this.frmImg = new FrameImage(Bullet.chatlong.image, 20, 20);
                this.changeFrame = true;
                this.img = null;
                break;
            }
            case 57: {
                this.tonardo = true;
                break;
            }
            case 58: {
                if (drabyFireballSheet == null || Bullet.drabyFireballSheet.image == null) {
                    drabyFireballSheet = mImage.createImage("/bullet10.png");
                }
                this.dan = CCanvas.cutBulletFrame(drabyFireballSheet, 0);
                this.img = null;
                this.rotate = true;
                this.fireSmoke = true;
            }
        }
        if (this.frmImg != null) {
            this._w = this.frmImg.frameWidth;
            this._h = this.frmImg.frameHeight;
        } else if (this.img != null) {
            this._w = this.img.image.getWidth();
            this._h = this.img.image.getHeight();
        } else {
            this._w = 2;
            this._h = 2;
        }
    }

    public int getColor(Image image) {
        try {
            int n = image.getWidth();
            int n2 = image.getHeight();
            int[] nArray = new int[n * n2];
            image.getRGB(nArray, 0, n, 0, 0, n, n2);
            return nArray[5];
        }
        catch (Exception exception) {
            return 0xFF2C2C;
        }
    }

    public static byte setBulletType(byte by) {
        switch (by) {
            case 0: {
                return 0;
            }
            case 1: {
                return 1;
            }
            case 2: {
                return 2;
            }
            case 3: {
                return 9;
            }
            case 4: {
                return 10;
            }
            case 5: {
                return 11;
            }
            case 6: {
                return 19;
            }
            case 7: {
                return 21;
            }
            case 8: {
                return 17;
            }
            case 9: {
                return 49;
            }
            case 10: {
                return 58;
            }
            default: {
                return 0;
            }
            case 14: {
                return 33;
            }
            case 15:
        }
        return 37;
    }

    public void checkTranslate() {
        PM pM = GameScr.pm;
        this.trans = this.x > PM.getCurPlayer().x ? 0 : 2;
    }

    public void checkFrameDirect() {
        PM pM = GameScr.pm;
        if (this.x > PM.getCurPlayer().x) {
            this.changeFrame = true;
        } else {
            this.changeFrameLeft = true;
        }
    }

    public void lazerBullUpdate(int n) {
        this.dis = this.changePositon ? (this.dis += this.dis_ChPosition) : (this.dis -= this.dis_ChPosition);
        this.dis_ChPosition = 2;
        this.x_1 = this.x + (CRes.cos(CRes.fixangle(BM.angle)) * this.dis >> 10);
        this.y_1 = this.y + -(CRes.sin(CRes.fixangle(BM.angle)) * this.dis >> 10);
        this.x_2 = this.x - (CRes.cos(CRes.fixangle(BM.angle)) * this.dis >> 10);
        this.y_2 = this.y - -(CRes.sin(CRes.fixangle(BM.angle)) * this.dis >> 10);
        GameScr.sm.addSmoke(this.x_1, this.y_1, (byte)(n == 0 ? 7 : 13));
        GameScr.sm.addSmoke(this.x_2, this.y_2, (byte)(n == 0 ? 7 : 13));
        if (n == 1) {
            this.x_3 = this.x + (CRes.cos(CRes.fixangle(BM.angle)) * this.dis >> 9);
            this.y_3 = this.y + -(CRes.sin(CRes.fixangle(BM.angle)) * this.dis >> 9);
            this.x_4 = this.x - (CRes.cos(CRes.fixangle(BM.angle)) * this.dis >> 9);
            this.y_4 = this.y - -(CRes.sin(CRes.fixangle(BM.angle)) * this.dis >> 9);
            GameScr.sm.addSmoke(this.x_3, this.y_3, (byte)7);
            GameScr.sm.addSmoke(this.x_4, this.y_4, (byte)7);
        }
        ++this.count;
        if (this.count == 3) {
            this.count = 0;
            this.changePositon = !this.changePositon;
        }
    }

    public boolean collisionTornado() {
        Vector vector = BM.vTornado;
        if (vector.size() >= 0) {
            for (int i = 0; i < vector.size(); ++i) {
                Tornado tornado = (Tornado)vector.elementAt(i);
                if (tornado.nturn < 0 || Math.abs(tornado.x + 10 - this.x) > 32) continue;
                return true;
            }
        }
        return false;
    }

    public void tornadoBullet() {
        GameScr.sm.addSmoke(this.x, this.y, (byte)1);
    }

    public boolean isSkyBullet() {
        return this.type == 3 || this.type == 15;
    }

    public void mouseUpdate() {
        if (this.isfall) {
            ++this.dym;
            this.y += this.dym / 2;
        }
        if (this.collisionTornado()) {
            this.y -= 4;
        }
        ++this.timecount;
        if (this.timecount < 3 * this.force) {
            if (!GameScr.mm.isLand(this.x, this.y - 5)) {
                this.x += this.mouseP;
                if (!(GameScr.mm.isLand(this.x - 8, this.y + 4) || GameScr.mm.isLand(this.x + 8, this.y + 4) || GameScr.mm.isLand(this.x, this.y + 4))) {
                    if (!this.collisionTornado()) {
                        this.isfall = true;
                    }
                } else {
                    --this.y;
                    this.dym = 0;
                    this.isfall = false;
                }
            }
        } else if (this.timecount == 3 * this.force) {
            this.explode(3);
        }
    }

    public void undergUpdate() {
        ++this.timecount;
        if (GameScr.mm.isLand(this.x, this.y)) {
            GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)2);
            GameScr.mm.makeHole(this.x, this.y, this.type, 1);
        }
        if (this.x < 0 || this.x > MM.mapWidth || this.y > MM.mapHeight || this.y < 100) {
            this.explode(4);
        }
    }

    public void missileRainUpdate() {
        if (this.isActiveRain) {
            ++this.timecount;
            if (this.timecount == 20) {
                this.timecount = 0;
                this.explode(5);
            }
        }
    }

    public void holeBulletUpdate() {
        this.y += 2;
        int n = CCanvas.gameTick % 3;
        if (GameScr.mm.isLand(this.x, this.y)) {
            GameScr.mm.makeHole(this.x, this.y - 5, this.type, 2);
            GameScr.sm.addRock(this.x, this.y, CRes.random(5), CRes.random(5), (byte)2);
            GameScr.sm.addRock(this.x, this.y, -CRes.random(5), CRes.random(5), (byte)2);
        }
        ++this.timecount;
        if (this.timecount == this.force * 2) {
            this.explode(6);
        }
    }

    public void nBullUpdate() {
        if (this.type == 26) {
            this.isActiveMissile = false;
            GameScr.bm.nBull = 1;
        }
        if (this.type == 17) {
            this.isActiveHammer = false;
            GameScr.bm.nBull = 1;
        }
    }

    public void fixedUpdate() {
        boolean bl;
        int n;
        Object object;
        int n2;
        int n3 = this.x;
        int n4 = this.y;
        this.x = this.xPaint[this.paintCount];
        this.y = this.yPaint[this.paintCount];
        if (this.whoShot < OfflinePvpBot.activeSquadCount && OfflineBotUfo.checkHit(this.x, this.y)) {
            this.explode(11);
            return;
        }
        if (this.type == 58 && this.bounceFrameSwitchAt != null && this.bounceFrameIndex < this.bounceFrameSwitchAt.length && this.paintCount >= this.bounceFrameSwitchAt[this.bounceFrameIndex]) {
            ++this.bounceFrameIndex;
            mImage mImage2 = CCanvas.cutBulletFrame(drabyFireballSheet, this.bounceFrameIndex);
            if (mImage2 != null) {
                this.dan = mImage2;
            }
        }
        if (this.type == 49) {
            int n5 = this.xPaint.length - 2;
            if (n5 < 1) {
                n5 = 1;
            }
            if (this.isPaintLazer) {
                if (this.offlineLaserStartMs == 0L) {
                    this.offlineLaserStartMs = mSystem.currentTimeMillis();
                } else if (mSystem.currentTimeMillis() - this.offlineLaserStartMs >= 2500L) {
                    this.x = this.xPaint[this.xPaint.length - 1];
                    this.y = this.yPaint[this.xPaint.length - 1];
                    this.explode(2);
                    return;
                }
                return;
            }
            if (dXLaser == 0 && dYLaser == 0) {
                if (this.paintCount >= this.xPaint.length - 1) {
                    this.explode(8);
                    return;
                }
            } else if (this.paintCount >= n5) {
                this.xLG = this.xPaint[n5];
                this.yLG = this.yPaint[n5];
                ++this.tL;
                if (this.tL >= 15) {
                    this.tL = 0;
                    this.isMirror = false;
                    this.xLaser1 = 0;
                    this.yLaser1 = 0;
                    this.xLaser2 = 0;
                    this.yLaser2 = 0;
                    this.isPaintLazer = true;
                    this.offlineLaserStartMs = mSystem.currentTimeMillis();
                }
                return;
            }
        }
        if (GameScr.curGRAPHIC_LEVEL != 2 && this.longSmoke) {
            GameScr.sm.addSmoke(this.x + CRes.random(-3, 3), this.y + CRes.random(-3, 3), (byte)19);
        }
        if (this.type == 36) {
            if (this.paintCount < 1) {
                this.paintCount = 1;
            }
            if (PM.getCurPlayer().isJump) {
                PM.getCurPlayer().x = this.xPaint[this.paintCount - 1];
                PM.getCurPlayer().y = this.yPaint[this.paintCount - 1];
            }
        }
        if (this.paintCount > this.xPaint.length - 1) {
            this.paintCount = this.xPaint.length - 1;
            if (this.type == 27) {
                new Explosion(this.x, this.y, 0);
            }
            this.x = this.xPaint[this.paintCount];
            this.y = this.yPaint[this.paintCount];
            if (this.type != 28) {
                this.explode(9);
            }
            this.nBullUpdate();
            if (this.type == 28) {
                this.isActiveRain = true;
                this.changeFrame = true;
            }
        }
        if (this.smoke) {
            this.createSmoke((byte)0);
        }
        if (this.blackSmoke) {
            this.createSmoke((byte)1);
        }
        if (this.fireSmoke) {
            int n6 = CRes.random(-7, 7);
            n2 = CRes.random(-7, 7);
            GameScr.sm.addSmoke(this.x + n6, this.y + n2, (byte)21);
        }
        if (this.x == CPlayer.xSuper && this.y == CPlayer.ySuper) {
            new Explosion(this.x, this.y, 0);
            this.isSuper = true;
        }
        if (this.type == 48 && this.pingFrame < this.postion.size()) {
            object = (Position)this.postion.elementAt(this.pingFrame);
            n2 = ((Position)object).x;
            n = ((Position)object).y;
            int n7 = Smoke.checkWay(n3, n4, this.x, this.y);
            int n8 = n7 != 0 && n7 != 1 ? 1 : 3;
            if (this.x == n2 && this.y == n) {
                this.getPingPongFrame(0, n8);
                ++this.pingFrame;
                this.pingColor += 4;
            }
        }
        if (this.pingChange) {
            this.curFrame = this.mainFrame + this.pos + this.pingColor;
            ++this.t;
            if (this.t == 3) {
                this.t = 0;
                this.pingChange = false;
                this.curFrame = this.pingColor;
            }
        }
        if (this.longSmoke && (Math.abs(this.x - n3) >= 10 || Math.abs(this.y - n4) >= 10)) {
            GameScr.sm.addSmoke((this.x + n3) / 2 + CRes.random(-3, 3), (this.y + n4) / 2 + CRes.random(-3, 3), (byte)19);
        }
        if (this.chickenHair && CCanvas.gameTick % 10 == 0) {
            GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)8);
        }
        if (this.fireSmoke) {
            int n9 = CRes.random(-7, 7);
            n2 = CRes.random(-7, 7);
            if (Math.abs(this.x - n3) >= 10 || Math.abs(this.y - n4) >= 10) {
                GameScr.sm.addSmoke((this.x + n3) / 2 + n9, (this.y + n4) / 2 + n2, (byte)21);
            }
        }
        if (Bullet.isDoubleBull(this.type)) {
            ++this.timecount;
            if (BM.force2 > 0 && !this.isActiveHammer && !this.isActiveEgg && this.isPhase2ReleaseFrame()) {
                if (this.type == 17) {
                    this.isActiveHammer = true;
                    if (PM.getCurPlayer() != null) {
                        this.xD = PM.getCurPlayer().x;
                        this.yD = PM.getCurPlayer().y;
                    }
                    object = GameScr.bm;
                    ((BM)object).nBull += 3;
                    return;
                }
                if (this.type == 19) {
                    this.isActiveEgg = true;
                    GameScr.bm.activeEgg(this.x, this.y);
                }
            }
        }
        if (this.lazerBullet) {
            if (this.type == 14) {
                this.lazerBullUpdate(0);
            }
            if (this.type == 40) {
                this.lazerBullUpdate(1);
            }
        }
        if (this.type == 25) {
            this.undergUpdate();
        }
        if (this.type == 52 && GameScr.mm.isLand(this.x, this.y)) {
            GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)2);
            new Explosion(this.x, this.y, 0);
            GameScr.mm.makeHole(this.x, this.y, this.type, 3);
        }
        if (this.type == 6 && GameScr.mm.isLand(this.x, this.y)) {
            GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)2);
            GameScr.mm.makeHole(this.x, this.y, this.type, 3);
        }
        if (this.type == 28) {
            this.missileRainUpdate();
        }
        if (this.type == 30) {
            this.holeBulletUpdate();
        }
        if (this.tonardo) {
            this.tornadoBullet();
        }
        if (this.rotate) {
            int n10 = this.x - n3;
            n = this.y - n4;
            if (n10 != 0 || n != 0) {
                this.lastAngle = this.angleRotate;
                int n11 = CRes.angle(n10, n);
                this.curFrame = (n11 << 1) / 45;
                this.angleRotate = n11;
            }
            if (this.type == 44 && this.curFrame > 7) {
                this.curFrame = 15 - this.curFrame;
            }
        }
        if ((this.type == 8 || this.type == 56) && this.x != n3) {
            this.changeFrame = this.x > n3;
            boolean bl2 = this.changeFrameLeft = this.x < n3;
        }
        if (this.translate) {
            this.checkTranslate();
        }
        if (this.changeFrame) {
            if (this.type == 37) {
                this.angle = CRes.angle(this.x - n3, this.y - n4);
                if (this.angle == -90 || this.angle == 270) {
                    this.trans = 0;
                    if (CCanvas.gameTick % 2 == 0) {
                        GameScr.sm.addSmoke(this.x, this.y + 15, (byte)15);
                    }
                }
                if (this.angle == 90) {
                    this.trans = 3;
                    if (CCanvas.gameTick % 2 == 0) {
                        GameScr.sm.addSmoke(this.x, this.y - 15, (byte)15);
                    }
                }
            }
            this.changeFrame(false);
        } else if (this.changeFrameLeft) {
            this.changeFrame(true);
        }
        if (this.type == 4 && GameScr.curGRAPHIC_LEVEL == 0) {
            GameScr.isDarkEffect = true;
        }
        if (this.type == 23 || this.type == 54) {
            GameScr.sm.addSmoke(this.x - CRes.random(5), this.y - CRes.random(5), (byte)11);
            GameScr.sm.addSmoke(this.x + CRes.random(5), this.y + CRes.random(5), (byte)11);
            GameScr.sm.addSmoke(this.x + CRes.random(5), this.y - CRes.random(5), (byte)11);
        }
        if (this.type == 15) {
            if (!this.lazerStop) {
                ++this.explDelay;
                if (this.explDelay == 26) {
                    this.x_Last = this.x;
                    this.y_Last = this.y;
                    if (GameScr.cam != null) {
                        GameScr.cam.setPlayerMode(GameScr.myIndex);
                    }
                    GameScr.bm.removeBullet(this, true, this.x, this.y, this.x_Last, this.y_Last, this.whoShot);
                    return;
                }
                if (this.explDelay == 5 || this.explDelay == 8) {
                    this.lazerShoot = true;
                }
            }
            if (this.lazerShoot) {
                this.lazerShoot = false;
                this.explode(10);
            }
        }
        if (MM.isHaveWaterOrGlass && !this.isWaterBum) {
            byte by = 0;
            if (this.type == 0) {
                by = 1;
            }
            if (MM.checkWaterBum(this.x, this.y, by)) {
                this.isWaterBum = true;
            }
        }
        int n12 = this.type != 19 ? 100 : 1500;
        boolean bl3 = bl = this.x >= -n12 && this.x <= MM.mapWidth + n12 && this.y <= MM.mapHeight + 100;
        if (this.type == 49 || bl) {
            if (this.paintCount < this.xPaint.length - 1) {
                ++this.paintCount;
            } else {
                if (this.type == 26) {
                    if (BM.nOrbit != 1) {
                        this.isActiveMissile = true;
                        GameScr.bm.nBull = 4;
                    } else {
                        GameScr.bm.nBull = 1;
                    }
                }
                if (!(this.type == 15 || this.type == 49 && this.isPaintLazer)) {
                    this.explode(11);
                }
            }
        } else {
            if (GameScr.isDarkEffect) {
                GameScr.isDarkEffect = false;
            }
            this.x_Last = this.x;
            this.y_Last = this.y;
            this.nBullUpdate();
            GameScr.bm.removeBullet(this, false, this.x, this.y, this.x_Last, this.y_Last, this.whoShot);
        }
    }

    public void getPingPongFrame(int n, int n2) {
        this.mainFrame = n;
        this.pos = n2;
        this.pingChange = true;
    }

    private int[] pelletHoleOffset(int n) {
        int n2;
        if (n <= 0) {
            return new int[]{0, 0};
        }
        int n3 = 0;
        int n4 = 16;
        if (this.xPaint != null && this.yPaint != null && this.paintCount > 0 && this.paintCount < this.xPaint.length) {
            n3 = this.xPaint[this.paintCount] - this.xPaint[this.paintCount - 1];
            n4 = this.yPaint[this.paintCount] - this.yPaint[this.paintCount - 1];
        }
        if ((n2 = CRes.sqrt(n3 * n3 + n4 * n4)) <= 0) {
            return new int[]{0, n * 16};
        }
        int n5 = 16;
        return new int[]{n3 * n5 * n / n2, n4 * n5 * n / n2};
    }

    public void explode(int n) {
        boolean bl;
        boolean bl2 = false;
        if (this.type != 6 && this.type != 5 && this.type != 22 && this.type != 30) {
            for (int pIdx = 0; pIdx < PM.p.length; ++pIdx) {
                if (PM.p[pIdx] == null) continue;
                this.px = PM.p[pIdx].x - 12;
                this.py = PM.p[pIdx].y - 24;
                if (!CRes.inRect(this.x, this.y, this.px - 5, this.py - 5, 34, 34)) continue;
                PM.p[pIdx].activeHurt(PM.p[pIdx].look == 0 ? 0 : 2);
                bl2 = true;
                break;
            }
        }
        if (this.type == 34) {
            new Explosion(this.x, this.y, 1);
        } else if (this.type == 4) {
            GameScr.bm.activeAirplane(this.x, this.y);
        } else if (this.type == 14) {
            GameScr.bm.activeLazer(this.x, this.y);
        } else if (this.type == 13) {
            GameScr.bm.activeTornado(this.x, this.y);
        } else if (this.type == 23) {
            GameScr.bm.activeMeteor(this.x, this.y, this.angle);
        } else if (this.type == 16) {
            new Explosion(this.x, this.y, 0);
            GameScr.bm.activeMortarBum(this.x, this.y);
        } else if (this.type == 17) {
            if (this.isActiveHammer) {
                GameScr.bm.activeExplore(this.xD, this.yD, this.x, this.y, this.vx, this.vy, (byte)this.force, this.angle);
            }
            new Explosion(this.x, this.y, 0);
            GameScr.mm.makeHole(this.x, this.y, this.type, 4);
        } else if (this.type == 26) {
            BM.pendingTenLuaFragments = true;
            BM.pendingTenLuaWhoShot = this.whoShot;
            new Explosion(this.x, this.y, 0);
            GameScr.mm.makeHole(this.x, this.y, this.type, 5);
        } else if (this.type == 28) {
            GameScr.bm.activeMissleRain(this.x, this.y);
            new Explosion(this.x, this.y, 0);
        } else if (this.type == 30) {
            CPlayer cPlayer = PM.getCurPlayer();
            GameScr.pm.updatePlayerXY(this.whoShot, (short)cPlayer.x, (short)cPlayer.y);
            new Explosion(this.x, this.y, 1);
        } else if (this.type != 5 && this.type != 36) {
            if (this.type != 8 && this.type != 56) {
                if (this.type == 57) {
                    new Explosion(this.x, this.y, 1);
                } else if (this.type != 53) {
                    if (this.type == 47) {
                        new Explosion(this.x, this.y, 10);
                        GameScr.mm.makeHole(this.x, this.y, this.type, 6);
                    } else {
                        Object object;
                        int n2;
                        boolean bl3 = bl = this.type == 10 || this.type == 11 || this.type == 1 || this.type == 2;
                        if (!bl2 && bl) {
                            int[] nArray = this.pelletHoleOffset(this.pelletIndex);
                            this.x += nArray[0];
                            this.y += nArray[1];
                        }
                        GameScr.mm.makeHole(this.x, this.y, this.type, 7, bl);
                        if (bl) {
                            int n3 = CMap.getHoleW(this.type);
                            int by = CMap.getHoleH(this.type);
                            n2 = Math.max(n3, by) / 2 + 3;
                            GameScr.mm.retraceRimAround(this.x, this.y, n2);
                        }
                        int n4 = 3;
                        if (this.type == 0 || this.type == 7 || this.type == 3 || this.type == 31 || this.type == 32) {
                            if (this.type == 31 || this.type == 32) {
                                PM.getCurPlayer().isPaint = false;
                            }
                            n4 = 2;
                        }
                        if (this.type == 31) {
                            new Explosion(this.x - 30, this.y + 5, 0);
                            new Explosion(this.x, this.y, 0);
                            new Explosion(this.x + 30, this.y + 5, 0);
                            GameScr.sm.addSmoke(this.x - 30, this.y + 5, (byte)4);
                            GameScr.sm.addSmoke(this.x, this.y, (byte)4);
                            GameScr.sm.addSmoke(this.x + 30, this.y + 5, (byte)4);
                            GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)3);
                            GameScr.sm.addRock(this.x, this.y, CRes.random(3 + this.force / 3), CRes.random(-8 - this.force / 3, -4 - this.force / 3), (byte)3);
                            GameScr.sm.addRock(this.x, this.y, CRes.random(2 + this.force / 3), CRes.random(-8 - this.force / 3, -4 - this.force / 3), (byte)3);
                        }
                        boolean bl4 = false;
                        for (n2 = 0; n2 < 2; ++n2) {
                            byte by = (byte)(CRes.r.nextInt(n4) < 1 ? 3 : 2);
                            if (this.type == 19) {
                                by = 8;
                            }
                            GameScr.sm.addRock(this.x, this.y, Math.abs(CRes.random(4 + this.force / 3)), -CRes.random(8 + this.force / 3, 10 + this.force / 3), by);
                            GameScr.sm.addRock(this.x, this.y, -Math.abs(CRes.random(4 + this.force / 3)), -CRes.random(8 + this.force / 3, 10 + this.force / 3), by);
                        }
                        if (this.type == 3) {
                            new Explosion(this.x - 30, this.y + 5, 0);
                            new Explosion(this.x, this.y, 0);
                            new Explosion(this.x + 30, this.y + 5, 0);
                            GameScr.sm.addSmoke(this.x - 30, this.y + 5, (byte)4);
                            GameScr.sm.addSmoke(this.x, this.y, (byte)4);
                            GameScr.sm.addSmoke(this.x + 30, this.y + 5, (byte)4);
                        } else if (this.type == 15) {
                            new Explosion(this.x, this.y + 20, 0);
                            GameScr.sm.addSmoke(this.x, this.y, (byte)4);
                        } else if (this.type != 13) {
                            if (this.type == 19) {
                                GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)10);
                            } else if (this.type == 22) {
                                new Explosion(this.x - 30, this.y + 5, 0);
                                new Explosion(this.x, this.y, 0);
                                new Explosion(this.x + 30, this.y + 5, 0);
                                GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)9);
                            } else if (this.type == 35) {
                                if (PM.getCurPlayer().gun != 15) {
                                    new Explosion(this.x - 15, this.y + 15, 0);
                                    new Explosion(this.x + 15, this.y + 15, 0);
                                }
                            } else if (this.type == 37) {
                                GameScr.whiteEffect = true;
                                GameScr.xNuke = this.x;
                                GameScr.yNuke = this.y;
                            } else if (this.type == 40) {
                                GameScr.electricEffect = true;
                                GameScr.xElectric = this.x;
                                GameScr.yElectric = this.y;
                            } else if (this.type == 54) {
                                GameScr.freezeEffect = true;
                                GameScr.xFreeze = this.x;
                                GameScr.yFreeze = this.y;
                            } else if (this.type == 50) {
                                GameScr.suicideEffect = true;
                                GameScr.xSuicide = this.x;
                                GameScr.ySuicide = this.y;
                                Camera.shaking = 2;
                            } else if (this.type == 55) {
                                GameScr.poisonEffect = true;
                                GameScr.xPoison = this.x;
                                GameScr.yPoison = this.y;
                                new Explosion(this.x, this.y, 15);
                            } else if (this.type == 54) {
                                new Explosion(this.x, this.y, 14);
                            } else if (this.type == 42) {
                                for (n2 = 0; n2 < PM.p.length; ++n2) {
                                    object = PM.p[n2];
                                    if (object == null || ((CPlayer)object).gun != 16 || ((CPlayer)object).getState() == 5) continue;
                                    BM.lazerPosition.addElement(new Position(((CPlayer)object).x, ((CPlayer)object).y, this.x, this.y));
                                }
                                BM.activeUFOLazer = true;
                            } else if (this.type == 45) {
                                GameScr.sm.addLazer(PM.p[this.whoShot].x, PM.p[this.whoShot].y, this.x, this.y, 0);
                                GameScr.electricEffect = true;
                                GameScr.xElectric = this.x;
                                GameScr.yElectric = this.y;
                            } else if (this.type == 43) {
                                new Explosion(this.x, this.y, 7);
                            } else if (this.type == 44) {
                                new Explosion(this.x, this.y, 9);
                            } else if (this.type == 48) {
                                new Explosion(this.x, this.y, 7);
                            } else if (this.type == 51) {
                                new Explosion(this.x, this.y, 12);
                            } else if (this.type != 53) {
                                if (GameScr.bm.critical == 0) {
                                    if (this.type == 49) {
                                        new Explosion(this.x, this.y, 9);
                                    } else {
                                        new Explosion(this.x, this.y, 0);
                                        GameScr.sm.addSmoke(this.x, this.y, (byte)4);
                                    }
                                } else {
                                    if (this.type == 0) {
                                        new Explosion(this.x, this.y, 0);
                                        new Explosion(this.x - 35, this.y, 0);
                                        new Explosion(this.x + 35, this.y, 0);
                                    } else if (this.type == 10) {
                                        new Explosion(this.x, this.y, 7);
                                    } else if (this.type == 49) {
                                        for (n2 = 0; n2 < 3; ++n2) {
                                            BM.lazerPosition.addElement(new Position(CRes.random(Camera.x, CCanvas.width + Camera.x), 0, this.x, this.y));
                                        }
                                        BM.activeUFOLazer = true;
                                        new Explosion(this.x, this.y, 0);
                                    } else if (this.type != 20 && this.type != 9) {
                                        new Explosion(this.x, this.y, 0);
                                        GameScr.sm.addSmoke(this.x, this.y, (byte)4);
                                    } else {
                                        new Explosion(this.x, this.y, 7);
                                    }
                                    Camera.shaking = 2;
                                }
                            }
                        }
                        if (BM.isActiveTornado) {
                            object = BM.vTornado;
                            for (int i = 0; i < ((Vector)object).size(); ++i) {
                                Tornado tornado = (Tornado)((Vector)object).elementAt(i);
                                if (this.x >= tornado.x + 10) continue;
                                int n3 = tornado.x;
                            }
                        }
                        mSound.playSound(0, mSound.volumeSound, 2);
                    }
                }
            } else {
                CMap cMap = new CMap(webId++, this.x - 21, this.y - 20, CMap.MANGNHEN, true);
                cMap.isSilkCollision = true;
                cMap.index = MM.maps.size();
                GameScr.mm.addMap(cMap);
                new Explosion(this.x, this.y, 1);
            }
        } else {
            boolean bl5 = bl = GameScr.mm != null && GameScr.mm.isLand(this.x, this.y);
            if (bl) {
                GameScr.pm.updatePlayerXY(this.whoShot, (short)this.x, (short)this.y);
                PM.p[this.whoShot].falling = true;
                PM.p[this.whoShot].isActiveFall = true;
                PM.p[this.whoShot].active = false;
            }
            if (bl && !PM.getCurPlayer().isJump) {
                new Explosion(this.x, this.y, 1);
            } else if (bl) {
                if (PM.getCurPlayer().gun == 13) {
                    PM.getCurPlayer().frameleg_1 = 0;
                    PM.getCurPlayer().yBugBack = 8;
                    GameScr.sm.addRock(this.x, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)3);
                    GameScr.sm.addRock(this.x, this.y, CRes.random(3 + this.force / 3), CRes.random(-8 - this.force / 3, -4 - this.force / 3), (byte)3);
                    GameScr.sm.addRock(this.x, this.y, CRes.random(2 + this.force / 3), CRes.random(-8 - this.force / 3, -4 - this.force / 3), (byte)3);
                }
                if (PM.getCurPlayer().gun == 14) {
                    PM.getCurPlayer().isBum = false;
                    PM.getCurPlayer().isFly = false;
                }
            }
        }
        if (this.type != 15) {
            this.x_Last = this.x;
            this.y_Last = this.y;
            GameScr.bm.removeBullet(this, true, this.x, this.y, this.x_Last, this.y_Last, this.whoShot);
        }
        if (CPlayer.isGetPosition) {
            PM.getXYResult();
            CPlayer.isGetPosition = false;
        }
    }

    public static boolean isFlagBull(int n) {
        return n == 4 || n == 14 || n == 16 || n == 23 || n == 28;
    }

    public static boolean isDoubleBull(int n) {
        return n == 17 || n == 19;
    }

    private boolean isPhase2ReleaseFrame() {
        byte by = BM.force2;
        if (by < 2) {
            return false;
        }
        if (this.timecount == by) {
            return true;
        }
        if (this.paintCount + 1 == by) {
            return true;
        }
        return this.xPaint != null && this.paintCount > 0 && this.paintCount == Math.min(by - 1, this.xPaint.length - 2);
    }

    public void changeFrame(boolean bl) {
        if (!bl) {
            if (this.type == 3) {
                this.curFrame = GameScr.tickCount / 2 % 2;
            } else if (this.type == 4) {
                this.curFrame = GameScr.tickCount / 2 % 2;
            } else if (this.type == 20) {
                this.curFrame = GameScr.tickCount / 2 % 2;
            } else if (this.type == 55) {
                if (CCanvas.gameTick % 2 == 0) {
                    ++this.curFrame;
                }
            } else {
                ++this.curFrame;
            }
            if (this.curFrame == this.frmImg.nFrame) {
                this.curFrame = 0;
            }
        } else {
            this.curFrame = this.curFrame < 0 ? this.frmImg.nFrame - 1 : (this.type == 4 ? GameScr.tickCount / 2 % 2 : --this.curFrame);
        }
    }

    public void createSmoke(byte by) {
        ++this.smokeDelay;
        if (this.smokeDelay > 3) {
            GameScr.sm.addSmoke(this.x, this.y, by);
            this.smokeDelay = 0;
        }
    }

    public void paintLazer(mGraphics mGraphics2) {
        int n = 20;
        int n2 = -500;
        int n3 = this.x - 10;
        if (this.explDelay < 15) {
            mGraphics2.setColor(16771821);
            mGraphics2.fillRect(n3, n2, 20, this.y + n - n2, false);
            mGraphics2.setColor(16756407);
            mGraphics2.fillRect(n3 + 2, n2, 16, this.y + n - n2, false);
            mGraphics2.setColor(16737907);
            mGraphics2.fillRect(n3 + 5, n2, 10, this.y + n - n2, false);
            mGraphics2.setColor(15745615);
            mGraphics2.fillRect(n3 + 8, n2, 4, this.y + n - n2, false);
        }
        if (this.explDelay >= 15 && this.explDelay < 17) {
            mGraphics2.setColor(16771821);
            mGraphics2.fillRect(n3 + 3, n2, 14, this.y + n - n2, false);
            mGraphics2.setColor(16737907);
            mGraphics2.fillRect(n3 + 5, n2, 10, this.y + n - n2, false);
            mGraphics2.setColor(15745615);
            mGraphics2.fillRect(n3 + 8, n2, 4, this.y + n - n2, false);
        }
        if (this.explDelay >= 17 && this.explDelay < 20) {
            mGraphics2.setColor(16771821);
            mGraphics2.fillRect(n3 + 6, n2, 8, this.y + n - n2, false);
            mGraphics2.setColor(15745615);
            mGraphics2.fillRect(n3 + 8, n2, 4, this.y + n - n2, false);
        }
        if (this.explDelay >= 20 && this.explDelay < 23) {
            mGraphics2.setColor(16771821);
            mGraphics2.fillRect(n3 + 8, n2, 4, this.y + n - n2, false);
        }
    }

    public void paint(mGraphics mGraphics2) {
        if (mGraphics2 == null || this.notPaint || this.type == 31) {
            return;
        }
        if (this.isSuper) {
            ++this.sFrame;
            if (this.sFrame == 5) {
                this.sFrame = 0;
            }
            mGraphics2.drawRegion(superShoot, 0, 32 * this.sFrame, 32, 32, 0, this.x, this.y, 3, false);
        }
        if (this.dan != null) {
            CCanvas.rotateImage(this.dan, this.angleRotate, mGraphics2, this.x, this.y, true);
        } else if (this.frmImg != null) {
            if (this.type != 2 && this.type != 11) {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y, this.trans, 3, mGraphics2, false);
            } else if (this.type == 2) {
                this.frmImg.drawRegionFrame(this.curFrame, this.x, this.y, 0, 3, mGraphics2, this.idBullet, 2);
            } else if (this.type == 11) {
                this.frmImg.drawRegionFrame(this.curFrame, this.x, this.y, 0, 3, mGraphics2, this.idBullet, 4);
            }
        } else if (this.img != null) {
            if ((this.type < 0 || this.type > 8) && this.type != 49) {
                mGraphics2.drawImage(this.img, this.x, this.y, mGraphics.VCENTER | mGraphics.HCENTER, false);
            } else {
                int imgW = this.img.image != null ? this.img.image.getWidth() : 16;
                mGraphics2.drawRegion(this.img, 0, this.idBullet * imgW, imgW, imgW, 0, this.x, this.y, mGraphics.VCENTER | mGraphics.HCENTER, false);
            }
        } else if (this.type == 14 || this.type == 15 || this.type == 40 || !this.lazerStop) {
            this.paintLazer(mGraphics2);
        } else {
            mGraphics2.setColor(0xFF2200);
            mGraphics2.fillRect(this.x - 3, this.y - 3, 6, 6, false);
            mGraphics2.setColor(0xFFFF00);
            mGraphics2.fillRect(this.x - 2, this.y - 2, 4, 4, false);
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillRect(this.x - 1, this.y - 1, 2, 2, false);
        }
        if (this.type == 49 && this.isPaintLazer) {
            this.paintLazerGirl(mGraphics2);
        }
    }

    public static int[] getCollisionPoint(int n, int n2, int n3, int n4) {
        int n5 = n3 - n;
        int n6 = n4 - n2;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        if (n5 < 0) {
            n8 = -1;
            n10 = -1;
        } else if (n5 > 0) {
            n8 = 1;
            n10 = 1;
        }
        if (n6 < 0) {
            n9 = -1;
        } else if (n6 > 0) {
            n9 = 1;
        }
        int n11 = Math.abs(n5);
        int n12 = Math.abs(n6);
        if (n11 <= n12) {
            n11 = Math.abs(n6);
            n12 = Math.abs(n5);
            if (n6 < 0) {
                n7 = -1;
            } else if (n6 > 0) {
                n7 = 1;
            }
            n8 = 0;
        }
        int n13 = n11 >> 1;
        for (int i = 0; i <= n11; ++i) {
            if (GameScr.mm.isLand(n, n2)) {
                return new int[]{n, n2};
            }
            if ((n13 += n12) >= n11) {
                n13 -= n11;
                n += n10;
                n2 += n9;
                continue;
            }
            n += n8;
            n2 += n7;
        }
        return null;
    }

    public void onClearMap() {
    }

    static {
        try {
            filePack = new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_ITEM + "item"));
            bomb = filePack.loadImage("bomb.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "bomb");
                }
            });
            bomb_flag = filePack.loadImage("specialbullet.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "specialbullet");
                }
            });
            filePack.loadImage("grenade.png", new IAction2(){

                public void perform(Object object) {
                    grenadeImg = new mImage((Image)object);
                    CRes.onSaveToFile((Image)object, "grenade");
                }
            });
            webImg = filePack.loadImage("web.png");
            filePack.loadImage("chuoi2.png", new IAction2(){

                public void perform(Object object) {
                    chuoiImg2 = new mImage((Image)object);
                    CRes.onSaveToFile((Image)object, "chuoi2");
                }
            });
            chicken2 = filePack.loadImage("gaBull2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "gaBull2");
                }
            });
            rocket2 = filePack.loadImage("rocket2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "rocket2");
                }
            });
            egg = filePack.loadImage("trungvang.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "trungvang");
                }
            });
            egg2 = filePack.loadImage("trung2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "trung2");
                }
            });
            mouse = filePack.loadImage("chuotBull.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "chuotBull");
                }
            });
            meteor = filePack.loadImage("thienthach.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "thienthach");
                }
            });
            mortar = filePack.loadImage("dan.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "dan");
                }
            });
            rainmissile = filePack.loadImage("13Missile.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "13Missile");
                }
            });
            khoang = filePack.loadImage("khoang.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "khoang");
                }
            });
            daodat = filePack.loadImage("daodat.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "daodat");
                }
            });
            daodat2 = filePack.loadImage("daodat2.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "daodat2");
                }
            });
            saobang = filePack.loadImage("saobang.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "saobang");
                }
            });
            imgGun = filePack.loadImage("gunIcon.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "gunIcon");
                }
            });
            nuke = filePack.loadImage("bigMissile.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "bigMissile");
                }
            });
            lazer = filePack.loadImage("laser.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "laser");
                }
            });
            cannon2 = filePack.loadImage("cannon.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "cannon");
                }
            });
            chatlong = filePack.loadImage("chatlong.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "chatlong");
                }
            });
            filePack.loadImage("boomerang-big.png", new IAction2(){

                public void perform(Object object) {
                    boomerangBig = new mImage((Image)object);
                    CRes.onSaveToFile((Image)object, "boomerang-big");
                }
            });
            superShoot = mImage.createImage("/effect/no.png");
            axit = mImage.createImage("/item/axit.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        filePack = null;
        BULLset_WIND_AFFECT = null;
        BULLset_WEIGHT = null;
        webId = 200;
    }
}

