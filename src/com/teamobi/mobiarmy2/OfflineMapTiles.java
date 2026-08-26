/*
 * Decompiled with CFR 0.152.
 */
package com.teamobi.mobiarmy2;

import CLib.LibSysTem;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import screen.GameScr;

public final class OfflineMapTiles {
    private OfflineMapTiles() {
    }

    public static void tryLoadMapIcon(int n) {
        if (GameScr.mm == null) {
            return;
        }
        if (n < 0) {
            GameScr.mm.skipImageLoad();
            return;
        }
        byte[] byArray = OfflineMapTiles.readMapIconBytes(n);
        if (byArray == null || byArray.length == 0) {
            GameScr.mm.skipImageLoad();
            return;
        }
        GameScr.mm.addImage(n, byArray, byArray.length);
    }

    private static byte[] readMapIconBytes(int n) {
        String string = "/icon/map/" + n + ".png";
        try {
            int n2;
            InputStream inputStream = LibSysTem.openResource("/" + LibSysTem.res + string);
            if (inputStream == null) {
                return null;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] byArray = new byte[1024];
            while ((n2 = inputStream.read(byArray)) != -1) {
                byteArrayOutputStream.write(byArray, 0, n2);
            }
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception exception) {
            return null;
        }
    }
}

