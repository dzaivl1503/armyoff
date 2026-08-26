/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import network.Command;
import screen.CScreen;

public class ArchivementScr
extends CScreen {
    public int level;
    public int levelPercen;
    public int xu;
    public int luong;
    public int exp;
    public int nextExp;
    public int cup;
    public String rank;
    public mImage imgClan;

    public ArchivementScr() {
        this.right = new Command(Language.back(), new IAction(){

            public void perform() {
                CCanvas.menuScr.show();
            }
        });
        this.nameCScreen = "Achivement screen!";
    }

    public void paint(mGraphics mGraphics2) {
        ArchivementScr.paintDefaultBg(mGraphics2);
        Font.bigFont.drawString(mGraphics2, "TH\u00c0NH T\u00cdCH", CCanvas.width / 2, 10, 3);
        String string = TerrainMidlet.myInfo.name;
        if (this.imgClan != null) {
            mGraphics2.drawImage(this.imgClan, CCanvas.width / 2 - Font.borderFont.getWidth(string) / 2 - 20, 41, 0, false);
        }
        Font.borderFont.drawString(mGraphics2, string, CCanvas.width / 2, 40, 2);
        mGraphics2.drawImage(CScreen.cup, CCanvas.width / 2 - 20, 60, 0, false);
        Font.borderFont.drawString(mGraphics2, String.valueOf(this.cup), CCanvas.width / 2 + 3, 62, 0);
        Font.borderFont.drawString(mGraphics2, "(" + this.rank + ")", CCanvas.width / 2, 80, 2);
        Font.normalFont.drawString(mGraphics2, this.xu + Language.xu() + " - " + this.luong + Language.luong(), CCanvas.width / 2, 100, 2);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        Font.borderFont.drawString(mGraphics2, "Level " + playerInfo.level2 + (this.level + this.levelPercen >= 0 ? " +" : " ") + this.levelPercen + "%", CCanvas.width / 2, 120, 2);
        mGraphics2.setColor(1521982);
        mGraphics2.fillRect(CCanvas.width / 2 - 50, 145, 102, 17, false);
        mGraphics2.setColor(2378093);
        mGraphics2.fillRect(CCanvas.width / 2 - 50 + 1, 146, 100, 15, false);
        int n = playerInfo.level2Percen * 100 / 100;
        mGraphics2.setColor(16767817);
        mGraphics2.fillRect(CCanvas.width / 2 - 50 + 1, 146, n, 15, false);
        Font.borderFont.drawString(mGraphics2, this.exp + "/" + this.nextExp, CCanvas.width / 2, 145, 2);
        super.paint(mGraphics2);
    }

    public void update() {
        super.update();
    }
}

