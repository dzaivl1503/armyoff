/*
 * Decompiled with CFR 0.152.
 */
package map;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import coreLG.CONFIG;
import map.HoleInfo;
import map.MM;
import model.CRes;
import model.FilePack;
import model.IAction2;

public class CMap {
    mImage map;
    public int[] aMap;
    public static boolean isDrawRGB = true;
    public static mImage MANGNHEN;
    static final byte HOLE_Proton = 0;
    static final byte HOLE_Ak = 1;
    static final byte HOLE_Small = 2;
    static final byte HOLE_Cannon = 3;
    static final byte HOLE_Rocket = 4;
    static final byte HOLE_Range = 5;
    static final byte HOLE_RANGCUA = 6;
    static final byte HOLE_GRENADE = 7;
    static final byte HOLE_Smallest = 8;
    static final byte HOLE_BigHole = 9;
    public int index;
    static byte curHoleType;
    public static mImage[] holeIMask;
    public static int[][] holeIntMask;
    static int holeW;
    static int holeH;
    mGraphics gMask;
    public int width;
    public int height;
    public int x;
    public int y;
    public boolean isDestroy;
    public boolean isSilkCollision;
    public int id;
    public boolean changed = false;
    private static final int DEFAULT_HOLE_W = 32;
    private static final int DEFAULT_HOLE_H = 26;

    public static void onInitCmap() {
        try {
            CRes.filePak = new FilePack(CCanvas.getClassPathConfig(CONFIG.PATH_EFFECT + "Hole"));
            CMap.loadHoleImagesFromPack();
        }
        catch (Exception exception) {
            CRes.out("Hole FilePack failed, loading PNG holes: " + exception.getMessage());
            CMap.loadHoleImagesFromPng();
        }
        CRes.filePak = null;
    }

    public static void ensureHoleMasksLoaded() {
        for (int i = 0; i < holeIMask.length; ++i) {
            if (holeIMask[i] != null && CMap.holeIMask[i].image != null) continue;
            CMap.onInitCmap();
            return;
        }
    }

    private static boolean holeMaskReady(int n) {
        if (n < 0 || n >= holeIMask.length) {
            return false;
        }
        return holeIMask[n] != null && CMap.holeIMask[n].image != null && holeIntMask[n] != null;
    }

    private static void loadHoleImagesFromPack() throws Exception {
        CRes.filePak.loadImage("mangnhen.png", new IAction2(){

            public void perform(Object object) {
                MANGNHEN = new mImage((Image)object);
            }
        });
        CMap.loadHoleMaskFromPack(0, "h32x26.png");
        CMap.loadHoleMaskFromPack(1, "smallhole.png");
        CMap.loadHoleMaskFromPack(2, "smallhole.png");
        CMap.loadHoleMaskFromPack(3, "h36x30.png");
        CMap.loadHoleMaskFromPack(4, "rocket.png");
        CMap.loadHoleMaskFromPack(5, "rangehole.png");
        CMap.loadHoleMaskFromPack(6, "hrangcua.png");
        CMap.loadHoleMaskFromPack(7, "hgrenade.png");
        CMap.loadHoleMaskFromPack(8, "h14x12.png");
        CMap.loadHoleMaskFromPack(9, "h55x50.png");
    }

    private static void loadHoleMaskFromPack(final int n, String string) throws Exception {
        CRes.filePak.loadImage(string, new IAction2(){

            public void perform(Object object) {
                CMap.setHoleMask(n, new mImage((Image)object));
            }
        });
    }

    private static void loadHoleImagesFromPng() {
        String string = "/" + CONFIG.PATH_EFFECT + "holes/";
        MANGNHEN = mImage.createImage(string + "mangnhen.png");
        CMap.setHoleMask(0, mImage.createImage(string + "h32x26.png"));
        CMap.setHoleMask(1, mImage.createImage(string + "smallhole.png"));
        CMap.setHoleMask(2, mImage.createImage(string + "smallhole.png"));
        CMap.setHoleMask(3, mImage.createImage(string + "h36x30.png"));
        CMap.setHoleMask(4, mImage.createImage(string + "rocket.png"));
        CMap.setHoleMask(5, mImage.createImage(string + "rangehole.png"));
        CMap.setHoleMask(6, mImage.createImage(string + "hrangcua.png"));
        CMap.setHoleMask(7, mImage.createImage(string + "hgrenade.png"));
        CMap.setHoleMask(8, mImage.createImage(string + "h14x12.png"));
        CMap.setHoleMask(9, mImage.createImage(string + "h55x50.png"));
    }

    private static void setHoleMask(int n, mImage mImage2) {
        if (mImage2 == null || mImage2.image == null || mImage2.image.midpImage == null) {
            return;
        }
        CMap.holeIMask[n] = mImage2;
        int n2 = mImage2.image.getWidth();
        int n3 = mImage2.image.getHeight();
        if (n2 <= 0 || n3 <= 0) {
            return;
        }
        CMap.holeIntMask[n] = new int[n2 * n3];
        try {
            mImage2.getRGB(holeIntMask[n], 0, n2, 0, 0, n2, n3);
        }
        catch (Exception exception) {
            CMap.holeIMask[n] = null;
            CMap.holeIntMask[n] = null;
            CRes.out("setHoleMask failed index " + n + ": " + exception.getMessage());
        }
    }

    public static byte getHoleType(int n) {
        switch (n) {
            case 0:
            case 32: {
                return 3;
            }
            case 1: {
                return 1;
            }
            case 2: {
                return 0;
            }
            case 3: {
                return 9;
            }
            default: {
                return 0;
            }
            case 6: {
                return 6;
            }
            case 7:
            case 31:
            case 37: {
                return 7;
            }
            case 9: {
                return 5;
            }
            case 10: {
                return 4;
            }
            case 11: {
                return 2;
            }
            case 12: {
                return 6;
            }
            case 15: {
                return 7;
            }
            case 17:
            case 18: {
                return 2;
            }
            case 19: {
                return 2;
            }
            case 20: {
                return 0;
            }
            case 21: {
                return 2;
            }
            case 22: {
                return 7;
            }
            case 24: {
                return 3;
            }
            case 25: {
                return 8;
            }
            case 27: {
                return 1;
            }
            case 30: {
                return 0;
            }
            case 42:
            case 43: {
                return 7;
            }
            case 44: {
                return 2;
            }
            case 45: {
                return 7;
            }
            case 47: {
                return 8;
            }
            case 48: {
                return 3;
            }
            case 52: {
                return 3;
            }
            case 57:
        }
        return 7;
    }

    public CMap() {
    }

    public void createRGB(mImage mImage2) {
        this.map = mImage2;
        this.width = this.map.image.getWidth();
        this.height = this.map.image.getHeight();
        if (!MM.isExistId(this.id)) {
            this.aMap = new int[this.width * this.height];
            this.map.getRGB(this.aMap, 0, this.width, 0, 0, this.width, this.height);
        } else {
            this.aMap = new int[this.width * this.height];
            int[] nArray = MM.rgbMap(this.id);
            System.arraycopy(nArray, 0, this.aMap, 0, this.width * this.height);
        }
        for (int i = 0; i < this.aMap.length; ++i) {
            int n = this.aMap[i];
        }
    }

    public CMap(int n, int n2, int n3, mImage mImage2, boolean bl) {
        this.x = n2;
        this.y = n3;
        this.id = n;
        this.isDestroy = bl;
        if (mImage2 != null) {
            this.createRGB(mImage2);
        }
    }

    public boolean isCollisionPixel(int n, int n2) {
        if (n < 0 || n2 < 0 || n >= this.width || n2 >= this.height) {
            return false;
        }
        return this.isSilkCollision || CRes.isLand(this.getPixel(n, n2));
    }

    public void paint(mGraphics mGraphics2) {
        if (this.map != null) {
            mGraphics2.drawImage(this.map, this.x, this.y, 0, false);
        }
    }

    public void update() {
        if (this.changed) {
            this.map = mImage.createImageNotRunable(this.aMap, this.width, this.height);
            this.changed = false;
        }
    }

    public void makeHole(int n, int n2, int n3) {
        this.makeHole(n, n2, n3, false);
    }

    public void makeHole(int n, int n2, int n3, boolean bl) {
        int n4;
        int n5;
        int n6;
        int n7;
        curHoleType = CMap.getHoleType(n3);
        if (!CMap.holeMaskReady(curHoleType)) {
            CMap.ensureHoleMasksLoaded();
        }
        if (!CMap.holeMaskReady(curHoleType)) {
            return;
        }
        holeW = CMap.holeIMask[CMap.curHoleType].image.getWidth();
        holeH = CMap.holeIMask[CMap.curHoleType].image.getHeight();
        int n8 = this.x;
        int n9 = this.y;
        int n10 = n - holeW / 2;
        int n11 = n2 - holeH / 2;
        if (n10 < n8) {
            n7 = holeW;
            n6 = n8 - n10;
            if (n7 - n6 > this.width) {
                n7 = n6 + this.width;
            }
        } else {
            n6 = 0;
            n7 = n6 + this.width - n10 + n8;
            if (n7 > holeW) {
                n7 = holeW;
            }
        }
        if (n11 < n9) {
            n5 = holeH;
            n4 = n9 - n11;
            if (n5 - n4 > this.height) {
                n5 = n4 + this.height;
            }
        } else {
            n4 = 0;
            n5 = n4 + this.height - n11 + n9;
            if (n5 > holeH) {
                n5 = holeH;
            }
        }
        int n12 = n - this.x - holeW / 2;
        int n13 = n2 - this.y - holeH / 2;
        boolean bl2 = false;
        for (int i = n4; i < n5; ++i) {
            for (int j = n6; j < n7; ++j) {
                HoleInfo holeInfo;
                int n14;
                int n15;
                if (!CRes.inRect(n12 + j, n13 + i, 0, 0, this.width, this.height) || MM.isProtectedStandPixel(n15 = this.x + n12 + j, n14 = this.y + n13 + i)) continue;
                if (holeIntMask[curHoleType][i * holeW + j] == -65536 && CRes.isLand(this.getPixel(n12 + j, n13 + i))) {
                    int n16 = this.aMap[(n13 + i) * this.width + n12 + j] = bl ? 0xFFFFFF : -16777216;
                    if (bl2) continue;
                    bl2 = true;
                    holeInfo = new HoleInfo();
                    holeInfo.mapID = (short)this.index;
                    holeInfo.x = (short)n;
                    holeInfo.y = (short)n2;
                    holeInfo.holeType = (byte)n3;
                    MM.vHoleInfo.addElement(holeInfo);
                    continue;
                }
                if (holeIntMask[curHoleType][i * holeW + j] != -16777216 && holeIntMask[curHoleType][i * holeW + j] != 0x1000000) continue;
                this.aMap[(n13 + i) * this.width + n12 + j] = 0xFFFFFF;
                if (bl2) continue;
                bl2 = true;
                holeInfo = new HoleInfo();
                holeInfo.mapID = (short)this.index;
                holeInfo.x = (short)n;
                holeInfo.y = (short)n2;
                holeInfo.holeType = (byte)n3;
                MM.vHoleInfo.addElement(holeInfo);
            }
        }
        if (bl2 && this.isSilkCollision) {
            this.isSilkCollision = false;
        }
        this.changed = true;
    }

    public static int getHoleW(int n) {
        byte by = CMap.getHoleType(n);
        if (!CMap.holeMaskReady(by)) {
            CMap.ensureHoleMasksLoaded();
        }
        if (!CMap.holeMaskReady(by)) {
            return 32;
        }
        return CMap.holeIMask[by].image.getWidth();
    }

    public static int getHoleH(int n) {
        byte by = CMap.getHoleType(n);
        if (!CMap.holeMaskReady(by)) {
            CMap.ensureHoleMasksLoaded();
        }
        if (!CMap.holeMaskReady(by)) {
            return 26;
        }
        return CMap.holeIMask[by].image.getHeight();
    }

    public int getPixel(int n, int n2) {
        return this.aMap[n2 * this.width + n];
    }

    static {
        holeIMask = new mImage[10];
        holeIntMask = new int[10][];
    }
}

