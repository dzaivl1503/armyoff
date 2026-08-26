/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mImage;

public class ImageIcon {
    public int id;
    public mImage img;

    public ImageIcon(int n, mImage mImage2) {
        this.id = n;
        this.img = mImage2;
    }

    public ImageIcon(int n, byte[] byArray, int n2) {
        this.img = mImage.createImage(byArray, 0, n2, "");
    }
}

