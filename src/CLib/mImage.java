/*
 * Decompiled with CFR 0.152.
 */
package CLib;

import CLib.Image;
import CLib.LibSysTem;
import CLib.TemGraphics;
import java.io.DataInputStream;
import model.IAction2;
import network.Command;

public class mImage {
    public Image image;
    public boolean isRegion;
    public int regionX;
    public int regionY;
    public int regionW;
    public int regionH;
    private static Command cmd;

    public mImage() {
    }

    public mImage(Image image) {
        this.image = image;
    }

    public static String getLink(String string) {
        return string;
    }

    public static String replaceImg(String string) {
        int n = string.indexOf(".img");
        if (n < 0) {
            return string;
        }
        return string.substring(0, n) + ".png" + string.substring(n + 4);
    }

    public static mImage createImage(String string) {
        string = mImage.replaceImg(string);
        mImage mImage2 = new mImage();
        try {
            mImage2.image = Image.createImage(string);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        return mImage2.image == null ? null : mImage2;
    }

    public static void createImage(String string, IAction2 iAction2) {
        string = mImage.replaceImg(string);
        new mImage();
        try {
            Image.createImage(string, iAction2);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static mImage createImageAll(String string) {
        mImage mImage2 = new mImage();
        try {
            mImage2.image = Image.createImage(string);
        }
        catch (Exception exception) {
        }
        return mImage2.image == null ? null : mImage2;
    }

    public static DataInputStream openFile(String string) {
        DataInputStream dataInputStream = null;
        dataInputStream = new DataInputStream(LibSysTem.getResourceAsStream(string));
        return dataInputStream;
    }

    public static mImage createImage(int n, int n2) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(n, n2);
        return mImage2;
    }

    public static mImage createImage(byte[] byArray, int n, int n2) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(byArray, n, n2);
        return mImage2;
    }

    public static mImage createImage(byte[] byArray, int n, int n2, IAction2 iAction2) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(byArray, n, n2, iAction2);
        return mImage2;
    }

    public static mImage createImage(int[] nArray, int n, int n2, IAction2 iAction2) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(nArray, n, n2, iAction2);
        return mImage2;
    }

    public static mImage createImage(byte[] byArray, int n, int n2, String string) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(byArray, n, n2, string);
        return mImage2;
    }

    public static mImage createImage(byte[] byArray, int n, int n2, boolean bl) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(byArray, n, n2, bl);
        return mImage2;
    }

    public static mImage createImage(int[] nArray, int n, int n2) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(nArray, n, n2);
        return mImage2;
    }

    public static mImage createImageNotRunable(int[] nArray, int n, int n2) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImageNotRunable(nArray, n, n2);
        return mImage2;
    }

    public TemGraphics getGraphics() {
        TemGraphics temGraphics = new TemGraphics();
        return temGraphics;
    }

    public static int getImageWidth(Image image) {
        return image == null ? 0 : image._getWidth();
    }

    public static int getImageHeight(Image image) {
        return image == null ? 0 : image._getHeight();
    }

    public void getRGB(int[] nArray, int n, int n2, int n3, int n4, int n5, int n6) {
        this.image.getRGB(nArray, n, n2, n3, n4, n5, n6);
    }

    public static mImage createImage(byte[] byArray, int n, int n2, String string, String string2) {
        mImage mImage2 = new mImage();
        mImage2.image = Image.createImage(byArray, n, n2, string, string2);
        return mImage2;
    }

    public static mImage cutRegion(mImage mImage2, int n, int n2, int n3, int n4) {
        if (mImage2 == null || mImage2.image == null || mImage2.image.midpImage == null || n3 <= 0 || n4 <= 0) {
            return null;
        }
        mImage mImage3 = new mImage();
        mImage3.image = mImage2.image;
        mImage3.isRegion = true;
        mImage3.regionX = n;
        mImage3.regionY = n2;
        mImage3.regionW = n3;
        mImage3.regionH = n4;
        return mImage3;
    }
}

