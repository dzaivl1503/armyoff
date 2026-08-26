/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;

public class Popup {
    public void paint(mGraphics mGraphics2) {
    }

    public void update() {
    }

    public void show() {
        CCanvas.arrPopups.addElement(this);
    }

    public void showSingle() {
        if (CCanvas.arrPopups.contains(this)) {
            CCanvas.arrPopups.removeElement(this);
        }
        this.show();
    }

    public void keyPress(int n) {
    }

    public void onPointerDragged(int n, int n2, int n3) {
    }

    public void onPointerPressed(int n, int n2, int n3) {
    }

    public void onPointerReleased(int n, int n2, int n3) {
    }

    public void hide() {
        if (CCanvas.arrPopups.contains(this)) {
            CCanvas.arrPopups.removeElement(this);
        }
    }
}

