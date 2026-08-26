/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import java.util.Vector;
import model.IAction2;

public class Font {
    public static final int LEFT = 0;
    public static final int RIGHT = 1;
    public static final int CENTER = 2;
    public mImage imgFont;
    private String charList;
    public byte[] charWidth;
    private int charHeight;
    private int charSpace;
    public String nameFont;
    public int imgWidth;
    public int imgHeight;
    public static Font normalFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyz\u00e1\u00e0\u1ea3\u00e3\u1ea1\u0103\u1eaf\u1eb1\u1eb3\u1eb5\u1eb7\u00e2\u1ea5\u1ea7\u1ea9\u1eab\u1ead\u00e9\u00e8\u1ebb\u1ebd\u1eb9\u00ea\u1ebf\u1ec1\u1ec3\u1ec5\u1ec7\u00ed\u00ec\u1ec9\u0129\u1ecb\u00f3\u00f2\u1ecf\u00f5\u1ecd\u00f4\u1ed1\u1ed3\u1ed5\u1ed7\u1ed9\u01a1\u1edb\u1edd\u1edf\u1ee1\u1ee3\u00fa\u00f9\u1ee7\u0169\u1ee5\u01b0\u1ee9\u1eeb\u1eed\u1eef\u1ef1\u00fd\u1ef3\u1ef7\u1ef9\u1ef5\u0111ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0110$", new byte[]{4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 9, 7, 7, 8, 8, 6}, 13, "fb.png", 0);
    public static Font normalYFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyz\u00e1\u00e0\u1ea3\u00e3\u1ea1\u0103\u1eaf\u1eb1\u1eb3\u1eb5\u1eb7\u00e2\u1ea5\u1ea7\u1ea9\u1eab\u1ead\u00e9\u00e8\u1ebb\u1ebd\u1eb9\u00ea\u1ebf\u1ec1\u1ec3\u1ec5\u1ec7\u00ed\u00ec\u1ec9\u0129\u1ecb\u00f3\u00f2\u1ecf\u00f5\u1ecd\u00f4\u1ed1\u1ed3\u1ed5\u1ed7\u1ed9\u01a1\u1edb\u1edd\u1edf\u1ee1\u1ee3\u00fa\u00f9\u1ee7\u0169\u1ee5\u01b0\u1ee9\u1eeb\u1eed\u1eef\u1ef1\u00fd\u1ef3\u1ef7\u1ef9\u1ef5\u0111ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0110$@", new byte[]{4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 9, 7, 7, 8, 8, 6, 9}, 13, "fb2.png", 0);
    public static Font normalGFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyz\u00e1\u00e0\u1ea3\u00e3\u1ea1\u0103\u1eaf\u1eb1\u1eb3\u1eb5\u1eb7\u00e2\u1ea5\u1ea7\u1ea9\u1eab\u1ead\u00e9\u00e8\u1ebb\u1ebd\u1eb9\u00ea\u1ebf\u1ec1\u1ec3\u1ec5\u1ec7\u00ed\u00ec\u1ec9\u0129\u1ecb\u00f3\u00f2\u1ecf\u00f5\u1ecd\u00f4\u1ed1\u1ed3\u1ed5\u1ed7\u1ed9\u01a1\u1edb\u1edd\u1edf\u1ee1\u1ee3\u00fa\u00f9\u1ee7\u0169\u1ee5\u01b0\u1ee9\u1eeb\u1eed\u1eef\u1ef1\u00fd\u1ef3\u1ef7\u1ef9\u1ef5\u0111ABCDEFGHIJKLMNOPQRSTUVWXYZ\u00c1\u00c0\u1ea2\u00c3\u1ea0\u0102\u1eae\u1eb0\u1eb2\u1eb4\u1eb6\u00c2\u1ea4\u1ea6\u1ea8\u1eaa\u1eac\u00c9\u00c8\u1eba\u1ebc\u1eb8\u00ca\u1ebe\u1ec0\u1ec2\u1ec4\u1ec6\u00cd\u00cc\u1ec8\u0128\u1eca\u00d3\u00d2\u1ece\u00d5\u1ecc\u00d4\u1ed0\u1ed2\u1ed4\u1ed6\u1ed8\u01a0\u1eda\u1edc\u1ede\u1ee0\u1ee2\u00da\u00d9\u1ee6\u0168\u1ee4\u01af\u1ee8\u1eea\u1eec\u1eee\u1ef0\u00dd\u1ef2\u1ef6\u1ef8\u1ef4\u0110$", new byte[]{4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 9, 7, 7, 8, 8, 6}, 13, "fb3.png", 0);
    public static Font normalRFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyz\u00e1\u00e0\u1ea3\u00e3\u1ea1\u0103\u1eaf\u1eb1\u1eb3\u1eb5\u1eb7\u00e2\u1ea5\u1ea7\u1ea9\u1eab\u1ead\u00e9\u00e8\u1ebb\u1ebd\u1eb9\u00ea\u1ebf\u1ec1\u1ec3\u1ec5\u1ec7\u00ed\u00ec\u1ec9\u0129\u1ecb\u00f3\u00f2\u1ecf\u00f5\u1ecd\u00f4\u1ed1\u1ed3\u1ed5\u1ed7\u1ed9\u01a1\u1edb\u1edd\u1edf\u1ee1\u1ee3\u00fa\u00f9\u1ee7\u0169\u1ee5\u01b0\u1ee9\u1eeb\u1eed\u1eef\u1ef1\u00fd\u1ef3\u1ef7\u1ef9\u1ef5\u0111ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0110$", new byte[]{4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 9, 7, 7, 8, 8, 6}, 13, "fb4.png", 0);
    public static Font borderFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyz\u00e1\u00e0\u1ea3\u00e3\u1ea1\u0103\u1eaf\u1eb1\u1eb3\u1eb5\u1eb7\u00e2\u1ea5\u1ea7\u1ea9\u1eab\u1ead\u00e9\u00e8\u1ebb\u1ebd\u1eb9\u00ea\u1ebf\u1ec1\u1ec3\u1ec5\u1ec7\u00ed\u00ec\u1ec9\u0129\u1ecb\u00f3\u00f2\u1ecf\u00f5\u1ecd\u00f4\u1ed1\u1ed3\u1ed5\u1ed7\u1ed9\u01a1\u1edb\u1edd\u1edf\u1ee1\u1ee3\u00fa\u00f9\u1ee7\u0169\u1ee5\u01b0\u1ee9\u1eeb\u1eed\u1eef\u1ef1\u00fd\u1ef3\u1ef7\u1ef9\u1ef5\u0111ABCDEFGHIJKLMNOPQRSTUVWXYZ\u0110$", new byte[]{4, 6, 5, 6, 6, 7, 6, 6, 6, 6, 6, 3, 3, 3, 4, 5, 4, 4, 6, 5, 8, 8, 6, 6, 10, 6, 7, 5, 7, 6, 4, 7, 7, 3, 4, 6, 3, 9, 7, 7, 7, 7, 5, 5, 4, 7, 6, 9, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 6, 6, 3, 3, 3, 5, 3, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7, 8, 8, 8, 8, 8, 7, 7, 7, 7, 8, 7, 7, 7, 7, 7, 6, 6, 7, 7, 3, 5, 7, 6, 10, 8, 7, 7, 7, 6, 7, 7, 7, 7, 9, 7, 7, 8, 8, 6}, 15, "fb1.png", 0);
    public static Font bigFont = new Font(" 0123456789.,:!?()-'/ABCDEFGHIJKLMNOPQRSTUVWXYZ\u00c1\u00c0\u1ea2\u00c3\u1ea0\u0102\u1eae\u1eb0\u1eb2\u1eb4\u1eb6\u00c2\u1ea4\u1ea6\u1ea8\u1eaa\u1eac\u00c9\u00c8\u1eba\u1ebc\u1eb8\u00ca\u1ebe\u1ec0\u1ec2\u1ec4\u1ec6\u00cd\u00cc\u1ec8\u0128\u1eca\u00d3\u00d2\u1ece\u00d5\u1ecc\u00d4\u1ed0\u1ed2\u1ed4\u1ed6\u1ed8\u01a0\u1eda\u1edc\u1ede\u1ee0\u1ee2\u00da\u00d9\u1ee6\u0168\u1ee4\u01af\u1ee8\u1eea\u1eec\u1eee\u1ef0\u00dd\u1ef2\u1ef6\u1ef8\u1ef4\u0110", new byte[]{4, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 4, 4, 4, 4, 8, 6, 6, 6, 3, 7, 10, 10, 10, 10, 8, 8, 10, 10, 5, 8, 9, 8, 13, 11, 10, 10, 10, 10, 10, 9, 10, 10, 13, 11, 11, 9, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 5, 5, 5, 5, 5, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10}, 21, "fcg14m.png", 0);
    public static Font numberFont = new Font("0123456789$+-", new byte[]{15, 12, 15, 15, 15, 15, 15, 15, 15, 15, 15, 10, 10}, 13, "so.png", -3);
    public static Font smallFontRed = new Font("0123456789+-%$:ABCDEFGHIJKLMNOPQRSTUVWXYZ", new byte[]{5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 7, 5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 7, 6, 5, 5, 5, 5, 5, 5, 5, 5, 7, 5, 5, 5}, 8, "fs0.png", -1);
    public static Font smallFontYellow = new Font("0123456789+-%$:ABCDEFGHIJKLMNOPQRSTUVWXYZ", new byte[]{5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 7, 5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 7, 6, 5, 5, 5, 5, 5, 5, 5, 5, 7, 5, 5, 5}, 8, "fs1.png", -1);
    public static Font smallFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ", new byte[]{2, 5, 4, 5, 5, 6, 5, 5, 5, 5, 5, 2, 3, 2, 2, 5, 4, 4, 6, 6, 6, 6, 4, 3, 8, 5, 5, 4, 5, 5, 4, 5, 5, 2, 3, 5, 2, 8, 5, 5, 5, 5, 4, 4, 3, 5, 6, 8, 4, 4, 4, 6, 5, 6, 6, 5, 5, 6, 6, 4, 4, 5, 5, 8, 7, 7, 5, 7, 6, 6, 6, 6, 6, 8, 6, 6, 5}, 10, "tahoma9.png", 0);
    public static Font arialFont = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyz\u00e1\u00e0\u1ea3\u00e3\u1ea1\u0103\u1eaf\u1eb1\u1eb3\u1eb5\u1eb7\u00e2\u1ea5\u1ea7\u1ea9\u1eab\u1ead\u00e9\u00e8\u1ebb\u1ebd\u1eb9\u00ea\u1ebf\u1ec1\u1ec3\u1ec5\u1ec7\u00ed\u00ec\u1ec9\u0129\u1ecb\u00f3\u00f2\u1ecf\u00f5\u1ecd\u00f4\u1ed1\u1ed3\u1ed5\u1ed7\u1ed9\u01a1\u1edb\u1edd\u1edf\u1ee1\u1ee3\u00fa\u00f9\u1ee7\u0169\u1ee5\u01b0\u1ee9\u1eeb\u1eed\u1eef\u1ef1\u00fd\u1ef3\u1ef7\u1ef9\u1ef5\u0111ABCDEFGHIJKLMNOPQRSTUVWXYZ", new byte[]{3, 5, 3, 5, 5, 5, 5, 5, 5, 5, 5, 1, 1, 1, 1, 5, 3, 2, 5, 4, 5, 6, 2, 2, 9, 5, 5, 4, 5, 5, 3, 5, 5, 1, 1, 5, 1, 9, 5, 5, 5, 5, 3, 5, 2, 5, 4, 8, 4, 4, 4, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 2, 1, 2, 3, 1, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 5, 6, 6, 6, 6, 6, 6, 5, 5, 5, 5, 5, 7, 7, 7, 7, 7, 7, 4, 4, 4, 4, 4, 6, 6, 6, 7, 7, 6, 5, 7, 7, 1, 4, 7, 6, 7, 7, 7, 6, 7, 7, 6, 6, 7, 6, 8, 6, 6, 6, 7, 4}, 14, "arialf.png", 1);
    public static Font blackF = new Font(" 0123456789.,:!?()+*$#/-%abcdefghijklmnopqrstuvwxyz\u00e1\u00e0\u1ea3\u00e3\u1ea1\u0103\u1eaf\u1eb1\u1eb3\u1eb5\u1eb7\u00e2\u1ea5\u1ea7\u1ea9\u1eab\u1ead\u00e9\u00e8\u1ebb\u1ebd\u1eb9\u00ea\u1ebf\u1ec1\u1ec3\u1ec5\u1ec7\u00ed\u00ec\u1ec9\u0129\u1ecb\u00f3\u00f2\u1ecf\u00f5\u1ecd\u00f4\u1ed1\u1ed3\u1ed5\u1ed7\u1ed9\u01a1\u1edb\u1edd\u1edf\u1ee1\u1ee3\u00fa\u00f9\u1ee7\u0169\u1ee5\u01b0\u1ee9\u1eeb\u1eed\u1eef\u1ef1\u00fd\u1ef3\u1ef7\u1ef9\u1ef5\u0111ABCDEFGHIJKLMNOPQRSTUVWXYZ", new byte[]{4, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 2, 2, 2, 2, 6, 4, 4, 6, 5, 6, 7, 4, 4, 10, 6, 6, 6, 6, 6, 4, 6, 6, 2, 2, 5, 2, 8, 6, 6, 6, 6, 4, 6, 3, 6, 6, 10, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 3, 2, 3, 4, 2, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 7, 7, 7, 7, 7, 7, 6, 6, 6, 6, 6, 8, 8, 8, 8, 8, 8, 6, 6, 6, 6, 6, 7, 8, 7, 7, 7, 6, 6, 8, 7, 2, 5, 7, 6, 8, 7, 8, 6, 8, 7, 7, 6, 7, 8, 11, 7, 8, 7, 7, 6, 7}, 13, "arial11b.png", 0);

    public Font(String string, byte[] byArray, int n, String string2, int n2) {
        try {
            this.charSpace = n2;
            this.charList = string;
            this.charWidth = byArray;
            this.charHeight = n;
            this.nameFont = string2;
            mImage.createImage("/font/fontT/" + string2, new IAction2(){

                public void perform(Object object) {
                    Font.this.imgFont = new mImage((Image)object);
                    Font.this.imgWidth = Font.this.imgFont.image.getWidth();
                    Font.this.imgHeight = Font.this.imgFont.image.getHeight();
                }
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private int maxDrawablePos() {
        return this.imgHeight > 0 ? this.imgHeight / this.charHeight - 1 : Integer.MAX_VALUE;
    }

    public void drawString(mGraphics mGraphics2, String string, int n, int n2, int n3, boolean bl) {
        int n4 = string.length();
        int n5 = n3 == 0 ? n : (n3 == 1 ? n - this.getWidth(string) : n - (this.getWidth(string) >> 1));
        int n6 = this.maxDrawablePos();
        for (int i = 0; i < n4; ++i) {
            int n7;
            int n8 = this.charList.indexOf(string.charAt(i));
            if (n8 == -1) {
                n8 = 0;
            }
            int n9 = n7 = n8 > n6 ? n6 : n8;
            if (n7 >= 0) {
                mGraphics2.drawRegion(this.imgFont, 0, n7 * this.charHeight, this.imgWidth, this.charHeight, 0, n5, n2, 20, bl);
            }
            if (n8 > this.charWidth.length - 1) {
                n8 = this.charWidth.length - 1;
            }
            n5 += this.charWidth[n8] + this.charSpace;
        }
    }

    public void drawString(mGraphics mGraphics2, String string, int n, int n2, int n3) {
        int n4 = string.length();
        int n5 = n3 == 0 ? n : (n3 == 1 ? n - this.getWidth(string) : n - (this.getWidth(string) >> 1));
        int n6 = this.maxDrawablePos();
        for (int i = 0; i < n4; ++i) {
            int n7;
            int n8 = this.charList.indexOf(string.charAt(i));
            if (n8 == -1) {
                n8 = 0;
            }
            int n9 = n7 = n8 > n6 ? n6 : n8;
            if (n7 >= 0) {
                mGraphics2.drawRegion(this.imgFont, 0, n7 * this.charHeight, this.imgWidth, this.charHeight, 0, n5, n2, mGraphics.BOTTOM | mGraphics.VCENTER, true);
            }
            if (n8 > this.charWidth.length - 1) {
                n8 = this.charWidth.length - 1;
            }
            n5 += this.charWidth[n8] + this.charSpace;
        }
    }

    public int getWidth(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); ++i) {
            int n2 = this.charList.indexOf(string.charAt(i));
            if (n2 == -1) {
                n2 = 0;
            }
            if (n2 > this.charWidth.length - 1) {
                n2 = this.charWidth.length - 1;
            }
            n += this.charWidth[n2];
        }
        return n;
    }

    public static String replace(String string, String string2, String string3) {
        StringBuffer stringBuffer = new StringBuffer();
        int n = string.indexOf(string2);
        int n2 = 0;
        int n3 = string2.length();
        while (n != -1) {
            stringBuffer.append(string.substring(n2, n)).append(string3);
            n2 = n + n3;
            n = string.indexOf(string2, n2);
        }
        stringBuffer.append(string.substring(n2, string.length()));
        return stringBuffer.toString();
    }

    public String[] splitFontBStrInLine(String string, int n) {
        Vector<String> vector = new Vector<String>();
        int n2 = (string = string.trim()).length();
        if (n2 == 0) {
            return null;
        }
        if (n2 == 1) {
            return new String[]{string};
        }
        String string2 = "";
        int n3 = 0;
        int n4 = 0;
        while (true) {
            if (this.getWidth(string2) < n) {
                string2 = string2 + string.charAt(n4);
                if (string.charAt(++n4) != '\n') {
                    if (n4 < n2 - 1) continue;
                    n4 = n2 - 1;
                }
            }
            if (n4 != n2 - 1 && string.charAt(n4 + 1) != ' ') {
                int n5 = n4;
                while (string.charAt(n4 + 1) != '\n' && (string.charAt(n4 + 1) != ' ' || string.charAt(n4) == ' ') && n4 != n3) {
                    --n4;
                }
                if (n4 == n3) {
                    n4 = n5;
                }
            }
            vector.addElement(string.substring(n3, n4 + 1));
            if (n4 == n2 - 1) break;
            for (n3 = n4 + 1; n3 != n2 - 1 && string.charAt(n3) == ' '; ++n3) {
            }
            if (n3 == n2 - 1) break;
            n4 = n3;
            string2 = "";
        }
        String[] stringArray = new String[vector.size()];
        for (int i = 0; i < vector.size(); ++i) {
            stringArray[i] = (String)vector.elementAt(i);
        }
        return stringArray;
    }

    public int getHeight() {
        return this.charHeight;
    }

    public static void OnSaveImageFont() {
    }
}

