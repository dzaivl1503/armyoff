/*
 * Decompiled with CFR 0.152.
 */
package player;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import Equipment.Equip;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineBulletSim;
import com.teamobi.mobiarmy2.OfflineCombat;
import coreLG.CCanvas;
import effect.Camera;
import effect.Explosion;
import effect.Smoke;
import item.BM;
import item.Bullet;
import map.Background;
import map.MM;
import model.CRes;
import model.FilePack;
import model.Font;
import model.FrameImage;
import model.Position;
import network.GameService;
import network.Session_ME;
import player.Boss;
import player.PM;
import screen.CScreen;
import screen.GameScr;
import screen.PrepareScr;
import shop.ShopItem;

public class CPlayer {
    public static final boolean TEAM_GREEN = true;
    public static final boolean TEAM_YELLOW = false;
    public boolean team;
    boolean isWaterBum;
    public static final byte NUM_GUN = 11;
    public static final byte GUN_CANNON = 0;
    public static final byte GUN_AK = 1;
    public static final byte GUN_PROTON = 2;
    public static final byte GUN_CHUOI = 3;
    public static final byte GUN_ROCKET = 4;
    public static final byte GUN_MORTAR = 5;
    public static final byte GUN_CHICKEN = 6;
    public static final byte GUN_BOOMERANG = 7;
    public static final byte GUN_HAMMER = 8;
    public static final byte GUN_PINGPONG = 10;
    public static final byte GUN_LASER_GIRL = 9;
    public static final byte GUN_BOMB_SMALL = 11;
    public static final byte GUN_BOMB_BIG = 12;
    public static final byte GUN_BUG_ROBOT = 13;
    public static final byte GUN_ROBOT = 14;
    public static final byte GUN_BIG_ROBOT = 15;
    public static final byte GUN_UFO = 16;
    public static final byte GUN_BOSS_KHICAU = 17;
    public static final byte GUN_BOSS_GUNKHICAU = 18;
    public static final byte GUN_BOSS_BOMBKHICAU = 19;
    public static final byte GUN_FAN1 = 20;
    public static final byte GUN_BOSS_EYEKHICAU = 21;
    public static final byte GUN_BOSS_SPIDER = 22;
    public static final byte GIFT_1 = 23;
    public static final byte GIFT_2 = 24;
    public static final byte GHOST = 25;
    public static final byte GHOST_2 = 26;
    static mImage crosshair;
    public static boolean isShooting;
    public boolean shootFrame;
    public static boolean isStopFall;
    public static boolean isGetPosition;
    public boolean isFreeze;
    public static mImage[] pImg;
    public boolean isPaint = true;
    public boolean isFly = false;
    public boolean isBum = false;
    public static FilePack filePack;
    public static byte[] fileData;
    public boolean notPaintNormal;
    public boolean flyPlayer;
    public boolean earthwakeActive;
    public boolean cantSee;
    public int xBug;
    public static mImage robotArm;
    public static mImage robotLeg;
    public static mImage robotBody;
    public static mImage robotInjured;
    public static mImage khicau;
    public static mImage gunkhicau;
    public static mImage bomb;
    public static mImage fan1;
    public static mImage fan2;
    public static mImage bombKhiCau;
    public static mImage mainGun;
    public static mImage eye;
    public static mImage back_fan;
    public static mImage front_fan;
    public static mImage injured;
    public static mImage diamond;
    public static mImage diamond2;
    public static mImage ghost;
    public static mImage ghost2;
    public static mImage fire;
    public static mImage imgUFO;
    public static mImage imgUFOFire;
    static mImage[] imgUFOFrames;
    static mImage[] imgUFOFireFrames;
    public static mImage spider;
    public static mImage web;
    private byte critical;
    public int idBullet;
    public boolean ghostBit;
    public mImage clanIcon;
    private int crossHairW;
    private int crossHairH;
    private int crossHairX;
    private int crossHairY;
    FrameImage pFrameImg;
    public boolean isJump;
    int framebd_1 = 0;
    public int frameleg_1 = 0;
    public int x;
    public int y;
    public static final int pW = 24;
    public static final int pH = 24;
    public boolean falling;
    public boolean diedFromFall;
    public int[] item;
    public int itemUsed = -1;
    public int CurSelectedItem = 0;
    public boolean isUsedItem;
    public boolean is2TurnItem;
    public int lastForcePoint = 0;
    public int lastForcePoint_2 = 0;
    public int movePoint = 0;
    public int IDDB;
    byte index;
    public byte gun = 0;
    public boolean isAllowSendPosAfterShoot;
    public boolean isActiveFall;
    public boolean activeFallbyEx;
    public boolean chophepGuiUpdateXY;
    public boolean isSecondPower = false;
    public boolean isDoublePower = false;
    public byte force = 0;
    public byte force_2 = 0;
    public int maxforce = 30;
    public int maxforce2 = 30;
    private int magentaTrajAngle = Integer.MIN_VALUE;
    private int magentaTrajForce = Integer.MIN_VALUE;
    private int magentaTrajX = Integer.MIN_VALUE;
    private int magentaTrajY = Integer.MIN_VALUE;
    private short[][] magentaTrajPaths;
    int radius = 30;
    public int angle = 0;
    int speedChangeAngle = 0;
    public int dx;
    public int dy;
    int curFrame = 5;
    int frameDelay;
    public int look = 2;
    int hurtLook = this.look;
    public static final int LEFT = 0;
    public static final int RIGHT = 2;
    public static final byte PSTATE_STAND = 0;
    public static final byte PSTATE_MOVE = 1;
    public static final byte PSTATE_AIM = 2;
    public static final byte PSTATE_READYSHOOT = 3;
    static final byte PSTATE_SHOOT = 4;
    public static final byte PSTATE_DIE = 5;
    public static final byte PSTATE_WIN = 7;
    public static final byte PSTATE_HURT = 8;
    public static final byte PSTATE_CAPTURE = 9;
    protected byte state = 0;
    int fspider;
    public static final int SLEFT = 0;
    public static final int SRIGHT = 1;
    public static final int SUP = 2;
    public static final int SDOWN = 3;
    public int sLook = 1;
    public static final int MAX_MOVE_POINT = 60;
    public boolean active = false;
    boolean isSendM_autoDie = false;
    public int hp;
    public int maxhp;
    String hpText = "";
    String expText = "";
    String cupText = "";
    public byte bulletType = 0;
    byte nShoot;
    public byte currAngry;
    public byte angryX;
    public boolean isAngry;
    public boolean isInvisible;
    public boolean isVampire;
    public boolean isRunSpeed;
    public boolean isStopWind;
    public boolean isPoison;
    public boolean poisonEff;
    public int tPEff;
    public boolean isCom;
    short lastx;
    public int lastUpdateX;
    public int lastUpdateY;
    public String name;
    int vy = 0;
    int g = 1;
    int nextx;
    int nexty;
    boolean isMove = false;
    public static mImage lua;
    public static short[] angleLock;
    public static short[] angleLockMain;
    public static mImage buggun;
    public static mImage bugbody;
    public static mImage bugleg;
    public PlayerEquip equip;
    int smokeDelayWhenDie = 100;
    boolean isCapture = false;
    byte whoCapture = (byte)-1;
    boolean capUp;
    boolean capDown;
    int capX;
    int capY;
    int playerHit;
    int xTo;
    int yTo;
    int xFrom;
    int yFrom;
    boolean flyActive = false;
    int xa;
    int ya;
    boolean isPointActive;
    public int yPoint;
    boolean outMap = false;
    int va;
    int vx;
    int tMove;
    public int xToNow;
    public int yToNow;
    int timeCount = 0;
    int delay = 1;
    int delayCount = 0;
    boolean isDelay = false;
    public boolean isExplore = true;
    public int xBugBack;
    public int yBugBack;
    public int bombIndex;
    public short[][] _x;
    public short[][] _y;
    public static int xSuper;
    public static int ySuper;
    public static boolean isStopFire;
    public boolean isPaintCountDown;
    public static int xM;
    public static int yM;
    public static int frameM;
    public int frameC;
    public static boolean isMirror;
    public static boolean closeMirror;
    public static int tCl;
    public int ta;
    public int fa;
    public int yT;
    public int hpRectW = 25;
    boolean hpChangeVisible = false;
    boolean addExp = false;
    boolean addCup = false;
    int exp;
    int cup;
    int hpChangeAmount = 0;
    int dyhp;
    int dyExp;
    int dyCup;
    public int nQuanHam;
    boolean hpTang;
    public byte runSpeed = 1;
    public static int deltaY;
    public static int tB;
    static int deltaX;
    public static int tBalloon;
    public static int deltaBalloon;
    boolean isHoldFire;
    boolean isHoldAngle;
    int t;
    private static final int AIM_CEILING_LOOK2 = 89;
    private static final int AIM_FLOOR_LOOK0 = 91;
    private int plusAngle = 1;
    private int angleHoldTicks = 0;
    public int savedAngle = Integer.MIN_VALUE;

    public void saveCurrentAngle() {
        this.savedAngle = this.angle;
    }

    public void onStartTurnAngle() {
        if (this.savedAngle != Integer.MIN_VALUE) {
            this.angle = this.savedAngle;
        }
        this.angleUpdate();
        this.checkAngleForSprite();
    }

    public static void init() {
        try {
            filePack = new FilePack(fileData);
            crosshair = GameScr.crosshair;
            CPlayer.pImg[0] = filePack.loadImage("yellowP.png");
            CPlayer.pImg[1] = filePack.loadImage("cuteGirl.png");
            CPlayer.pImg[2] = filePack.loadImage("greenP.png");
            CPlayer.pImg[3] = filePack.loadImage("conKhiP.png");
            CPlayer.pImg[4] = filePack.loadImage("rocketer.png");
            CPlayer.pImg[5] = filePack.loadImage("robot.png");
            CPlayer.pImg[6] = filePack.loadImage("ga.png");
            CPlayer.pImg[7] = filePack.loadImage("tazz.png");
            CPlayer.pImg[8] = filePack.loadImage("apache.png");
            CPlayer.pImg[9] = filePack.loadImage("cowg.png");
            CPlayer.pImg[10] = filePack.loadImage("magenta.png");
            CPlayer.pImg[11] = filePack.loadImage("bosssmall.png");
            CPlayer.pImg[12] = filePack.loadImage("bossbig.png");
            buggun = filePack.loadImage("bug_gun.png");
            bugbody = filePack.loadImage("bug_body.png");
            bugleg = filePack.loadImage("bug_leg.png");
            fire = filePack.loadImage("fire.png");
            robotArm = filePack.loadImage("arm.png");
            robotLeg = filePack.loadImage("leg.png");
            robotBody = filePack.loadImage("body.png");
            robotInjured = filePack.loadImage("body_injured.png");
            CPlayer.pImg[14] = filePack.loadImage("bossrobot.png");
            imgUFO = filePack.loadImage("UFO.png");
            imgUFOFire = filePack.loadImage("UFOFire.png");
            mImage mImage2 = mImage.createImage("/player/UFO.png");
            mImage mImage3 = mImage.createImage("/player/UFOFire.png");
            if (mImage2 != null) {
                imgUFO = mImage2;
            }
            if (mImage3 != null) {
                imgUFOFire = mImage3;
            }
            CPlayer.initUFOFrames();
            khicau = filePack.loadImage("khicau.png");
            gunkhicau = filePack.loadImage("gun.png");
            bomb = filePack.loadImage("bomb.png");
            fan1 = filePack.loadImage("fan1.png");
            fan2 = filePack.loadImage("fan2.png");
            bombKhiCau = filePack.loadImage("gunbig.png");
            mainGun = filePack.loadImage("mainGun.png");
            eye = filePack.loadImage("eye.png");
            ghost = filePack.loadImage("ma.png");
            ghost2 = filePack.loadImage("ma2.png");
            back_fan = filePack.loadImage("back_fan.png");
            front_fan = filePack.loadImage("front_fan.png");
            injured = filePack.loadImage("injured.png");
            spider = filePack.loadImage("spider.png");
            diamond = filePack.loadImage("diamond.png");
            CPlayer.pImg[18] = filePack.loadImage("mainGun.png");
            CPlayer.pImg[21] = eye;
            CPlayer.pImg[13] = buggun;
            CPlayer.pImg[15] = robotArm;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        filePack = null;
    }

    private static void initUFOFrames() {
        try {
            int n;
            if (imgUFO != null && CPlayer.imgUFO.image != null && CPlayer.imgUFO.image.getWidth() >= 51 && CPlayer.imgUFO.image.getHeight() >= 138) {
                imgUFOFrames = new mImage[3];
                for (n = 0; n < imgUFOFrames.length; ++n) {
                    CPlayer.imgUFOFrames[n] = new mImage(Image.createImage(CPlayer.imgUFO.image, 0, n * 46, 51, 46));
                }
            }
            if (imgUFOFire != null && CPlayer.imgUFOFire.image != null && CPlayer.imgUFOFire.image.getWidth() >= 16 && CPlayer.imgUFOFire.image.getHeight() >= 22) {
                imgUFOFireFrames = new mImage[2];
                for (n = 0; n < imgUFOFireFrames.length; ++n) {
                    CPlayer.imgUFOFireFrames[n] = new mImage(Image.createImage(CPlayer.imgUFOFire.image, 0, n * 11, 16, 11));
                }
            }
        }
        catch (Exception exception) {
            imgUFOFrames = null;
            imgUFOFireFrames = null;
        }
    }

    public CPlayer(int n, byte by, int n2, int n3, boolean bl, int n4, byte by2, PlayerEquip playerEquip, int n5) {
        this.IDDB = n;
        this.index = by;
        this.isCom = bl;
        this.look = n4;
        this.gun = by2;
        this.equip = playerEquip;
        this.isMove = false;
        this.angle = by2 >= 11 ? 0 : (n4 == 2 ? (angleLock[this.gun] < 0 ? 0 : angleLock[this.gun]) : 180 - angleLock[this.gun]);
        this.team = this.index % 2 == 0;
        this.idBullet = 0;
        short maskId = playerEquip != null ? playerEquip.getActiveMaskId() : -1;
        if (maskId > 0) {
            short[] setData = PlayerEquip.getMaskSetData(this.gun, maskId);
            if (setData != null && setData[0] > 0) {
                Equip maskGun = PlayerEquip.getEquip(this.gun, (byte)0, setData[0]);
                if (maskGun != null) {
                    this.idBullet = maskGun.bullet & 0xFF;
                }
            }
        } else if (playerEquip != null && playerEquip.equips[0] != null) {
            this.idBullet = playerEquip.equips[0].bullet & 0xFF;
        }
        if (this.gun == 3) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 30, 32);
        } else if (this.gun == 6) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 29, 24);
        } else if (this.gun == 7) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 32, 32);
        } else if (this.gun == 11) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 18, 19);
        } else if (this.gun == 12) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 30, 28);
        } else if (this.gun == 14) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 24, 30);
        } else if (this.gun == 13) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 15, 15);
        } else if (this.gun == 15) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 35, 40);
        } else if (this.gun == 9) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 24, 24);
        } else if (this.gun == 10) {
            this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 32, 32);
        } else if (this.gun != 16) {
            if (this.gun == 18) {
                this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 21, 20);
            } else if (this.gun != 17 && this.gun != 19) {
                if (this.gun == 21) {
                    this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 12, 12);
                } else if (this.gun != 20 && this.gun != 22 && this.gun != 23 && this.gun != 24 && this.gun != 25 && this.gun != 26) {
                    this.pFrameImg = new FrameImage(CPlayer.pImg[this.gun].image, 24, 24);
                }
            }
        }
        this.falling = true;
        this.activeFallbyEx = false;
        this.chophepGuiUpdateXY = true;
        this.x = n2;
        this.y = n3;
        xM = this.x;
        yM = this.y - 50;
        this.capX = n2;
        this.capY = n3;
        this.lastUpdateX = n2;
        this.lastUpdateY = n3;
        this.nextx = this.x;
        this.nexty = this.y;
        this.lastx = (short)this.x;
        this.itemUsed = -1;
        this.item = new int[PrepareScr.numCurItemSlot];
        this.hp = this.maxhp = n5;
        this.isPoison = false;
        this.crossHairH = CPlayer.crosshair.image.getHeight();
        this.crossHairW = CPlayer.crosshair.image.getWidth();
    }

    public void capture(byte by) {
        this.isCapture = true;
        this.whoCapture = by;
        this.capDown = true;
        this.capX = this.x;
        this.capY = this.y;
    }

    public boolean isCapturingPlayer() {
        return this.isCapture;
    }

    public void ghostHit(int n) {
        this.ghostBit = true;
        this.playerHit = n;
    }

    public void capturePlayer() {
        if (this.isCapture) {
            this.falling = false;
            this.sLook = 3;
            if (this.capDown) {
                this.y += 6;
                if (this.y >= PM.p[this.whoCapture].y - 24) {
                    this.y = PM.p[this.whoCapture].y - 24;
                    this.capDown = false;
                    this.capUp = true;
                }
            }
            if (this.capUp) {
                this.y -= 6;
                PM.p[this.whoCapture].y = this.y + 24;
                PM.p[this.whoCapture].nexty = this.y + 24;
                if (this.y <= this.capY) {
                    this.y = this.capY;
                    this.nexty = this.capY;
                    this.capUp = false;
                    this.isCapture = false;
                    CCanvas.lockNotify = true;
                }
            }
        }
    }

    public boolean checkMapDeep(int n, int n2, int n3) {
        int n4 = n3;
        for (int i = 0; i < n; ++i) {
            if (!GameScr.mm.isLand(n2, n4)) {
                return false;
            }
            ++n4;
        }
        return true;
    }

    public boolean isBestLocation(int n, int n2, int n3, int n4) {
        int n5 = n - n3 / 2;
        for (int i = 0; i < n3; ++i) {
            if (!this.checkMapDeep(n4, n5, n2)) {
                return false;
            }
            ++n5;
        }
        return true;
    }

    public boolean isChangeLocation() {
        return !this.checkMapDeep(60, this.x, this.y);
    }

    public Position move(int n, int n2) {
        int n3 = n2 * CRes.cos(CRes.fixangle(n)) >> 10;
        int n4 = -(n2 * CRes.sin(CRes.fixangle(n))) >> 10;
        return new Position(n3, n4);
    }

    public void setState(byte by) {
        if (this.state != 5) {
            this.state = by;
        }
    }

    public byte getState() {
        return this.state;
    }

    public void flyToPoint(int n, int n2) {
        this.flyActive = true;
        this.xTo = n;
        this.yTo = n2;
        this.xFrom = this.x;
        this.yFrom = this.y;
        if (this.xTo > this.xFrom) {
            this.sLook = 1;
            this.look = 2;
        } else {
            this.sLook = 0;
            this.look = 0;
        }
    }

    public boolean isFlyingToPoint() {
        return this.flyActive;
    }

    public void stopServerFlight() {
        if (this.flyActive) {
            this.x = this.xFrom;
            this.y = this.yFrom;
            this.nextx = this.x;
            this.nexty = this.y;
            this.lastx = (short)this.x;
        }
        this.flyActive = false;
        this.isPointActive = false;
        this.bulletType = (byte)-1;
        Boss.camY = 0;
    }

    public void flyTo(int n) {
        if (this.x >= MM.mapWidth) {
            this.x = -100;
            this.xTo = this.xFrom;
            this.yTo = this.yFrom;
            Boss.camY = 0;
        }
        int n2 = this.xTo - this.x;
        int n3 = this.y - this.yTo;
        int n4 = CRes.angle(n2, n3);
        this.xa = this.move((int)n4, (int)n).x;
        this.ya = this.move((int)n4, (int)n).y;
        this.x += this.xa;
        this.y += this.ya;
        if (this.x < this.xTo + n / 2 && this.x >= this.xTo - n / 2 && this.y < this.yTo + n / 2 && this.y >= this.yTo - n / 2) {
            this.x = this.xTo;
            this.y = this.yTo;
            this.yPoint = this.yTo;
            while (!GameScr.mm.isLand(this.x, this.yPoint) && this.yPoint < MM.mapHeight) {
                ++this.yPoint;
            }
            this.bulletType = (byte)-1;
            this.isPointActive = true;
            this.flyActive = false;
            CCanvas.lockNotify = true;
        }
    }

    public void update() {
        if (PM.curP == GameScr.myIndex) {
            if (this.angryX < this.currAngry) {
                this.angryX = (byte)(this.angryX + 2);
            }
            if (this.angryX > this.currAngry) {
                this.angryX = this.currAngry;
            }
        }
        switch (this.state) {
            default: {
                break;
            }
            case 5: {
                if (this.gun == 23 || this.gun == 24 || this.gun == 25 || this.gun == 26) break;
                if (this.smokeDelayWhenDie <= 0) {
                    GameScr.sm.addSmoke(this.x, this.y - 8, (byte)5);
                    this.smokeDelayWhenDie = 100;
                    break;
                }
                --this.smokeDelayWhenDie;
                break;
            }
            case 7: {
                this.animWin();
                break;
            }
            case 8: {
                this.animHurt();
            }
        }
        this.dx = this.radius * CRes.cos(this.angle) >> 10;
        this.dy = this.radius * CRes.sin(this.angle) >> 10;
        if (this.isCom && !this.flyPlayer && !this.isJump) {
            if (this.nextx == this.x && this.nexty == this.y) {
                this.setState((byte)1);
            }
            if (this.nextx < this.x && !this.falling) {
                this.move(0);
            } else if (this.nextx > this.x && !this.falling) {
                this.move(2);
            }
            if (this.nextx == this.x && this.nexty != this.y && this.state == 0 && !this.falling) {
                this.y = this.nexty;
            }
        }
        if (this.activeFallbyEx && !BM.active) {
            this.falling = true;
            this.activeFallbyEx = false;
            this.isActiveFall = true;
        }
        if (this.falling) {
            this.fall();
        }
        if (!this.isUsedItem) {
            this.angleReset();
        }
        if (this.poisonEff) {
            ++this.tPEff;
            if (this.tPEff == 20) {
                this.tPEff = 0;
                this.poisonEff = false;
                CCanvas.lockNotify = true;
            }
            if (CCanvas.gameTick % 2 == 0) {
                new Explosion(CRes.random(this.x - 10, this.x + 10), CRes.random(this.y - 20, this.y + 2), 10);
            }
        }
        if (this.state == 1 && this.x == this.xToNow && this.y == this.yToNow && this.isMove) {
            this.isMove = false;
            Session_ME.receiveSynchronized = 0;
        }
        if (this.isMove) {
            ++this.tMove;
            if (this.tMove == 200) {
                this.tMove = 0;
                this.x = this.xToNow;
                this.y = this.yToNow;
                this.tMove = 0;
                this.isMove = false;
                Session_ME.receiveSynchronized = 0;
            }
        }
    }

    public void angleReset() {
        if (this.gun <= 9 && this.gun != 15 && this.gun != 17) {
            if (this.look == 0 && this.angle > 180 - angleLock[this.gun]) {
                this.angle = 180 - angleLock[this.gun];
            }
            if (this.look == 2 && this.angle < angleLock[this.gun]) {
                this.angle = angleLock[this.gun];
            }
        }
    }

    public void checkNomarShoot() {
        if (this.look == 2) {
            if (this.angle < angleLock[this.gun]) {
                this.angle = angleLock[this.gun];
            } else if (this.angle > 89) {
                this.angle = 89;
            }
        } else if (this.look == 0) {
            if (this.angle > 180 - angleLock[this.gun]) {
                this.angle = 180 - angleLock[this.gun];
            } else if (this.angle < 91) {
                this.angle = 91;
            }
        }
    }

    public void angleUpdate() {
        if (!this.isUsedItem) {
            this.checkNomarShoot();
        } else {
            if (this.look == 0) {
                if (this.itemUsed == 20) {
                    if (this.angle > 225) {
                        this.angle = 225;
                    }
                    if (this.angle < 180) {
                        this.angle = 180;
                    }
                } else if (this.itemUsed == 22) {
                    if (this.angle != 91) {
                        this.angle = 91;
                        this.curFrame = 3;
                    }
                } else if (this.itemUsed == 23) {
                    this.angle = -269;
                } else {
                    this.checkNomarShoot();
                }
            }
            if (this.look == 2) {
                if (this.itemUsed == 20) {
                    if (this.angle > 0) {
                        this.angle = 0;
                    }
                    if (this.angle < -45) {
                        this.angle = -45;
                    }
                } else if (this.itemUsed == 22) {
                    if (this.angle != 89) {
                        this.angle = 89;
                        this.curFrame = 3;
                    }
                } else if (this.itemUsed == 23) {
                    this.angle = -89;
                } else {
                    this.checkNomarShoot();
                }
            }
        }
    }

    public void fall() {
        if (this.flyPlayer || this.isCapture) {
            if (this.gun == 25 || this.gun == 26) {
                this.falling = false;
                this.isActiveFall = false;
                if (this.state == 5) {
                    new Explosion(this.x, this.y, 1);
                    this.isPaint = false;
                }
                return;
            }
            if (this.state != 5) {
                this.falling = false;
                this.isActiveFall = false;
                return;
            }
        }
        if (this.y > MM.mapHeight + 200) {
            if (this.isActiveFall) {
                boolean bl = false;
                boolean bl2;
                this.isAllowSendPosAfterShoot = true;
                this.isActiveFall = false;
                if (this.state != 5 && !this.isSendM_autoDie) {
                    if (GameScr.myIndex != this.index) {
                    }
                    bl2 = true;
                } else {
                    bl2 = bl = false;
                }
                if (bl) {
                    if (GameScr.myIndex == this.index) {
                        GameService.gI().move((short)this.x, (short)this.y);
                    }
                    this.diedFromFall = true;
                    this.die();
                    this.isSendM_autoDie = true;
                    if (!GameScr.trainingMode) {
                        OfflineCombat.checkBattleEndNow();
                    }
                }
                this.falling = false;
                if (GameScr.trainingMode) {
                    GameService.gI().training((byte)1);
                    GameScr.trainingMode = false;
                    this.die();
                }
            }
            this.nexty = this.y;
            if (this.state != 1) {
                this.resetLastUpdateXY(this.x, this.y);
            }
        } else {
            this.vy += this.g;
            int n = this.y + this.vy;
            int n2 = Math.abs(n - this.y);
            for (int i = 0; i <= n2; ++i) {
                if (GameScr.mm.isLand(this.x, this.y)) {
                    this.vy = 0;
                    this.falling = false;
                    if (this.isActiveFall) {
                        this.isAllowSendPosAfterShoot = true;
                        this.isActiveFall = false;
                        if (PM.getCurPlayer().gun == 15) {
                            this.earthwakeActive = true;
                        }
                    }
                    this.nexty = this.y;
                    if (this.state != 1) {
                        this.resetLastUpdateXY(this.x, this.y);
                    }
                    if (this.index != GameScr.myIndex || this.state == 1 || BM.active || !this.chophepGuiUpdateXY) break;
                    GameService.gI().requiredUpdateXY((short)this.x, (short)this.y);
                    if (this.itemUsed != 23) break;
                    GameScr.pm.updatePlayerXY(this.index, (short)this.x, (short)this.y);
                    break;
                }
                ++this.y;
            }
        }
        if (MM.isHaveWaterOrGlass && !this.isWaterBum && MM.checkWaterBum(this.x, this.y, (byte)2)) {
            this.isWaterBum = true;
        }
    }

    public void holdFire() {
        CCanvas.keyPressed[12] = false;
        CCanvas.keyPressed[13] = false;
        if (this.state != 3 && this.force > 1) {
            this.setState((byte)3);
            this.bulletType = this.resolveCurrentShotBulletType();
            this.isDoublePower = Bullet.isDoubleBull(this.bulletType) && !this.isUsedItem ? true : (this.is2TurnItem ? Bullet.isDoubleBull(this.bulletType) : false);
            GameScr.time.stop();
        } else if (!this.isDoublePower) {
            this.force = (byte)(this.force + 1);
            if (this.force >= this.maxforce) {
                this.shoot();
                if (Bullet.isDoubleBull(this.bulletType)) {
                    this.isDoublePower = true;
                }
                GameScr.clearKey();
                isStopFire = true;
            }
        } else if (!this.isSecondPower) {
            this.force = (byte)(this.force + 1);
            if (this.force >= this.maxforce) {
                this.isSecondPower = true;
            }
        } else {
            this.force_2 = (byte)(this.force_2 + 1);
            if (this.force_2 >= this.maxforce2) {
                this.shoot();
                this.isSecondPower = false;
                GameScr.clearKey();
                isStopFire = true;
            }
        }
    }

    public void fire() {
        if (!this.isDoublePower) {
            if (this.force > 1 && this.state == 3) {
                this.setState((byte)2);
                this.shoot();
                GameScr.clearKey();
                this.force = 0;
            } else {
                this.force = 0;
            }
        } else if (this.state == 3) {
            if (!this.isSecondPower) {
                this.isSecondPower = true;
            } else if (this.force_2 > 1) {
                this.setState((byte)2);
                this.shoot();
                this.isSecondPower = false;
                GameScr.clearKey();
                this.force = 0;
                this.force_2 = 0;
            }
        } else {
            this.force = 0;
            this.force_2 = 0;
        }
    }

    public void shoot() {
        this.saveCurrentAngle();
        this.active = false;
        isShooting = true;
        this.shootFrame = true;
        this.nShoot = 1;
        isGetPosition = true;
        this.bulletType = this.resolveCurrentShotBulletType();
        if (this.itemUsed == 2) {
            this.nShoot = (byte)(this.nShoot * 2);
            this.itemUsed = -1;
            this.sendWaitForFire(this.bulletType, this.nShoot);
        } else if (this.isProjectileItem(this.itemUsed)) {
            this.itemUsed = -1;
            this.sendWaitForFire(this.bulletType, (byte)1);
        } else {
            this.sendWaitForFire(this.bulletType, this.nShoot);
        }
        CPlayer.angleLock[this.gun] = angleLockMain[this.gun];
    }

    public byte resolveCurrentShotBulletType() {
        switch (this.itemUsed) {
            case 1: {
                return 5;
            }
            case 6: {
                return 6;
            }
            case 7: {
                return 7;
            }
            case 8: {
                return 4;
            }
            case 9: {
                return 8;
            }
            case 11: {
                return 16;
            }
            case 16: {
                return 14;
            }
            case 17: {
                return 13;
            }
            case 18: {
                return 22;
            }
            case 19: {
                return 26;
            }
            case 20: {
                return 25;
            }
            case 21: {
                return 23;
            }
            case 22: {
                return 28;
            }
            case 23: {
                return 30;
            }
            case 24: {
                return 50;
            }
            case 25: {
                return 51;
            }
            case 26: {
                return 52;
            }
            case 27: {
                return 53;
            }
            case 28: {
                return 54;
            }
            case 29: {
                return 55;
            }
            case 30: {
                return 56;
            }
            case 31: {
                return 57;
            }
        }
        return Bullet.setBulletType(this.gun);
    }

    private boolean isProjectileItem(int n) {
        switch (n) {
            case 1:
            case 6:
            case 7:
            case 8:
            case 9:
            case 11:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 28:
            case 29:
            case 30:
            case 31: {
                return true;
            }
        }
        return false;
    }

    public void shoot(byte by, byte by2, short s, short s2, byte by3, short[][] sArray, short[][] sArray2, byte by4, byte by5, short s3, short[][] sArray3, short[][] sArray4, int n, int n2) {
        this.shootFrame = true;
        this.isPointActive = false;
        if (GameScr.pm.isYourTurn() && this.index == GameScr.myIndex) {
            this.active = false;
            isShooting = true;
        }
        this.lastx = s;
        this.nextx = this.lastx;
        this.x = this.lastx;
        short s4 = s2;
        this.nexty = s4;
        this.y = s4;
        xSuper = n;
        ySuper = n2;
        this.resetLastUpdateXY(this.x, this.y);
        if (by3 != 36) {
            this.look = sArray[0][0] > s ? 2 : 0;
        }
        this.bulletType = by3;
        this.angle = s3;
        this.checkAngleForSprite();
        int bulletFrame = this.idBullet;
        short maskId2 = this.equip != null ? this.equip.getActiveMaskId() : -1;
        if (maskId2 > 0) {
            short[] setData = PlayerEquip.getMaskSetData(this.gun, maskId2);
            if (setData != null && setData[0] > 0) {
                Equip maskGun = PlayerEquip.getEquip(this.gun, (byte)0, setData[0]);
                if (maskGun != null) {
                    bulletFrame = maskGun.bullet & 0xFF;
                }
            }
        } else if (this.equip != null && this.equip.equips[0] != null) {
            bulletFrame = (int)this.equip.equips[0].bullet;
        }
        GameScr.bm.setBullType(by, by2, by3, sArray, sArray2, by4, by5, sArray3, sArray4, bulletFrame);
        this.setState((byte)4);
        if (by3 == 35) {
            this.isBum = true;
        }
        if (by3 == 43) {
            for (int i = 0; i < sArray.length; ++i) {
                if (sArray[i][0] + MM.mapWidth / 10 < MM.mapWidth) continue;
                this.bombIndex = i;
                break;
            }
            this._x = sArray;
            this._y = sArray2;
            GameScr.pm.flyTo(by2, (short)(MM.mapWidth + 100), sArray2[0][0]);
        }
        if (by3 == 44) {
            Boss.xTo = sArray[0][sArray[0].length - 1];
        }
        if (by3 == 47) {
            try {
                this.check_Spider_look();
            }
            catch (Exception exception) {
            }
        }
    }

    public void check_Spider_look() {
        short s = this._x[2][3];
        short s2 = this._y[2][3];
        int n = Smoke.checkWay(this.x, this.y, s, s2);
        switch (n) {
            case 0: {
                this.sLook = 0;
                break;
            }
            case 1: {
                this.sLook = 1;
                break;
            }
            case 2: {
                this.sLook = 2;
                break;
            }
            case 3: {
                this.sLook = 3;
            }
        }
    }

    public void lucky() {
        GameScr.sm.addRock(this.x, this.y - 12, CRes.random(4), CRes.random(-8, -5), (byte)20);
        GameScr.sm.addRock(this.x, this.y - 12, CRes.random(3), CRes.random(-8, -4), (byte)20);
        GameScr.sm.addRock(this.x, this.y - 12, -CRes.random(4), CRes.random(-8, -5), (byte)20);
        GameScr.sm.addRock(this.x, this.y - 12, -CRes.random(3), CRes.random(-8, -4), (byte)20);
        CCanvas.tNotify = 0;
        CCanvas.lockNotify = true;
    }

    void sendWaitForFire(byte by, byte by2) {
        if (GameScr.trainingMode) {
            GameService.gI().waitForFIRETraining(by, (short)this.x, (short)this.y, (short)this.angle, this.force, this.force_2, by2);
        } else {
            GameService.gI().waitForFIRE(by, (short)this.x, (short)this.y, (short)this.angle, this.force, this.force_2, by2);
        }
        this.lastForcePoint = this.force;
        this.lastForcePoint_2 = this.force_2;
        this.force = 0;
        this.force_2 = 0;
    }

    public void UseItem(int n, boolean bl, int n2) {
        if (!bl) {
            if (this.isUsedItem || n < 0 || n > 37 || this.item == null || n2 < 0 || n2 >= this.item.length) {
                return;
            }
            this.isUsedItem = true;
            GameService.gI().useItem((byte)n);
            if (PrepareScr.currLevel != 7) {
                this.item[n2] = -1;
            }
        } else {
            if ((n < 0 || n > 37) && n != 100) {
                return;
            }
            switch (n) {
                case 0:
                case 2:
                case 32:
                case 33: {
                    this.is2TurnItem = true;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    break;
                }
                case 1:
                case 6:
                case 7:
                case 8:
                case 9:
                case 11:
                case 16:
                case 17:
                case 18:
                case 19:
                case 21:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30: {
                    this.is2TurnItem = false;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    break;
                }
                case 3: {
                    this.is2TurnItem = true;
                    this.isRunSpeed = true;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    if (this.isInvisible) break;
                    new Explosion(this.x, this.y, 4, this.index, (byte)n);
                    break;
                }
                case 4:
                case 34: {
                    this.is2TurnItem = true;
                    this.isInvisible = true;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    new Explosion(this.x, this.y, 4, this.index, (byte)n);
                    break;
                }
                case 5: {
                    this.is2TurnItem = true;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    break;
                }
                case 10: {
                    this.is2TurnItem = true;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    break;
                }
                case 20: {
                    if (this.look == 2) {
                        this.angle = 0;
                    }
                    if (this.look == 0) {
                        this.angle = 180;
                    }
                    this.is2TurnItem = false;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    break;
                }
                case 22: {
                    this.angle = 89;
                    this.is2TurnItem = false;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    break;
                }
                case 23: {
                    this.angle = -89;
                    this.is2TurnItem = false;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    break;
                }
                case 35: {
                    this.is2TurnItem = true;
                    new Explosion(this.x, this.y - 12, 5, this.index, (byte)n);
                    new Explosion(this.x, this.y, 4, this.index, (byte)n);
                    break;
                }
                case 100: {
                    new Explosion(this.x, this.y - 12, 5, this.index, 38);
                }
            }
            this.itemUsed = n;
            this.bulletType = this.resolveCurrentShotBulletType();
            this.setState((byte)0);
            if (!this.isInvisible) {
                new Explosion(this.x, this.y - 12, 3, this.index, (byte)n);
            }
            if (this.index == GameScr.myIndex && n != 100) {
                ShopItem.consumeOwnedItem(n);
            }
            this.force = 0;
            this.force_2 = 0;
            if (this.itemUsed != 100 && this.isSecondPower) {
                this.isSecondPower = false;
            }
            if (this.gun == 6 || this.gun == 8) {
                this.isDoublePower = true;
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        block17: {
            Object object;
            block19: {
                block21: {
                    block20: {
                        block18: {
                            if (!this.isPaint || PrepareScr.currLevel == 7 && (PrepareScr.currLevel != 7 || this.state == 5 || this.hp <= 0)) break block17;
                            if (CRes.isNullOrEmpty(GameScr.res)) break block18;
                            this.paintPlayer(mGraphics2);
                            this.paintName(mGraphics2);
                            this.painthp(mGraphics2);
                            break block19;
                        }
                        if (this.isInvisible) break block20;
                        this.paintPlayer(mGraphics2);
                        this.paintName(mGraphics2);
                        this.painthp(mGraphics2);
                        break block19;
                    }
                    if (this.index == GameScr.myIndex) break block21;
                    object = GameScr.pm;
                    if (this.team != PM.getMyPlayer().team) break block19;
                }
                this.paintPlayer(mGraphics2);
                this.paintName(mGraphics2);
                this.painthp(mGraphics2);
            }
            this.painthpChange(mGraphics2);
            this.paintExpChange(mGraphics2);
            this.paintCup(mGraphics2);
            if (GameScr.pm.isYourTurn() && GameScr.myIndex == this.index && this.state != 8) {
                this.paintCrosshair(mGraphics2);
            }
            if (CCanvas.isDebugging()) {
                object = "";
                if (this.state == 0) {
                    object = "STAND 0";
                }
                if (this.state == 1) {
                    object = " MOVE   1";
                }
                if (this.state == 2) {
                    object = " AIM   2";
                }
                if (this.state == 3) {
                    object = " READYSHOOT 3";
                }
                if (this.state == 4) {
                    object = " SHOOT 4";
                }
                if (this.state == 5) {
                    object = " DIE 5";
                }
                if (this.state == 7) {
                    object = " WIN   6";
                }
                if (this.state == 8) {
                    object = " HURT  8";
                }
                if (this.state == 9) {
                    object = " CAPTURE   9";
                }
                String string = "";
                if (this.look == 0) {
                    string = "LEFT";
                }
                if (this.look == 2) {
                    string = "RIGHT";
                }
                Font.normalFont.drawString(mGraphics2, (String)object, this.x, this.y - 44 - 15, 2);
                Font.normalFont.drawString(mGraphics2, string, this.x, this.y - 44 - 30, 2);
                Font.normalFont.drawString(mGraphics2, "angle " + this.angle, this.x, this.y - 44 - 30 - 15, 2);
                Font.normalFont.drawString(mGraphics2, "lock[ " + angleLock[this.gun] + " ]", this.x, this.y - 44 - 30 - 15 - 15, 2);
                mGraphics2.setColor(2263535);
                mGraphics2.fillRect(this.x, this.y - 5, 1, 1, false);
                for (int i = 0; i < 5; ++i) {
                    mGraphics2.setColor(16767817);
                    mGraphics2.fillRect(this.x, this.y - i, 1, 1, false);
                }
                Font.normalFont.drawString(mGraphics2, this.x + "/" + this.y, this.x, this.y, 2);
                mGraphics2.setColor(1133755);
                mGraphics2.drawLine(this.x, this.y, this.x + this.dx, this.y - this.dy - 11, false);
            }
        }
    }

    private void paintPlayer(mGraphics mGraphics2) {
        if (this.state != 5 && this.gun < 11 && this.isAngry) {
            ++this.ta;
            if (this.ta == 2) {
                this.ta = 0;
                ++this.fa;
                if (this.fa > 3) {
                    this.fa = 0;
                    this.yT = 0;
                }
            }
            mGraphics2.drawRegion(lua, 0, this.fa * 47, 41, 47, 0, this.x, this.y, 33, false);
        }
        int n = this.look;
        if (this.state == 8) {
            n = this.hurtLook;
        }
        if (this.gun != 15 && this.gun != 16 && this.gun != 17 && this.gun != 19 && this.gun != 21 && this.gun != 22 && this.gun != 23 && this.gun != 24) {
            if (PrepareScr.currLevel != 7 || PrepareScr.currLevel == 7 && this.state != 5 && this.hp != 0) {
                if (this.equip != null) {
                    this.equip.paint(mGraphics2, n, this.curFrame, this.x, this.y);
                } else {
                    this.pFrameImg.drawFrame(this.curFrame, this.x, this.y, n, 33, mGraphics2);
                }
            }
            if (this.clanIcon != null && GameScr.iconOnOf) {
                mGraphics2.drawImage(this.clanIcon, this.x, this.y - 47, 33, false);
            }
            if (closeMirror && ++tCl == 10) {
                tCl = 0;
                isMirror = false;
                closeMirror = false;
            }
            if (this.isPoison && CCanvas.gameTick % 5 == 0) {
                new Explosion(CRes.random(this.x - 10, this.x + 10), CRes.random(this.y - 20, this.y + 2), 10);
            }
            if (GameScr.bm.critical == 0 && this.index == GameScr.myIndex && CCanvas.gameTick % 2 == 0) {
                ++this.frameC;
                if (this.frameC == 2) {
                    this.frameC = 0;
                }
            }
        }
    }

    private void paintCrosshair(mGraphics mGraphics2) {
        if (!(this instanceof Boss)) {
            mGraphics2.drawImage(crosshair, this.x + this.dx, this.y - this.dy - 11, mGraphics.HCENTER | mGraphics.VCENTER, false);
            if (CCanvas.isDebugging()) {
                String string = "a " + this.angle;
                Font.smallFont.drawString(mGraphics2, string, this.x + this.dx + 5, this.y - this.dy - 11 + 5, 2, false);
                this.crossHairX = this.x - this.dx;
                this.crossHairY = this.y + this.dy + 11;
                mGraphics2.drawImage(crosshair, this.crossHairX, this.crossHairY, mGraphics.HCENTER | mGraphics.VCENTER, false);
                string = "xywh " + this.crossHairX + "/" + this.crossHairY + "/" + this.crossHairW + "/" + this.crossHairH;
                Font.smallFont.drawString(mGraphics2, string, this.crossHairX, this.crossHairY - 20, 2, false);
            }
        }
    }

    void painthp(mGraphics mGraphics2) {
        if (GameScr.res.equals("") && this.gun != 15) {
            this.paintHpBar(mGraphics2, this.x - 15, this.y - 58, 25);
        }
    }

    protected void paintHpBar(mGraphics mGraphics2, int n, int n2, int n3) {
        int n4;
        if (this.state == 5 || this.hp <= 0) {
            return;
        }
        int n5 = n4 = this.maxhp > 0 ? this.hp * n3 / this.maxhp : 0;
        if (n4 > n3) {
            n4 = n3;
        }
        if (n4 < 0) {
            n4 = 0;
        }
        Font.borderFont.drawString(mGraphics2, this.hp + "/" + this.maxhp, n + n3 / 2, n2 - 18, 2);
        mGraphics2.setColor(0xFFFFFF);
        mGraphics2.fillRect(n, n2, n3, 4, false);
        if (n4 > n3 * 16 / 25) {
            mGraphics2.setColor(65280);
        } else if (n4 > n3 * 8 / 25) {
            mGraphics2.setColor(16744512);
        } else {
            mGraphics2.setColor(0xFF0000);
        }
        mGraphics2.fillRect(n, n2, n4, 4, false);
        mGraphics2.setColor(0);
        mGraphics2.drawRect(n, n2, n3, 4, false);
    }

    public void paintName(mGraphics mGraphics2) {
        if (this.name != null) {
            if (PM.curP == this.index) {
                if (CCanvas.gameTick % 10 > 5) {
                    (this.team ? Font.smallFontRed : Font.smallFontYellow).drawString(mGraphics2, this.name.toUpperCase(), this.x, this.y - 44, 2);
                }
            } else {
                (this.team ? Font.smallFontRed : Font.smallFontYellow).drawString(mGraphics2, this.name.toUpperCase(), this.x, this.y - 44, 2);
            }
        }
        int n = this.gun != 10 ? 27 : 35;
        PrepareScr.paintQuanHam(this.nQuanHam, this.x, this.y - n, mGraphics.VCENTER | mGraphics.HCENTER, mGraphics2);
    }

    public void updateAngry(byte by) {
        this.currAngry = by;
        if (this.currAngry == 100) {
            this.isAngry = true;
        }
        if (this.currAngry != 100) {
            this.isAngry = false;
        }
    }

    public void updateHP(int n, byte by) {
        boolean bl = false;
        this.hpChangeVisible = true;
        this.hpRectW = by;
        if (n < this.hp) {
            this.hpChangeAmount = Math.abs(this.hp - n);
            this.hpText = "- " + this.hpChangeAmount;
            this.hpTang = false;
            if (GameScr.cam != null) {
                GameScr.cam.setTargetPointMode(this.x, this.y);
            }
        } else {
            this.hpTang = true;
            this.hpChangeAmount = Math.abs(n - this.hp);
            this.hpText = "+ " + this.hpChangeAmount;
            bl = true;
        }
        this.dyhp = 25;
        this.hp = n;
        if (n <= 0) {
            this.die();
        }
        if (!bl) {
            GameScr.waitting();
        }
    }

    public void updateExp(int n) {
        this.addExp = true;
        this.exp = n;
        this.dyExp = 30;
        this.expText = n >= 0 ? "exp: +" + n : "exp: " + n;
        GameScr.cam.setTargetPointMode(this.x, this.y);
        GameScr.waitting();
    }

    public void updateCup(int n) {
        this.addCup = true;
        this.cup = n;
        this.dyCup = 35;
        this.cupText = n >= 0 ? " +" + n : "" + n;
        GameScr.cam.setTargetPointMode(this.x, this.y);
        GameScr.waitting();
    }

    public void die() {
        this.setState((byte)5);
        this.hp = 0;
        int n = this.curFrame = this.gun == 10 ? 9 : 7;
        if (this.isInvisible) {
            this.isInvisible = false;
        }
        if (this.isRunSpeed) {
            this.isRunSpeed = false;
        }
        if (this.flyPlayer) {
            if (this.gun == 21) {
                return;
            }
            if (this.gun == 17) {
                return;
            }
            this.falling = true;
        }
        Session_ME.receiveSynchronized = 0;
    }

    void paintExpChange(mGraphics mGraphics2) {
        if (this.addExp) {
            mGraphics2.setColor(0xFF0000);
            Font.borderFont.drawString(mGraphics2, this.expText, this.x, this.y - this.dyExp, mGraphics.VCENTER | mGraphics.HCENTER);
            ++this.dyExp;
            if (this.dyExp > 200) {
                this.addExp = false;
                this.expText = "";
            }
        }
    }

    void paintCup(mGraphics mGraphics2) {
        if (this.addCup) {
            Font.borderFont.drawString(mGraphics2, this.cupText, this.x, this.y - this.dyCup, mGraphics.VCENTER | mGraphics.HCENTER);
            mGraphics2.drawImage(CScreen.cup, this.x + 20, this.y - this.dyCup + 10, 3, false);
            ++this.dyCup;
            if (this.dyCup > 200) {
                this.addCup = this.falling;
                this.cupText = "";
            }
        }
    }

    void painthpChange(mGraphics mGraphics2) {
        if (this.hpChangeVisible) {
            mGraphics2.setColor(0xFF0000);
            Font.bigFont.drawString(mGraphics2, this.hpText, this.x, this.y - this.dyhp, mGraphics.VCENTER | mGraphics.HCENTER);
            ++this.dyhp;
            if (this.dyhp > 50) {
                this.hpChangeVisible = false;
                this.hpText = "";
            }
        }
    }

    public void move(int n) {
        if (this.state == 0 || this.state == 2 || this.state == 8) {
            this.setState((byte)1);
            this.curFrame = 4;
        }
        if (this.state != 3) {
            if (n == 0) {
                if (this.look != 0) {
                    this.angle = 180 - this.angle;
                    this.saveCurrentAngle();
                }
            } else if (n == 2 && this.look != 2) {
                this.angle = 180 - this.angle;
                this.saveCurrentAngle();
            }
            this.look = n;
        }
        if (!this.isFreeze && this.state == 1) {
            if (!this.isActiveFall) {
                this.isActiveFall = true;
            }
            if (MM.isHaveWaterOrGlass && GameScr.exs.size() == 0) {
                MM.checkWaterBum(this.x, this.y, (byte)0);
            }
            int n2 = this.runSpeed;
            if (this.isRunSpeed) {
                n2 = 2;
            }
            if (!this.isCom) {
                if (this.movePoint > 60) {
                    return;
                }
                ++this.movePoint;
            }
            this.animMove();
            if (n == 0) {
                this.x -= n2;
            } else if (n == 2) {
                this.x += n2;
            }
            this.falling = true;
            if (GameScr.mm.isLand(this.x, this.y - 5)) {
                if (!this.isCom) {
                    --this.movePoint;
                }
                this.falling = false;
                if (n == 0) {
                    this.x += n2;
                } else if (n == 2) {
                    this.x -= n2;
                }
                if (this.isCom && this.x == this.nextx && this.y != this.nexty) {
                    this.y = this.nexty;
                }
            } else {
                for (int i = 4; i >= 0; --i) {
                    if (!GameScr.mm.isLand(this.x, this.y - i)) continue;
                    this.falling = false;
                    this.y -= i;
                    this.nexty = this.y;
                    if (!this.isCom || this.x != this.nextx || this.y == this.nexty) continue;
                    this.y = this.nexty;
                }
            }
        }
    }

    public void move(int n, int n2, int n3) {
        if (this.state == 0 || this.state == 2 || this.state == 8) {
            this.setState((byte)1);
            this.curFrame = 4;
        }
        if (this.state != 3) {
            if (n == 0) {
                if (this.look != 0) {
                    this.angle = 180 - this.angle;
                }
                this.look = 0;
            } else if (n == 2) {
                if (this.look != 2) {
                    this.angle = 180 - this.angle;
                }
                this.look = 2;
            }
        }
        if (!this.isFreeze && this.state == 1) {
            if (!this.isActiveFall) {
                this.isActiveFall = true;
            }
            if (MM.isHaveWaterOrGlass && GameScr.exs.size() == 0) {
                MM.checkWaterBum(this.x, this.y, (byte)0);
            }
            int n4 = this.runSpeed;
            if (this.isRunSpeed) {
                n4 = 2;
            }
            if (!this.isCom) {
                if (this.movePoint > 60) {
                    return;
                }
                ++this.movePoint;
            }
            this.animMove();
            if (n == 0) {
                this.x -= n4;
            } else if (n == 2) {
                this.x += n4;
            }
            this.falling = true;
            if (GameScr.pm.isYourTurn() && !CCanvas.isPointerDown[this.index]) {
                this.resetLastUpdateXY(this.x, this.y);
                GameService.gI().move((short)this.x, (short)this.y);
            }
            if (GameScr.mm.isLand(this.x, this.y - 5)) {
                if (!this.isCom) {
                    --this.movePoint;
                }
                if (n == 0) {
                    this.x += n4;
                } else if (n == 2) {
                    this.x -= n4;
                }
                this.falling = false;
                if (this.isCom && this.x == this.nextx && this.y != this.nexty) {
                    this.y = this.nexty;
                }
            } else {
                for (int i = 4; i >= 0; --i) {
                    if (!GameScr.mm.isLand(this.x, this.y - i)) continue;
                    this.y -= i;
                    this.nexty = this.y;
                    this.falling = false;
                    if (!this.isCom || this.x != this.nextx || this.y == this.nexty) continue;
                    this.y = this.nexty;
                }
            }
        }
    }

    public void fillMaxForce() {
        if (this.falling || this.state == 5 || isStopFire || isShooting) {
            return;
        }
        if (this.state != 3) {
            this.bulletType = this.resolveCurrentShotBulletType();
            this.isDoublePower = Bullet.isDoubleBull(this.bulletType) && !this.isUsedItem ? true : (this.is2TurnItem ? Bullet.isDoubleBull(this.bulletType) : false);
            this.setState((byte)3);
            GameScr.time.stop();
            this.force = 1;
        }
        this.force = (byte)this.maxforce;
        if (this.isDoublePower && this.isSecondPower) {
            this.force_2 = (byte)this.maxforce2;
        }
    }

    private int getAngleStep() {
        ++this.angleHoldTicks;
        if (this.angleHoldTicks <= 2) {
            return 1;
        }
        if (this.angleHoldTicks <= 6) {
            return 2;
        }
        if (this.angleHoldTicks <= 12) {
            return 3;
        }
        return 4;
    }

    private void plusAngle() {
        if (this.state != 1) {
            this.angle += this.getAngleStep();
            this.angleUpdate();
            this.checkAngleForSprite();
            this.saveCurrentAngle();
        }
    }

    private void minusAngle() {
        if (this.state != 1) {
            this.angle -= this.getAngleStep();
            this.angleUpdate();
            this.checkAngleForSprite();
            this.saveCurrentAngle();
        }
    }

    public void updateHoldKey() {
        if (CCanvas.curScr instanceof GameScr && ((GameScr)CCanvas.curScr).isSelectingItem()) {
            return;
        }
        if (CCanvas.curScr instanceof GameScr && System.currentTimeMillis() - ((GameScr)CCanvas.curScr).timeDelayClosePauseMenu < 350L) {
            return;
        }
        if (!(this.state == 5 || isShooting || this.falling || CCanvas.currentDialog != null || CCanvas.pausemenu.isShow || isStopFire || Camera.mode != 1)) {
            this.keyHold();
        }
    }

    public final void keyHold() {
        if (!isStopFire && (!this.isCom && this.active || GameScr.trainingMode) && this.state != 5) {
            boolean isHoldingAngle = false;
            if (!CScreen.keyFire) {
                if (CCanvas.keyHold[2] && !this.falling) {
                    isHoldingAngle = true;
                    if (this.look == 2) {
                        this.plusAngle();
                    } else {
                        this.minusAngle();
                    }
                }
            }
            if (!CScreen.keyFire) {
                if (CCanvas.keyHold[8] && !this.falling) {
                    isHoldingAngle = true;
                    if (this.look == 0) {
                        this.plusAngle();
                    } else {
                        this.minusAngle();
                    }
                }
            }
            if (!isHoldingAngle) {
                this.angleHoldTicks = 0;
            }
            if (!CScreen.keyFire) {
                if (CCanvas.keyHold[4] && !this.falling && !CCanvas.keyHold[6] && !CCanvas.keyHold[5]) {
                    this.move(0);
                    GameScr.cam.checkIndex(4);
                }
            } else if (CCanvas.keyHold[4] && !this.falling) {
                this.move(0);
                GameScr.cam.checkIndex(4);
            }
            if (!CScreen.keyFire) {
                if (CCanvas.keyHold[6] && !this.falling && !CCanvas.keyHold[4] && !CCanvas.keyHold[5]) {
                    this.move(2);
                    GameScr.cam.checkIndex(4);
                }
            } else if (CCanvas.keyHold[6] && !this.falling) {
                this.move(2);
                GameScr.cam.checkIndex(4);
            }
            if (CCanvas.keyHold[5] && !this.falling && !CCanvas.keyHold[4] && !CCanvas.keyHold[6]) {
                this.holdFire();
            } else if (!this.isDoublePower) {
                if (this.force > 1 && this.state == 3) {
                    this.setState((byte)2);
                    this.shoot();
                    CScreen.clearKey();
                    this.force = 0;
                } else {
                    this.force = 0;
                }
            } else if (this.state == 3) {
                if (!this.isSecondPower) {
                    this.isSecondPower = true;
                } else if (this.force_2 > 1) {
                    this.setState((byte)2);
                    this.shoot();
                    this.isSecondPower = false;
                    CScreen.clearKey();
                    this.force = 0;
                    this.force_2 = 0;
                }
            } else {
                this.force = 0;
                this.force_2 = 0;
            }
            if (this.state == 1 && (!CCanvas.keyHold[4] && this.look == 0 || !CCanvas.keyHold[6] && this.look == 2)) {
                this.state = 0;
                this.checkAngleForSprite();
                if (this.lastx != this.x) {
                    GameService.gI().move((short)this.x, (short)this.y);
                }
                this.lastx = (short)this.x;
            }
        }
    }

    public void aimUp() {
    }

    public void aimDown() {
    }

    public void checkAngleForSprite() {
        this.curFrame = this.angle < 255 && this.angle > 90 ? (this.angle > 195 ? 0 : (this.angle > 165 ? 1 : (this.angle > 115 ? 2 : 3))) : (this.angle > 65 ? 3 : (this.angle > 15 ? 2 : (this.angle > -15 ? 1 : 0)));
    }

    public void animMove() {
        if (this.curFrame < 4 || this.curFrame > 5) {
            this.curFrame = 4;
            this.frameDelay = 0;
            return;
        }
        ++this.frameDelay;
        if (this.frameDelay > 2) {
            ++this.curFrame;
            if (this.curFrame == 6) {
                this.curFrame = 4;
            }
            this.frameDelay = 0;
        }
    }

    public void animWin() {
        ++this.frameDelay;
        if (this.frameDelay > 4) {
            ++this.curFrame;
            if (this.curFrame == 10) {
                this.curFrame = 8;
            }
            this.frameDelay = 0;
        }
    }

    public void activeHurt(int n) {
        if (this.state != 8 && this.state != 5) {
            this.setState((byte)8);
            this.frameDelay = 0;
            this.curFrame = 6;
            this.hurtLook = n;
        }
    }

    public void checkGhostLook(int n, int n2) {
        this.look = n > n2 ? 2 : 0;
    }

    public void animHurt() {
        ++this.frameDelay;
        if (this.frameDelay > 25) {
            if (this.hp <= 0) {
                this.die();
            } else {
                this.setState((byte)0);
                this.checkAngleForSprite();
                this.frameDelay = 0;
            }
        }
    }

    public void setWin() {
        this.curFrame = 8;
        this.setState((byte)7);
    }

    public static void paintBugRobot(mGraphics mGraphics2, int n, int n2) {
        if (++tB == 10) {
            tB = 0;
        }
        if (tB == 5) {
            deltaY = 1;
        }
        if (tB == 0) {
            deltaY = 0;
        }
        int n3 = n + 4;
        int n4 = n2 - 10 + deltaY;
        int n5 = n - 15;
        int n6 = n2 - 15 + deltaY;
        mGraphics2.drawRegion(bugbody, 0, 0, 42, 30, 2, n3, n4, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        mGraphics2.drawRegion(bugleg, 0, 0, 44, 25, 2, n, n2, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        mGraphics2.drawRegion(buggun, 0, 15, 15, 15, 0, n5, n6, mGraphics.BOTTOM | mGraphics.HCENTER, false);
    }

    public static void paintBigRobot(mGraphics mGraphics2, int n, int n2) {
        if (++tB == 10) {
            tB = 0;
        }
        if (tB == 5) {
            deltaY = 1;
        }
        if (tB == 0) {
            deltaY = 0;
        }
        mGraphics2.drawImage(robotLeg, n, n2, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        int n3 = mGraphics.VCENTER | mGraphics.HCENTER;
        deltaX = -20;
        mGraphics2.drawRegion(robotArm, 0, 0, 35, 40, 0, n + deltaX, n2 - 22 + deltaY, n3, false);
        mGraphics2.drawImage(robotBody, n - 1, n2 - 10 + deltaY, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        mGraphics2.drawRegion(robotArm, 0, 0, 35, 40, 0, n, n2 - 20 + deltaY, n3, false);
    }

    public static void paintUFO(mGraphics mGraphics2, int n, int n2) {
        int n3;
        if (imgUFOFrames != null && imgUFOFrames[0] != null) {
            mGraphics2.drawImage(imgUFOFrames[0], n, n2, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        } else {
            mGraphics2.drawRegion(imgUFO, 0, 0, 51, 46, 0, n, n2, 33, false);
        }
        int n4 = n3 = CCanvas.gameTick % 3 == 0 ? 0 : 1;
        if (imgUFOFireFrames != null && imgUFOFireFrames[n3] != null) {
            mGraphics2.drawImage(imgUFOFireFrames[n3], n, n2 - 5, mGraphics.TOP | mGraphics.HCENTER, false);
            return;
        }
        if (CCanvas.gameTick % 3 == 0) {
            mGraphics2.drawRegion(imgUFOFire, 0, 0, 16, 11, 0, n, n2 + -5, 17, false);
        } else {
            mGraphics2.drawRegion(imgUFOFire, 0, 11, 16, 11, 0, n, n2 - 5, 17, false);
        }
    }

    public static void paintBalloon(mGraphics mGraphics2, int n, int n2) {
        if (++tBalloon == 20) {
            tBalloon = 0;
        }
        deltaBalloon = tBalloon <= 10 ? -1 : 0;
        mGraphics2.drawImage(Background.balloon, n, n2 + deltaBalloon, 33, false);
    }

    public static void paintSimplePlayer(int n, int n2, int n3, int n4, int n5, PlayerEquip playerEquip, mGraphics mGraphics2) {
        int n6;
        int n7 = n6 = n == 3 ? 32 : 24;
        if (n == 7) {
            n6 = 32;
        }
        if (n == 12) {
            n6 = 28;
        }
        if (n == 14) {
            n6 = 30;
            n2 = 0;
        }
        if (n == 10) {
            n6 = 32;
        }
        if (n == 13) {
            CPlayer.paintBugRobot(mGraphics2, n3, n4);
        } else if (n == 15) {
            CPlayer.paintBigRobot(mGraphics2, n3, n4);
        } else if (n == 16) {
            CPlayer.paintUFO(mGraphics2, n3, n4);
        } else if (n == 17) {
            CPlayer.paintBalloon(mGraphics2, n3, n4);
        } else if (n == 22) {
            CPlayer.paintSpider(mGraphics2, n3, n4);
        } else if (n == 25) {
            CPlayer.paintGhost(mGraphics2, 0, n3, n4);
        } else if (n == 26) {
            CPlayer.paintGhost(mGraphics2, 1, n3, n4);
        } else if (playerEquip != null) {
            playerEquip.paint(mGraphics2, n5, n2, n3, n4);
        } else {
            if (pImg != null && n >= 0 && n < pImg.length && pImg[n] != null && pImg[n].image != null) {
                mGraphics2.drawRegion(pImg[n], 0, n2 * n6, CPlayer.pImg[n].image.getWidth(), n6, n5, n3, n4, mGraphics.BOTTOM | mGraphics.HCENTER, false);
            }
        }
    }

    public static void paintSpider(mGraphics mGraphics2, int n, int n2) {
        mGraphics2.setColor(0x7A7A7A);
        mGraphics2.drawRegion(spider, 0, 0, 41, 22, 1, n + 1, n2, 33, false);
        mGraphics2.drawRegion(spider, 0, 0, 41, 22, 0, n, n2 - 22, 33, false);
    }

    public static void paintGhost(mGraphics mGraphics2, int n, int n2, int n3) {
        mGraphics2.drawRegion(n == 0 ? ghost : ghost2, 0, 0, 35, 32, 0, n2, n3, 33, false);
    }

    public void resetLastUpdateXY(int n, int n2) {
        if (this.index == GameScr.myIndex) {
            this.lastUpdateX = n;
            this.lastUpdateY = n2;
        }
    }

    public void resetXYwhenNEXTTURN() {
        if (this.x != this.lastUpdateX) {
            this.x = this.lastUpdateX;
        }
        if (this.y != this.lastUpdateY) {
            this.y = this.lastUpdateY;
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (!(this instanceof Boss || isStopFire || CCanvas.currentDialog != null || GameScr.pm.isYourTurn() && this.state == 5 || isShooting || CCanvas.pausemenu.isShow)) {
            if (!CCanvas.isTouch || !GameScr.useLegacyTouchButtons()) {
                return;
            }
            ++this.t;
            if (this.t == 1) {
                if (CCanvas.isPointer(this.crossHairX, this.crossHairY, this.crossHairW, this.crossHairH, n3)) {
                    this.isHoldAngle = true;
                }
                if (GameScr.crossHair2 != null && CCanvas.isPointer(GameScr.xF - 15, GameScr.yF - 15, 50, 50, n3)) {
                    this.isHoldFire = true;
                }
            }
        }
    }

    public void onPointerDrag(int n, int n2, int n3) {
        if (!isStopFire && CCanvas.currentDialog == null) {
            if (GameScr.useLegacyTouchButtons() && CCanvas.isPointer(0, GameScr.yF - 10, CCanvas.width, CCanvas.hieght, n3)) {
                return;
            }
            if (!CCanvas.pausemenu.isShow && CCanvas.isTouch && GameScr.useLegacyTouchButtons() && this.state != 5 && !this.falling && !isShooting) {
                ++this.t;
                if (this.t == 1) {
                    if (CCanvas.isPointer(this.crossHairX, this.crossHairY, this.crossHairW, this.crossHairH, n3)) {
                        this.isHoldAngle = true;
                    }
                    if (GameScr.crossHair2 != null && CCanvas.isPointer(GameScr.xF - 15, GameScr.yF - 15, 50, 50, 0)) {
                        this.isHoldFire = true;
                    }
                }
            }
        }
    }

    public void onPointerDragRightConner(int n, int n2, int n3) {
    }

    public void onPointerHold(int n, int n2, int n3) {
        if (!isStopFire && CCanvas.currentDialog == null && this.state != 5 && !this.falling) {
            if (CCanvas.isPointerClick[n3]) {
                this.isHoldAngle = false;
                this.t = 0;
            }
            if (!isShooting && !CCanvas.pausemenu.isShow) {
                int n4 = 30;
                int n5 = 30;
                if (CCanvas.isPointer(this.crossHairX, this.crossHairY, this.crossHairW, this.crossHairH, n3)) {
                    this.isHoldAngle = true;
                }
                if (GameScr.useLegacyTouchButtons() && GameScr.crossHair2 != null && GameScr.trai != null && GameScr.phai != null) {
                    if (CCanvas.isPointer(GameScr.xF - n4 / 2, GameScr.yF - n5 / 2, GameScr.crossHair2.image.getWidth() + n4, GameScr.crossHair2.image.getHeight() + n5, n3)) {
                        this.isHoldFire = true;
                    }
                    if (CCanvas.isPointer(GameScr.xL - GameScr.trai.image.width / 2 - n4 / 2, GameScr.yL - GameScr.trai.image.height / 2 - n5 / 2, GameScr.trai.image.getWidth() + n4, GameScr.trai.image.getHeight() + n5, n3) && GameScr.pm.isYourTurn()) {
                        this.move(0);
                        return;
                    }
                    if (CCanvas.isPointer(GameScr.xR - GameScr.phai.image.width / 2 - n4 / 2, GameScr.yR - GameScr.trai.image.height / 2 - n5 / 2, GameScr.phai.image.getWidth() + n4, GameScr.phai.image.getHeight() + n5, n3) && GameScr.pm.isYourTurn()) {
                        this.move(2);
                        return;
                    }
                    if (this.isHoldFire && CCanvas.isPointer(GameScr.xF - (n4 = 30) / 2, GameScr.yF - (n5 = 30) / 2, GameScr.crossHair2.image.getWidth() + n4, GameScr.crossHair2.image.getHeight() + n5, n3) && !this.falling) {
                        if (GameScr.pm.isYourTurn()) {
                            this.holdFire();
                        }
                        return;
                    }
                }
                boolean bl = CCanvas.isPointer(0, CScreen.ITEM_HEIGHT * 2, CCanvas.w, CCanvas.h - 2 * CScreen.ITEM_HEIGHT, n3);
                boolean bl2 = CCanvas.isPointer(0, this.y + 10 - Camera.y, CCanvas.w, CCanvas.h, n3);
                this.crossHairX -= Camera.x;
                this.crossHairY -= Camera.y;
                boolean bl3 = CCanvas.isPointer(this.crossHairX, this.crossHairY, 150, 150, n3);
                if (!this.isHoldFire && this.isHoldAngle) {
                    this.look = n <= this.x - Camera.x ? 0 : 2;
                    int n6 = this.x - n - Camera.x;
                    int n7 = this.y - n2 - Camera.y;
                    int n8 = CRes.angle(-n6, n7);
                    if (this.look == 2 && n8 < angleLock[this.gun]) {
                        n8 = angleLock[this.gun];
                    }
                    if (this.look == 0 && n8 > 180 - angleLock[this.gun]) {
                        n8 = 180 - angleLock[this.gun];
                    }
                    this.angle = n8;
                    this.angleUpdate();
                    this.checkAngleForSprite();
                }
            }
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        this.isHoldAngle = false;
        if (!(CCanvas.pausemenu.isShow || GameScr.pm.isYourTurn() && this.state == 5)) {
            if (this.lastx != this.x) {
                this.resetLastUpdateXY(this.x, this.y);
                GameService.gI().move((short)this.x, (short)this.y);
            }
            this.lastx = (short)this.x;
            if (this.isHoldFire) {
                this.fire();
                this.isHoldFire = false;
            } else {
                this.setState((byte)0);
            }
        }
    }

    public void drawKegoc(mGraphics mGraphics2) {
        if (mGraphics2 == null) {
            return;
        }
        try {
            byte bulletType = this.resolveCurrentShotBulletType();
            int maxF = this.maxforce > 0 ? this.maxforce : 30;
            int maxF2 = this.maxforce2 > 0 ? this.maxforce2 : 30;
            boolean isPow = this.isAngry || this.itemUsed == 100;

            short[][][] fullPaths = OfflineBulletSim.buildAllPaths(this, bulletType, (byte)maxF, (byte)maxF2, isPow);

            if (fullPaths != null && fullPaths.length > 0) {
                for (int p = 0; p < fullPaths.length; ++p) {
                    if (fullPaths[p] == null || fullPaths[p].length < 2 || fullPaths[p][0] == null || fullPaths[p][1] == null) {
                        continue;
                    }
                    short[] xs = fullPaths[p][0];
                    short[] ys = fullPaths[p][1];
                    int len = Math.min(xs.length, ys.length);
                    if (len < 2) {
                        continue;
                    }

                    int colorCore = (p == 0) ? (isPow ? 0xFF3300 : 0xFFFF00) : 0x00E5FF;

                    // 1. Sleek 1px line
                    for (int i = 1; i < len; ++i) {
                        mGraphics2.setColor(colorCore);
                        mGraphics2.drawLine(xs[i - 1], ys[i - 1], xs[i], ys[i], false);
                    }

                    // 2. Main trajectory: partition forces 1..30 and draw force numbers on the line
                    if (p == 0) {
                        int totalPathDist = 0;
                        for (int i = 1; i < len; ++i) {
                            int dX = xs[i] - xs[i - 1];
                            int dY = ys[i] - ys[i - 1];
                            totalPathDist += (int)Math.sqrt(dX * dX + dY * dY);
                        }
                        int distPerForce = totalPathDist / Math.max(1, maxF);

                        for (int f = 1; f <= maxF; ++f) {
                            int idx = (int)((long)f * (len - 1) / maxF);
                            if (idx < 0) {
                                idx = 0;
                            }
                            if (idx >= len) {
                                idx = len - 1;
                            }

                            short fx = xs[idx];
                            short fy = ys[idx];

                            int p0 = Math.max(0, idx - 1);
                            int p1 = Math.min(len - 1, idx + 1);
                            int dx = xs[p1] - xs[p0];
                            int dy = ys[p1] - ys[p0];
                            int hyp = (int)Math.sqrt(dx * dx + dy * dy);
                            if (hyp == 0) {
                                hyp = 1;
                            }

                            int nx = -dy * 3 / hyp;
                            int ny = dx * 3 / hyp;

                            boolean isMajor = (f % 5 == 0) || (f == maxF);

                            if (isMajor) {
                                mGraphics2.setColor(0xFFFFFF);
                                mGraphics2.drawLine(fx - nx * 4 / 3, fy - ny * 4 / 3, fx + nx * 4 / 3, fy + ny * 4 / 3, false);
                                mGraphics2.fillRect(fx - 1, fy - 1, 3, 3, false);
                            } else {
                                mGraphics2.setColor(isPow ? 0xFF8866 : 0xFFEE77);
                                mGraphics2.drawLine(fx - nx, fy - ny, fx + nx, fy + ny, false);
                            }

                            boolean showNumber = false;
                            if (distPerForce >= 14) {
                                showNumber = true;
                            } else if (distPerForce >= 7) {
                                showNumber = (f % 2 == 0) || isMajor;
                            } else {
                                showNumber = isMajor;
                            }

                            if (showNumber && idx < len - 2) {
                                int textX = fx;
                                int textY = fy - 10;
                                if (isMajor) {
                                    Font.smallFontYellow.drawString(mGraphics2, String.valueOf(f), textX, textY, 2);
                                } else {
                                    Font.smallFont.drawString(mGraphics2, String.valueOf(f), textX, textY, 2);
                                }
                            }
                        }
                    }

                    // 3. Compact neat landing marker
                    short lastX = xs[len - 1];
                    short lastY = ys[len - 1];

                    mGraphics2.setColor(isPow ? 0xFF0033 : 0xFF2200);
                    mGraphics2.drawRect(lastX - 2, lastY - 2, 4, 4, false);
                    mGraphics2.setColor(0xFFFF00);
                    mGraphics2.drawLine(lastX - 3, lastY, lastX + 3, lastY, false);
                    mGraphics2.drawLine(lastX, lastY - 3, lastX, lastY + 3, false);
                    mGraphics2.fillRect(lastX - 1, lastY - 1, 3, 3, false);
                }
                return;
            }
        } catch (Exception exception) {
        }

        int n = 24;
        int n2 = 24;
        int startX = this.x + ((n - 4) * CRes.cos(this.angle) >> 10);
        int startY = this.y - n2 / 2 - ((n2 - 4) * CRes.sin(this.angle) >> 10);
        int maxF = this.maxforce > 0 ? this.maxforce : 30;
        int vx = maxF * CRes.cos(this.angle) >> 10;
        int vy = -(maxF * CRes.sin(this.angle) >> 10);

        int curX = startX;
        int curY = startY;
        for (int i = 0; i < 300; ++i) {
            int nextX = curX + vx;
            int nextY = curY + vy;
            if (nextX < -50 || nextX > MM.mapWidth + 50 || nextY > MM.mapHeight + 50) {
                break;
            }
            mGraphics2.setColor(0xFFFF00);
            mGraphics2.drawLine(curX, curY, nextX, nextY, false);
            if (GameScr.mm != null && GameScr.mm.isLand(nextX, nextY)) {
                mGraphics2.setColor(0xFF0000);
                mGraphics2.drawRect(nextX - 2, nextY - 2, 4, 4, false);
                mGraphics2.setColor(0xFFFF00);
                mGraphics2.fillRect(nextX - 1, nextY - 1, 3, 3, false);
                break;
            }
            curX = nextX;
            curY = nextY;
            vy += 1;
        }
    }

    static {
        isGetPosition = false;
        pImg = new mImage[25];
        lua = Smoke.lua;
        angleLock = null;
        angleLockMain = null;
    }
}

