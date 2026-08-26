/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;
import java.util.Vector;
import model.Font;
import model.Popup;

public class InfoPopup
extends Popup {
    private static InfoPopup me;
    private Vector list = new Vector();
    int x;
    int lim;

    public static InfoPopup gI() {
        return me == null ? (me = new InfoPopup()) : me;
    }

    public void show() {
        boolean bl = false;
        for (int i = 0; i < CCanvas.arrPopups.size(); ++i) {
            Popup popup = (Popup)CCanvas.arrPopups.elementAt(i);
            if (!(popup instanceof InfoPopup)) continue;
            bl = true;
            break;
        }
        if (!bl) {
            super.show();
        }
    }

    public void setInfo(String string) {
        if (this.list.size() == 0) {
            this.lim = -Font.normalFont.getWidth(string);
        }
        this.list.addElement(string);
        if (this.list.size() == 1) {
            this.x = CCanvas.width;
        }
    }

    public void update() {
        this.x -= 2;
        if (this.x < this.lim) {
            this.x = CCanvas.width;
            this.list.removeElementAt(0);
            if (this.list.size() <= 0) {
                CCanvas.arrPopups.removeElement(this);
            } else {
                this.lim = -Font.normalFont.getWidth((String)this.list.elementAt(0));
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        if (!CCanvas.isIos()) {
            mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
            mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
            mGraphics2.setClip(0, 0, CCanvas.width, 50);
            mGraphics2.translate(this.x, 0);
            String string = (String)this.list.elementAt(0);
            if (CCanvas.curScr != CCanvas.gameScr) {
                Font.borderFont.drawString(mGraphics2, string, 10, 2, 0);
            } else {
                Font.borderFont.drawString(mGraphics2, string, 10, 20, 0);
            }
        }
    }
}

