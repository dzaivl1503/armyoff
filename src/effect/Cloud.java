/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import map.Background;
import screen.PrepareScr;

public class Cloud {
    public static mImage imgSun;
    public static mImage[] imgCloud;
    private static int[] yCloud;
    private static int[] xCloud;
    private static int[] dxCloud;
    private static int xB1;
    private static int xB2;

    public static void updateCloud() {
        if (imgCloud == null || imgCloud[0] == null) {
            imgSun = PrepareScr.imgSun;
            imgCloud = PrepareScr.imgCloud;
        }
        if (imgCloud == null) return;
        int[] nArray;
        for (int i = 0; i < 2; ++i) {
            nArray = xCloud;
            int n = i;
            nArray[n] = nArray[n] + dxCloud[i];
            if (xCloud[i] <= CCanvas.width) continue;
            Cloud.xCloud[i] = -Cloud.imgCloud[i].image.getWidth();
        }
        nArray = xCloud;
        nArray[3] = nArray[3] + dxCloud[3];
        if (xCloud[2] > CCanvas.width) {
            Cloud.xCloud[2] = -Cloud.imgCloud[2].image.getWidth();
        }
        if (xCloud[3] > CCanvas.width) {
            Cloud.xCloud[3] = -Cloud.imgCloud[2].image.getWidth();
        }
        if (CCanvas.gameTick % 2 == 0) {
            nArray = xCloud;
            nArray[2] = nArray[2] + dxCloud[2];
        }
    }

    public static void balloonUpdate() {
        if (CCanvas.gameTick % 2 == 0) {
            ++xB2;
        }
        if (CCanvas.gameTick % 3 == 0) {
            ++xB1;
        }
        if (xB1 > CCanvas.width) {
            xB1 = -Background.balloon.image.getWidth();
        }
        if (xB2 > CCanvas.width) {
            xB2 = -Background.balloon.image.getWidth();
        }
    }

    public static void paintCloud(mGraphics mGraphics2) {
        if (imgCloud == null || imgCloud[0] == null) {
            imgSun = PrepareScr.imgSun;
            imgCloud = PrepareScr.imgCloud;
        }
        if (imgSun != null) mGraphics2.drawImage(imgSun, 30, 40, 0, false);
        if (imgCloud != null) {
            for (int i = 2; i >= 0; --i) {
                if (i < imgCloud.length && imgCloud[i] != null) {
                    mGraphics2.drawImage(imgCloud[i], xCloud[i], yCloud[i], 0, false);
                }
            }
        }
    }

    public static void paintBalloonWithCloud(mGraphics mGraphics2) {
        if (imgCloud == null || imgCloud[0] == null) {
            imgCloud = PrepareScr.imgCloud;
        }
        if (Background.sun != null) mGraphics2.drawImage(Background.sun, CCanvas.width - 20, 20, mGraphics.TOP | mGraphics.RIGHT, false);
        mGraphics2.drawImage(imgCloud[2], xCloud[2], 100, 0, false);
        mGraphics2.drawImage(imgCloud[2], xCloud[3], 20, 0, false);
        mGraphics2.drawImage(imgCloud[1], xCloud[1], yCloud[1], 0, false);
        mGraphics2.drawImage(Background.balloon, xB1, 20, 0, false);
        mGraphics2.drawImage(Background.balloon, xB2, 50, 0, false);
        mGraphics2.drawImage(imgCloud[0], xCloud[0], yCloud[0], 0, false);
    }

    public static void paintSimpleClound(int n, int n2, int n3, mGraphics mGraphics2) {
        mGraphics2.drawImage(imgCloud[n], n2, n3, 0, false);
    }

    static {
        xB1 = 60;
        xB2 = 170;
        xCloud = new int[]{0, CCanvas.hw, 20, CCanvas.width / 2 + 10};
        yCloud = new int[]{30, 80, 40, 0};
        dxCloud = new int[]{2, 1, 1, 1};
        try {
            imgSun = PrepareScr.imgSun;
            imgCloud = PrepareScr.imgCloud;
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

