/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.mGraphics;
import coreLG.CCanvas;
import item.BM;
import item.Bullet;
import map.MM;
import model.CRes;
import player.PM;
import screen.CScreen;
import screen.GameScr;

public class Camera {
    public static final byte FREE_MODE = 0;
    public static final byte PLAYER_MODE = 1;
    static final byte BULLET_MODE = 2;
    static final byte AIRPLANE_MODE = 3;
    static final byte TARGETPOINT_MODE = 4;
    static final byte TARGETPOINT_MODE_NORETRICT = 5;
    static final byte LAZER_MODE = 6;
    static final byte METEOR_MODE = 7;
    static final byte MISSILE_RAIN_MODE = 8;
    public static byte mode;
    public static int shaking;
    int vx = 15;
    int vy = 15;
    public static int x;
    public static int y;
    public static int startx;
    public static int starty;
    public static int cameraW;
    public static int cameraH;
    int player;
    Bullet bullet;
    int count = 0;
    public int dx;
    public int dy;
    public static int dx2;
    public static int dy2;
    int indexX;
    int indexY;
    int setIndexX;
    int setIndexY;
    int deltaY = 20;

    public Camera() {
        startx = 0;
        starty = 0;
    }

    public Camera(int n, int n2) {
        this.setxy(n, n2);
        startx = n;
        starty = n2;
        this.setFreeMode();
    }

    public Camera(int n, int n2, int n3, int n4) {
        this.setxy(n, n2);
        startx = n;
        starty = n2;
        cameraW = n3;
        cameraH = n4;
        this.setFreeMode();
    }

    public void init() {
        shaking = 0;
        this.setFreeMode();
    }

    public void setxy(int n, int n2) {
        x = n;
        y = n2;
    }

    public void setFreeMode() {
        mode = 0;
        dx2 = 0;
        dy2 = 0;
    }

    public void setPlayerMode(int n) {
        mode = 1;
        this.player = n;
    }

    public void setBulletMode(Bullet bullet) {
        mode = (byte)2;
        this.bullet = bullet;
    }

    public void setMeteorMode(Bullet bullet) {
        mode = (byte)7;
        this.bullet = bullet;
    }

    public void setAirPlaneMode() {
        mode = (byte)3;
    }

    public void setLazerMode(Bullet bullet) {
        mode = (byte)6;
        this.bullet = bullet;
    }

    public void setMRainMode(Bullet bullet) {
        mode = (byte)8;
        this.bullet = bullet;
    }

    public void setTargetPointMode(int n, int n2) {
        mode = (byte)4;
        this.setIndexX = n - CScreen.w / 2;
        this.setIndexY = n2 - CScreen.h / 2;
    }

    public void setTargetPointModeNoRetrict(int n, int n2) {
        mode = (byte)5;
        this.setIndexX = n - CScreen.w / 2;
        this.setIndexY = n2 - CScreen.h / 2;
    }

    public void update() {
        startx = x;
        starty = y;
        this.indexX = 0;
        this.indexY = 0;
        switch (mode) {
            case 1: {
                if (PM.p[this.player] == null) break;
                if (PM.p[this.player].look == 0) {
                    this.indexX = PM.p[this.player].x - 40 - CScreen.w / 2;
                } else if (PM.p[this.player].look == 2) {
                    this.indexX = PM.p[this.player].x + 40 - CScreen.w / 2;
                }
                this.indexY = PM.p[this.player].y - CScreen.h / 2 - 12;
                if (GameScr.pm.isYourTurn()) {
                    if (PM.p[this.player].getState() == 1) break;
                    this.checkIndex(4);
                    break;
                }
                this.checkIndex(4);
                break;
            }
            case 2: {
                if (this.bullet == null || this.bullet.paintCount >= this.bullet.yPaint.length || this.bullet.type == 37 && (this.bullet.angle == -90 || this.bullet.angle == 270) && this.bullet.yPaint[this.bullet.paintCount] <= -300) break;
                this.indexX = this.bullet.xPaint[this.bullet.paintCount] - CScreen.w / 2;
                this.indexY = this.bullet.yPaint[this.bullet.paintCount] - CScreen.h / 2;
                if (this.bullet.type != 37) {
                    this.checkIndex(2);
                    break;
                }
                this.checkIndex(1);
                break;
            }
            case 3: {
                this.indexX = BM.airPlaneX - CScreen.w / 4;
                this.indexY = BM.airPlaneY - CScreen.h / 2;
                this.checkIndex(2);
                break;
            }
            case 4: {
                this.indexX = this.setIndexX;
                this.indexY = this.setIndexY;
                this.checkIndex(2);
                break;
            }
            case 5: {
                this.indexX = this.setIndexX;
                this.indexY = this.setIndexY;
                this.checkIndex(3);
                break;
            }
            case 6: {
                this.indexX = this.bullet.xPaint[this.bullet.paintCount] - CScreen.w / 2;
                this.indexY = this.bullet.yPaint[this.bullet.paintCount] - CScreen.h / 3;
                this.checkIndex(1);
                break;
            }
            case 7: {
                this.indexX = this.bullet.xPaint[this.bullet.paintCount] - CScreen.w / 2;
                this.indexY = this.bullet.yPaint[this.bullet.paintCount] - CScreen.h / 3;
                this.checkIndex(1);
                break;
            }
            case 8: {
                this.indexX = PM.getCurPlayer().x - CScreen.w / 2;
                this.indexY = this.bullet.yPaint[this.bullet.paintCount] - CScreen.h / 2;
                this.checkIndex(2);
            }
        }
        if (shaking != 0) {
            if (shaking == 1) {
                x += CRes.random(-5, 5);
                y += CRes.random(-5, 5);
            } else if (shaking == 2) {
                x += CRes.random(-20, 20);
                y += CRes.random(-20, 20);
            } else if (shaking == 3) {
                y = CCanvas.gameTick % 3 == 0 ? (y += this.deltaY) : (y -= this.deltaY);
                if (this.count % 2 == 0) {
                    --this.deltaY;
                }
                if (this.deltaY < 0) {
                    this.deltaY = 0;
                }
            }
            ++this.count;
            if (this.count > (shaking != 3 ? 10 : 30)) {
                this.deltaY = 20;
                shaking = 0;
                this.count = 0;
            }
        }
        if (mode != 5) {
            Camera.restrict(0, MM.mapWidth, -1000, MM.mapHeight);
        }
    }

    public void mainLoop() {
        switch (mode) {
            case 0: {
                x += dx2;
                if (dx2 != 0) {
                    dx2 = 0;
                }
                y += dy2;
                if (dy2 == 0) break;
                dy2 = 0;
            }
        }
        if (mode != 5) {
            Camera.restrict(0, MM.mapWidth, -1000, MM.mapHeight);
        }
    }

    public void checkIndex(int n) {
        if (x != this.indexX) {
            this.dx = x - this.indexX;
            x -= this.dx >> n;
        }
        if (y != this.indexY) {
            this.dy = y - this.indexY;
            y -= this.dy >> n;
        }
    }

    public static void restrict(int n, int n2, int n3, int n4) {
        if (y < n3) {
            y = n3;
        }
        if (y > n4 - CScreen.h) {
            starty = y = n4 - CScreen.h;
        }
        if (x < n) {
            x = n;
        }
        if (x > n2 - CScreen.w) {
            x = n2 - CScreen.w;
        }
    }

    public static void translate(mGraphics mGraphics2) {
        mGraphics2.translate(-x, -y);
    }

    public static void reTranslate(mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
    }

    public static String getMode() {
        if (mode == 0) {
            return "FREE_MODE";
        }
        if (mode == 1) {
            return "PLAYER_MODE";
        }
        if (mode == 2) {
            return "BULLET_MODE";
        }
        if (mode == 3) {
            return "AIRPLANE_MODE";
        }
        if (mode == 4) {
            return "TARGETPOINT_MODE";
        }
        if (mode == 5) {
            return "TARGETPOINT_MODE_NORETRICT";
        }
        if (mode == 6) {
            return "LAZER_MODE";
        }
        if (mode == 7) {
            return "METEOR_MODE";
        }
        return mode == 8 ? "MISSILE_RAIN_MODE" : "NONE";
    }

    public void onClearCamera() {
        this.bullet = null;
        this.setFreeMode();
    }

    static {
        shaking = 0;
        x = 0;
        y = 0;
        startx = 0;
        starty = 0;
        cameraW = 0;
        cameraH = 0;
    }
}

