/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import coreLG.CONFIG;
import effect.Camera;
import map.MM;
import model.CRes;
import model.FilePack;
import model.FrameImage;
import model.IAction2;
import screen.GameScr;

public class Smoke {
    FrameImage frmImg;
    int x;
    int y;
    int vx;
    int vy;
    int endY;
    int curFrame;
    int delay;
    boolean isSmallSmoke;
    int smallSmokeDelay;
    boolean isWaterBum;
    public static mImage smoke;
    public static mImage smoke2;
    public static mImage blackSmokeImg;
    public static mImage rockImg;
    public static mImage rock2Img;
    public static mImage glassFly;
    public static mImage chickenHair;
    public static mImage lacay;
    public static mImage blueSmoke;
    public static mImage lazerSmoke;
    public static mImage chuotBay;
    public static mImage gatruilong;
    public static mImage water;
    public static mImage imgTornado;
    public static mImage explode;
    public static mImage glassFly2;
    public static mImage smokeNuke;
    public static mImage bat;
    public static mImage star;
    public static mImage smokeFire;
    public static mImage lua;
    public static mImage wind;
    public static FilePack filePack;
    byte type;
    public static final byte SMOKE = 0;
    public static final byte BLACK_SMOKE = 1;
    public static final byte ROCK = 2;
    public static final byte ROCK_2 = 3;
    public static final byte BLACKSMOKE_EX = 4;
    public static final byte SMALL_BLACKSMOKE_EX = 5;
    public static final byte GLASS_FLY = 6;
    public static final byte REDSMOKE = 7;
    public static final byte CHICKEN_HAIR = 8;
    public static final byte CHUOT = 9;
    public static final byte GA = 10;
    public static final byte WATER = 11;
    public static final byte LACAY = 12;
    public static final byte LAZER_SMOKE = 13;
    public static final byte GLASS_FLY_2 = 14;
    public static final byte NUKE_SMOKE = 15;
    public static final byte LAZER = 16;
    public static final byte BAT = 17;
    public static final byte SMOKE_BG = 18;
    public static final byte LONG_SMOKE = 19;
    public static final byte LUCKY = 20;
    public static final byte FIRE_SMOKE = 21;
    public static final byte WIND = 22;
    private static FrameImage[] frameImages;
    int xLazer;
    int yLazer;
    int num;
    int[] smokeX;
    int[] smokeY;
    int[] smokeRadius;
    int smokeHeight;
    int typeLazer;
    public static final int REDLAZER = 0;
    public static final int GREENLAZER = 1;
    public int xBat;
    public int yBat;
    public int angle;
    public int tWind;
    int va;
    public boolean isStop = false;
    public int timeStop;
    boolean activeLazer;
    int wLazer;

    public Smoke(int n, int n2, int n3, int n4, int n5) {
        this.xLazer = n;
        this.yLazer = n2;
        this.x = n3;
        this.y = n4;
        this.typeLazer = n5;
        this.delay = 0;
        this.type = (byte)16;
        this.activeLazer = true;
        if (n5 == 0) {
            this.wLazer = 25;
        }
        if (n5 == 1) {
            this.wLazer = 15;
        }
    }

    public Smoke(int n, int n2, int n3, int n4) {
        this.x = n;
        this.y = n2;
        this.xBat = n3;
        this.yBat = n4;
        this.angle = 90;
        this.va = 256;
        this.type = (byte)17;
        this.frmImg = new FrameImage(Smoke.bat.image, 19, 19);
    }

    public Smoke(int n, int n2, byte by) {
        this.x = n;
        this.y = n2;
        this.endY = n2 - CRes.random(60, 80);
        this.delay = 0;
        this.type = by;
        switch (by) {
            case 0: {
                this.frmImg = frameImages[by];
                break;
            }
            case 1: {
                this.frmImg = frameImages[by];
            }
            default: {
                break;
            }
            case 4:
            case 5:
            case 18: {
                int n3 = 10;
                if (by == 5) {
                    n3 = 6;
                }
                if (GameScr.curGRAPHIC_LEVEL == 1) {
                    n3 = (byte)(n3 - 4);
                } else if (GameScr.curGRAPHIC_LEVEL == 2) {
                    n3 = (byte)(n3 - 6);
                }
                if (n3 < 3) {
                    n3 = 3;
                }
                this.num = CRes.random(n3 / 2, n3);
                this.vx = GameScr.windx / 12;
                this.vy = CRes.random(-3, -1);
                this.smokeHeight = 25;
                this.smokeX = new int[this.num];
                this.smokeY = new int[this.num];
                this.smokeRadius = new int[this.num];
                int n4 = 20;
                int n5 = 16;
                if (by == 5) {
                    n4 = 4;
                    n5 = 8;
                }
                for (int i = 0; i < this.num; ++i) {
                    this.smokeX[i] = CRes.random(n - n4, n + n4);
                    this.smokeY[i] = CRes.random(n2 - n4 / 2, n2 + n4 / 2);
                    this.smokeRadius[i] = CRes.random(6, n5);
                }
                return;
            }
            case 7: {
                this.frmImg = frameImages[by];
                break;
            }
            case 11: {
                this.frmImg = frameImages[by];
                break;
            }
            case 13: {
                this.frmImg = frameImages[by];
                break;
            }
            case 15: {
                this.frmImg = frameImages[by];
                break;
            }
            case 16: {
                this.xLazer = CRes.random(Camera.x, Camera.x + CCanvas.width);
                this.yLazer = Camera.y;
                this.activeLazer = true;
                break;
            }
            case 19: {
                this.curFrame = CRes.random(0, 4);
                this.frmImg = frameImages[by];
                break;
            }
            case 21: {
                this.curFrame = CRes.random(1, 2);
                this.frmImg = frameImages[by];
                break;
            }
            case 22: {
                this.frmImg = new FrameImage(Smoke.wind.image, 32, 32);
            }
        }
    }

    public Smoke(int n, int n2, int n3, int n4, byte by) {
        this.x = n;
        this.y = n2;
        this.vx = n3;
        this.vy = n4;
        this.type = by;
        if (by == 3) {
            this.frmImg = frameImages[by];
        } else if (by == 6) {
            this.frmImg = frameImages[by];
        } else if (by == 8) {
            this.frmImg = frameImages[by];
        } else if (by == 12) {
            this.frmImg = frameImages[by];
        } else if (by == 9) {
            this.frmImg = frameImages[by];
        } else if (by == 10) {
            this.frmImg = frameImages[by];
        } else if (by == 14) {
            this.frmImg = frameImages[by];
        }
        this.isSmallSmoke = CRes.r.nextInt(3) == 0;
    }

    public void createSmoke(byte by) {
        ++this.smallSmokeDelay;
        if (this.smallSmokeDelay > 2) {
            GameScr.sm.addSmoke(this.x, this.y, by);
            this.smallSmokeDelay = 0;
        }
    }

    public void checkWaterCollide() {
        if (MM.isHaveWaterOrGlass && !this.isWaterBum && MM.checkWaterBum(this.x, this.y, (byte)0)) {
            this.isWaterBum = true;
        }
    }

    public static int checkWay(int n, int n2, int n3, int n4) {
        int n5;
        int n6;
        int n7 = 0;
        int n8 = 1;
        int n9 = 2;
        int n10 = 3;
        int n11 = -1;
        if (n3 >= n && n4 <= n2) {
            n6 = n3 - n;
            n5 = n2 - n4;
            n11 = n6 > n5 ? n8 : n9;
        }
        if (n3 >= n && n4 >= n2) {
            n6 = n3 - n;
            n5 = n4 - n2;
            n11 = n6 > n5 ? n8 : n10;
        }
        if (n3 <= n && n4 <= n2) {
            n6 = n - n3;
            n5 = n2 - n4;
            n11 = n6 > n5 ? n7 : n9;
        }
        if (n3 <= n && n4 >= n2) {
            n6 = n - n3;
            n5 = n4 - n2;
            n11 = n6 > n5 ? n7 : n10;
        }
        return n11;
    }

    public void flyTo() {
        int n = this.xBat - this.x;
        int n2 = this.yBat - this.y;
        if (Math.abs(n) < 16 && Math.abs(n2) < 16) {
            GameScr.sm.removeSmoke(this);
        } else {
            int n3 = CRes.angle(n, n2);
            if (Math.abs(n3 - this.angle) < 90 || n * n + n2 * n2 > 4096) {
                this.angle = Math.abs(n3 - this.angle) < 15 ? n3 : ((n3 - this.angle < 0 || n3 - this.angle >= 180) && n3 - this.angle >= -180 ? CRes.fixangle(this.angle - 15) : CRes.fixangle(this.angle + 15));
            }
            if (this.va < 8192) {
                this.va += 1024;
            }
            this.vx = this.va * CRes.cos(this.angle) >> 10;
            this.vy = this.va * CRes.sin(this.angle) >> 10;
            int n4 = (n += this.vx) >> 10;
            this.x += n4;
            n &= 0x3FF;
            int n5 = (n2 += this.vy) >> 10;
            this.y += n5;
            n2 &= 0x3FF;
        }
    }

    public void update() {
        switch (this.type) {
            case 0:
            case 7:
            case 11:
            case 13: {
                if (this.curFrame == 3) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.updateFrame();
                break;
            }
            case 1: {
                if (this.curFrame == 3) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.updateFrame();
                break;
            }
            case 2:
            case 3: {
                if (this.type == 3) {
                    this.updateFrame();
                    if (this.curFrame > 3) {
                        this.curFrame = 0;
                    }
                }
                if (this.y > MM.mapHeight) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.x += this.vx;
                this.y += this.vy;
                ++this.vy;
                this.checkWaterCollide();
                if (!this.isSmallSmoke) break;
                this.createSmoke((byte)1);
                break;
            }
            case 4:
            case 5:
            case 18: {
                for (int i = 0; i < this.num; ++i) {
                    int[] nArray = this.smokeX;
                    int n = i;
                    nArray[n] = nArray[n] + this.vx;
                    nArray = this.smokeY;
                    int n2 = i;
                    nArray[n2] = nArray[n2] + this.vy;
                    if (this.smokeY[i] >= this.endY) continue;
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.smokeHeight += 5;
                if (this.smokeHeight <= 255) break;
                this.smokeHeight = 255;
                break;
            }
            case 6:
            case 8:
            case 12:
            case 14: {
                this.updateFrame();
                if (this.vy > 0) {
                    this.vx = GameScr.windx / 20;
                    if (this.curFrame > 3) {
                        this.curFrame = 0;
                    }
                } else {
                    this.curFrame = 0;
                }
                if (this.y > MM.mapHeight) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.x += this.vx;
                this.y += this.vy;
                if (this.vy >= 2) break;
                ++this.vy;
                break;
            }
            case 9:
            case 10: {
                if (this.y > MM.mapHeight) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.x += this.vx;
                this.y += this.vy;
                ++this.vy;
                this.checkWaterCollide();
                break;
            }
            case 15: {
                if (this.curFrame == 3) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.updateFrame();
            }
            default: {
                break;
            }
            case 17: {
                ++this.delay;
                if (this.delay == 10) {
                    this.delay = 0;
                }
                this.curFrame = this.delay < 5 ? 0 : 1;
                this.flyTo();
                break;
            }
            case 19: {
                if (!this.isStop) {
                    ++this.timeStop;
                    if (this.timeStop == 10) {
                        this.timeStop = 0;
                        this.isStop = true;
                    }
                    if (this.curFrame > 4) {
                        this.curFrame = 0;
                    }
                    if (CCanvas.gameTick % 2 != 0) break;
                    ++this.curFrame;
                    break;
                }
                if (CCanvas.gameTick % 2 != 0) break;
                if (this.curFrame == 7) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                ++this.curFrame;
                break;
            }
            case 20: {
                if (this.y > MM.mapHeight) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                this.x += this.vx;
                this.y += this.vy;
                ++this.vy;
                break;
            }
            case 21: {
                if (this.curFrame == 7) {
                    GameScr.sm.removeSmoke(this);
                    return;
                }
                ++this.curFrame;
                break;
            }
            case 22: {
                if (this.curFrame == 3) {
                    this.curFrame = 0;
                }
                ++this.tWind;
                if (this.tWind == 50) {
                    this.tWind = 0;
                    GameScr.sm.removeSmoke(this);
                }
                this.updateFrame();
            }
        }
    }

    private void updateFrame() {
        ++this.delay;
        if (this.delay > 1) {
            ++this.curFrame;
            this.delay = 0;
        }
    }

    public void paintEffect(mGraphics mGraphics2) {
        switch (this.type) {
            case 0:
            case 1:
            case 7:
            case 13:
            case 15:
            case 19:
            case 21: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                break;
            }
            case 2: {
                mGraphics2.drawImage(rockImg, this.x, this.y, mGraphics.HCENTER | mGraphics.VCENTER, false);
                break;
            }
            case 3: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                break;
            }
            case 4:
            case 5: {
                for (int i = 0; i < this.num; ++i) {
                    int n = CRes.random(this.smokeHeight - 20, this.smokeHeight);
                    mGraphics2.setColor(n, n, n);
                    mGraphics2.fillArc(this.smokeX[i], this.smokeY[i], this.smokeRadius[i], this.smokeRadius[i], 0, 360, false);
                }
                return;
            }
            case 6:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 14: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                break;
            }
            case 16: {
                if (!this.activeLazer) break;
                for (int i = 0; i < this.wLazer; ++i) {
                    if (i < this.wLazer / 2 + this.wLazer / 4 && i > this.wLazer / 2 - this.wLazer / 4) {
                        mGraphics2.setColor(0xFFFFFF);
                    } else {
                        if (this.typeLazer == 1) {
                            mGraphics2.setColor(718162);
                        }
                        if (this.typeLazer == 0) {
                            mGraphics2.setColor(0xFF4646);
                        }
                    }
                    mGraphics2.drawLine(this.xLazer - this.wLazer / 2 + i, this.yLazer, this.x - this.wLazer / 2 + i, this.y, false);
                }
                --this.wLazer;
                if (this.wLazer != 0) break;
                this.activeLazer = false;
                GameScr.sm.removeSmoke(this);
                return;
            }
            case 17: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y, 0, mGraphics.HCENTER | mGraphics.VCENTER, mGraphics2);
                break;
            }
            case 18: {
                for (int i = 0; i < this.num; ++i) {
                    int n = CRes.random(190, 200);
                    mGraphics2.setColor(n, n, n);
                    mGraphics2.fillArc(this.smokeX[i], this.smokeY[i], this.smokeRadius[i], this.smokeRadius[i], 0, 360, false);
                }
                return;
            }
            case 20: {
                mGraphics2.drawImage(star, this.x, this.y, mGraphics.HCENTER | mGraphics.VCENTER, false);
                break;
            }
            case 22: {
                this.frmImg.drawFrame(this.curFrame, this.x, this.y, 0, 33, mGraphics2);
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        this.paintEffect(mGraphics2);
    }

    public void onClearMap() {
    }

    static {
        frameImages = new FrameImage[22];
        try {
            filePack = new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_EFFECT + "effect"));
            smoke = filePack.loadImage("smoke.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[0] = new FrameImage((Image)object, 14, 15);
                    CRes.onSaveToFile((Image)object, "smoke", true);
                }
            });
            smoke2 = filePack.loadImage("smoke2.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[19] = new FrameImage((Image)object, 14, 15);
                    CRes.onSaveToFile((Image)object, "smoke2", true);
                }
            });
            chickenHair = filePack.loadImage("longga.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[8] = new FrameImage((Image)object, 16, 10);
                    CRes.onSaveToFile((Image)object, "longga", true);
                }
            });
            blackSmokeImg = filePack.loadImage("blacksmoke.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[1] = new FrameImage((Image)object, 12, 13);
                    CRes.onSaveToFile((Image)object, "blacksmoke", true);
                }
            });
            blueSmoke = filePack.loadImage("blueSmoke.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[7] = new FrameImage((Image)object, 10, 10);
                    CRes.onSaveToFile((Image)object, "blueSmoke", true);
                }
            });
            chuotBay = filePack.loadImage("chuotbay.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[9] = new FrameImage((Image)object, 22, 18);
                    CRes.onSaveToFile((Image)object, "chuotbay", true);
                }
            });
            gatruilong = filePack.loadImage("gatruilong.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[10] = new FrameImage((Image)object, 19, 17);
                    CRes.onSaveToFile((Image)object, "gatruilong", true);
                }
            });
            water = filePack.loadImage("water.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[11] = new FrameImage((Image)object, 3, 3);
                    CRes.onSaveToFile((Image)object, "water", true);
                }
            });
            imgTornado = filePack.loadImage("locxoay.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "locxoay", true);
                }
            });
            explode = filePack.loadImage("ex3.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "ex3", true);
                }
            });
            lacay = filePack.loadImage("lacay.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[12] = new FrameImage((Image)object, 8, 5);
                    CRes.onSaveToFile((Image)object, "lacay", true);
                }
            });
            lazerSmoke = filePack.loadImage("blueSmoke2.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[13] = new FrameImage((Image)object, 10, 10);
                    CRes.onSaveToFile((Image)object, "blueSmoke2", true);
                }
            });
            glassFly2 = filePack.loadImage("lacay2.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[14] = new FrameImage((Image)object, 16, 10);
                    CRes.onSaveToFile((Image)object, "lacay2", true);
                }
            });
            smokeNuke = filePack.loadImage("khoi.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[15] = new FrameImage((Image)object, 27, 27);
                    CRes.onSaveToFile((Image)object, "khoi", true);
                }
            });
            smokeFire = filePack.loadImage("smokeFire.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[21] = new FrameImage((Image)object, 14, 15);
                    CRes.onSaveToFile((Image)object, "smokeFire", true);
                }
            });
            lua = filePack.loadImage("lua.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "lua", true);
                }
            });
            filePack = new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_MAP + "bg"));
            rockImg = filePack.loadImage("rock1.png", new IAction2(){

                public void perform(Object object) {
                    CRes.onSaveToFile((Image)object, "Smoke_ rock1");
                }
            });
            rock2Img = filePack.loadImage("rock2.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[3] = new FrameImage((Image)object, 12, 12);
                    CRes.onSaveToFile((Image)object, "Smoke_ rock2");
                }
            });
            glassFly = filePack.loadImage("cobay.png", new IAction2(){

                public void perform(Object object) {
                    frameImages[6] = new FrameImage((Image)object, 16, 10);
                    CRes.onSaveToFile((Image)object, "Smoke_ cobay");
                }
            });
            bat = mImage.createImage("/effect/bat.png");
            star = mImage.createImage("/effect/star.png");
            wind = mImage.createImage("/effect/locxoay.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        filePack = null;
    }
}

