/*
 * Decompiled with CFR 0.152.
 */
package map;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import coreLG.CONFIG;
import effect.Camera;
import effect.Snow;
import map.MM;
import model.CRes;
import model.FilePack;
import model.FrameImage;
import model.IAction2;
import player.CPlayer;
import screen.CScreen;
import screen.GameScr;
import screen.PrepareScr;

public class Background {
    public static final byte BACKGR_HALONG = 0;
    public static final byte BACKGR_RUONGLUA = 1;
    public static final byte BACKGR_SIMPLESKY = 2;
    public static final byte BACKGR_RUNGRAM = 3;
    public static final byte BACKGR_HOANGTAN = 4;
    public static final byte BACKGR_SONGNUI = 5;
    public static final byte BACKGR_CITY = 6;
    public static final byte BACKGR_ICE = 7;
    public static final byte BACKGR_FORREST = 8;
    public static final byte BACKGR_BOSS_1 = 9;
    public static final byte BACKGR_CITY_NIGHT = 10;
    public static final byte BACKGR_NIGHT_FORREST = 12;
    public static final byte BACKGR_CAVE = 13;
    public static final byte BACKGR_CLOUD = 14;
    public static final byte BACKGR_GREY = 15;
    public static mImage sun;
    public static mImage sun2;
    public static mImage haLongbg;
    public static mImage cloud;
    public static mImage water;
    public static mImage inWater;
    public static mImage canhdong;
    public static mImage co;
    public static mImage co2;
    public static mImage rungRam;
    public static mImage bang;
    public static mImage back01;
    public static mImage back02;
    public static mImage back03;
    public static mImage back04;
    public static mImage back05;
    public static mImage back06;
    public static mImage back07;
    public static mImage back08;
    public static mImage back11;
    public static mImage back12;
    public static mImage back14;
    public static mImage back15;
    public static mImage back16;
    public static mImage back17;
    public static mImage thaprua;
    public static mImage balloon;
    public static mImage a;
    public static mImage b;
    public static mImage bigBalloon;
    public static mImage mocxich;
    public static mImage bg_cloud;
    public static mImage bg_cloud_1;
    public static mImage logo;
    public static mImage[] may;
    public static mImage rock_up;
    public static mImage map_spider_layout;
    public static mImage rock_down;
    public static mImage stone;
    public static boolean isLoadImage;
    public static byte curBGType;
    public int yBackGr = 0;
    public int yCloud = 130;
    static FrameImage waterSp;
    static int skyLine;
    static int sunX;
    static int sunY;
    static int nBgX;
    static int nBgY;
    static int nBgX2;
    public static int waterY;
    public static int glassY;
    int[] cloudx;
    int[] cloudy;
    int[] cloudz;
    int[] cloudx2;
    int[] cloudy2;
    static int nWave;
    static int[] wavex;
    static int[] wavex2;
    static int[] wavey;
    static int[] length;
    static int[] delay;
    Snow snow;
    boolean boltActive;
    int tBolt;
    int wLazer = 8;
    boolean changeSign;
    int limit = 613;
    int[] t = new int[10];
    static int[] xT;
    static int[] yT;

    public void loadImage(int n) {
        try {
            switch (n) {
                case 0: {
                    haLongbg = mImage.createImage("/map/bgItem/halongkaka.png");
                    break;
                }
                case 1: {
                    canhdong = mImage.createImage("/map/bgItem/canhdong.png");
                    break;
                }
                case 2: {
                    bang = mImage.createImage("/map/bgItem/bang.png");
                    break;
                }
                case 3: {
                    rungRam = mImage.createImage("/map/bgItem/rungRam.png");
                    break;
                }
                case 4: {
                    back01 = mImage.createImage("/map/bgItem/back1.png");
                    back02 = mImage.createImage("/map/bgItem/back2.png");
                    break;
                }
                case 5: {
                    back03 = mImage.createImage("/map/bgItem/back3.png");
                    back04 = mImage.createImage("/map/bgItem/back4.png");
                    break;
                }
                case 6: {
                    back05 = mImage.createImage("/map/bgItem/back5.png");
                    back06 = mImage.createImage("/map/bgItem/back6.png");
                    break;
                }
                case 7: {
                    back07 = mImage.createImage("/map/bgItem/back7.png");
                    back08 = mImage.createImage("/map/bgItem/back8.png");
                }
                default: {
                    break;
                }
                case 9: {
                    back05 = mImage.createImage("/map/bgItem/back5.png");
                    back06 = mImage.createImage("/map/bgItem/back6.png");
                    break;
                }
                case 10: {
                    back11 = mImage.createImage("/map/bgItem/back11.png");
                    back12 = mImage.createImage("/map/bgItem/back12.png");
                    break;
                }
                case 12: {
                    back14 = mImage.createImage("/map/bgItem/back14.png");
                    back15 = mImage.createImage("/map/bgItem/back15.png");
                    break;
                }
                case 13: {
                    rock_up = mImage.createImage("/map/bgItem/rock_up.png");
                    map_spider_layout = mImage.createImage("/map/bgItem/map_spider_layout.png");
                    rock_down = mImage.createImage("/map/bgItem/rock_down.png");
                    break;
                }
                case 14: {
                    bg_cloud = mImage.createImage("/map/bgItem/bg-cloud.png");
                    bg_cloud_1 = mImage.createImage("/map/bgItem/bg_cloud1.png");
                    break;
                }
                case 15: {
                    back16 = mImage.createImage("/map/bgItem/back16.png");
                    back17 = mImage.createImage("/map/bgItem/back17.png");
                    break;
                }
            }
        }
        catch (Exception exception) {
        }
    }

    public static void removeImage() {
        haLongbg = null;
        canhdong = null;
        rungRam = null;
        bang = null;
        back01 = null;
        back02 = null;
        back03 = null;
        back04 = null;
        back05 = null;
        back06 = null;
        back07 = null;
        back08 = null;
        back11 = null;
        back12 = null;
        back14 = null;
        back15 = null;
        back16 = null;
        back17 = null;
        bg_cloud = null;
        bg_cloud_1 = null;
        rock_up = null;
        map_spider_layout = null;
        rock_down = null;
    }

    public static void initImage() {
    }

    public Background(byte by) {
        Background.removeImage();
        this.cloudx = new int[]{52, 110, 250};
        this.cloudy = new int[]{100, 180, 150};
        this.cloudz = new int[]{45, 40, 50};
        this.cloudx2 = new int[]{100, 200, 300};
        this.cloudy2 = new int[]{80, 50, 100};
        skyLine = CScreen.h - 135;
        curBGType = by;
        waterSp = new FrameImage(Background.water.image, 24, 24);
        glassY = MM.mapHeight - Background.co.image.getHeight();
        sunX = CScreen.w - 60;
        sunY = skyLine - 75;
        switch (by) {
            case 0: {
                nBgX = CScreen.w / 128;
                break;
            }
            case 1: {
                nBgX = CScreen.w / 72;
                break;
            }
            case 2: {
                nBgX = CScreen.w / 64;
                break;
            }
            case 3: {
                nBgX = CScreen.w / 72;
                break;
            }
            case 4: {
                nBgX = CScreen.w / 241;
                nBgX2 = CScreen.w / 226;
                break;
            }
            case 5: {
                nBgX = CScreen.w / 241;
                break;
            }
            case 6:
            case 10: {
                nBgX = CScreen.w / 238;
                nBgX2 = CScreen.w / 225;
                break;
            }
            case 7: {
                nBgX = GameScr.w / 219;
                nBgX2 = GameScr.w / 218;
                break;
            }
            case 8: {
                nBgX = GameScr.w / 219;
                nBgX2 = GameScr.w / 210;
            }
            default: {
                break;
            }
            case 12: {
                nBgX = CScreen.w / 108;
                nBgX2 = CScreen.w / 108;
                this.snow = new Snow();
                this.snow.min = 300;
                this.snow.max = 400;
                this.snow.vymin = 5;
                this.snow.vymax = 7;
                this.snow.vxmin = 3;
                this.snow.waterY = 150;
                this.snow.startSnow(1);
                this.snow.waterY = -50;
                break;
            }
            case 13: {
                nBgX = GameScr.w / 219;
                nBgX2 = GameScr.w / 218;
                break;
            }
            case 14: {
                nBgX = GameScr.w / 128;
                nBgX2 = GameScr.w / 128;
                break;
            }
            case 15: {
                nBgX = GameScr.w / 241;
                nBgX = GameScr.w / 241;
            }
        }
        this.loadImage(by);
    }

    public void update() {
        if (GameScr.curGRAPHIC_LEVEL != 2 && curBGType != 3) {
            this.updateCloud();
        }
        if (this.snow != null) {
            this.snow.update();
        }
    }

    private void updateCloud() {
        for (int i = 0; i < this.cloudx.length; ++i) {
            int[] nArray = this.cloudx;
            int n = i;
            nArray[n] = nArray[n] - (Camera.x - Camera.startx) * this.cloudz[i] / 100;
            nArray = this.cloudy;
            int n2 = i;
            nArray[n2] = nArray[n2] - (Camera.y - Camera.starty) * this.cloudz[i] / 100;
            nArray = this.cloudx2;
            int n3 = i;
            nArray[n3] = nArray[n3] - (Camera.x - Camera.startx) * this.cloudz[i] / 100;
            nArray = this.cloudy2;
            int n4 = i;
            nArray[n4] = nArray[n4] - (Camera.y - Camera.starty) * this.cloudz[i] / 100;
        }
    }

    public void updateWave() {
        for (int i = 0; i < nWave; ++i) {
            if (delay[i] > 50) {
                Background.wavex[i] = CRes.random(0, CScreen.w - 10);
                Background.wavey[i] = CRes.random(skyLine, CScreen.h);
                Background.length[i] = CRes.random(1, 5);
                Background.delay[i] = 0;
                continue;
            }
            int n = i;
            delay[n] = delay[n] + 1;
        }
    }

    public void paint(mGraphics mGraphics2) {
        this.drawBackGround(curBGType, mGraphics2);
    }

    private void drawBackGround(byte by, mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        switch (by) {
            case 0: {
                mGraphics2.setColor(8831994);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL != 2) {
                    int n = (Camera.x >> 2) % 128;
                    int n2 = CScreen.h - (Camera.y + CScreen.h >> 2) + this.yBackGr;
                    mGraphics2.setColor(2002158);
                    mGraphics2.fillRect(0, n2, CScreen.w, CScreen.h - n2, false);
                    for (int i = 0; i <= nBgX + 1; ++i) {
                        mGraphics2.drawImage(haLongbg, -n + i * 128, n2, mGraphics.LEFT | mGraphics.VCENTER, false);
                    }
                    mGraphics2.drawImage(sun, sunX, n2 - 100, 0, false);
                    mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                    this.drawCloud(mGraphics2);
                    break;
                }
                mGraphics2.setColor(8438010);
                mGraphics2.fillRect(0, 0, CScreen.w, CScreen.h, false);
                break;
            }
            case 1: {
                mGraphics2.setColor(8180459);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n = (Camera.x >> 2) % 64;
                int n3 = CScreen.h - (Camera.y + CScreen.h >> 2) + this.yBackGr;
                for (int i = 0; i <= nBgX + 1; ++i) {
                    mGraphics2.drawImage(canhdong, -n + i * 64, n3, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.drawImage(sun, sunX, n3 - 200, 0, false);
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                this.drawCloud(mGraphics2);
                break;
            }
            case 2: {
                mGraphics2.setColor(8180459);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n = (Camera.x >> 2) % 64;
                int n4 = CScreen.h - (Camera.y + CScreen.h >> 2) + 150 + this.yBackGr;
                for (int i = 0; i <= nBgX + 1; ++i) {
                    mGraphics2.drawImage(bang, -n + i * 64, n4, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.drawImage(sun, sunX, n4 - 200, 0, false);
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                this.drawCloud(mGraphics2);
                mGraphics2.setColor(18797);
                mGraphics2.fillRect(0, n4, CCanvas.width, 500, false);
                break;
            }
            case 3: {
                mGraphics2.setColor(8711932);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n = (Camera.x >> 2) % 72;
                int n5 = CScreen.h - (Camera.y + CScreen.h >> 2) + this.yBackGr;
                for (int i = 0; i <= nBgX + 1; ++i) {
                    mGraphics2.drawImage(rungRam, -n + i * 72, n5, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                break;
            }
            case 4: {
                int n;
                mGraphics2.setColor(14282750);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n6 = (Camera.x >> 3) % 241;
                int n7 = (Camera.x >> 2) % 226;
                int n8 = CScreen.h - (Camera.y + CScreen.h >> 2) + this.yBackGr;
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back02, -n6 + n * 241, n8 - 5, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                for (n = 0; n <= nBgX2 + 1; ++n) {
                    mGraphics2.drawImage(back01, -n7 + n * 226, n8, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(0x717171);
                mGraphics2.fillRect(0, n8, CCanvas.width, 100, false);
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                this.drawCloud(mGraphics2);
                break;
            }
            case 5: {
                int n;
                mGraphics2.setColor(0xFFF7F0);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n9 = (Camera.x >> 3) % 241;
                int n10 = (Camera.x >> 2) % 241;
                int n11 = CScreen.h - Camera.y / 2 + this.yBackGr;
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back04, -n9 + n * 241, n11, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back03, -n10 + n * 241, n11, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(7905231);
                mGraphics2.fillRect(0, n11, CCanvas.width, 100, false);
                break;
            }
            case 6: {
                int n;
                mGraphics2.setColor(16706268);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n12 = (Camera.x >> 3) % 238;
                int n13 = (Camera.x >> 2) % 225;
                int n14 = CScreen.h - Camera.y / 2 + this.yBackGr;
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back06, -n12 + n * 238, n14, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back05, -n13 + n * 225, n14, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(10268132);
                mGraphics2.fillRect(0, n14, CCanvas.width, 600, false);
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                this.drawCloud(mGraphics2);
                break;
            }
            case 7: {
                mGraphics2.setColor(15267327);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL != 2) {
                    int n = (Camera.x >> 3) % 269;
                    int n15 = (Camera.x >> 2) % 368;
                    int n16 = CScreen.h - Camera.y / 3 + this.yBackGr - 100;
                    int n17 = CScreen.h - Camera.y / 2 + this.yBackGr;
                    for (n17 = 0; n17 <= nBgX + 1; ++n17) {
                        mGraphics2.drawImage(back08, -n + n17 * 269, n16, mGraphics.LEFT | mGraphics.BOTTOM, false);
                    }
                    for (n17 = 0; n17 <= nBgX + 1; ++n17) {
                        mGraphics2.drawImage(back08, -n + n17 * 269 + 30, n16 + 150, mGraphics.LEFT | mGraphics.BOTTOM, false);
                    }
                    for (n17 = 0; n17 <= nBgX + 1; ++n17) {
                        mGraphics2.drawImage(back07, -n15 + n17 * 368 + 50, n17, mGraphics.LEFT | mGraphics.BOTTOM, false);
                    }
                }
            }
            default: {
                break;
            }
            case 9: {
                int n;
                mGraphics2.setColor(16752448);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n18 = (Camera.x >> 3) % 238;
                int n19 = (Camera.x >> 2) % 225;
                int n20 = CScreen.h - Camera.y / 2 + this.yBackGr;
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back06, -n18 + n * 238, n20, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back05, -n19 + n * 225, n20, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(0xBCCCFF);
                mGraphics2.fillRect(0, n20, CCanvas.width, 100, false);
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                this.drawCloud(mGraphics2);
                break;
            }
            case 10: {
                int n;
                mGraphics2.setColor(7106965);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n21 = (Camera.x >> 3) % 238;
                int n22 = (Camera.x >> 2) % 225;
                int n23 = CScreen.h - Camera.y / 2 + this.yBackGr;
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back12, -n21 + n * 238, n23, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back11, -n22 + n * 225, n23, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(3093573);
                mGraphics2.fillRect(0, n23, CCanvas.width, 600, false);
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                break;
            }
            case 12: {
                int n;
                if (CCanvas.gameTick % 200 == 0) {
                    this.boltActive = true;
                }
                mGraphics2.setColor(530454);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n24 = (Camera.x >> 4) % 108;
                int n25 = (Camera.x >> 3) % 108;
                int n26 = CScreen.h - (Camera.y + CScreen.h >> 2) + this.yBackGr;
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back15, -n24 + n * 108, n26, mGraphics.LEFT | mGraphics.VCENTER, false);
                }
                if (this.boltActive) {
                    ++this.tBolt;
                    if (this.tBolt == 10) {
                        this.tBolt = 0;
                        this.boltActive = false;
                    }
                    if (this.tBolt % 2 == 0) {
                        mGraphics2.setColor(0xFFFFFF);
                        mGraphics2.fillRect(0, n26 - 60, CCanvas.width, 130, false);
                    }
                }
                if (this.snow != null) {
                    mGraphics2.setClip(0, n26 - 50, 1000, 120);
                    this.snow.paintOnlySmall(mGraphics2);
                    mGraphics2.resetClip();
                }
                for (n = 0; n <= nBgX + 1; ++n) {
                    mGraphics2.drawImage(back14, -n25 + n * 108, n26, mGraphics.LEFT | mGraphics.VCENTER, false);
                }
                mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY() + 1);
                break;
            }
            case 13: {
                mGraphics2.setColor(8229794);
                mGraphics2.fillRect(0, 0, CScreen.w + 10, CScreen.h + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n = (Camera.x >> 2) % 144;
                int n27 = (Camera.x >> 2) % 176;
                int n28 = (Camera.x >> 3) % 69;
                int n29 = CScreen.h - Camera.y / 3 + this.yBackGr;
                n29 = CScreen.h - Camera.y / 4 + this.yBackGr - 80;
                for (n29 = 0; n29 <= CCanvas.width / 69 + 1; ++n29) {
                    mGraphics2.drawImage(map_spider_layout, -n28 + n29 * 69, n29, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(9728620);
                mGraphics2.fillRect(0, n29, CCanvas.width, 300, false);
                for (n29 = 0; n29 <= CCanvas.width / 144 + 1; ++n29) {
                    mGraphics2.drawImage(rock_down, -n + n29 * 144, n29, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(0x313131);
                mGraphics2.fillRect(0, -500, CCanvas.width, -Camera.y / 3 + 120 + 500 - 88, false);
                for (n29 = 0; n29 <= CCanvas.width / 176 + 1; ++n29) {
                    mGraphics2.drawImage(rock_up, -n27 + n29 * 176, -Camera.y / 3 + 120, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(4996403);
                mGraphics2.fillRect(0, n29, CCanvas.width, 300, false);
                break;
            }
            case 14: {
                int n;
                mGraphics2.setColor(6606845);
                mGraphics2.fillRect(0, 0, CCanvas.width + 10, CCanvas.hieght + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                int n30 = (Camera.x >> 2) % 128;
                int n31 = (Camera.x >> 3) % 128;
                int n32 = CCanvas.hieght;
                for (n = 0; n <= CCanvas.width / 128 + 1; ++n) {
                    mGraphics2.drawImage(bg_cloud_1, -n31 + n * 128, n32 + 10, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                for (n = 0; n <= CCanvas.width / 128 + 1; ++n) {
                    mGraphics2.drawImage(bg_cloud, -n30 + n * 128, n32, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                this.drawSun(mGraphics2);
                break;
            }
            case 15: {
                int n;
                mGraphics2.setColor(16694934);
                mGraphics2.fillRect(0, 0, CCanvas.width + 10, CCanvas.hieght + 10, false);
                if (GameScr.curGRAPHIC_LEVEL == 2) break;
                this.drawSun2(mGraphics2);
                int n33 = (Camera.x >> 2) % 241;
                int n34 = (Camera.x >> 3) % 241;
                int n35 = CCanvas.hieght - Camera.y / 3 - this.yBackGr;
                int n36 = CCanvas.hieght - 10 - Camera.y / 4 - this.yBackGr;
                for (n = 0; n <= CCanvas.width / 241 + 1; ++n) {
                    mGraphics2.drawImage(back16, -n34 + n * 241, n36, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(7628133);
                mGraphics2.fillRect(0, n36, CCanvas.width, 200, false);
                for (n = 0; n <= CCanvas.width / 241 + 1; ++n) {
                    mGraphics2.drawImage(back17, -n33 + n * 241, n35, mGraphics.LEFT | mGraphics.BOTTOM, false);
                }
                mGraphics2.setColor(0x260606);
                mGraphics2.fillRect(0, n35, CCanvas.width, 200, false);
            }
        }
        mGraphics2.translate(-Camera.x, -Camera.y);
    }

    public void drawSun(mGraphics mGraphics2) {
        mGraphics2.drawImage(sun, sunX, sunY, 0, false);
    }

    public void drawSun2(mGraphics mGraphics2) {
        mGraphics2.drawImage(sun2, sunX, sunY, 0, false);
    }

    public void drawWave(mGraphics mGraphics2) {
        if (GameScr.curGRAPHIC_LEVEL == 0) {
            mGraphics2.setColor(0xFFFFFF);
            for (int i = 0; i < nWave; ++i) {
                mGraphics2.drawLine(wavex[i], wavey[i], wavex[i] + length[i], wavey[i], false);
            }
        }
    }

    private void drawCloud(mGraphics mGraphics2) {
        for (int i = 0; i < this.cloudx.length; ++i) {
            mGraphics2.drawImage(cloud, this.cloudx[i], this.cloudy[i] + this.yCloud, 0, false);
        }
    }

    public static void drawWater(byte by, mGraphics mGraphics2) {
        block4: {
            block5: {
                block3: {
                    if (by != 0) break block3;
                    int n = Camera.x / 24;
                    int n2 = (Camera.x + CScreen.w) / 24;
                    for (int i = n; i <= n2; ++i) {
                        waterSp.drawFrame(CCanvas.gameTick % 8 > 4 ? 0 : 1, i * 24, waterY - 12 + Background.inWater.image.getHeight() / 2, 0, 0, mGraphics2);
                        mGraphics2.drawImage(inWater, i * 24, waterY - 12 + 24 + Background.inWater.image.getHeight() / 2, 0, false);
                    }
                    break block4;
                }
                if (by != 1) break block5;
                int n = Camera.x / 64;
                int n3 = (Camera.x + CScreen.w) / 64;
                for (int i = n; i <= n3; ++i) {
                    mGraphics2.drawImage(co, i * 64, glassY, 0, false);
                }
                break block4;
            }
            if (by != 2) break block4;
            int n = Camera.x / 64;
            int n4 = (Camera.x + CScreen.w) / 64;
            for (int i = n; i <= n4; ++i) {
                mGraphics2.drawImage(co2, i * 64, glassY, 0, false);
            }
        }
    }

    public static void paintTree(mGraphics mGraphics2) {
        for (int i = 0; i < 7; ++i) {
            if (xT[i] >= 0 && xT[i] <= CCanvas.width) {
                mGraphics2.drawImage(b, xT[i], yT[i], mGraphics.HCENTER | mGraphics.VCENTER, false);
            }
            for (int j = 0; j < (CCanvas.hieght - yT[i]) / 21 + 1; ++j) {
                mGraphics2.drawImage(a, xT[i], yT[i] + j * 21 + Background.b.image.getHeight() / 2, mGraphics.TOP | mGraphics.HCENTER, false);
            }
        }
    }

    public static void paintMenuBackGround(mGraphics mGraphics2) {
        mGraphics2.setColor(6606845);
        mGraphics2.fillRect(0, 0, CCanvas.width, CCanvas.hieght, false);
        for (int i = 0; i <= CCanvas.width; i += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, i, CCanvas.hieght - 62, 0, false);
        }
    }

    public void onClearMap() {
    }

    static {
        int n;
        FilePack filePack;
        isLoadImage = false;
        try {
            filePack = new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_MAP + "bg"));
            PrepareScr.rockImg = filePack.loadImage("rock1.png");
            PrepareScr.rock2Img = filePack.loadImage("rock2.png");
            PrepareScr.glassFly = filePack.loadImage("cobay.png");
            PrepareScr.cloud1 = filePack.loadImage("cloud1.png");
            PrepareScr.chickenHair = filePack.loadImage("longga.png");
            cloud = filePack.loadImage("cl2.png");
            sun = filePack.loadImage("sun0.png");
            sun2 = filePack.loadImage("sun1.png");
            water = filePack.loadImage("wts.png");
            co = filePack.loadImage("co.png");
            logo = filePack.loadImage("lg.png", new IAction2(){

                public void perform(Object object) {
                    CRes.out("===> isData");
                }
            });
            CPlayer.web = filePack.loadImage("web.png");
            co2 = filePack.loadImage("co2.png");
            inWater = filePack.loadImage("inWater.png");
            may = new mImage[3];
            PrepareScr.imgSun = filePack.loadImage("sun0.png");
            PrepareScr.imgCloud = new mImage[3];
            for (n = 0; n < 3; ++n) {
                PrepareScr.imgCloud[n] = filePack.loadImage("cl" + (n + 1) + ".png");
            }
            balloon = filePack.loadImage("miniballoon.png");
            a = filePack.loadImage("a.png");
            b = filePack.loadImage("b.png");
            bigBalloon = filePack.loadImage("bigballoon.png");
            mocxich = filePack.loadImage("mocxich.png");
            stone = filePack.loadImage("stone.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        filePack = null;
        nWave = 10;
        wavex = new int[nWave];
        wavex2 = new int[nWave];
        wavey = new int[nWave];
        length = new int[nWave];
        delay = new int[]{1, 1, 10, 10, 20, 20, 30, 30, 40, 40, 40};
        xT = new int[]{CCanvas.width / 2 - 150, CCanvas.width / 2 - 110, CCanvas.width / 2 - 80, CCanvas.width / 2 - 10, CCanvas.width / 2 + 90, CCanvas.width / 2 + 110, CCanvas.width / 2 + 140};
        n = CCanvas.hieght >= 320 ? CCanvas.hieght - 320 : -(320 - CCanvas.hieght);
        yT = new int[]{221 + n, 201 + n, 240 + n, 271 + n, 223 + n, 265 + n, 243 + n};
    }
}

