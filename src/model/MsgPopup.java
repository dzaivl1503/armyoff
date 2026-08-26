/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import model.Font;
import model.Popup;
import screen.GameScr;

public class MsgPopup
extends Popup {
    public static mImage[] imgMsg;
    public int nMessage;

    public MsgPopup() {
        if (imgMsg == null) {
            imgMsg = new mImage[2];
        }
        MsgPopup.imgMsg[0] = GameScr.imgMsg[0];
        MsgPopup.imgMsg[1] = GameScr.imgMsg[1];
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        mGraphics2.setColor(14279153);
        mGraphics2.fillRoundRect(CCanvas.width - 60, 5, 34, 20, 6, 6, false);
        mGraphics2.setColor(4682453);
        mGraphics2.fillRect(CCanvas.width - 60 + 2, 7, 30, 16, false);
        mGraphics2.drawImage(imgMsg[0], CCanvas.width - 60 + 4, 9, 0, false);
        Font.normalFont.drawString(mGraphics2, "" + this.nMessage, CCanvas.width - 60 + 28, 9, 2);
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        if (CCanvas.isPointer(CCanvas.width - 60, 5, 34, 20, n3)) {
            CCanvas.msgScr.show(CCanvas.curScr);
        }
    }
}

