/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import effect.Camera;
import model.Font;
import model.Popup;
import screen.GameScr;

public class ChatPopup
extends Popup {
    public static mImage imgChat = GameScr.imgChat;
    public int timeOut;
    public int xArrow;
    public int yArrow;
    public int x;
    public int y;
    public int h;
    public int popupW = 60;
    String[] chats;

    public void show(int n, int n2, int n3, String string) {
        this.prepareData(n, n2, n3, string);
    }

    private void prepareData(int n, int n2, int n3, String string) {
        if (Font.smallFontYellow.getWidth(string) < this.popupW) {
            this.popupW = Font.smallFontYellow.getWidth(string) + 10;
        }
        this.chats = Font.smallFontYellow.splitFontBStrInLine(string, this.popupW);
        this.h = 10 * this.chats.length + 4;
        this.xArrow = n2 - 2;
        this.yArrow = n3 - 4;
        this.x = this.xArrow - this.popupW / 2;
        this.y = this.yArrow - this.h;
        if (this.x < 2) {
            this.x = 2;
        }
        if (this.x + 60 > CCanvas.w - 2) {
            this.x = CCanvas.w - 62;
        }
        this.timeOut = n;
    }

    public void showSingle(int n, int n2, int n3, String string) {
        this.prepareData(n, n2, n3, string);
        super.showSingle();
    }

    public void update() {
        if (this.timeOut > 0) {
            --this.timeOut;
            if (this.timeOut == 0) {
                CCanvas.arrPopups.removeElement(this);
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        if (Camera.mode == 0) {
            mGraphics2.translate(mGraphics2.getTranslateX(), mGraphics2.getTranslateY());
        } else {
            mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        }
        mGraphics2.setColor(16711553);
        mGraphics2.fillRect(this.x, this.y, this.popupW, this.h, false);
        mGraphics2.setColor(0);
        mGraphics2.drawRect(this.x, this.y, this.popupW, this.h, false);
        mGraphics2.drawImage(imgChat, this.xArrow, this.yArrow, 0, false);
        int n = this.y + 2;
        for (int i = 0; i < this.chats.length; ++i) {
            Font.smallFont.drawString(mGraphics2, this.chats[i], this.x + 5, n, 0);
            n += 10;
        }
    }
}

