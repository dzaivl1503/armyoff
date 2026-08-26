/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mImage;
import java.util.Vector;
import model.ImageIcon;

public class MaterialIconMn {
    public static Vector icons = new Vector();

    public static void addIcon(ImageIcon imageIcon) {
        icons.addElement(imageIcon);
    }

    public static boolean isExistIcon(int n) {
        for (int i = 0; i < icons.size(); ++i) {
            if (((ImageIcon)MaterialIconMn.icons.elementAt((int)i)).id != n) continue;
            return true;
        }
        return false;
    }

    public static mImage getImageFromID(int n) {
        for (int i = 0; i < icons.size(); ++i) {
            if (((ImageIcon)MaterialIconMn.icons.elementAt((int)i)).id != n) continue;
            return ((ImageIcon)MaterialIconMn.icons.elementAt((int)i)).img;
        }
        return null;
    }
}

