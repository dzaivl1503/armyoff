/*
 * Decompiled with CFR 0.152.
 */
package map;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import map.CMap;
import map.MM;
import model.CRes;
import player.CPlayer;
import player.PM;
import screen.GameScr;
import screen.PrepareScr;

public final class MiniMap {
    private static final int WORLD_SCALE = 12;
    private static final int SIZE_MINI = 2;
    private static final int COLOR_BG = -15919840;
    private static final int COLOR_EMPTY = -15062984;
    private static final int[] COLOR_BORDER = new int[]{5257738, 8807192};
    private static final int COLOR_PANEL = -15722464;
    private static final int COLOR_DOT_SELF = 0xFF0000;
    private static final int COLOR_DOT_TEAM = 65280;
    private static final int COLOR_DOT_ENEMY = 8900331;
    private static final int COLOR_LINE_AIM = 0xFFFFFF;
    private static final int COLOR_LINE_ENEMY = 0x66CCFF;
    public static mImage imgMiniMap;
    private static mImage imgMiniMapDisplay;
    public static int tmw;
    public static int tmh;
    public static int wMiniMap;
    public static int hMiniMap;
    public static int posMiniMapX;
    public static int posMiniMapY;
    private static int mapDrawX;
    private static int mapDrawY;
    private static int mapDrawW;
    private static int mapDrawH;
    private static int mapSrcW;
    private static int mapSrcH;
    private static boolean visible;

    private MiniMap() {
    }

    public static void clear() {
        imgMiniMap = null;
        imgMiniMapDisplay = null;
        tmw = 0;
        tmh = 0;
        visible = false;
    }

    private static boolean hasMapTileData() {
        for (int i = 0; i < MM.maps.size(); ++i) {
            CMap cMap = (CMap)MM.maps.elementAt(i);
            if (cMap.aMap == null) continue;
            return true;
        }
        return false;
    }

    public static void prepareMapImage() {
        if (MM.mapWidth <= 0 || MM.mapHeight <= 0 || !MiniMap.hasMapTileData()) {
            return;
        }
        MiniMap.buildFromMaps();
        MiniMap.rebuildDisplayMap();
    }

    public static void enterBattle() {
        visible = true;
        MiniMap.layoutForScreen();
        MiniMap.prepareMapImage();
    }

    public static void leaveBattle() {
        visible = false;
    }

    private static boolean inBattle() {
        if (!visible || PM.p == null) {
            return false;
        }
        byte by = GameScr.myIndex;
        return by >= 0 && by < PM.p.length && PM.p[by] != null;
    }

    private static CPlayer getMyPlayerSafe() {
        if (PM.p == null) {
            return null;
        }
        byte by = GameScr.myIndex;
        if (by < 0 || by >= PM.p.length) {
            return null;
        }
        return PM.p[by];
    }

    private static void layoutForScreen() {
        if (PrepareScr.khungMap != null && PrepareScr.khungMap.image != null) {
            wMiniMap = PrepareScr.khungMap.image.getWidth();
            hMiniMap = PrepareScr.khungMap.image.getHeight();
        } else {
            wMiniMap = CCanvas.width > 176 ? 50 : 60;
            hMiniMap = CCanvas.width > 176 ? 40 : 42;
        }
        int n = CCanvas.width / 2;
        int n2 = 22;
        int n3 = 24;
        int n4 = 24;
        if (CRes.imgCam != null && CRes.imgCam.image != null) {
            n3 = CRes.imgCam.image.getWidth();
            n4 = CRes.imgCam.image.getHeight();
        }
        if (CCanvas.isTouch) {
            int n5 = 0;
            if (CRes.imgMenu != null && CRes.imgMenu.image != null) {
                n5 = CRes.imgMenu.image.getHeight();
            }
            int n6 = CCanvas.width - n3 - 5;
            int n7 = n5 + 5;
            int n8 = (n + n6 + n3 / 2) / 2;
            int n9 = (n2 + n7 + n4 / 2) / 2;
            posMiniMapX = n8 - wMiniMap / 2;
            posMiniMapY = n9 - hMiniMap / 2;
        } else {
            posMiniMapX = CCanvas.width - wMiniMap - 4;
            posMiniMapY = 4;
        }
        if (posMiniMapX < 2) {
            posMiniMapX = 2;
        }
        if (posMiniMapY < 2) {
            posMiniMapY = 2;
        }
        if (posMiniMapX + wMiniMap > CCanvas.width - 2) {
            posMiniMapX = CCanvas.width - wMiniMap - 2;
        }
        MiniMap.rebuildDisplayMap();
    }

    private static int getViewW() {
        int n = wMiniMap - 2;
        return n < 1 ? 1 : n;
    }

    private static int getViewH() {
        int n = hMiniMap - 3;
        return n < 1 ? 1 : n;
    }

    private static void rebuildDisplayMap() {
        if (imgMiniMap == null || MiniMap.imgMiniMap.image == null) {
            imgMiniMapDisplay = null;
            return;
        }
        mapSrcW = MiniMap.imgMiniMap.image.getWidth();
        mapSrcH = MiniMap.imgMiniMap.image.getHeight();
        int n = MiniMap.getViewW();
        int n2 = MiniMap.getViewH();
        float f = Math.min((float)n / (float)mapSrcW, (float)n2 / (float)mapSrcH);
        mapDrawW = Math.max(1, (int)((float)mapSrcW * f));
        mapDrawH = Math.max(1, (int)((float)mapSrcH * f));
        mapDrawX = posMiniMapX + 1 + (n - mapDrawW) / 2;
        mapDrawY = posMiniMapY + 2 + (n2 - mapDrawH) / 2;
        int[] nArray = new int[mapSrcW * mapSrcH];
        imgMiniMap.getRGB(nArray, 0, mapSrcW, 0, 0, mapSrcW, mapSrcH);
        int[] nArray2 = new int[mapDrawW * mapDrawH];
        for (int i = 0; i < mapDrawH; ++i) {
            int n3 = i * mapSrcH / mapDrawH;
            for (int j = 0; j < mapDrawW; ++j) {
                int n4 = j * mapSrcW / mapDrawW;
                nArray2[i * MiniMap.mapDrawW + j] = nArray[n3 * mapSrcW + n4];
            }
        }
        imgMiniMapDisplay = mImage.createImageNotRunable(nArray2, mapDrawW, mapDrawH);
    }

    private static void buildFromMaps() {
        int n;
        tmw = Math.max(1, (MM.mapWidth + 12 - 1) / 12);
        tmh = Math.max(1, (MM.mapHeight + 12 - 1) / 12);
        int n2 = tmw * 2;
        int n3 = tmh * 2;
        int[] nArray = new int[n2 * n3];
        for (n = 0; n < nArray.length; ++n) {
            nArray[n] = -15919840;
        }
        for (n = 0; n < tmh; ++n) {
            for (int i = 0; i < tmw; ++i) {
                int n4;
                int n5;
                int n6;
                int n7 = i * 12 + 6;
                int n8 = n * 12 + 6;
                int n9 = -15062984;
                for (n6 = 0; n6 < MM.maps.size(); ++n6) {
                    int n10;
                    CMap cMap = (CMap)MM.maps.elementAt(n6);
                    if (cMap.aMap == null) continue;
                    n5 = n7 - cMap.x;
                    n4 = n8 - cMap.y;
                    if (n5 < 0 || n4 < 0 || n5 >= cMap.width || n4 >= cMap.height || !MiniMap.isVisiblePixel(n10 = cMap.getPixel(n5, n4))) continue;
                    n9 = MiniMap.sampleColor(n10);
                }
                n6 = i * 2;
                int n11 = n * 2;
                for (n5 = 0; n5 < 2; ++n5) {
                    for (n4 = 0; n4 < 2; ++n4) {
                        nArray[(n11 + n5) * n2 + n6 + n4] = n9;
                    }
                }
            }
        }
        imgMiniMap = mImage.createImageNotRunable(nArray, n2, n3);
    }

    private static boolean isVisiblePixel(int n) {
        int n2 = n >>> 24 & 0xFF;
        return n2 != 0 && n != 0xFFFFFF;
    }

    private static int sampleColor(int n) {
        int n2 = n >> 16 & 0xFF;
        int n3 = n >> 8 & 0xFF;
        int n4 = n & 0xFF;
        n2 = n2 * 4 / 5 + 20;
        n3 = n3 * 4 / 5 + 30;
        n4 = n4 * 3 / 5 + 15;
        if (n2 > 255) {
            n2 = 255;
        }
        if (n3 > 255) {
            n3 = 255;
        }
        if (n4 > 255) {
            n4 = 255;
        }
        return 0xFF000000 | n2 << 16 | n3 << 8 | n4;
    }

    private static void worldToScreen(int n, int n2, int[] nArray) {
        if (mapSrcW <= 0 || mapSrcH <= 0) {
            nArray[0] = mapDrawX;
            nArray[1] = mapDrawY;
            return;
        }
        int n3 = n / 12 * 2;
        int n4 = n2 / 12 * 2;
        nArray[0] = mapDrawX + n3 * mapDrawW / mapSrcW;
        nArray[1] = mapDrawY + n4 * mapDrawH / mapSrcH;
    }

    public static void update() {
        if (!MiniMap.inBattle()) {
            return;
        }
        if (imgMiniMap == null && MiniMap.hasMapTileData()) {
            MiniMap.prepareMapImage();
        }
    }

    public static void paint(mGraphics mGraphics2) {
        if (!MiniMap.inBattle() || CCanvas.width <= 176 || GameScr.cantSee) {
            return;
        }
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        int n = posMiniMapX;
        int n2 = posMiniMapY;
        int n3 = MiniMap.getViewW();
        int n4 = MiniMap.getViewH();
        int n5 = n + 1;
        int n6 = n2 + 2;
        if (PrepareScr.khungMap != null) {
            mGraphics2.drawImage(PrepareScr.khungMap, n, n2, mGraphics.TOP | mGraphics.LEFT, false);
        } else {
            for (int i = 0; i < COLOR_BORDER.length; ++i) {
                mGraphics2.setColor(COLOR_BORDER[i]);
                mGraphics2.fillRect(n + i - 2, n2 + i - 2, wMiniMap + 2 - (i << 1), hMiniMap - (i << 1), false);
            }
        }
        mGraphics2.setColor(-15722464);
        mGraphics2.fillRect(n5, n6, n3, n4, false);
        if (imgMiniMapDisplay == null) {
            if (imgMiniMap != null) {
                MiniMap.rebuildDisplayMap();
            }
            if (imgMiniMapDisplay == null) {
                return;
            }
        }
        mGraphics2.setClip(n5, n6, n3, n4);
        mGraphics2.drawRegion(imgMiniMapDisplay, 0, 0, mapDrawW, mapDrawH, 0, mapDrawX, mapDrawY, mGraphics.TOP | mGraphics.LEFT, false);
        MiniMap.paintAimLines(mGraphics2);
        MiniMap.paintPlayers(mGraphics2);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
    }

    private static void paintAimLines(mGraphics mGraphics2) {
        CPlayer cPlayer = MiniMap.getMyPlayerSafe();
        if (cPlayer == null || PM.p == null) {
            return;
        }
        int[] nArray = new int[2];
        MiniMap.worldToScreen(cPlayer.x, cPlayer.y, nArray);
        int n = nArray[0];
        int n2 = nArray[1];
        int n3 = CRes.cos(CRes.fixangle(cPlayer.angle));
        int n4 = CRes.sin(CRes.fixangle(cPlayer.angle));
        int n5 = Math.max(mapDrawW, mapDrawH) + 20;
        mGraphics2.setColor(0xFFFFFF);
        mGraphics2.drawLine(n, n2, n + (n5 * n3 >> 10), n2 - (n5 * n4 >> 10), false);
        if (GameScr.windPower != 0) {
            int n6 = 8 * CRes.cos(GameScr.windAngle) >> 10;
            int n7 = -(8 * CRes.sin(GameScr.windAngle) >> 10);
            mGraphics2.setColor(0x66CCFF);
            mGraphics2.drawLine(n, n2 - 8, n + n6, n2 - 8 + n7, false);
        }
    }

    private static void paintPlayers(mGraphics mGraphics2) {
        CPlayer cPlayer = MiniMap.getMyPlayerSafe();
        if (cPlayer == null || PM.p == null) {
            return;
        }
        int[] nArray = new int[2];
        for (int i = 0; i < PM.p.length; ++i) {
            CPlayer cPlayer2 = PM.p[i];
            if (cPlayer2 == null || cPlayer2.getState() == 5) continue;
            MiniMap.worldToScreen(cPlayer2.x, cPlayer2.y, nArray);
            int n = nArray[0];
            int n2 = nArray[1];
            if (cPlayer2 == cPlayer) {
                mGraphics2.setColor(0xFFFFFF);
                mGraphics2.fillRect(n - 2, n2 - 2, 5, 5, false);
                mGraphics2.setColor(0xFF0000);
                mGraphics2.fillRect(n - 1, n2 - 1, 3, 3, false);
                continue;
            }
            if (PM.isAlly(cPlayer, cPlayer2)) {
                if (CCanvas.gameTick % 10 >= 8) continue;
                mGraphics2.setColor(0xFFFFFF);
                mGraphics2.fillRect(n - 2, n2 - 2, 5, 5, false);
                mGraphics2.setColor(65280);
                mGraphics2.fillRect(n - 1, n2 - 1, 3, 3, false);
                continue;
            }
            if (CCanvas.gameTick % 10 >= 8) continue;
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillRect(n - 2, n2 - 2, 5, 5, false);
            mGraphics2.setColor(8900331);
            mGraphics2.fillRect(n - 1, n2 - 1, 3, 3, false);
        }
    }

    static {
        wMiniMap = 50;
        hMiniMap = 40;
        posMiniMapY = 4;
    }
}

