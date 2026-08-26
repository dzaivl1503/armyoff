/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import coreLG.CCanvas;
import map.Background;
import screen.CScreen;
import screen.MainMenuScr;

public class SplashScr
extends CScreen {
    int i = 0;
    boolean loadScreen;
    int frame = 0;
    boolean isPaint;

    public SplashScr() {
        this.nameCScreen = " SplashScr screen!";
        w = CCanvas.width;
        h = CCanvas.hieght;
        this.loadScreen = true;
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setColor(7852799);
        mGraphics2.fillRect(0, 0, w, h, false);
        if (this.isPaint) {
            mGraphics2.drawRegion(Background.logo, 0, this.frame * 51, 71, 51, 0, CCanvas.width / 2, CCanvas.hieght / 2, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        }
    }

    public void show() {
        super.show();
    }

    public void update() {
        if (this.isPaint) {
            if (this.i < 50) {
                ++this.frame;
                if (this.frame > 3) {
                    this.frame = 3;
                }
            } else {
                --this.frame;
                if (this.frame < 0) {
                    this.frame = 0;
                    this.isPaint = false;
                }
            }
        }
        if (this.loadScreen) {
            this.loadScreen = false;
            this.isPaint = true;
        } else if (this.i == 55) {
            if (CCanvas.mainMenuScr == null) {
                CCanvas.mainMenuScr = new MainMenuScr();
            }
            CCanvas.mainMenuScr.show();
            return;
        }
        ++this.i;
    }
}

