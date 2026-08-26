/*
 * Decompiled with CFR 0.152.
 */
package map;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import com.teamobi.mobiarmy2.OfflineBossFight;
import coreLG.CCanvas;
import effect.Explosion;
import java.util.Hashtable;
import java.util.Vector;
import map.Background;
import map.CMap;
import map.MapFile;
import map.MapImage;
import map.MiniMap;
import model.CRes;
import model.IAction2;
import model.TimeBomb;
import network.GameService;
import player.PM;
import screen.GameScr;
import screen.MenuScr;
import screen.PrepareScr;

public class MM {
    public static int mapWidth;
    public static int mapHeight;
    public static Vector maps;
    public static Vector mapFiles;
    int index = 0;
    public static boolean isHaveWaterOrGlass;
    public static final byte WATER = 0;
    public static final byte GLASS = 1;
    public static final byte GLASS_2 = 2;
    public static byte curWaterType;
    public static int mapID;
    static Background background;
    public static byte NUM_MAP;
    public static String[] mapName;
    public static String[] mapFileName;
    public static final byte WATERBUM_SMALL_THING = 0;
    public static final byte WATERBUM_NORMAL_THING = 1;
    public static final byte WATERBUM_BIG_THING = 2;
    public static byte[] fullData;
    int count1 = 0;
    int count2 = 0;
    public static Vector mapImages;
    public static Vector vHoleInfo;
    public static short[] undestroyTile;
    private static final int PLAYER_STAND_PROTECT_HALF_W = 18;
    private static final int BOSS_STAND_PROTECT_HALF_W = 38;
    private static final int STAND_PROTECT_ABOVE_FOOT = 10;
    private static final int STAND_PROTECT_BELOW_FOOT = 10;

    public void createMap(int n) {
        isHaveWaterOrGlass = false;
        MiniMap.clear();
        CRes.out("=====================> Create map = " + n);
        mapID = n;
        this.loadMapFile(n);
    }

    public void createBackGround() {
        Background.waterY = mapHeight - (Background.water.image.getHeight() + Background.inWater.image.getHeight()) + 37;
        CRes.out(this.getClass().getName() + " createBackGround have mapID = " + mapID);
        if (GameScr.curGRAPHIC_LEVEL != 2) {
            for (int i = 0; i < mapFiles.size(); ++i) {
                MapFile mapFile = (MapFile)mapFiles.elementAt(i);
                if (mapID != mapFile.mapID) continue;
                background = new Background((byte)mapFile.backGroundID);
                if (mapFile.yBackGround != -1) {
                    MM.background.yBackGr = mapFile.yBackGround;
                }
                if (mapFile.yCloud != -1) {
                    MM.background.yCloud = mapFile.yCloud - 100;
                }
                if (mapFile.yWater != -1) {
                    Background.waterY += mapFile.yWater;
                }
                if (mapFile.water_class == -1) break;
                curWaterType = (byte)mapFile.water_class;
                isHaveWaterOrGlass = true;
                break;
            }
        }
    }

    public void clearBackGround() {
        CRes.out("===================================> OnClear BG");
        mapFiles.removeAllElements();
    }

    public byte[] getDataByID(int n) {
        byte[] byArray = null;
        for (int i = 0; i < mapFiles.size(); ++i) {
            MapFile mapFile = (MapFile)mapFiles.elementAt(i);
            if (mapFile.mapID != n) continue;
            byArray = mapFile.data;
        }
        return byArray;
    }

    public boolean isTileDestroy(int n) {
        if (undestroyTile == null) {
            return false;
        }
        for (int i = 0; i < undestroyTile.length; ++i) {
            if (n != undestroyTile[i]) continue;
            return true;
        }
        return false;
    }

    public void addImage(final int n, byte[] byArray, int n2) {
        mImage.createImage(byArray, 0, n2, new IAction2(){

            public void perform(Object object) {
                int n3;
                Image image = (Image)object;
                for (n3 = 0; n3 < maps.size(); ++n3) {
                    CMap cMap = (CMap)maps.elementAt(n3);
                    if (cMap.id != n) continue;
                    cMap.createRGB(new mImage(image));
                }
                ++MM.this.count2;
                if (MM.this.count2 == MM.this.count1) {
                    CCanvas.endDlg();
                    MiniMap.prepareMapImage();
                    if (MenuScr.isTraining) {
                        MenuScr.isTraining = false;
                        GameService.gI().training((byte)0);
                    }
                }
                mapImages.addElement(new MapImage(new mImage(image), n));
                int n2 = n3 = GameScr.curGRAPHIC_LEVEL == 2 ? 5 : 30;
                if (mapImages.size() >= n3) {
                    while (mapImages.size() > n3) {
                        mapImages.removeElementAt(0);
                    }
                }
                CRes.out("=====================> MapImage " + mapImages.size());
            }
        });
    }

    public void addImage(int n, mImage mImage2) {
        int n2;
        for (n2 = 0; n2 < maps.size(); ++n2) {
            CMap cMap = (CMap)maps.elementAt(n2);
            if (cMap.id != n) continue;
            cMap.createRGB(mImage2);
        }
        ++this.count2;
        if (this.count2 == this.count1) {
            CCanvas.endDlg();
            MiniMap.prepareMapImage();
            if (MenuScr.isTraining) {
                MenuScr.isTraining = false;
                GameService.gI().training((byte)0);
            }
        }
        mapImages.addElement(new MapImage(mImage2, n));
        int n3 = n2 = GameScr.curGRAPHIC_LEVEL == 2 ? 5 : 30;
        if (mapImages.size() >= n2) {
            while (mapImages.size() > n2) {
                mapImages.removeElementAt(0);
            }
        }
    }

    public void skipImageLoad() {
        ++this.count2;
        if (this.count2 == this.count1) {
            CCanvas.endDlg();
            MiniMap.prepareMapImage();
            if (MenuScr.isTraining) {
                MenuScr.isTraining = false;
                GameService.gI().training((byte)0);
            }
        }
    }

    public boolean containsImage(int n) {
        for (int i = 0; i < mapImages.size(); ++i) {
            MapImage mapImage = (MapImage)mapImages.elementAt(i);
            if (mapImage.id != n) continue;
            return true;
        }
        return false;
    }

    public mImage getImage(int n) {
        for (int i = 0; i < mapImages.size(); ++i) {
            MapImage mapImage = (MapImage)mapImages.elementAt(i);
            if (mapImage.id != n) continue;
            return mapImage.image;
        }
        return null;
    }

    public static boolean isExistId(int n) {
        for (int i = 0; i < maps.size(); ++i) {
            CMap cMap = (CMap)maps.elementAt(i);
            if (cMap.aMap == null || cMap.id != n) continue;
            return true;
        }
        return false;
    }

    public static int[] rgbMap(int n) {
        for (int i = 0; i < maps.size(); ++i) {
            CMap cMap = (CMap)maps.elementAt(i);
            if (cMap.id != n) continue;
            return cMap.aMap;
        }
        return null;
    }

    private void loadMapFile(int n) {
        byte[] byArray = this.getDataByID(n);
        if (byArray != null) {
            this.count1 = 0;
            this.count2 = 0;
            int n2 = 0;
            mapWidth = CRes.getShort(n2, byArray);
            mapHeight = CRes.getShort(n2 += 2, byArray);
            int n3 = byArray[n2 += 2];
            int[] nArray = new int[n3];
            ++n2;
            Hashtable<String, MapImage> hashtable = new Hashtable<String, MapImage>();
            CMap cMap = null;
            for (int i = 0; i < n3; ++i) {
                nArray[i] = byArray[n2];
                if (this.containsImage(nArray[i])) {
                    cMap = new CMap(nArray[i], CRes.getShort(n2 + 1, byArray), CRes.getShort(n2 + 3, byArray), this.getImage(nArray[i]), !this.isTileDestroy(nArray[i]));
                    cMap.index = i;
                    this.addMap(cMap);
                } else {
                    cMap = new CMap(nArray[i], CRes.getShort(n2 + 1, byArray), CRes.getShort(n2 + 3, byArray), null, !this.isTileDestroy(nArray[i]));
                    cMap.index = i;
                    this.addMap(cMap);
                    if (!hashtable.containsKey(String.valueOf(nArray[i]))) {
                        GameService.gI().getMaterialIcon((byte)2, nArray[i], -1);
                    }
                    hashtable.put(String.valueOf(nArray[i]), new MapImage(null, nArray[i]));
                }
                n2 += 5;
            }
            if (hashtable != null) {
                this.count1 = hashtable.size();
            }
            if (MenuScr.isTraining) {
                MenuScr.isTraining = false;
                GameService.gI().training((byte)0);
            }
            CRes.out("=====================> MapImage " + mapImages.size());
            if (this.count1 == 0) {
                CCanvas.endDlg();
                MiniMap.prepareMapImage();
            }
        }
    }

    public void addMap(CMap cMap) {
        maps.addElement(cMap);
    }

    public CMap getMap(int n) {
        return (CMap)maps.elementAt(n);
    }

    public boolean isLand(int n, int n2) {
        for (int i = 0; i < maps.size(); ++i) {
            CMap cMap = (CMap)maps.elementAt(i);
            if (!CRes.inRect(n, n2, cMap.x, cMap.y, cMap.width, cMap.height) || !cMap.isCollisionPixel(n - cMap.x, n2 - cMap.y)) continue;
            return true;
        }
        return false;
    }

    public static boolean checkWaterBum(int n, int n2, byte by) {
        if (GameScr.curGRAPHIC_LEVEL == 2) {
            if (GameScr.exs.size() > 3) {
                return true;
            }
            if (GameScr.curGRAPHIC_LEVEL == 1 && GameScr.exs.size() > 6) {
                return true;
            }
        }
        if (curWaterType == 0) {
            if (n2 >= Background.waterY + 48) {
                switch (by) {
                    case 0: {
                        new Explosion(n, n2, 2);
                        return true;
                    }
                    case 1: {
                        new Explosion(n, n2 - 5, 2);
                        new Explosion(n, n2 + 6, 2);
                        return true;
                    }
                    case 2: {
                        new Explosion(n, n2 - 10, 2);
                        new Explosion(n + 12, n2, 2);
                        new Explosion(n - 12, n2, 2);
                        return true;
                    }
                }
            }
        } else if (curWaterType == 1) {
            if (n2 >= Background.glassY) {
                switch (by) {
                    case 0: {
                        GameScr.sm.addRock(n, n2, CRes.random(-4, 4), CRes.random(-11, -9), (byte)6);
                        return true;
                    }
                    case 1: {
                        GameScr.sm.addRock(n, n2, CRes.random(-4, 4), CRes.random(-14, -12), (byte)6);
                        GameScr.sm.addRock(n, n2, CRes.random(-6, 6), CRes.random(-11, -9), (byte)6);
                        return true;
                    }
                    case 2: {
                        GameScr.sm.addRock(n, n2, CRes.random(-4, 4), CRes.random(-13, -11), (byte)6);
                        GameScr.sm.addRock(n, n2, CRes.random(-6, 6), CRes.random(-11, -9), (byte)6);
                        GameScr.sm.addRock(n, n2, CRes.random(-2, 2), CRes.random(-17, -15), (byte)6);
                        return true;
                    }
                }
            }
        } else if (curWaterType == 2 && n2 >= Background.glassY) {
            switch (by) {
                case 0: {
                    GameScr.sm.addRock(n, n2, CRes.random(-4, 4), CRes.random(-11, -9), (byte)14);
                    return true;
                }
                case 1: {
                    GameScr.sm.addRock(n, n2, CRes.random(-4, 4), CRes.random(-14, -12), (byte)14);
                    GameScr.sm.addRock(n, n2, CRes.random(-6, 6), CRes.random(-11, -9), (byte)14);
                    return true;
                }
                case 2: {
                    GameScr.sm.addRock(n, n2, CRes.random(-4, 4), CRes.random(-13, -11), (byte)14);
                    GameScr.sm.addRock(n, n2, CRes.random(-6, 6), CRes.random(-11, -9), (byte)14);
                    GameScr.sm.addRock(n, n2, CRes.random(-2, 2), CRes.random(-17, -15), (byte)14);
                    return true;
                }
            }
        }
        return false;
    }

    public void makeHole(int n, int n2, byte by, int n3) {
        this.makeHole(n, n2, by, n3, false);
    }

    public void makeHole(int n, int n2, byte by, int n3, boolean bl) {
        Object object;
        int n4;
        int n5 = CMap.getHoleW(by);
        int n6 = CMap.getHoleH(by);
        if (n5 <= 0 || n6 <= 0) {
            return;
        }
        if (!MM.isBossTerrainLocked()) {
            for (n4 = 0; n4 < maps.size(); ++n4) {
                object = (CMap)maps.elementAt(n4);
                if (!((CMap)object).isDestroy || !CRes.isHit(n - n5 / 2, n2 - n6 / 2, n5, n6, ((CMap)object).x, ((CMap)object).y, ((CMap)object).width, ((CMap)object).height)) continue;
                ((CMap)maps.elementAt(n4)).makeHole(n, n2, by, bl);
            }
        }
        for (n4 = 0; n4 < PM.p.length; ++n4) {
            if (PM.p[n4] == null) continue;
            if (CRes.inRect(PM.p[n4].x, PM.p[n4].y, n - n5 / 2, n2 - n6 / 2, n5, n6)) {
                if (PM.p[n4].getState() != 5 && PM.p[n4].bulletType != 30) {
                    PM.p[n4].activeHurt(n > PM.p[n4].x ? 2 : 0);
                }
                PM.p[n4].isActiveFall = false;
                PM.p[n4].activeFallbyEx = true;
                PM.p[n4].chophepGuiUpdateXY = true;
            }
            if (PM.p[n4].gun != 16) continue;
            while (!this.isLand(PM.p[n4].x, PM.p[n4].yPoint)) {
                ++PM.p[n4].yPoint;
                if (PM.p[n4].yPoint <= mapHeight) continue;
            }
        }
        for (n4 = 0; n4 < GameScr.timeBombs.size(); ++n4) {
            object = (TimeBomb)GameScr.timeBombs.elementAt(n4);
            if (object == null || ((TimeBomb)object).isFall) continue;
            ((TimeBomb)object).isFall = true;
        }
    }

    public void retraceRimAround(int n, int n2, int n3) {
        int n4 = n - n3;
        int n5 = n + n3;
        int n6 = n2 - n3;
        int n7 = n2 + n3;
        for (int i = n6; i <= n7; ++i) {
            for (int j = n4; j <= n5; ++j) {
                this.retraceRimPixel(j, i);
            }
        }
    }

    private void retraceRimPixel(int n, int n2) {
        CMap cMap = this.findTileAt(n, n2);
        if (cMap == null) {
            return;
        }
        int n3 = n2 - cMap.y;
        int n4 = n - cMap.x;
        int n5 = n3 * cMap.width + n4;
        int n6 = cMap.aMap[n5];
        if (n6 == 0xFFFFFF) {
            return;
        }
        boolean bl = this.isDugAt(n - 1, n2);
        boolean bl2 = this.isDugAt(n + 1, n2);
        boolean bl3 = this.isDugAt(n, n2 - 1);
        boolean bl4 = this.isDugAt(n, n2 + 1);
        if (n6 == -16777216) {
            if (bl && bl2 && bl3 && bl4) {
                cMap.aMap[n5] = 0xFFFFFF;
                cMap.changed = true;
            }
        } else if (bl || bl2 || bl3 || bl4) {
            cMap.aMap[n5] = -16777216;
            cMap.changed = true;
        }
    }

    private boolean isDugAt(int n, int n2) {
        CMap cMap = this.findTileAt(n, n2);
        if (cMap == null) {
            return false;
        }
        int n3 = n2 - cMap.y;
        int n4 = n - cMap.x;
        return cMap.aMap[n3 * cMap.width + n4] == 0xFFFFFF;
    }

    private CMap findTileAt(int n, int n2) {
        for (int i = 0; i < maps.size(); ++i) {
            CMap cMap = (CMap)maps.elementAt(i);
            if (n < cMap.x || n >= cMap.x + cMap.width || n2 < cMap.y || n2 >= cMap.y + cMap.height) continue;
            return cMap;
        }
        return null;
    }

    public static boolean isBossTerrainLocked() {
        if (!PrepareScr.isBossRoom) {
            return false;
        }
        byte by = OfflineBossFight.currentRoomIndex;
        return by == 0 || by == 1 || by == 3 || by == 4;
    }

    public static boolean isProtectedStandPixel(int n, int n2) {
        return false;
    }

    public void update() {
        if (background != null) {
            background.update();
        }
        for (int i = 0; i < maps.size(); ++i) {
            if (maps.elementAt(i) == null) continue;
            ((CMap)maps.elementAt(i)).update();
        }
    }

    public void paint(mGraphics mGraphics2) {
        if (!GameScr.cantSee) {
            for (int i = 0; i < maps.size(); ++i) {
                if (maps.elementAt(i) == null) continue;
                ((CMap)maps.elementAt(i)).paint(mGraphics2);
            }
        }
    }

    public void paintBackGround(mGraphics mGraphics2) {
        if (!GameScr.cantSee && background != null) {
            background.paint(mGraphics2);
        }
    }

    public void paintWater(mGraphics mGraphics2) {
        if (!GameScr.cantSee) {
            Background.drawWater(curWaterType, mGraphics2);
        }
    }

    public void onClearMap() {
        maps.removeAllElements();
    }

    static {
        maps = new Vector();
        mapFiles = new Vector();
        mapImages = new Vector();
        vHoleInfo = new Vector();
    }
}

