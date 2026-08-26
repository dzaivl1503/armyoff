/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import CLib.mImage;
import Equipment.Equip;
import coreLG.CCanvas;
import effect.Explosion;
import item.Item;
import model.CRes;
import model.Font;
import player.Boss;

public class LuckyGift {
    public int id;
    public String info;
    public mImage icon;
    public int num;
    public int type;
    public int itemID = -1;
    public Equip rewardIcon;
    public int luckyCode;
    public int tCount = 0;
    public int index = CRes.random(0, 5);
    public boolean isWait = false;
    public boolean isServerSend = false;
    public boolean isShow = false;
    int[] dx = new int[]{-2, 2, -2, 2, -2, 2, 0, 0, 0, 0};
    int x;
    int y;

    public void setIcon(byte[] byArray, int n) {
        this.icon = mImage.createImage(byArray, 0, n, "");
    }

    public void paint(mGraphics mGraphics2, int n, int n2) {
        this.x = n;
        this.y = n2;
        if (this.isShow) {
            if (this.rewardIcon != null) {
                this.rewardIcon.drawIcon(mGraphics2, n - 8, n2 - 8, false);
                Font.borderFont.drawString(mGraphics2, LuckyGift.fitLabel(this.info), n, n2 + 14, 3);
            } else if (this.type == 0 || this.type == 1) {
                Font.borderFont.drawString(mGraphics2, this.info, n, n2, 3);
            } else if (this.type == 3) {
                Item.DrawItem(mGraphics2, this.itemID, n - 8, n2 - 8);
                Font.borderFont.drawString(mGraphics2, this.info, n + 8, n2 + 6, 3);
            } else if (this.type == 2 && this.icon != null) {
                mGraphics2.drawImage(this.icon, n, n2, 3, false);
                Font.borderFont.drawString(mGraphics2, this.info, n + 8, n2 + 6, 3);
            }
        } else {
            mGraphics2.drawImage(Boss.gift_1, n + this.dx[this.index], n2, 3, false);
        }
    }

    private static String fitLabel(String string) {
        int n;
        if (string == null) {
            return "";
        }
        int n2 = n = CCanvas.width < 240 ? 34 : 44;
        if (Font.borderFont.getWidth(string) <= n) {
            return string;
        }
        String string2 = string;
        while (string2.length() > 1 && Font.borderFont.getWidth(string2 + "..") > n) {
            string2 = string2.substring(0, string2.length() - 1);
        }
        return string2 + "..";
    }

    public void update() {
        if (this.isWait) {
            ++this.tCount;
            ++this.index;
            if (this.index == this.dx.length) {
                this.index = 0;
            }
            if (this.type != 2) {
                if (this.tCount > 15 && this.isServerSend) {
                    this.isWait = false;
                    this.isShow = true;
                    this.isServerSend = false;
                    new Explosion(this.x, this.y + 6, 1);
                }
            } else if (this.tCount > 15 && this.icon != null && this.isServerSend) {
                this.isWait = false;
                this.isShow = true;
                this.isServerSend = false;
                new Explosion(this.x, this.y + 6, 1);
            }
        }
    }
}

