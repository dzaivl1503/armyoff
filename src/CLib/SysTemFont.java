/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.lcdui.Font
 */
package CLib;

import CLib.mGraphics;
import CLib.mVector;
import javax.microedition.lcdui.Font;

public class SysTemFont {
    public Font font = Font.getDefaultFont();
    public byte charHeight = (byte)this.font.getHeight();
    float yAddFont;

    public SysTemFont(int n, int n2) {
    }

    public SysTemFont(String string, int n, float f) {
    }

    public int getWidth(String string) {
        return this.font.stringWidth(string);
    }

    public int convert_RGB_to_ARGB(int n) {
        int n2 = n >> 16 & 0xFF;
        int n3 = n >> 8 & 0xFF;
        int n4 = n >> 0 & 0xFF;
        return 0xFF000000 | n2 << 16 | n3 << 8 | n4;
    }

    public String replace(String string, String string2, String string3) {
        StringBuffer stringBuffer = new StringBuffer();
        int n = string.indexOf(string2);
        int n2 = 0;
        int n3 = string2.length();
        while (n != -1) {
            stringBuffer.append(string.substring(n2, n)).append(string3);
            n2 = n + n3;
            n = string.indexOf(string2, n2);
        }
        stringBuffer.append(string.substring(n2, string.length()));
        return stringBuffer.toString();
    }

    public String[] splitFont(String string, int n) {
        mVector mVector2 = this._splitFont(string, n);
        String[] stringArray = new String[mVector2.size()];
        for (int i = 0; i < mVector2.size(); ++i) {
            stringArray[i] = mVector2.elementAt(i).toString();
        }
        return stringArray;
    }

    public mVector _splitFont(String string, int n) {
        mVector mVector2 = new mVector();
        if (n <= 0) {
            mVector2.add(string);
            return mVector2;
        }
        String string2 = "";
        for (int i = 0; i < string.length(); ++i) {
            if (string.charAt(i) == '\n') {
                mVector2.addElement(string2);
                string2 = "";
                continue;
            }
            if (this.getWidth(string2 = string2 + string.charAt(i)) > n) {
                int n2 = 0;
                for (n2 = string2.length() - 1; n2 >= 0 && string2.charAt(n2) != ' '; --n2) {
                }
                if (n2 < 0) {
                    n2 = string2.length() - 1;
                }
                mVector2.addElement(string2.substring(0, n2));
                i = i - (string2.length() - n2) + 1;
                string2 = "";
            }
            if (i != string.length() - 1 || string2.trim().equals("")) continue;
            mVector2.addElement(string2);
        }
        return mVector2;
    }

    public int getHeight() {
        return this.charHeight;
    }

    public void drawString(mGraphics mGraphics2, String string, int n, int n2, int n3, boolean bl) {
        int n4 = 8;
        switch (n3) {
            default: {
                break;
            }
            case 1: {
                n4 = 16;
                break;
            }
            case 2: {
                n4 = 1;
            }
        }
        mGraphics2.drawString(string, n, (float)n2 + this.yAddFont, this.font, n4, bl);
    }

    public mVector splitFontVector(String string, int n) {
        mVector mVector2 = new mVector();
        if (n <= 0) {
            mVector2.add(string);
            return mVector2;
        }
        String string2 = "";
        for (int i = 0; i < string.length(); ++i) {
            if (string.charAt(i) != '\n' && string.charAt(i) != '\b') {
                if (this.getWidth(string2 = string2 + string.charAt(i)) > n) {
                    int n2 = 0;
                    for (n2 = string2.length() - 1; n2 >= 0 && string2.charAt(n2) != ' '; --n2) {
                    }
                    if (n2 < 0) {
                        n2 = string2.length() - 1;
                    }
                    mVector2.addElement(string2.substring(0, n2));
                    i = i - (string2.length() - n2) + 1;
                    string2 = "";
                }
                if (i != string.length() - 1 || string2.trim().equals("")) continue;
                mVector2.addElement(string2);
                continue;
            }
            mVector2.addElement(string2);
            string2 = "";
        }
        return mVector2;
    }
}

