/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import effect.Explosion;
import map.Background;
import map.MM;
import screen.GameScr;

public class Snow {
    int[] x;
    int[] y;
    int[] vx;
    int[] vy;
    public static mImage imgSnow;
    int[] type;
    int sum;
    int state;
    int typeSnow;
    int xx;
    public int waterY = 0;
    boolean[] isRainEffect;
    int[] frame;
    int[] t;
    boolean[] activeEff;
    public int min = 99;
    public int max = 100;
    public int vymin = 15;
    public int vymax = 20;
    public int vxmin = 7;

    public void startSnow(int n) {
        this.typeSnow = n;
        if (this.typeSnow == 0) {
            this.sum = CCanvas.random(150, 200);
        }
        if (this.typeSnow == 1) {
            this.sum = CCanvas.random(this.min, this.max);
        }
        if (imgSnow == null) {
            try {
                imgSnow = mImage.createImage("/tuyet.png");
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        this.x = new int[this.sum];
        this.y = new int[this.sum];
        this.vx = new int[this.sum];
        this.vy = new int[this.sum];
        this.type = new int[this.sum];
        this.isRainEffect = new boolean[this.sum];
        for (int i = 0; i < this.sum; ++i) {
            this.x[i] = CCanvas.random(-10, MM.mapWidth);
            this.y[i] = CCanvas.random(-100, MM.mapHeight - this.waterY);
            this.vx[i] = 0;
            if (this.typeSnow == 0) {
                this.vy[i] = CCanvas.random(1, 3);
            }
            if (this.typeSnow == 1) {
                this.vy[i] = CCanvas.random(this.vymin, this.vymax);
            }
            this.type[i] = CCanvas.random(1, 3);
            if (this.type[i] != 2 || i % 2 != 0) continue;
            this.isRainEffect[i] = true;
        }
    }

    public void update() {
        if (this.state != 100) {
            for (int i = 0; i < this.sum; ++i) {
                if (this.state == 0) {
                    int[] nArray = this.y;
                    int n = i;
                    nArray[n] = nArray[n] + this.vy[i];
                    if (this.typeSnow == 0) {
                        this.vx[i] = GameScr.windx >> 4;
                    }
                    if (this.typeSnow == 1) {
                        this.vx[i] = GameScr.windx == 0 ? 1 : (GameScr.windx > 0 ? this.vxmin : -this.vxmin);
                    }
                    nArray = this.x;
                    int n2 = i;
                    nArray[n2] = nArray[n2] + this.vx[i];
                }
                if (this.y[i] >= -200 && this.y[i] <= Background.waterY + 40 && this.x[i] <= MM.mapWidth && this.x[i] >= -10) continue;
                if (this.y[i] > Background.waterY + 40) {
                    new Explosion(this.x[i], this.y[i], 6);
                }
                this.x[i] = CCanvas.random(-10, MM.mapWidth);
                this.y[i] = CCanvas.random(-100, Background.waterY + 40);
            }
        }
    }

    public void paintBigSnow(mGraphics mGraphics2) {
        if (this.state != 100) {
            for (int i = 0; i < this.sum; ++i) {
                if (this.type[i] != 2) continue;
                if (this.typeSnow == 0) {
                    mGraphics2.drawImage(imgSnow, this.x[i], this.y[i], 0, false);
                }
                if (this.typeSnow != 1) continue;
                mGraphics2.setColor(0xF3F3F3);
                int n = Math.abs(GameScr.windx);
                int n2 = GameScr.windx;
                if (n == 0) {
                    mGraphics2.drawLine(this.x[i], this.y[i], this.x[i] + 1, this.y[i] + 4, false);
                }
                if (n <= 0) continue;
                mGraphics2.drawLine(this.x[i], this.y[i], this.x[i] + (n2 > 0 ? 4 : -4), this.y[i] + 4, false);
                mGraphics2.drawLine(this.x[i], this.y[i], this.x[i] + (n2 > 0 ? 3 : -3), this.y[i] + 4, false);
            }
        }
    }

    public void paintOnlySmall(mGraphics mGraphics2) {
        for (int i = 0; i < this.sum; ++i) {
            if (this.typeSnow != 1) continue;
            mGraphics2.setColor(0xA6A6A6);
            int n = Math.abs(GameScr.windx);
            int n2 = GameScr.windx;
            if (n == 0) {
                mGraphics2.drawLine(this.x[i], this.y[i], this.x[i] + 1, this.y[i] + 2, false);
            }
            if (n <= 0) continue;
            mGraphics2.drawLine(this.x[i], this.y[i], this.x[i] + (n2 > 0 ? 2 : -2), this.y[i] + 2, false);
        }
    }

    public void paintSmallSnow(mGraphics mGraphics2) {
        if (this.state != 100) {
            mGraphics2.setColor(10742731);
            for (int i = 0; i < this.sum; ++i) {
                if (this.type[i] == 2) continue;
                if (this.typeSnow == 0) {
                    mGraphics2.fillRect(this.x[i], this.y[i], 2, 2, false);
                }
                if (this.typeSnow != 1) continue;
                mGraphics2.setColor(0xB5B6B6);
                int n = Math.abs(GameScr.windx);
                int n2 = GameScr.windx;
                if (n == 0) {
                    mGraphics2.drawLine(this.x[i], this.y[i], this.x[i] + 1, this.y[i] + 2, false);
                }
                if (n <= 0) continue;
                mGraphics2.drawLine(this.x[i], this.y[i], this.x[i] + (n2 > 0 ? 2 : -2), this.y[i] + 2, false);
            }
        }
    }
}

