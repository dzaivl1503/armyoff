/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Canvas
 *  javax.microedition.lcdui.Font
 *  javax.microedition.lcdui.Graphics
 */
package com.teamobi.mobiarmy2.j2me;

import com.teamobi.mobiarmy2.j2me.Army2Midlet;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class SplashCanvas
extends Canvas
implements Runnable {
    private final Army2Midlet midlet;
    private volatile boolean armed = true;

    public SplashCanvas(Army2Midlet army2Midlet) {
        this.midlet = army2Midlet;
        this.setFullScreenMode(true);
        Thread thread = new Thread(this);
        thread.start();
    }

    public void run() {
        try {
            Thread.sleep(2000L);
        }
        catch (InterruptedException interruptedException) {
        }
        this.goToMenu();
    }

    private synchronized void goToMenu() {
        if (this.armed) {
            this.armed = false;
            this.midlet.showMenu();
        }
    }

    protected void keyPressed(int n) {
        this.goToMenu();
    }

    protected void paint(Graphics graphics) {
        int n = this.getWidth();
        int n2 = this.getHeight();
        graphics.setColor(1056792);
        graphics.fillRect(0, 0, n, n2);
        graphics.setColor(16766282);
        Font font = Font.getFont((int)64, (int)1, (int)16);
        graphics.setFont(font);
        String string = "ARMY 2";
        int n3 = font.stringWidth(string);
        graphics.drawString(string, (n - n3) / 2, n2 / 2 - 24, 20);
        graphics.setColor(0xCCCCCC);
        Font font2 = Font.getFont((int)0, (int)0, (int)8);
        graphics.setFont(font2);
        String string2 = "Offline Edition - J2ME";
        int n4 = font2.stringWidth(string2);
        graphics.drawString(string2, (n - n4) / 2, n2 / 2 + 8, 20);
        String string3 = "Nhan phim bat ky...";
        int n5 = font2.stringWidth(string3);
        graphics.drawString(string3, (n - n5) / 2, n2 - 20, 20);
    }
}

