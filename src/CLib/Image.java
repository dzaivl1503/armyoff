/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Graphics
 *  javax.microedition.lcdui.Image
 */
package CLib;

import CLib.LibSysTem;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import model.CRes;
import model.IAction2;

public class Image {
    public javax.microedition.lcdui.Image midpImage;
    public int width;
    public int height;

    private void setMidp(javax.microedition.lcdui.Image image) {
        this.midpImage = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
    }

    public static Image createImage(String string) {
        Image image = new Image();
        try {
            InputStream inputStream = LibSysTem.openResource("/" + LibSysTem.res + string);
            if (inputStream != null) {
                image.setMidp(javax.microedition.lcdui.Image.createImage((InputStream)inputStream));
            }
        }
        catch (Exception iOException) {
            iOException.printStackTrace();
        }
        return image;
    }

    public static void createImage(String string, IAction2 iAction2) {
        Image image = Image.createImage(string);
        if (iAction2 != null) {
            iAction2.perform(image);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int _getWidth() {
        return this.width;
    }

    public int _getHeight() {
        return this.height;
    }

    public static Image createImage(int n, int n2) {
        Image image = new Image();
        image.setMidp(javax.microedition.lcdui.Image.createImage((int)n, (int)n2));
        return image;
    }

    public static Image createImage(byte[] byArray, int n, int n2) {
        Image image = new Image();
        image.setMidp(javax.microedition.lcdui.Image.createImage((byte[])byArray, (int)n, (int)n2));
        return image;
    }

    public static Image createImage(byte[] byArray, int n, int n2, IAction2 iAction2) {
        Image image = new Image();
        image.setMidp(javax.microedition.lcdui.Image.createImage((byte[])byArray, (int)n, (int)n2));
        if (iAction2 != null) {
            iAction2.perform(image);
        }
        return image;
    }

    public static Image createImage(byte[] byArray, int n, int n2, String string) {
        Image image = new Image();
        image.setMidp(javax.microedition.lcdui.Image.createImage((byte[])byArray, (int)n, (int)n2));
        return image;
    }

    public static Image createImage(byte[] byArray, int n, int n2, boolean bl) {
        Image image = new Image();
        try {
            image.setMidp(javax.microedition.lcdui.Image.createImage((byte[])byArray, (int)n, (int)n2));
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return image;
    }

    public static byte[] int2byte(int[] nArray) {
        int n = nArray.length;
        byte[] byArray = new byte[n << 2];
        for (int i = 0; i < n; ++i) {
            int n2 = nArray[i];
            int n3 = i << 2;
            byArray[n3++] = (byte)(n2 >>> 0 & 0xFF);
            byArray[n3++] = (byte)(n2 >>> 8 & 0xFF);
            byArray[n3++] = (byte)(n2 >>> 16 & 0xFF);
            byArray[n3++] = (byte)(n2 >>> 24 & 0xFF);
        }
        return byArray;
    }

    private static int[] normalizeAlpha(int[] nArray) {
        int[] nArray2 = new int[nArray.length];
        for (int i = 0; i < nArray.length; ++i) {
            int n = nArray[i];
            nArray2[i] = n == 0xFFFFFF || n == -16777215 ? 0 : n;
        }
        return nArray2;
    }

    public static Image createImage(int[] nArray, int n, int n2) {
        Image image = new Image();
        image.setMidp(javax.microedition.lcdui.Image.createRGBImage((int[])Image.normalizeAlpha(nArray), (int)n, (int)n2, (boolean)true));
        return image;
    }

    public static Image createImageNotRunable(int[] nArray, int n, int n2) {
        return Image.createImage(nArray, n, n2);
    }

    public static Image createImage(int[] nArray, int n, int n2, IAction2 iAction2) {
        Image image = Image.createImage(nArray, n, n2);
        if (iAction2 != null) {
            iAction2.perform(image);
        }
        return image;
    }

    public static Image createImage(Image image, int n, int n2, int n3, int n4) {
        if (image == null) {
            throw new IllegalArgumentException("Image scr is NULL-----------.");
        }
        Image image2 = new Image();
        image2.setMidp(javax.microedition.lcdui.Image.createImage((javax.microedition.lcdui.Image)image.midpImage, (int)n, (int)n2, (int)n3, (int)n4, (int)0));
        return image2;
    }

    public static Image createImageMiniMap(Image image, int n, int n2, int[] nArray, int n3, int n4) {
        if (image == null) {
            throw new IllegalArgumentException("Image imgTile is NULL-----------.");
        }
        Image image2 = Image.createImage(n * n4, n2 * n4);
        Graphics graphics = image2.midpImage.getGraphics();
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n2; ++j) {
                int n5 = nArray[j * n + i] - 1;
                if (n5 <= n3) continue;
                graphics.drawRegion(image.midpImage, 0, n5 * n4, n4, n4, 0, i * n4, (n2 - 1 - j) * n4, 20);
            }
        }
        return image2;
    }

    public static DataInputStream openFile(String string) {
        return new DataInputStream(LibSysTem.getResourceAsStream(string));
    }

    public static int argb(int n, int n2, int n3, int n4) {
        return n << 24 | n2 << 16 | n3 << 8 | n4;
    }

    public void getRGB(int[] nArray, int n, int n2, int n3, int n4, int n5, int n6) {
        if (this.midpImage == null) {
            throw new IllegalArgumentException("texture Image getRGB is NULL-----------.");
        }
        this.midpImage.getRGB(nArray, n, n2, n3, n4, n5, n6);
    }

    public static Image createRGBImage(int[] nArray, int n, int n2, boolean bl) {
        return Image.createImage(nArray, n, n2);
    }

    public static Image createRGBImage(int[] nArray, int n, int n2, boolean bl, IAction2 iAction2) {
        return Image.createImage(nArray, n, n2, iAction2);
    }

    public static Image createImage(byte[] byArray, int n, int n2, String string, String string2) {
        Image image = new Image();
        image.setMidp(javax.microedition.lcdui.Image.createImage((byte[])byArray, (int)n, (int)n2));
        CRes.onSaveToFile(image, string, string2);
        return image;
    }
}

