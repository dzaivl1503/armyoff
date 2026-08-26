/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import Debug.Debug;

public class FrameImage {
    public int frameWidth;
    public int frameHeight;
    public int nFrame;
    public mImage imgFrame;
    private int[] pos;
    private int totalHeight;
    private mImage[] imgList;
    private boolean isRotate;

    public FrameImage(Image image, int n, int n2, boolean bl) {
        this.imgFrame = new mImage(image);
        this.frameWidth = n;
        this.frameHeight = n2;
        this.totalHeight = image.getHeight();
        this.nFrame = this.totalHeight / n2;
        this.pos = new int[this.nFrame];
        for (int i = 0; i < this.nFrame; ++i) {
            this.pos[i] = i * n2;
        }
    }

    public FrameImage(Image image, int n, int n2) {
        this.imgFrame = new mImage(image);
        this.frameWidth = n;
        this.frameHeight = n2;
        this.totalHeight = image.getHeight();
        this.nFrame = this.totalHeight / n2;
        this.pos = new int[this.nFrame];
        for (int i = 0; i < this.nFrame; ++i) {
            this.pos[i] = i * n2;
        }
    }

    public FrameImage(Image image, int n) {
        this.imgFrame = new mImage(image);
        this.frameWidth = image.getWidth();
        this.frameHeight = image.getHeight();
        this.nFrame = n;
        this.imgList = new mImage[this.nFrame];
        this.isRotate = true;
        int n2 = 360 / n;
    }

    public void drawRegionFrame(int n, int n2, int n3, int n4, int n5, mGraphics mGraphics2, int n6, int n7) {
        mGraphics2.drawRegion(this.imgFrame, 0, (n6 * n7 + n) * this.frameWidth, this.frameWidth, this.frameHeight, n4, n2, n3, n5, false);
    }

    public void drawFrame(int n, int n2, int n3, int n4, int n5, mGraphics mGraphics2) {
        if (!this.isRotate) {
            if (n >= 0 && n < this.nFrame) {
                mGraphics2.drawRegion(this.imgFrame, 0, this.pos[n], this.frameWidth, this.frameHeight, n4, n2, n3, n5, true);
            }
        } else if (n >= 0 && n < this.nFrame) {
            int n6 = n * (360 / this.nFrame);
            Debug.isDraw = true;
            if (n6 == 0) {
                mGraphics2.drawImage(this.imgFrame, n2, n3, 3, true);
            } else if (n6 == 90) {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.imgFrame.image.getWidth(), this.imgFrame.image.getHeight(), 4, n2, n3, n5, true);
            } else if (n6 == 270) {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.frameWidth, this.frameHeight, 7, n2, n3, n5, true);
            } else if (n6 == 180) {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.frameWidth, this.frameHeight, 1, n2, n3, n5, true);
            } else if (this.imgList[n] != null) {
                mGraphics2.drawRegion(this.imgList[n], 0, 0, this.frameWidth, this.frameHeight, n4, n2, n3, n5, true);
            } else {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.frameWidth, this.frameHeight, n4, n2, n3, n5, true);
            }
            Debug.isDraw = false;
        }
    }

    public void fillFrame(int n, int n2, int n3, int n4, int n5, int n6, mGraphics mGraphics2, boolean bl) {
        if (n >= 0 && n < this.nFrame && n4 > 0) {
            int n7;
            if (n4 > 100) {
                n4 = 100;
            }
            if ((n7 = this.frameWidth * n4 / 100) > 0) {
                mGraphics2.drawRegion(this.imgFrame, 0, this.pos[n], n7, this.frameHeight, 0, n2, n3, n6, bl);
            }
        }
    }

    public void drawFrame(int n, int n2, int n3, int n4, int n5, mGraphics mGraphics2, boolean bl) {
        if (!this.isRotate) {
            if (n >= 0 && n < this.nFrame) {
                mGraphics2.drawRegion(this.imgFrame, 0, this.pos[n], this.frameWidth, this.frameHeight, n4, n2, n3, n5, bl);
            }
        } else if (n >= 0 && n < this.nFrame) {
            int n6 = n * (360 / this.nFrame);
            if (n6 == 0) {
                mGraphics2.drawImage(this.imgFrame, n2, n3, 3, bl);
            } else if (n6 == 90) {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.imgFrame.image.getWidth(), this.imgFrame.image.getHeight(), 4, n2, n3, n5, bl);
            } else if (n6 == 270) {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.frameWidth, this.frameHeight, 7, n2, n3, n5, bl);
            } else if (n6 == 180) {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.frameWidth, this.frameHeight, 1, n2, n3, n5, bl);
            } else if (this.imgList[n] != null) {
                mGraphics2.drawRegion(this.imgList[n], 0, 0, this.frameWidth, this.frameHeight, n4, n2, n3, n5, bl);
            } else {
                mGraphics2.drawRegion(this.imgFrame, 0, 0, this.frameWidth, this.frameHeight, n4, n2, n3, n5, bl);
            }
        }
    }

    public void unload() {
        this.imgFrame = null;
        this.pos = null;
    }
}

