/*
 * Decompiled with CFR 0.152.
 */
package item;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import model.Font;
import model.Language;
import screen.GameScr;

public class Item {
    private static mImage s_imgITEM = GameScr.s_imgITEM;
    public static final String[] ITEM_NAME = Language.items();
    public static byte[] NUM_MAX_USED = new byte[]{2, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, -1, -1, -1, -1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    public static byte[] NUM_BUY_PACKAGE = new byte[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
    public static final int IMG_SIZE = 16;
    public static final int IMG_SIZE_2 = 37;
    public static final byte numItem = 36;
    public static final byte ITEM_NULL = -2;
    public static final byte ITEM_AFTER_USED = -1;
    public static final byte ITEM_HEALTH = 0;
    public static final byte ITEM_TELEPORT = 1;
    public static final byte ITEM_DOUBLE = 2;
    public static final byte ITEM_RUN_SPEED = 3;
    public static final byte ITEM_INVISIBLE = 4;
    public static final byte ITEM_STOP_WIND = 5;
    public static final byte ITEM_RANG_CUA_BULL = 6;
    public static final byte ITEM_GENERADE = 7;
    public static final byte ITEM_BOMB_BAY = 8;
    public static final byte ITEM_WEB = 9;
    public static final byte ITEM_HEALTH_FOR_TEAM = 10;
    public static final byte ITEM_DAN_TRAI_PHA = 11;
    public static final byte ITEM_SLOT_1 = 12;
    public static final byte ITEM_SLOT_2 = 13;
    public static final byte ITEM_SLOT_3 = 14;
    public static final byte ITEM_SLOT_4 = 15;
    public static final byte ITEM_LAZER = 16;
    public static final byte ITEM_TORNADO = 17;
    public static final byte ITEM_MOUSE = 18;
    public static final byte ITEM_4MISSILE = 19;
    public static final byte ITEM_UNDERGROUND = 20;
    public static final byte ITEM_METEOR = 21;
    public static final byte ITEM_MRAIN = 22;
    public static final byte ITEM_HOLE = 23;
    public static final byte ITEM_SUICIDE = 24;
    public static final byte ITEM_SMOKE = 25;
    public static final byte ITEM_BIG_HOLE = 26;
    public static final byte ITEM_UFO = 27;
    public static final byte ITEM_FREEZE = 28;
    public static final byte ITEM_POSION = 29;
    public static final byte ITEM_ANGRY = 100;
    public static final byte ITEM_WEB3 = 30;
    public static final byte ITEM_TIME_BOMB = 31;
    public static final byte ITEM_HEALTH_500 = 32;
    public static final byte ITEM_HEALTH_1000 = 33;
    public static final byte ITEM_INVISIBLE_2 = 34;
    public static final byte ITEM_VAMPIRE = 35;
    public static final byte ITEM_RESETPOINT = 36;
    public static final byte ITEM_X2EXP = 37;
    public byte type;
    public String decription;
    public byte num;
    public int price;
    public int price2;
    public byte numUsed;
    public byte numToBuy;
    public byte nCurBuyPackage;
    public byte nCurMaxUsed;
    public boolean isSell = true;
    public boolean isPassive_Item;
    public boolean isCannotBuy;
    public boolean isFreeItem;
    static int blank = 2;
    public static int iWitdh = 4;

    public Item(byte by, byte by2, int n, int n2) {
        this.type = by;
        this.num = by2;
        this.numUsed = 0;
        this.numToBuy = 0;
        this.price = n;
        this.price2 = n2;
        this.decription = ITEM_NAME[by];
        this.nCurBuyPackage = NUM_BUY_PACKAGE[by];
        this.nCurMaxUsed = NUM_MAX_USED[by];
        this.isSell = n >= 0;
        this.isPassive_Item = false;
        switch (this.type) {
            case 0: {
                this.isFreeItem = true;
                this.num = (byte)99;
                break;
            }
            case 1: {
                this.isFreeItem = true;
                this.num = (byte)99;
                break;
            }
            case 12: {
                this.isPassive_Item = true;
                break;
            }
            case 13: {
                this.isPassive_Item = true;
                break;
            }
            case 14: {
                this.isPassive_Item = true;
                break;
            }
            case 15: {
                this.isPassive_Item = true;
            }
        }
    }

    public static void DrawItem(mGraphics mGraphics2, int n, int n2, int n3) {
        try {
            mGraphics2.drawRegion(s_imgITEM, 0, (n + 2) * 16, 16, 16, 0, n2 + 8, n3 + 8, 3, true);
        }
        catch (Exception exception) {
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillRect(n2, n3, 16, 16, true);
        }
    }

    public void drawThisItem(mGraphics mGraphics2, int n, int n2) {
        try {
            mGraphics2.drawRegion(s_imgITEM, 0, (this.type + 2) * 16, 16, 16, 0, n, n2, 0, true);
        }
        catch (Exception exception) {
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillRect(n, n2, 16, 16, true);
        }
    }

    public static void DrawSetItem(mGraphics mGraphics2, int[] nArray, int n, int n2, int n3, boolean bl, byte[] byArray) {
        int n4;
        int n5;
        int n6;
        int n7 = 0;
        int n8 = 0;
        int n9 = 0;
        int n10 = 0;
        int n11 = bl ? 22 : 0;
        for (n6 = 0; n6 < nArray.length; ++n6) {
            n5 = n2 + n7 * (16 + blank + n11);
            n4 = CCanvas.curScr == CCanvas.gameScr ? n3 + n8 * (16 + blank + n11) : n3 + n8 * (16 + blank + n11);
            if (n >= 0 && n6 == n) {
                mGraphics2.setColor(CCanvas.gameTick % 5 > 2 ? 0xFFFF00 : 0xFF0000);
                mGraphics2.fillRect(n5 - blank / 2, n4 - blank / 2, 16 + blank, 16 + blank, false);
            }
            try {
                mGraphics2.drawRegion(s_imgITEM, 0, (nArray[n6] + 2) * 16, 16, 16, 0, n5, n4, 0, false);
            }
            catch (Exception exception) {
                mGraphics2.fillRect(n5, n4, 16, 16, false);
            }
            if (++n7 <= iWitdh - 1) continue;
            n7 = 0;
            ++n8;
        }
        for (n6 = 0; n6 < nArray.length; ++n6) {
            n5 = n2 + n9 * (16 + blank + n11);
            n4 = CCanvas.curScr == CCanvas.gameScr ? n3 + n10 * (16 + blank + n11) : n3 + n10 * (16 + blank + n11);
            try {
                String string = byArray[n6] != 100 ? String.valueOf(byArray[n6]) : "";
                Font.smallFontYellow.drawString(mGraphics2, string, n5 + 12, n4 + 12, 0);
            }
            catch (Exception exception) {
            }
            if (++n9 <= iWitdh - 1) continue;
            n9 = 0;
            ++n10;
        }
    }
}

