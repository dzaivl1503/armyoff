/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.Image;
import java.util.Vector;
import model.CRes;
import model.Clan;

public class IconManager {
    public Vector icon = new Vector();

    public void addIcon(Clan clan) {
        if (this.icon.size() < 20) {
            this.icon.addElement(clan);
            CRes.out("================> IconManager add icon");
        } else {
            this.icon.removeElementAt(19);
            this.icon.insertElementAt(clan, 0);
        }
    }

    public boolean isExist(int n) {
        if (this.icon.size() == 0) {
            return false;
        }
        for (int i = 0; i < this.icon.size(); ++i) {
            Clan clan = (Clan)this.icon.elementAt(i);
            if (clan.id != n) continue;
            return true;
        }
        return false;
    }

    public Image getImage(int n) {
        for (int i = 0; i < this.icon.size(); ++i) {
            Clan clan = (Clan)this.icon.elementAt(i);
            if (clan.id != n) continue;
            return clan.icon.image;
        }
        return null;
    }
}

