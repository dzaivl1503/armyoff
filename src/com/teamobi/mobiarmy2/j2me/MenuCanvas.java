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

public class MenuCanvas
extends Canvas {
    private static final String[] ITEMS = new String[]{"Choi tiep (offline)", "Choi moi", "Thoat"};
    private final Army2Midlet midlet;
    private int selected = 0;

    public MenuCanvas(Army2Midlet army2Midlet) {
        this.midlet = army2Midlet;
        this.setFullScreenMode(true);
    }

    protected void keyPressed(int n) {
        int n2 = this.getGameAction(n);
        if (n2 == 1) {
            this.selected = (this.selected - 1 + ITEMS.length) % ITEMS.length;
            this.repaint();
        } else if (n2 == 6) {
            this.selected = (this.selected + 1) % ITEMS.length;
            this.repaint();
        } else if (n2 == 8) {
            this.select();
        }
    }

    private void select() {
        if (this.selected == ITEMS.length - 1) {
            this.midlet.exitGame();
        } else {
            this.repaint();
        }
    }

    protected void paint(Graphics graphics) {
        int n = this.getWidth();
        int n2 = this.getHeight();
        graphics.setColor(657930);
        graphics.fillRect(0, 0, n, n2);
        Font font = Font.getFont((int)64, (int)1, (int)0);
        graphics.setFont(font);
        graphics.setColor(16766282);
        graphics.drawString("ARMY 2 OFFLINE", 8, 10, 20);
        Font font2 = Font.getFont((int)0, (int)0, (int)0);
        graphics.setFont(font2);
        int n3 = 50;
        for (int i = 0; i < ITEMS.length; ++i) {
            if (i == this.selected) {
                graphics.setColor(2780970);
                graphics.fillRect(4, n3 - 2, n - 8, font2.getHeight() + 4);
                graphics.setColor(0xFFFFFF);
            } else {
                graphics.setColor(0xAAAAAA);
            }
            graphics.drawString(ITEMS[i], 12, n3, 20);
            n3 += font2.getHeight() + 10;
        }
    }
}

