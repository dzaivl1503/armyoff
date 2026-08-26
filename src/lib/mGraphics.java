/*
 * Decompiled with CFR 0.152.
 */
package lib;

import CLib.mImage;

public class mGraphics {
    public mGraphics g;
    public static int zoomLevel = 1;
    public static final int HCENTER = 1;
    public static final int VCENTER = 2;
    public static final int LEFT = 4;
    public static final int RIGHT = 8;
    public static final int TOP = 16;
    public static final int BOTTOM = 32;
    public static final int BASELINE = 64;
    public static final int SOLID = 0;
    public static final int DOTTED = 1;

    public mGraphics(mGraphics mGraphics2) {
        this.g = mGraphics2;
    }

    public mGraphics() {
    }

    public void drawImage(mImage mImage2, int n, int n2, int n3) {
        this.g.drawImage(mImage2, n *= zoomLevel, n2 *= zoomLevel, n3);
    }

    public void drawImage(mImage mImage2, float f, float f2, int n) {
        this.g.drawImage(mImage2, (int)(f *= (float)zoomLevel), (int)(f2 *= (float)zoomLevel), n);
    }

    public void drawLine(int n, int n2, int n3, int n4) {
        this.g.drawLine(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel);
    }

    public void fillRect(int n, int n2, int n3, int n4, int n5, int n6) {
        this.g.setColor(n5);
        this.g.fillRect(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel);
    }

    public void drawRect(int n, int n2, int n3, int n4) {
        this.fillRect(n, n2, 1, n4);
        this.fillRect(n + n3, n2, 1, n4);
        this.fillRect(n, n2, n3, 1);
        this.fillRect(n, n2 + n4, n3 + 1, 1);
    }

    public void drawRegion(mImage mImage2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        this.g.drawRegion(mImage2, n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel, n5, n6 *= zoomLevel, n7 *= zoomLevel, n8);
    }

    public void drawRoundRect(int n, int n2, int n3, int n4, int n5, int n6) {
        this.g.drawRoundRect(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel, n5 *= zoomLevel, n6 *= zoomLevel);
    }

    public void fillTrans(mImage mImage2, int n, int n2, int n3, int n4) {
    }

    public void drawString(String string, int n, int n2, int n3) {
        this.g.drawString(string, n *= zoomLevel, n2 *= zoomLevel, n3);
    }

    public static int blendColor(float f, int n, int n2) {
        float f2 = n2 >> 16 & 0xFF;
        float f3 = n2 >> 8 & 0xFF;
        float f4 = n2 & 0xFF;
        float f5 = (f2 + (float)(n >> 16 & 0xFF)) * f + (float)(n >> 16 & 0xFF);
        float f6 = (f3 + (float)(n >> 8 & 0xFF)) * f + (float)(n >> 8 & 0xFF);
        float f7 = (f4 + (float)(n >> 0 & 0xFF)) * f + (float)(n >> 0 & 0xFF);
        if (f5 > 255.0f) {
            f5 = 255.0f;
        }
        if (f5 < 0.0f) {
            f5 = 0.0f;
        }
        if (f6 > 255.0f) {
            f6 = 255.0f;
        }
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        if (f7 < 0.0f) {
            f7 = 0.0f;
        }
        if (f7 > 255.0f) {
            f7 = 255.0f;
        }
        int n3 = 0xFF000000 | (int)f5 << 16 | (int)f6 << 8 | (int)f7 & 0xFF;
        return n3;
    }

    public static mImage blend(mImage mImage2, float f, int n) {
        return new mImage();
    }

    public void fillRect(int n, int n2, int n3, int n4) {
        this.g.fillRect(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel);
    }

    public void fillRoundRect(int n, int n2, int n3, int n4, int n5, int n6) {
        this.g.fillRoundRect(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel, n5 *= zoomLevel, n6 *= zoomLevel);
    }

    public int getTranslateX() {
        return this.g.getTranslateX() / zoomLevel;
    }

    public int getTranslateY() {
        return this.g.getTranslateY() / zoomLevel;
    }

    public void setClip(int n, int n2, int n3, int n4) {
        this.g.setClip(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel);
    }

    public void setColor(int n) {
        this.g.setColor(n);
    }

    public void setColor(int n, int n2, int n3) {
        this.g.setColor(n, n2, n3);
    }

    public void translate(int n, int n2) {
        this.g.translate(n *= zoomLevel, n2 *= zoomLevel);
    }

    public static int getImageWidth(mImage mImage2) {
        return mImage.getImageWidth(mImage2.image) / zoomLevel;
    }

    public static int getImageHeight(mImage mImage2) {
        return mImage.getImageHeight(mImage2.image) / zoomLevel;
    }

    public static int getRealImageWidth(mImage mImage2) {
        return mImage.getImageWidth(mImage2.image);
    }

    public static int getRealImageHeight(mImage mImage2) {
        return mImage.getImageHeight(mImage2.image);
    }

    public void drawRGB(int[] nArray, int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
    }

    public int getClipX() {
        return 0;
    }

    public int getClipY() {
        return 0;
    }

    public int getClipWidth() {
        return 0;
    }

    public int getClipHeight() {
        return 0;
    }

    public void fillArc(int n, int n2, int n3, int n4, int n5, int n6) {
    }

    public void fillTriangle(int n, int n2, int n3, int n4, int n5, int n6) {
    }
}

