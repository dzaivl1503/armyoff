/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;
import model.Font;
import network.Command;
import screen.CScreen;

public abstract class Dialog {
    public Command left;
    public Command center;
    public Command right;

    public void update() {
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        int n = CCanvas.isTouch ? 10 : 3;
        if (this.left != null) {
            Font.normalFont.drawString(mGraphics2, this.left.caption, 5, CCanvas.hieght - Font.normalFont.getHeight() - n, 0);
        }
        if (this.center != null) {
            Font.normalFont.drawString(mGraphics2, this.center.caption, CCanvas.hw, CCanvas.hieght - Font.normalFont.getHeight() - n, 2);
        }
        if (this.right != null) {
            Font.normalFont.drawString(mGraphics2, this.right.caption, CCanvas.width - 5, CCanvas.hieght - Font.normalFont.getHeight() - n, 1);
        }
    }

    public void keyPress(int n) {
    }

    public void onPointerPressed(int n, int n2, int n3) {
    }

    public void onPointerReleased(int n, int n2, int n3) {
        if (CCanvas.isTouchOnGamePad(n, n2)) {
            return;
        }
        this.input(n, n2, n3);
    }

    public void onInputHolder(int n, int n2, int n3) {
    }

    public void handlePadKey(int n) {
        this.input(0, 0, n);
        CScreen.clearKey();
    }

    private void input(int n, int n2, int n3) {
        if (CCanvas.keyPressed[5] || CScreen.getCmdPointerPressed((byte)2, n3, true)) {
            CCanvas.keyPressed[5] = false;
            if (this.center != null && this.center.action != null) {
                this.center.action.perform();
            }
        }
        if (CCanvas.keyPressed[12] || CScreen.getCmdPointerPressed((byte)0, n3, true)) {
            CCanvas.keyPressed[12] = false;
            if (this.left != null && this.left.action != null) {
                this.left.action.perform();
            }
        }
        if (CCanvas.keyPressed[13] || CScreen.getCmdPointerPressed((byte)1, n3, true)) {
            CCanvas.keyPressed[13] = false;
            if (this.right != null && this.right.action != null) {
                this.right.action.perform();
            }
        }
    }

    public abstract void show();

    public abstract void close();
}

