/*
 * Decompiled with CFR 0.152.
 */
package Debug;

import CLib.mGraphics;
import CLib.mImage;

public class Debug {
    private static Debug instance;
    public static boolean isDraw;
    private mImage[] bigImage;
    public static int numTileInMap;
    int w;
    int h;
    public static int xM;
    public static int yM;
    public mImage imageTest;
    public static boolean isLockCam;
    public static int inFillImage;

    public static Debug gI() {
        if (instance == null) {
            instance = new Debug();
        }
        return instance;
    }

    public void setup() {
        this.bigImage = new mImage[10];
    }

    public void paint(mGraphics mGraphics2) {
    }

    public void create_RBGImage() {
    }

    public void update() {
    }

    private void onGizmoMouse(mGraphics mGraphics2, int n, int n2, int n3) {
    }

    public static void paintZoneTouch(mGraphics mGraphics2, int n, int n2, String string, int n3, int n4, int n5) {
    }

    public static void onPaintPerformanceInGame() {
    }

    public static void ActionClick(String string) {
    }

    public static void onKeyPress(int n) {
    }

    public static int getNumberFingerOnScreen() {
        return 0;
    }

    static {
        inFillImage = 1;
    }
}

