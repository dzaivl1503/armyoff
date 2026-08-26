/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Image
 */
package CLib;

import CLib.Image;
import CLib.mImage;
import CLib.mSystem;

public class ManagerTile {
    public static final byte[] totalTile = new byte[]{73, 60, 57, 64, 47, 43, 66, 61, 52, 51, 61, 62, 67, 69, 59};
    public static mImage[] tileBig;
    public static mImage[] tileMapLogin;
    public static javax.microedition.lcdui.Image[] miniTile;
    public static javax.microedition.lcdui.Image[] bigTile;

    public static void loadTileBig(int n) {
        Object object;
        String string;
        int n2;
        ManagerTile.freeBigTile(n);
        tileBig = new mImage[totalTile[n]];
        miniTile = new javax.microedition.lcdui.Image[totalTile[n]];
        for (n2 = 0; n2 < tileBig.length; ++n2) {
            string = n2 < 9 ? "tile" + n + "_0" : "tile" + n + "_";
            object = mImage.createImage("/Tile/tile" + n + "/" + string + (n2 + 1) + ".png");
            ManagerTile.tileBig[n2] = (mImage)object;
        }
        for (n2 = 0; n2 < miniTile.length; ++n2) {
            string = n2 < 9 ? "tile_small" + n + "_0" : "tile_small" + n + "_";
            object = mSystem.createPixmap("/Tile/tile_small" + n + "/" + string + (n2 + 1) + ".png");
            ManagerTile.miniTile[n2] = object == null ? null : ((Image)object).midpImage;
        }
    }

    public static void loadTileBigLogin(int n) {
        tileMapLogin = new mImage[totalTile[n]];
        for (int i = 0; i < tileMapLogin.length; ++i) {
            mImage mImage2;
            String string = i < 9 ? "tile" + n + "_0" : "tile" + n + "_";
            ManagerTile.tileMapLogin[i] = mImage2 = mImage.createImage("/Tile/tile" + n + "/" + string + (i + 1) + ".png");
        }
    }

    public static void freeBigTile(int n) {
        try {
            int n2;
            for (n2 = 0; n2 < tileBig.length; ++n2) {
                if (tileBig[n2] == null || ManagerTile.tileBig[n2].image == null) continue;
                ManagerTile.tileBig[n2].image = null;
                ManagerTile.tileBig[n2] = null;
            }
            for (n2 = 0; n2 < miniTile.length; ++n2) {
                ManagerTile.miniTile[n2] = null;
            }
        }
        catch (Exception exception) {
        }
        System.gc();
    }

    public static void freeBigTile1(int n) {
        try {
            int n2;
            for (n2 = 0; n2 < bigTile.length; ++n2) {
                ManagerTile.bigTile[n2] = null;
            }
            for (n2 = 0; n2 < miniTile.length; ++n2) {
                ManagerTile.miniTile[n2] = null;
            }
        }
        catch (Exception exception) {
        }
        System.gc();
    }
}

