/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Canvas
 *  javax.microedition.lcdui.Display
 *  javax.microedition.lcdui.Displayable
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.midlet.MIDlet
 */
package com.teamobi.mobiarmy2;

import CLib.mGraphics;
import CLib.mSystem;
import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import model.CRes;

public abstract class MotherCanvas
extends Canvas {
    public static int w;
    public static int h;
    public static int hw;
    public static int hh;
    public static int w4;
    public static int h4;
    public CCanvas tCanvas;
    public static int[] ipadPro;
    public static int[] ipadPro1;
    public static int[] iphoneXSMAX;
    public static int[] ipad;
    public static int[] iphoneX;
    public static int[] iphoneXR;
    public static int[] ipadMini;
    public static int[] iphone5;
    public static int[] iphone4;
    public static boolean bRun;
    public static int FPS;
    public static int gameDelay = 30;
    public static boolean touchDrag;
    public static String mainThreadName;
    private long lastSubLoop;

    public static int getNumberFingerOnScreen() {
        return 0;
    }

    private void checkZoomLevel(int n, int n2) {
        if (GameMidlet.DEVICE == 0) {
            mGraphics.zoomLevel = 1;
        } else if (n * n2 >= 1228800) {
            mGraphics.zoomLevel = 4;
        } else if (n * n2 >= 691200) {
            mGraphics.zoomLevel = 3;
        } else if (n * n2 >= 240000) {
            mGraphics.zoomLevel = 2;
        }
        mGraphics.zoomLevel = n * n2 >= 0x305000 ? 6 : (n * n2 >= 2073600 ? 4 : (n * n2 >= 1000500 ? 3 : (n * n2 >= 727040 ? 3 : (n * n2 >= 384000 ? 2 : 1))));
        if (mGraphics.zoomLevel > 1) {
            --mGraphics.zoomLevel;
        }
        w = n / mGraphics.zoomLevel;
        h = n2 / mGraphics.zoomLevel;
        hw = w / 2;
        hh = h / 2;
        w4 = w / 4;
        h4 = h / 4;
        CRes.out("======> Mother canvas zoom level = " + mGraphics.zoomLevel);
        if (CCanvas.isJ2ME() && CCanvas.width * CCanvas.hieght <= 82500) {
            GameMidlet.lowGraphic = true;
        }
    }

    public void displayMe(GameMidlet gameMidlet) {
        Display.getDisplay((MIDlet)gameMidlet).setCurrent((Displayable)this);
    }

    public int getHeightL() {
        return this.getHeight();
    }

    public int getWidthL() {
        return this.getWidth();
    }

    public MotherCanvas() {
        this.setFullScreenMode(true);
        this.recalcScreenSize();
    }

    public void recalcScreenSize() {
        this.checkZoomLevel(this.getWidthL(), this.getHeightL());
    }

    public abstract void mainLoop();

    public abstract void paint(mGraphics var1);

    protected void paint(Graphics graphics) {
        this.paint(new mGraphics(graphics));
    }

    protected void pointerPressed(int n, int n2) {
        this.onPointerPressed(n, n2, 0, 0);
    }

    protected void pointerReleased(int n, int n2) {
        this.onPointerReleased(n, n2, 0, 0);
    }

    protected void pointerDragged(int n, int n2) {
        this.onPointerDragged(n, n2, 0);
    }

    public void start() {
        bRun = true;
        Thread thread = new Thread(new Runnable(){

            public void run() {
                while (bRun) {
                    MotherCanvas.this.mainLoop();
                    long l = mSystem.currentTimeMillis();
                    if (l - MotherCanvas.this.lastSubLoop > 10L) {
                        MotherCanvas.this.update();
                        MotherCanvas.this.lastSubLoop = l;
                    }
                    MotherCanvas.this.repaint();
                    MotherCanvas.this.serviceRepaints();
                    try {
                        Thread.sleep(Math.max(1, (long)MotherCanvas.gameDelay));
                    }
                    catch (InterruptedException interruptedException) {}
                }
            }
        });
        thread.start();
    }

    public static String getPlatformName() {
        return System.getProperty("microedition.platform");
    }

    public int getWidthz() {
        int n = this.getWidthL();
        return n / mGraphics.zoomLevel + (n % mGraphics.zoomLevel == 0 ? 0 : 1);
    }

    public int getHeightz() {
        int n = this.getHeightL();
        return n / mGraphics.zoomLevel + (n % mGraphics.zoomLevel == 0 ? 0 : 1);
    }

    public abstract void onPointerDragged(int var1, int var2, int var3);

    public abstract void onPointerPressed(int var1, int var2, int var3, int var4);

    public abstract void onPointerReleased(int var1, int var2, int var3, int var4);

    public abstract void onPointerHolder(int var1, int var2, int var3);

    public abstract void onPointerHolder();

    protected abstract void update();

    public boolean hasPointerEvents() {
        return true;
    }

    public void run() {
    }

    public static int getSecond() {
        return (int)(mSystem.currentTimeMillis() / 1000L);
    }

    public boolean keyPressPc(int n) {
        switch (n) {
            case -22: {
                CCanvas.keyHold[41] = true;
                CCanvas.keyPressed[41] = true;
                return true;
            }
            case -21: {
                CCanvas.keyPressed[40] = true;
                break;
            }
            case 97: {
                CCanvas.keyHold[34] = true;
                CCanvas.keyPressed[34] = true;
                return true;
            }
            case 98: {
                CCanvas.keyPressed[51] = true;
                return true;
            }
            case 99: {
                CCanvas.keyPressed[48] = true;
                return true;
            }
            case 100: {
                CCanvas.keyHold[36] = true;
                CCanvas.keyPressed[36] = true;
                return true;
            }
            case 101: {
                CCanvas.keyHold[43] = true;
                CCanvas.keyPressed[43] = true;
                return true;
            }
            case 103: {
                CCanvas.keyHold[31] = true;
                CCanvas.keyPressed[31] = true;
                return true;
            }
            case 104: {
                CCanvas.keyPressed[33] = true;
                return true;
            }
            case 105: {
                CCanvas.keyHold[46] = true;
                CCanvas.keyPressed[46] = true;
                return true;
            }
            case 106: {
                CCanvas.keyHold[35] = true;
                CCanvas.keyPressed[35] = true;
                return true;
            }
            case 107: {
                CCanvas.keyPressed[37] = true;
                return true;
            }
            case 108: {
                CCanvas.keyHold[39] = true;
                CCanvas.keyPressed[39] = true;
                break;
            }
            case 109: {
                CCanvas.keyHold[42] = true;
                CCanvas.keyPressed[42] = true;
                return true;
            }
            case 111: {
                CCanvas.keyHold[44] = true;
                CCanvas.keyPressed[44] = true;
                return true;
            }
            case 112: {
                CCanvas.keyPressed[50] = true;
                return true;
            }
            case 113: {
                CCanvas.keyPressed[47] = true;
                return true;
            }
            case 115: {
                CCanvas.keyPressed[38] = true;
                return true;
            }
            case 119: {
                CCanvas.keyPressed[32] = true;
                return true;
            }
            case 120: {
                CCanvas.keyPressed[49] = true;
                return true;
            }
            case 121: {
                CCanvas.keyHold[45] = true;
                CCanvas.keyPressed[45] = true;
                return true;
            }
        }
        return false;
    }

    public boolean keyReleasedPc(int n) {
        switch (n) {
            case -22: {
                CCanvas.keyHold[41] = false;
                CCanvas.keyPressed[41] = false;
                return true;
            }
            case -21: {
                CCanvas.keyPressed[40] = false;
                break;
            }
            case 97: {
                CCanvas.keyHold[34] = false;
                CCanvas.keyPressed[34] = false;
                return true;
            }
            case 98: {
                CCanvas.keyHold[51] = false;
                CCanvas.keyPressed[51] = false;
                return true;
            }
            case 99: {
                CCanvas.keyHold[48] = false;
                CCanvas.keyPressed[48] = false;
                return true;
            }
            case 100: {
                CCanvas.keyHold[36] = false;
                CCanvas.keyPressed[36] = false;
                return true;
            }
            case 101: {
                CCanvas.keyPressed[43] = false;
                return true;
            }
            case 103: {
                CCanvas.keyHold[31] = false;
                CCanvas.keyPressed[31] = false;
                return true;
            }
            case 104: {
                CCanvas.keyPressed[33] = false;
                return true;
            }
            case 105: {
                CCanvas.keyPressed[46] = false;
                return true;
            }
            case 106: {
                CCanvas.keyHold[35] = false;
                CCanvas.keyPressed[35] = false;
                return true;
            }
            case 107: {
                CCanvas.keyPressed[37] = false;
                return true;
            }
            case 108: {
                CCanvas.keyHold[39] = false;
                CCanvas.keyPressed[39] = false;
                break;
            }
            case 109: {
                CCanvas.keyPressed[42] = false;
                return true;
            }
            case 111: {
                CCanvas.keyPressed[44] = false;
                return true;
            }
            case 112: {
                CCanvas.keyHold[50] = false;
                CCanvas.keyPressed[50] = false;
                return true;
            }
            case 113: {
                CCanvas.keyHold[47] = false;
                CCanvas.keyPressed[47] = false;
                return true;
            }
            case 115: {
                CCanvas.keyPressed[38] = false;
                return true;
            }
            case 119: {
                CCanvas.keyPressed[32] = false;
                return true;
            }
            case 120: {
                CCanvas.keyHold[49] = false;
                CCanvas.keyPressed[49] = false;
                return true;
            }
            case 121: {
                CCanvas.keyPressed[45] = false;
                return true;
            }
        }
        return false;
    }

    public abstract void onClearMap();

    static {
        ipadPro = new int[]{2732, 2048};
        ipadPro1 = new int[]{2224, 1668};
        iphoneXSMAX = new int[]{2688, 1242};
        ipad = new int[]{2048, 1563};
        iphoneX = new int[]{2436, 1125};
        iphoneXR = new int[]{1792, 828};
        ipadMini = new int[]{1334, 750};
        iphone5 = new int[]{1136, 640};
        iphone4 = new int[]{960, 640};
        FPS = 60;
    }
}

