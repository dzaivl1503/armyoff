/*
 * Decompiled with CFR 0.152.
 */
package CLib;

import CLib.SysTemFont;
import CLib.mGraphics;
import CLib.mImage;
import CLib.mVector;

public class mFont {
    public static final int LEFT = 0;
    public static final byte RIGHT = 1;
    public static final byte CENTER = 2;
    public static final byte RED = 0;
    public static final byte YELLOW = 1;
    public static final byte GREEN = 2;
    public static final byte FATAL = 3;
    public static final byte MISS = 4;
    public static final byte ORANGE = 5;
    public static final byte ADDMONEY = 6;
    public static final byte MISS_ME = 7;
    public static final byte FATAL_ME = 8;
    private int space;
    private int height;
    private mImage imgFont;
    private String strFont;
    private int[][] fImages;
    public static String str = " 0123456789+-*='_?.,<>/[]{}!@#$%^&*():a\u00c3\u00a1\u00c3\u00a0\u00e1\u00ba\u00a3\u00c3\u00a3\u00e1\u00ba\u00a1\u00c3\u00a2\u00e1\u00ba\u00a5\u00e1\u00ba\u00a7\u00e1\u00ba\u00a9\u00e1\u00ba\u00ab\u00e1\u00ba\u00ad\u00c4\u0192\u00e1\u00ba\u00af\u00e1\u00ba\u00b1\u00e1\u00ba\u00b3\u00e1\u00ba\u00b5\u00e1\u00ba\u00b7bcd\u00c4\u2018e\u00c3\u00a9\u00c3\u00a8\u00e1\u00ba\u00bb\u00e1\u00ba\u00bd\u00e1\u00ba\u00b9\u00c3\u00aa\u00e1\u00ba\u00bf\u00e1\u00bb\ufffd\u00e1\u00bb\u0192\u00e1\u00bb\u2026\u00e1\u00bb\u2021fghi\u00c3\u00ad\u00c3\u00ac\u00e1\u00bb\u2030\u00c4\u00a9\u00e1\u00bb\u2039jklmno\u00c3\u00b3\u00c3\u00b2\u00e1\u00bb\ufffd\u00c3\u00b5\u00e1\u00bb\ufffd\u00c3\u00b4\u00e1\u00bb\u2018\u00e1\u00bb\u201c\u00e1\u00bb\u2022\u00e1\u00bb\u2014\u00e1\u00bb\u2122\u00c6\u00a1\u00e1\u00bb\u203a\u00e1\u00bb\ufffd\u00e1\u00bb\u0178\u00e1\u00bb\u00a1\u00e1\u00bb\u00a3pqrstu\u00c3\u00ba\u00c3\u00b9\u00e1\u00bb\u00a7\u00c5\u00a9\u00e1\u00bb\u00a5\u00c6\u00b0\u00e1\u00bb\u00a9\u00e1\u00bb\u00ab\u00e1\u00bb\u00ad\u00e1\u00bb\u00af\u00e1\u00bb\u00b1vxy\u00c3\u00bd\u00e1\u00bb\u00b3\u00e1\u00bb\u00b7\u00e1\u00bb\u00b9\u00e1\u00bb\u00b5zwA\u00c3\ufffd\u00c3\u20ac\u00e1\u00ba\u00a2\u00c3\u0192\u00e1\u00ba\u00a0\u00c4\u201a\u00e1\u00ba\u00b0\u00e1\u00ba\u00ae\u00e1\u00ba\u00b2\u00e1\u00ba\u00b4\u00e1\u00ba\u00b6\u00c3\u201a\u00e1\u00ba\u00a4\u00e1\u00ba\u00a6\u00e1\u00ba\u00a8\u00e1\u00ba\u00aa\u00e1\u00ba\u00acBCD\u00c4\ufffdE\u00c3\u2030\u00c3\u02c6\u00e1\u00ba\u00ba\u00e1\u00ba\u00bc\u00e1\u00ba\u00b8\u00c3\u0160\u00e1\u00ba\u00be\u00e1\u00bb\u20ac\u00e1\u00bb\u201a\u00e1\u00bb\u201e\u00e1\u00bb\u2020FGHI\u00c3\ufffd\u00c3\u0152\u00e1\u00bb\u02c6\u00c4\u00a8\u00e1\u00bb\u0160JKLMNO\u00c3\u201c\u00c3\u2019\u00e1\u00bb\u017d\u00c3\u2022\u00e1\u00bb\u0152\u00c3\u201d\u00e1\u00bb\ufffd\u00e1\u00bb\u2019\u00e1\u00bb\u201d\u00e1\u00bb\u2013\u00e1\u00bb\u02dc\u00c6\u00a0\u00e1\u00bb\u0161\u00e1\u00bb\u0153\u00e1\u00bb\u017e\u00e1\u00bb\u00a0\u00e1\u00bb\u00a2PQRSTU\u00c3\u0161\u00c3\u2122\u00e1\u00bb\u00a6\u00c5\u00a8\u00e1\u00bb\u00a4\u00c6\u00af\u00e1\u00bb\u00a8\u00e1\u00bb\u00aa\u00e1\u00bb\u00ac\u00e1\u00bb\u00ae\u00e1\u00bb\u00b0VXY\u00c3\ufffd\u00e1\u00bb\u00b2\u00e1\u00bb\u00b6\u00e1\u00bb\u00b8\u00e1\u00bb\u00b4ZW";
    public static mFont tahoma_7b_orange;
    public static mFont tahoma_7b_blue;
    public static mFont tahoma_7b_black;
    public static mFont tahoma_7b_yellow;
    public static mFont tahoma_7b_violet;
    public static mFont tahoma_7b_white;
    public static mFont tahoma_7b_green;
    public static mFont tahoma_7b_red;
    public static mFont tahoma_7b_brown;
    public static mFont tahoma_7_black;
    public static mFont tahoma_7_white;
    public static mFont tahoma_7_yellow;
    public static mFont tahoma_7_orange;
    public static mFont tahoma_7_red;
    public static mFont tahoma_7_blue;
    public static mFont tahoma_7_green;
    public static mFont tahoma_7_violet;
    public static mFont number_yellow;
    public static mFont number_red;
    public static mFont number_green;
    public static mFont number_white;
    public static mFont number_orange;
    public static mFont number_Yellow_Small;
    public static mFont tahoma_8b_brown;
    public static mFont tahoma_8b_black;
    public static mFont tahoma_7_gray;
    SysTemFont temfont;
    String pathImage;
    public static int[] colorJava;
    public static int[] colorJava1;
    public SysTemFont f;

    public static void loadmFont() {
        tahoma_7b_orange = new mFont(0, -90838);
        tahoma_7b_blue = new mFont(1, -9265665);
        tahoma_7b_black = new mFont(2, -15527149);
        tahoma_7b_yellow = new mFont(3, -197061);
        tahoma_7b_violet = new mFont(4, -4947201);
        tahoma_7b_white = new mFont(5, -1);
        tahoma_7b_green = new mFont(6, -10035407);
        tahoma_7b_brown = new mFont(7, -12052464);
        tahoma_7b_red = new mFont(8, -65536);
        tahoma_7_black = new mFont(9, -15527149);
        tahoma_7_white = new mFont(10, -1);
        tahoma_7_yellow = new mFont(11, -197061);
        tahoma_7_orange = new mFont(12, -90838);
        tahoma_7_red = new mFont(13, -65536);
        tahoma_7_blue = new mFont(14, -9265665);
        tahoma_7_green = new mFont(15, -10035407);
        tahoma_7_violet = new mFont(21, -4947201);
        number_yellow = new mFont(16, -197061);
        number_red = new mFont(17, -65536);
        number_green = new mFont(18, -10035407);
        number_white = new mFont(19, -1);
        number_orange = new mFont(20, -90838);
        number_Yellow_Small = new mFont(22, -197061);
        tahoma_8b_brown = new mFont(30, -4819663);
        tahoma_8b_black = new mFont(31, -15527149);
        tahoma_7_gray = new mFont(10, -10000537);
    }

    public int setColoFont(int n) {
        return colorJava[n + 1];
    }

    public mFont(int n, int n2) {
        this.f = new SysTemFont(n, n2);
    }

    public mFont(String string, byte[] byArray, int n, String string2, int n2, int n3) {
        this.f = new SysTemFont(n3, this.setColoFont(n3));
    }

    public void reloadImage() {
    }

    public void freeImage() {
    }

    public int getHeight() {
        return this.f.getHeight();
    }

    public void setHeight(int n) {
        this.height = n;
    }

    public int getWidth(String string) {
        return this.f.getWidth(string);
    }

    public void drawString(mGraphics mGraphics2, String string, int n, int n2, int n3, boolean bl) {
        this.f.drawString(mGraphics2, string, n, n2, n3, bl);
    }

    public mVector splitFontVector(String string, int n) {
        return this.f.splitFontVector(string, n);
    }

    public static String[] split(String string, String string2) {
        mVector mVector2 = new mVector();
        int n = string.indexOf(string2);
        while (n >= 0) {
            mVector2.addElement(string.substring(0, n));
            string = string.substring(n + string2.length());
            n = string.indexOf(string2);
        }
        mVector2.addElement(string);
        String[] stringArray = new String[mVector2.size()];
        if (mVector2.size() > 0) {
            for (int i = 0; i < mVector2.size(); ++i) {
                stringArray[i] = (String)mVector2.elementAt(i);
            }
        }
        return stringArray;
    }

    public String splitFirst(String string) {
        String string2 = "";
        boolean bl = false;
        for (int i = 0; i < string.length(); ++i) {
            if (!bl) {
                String string3 = string.substring(i, string.length());
                string2 = this.compare(string3, " ") ? string2 + string.charAt(i) + "-" : string2 + string3;
                bl = true;
                continue;
            }
            if (string.charAt(i) != ' ') continue;
            bl = false;
        }
        return string2;
    }

    public String[] splitFontArray(String string, int n) {
        mVector mVector2 = this.splitFontVector(string, n);
        String[] stringArray = new String[mVector2.size()];
        for (int i = 0; i < mVector2.size(); ++i) {
            stringArray[i] = mVector2.elementAt(i).toString();
        }
        return stringArray;
    }

    public boolean compare(String string, String string2) {
        for (int i = 0; i < string.length(); ++i) {
            if (!String.valueOf(string.charAt(i)).equals(string2)) continue;
            return true;
        }
        return false;
    }

    static {
        colorJava = new int[]{-90838, -9265665, -15527149, -197061, -4947201, -1, -10035407, -12052464, -65536, -15527149, -1, -197061, -90838, -65536, -9265665, -10035407, -4947201, -197061, -65536, -10035407, -1, -90838, -197061, -4819663, -15527149, -10000537};
        colorJava1 = new int[]{-90838, -9265665, -15527149, -197061, -4947201, -1, -10035407, -12052464, -65536, -15527149, -1, -197061, -90838, -65536, -9265665, -10035407, -4947201, -197061, -65536, -10035407, -1, -90838, -197061, -4819663, -15527149, -10000537};
    }
}

