/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Font
 *  javax.microedition.lcdui.Graphics
 */
package CLib;

import CLib.Image;
import CLib.mImage;
import CLib.mSystem;
import CLib.mVector;
import coreLG.CCanvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class mGraphics {
    public static int zoomLevel = 1;
    public Graphics g;
    public int clipX;
    public int clipY;
    public int clipW;
    public int clipH;
    private boolean isClip;
    public static int HCENTER = 1;
    public static int VCENTER = 2;
    public static int LEFT = 4;
    public static int RIGHT = 8;
    public static int TOP = 16;
    public static int BOTTOM = 32;
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT90 = 5;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;
    public boolean isRorate;
    public int xRotate;
    public int yRotate;
    public float rotation;

    public mGraphics(Graphics graphics) {
        this.g = graphics;
    }

    public mGraphics() {
    }

    public void translate(int n, int n2) {
        this.g.translate(n * zoomLevel, n2 * zoomLevel);
    }

    public void begin() {
    }

    public void end() {
    }

    public int getTranslateX() {
        return this.g.getTranslateX() / zoomLevel;
    }

    public int getTranslateY() {
        return this.g.getTranslateY() / zoomLevel;
    }

    public void enableBlending(float f) {
    }

    public void disableBlending() {
    }

    public void setClip(int n, int n2, int n3, int n4) {
        if (n3 <= 0) {
            n3 = 1;
        }
        if (n4 <= 0) {
            n4 = 1;
        }
        this.clipX = n * zoomLevel;
        this.clipY = n2 * zoomLevel;
        this.clipW = n3 * zoomLevel;
        this.clipH = n4 * zoomLevel;
        this.isClip = true;
    }

    public void setClipTrung(int n, int n2, int n3, int n4) {
        this.setClip(n, n2, n3, n4);
    }

    public void beginClip() {
        if (!this.isClip || this.clipW <= 0 || this.clipH <= 0) {
            return;
        }
        this.g.setClip(this.clipX, this.clipY, this.clipW, this.clipH);
    }

    public void endClip() {
        this.g.setClip(0, 0, CCanvas.width, CCanvas.hieght);
    }

    public void endClip0() {
        this.g.setClip(0, 0, CCanvas.width, CCanvas.hieght);
    }

    public void resetClip() {
        this.isClip = false;
        this.clipX = 0;
        this.clipY = 0;
        this.clipW = CCanvas.width;
        this.clipH = CCanvas.hieght;
        this.g.setClip(0, 0, CCanvas.width, CCanvas.hieght);
    }

    public boolean isClipWithWHZero() {
        return this.isClip && (this.clipH == 0 || this.clipW == 0);
    }

    public int getClipX() {
        return this.isClip ? this.clipX : 0;
    }

    public int getClipY() {
        return this.isClip ? this.clipY : 0;
    }

    public int getClipWidth() {
        return this.isClip ? this.clipW : CCanvas.width;
    }

    public int getClipHeight() {
        return this.isClip ? this.clipH : CCanvas.hieght;
    }

    public void setColor(int n) {
        this.g.setColor(n & 0xFFFFFF);
    }

    public void setColor(int n, float f) {
        this.g.setColor(n & 0xFFFFFF);
    }

    public void setColor(int n, int n2, int n3) {
        this.g.setColor(n & 0xFFFFFF);
    }

    public void resetRotate() {
        this.isRorate = false;
        this.xRotate = 0;
        this.yRotate = 0;
    }

    public void rotate(int n, int n2, int n3) {
        if (n != 0) {
            this.isRorate = true;
            this.rotation = n;
            this.xRotate = n2;
            this.yRotate = n3;
        }
    }

    private void applyClip(boolean bl) {
        if (this.isClip && bl) {
            this.beginClip();
        }
    }

    private void releaseClip(boolean bl) {
        if (this.isClip && bl) {
            this.endClip0();
        }
    }

    public void drawRegion(mImage mImage2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        this.drawRegion(mImage2, n, n2, n3, n4, n5, n6, n7, n8, false, bl);
    }

    public void drawRegion(mImage mImage2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl, boolean bl2) {
        if (mImage2 == null || mImage2.image == null || mImage2.image.midpImage == null) {
            return;
        }
        n6 *= zoomLevel;
        n7 *= zoomLevel;
        if (mImage2.isRegion) {
            this._drawRegion(mImage2.image, mImage2.regionX, mImage2.regionY, n3, n4, n5, n6, n7, n8, bl2);
        } else {
            this._drawRegion(mImage2.image, n, n2, n3, n4, n5, n6, n7, n8, bl2);
        }
    }

    public void drawRegionNotSetClip(mImage mImage2, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8) {
        if (mImage2 == null || mImage2.image == null || mImage2.image.midpImage == null) {
            return;
        }
        this._drawRegion(mImage2.image, n *= zoomLevel, n2 *= zoomLevel, n3, n4, n5, n6, n7, n8, false);
    }

    public void fillTriangle(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        this.applyClip(bl);
        this.g.fillTriangle(n, n2, n3, n4, n5, n6);
        this.releaseClip(bl);
    }

    public void drawImageMap(mImage mImage2, int n, int n2) {
        if (mImage2 == null || mImage2.image == null || mImage2.image.midpImage == null) {
            return;
        }
        this.g.drawImage(mImage2.image.midpImage, n *= zoomLevel, n2 *= zoomLevel, 20);
    }

    public void drawImage(mImage mImage2, int n, int n2, int n3, boolean bl, boolean bl2) {
        if (mImage2 == null || mImage2.image == null || mImage2.image.midpImage == null) {
            return;
        }
        n *= zoomLevel;
        n2 *= zoomLevel;
        if (mImage2.isRegion) {
            int n4 = mImage2.regionW > 0 ? mImage2.regionW : mImage2.image._getWidth();
            int n5 = mImage2.regionH > 0 ? mImage2.regionH : mImage2.image._getHeight();
            this._drawRegion(mImage2.image, mImage2.regionX, mImage2.regionY, n4, n5, 0, n, n2, n3, bl);
        } else {
            this._drawRegion(mImage2.image, 0, 0, mImage2.image._getWidth(), mImage2.image._getHeight(), 0, n, n2, n3, bl);
        }
    }

    public void drawImage(mImage mImage2, int n, int n2, int n3, boolean bl) {
        this.drawImage(mImage2, n, n2, n3, bl, false);
    }

    public void _drawImage(Image image, int n, int n2, int n3, boolean bl) {
        if (image == null || image.midpImage == null) {
            return;
        }
        this.applyClip(bl);
        this.g.drawImage(image.midpImage, n, n2, mGraphics.sanitizeAnchor(n3));
        this.releaseClip(bl);
    }

    private static int sanitizeAnchor(int n) {
        int n2 = n & (TOP | VCENTER | BOTTOM);
        int n3 = n2 == TOP || n2 == VCENTER || n2 == BOTTOM ? n2 : TOP;
        int n4 = n & (LEFT | HCENTER | RIGHT);
        int n5 = n4 == LEFT || n4 == HCENTER || n4 == RIGHT ? n4 : LEFT;
        return n5 | n3;
    }

    public void drawRect(int n, int n2, int n3, int n4, boolean bl) {
        this.applyClip(bl);
        this.g.drawRect(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel);
        this.releaseClip(bl);
    }

    public void drawRoundRect(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        this.applyClip(bl);
        this.g.drawRoundRect(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel, n5, n6);
        this.releaseClip(bl);
    }

    public void fillRoundRect(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        this.applyClip(bl);
        this.g.fillRoundRect(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel, n5, n6);
        this.releaseClip(bl);
    }

    public void drawString(mVector mVector2) {
    }

    public void drawStringNotSetClip(mVector mVector2) {
    }

    public void drawString(String string, float f, float f2, Font font, int n, boolean bl) {
        if (string == null || font == null) {
            return;
        }
        this.applyClip(bl);
        Font font2 = this.g.getFont();
        this.g.setFont(font);
        this.g.drawString(string, (int)f, (int)f2, n);
        this.g.setFont(font2);
        this.releaseClip(bl);
    }

    public static Image blend(Image image, float f, int n) {
        return image;
    }

    public void fillRecAlpla(int n, int n2, int n3, int n4, int n5) {
    }

    public void saveCanvas() {
    }

    public void ClipRec(int n, int n2, int n3, int n4) {
    }

    public static void resetTransAndroid(mGraphics mGraphics2) {
    }

    public void restoreCanvas() {
    }

    public void translateAndroid(int n, int n2) {
    }

    public void fillArc(int n, int n2, mImage mImage2, int n3, int n4, boolean bl) {
        int n5 = Math.abs(n4);
        mImage mImage3 = mSystem.imgCircle_45;
        if (n5 == 60) {
            mImage3 = mSystem.imgCircle_30;
        } else if (n5 == 70) {
            mImage3 = mSystem.imgCircle_20;
        } else if (n5 == 90) {
            mImage3 = mSystem.imgCircle_0;
        }
        if (n4 > 0) {
            this.drawRegion(mImage3, 0, 0, mImage3.image.getWidth(), mImage3.image.getHeight(), 0, n, n2, 0, bl);
        } else if (n4 < 0) {
            this.drawRegion(mImage3, 0, 0, mImage3.image.getWidth(), mImage3.image.getHeight(), 2, n, n2, 0, bl);
        }
    }

    public void fillArc(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        this.applyClip(bl);
        this.g.fillArc(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel, n5, n6);
        this.releaseClip(bl);
    }

    public void fillArc(int n, int n2, int n3, int n4, int n5) {
        int n6 = n3 * 2 * zoomLevel;
        this.g.fillArc((n *= zoomLevel) - n3 * zoomLevel, (n2 *= zoomLevel) - n3 * zoomLevel, n6, n6, n4, n5);
    }

    public void drawRGB(int[] nArray, int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        this.g.drawRGB(nArray, n, n2, n3, n4, n5, n6, bl);
    }

    public void fillRect(int n, int n2, int n3, int n4, boolean bl) {
        n *= zoomLevel;
        n2 *= zoomLevel;
        if ((n3 *= zoomLevel) < 0 || (n4 *= zoomLevel) < 0) {
            return;
        }
        this.applyClip(bl);
        this.g.fillRect(n, n2, n3, n4);
        this.releaseClip(bl);
    }

    private void _drawRegion(Image image, int n, int n2, int n3, int n4, int n5, int n6, int n7, int n8, boolean bl) {
        if (image == null || image.midpImage == null) {
            return;
        }
        this.applyClip(bl);
        this.g.drawRegion(image.midpImage, n, n2, n3, n4, n5, n6, n7, mGraphics.sanitizeAnchor(n8));
        this.releaseClip(bl);
    }

    public void drawLine(int n, int n2, int n3, int n4, boolean bl) {
        this.applyClip(bl);
        this.g.drawLine(n *= zoomLevel, n2 *= zoomLevel, n3 *= zoomLevel, n4 *= zoomLevel);
        this.releaseClip(bl);
    }

    public void drawRecAlpa(int n, int n2, int n3, int n4, int n5) {
        n *= zoomLevel;
        n2 *= zoomLevel;
        if ((n3 *= zoomLevel) < 0 || (n4 *= zoomLevel) < 0) {
            return;
        }
        this.setColor(n5);
        this.g.fillRect(n, n2, n3, n4);
    }

    public void fillRect(int n, int n2, int n3, int n4, int n5, int n6, boolean bl) {
        n *= zoomLevel;
        n2 *= zoomLevel;
        this.setColor(n5);
        this.applyClip(bl);
        this.g.fillRect(n, n2, n3, n4);
        this.releaseClip(bl);
    }
}


