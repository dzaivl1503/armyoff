/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import CLib.mSystem;
import coreLG.CCanvas;
import model.CRes;
import model.Dialog;
import model.Font;
import model.IAction;
import network.Command;
import screen.CScreen;

public class MsgDlg
extends Dialog {
    protected String[] info = null;
    private long timeShow;
    private long timeCountDown;
    private IAction action;

    public void show() {
        CCanvas.currentDialog = this;
    }

    public void close() {
        this.action = null;
        this.timeShow = -1L;
    }

    public void setInfo(String string, Command command, Command command2, Command command3) {
        this.info = Font.normalFont.splitFontBStrInLine(string, CCanvas.width - 50);
        this.left = command;
        this.center = command2;
        this.right = command3;
        this.timeShow = -1L;
    }

    public void setInfo(String string, long l, IAction iAction, Command command, Command command2, Command command3) {
        this.info = Font.normalFont.splitFontBStrInLine(string, CCanvas.width - 50);
        this.left = command;
        this.center = command2;
        this.right = command3;
        this.timeShow = l;
        this.action = iAction;
    }

    public void setInfo(String string, long l, IAction iAction) {
        this.info = Font.normalFont.splitFontBStrInLine(string, CCanvas.width - 50);
        this.timeCountDown = l;
        this.timeShow = l;
        this.action = iAction;
    }

    public void update() {
        if (this.timeShow != -1L && this.timeShow <= mSystem.currentTimeMillis()) {
            CCanvas.endDlg();
            if (this.action != null) {
                this.action.perform();
                this.action = null;
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        int n = 0;
        if (CCanvas.hieght < 200) {
            n += 10;
        }
        int n2 = 0;
        if (this.info != null) {
            int n3;
            if (this.info.length > 3) {
                n2 = (this.info.length - 3) * 8;
            }
            if ((n3 = this.info.length) > 0) {
                CScreen.paintDefaultPopup(8, CCanvas.hieght - 112 - n2, CCanvas.width - 16, 69 + 2 * n2, mGraphics2);
                if (Font.normalFont != null) {
                    int n4 = CCanvas.hieght - 20 - 50 - (n3 * Font.normalFont.getHeight() >> 1) + n;
                    int n5 = 0;
                    int n6 = n4;
                    while (n5 < n3) {
                        if (!CRes.isNullOrEmpty(this.info[n5])) {
                            Font.normalFont.drawString(mGraphics2, this.info[n5], CCanvas.hw, n6 - 10, 2);
                        }
                        ++n5;
                        n6 += Font.normalFont.getHeight();
                    }
                }
            }
            super.paint(mGraphics2);
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }
}

