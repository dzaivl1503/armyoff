/*
 * Decompiled with CFR 0.152.
 */
package player;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import effect.Camera;
import effect.Explosion;
import java.util.Random;
import map.MM;
import model.CRes;
import model.CTime;
import network.MessageHandler;
import network.Session_ME;
import player.CPlayer;
import player.PM;
import screen.GameScr;
import screen.PrepareScr;

public class Boss
extends CPlayer {
    static Image umbrella;
    int[] xWeb = new int[6];
    int[] yWeb = new int[6];
    boolean bombShoot = false;
    boolean isGift_1 = false;
    boolean isGift_2 = false;
    boolean up = true;
    boolean down = false;
    int yGift_1 = 0;
    int yGift_2 = MM.mapHeight + 30;
    int dy;
    int frameG;
    int tG;
    public static mImage gift_1;
    public static mImage gift_2;
    public static mImage gift_empty;
    public static mImage bongbong;
    int fanFrame;
    static int deltaYKC;
    static int tKC;
    public static int xTo;
    int dxUFO;
    int dyUFO;
    int dtXUFO;
    int dtYUFO;
    int wLazer = 8;
    boolean changeSign;
    int deltaY;
    int tB;
    int deltaX1 = 0;
    int deltaY1 = 0;
    int deltaX2 = 0;
    int gFrame;
    int tg;
    int ghostLook;
    int tBit;
    int fireFrame;
    int tFire;
    int t;
    Random r = new Random();
    int vy = this.random(5, 6);
    int dyRobot = 18;
    public static int camY;
    public boolean getGift;

    public Boss(int n, byte by, int n2, int n3, boolean bl, int n4, byte by2, int n5) {
        super(n, by, n2, n3, bl, n4, by2, null, n5);
        if (this.gun == 11) {
            new Explosion(this.x, this.y, 1);
        }
        if (this.gun == 13 || this.gun == 14) {
            this.isJump = true;
        }
        if (this.gun == 16 || this.gun == 17 || this.gun == 19 || this.gun == 18 || this.gun == 21 || this.gun == 20 || this.gun == 22 || this.gun == 24 || this.gun == 25 || this.gun == 26) {
            this.flyPlayer = true;
        }
        if (this.gun == 22) {
            for (int i = 0; i < 6; ++i) {
                if (i == 0) {
                    this.xWeb[i] = CRes.random(20, CCanvas.width / 3);
                    this.yWeb[i] = CRes.random(this.y - 44, this.y);
                    continue;
                }
                this.xWeb[i] = this.xWeb[i - 1] + 25 + CRes.random(0, MM.mapWidth / 3 - 20);
                this.yWeb[i] = CRes.random(this.y - 44, this.y);
            }
        }
        if (this.gun == 23) {
            new Explosion(this.x, this.yGift_1, 1);
        }
        if (this.gun == 25 || this.gun == 26) {
            new Explosion(this.x, this.y, 1);
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void paintBigRobot(mGraphics mGraphics2) {
        if (this.state == 5) {
            mGraphics2.drawImage(robotInjured, this.x, this.y, 33, false);
        } else {
            if (this.curFrame > 3) {
                this.curFrame = 1;
            }
            mGraphics2.drawImage(robotLeg, this.x, this.y, 33, false);
            int n = this.curFrame != 3 ? 3 : 33;
            this.deltaX1 = this.curFrame != 2 && this.curFrame != 3 ? -20 : -15;
            this.pFrameImg.drawFrame(this.curFrame, this.x + this.deltaX1, this.y - 22 + this.deltaY, 0, n, mGraphics2);
            mGraphics2.drawImage(robotBody, this.x - 1, this.y - 10 + this.deltaY, 33, false);
            this.pFrameImg.drawFrame(this.curFrame, this.x, this.y - 20 + this.deltaY, 0, n, mGraphics2);
            if (this.isBum) {
                ++this.tFire;
                if (this.tFire == 10) {
                    this.tFire = 0;
                }
                this.fireFrame = this.tFire <= 5 ? 0 : 1;
                mGraphics2.drawRegion(fire, 0, 10 * this.fireFrame, 5, 10, 0, this.x - 10, this.y, 17, false);
                mGraphics2.drawRegion(fire, 0, 10 * this.fireFrame, 5, 10, 0, this.x + 10, this.y, 17, false);
                mGraphics2.drawRegion(fire, 0, 10 * this.fireFrame, 5, 10, 0, this.x - 15, this.y, 17, false);
                mGraphics2.drawRegion(fire, 0, 10 * this.fireFrame, 5, 10, 0, this.x + 15, this.y, 17, false);
            }
        }
    }

    public void paintKhiCau(mGraphics mGraphics2) {
        mGraphics2.drawImage(khicau, this.x, this.y + deltaYKC, 33, false);
        mGraphics2.drawRegion(fan1, 0, this.fanFrame * 23, 4, 23, 0, this.x - 8, this.y - 18 + deltaYKC, 33, false);
        if (this.fanFrame == 0) {
            this.fanFrame = 1;
        } else if (this.fanFrame == 1) {
            this.fanFrame = 0;
        }
        if (CCanvas.gameTick % 2 == 0) {
            mGraphics2.drawImage(fan2, this.x + 10, this.y - 35 + deltaYKC, 33, false);
            mGraphics2.drawImage(fan2, this.x + 34, this.y - 35 + deltaYKC, 33, false);
        }
        mGraphics2.drawImage(front_fan, this.x - 5, this.y - 43 + deltaYKC, 33, false);
        mGraphics2.drawRegion(fan1, 0, this.fanFrame * 23, 4, 23, 0, this.x, this.y - 46 + deltaYKC, 33, false);
        int[] nArray = new int[]{this.x, this.x - 30, this.x + 20};
        int[] nArray2 = new int[]{this.y - 20, this.y - 35, this.y - 25};
        if (this.state == 5) {
            for (int i = 0; i < 3; ++i) {
                mGraphics2.drawRegion(injured, 0, i * 12, 14, 12, 0, nArray[i], nArray2[i] + deltaYKC, 0, false);
            }
        }
    }

    public void checkFrameBalloonGun() {
        if (xTo <= this.x + 50 && xTo >= this.x - 50) {
            this.curFrame = 1;
        } else if (xTo > this.x + 50) {
            this.curFrame = 0;
            this.look = 0;
        } else if (xTo < this.x - 50) {
            this.curFrame = 0;
            this.look = 2;
        }
    }

    public void paintFan1(mGraphics mGraphics2) {
        int n = this.state == 5 ? 0 : deltaYKC;
        mGraphics2.drawImage(back_fan, this.x, this.y + n, 33, false);
    }

    public void paintFan2(mGraphics mGraphics2) {
    }

    public void paintEye(mGraphics mGraphics2) {
        if (this.state == 5) {
            this.pFrameImg.drawFrame(2, this.x + 2, this.y + deltaYKC - 1, 0, 33, mGraphics2);
        } else if (CCanvas.gameTick % 2 == 0) {
            this.pFrameImg.drawFrame(0, this.x + 2, this.y + deltaYKC - 1, 0, 33, mGraphics2);
        } else {
            this.pFrameImg.drawFrame(1, this.x + 2, this.y + deltaYKC - 1, 0, 33, mGraphics2);
        }
    }

    public void paintGunKhiCau(mGraphics mGraphics2) {
        this.checkFrameBalloonGun();
        if (this.curFrame > 1) {
            this.curFrame = 0;
        }
        int n = this.state == 5 ? 0 : deltaYKC;
        mGraphics2.drawImage(gunkhicau, this.x, this.y - 7 + n, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        this.pFrameImg.drawFrame(this.curFrame, this.x + 2, this.y + n - 7, this.look, 3, mGraphics2);
    }

    public void paintBombKhiCau(mGraphics mGraphics2) {
        int n = this.state == 5 ? 0 : deltaYKC;
        mGraphics2.drawImage(bombKhiCau, this.x, this.y + n, 33, false);
    }

    public void paintUFO(mGraphics mGraphics2) {
        int n = 0;
        if (this.state == 8) {
            n = 1;
        } else if (this.state == 5) {
            n = 2;
        }
        int n2 = this.x + this.dxUFO;
        int n3 = this.y + this.dyUFO;
        if (imgUFOFrames != null && imgUFOFrames[n] != null) {
            mGraphics2.drawImage(imgUFOFrames[n], n2, n3, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        } else {
            mGraphics2.drawRegion(imgUFO, 0, n * 46, 51, 46, 0, n2, n3, 33, false);
        }
        if (this.state != 5) {
            int n4;
            int n5 = n4 = CCanvas.gameTick % 3 == 0 ? 0 : 1;
            if (imgUFOFireFrames != null && imgUFOFireFrames[n4] != null) {
                mGraphics2.drawImage(imgUFOFireFrames[n4], n2, n3 - 5, mGraphics.TOP | mGraphics.HCENTER, false);
            } else {
                mGraphics2.drawRegion(imgUFOFire, 0, n4 * 11, 16, 11, 0, n2, n3 - 5, 17, false);
            }
            if (this.isPointActive) {
                mGraphics2.setColor(149905);
                for (int i = 0; i < this.wLazer; ++i) {
                    if (i < this.wLazer / 2 + this.wLazer / 3 && i > this.wLazer / 2 - this.wLazer / 3) {
                        mGraphics2.setColor(0xFFFFFF);
                    } else {
                        mGraphics2.setColor(0x59BBBB);
                    }
                    mGraphics2.drawLine(this.x - this.wLazer / 2 + i + this.dxUFO, this.y + this.dyUFO, this.x - this.wLazer / 2 + i + this.dxUFO, this.yPoint, false);
                }
                if (this.wLazer == 0) {
                    this.changeSign = true;
                }
                if (this.wLazer == 8) {
                    this.changeSign = false;
                }
                if (CCanvas.gameTick % 2 == 0) {
                    this.wLazer = !this.changeSign ? --this.wLazer : ++this.wLazer;
                }
            }
        }
    }

    public void paintSpider(mGraphics mGraphics2) {
        int n;
        mGraphics2.setColor(0x7A7A7A);
        for (n = 0; n < 3; ++n) {
            mGraphics2.drawLine(0, this.capY + n * 22 - 44, MM.mapWidth, this.capY + n * 22 - 44, false);
        }
        for (n = 0; n < 3; ++n) {
            mGraphics2.drawImage(web, this.xWeb[n], this.yWeb[n], 3, false);
        }
        if (this.sLook == 0) {
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 1, this.x + 1, this.y, 33, false);
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 0, this.x, this.y - 22, 33, false);
        }
        if (this.sLook == 1) {
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 3, this.x, this.y, 33, false);
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 2, this.x, this.y - 22, 33, false);
        }
        if (this.sLook == 2) {
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 4, this.x, this.y, 40, false);
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 5, this.x + 22, this.y, 40, false);
        }
        if (this.sLook == 3) {
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 6, this.x, this.y, 40, false);
            mGraphics2.drawRegion(spider, 0, this.fspider * 22, 41, 22, 7, this.x + 22, this.y, 40, false);
        }
        if (this.isCapture) {
            mGraphics2.setColor(0x525252);
            mGraphics2.fillRect(this.capX, this.capY - 41, 1, this.y - this.capY, false);
        }
    }

    public void paintBugRobot(mGraphics mGraphics2) {
        int n;
        if (this.look == 2) {
            n = 0;
            if (this.curFrame == 1) {
                this.deltaX1 = 15;
                this.deltaY1 = 0;
            }
            if (this.curFrame == 2) {
                this.deltaX1 = 15;
                this.deltaY1 = -5;
            }
            if (this.curFrame == 3) {
                this.deltaX1 = 7;
                this.deltaY1 = -12;
            }
            this.deltaX2 = 4;
        } else {
            n = 2;
            if (this.curFrame == 1) {
                this.deltaX1 = -15;
                this.deltaY1 = 0;
            }
            if (this.curFrame == 2) {
                this.deltaX1 = -15;
                this.deltaY1 = -5;
            }
            if (this.curFrame == 3) {
                this.deltaX1 = -7;
                this.deltaY1 = -12;
            }
            this.deltaX2 = -4;
        }
        if (this.look == 0) {
            this.xBugBack = -this.xBugBack;
        }
        int n2 = this.x - this.deltaX2 + (this.curFrame != 1 && this.curFrame != 2 ? 0 : this.xBugBack);
        int n3 = this.y - 10 + this.deltaY + (this.curFrame != 2 && this.curFrame != 3 ? 0 : this.yBugBack);
        int n4 = this.x + this.deltaX1 + (this.curFrame != 1 && this.curFrame != 2 ? 0 : this.xBugBack);
        int n5 = this.y - 15 + this.deltaY + this.deltaY1 + (this.curFrame != 2 && this.curFrame != 3 ? 0 : this.yBugBack);
        mGraphics2.drawRegion(bugbody, 0, 30 * this.framebd_1, 42, 30, n, n2, n3, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        mGraphics2.drawRegion(bugleg, 0, 25 * this.frameleg_1, 44, 25, n, this.x, this.y, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        if (this.curFrame == 0 || this.curFrame == 6) {
            this.curFrame = 1;
        }
        this.pFrameImg.drawFrame(this.curFrame, n4, n5, this.look, mGraphics.BOTTOM | mGraphics.HCENTER, mGraphics2);
    }

    void paintBossHP(mGraphics mGraphics2, int n) {
        this.paintHpBar(mGraphics2, this.x - 20 + this.dxUFO, this.y - 58 + this.dyUFO, 40);
    }

    public void paintGhost(mGraphics mGraphics2, int n) {
        mGraphics2.drawRegion(n == 0 ? ghost : ghost2, 0, this.gFrame * 32, 35, 32, this.look, this.x, this.y, 33, false);
    }

    public void paint(mGraphics mGraphics2) {
        if (this.isPaint) {
            if (this.gun == 13) {
                this.paintBugRobot(mGraphics2);
                this.paintBossHP(mGraphics2, 50);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 15) {
                this.paintBigRobot(mGraphics2);
                this.paintBossHP(mGraphics2, 60);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 16) {
                this.paintUFO(mGraphics2);
                this.paintBossHP(mGraphics2, 55);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 17) {
                this.paintKhiCau(mGraphics2);
                this.paintBossHP(mGraphics2, 55);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 18) {
                this.paintGunKhiCau(mGraphics2);
                this.paintBossHP(mGraphics2, -7);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 21) {
                this.paintEye(mGraphics2);
                this.paintBossHP(mGraphics2, 15);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 19) {
                this.paintBombKhiCau(mGraphics2);
                this.paintBossHP(mGraphics2, -7);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 20) {
                this.paintFan1(mGraphics2);
                this.paintBossHP(mGraphics2, 30);
                this.painthpChange(mGraphics2);
            } else if (this.gun == 22) {
                this.paintSpider(mGraphics2);
                this.paintBossHP(mGraphics2, 48);
                this.painthpChange(mGraphics2);
            } else {
                if (this.gun == 14 && (this.isFly || this.isBum)) {
                    ++this.tFire;
                    if (this.tFire == 10) {
                        this.tFire = 0;
                    }
                    this.fireFrame = this.tFire <= 5 ? 0 : 1;
                    mGraphics2.drawRegion(fire, 0, 10 * this.fireFrame, 5, 10, 0, this.x - 5, this.y + 2, mGraphics.HCENTER | mGraphics.TOP, false);
                    mGraphics2.drawRegion(fire, 0, 10 * this.fireFrame, 5, 10, 0, this.x + 5, this.y + 2, mGraphics.HCENTER | mGraphics.TOP, false);
                }
                if (this.gun == 25) {
                    this.paintGhost(mGraphics2, 0);
                    this.paintBossHP(mGraphics2, 40);
                    this.painthpChange(mGraphics2);
                } else if (this.gun == 26) {
                    this.paintGhost(mGraphics2, 1);
                    this.paintBossHP(mGraphics2, 40);
                    this.painthpChange(mGraphics2);
                } else if (this.gun == 23) {
                    if (this.state != 5 && this.hp > 0) {
                        mGraphics2.drawImage(gift_1, this.x, this.yGift_1, 33, false);
                    }
                } else if (this.gun == 24) {
                    if (this.state != 5) {
                        ++this.tG;
                        if (this.tG == 10) {
                            this.tG = 0;
                        }
                        this.frameG = this.tG < 5 ? 0 : 1;
                        mGraphics2.drawImage(gift_2, this.x, this.yGift_2 - 8, 33, false);
                        mGraphics2.drawRegion(bongbong, 0, this.frameG * 35, 35, 35, 0, this.x, this.yGift_2, 33, false);
                    }
                } else {
                    super.paint(mGraphics2);
                }
            }
        }
    }

    public void frameMoveCheck() {
        ++this.t;
        if (this.t == 10) {
            this.t = 0;
        }
        this.curFrame = this.t < 5 ? 1 : 2;
    }

    public void frameStandCheck() {
        if (this.bombShoot) {
            this.curFrame = 4;
        } else {
            ++this.t;
            if (this.t == 10) {
                this.t = 0;
            }
            this.curFrame = this.t < 5 ? 0 : 1;
        }
    }

    public int random(int n, int n2) {
        return n + this.r.nextInt(n2 - n);
    }

    public void update() {
        super.update();
        switch (this.gun) {
            case 11:
            case 12: {
                if (this.state == 5) {
                    this.curFrame = 3;
                    return;
                }
                if (this.isCom && !this.isJump) {
                    if (this.nextx < this.x && !this.falling) {
                        this.move(0);
                        this.frameMoveCheck();
                        return;
                    }
                    if (this.nextx > this.x && !this.falling) {
                        this.move(2);
                        this.frameMoveCheck();
                        return;
                    }
                    if (this.nextx == this.x && this.nexty != this.y && this.state == 0 && !this.falling) {
                        this.y = this.nexty;
                    }
                }
                if (this.shootFrame) {
                    if (this.gun == 12) {
                        this.bombShoot = true;
                    }
                } else {
                    this.bombShoot = false;
                }
                this.frameStandCheck();
                break;
            }
            case 13: {
                if (this.state == 5) {
                    this.frameleg_1 = 2;
                    this.framebd_1 = 1;
                    this.curFrame = 0;
                    return;
                }
                if (this.xBugBack < 0) {
                    this.xBugBack += 2;
                }
                if (this.xBugBack > 0) {
                    this.xBugBack = 0;
                }
                if (this.yBugBack > 0) {
                    --this.yBugBack;
                }
                if (this.yBugBack < 0) {
                    this.yBugBack = 0;
                }
                if (this.state != 4) {
                    ++this.tB;
                    if (this.tB == 10) {
                        this.tB = 0;
                    }
                    if (this.tB == 5) {
                        this.deltaY = 1;
                    }
                    if (this.tB == 0) {
                        this.deltaY = 0;
                    }
                }
                if (this.state == 4) {
                    this.tB = 0;
                    this.deltaY = 0;
                }
                if (this.frameleg_1 == 0) break;
                if (this.x < this.xBug) {
                    this.look = 2;
                    break;
                }
                this.look = 0;
                break;
            }
            case 14: {
                if (!this.isBum) break;
                this.y -= this.dyRobot;
                --this.dyRobot;
                if (this.dyRobot != 0) break;
                this.isBum = false;
                this.dyRobot = 18;
                this.falling = true;
                break;
            }
            case 15: {
                if (this.state != 4) {
                    ++this.tB;
                    if (this.tB == 10) {
                        this.tB = 0;
                    }
                    if (this.tB == 5) {
                        this.deltaY = 1;
                    }
                    if (this.tB == 0) {
                        this.deltaY = 0;
                    }
                }
                if (this.isBum) {
                    this.y -= this.dyRobot;
                    --this.dyRobot;
                    if (this.dyRobot == 0) {
                        this.falling = true;
                    }
                }
                if (this.isBum && GameScr.mm.isLand(this.x, this.y)) {
                    this.earthwakeActive = true;
                    this.dyRobot = 18;
                    this.isBum = false;
                }
                if (!this.earthwakeActive) break;
                this.earthwakeActive = false;
                for (int i = 0; i < 4; ++i) {
                    GameScr.sm.addRock(this.x - i * 20, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)3);
                    GameScr.sm.addRock(this.x + i * 20, this.y, CRes.random(4 + this.force / 3), CRes.random(-8 - this.force / 3, -5 - this.force / 3), (byte)3);
                }
                Camera.shaking = 3;
                break;
            }
            case 16: {
                if (this.flyActive) {
                    this.flyTo(10);
                    break;
                }
                if (this.state != 4 && this.state != 5) {
                    if (CCanvas.gameTick % 2 != 0) break;
                    ++this.dtXUFO;
                    if (this.dtXUFO == 20) {
                        this.dtXUFO = 0;
                    }
                    this.dxUFO = this.dtXUFO < 10 ? ++this.dxUFO : --this.dxUFO;
                    ++this.dtYUFO;
                    if (this.dtYUFO == 10) {
                        this.dtYUFO = 0;
                    }
                    if (this.dtYUFO < 5) {
                        --this.dyUFO;
                        break;
                    }
                    ++this.dyUFO;
                    break;
                }
                this.dxUFO = 0;
                this.dyUFO = 0;
                break;
            }
            case 17: {
                if (this.state == 5) {
                    ++this.y;
                    this.falling = false;
                    if (CCanvas.gameTick % 5 == 0) {
                        new Explosion(CRes.random(this.x - 50, this.x + 50), CRes.random(this.y - 40, this.y), 0);
                    }
                }
                if (this.flyActive) {
                    this.flyTo(10);
                    deltaYKC = 0;
                    if (this.bulletType == 43) {
                        GameScr.cam.setTargetPointMode(this.x, this.y + (camY += 2));
                    }
                } else {
                    camY = 0;
                    deltaYKC = PM.deltaYKC;
                }
                for (int i = 0; i < PM.p.length; ++i) {
                    if (PM.p[i] == null) continue;
                    if (PM.p[i].state != 5) {
                        if (PM.p[i].gun == 18) {
                            PM.p[i].x = this.x + 51;
                            PM.p[i].y = this.y + 19;
                        }
                        if (PM.p[i].gun == 19) {
                            PM.p[i].x = this.x - 5;
                            PM.p[i].y = this.y + 30;
                        }
                        if (PM.p[i].gun == 20) {
                            PM.p[i].x = this.x - 67;
                            PM.p[i].y = this.y - 6;
                        }
                    }
                    if (PM.p[i].gun != 21) continue;
                    PM.p[i].x = this.x + 57;
                    PM.p[i].y = this.y - 27;
                }
            }
            default: {
                break;
            }
            case 22: {
                this.runSpeed = (byte)5;
                if (this.flyActive) {
                    ++this.t;
                    if (this.t == 4) {
                        this.t = 0;
                    }
                    this.fspider = this.t < 2 ? 0 : 1;
                } else {
                    this.fspider = this.state == 5 ? 3 : (this.state == 8 ? 2 : 0);
                }
                if (this.flyActive) {
                    this.flyTo(8);
                }
                if (!this.isCapture) break;
                this.capturePlayer();
                break;
            }
            case 23: {
                if (PrepareScr.currLevel == 7 && MessageHandler.nextTurnFlag) {
                    this.yGift_1 = this.y;
                    this.isGift_1 = true;
                    Session_ME.receiveSynchronized = 0;
                }
                if (!this.isGift_1) {
                    ++this.dy;
                    this.yGift_1 += this.dy;
                    GameScr.cam.setTargetPointMode(this.x, this.yGift_1);
                    if (this.yGift_1 > this.y) {
                        this.isGift_1 = true;
                        this.yGift_1 = this.y;
                        CTime.seconds += 2;
                        CCanvas.tNotify = 0;
                        CCanvas.lockNotify = true;
                    }
                }
                if (this.state != 5 || this.getGift) break;
                this.getGift = true;
                new Explosion(this.x, this.y, 1);
                break;
            }
            case 24: {
                if (PrepareScr.currLevel == 7 && MessageHandler.nextTurnFlag) {
                    this.yGift_2 = this.y;
                    this.isGift_2 = true;
                    Session_ME.receiveSynchronized = 0;
                }
                if (!this.isGift_2) {
                    this.yGift_2 -= 9;
                    GameScr.cam.setTargetPointMode(this.x, this.yGift_2);
                    if (this.yGift_2 < this.y) {
                        this.isGift_2 = true;
                        this.yGift_2 = this.y;
                        CTime.seconds += 2;
                        CCanvas.tNotify = 0;
                        CCanvas.lockNotify = true;
                    }
                } else {
                    if (this.up) {
                        --this.yGift_2;
                    }
                    if (this.down) {
                        ++this.yGift_2;
                    }
                    if (this.yGift_2 < this.y - 3) {
                        this.down = true;
                        this.up = false;
                    }
                    if (this.yGift_2 > this.y + 3) {
                        this.down = false;
                        this.up = true;
                    }
                }
                if (this.state != 5 || this.getGift) break;
                this.getGift = true;
                new Explosion(this.x, this.y, 1);
                break;
            }
            case 25:
            case 26: {
                if (!this.ghostBit) {
                    ++this.tg;
                    if (this.tg == 10) {
                        this.tg = 0;
                    }
                    this.gFrame = this.tg < 5 ? 0 : 1;
                    this.tBit = 0;
                } else {
                    ++this.tBit;
                    if (this.tBit == 5) {
                        this.gFrame = 2;
                    }
                    if (this.tBit == 10) {
                        this.gFrame = 3;
                        PM.p[this.playerHit].activeHurt(this.look == 2 ? 0 : 2);
                        GameScr.sm.addSmoke(PM.p[this.playerHit].x, PM.p[this.playerHit].y, (byte)22);
                    }
                    if (this.tBit == 18) {
                        this.ghostBit = false;
                        Session_ME.receiveSynchronized = 0;
                    }
                }
                if (!this.flyActive) break;
                this.flyTo(10);
            }
        }
    }

    static {
        try {
            gift_1 = mImage.createImage("/item/box.png");
            bongbong = mImage.createImage("/item/bongbong.png");
            gift_2 = mImage.createImage("/item/box2.png");
            gift_empty = mImage.createImage("/item/boxEmpty.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

