/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import CLib.mImage;
import Equipment.Equip;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import model.Font;
import model.Language;

public class Fomula {
    public String[] numMaterial;
    public mImage[] imgMaterial;
    public int[] idImage;
    public Equip equipRequire;
    public boolean isHave;
    public int levelRequire;
    public Equip e;
    public Equip eRequire;
    public String[] materialName;
    public String[] ability;
    public boolean finish;
    public int ID;
    public int h1;
    public int h2;

    public mImage getImageMaterial(int n) {
        for (int i = 0; i < this.idImage.length; ++i) {
            if (this.idImage[i] != n) continue;
            return this.imgMaterial[i];
        }
        return null;
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        Font.bigFont.drawString(mGraphics2, Language.congthucchetao(), CCanvas.width / 2, 5, 3);
        if (this.e != null) {
            Font.normalFont.drawString(mGraphics2, this.e.name, CCanvas.width / 2, 30, 3);
            this.e.drawIcon(mGraphics2, CCanvas.width / 2 + Font.normalFont.getWidth(this.e.name) / 2 + 10, 30, false);
        }
        for (n = 0; n < this.ability.length; ++n) {
            Font.borderFont.drawString(mGraphics2, this.ability[n], CCanvas.width / 2, 50 + n * 18, 3);
        }
        n = TerrainMidlet.myInfo.level2;
        Font.borderFont.drawString(mGraphics2, Language.levelRequire() + ": " + n + "/" + this.levelRequire, CCanvas.width / 2, this.h1 + 53, 3);
        this.equipRequire.drawIcon(mGraphics2, CCanvas.width / 2 - 70 - 8, this.h1 + 78 - 8, false);
        String string = Fomula.truncateBeforeRight(this.equipRequire.name, this.isHave ? "1/1" : "0/1", 165);
        Font.normalFont.drawString(mGraphics2, string, CCanvas.width / 2 - 55, this.h1 + 71, 0);
        Font.normalFont.drawString(mGraphics2, this.isHave ? "1/1" : "0/1", CCanvas.width / 2 + 110, this.h1 + 71, 3);
        for (int i = 0; i < this.imgMaterial.length; ++i) {
            if (this.imgMaterial[i] != null) {
                mGraphics2.drawImage(this.imgMaterial[i], CCanvas.width / 2 - 70, this.h1 + 97 + i * 18, 3, false);
            }
            String string2 = Fomula.truncateBeforeRight(this.materialName[i], this.numMaterial[i], 165);
            Font.normalFont.drawString(mGraphics2, string2, CCanvas.width / 2 - 55, this.h1 + 89 + i * 18, 0);
            Font.normalFont.drawString(mGraphics2, this.numMaterial[i], CCanvas.width / 2 + 110, this.h1 + 89 + i * 18, 3);
        }
    }

    public int contentBottomY() {
        return this.h1 + 97 + this.imgMaterial.length * 18;
    }

    private static String truncateBeforeRight(String string, String string2, int n) {
        int n2 = Font.normalFont.getWidth(string2) / 2;
        int n3 = n - n2 - 4;
        if (n3 < 20) {
            n3 = 20;
        }
        return Fomula.truncateToWidth(string, n3);
    }

    private static String truncateToWidth(String string, int n) {
        int n2;
        if (Font.normalFont.getWidth(string) <= n) {
            return string;
        }
        String string2 = "...";
        for (n2 = string.length(); n2 > 0 && Font.normalFont.getWidth(string.substring(0, n2) + string2) > n; --n2) {
        }
        return n2 > 0 ? string.substring(0, n2) + string2 : string2;
    }

    public void update() {
    }

    public void input() {
    }
}

