/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.TemGraphics;
import CLib.mGraphics;
import CLib.mSystem;
import CLib.mVector;
import coreLG.CCanvas;
import model.CRes;

public class TemCanvas {
    public static TemCanvas instance;
    public static TemGraphics tem;
    public static int wMain;
    public static int hMain;
    public CCanvas gamecanvas;
    public static mVector listPoint;

    public TemCanvas() {
        instance = this;
        this.checkZoomLevel(wMain, hMain);
    }

    private void checkZoomLevel(int n, int n2) {
        CRes.out("w-H " + n + "-" + n2);
    }

    public void start() {
    }

    public void paint(mGraphics mGraphics2) {
        if (this.gamecanvas != null) {
            this.gamecanvas.paint(mGraphics2);
        }
    }

    public void update() {
        CCanvas.timeNow = mSystem.currentTimeMillis();
        if (this.gamecanvas != null) {
            this.gamecanvas.update();
        }
    }

    public void keyPressed(int n) {
        this.gamecanvas.keyPressed(n);
    }

    public void keyReleased(int n) {
        this.gamecanvas.keyReleased(n);
    }

    static {
        tem = new TemGraphics();
    }
}

