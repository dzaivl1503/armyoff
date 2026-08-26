/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;
import java.util.Vector;
import model.Font;
import network.Command;
import screen.CScreen;
import screen.GameScr;

public class PauseMenu {
    public boolean isShow;
    public Vector menuItems;
    public int menuSelectedItem;
    public int menuX;
    public int menuY;
    public int menuW;
    public int menuH;
    public int menuTemY;

    public void startAt(Vector vector) {
        int n = CCanvas.isTouch ? 30 : 24;
        this.menuItems = vector;
        this.menuW = 0;
        this.menuH = 0;
        for (int i = 0; i < vector.size(); ++i) {
            Command command = (Command)vector.elementAt(i);
            int n2 = Font.bigFont.getWidth(command.caption);
            if (n2 > this.menuW) {
                this.menuW = n2;
            }
            this.menuH += n;
        }
        this.menuW += 10;
        if (this.menuW < 100) {
            this.menuW = 100;
        }
        this.menuH += 4;
        this.menuX = (CCanvas.width >> 1) - (this.menuW >> 1);
        this.menuY = CCanvas.hieght - this.menuH >> 1;
        this.menuTemY = CCanvas.hieght - 24;
        this.isShow = true;
        this.menuSelectedItem = 0;
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (this.menuItems == null || this.menuItems.size() == 0) {
            CScreen.clearKey();
            return;
        }
        if (CCanvas.keyPressed[2] || CScreen.keyUp) {
            --this.menuSelectedItem;
            if (this.menuSelectedItem < 0) {
                this.menuSelectedItem = this.menuItems.size() - 1;
            }
        } else if (CCanvas.keyPressed[8] || CScreen.keyDown) {
            ++this.menuSelectedItem;
            if (this.menuSelectedItem > this.menuItems.size() - 1) {
                this.menuSelectedItem = 0;
            }
        } else if (CCanvas.keyPressed[5] || CScreen.getCmdPointerPressed((byte)2, n3, true)) {
            this.performSelected();
        } else if (CCanvas.keyPressed[12] || CCanvas.keyPressed[13] || CScreen.getCmdPointerPressed((byte)1, n3, true)) {
            this.dismiss();
        }
        CScreen.clearKey();
    }

    private void dismiss() {
        this.isShow = false;
        if (CCanvas.curScr instanceof GameScr) {
            ((GameScr)CCanvas.curScr).isShowPausemenu = false;
        }
    }

    private void performSelected() {
        this.isShow = false;
        if (CCanvas.curScr instanceof GameScr) {
            ((GameScr)CCanvas.curScr).isShowPausemenu = false;
        }
        ((Command)this.menuItems.elementAt((int)this.menuSelectedItem)).action.perform();
    }

    public void onPointerDrag(int n, int n2, int n3) {
    }

    public void onPointerRealeased(int n, int n2, int n3) {
        int n4;
        int n5;
        if (CCanvas.isTouchOnGamePad(n, n2)) {
            return;
        }
        int n6 = n5 = CCanvas.isTouch ? 30 : 24;
        if (CCanvas.isPointerLast(this.menuX, this.menuY, this.menuW, this.menuH, n3) && (n4 = (n2 - this.menuY) / n5) >= 0 && n4 < this.menuItems.size()) {
            if (this.menuSelectedItem != n4) {
                this.menuSelectedItem = n4;
            } else {
                this.performSelected();
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        if (CCanvas.isDebugging()) {
            mGraphics2.setColor(2263535);
            mGraphics2.fillRect(this.menuX, this.menuY, this.menuW, this.menuH, false);
        }
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        CScreen.paintBorderRect(mGraphics2, this.menuY - 25, 4, this.menuH + 25, "");
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        int n = CCanvas.isTouch ? 30 : 24;
        for (int i = 0; i < this.menuItems.size(); ++i) {
            mGraphics2.setColor(0);
            if (i == this.menuSelectedItem) {
                mGraphics2.setColor(16767817);
                mGraphics2.fillRect(CCanvas.width / 2 - 85, this.menuY + i * n - 1, 170, 24, false);
            }
            Font.bigFont.drawString(mGraphics2, ((Command)this.menuItems.elementAt((int)i)).caption, CCanvas.hw, this.menuY + i * n, 2);
        }
    }

    public void update() {
        if (this.menuTemY > this.menuY) {
            int n = this.menuTemY - this.menuY >> 1;
            if (n < 1) {
                n = 1;
            }
            this.menuTemY -= n;
        }
        this.menuTemY = this.menuY;
    }
}

