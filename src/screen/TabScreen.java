/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import coreLG.CCanvas;
import screen.CScreen;

public class TabScreen
extends CScreen {
    public int xPaint;
    public int yPaint;
    public int wTabScreen;
    public int hTabScreen;
    public int n = 4;
    public int select;
    public String title;
    public boolean isClose;
    public int tClose;
    protected int wBlank = 56;
    protected int size_px = 32;

    public void getW() {
        this.wTabScreen = this.n * this.size_px + this.wBlank;
    }

    public void paint(mGraphics mGraphics2) {
        if (lastSCreen != null) {
            lastSCreen.paint(mGraphics2);
        }
        mGraphics2.translate(this.cmx, 0);
        TabScreen.paintBorderRect(mGraphics2, this.yPaint, this.n, this.hTabScreen, this.title);
    }

    public void paintSuper(mGraphics mGraphics2) {
        super.paint(mGraphics2);
    }

    public void update() {
        this.moveCamera();
        if (lastSCreen != null) {
            lastSCreen.update();
        }
        if (this.isClose) {
            this.cmtoX = -CCanvas.width;
            ++this.tClose;
            if (this.tClose == 5) {
                this.tClose = 0;
                this.isClose = false;
                CCanvas.curScr = null;
                if (lastSCreen != null) {
                    lastSCreen.show();
                }
            }
        }
    }

    public void show(CScreen cScreen) {
        lastSCreen = cScreen;
        this.cmx = -CCanvas.width;
        this.cmtoX = 0;
        super.show();
    }
}

