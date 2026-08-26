/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;
import effect.Camera;
import model.Font;
import network.GameService;
import network.Session_ME;
import player.PM;
import screen.CScreen;
import screen.GameScr;

public class CTime {
    int timeInterval;
    public static int seconds;
    long last = this.cur = System.currentTimeMillis();
    long cur;
    boolean visible = true;

    public void initTimeInterval(byte by) {
        this.timeInterval = by;
    }

    public void resetTime() {
        seconds = this.timeInterval;
        this.visible = true;
    }

    public void skipTurn() {
        if (GameScr.pm.isYourTurn()) {
            PM pM = GameScr.pm;
            PM.getMyPlayer().active = false;
            GameService.gI().skipTurn();
            this.visible = false;
        }
    }

    public void update() {
        if (this.visible && !GameScr.trainingMode) {
            this.cur = System.currentTimeMillis();
            if (this.cur - this.last >= 1000L) {
                if (--seconds <= -10 && seconds % 10 == 0) {
                    --seconds;
                    Session_ME.receiveSynchronized = 0;
                    PM.getCurPlayer().x = PM.getCurPlayer().xToNow;
                    PM.getCurPlayer().y = PM.getCurPlayer().yToNow;
                }
                if (CCanvas.curScr == CCanvas.gameScr && seconds <= 0) {
                    seconds = 0;
                    return;
                }
                this.last = this.cur;
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        int n2 = n = CCanvas.isTouch ? 25 : 0;
        if (this.visible) {
            if (CCanvas.curScr == CCanvas.gameScr) {
                Font.bigFont.drawString(mGraphics2, Integer.toString(seconds), Camera.x + CScreen.w - 16, Camera.y + 2 + n, 2, false);
            }
            if (CCanvas.curScr == CCanvas.luckyGifrScreen) {
                Font.bigFont.drawString(mGraphics2, Integer.toString(seconds), CScreen.w - 16, 2, 2, false);
            }
        }
    }

    public void stop() {
        this.visible = false;
    }
}

