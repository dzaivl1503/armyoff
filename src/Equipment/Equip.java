/*
 * Decompiled with CFR 0.152.
 */
package Equipment;

import CLib.mGraphics;
import CLib.mImage;
import Equipment.PlayerEquip;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.Font;
import model.Language;
import model.PlayerInfo;
import screen.EquipScreen;

public class Equip {
    public int dbKey;
    public short id;
    public byte frame;
    public short[] x;
    public short[] y;
    public byte[] w;
    public byte[] h;
    public byte[] dx;
    public byte[] dy;
    public short icon;
    public byte type;
    public byte glass;
    public byte slot;
    public byte[] socketGems = new byte[]{-1, -1, -1};
    public byte vip;
    public boolean isVip;
    public int num = 1;
    public int numSelected = 1;
    public boolean isBuyNum = false;
    public short[] inv_attAddPoint = new short[5];
    public byte[] inv_ability = new byte[5];
    public byte[] inv_percen = new byte[5];
    public short[] shop_attAddPoint = new short[5];
    public byte[] shop_ability = new byte[5];
    public byte[] shop_percen = new byte[5];
    public int index;
    public byte date;
    public String name;
    public int xu;
    public int luong;
    public byte level;
    public int level2;
    public byte craftTier;
    public boolean isSelect = false;
    public byte bullet;
    public boolean isMaterial = false;
    public String strDetail;
    public mImage materialIcon;
    public boolean notPaint;
    public int shopDetailNunmLines;
    public Vector shopDetailNunStrs = null;
    int header;

    public Equip(short s, byte by, byte n, short[] sArray, short[] sArray2, byte[] byArray, byte[] byArray2, byte[] byArray3, byte[] byArray4, short s2, byte by2) {
        this.id = s;
        this.type = by;
        this.frame = n;
        this.x = new short[n];
        this.y = new short[n];
        this.w = new byte[n];
        this.h = new byte[n];
        this.dx = new byte[n];
        this.dy = new byte[n];
        for (int i = 0; i < n; ++i) {
            this.x[i] = sArray[i];
            this.y[i] = sArray2[i];
            this.w[i] = byArray[i];
            this.h[i] = byArray2[i];
            this.dx[i] = byArray3[i];
            this.dy[i] = byArray4[i];
        }
        this.icon = s2;
        this.type = by;
        this.bullet = by2;
    }

    public Equip() {
    }

    public int[] getBaseAttribute() {
        int n;
        int[] nArray = new int[5];
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        int[] nArray2 = new int[5];
        int[] nArray3 = new int[5];
        for (n = 0; n < 5; ++n) {
            int n2 = n;
            nArray2[n2] = nArray2[n2] + this.inv_ability[n];
            int n3 = n;
            nArray3[n3] = nArray3[n3] + this.inv_percen[n];
        }
        nArray[0] = nArray2[0] * 10;
        nArray[0] = nArray[0] + (1000 + playerInfo.ability[0] * 10) * nArray3[0] / 100;
        n = PlayerEquip.getEquipGlass((byte)playerInfo.gun).maxDamage;
        int n4 = nArray2[1] + playerInfo.attribute[1];
        int n5 = nArray2[2] + playerInfo.attribute[2];
        int n6 = nArray2[3] + playerInfo.attribute[3];
        int n7 = nArray2[4] + playerInfo.attribute[4];
        nArray[1] = n * (n4 / 3 + 100 + nArray3[1]) / 100;
        nArray[2] = n5 * 10;
        nArray[2] = nArray[2] + nArray[2] * nArray3[2] / 100;
        nArray[3] = n6 * 10;
        nArray[3] = nArray[3] + nArray[3] * nArray3[3] / 100;
        nArray[4] = n7 * 10;
        nArray[4] = nArray[4] + nArray[4] * nArray3[4] / 100;
        return nArray;
    }

    public void setInvAtribute() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        for (int i = 0; i < 5; ++i) {
            this.inv_attAddPoint[i] = 0;
            short[] sArray = this.inv_attAddPoint;
            sArray[i] = (short)(sArray[i] + this.inv_ability[i]);
            sArray = this.inv_attAddPoint;
            sArray[i] = (short)(sArray[i] + playerInfo.attribute[i] * this.inv_percen[i] / 100);
        }
    }

    public void removeAbility() {
        for (int i = 0; i < 5; ++i) {
            this.inv_attAddPoint[i] = 0;
            this.inv_percen[i] = 0;
            this.inv_ability[i] = 0;
        }
    }

    public void addAbilityFromEquip(Equip equip) {
        for (int i = 0; i < 5; ++i) {
            this.inv_ability[i] = equip.inv_ability[i];
            this.inv_attAddPoint[i] = equip.inv_attAddPoint[i];
            this.inv_percen[i] = equip.inv_percen[i];
        }
    }

    public void getInvAtribute(byte[] byArray) {
        int n;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        int n2 = 0;
        int n3 = 0;
        int n4 = 0;
        for (n = 0; n < byArray.length; ++n) {
            short[] sArray;
            this.inv_attAddPoint[n2] = 0;
            if (n % 2 == 0) {
                sArray = this.inv_attAddPoint;
                sArray[n2] = (short)(sArray[n2] + byArray[n]);
                continue;
            }
            sArray = this.inv_attAddPoint;
            sArray[n2] = (short)(sArray[n2] + playerInfo.attribute[n2] * byArray[n] / 100);
            ++n2;
        }
        for (n = 0; n < byArray.length; ++n) {
            if (n % 2 == 0) {
                this.inv_ability[n3] = byArray[n];
                ++n3;
                continue;
            }
            this.inv_percen[n4] = byArray[n];
            ++n4;
        }
    }

    public boolean isSameEquip(Equip equip) {
        return this.id == equip.id && this.type == equip.type && this.glass == equip.glass;
    }

    public void getShopAtribute(byte[] byArray) {
        int n = 0;
        int n2 = 0;
        for (int i = 0; i < byArray.length; ++i) {
            if (i % 2 == 0) {
                this.shop_ability[n] = byArray[i];
                ++n;
                continue;
            }
            this.shop_percen[n2] = byArray[i];
            ++n2;
        }
    }

    public void changeToEquip(Equip equip) {
        int n;
        this.icon = equip.icon;
        this.glass = equip.glass;
        this.type = equip.type;
        this.id = equip.id;
        this.date = equip.date;
        int n2 = equip.x.length;
        this.x = new short[n2];
        this.y = new short[n2];
        this.w = new byte[n2];
        this.h = new byte[n2];
        this.dx = new byte[n2];
        this.dy = new byte[n2];
        for (n = 0; n < n2; ++n) {
            this.x[n] = equip.x[n];
            this.y[n] = equip.y[n];
            this.dx[n] = equip.dx[n];
            this.dy[n] = equip.dy[n];
            this.w[n] = equip.w[n];
            this.h[n] = equip.h[n];
        }
        this.bullet = equip.bullet;
        this.frame = equip.frame;
        this.addAbilityFromEquip(equip);
        this.dbKey = equip.dbKey;
        this.level = equip.level;
        this.slot = equip.slot;
        this.craftTier = equip.craftTier;
        this.name = equip.name;
        this.socketGems = new byte[]{-1, -1, -1};
        if (equip.socketGems != null) {
            for (n = 0; n < this.socketGems.length && n < equip.socketGems.length; ++n) {
                this.socketGems[n] = equip.socketGems[n];
            }
        }
    }

    public int socketCount() {
        int n = 0;
        if (this.socketGems != null) {
            for (int i = 0; i < this.socketGems.length; ++i) {
                if (this.socketGems[i] < 0) continue;
                ++n;
            }
        }
        return n;
    }

    public String getStrInvDetail() {
        String string = "";
        if (this.inv_ability[0] != 0) {
            string = string + Language.sinhluc() + " +" + this.inv_ability[0] + ".";
        }
        if (this.inv_ability[1] != 0) {
            string = string + " " + Language.sucmanh() + " +" + this.inv_ability[1] + ".";
        }
        if (this.inv_ability[2] != 0) {
            string = string + " " + Language.phongthu() + " +" + this.inv_ability[2] + ".";
        }
        if (this.inv_ability[3] != 0) {
            string = string + " " + Language.mayman() + " +" + this.inv_ability[3] + ".";
        }
        if (this.inv_ability[4] != 0) {
            string = string + " " + Language.dongdoi() + " +" + this.inv_ability[4] + ".";
        }
        if (this.vip == 0) {
            if (this.inv_percen[0] != 0) {
                string = string + " " + Language.sinhluc() + " +" + this.inv_percen[0] + "%.";
            }
            if (this.inv_percen[1] != 0) {
                string = string + " " + Language.sucmanh() + " +" + this.inv_percen[1] + "%.";
            }
            if (this.inv_percen[2] != 0) {
                string = string + " " + Language.phongthu() + " +" + this.inv_percen[2] + "%.";
            }
            if (this.inv_percen[3] != 0) {
                string = string + " " + Language.mayman() + " +" + this.inv_percen[3] + "%.";
            }
            if (this.inv_percen[4] != 0) {
                string = string + " " + Language.dongdoi() + " +" + this.inv_percen[4] + "%.";
            }
        } else {
            string = string + " " + Language.All5();
        }
        string = this.date <= 0 ? string + " V\u0129nh vi\u1ec5n." : string + " " + Language.expr() + ": " + this.date + ".";
        int n = 3 - this.slot;
        if (n < 0) {
            n = 0;
        }
        string = string + " " + Language.eSlot() + " " + n + ".";
        return string;
    }

    public String getStrShopDetail() {
        String string = "";
        this.shopDetailNunmLines = 0;
        this.shopDetailNunStrs = new Vector();
        if (this.shop_ability[0] != 0) {
            string = string + Language.sinhluc() + " +" + this.shop_ability[0] + " ";
            this.shopDetailNunStrs.addElement(Language.sinhluc() + " +" + this.shop_ability[0] + " ");
            ++this.shopDetailNunmLines;
        }
        if (this.shop_ability[1] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.sucmanh() + " +" + this.shop_ability[1] + " ";
            this.shopDetailNunStrs.addElement(" " + Language.sucmanh() + " +" + this.shop_ability[1] + " ");
        }
        if (this.shop_ability[2] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.phongthu() + " +" + this.shop_ability[2] + " ";
            this.shopDetailNunStrs.addElement(" " + Language.phongthu() + " +" + this.shop_ability[2] + " ");
        }
        if (this.shop_ability[3] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.mayman() + " +" + this.shop_ability[3] + " ";
            this.shopDetailNunStrs.addElement(" " + Language.mayman() + " +" + this.shop_ability[3] + " ");
        }
        if (this.shop_ability[4] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.dongdoi() + " +" + this.shop_ability[4] + " ";
            this.shopDetailNunStrs.addElement(" " + Language.dongdoi() + " +" + this.shop_ability[4] + " ");
        }
        if (this.shop_percen[0] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.sinhluc() + " +" + this.shop_percen[0] + "% ";
            this.shopDetailNunStrs.addElement(" " + Language.sinhluc() + " +" + this.shop_percen[0] + "% ");
        }
        if (this.shop_percen[1] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.sucmanh() + " +" + this.shop_percen[1] + "% ";
            this.shopDetailNunStrs.addElement(" " + Language.sucmanh() + " +" + this.shop_percen[1] + "% ");
        }
        if (this.shop_percen[2] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.phongthu() + " +" + this.shop_percen[2] + "% ";
            this.shopDetailNunStrs.addElement(" " + Language.phongthu() + " +" + this.shop_percen[2] + "% ");
        }
        if (this.shop_percen[3] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.mayman() + " +" + this.shop_percen[3] + "% ";
            this.shopDetailNunStrs.addElement(" " + Language.mayman() + " +" + this.shop_percen[3] + "% ");
        }
        if (this.shop_percen[4] != 0) {
            ++this.shopDetailNunmLines;
            string = string + " " + Language.dongdoi() + " +" + this.shop_percen[4] + "%";
            this.shopDetailNunStrs.addElement(" " + Language.dongdoi() + " +" + this.shop_percen[4] + "% ");
        }
        return string;
    }

    public void drawImage(mGraphics mGraphics2, int n, int n2, int n3, int n4) {
        if (!this.notPaint) {
            int n5;
            int n6 = 0;
            int n7 = 0;
            if (this.glass == 0 || this.glass == 1 || this.glass == 2 || this.glass == 4 || this.glass == 5 || this.glass == 8 || this.glass == 9) {
                n6 = 24;
                n7 = 24;
            }
            if (this.glass == 3) {
                n6 = 30;
                n7 = 32;
            }
            if (this.glass == 6) {
                n6 = 29;
                n7 = 24;
            }
            if (this.glass == 7) {
                n6 = 32;
                n7 = 32;
            }
            if (this.glass == 10) {
                n6 = 32;
                n7 = 32;
            }
            int n8 = n5 = this.x == null ? -1 : this.x.length - 1;
            if (n2 > n5) {
                n2 = n5;
            }
            if (n2 < 0) {
                n2 = 0;
            }
            if (n2 <= n5) {
                if (n == 0) {
                    mGraphics2.drawRegion(PlayerEquip.imgData[this.glass], this.x[n2], this.y[n2], this.w[n2], this.h[n2], n, n3 - n6 / 2 + this.dx[n2] + 18, n4 - n7 + this.dy[n2] + 40, 0, false);
                    Font.smallFont.drawString(mGraphics2, " ", n3 - n6 / 2 + this.dx[n2] + 18, n4 - n7 + this.dy[n2] + 40, 0, false);
                }
                if (n == 2) {
                    mGraphics2.drawRegion(PlayerEquip.imgData[this.glass], this.x[n2], this.y[n2], this.w[n2], this.h[n2], n, n3 + n6 / 2 - (this.dx[n2] + 18), n4 - n7 + this.dy[n2] + 40, mGraphics.TOP | mGraphics.RIGHT, false);
                    Font.smallFont.drawString(mGraphics2, "  ", n3 + n6 / 2 - (this.dx[n2] + 18), n4 - n7 + this.dy[n2] + 40, 0, false);
                }
            }
        }
    }

    public void drawIcon(mGraphics mGraphics2, int n, int n2, boolean bl) {
        if (!this.isMaterial) {
            int n3;
            int n4 = 0;
            for (n3 = this.icon * 16; n3 >= 1024; n3 -= 1024) {
                ++n4;
            }
            mGraphics2.drawRegion(EquipScreen.imgIconEQ[n4], 0, n3, 16, 16, 0, n, n2, 0, bl);
        } else {
            if (this.materialIcon != null) {
                mGraphics2.drawImage(this.materialIcon, n + 8, n2 + 8, 3, bl);
            } else {
                int n5 = 5143986;
                if (this.id < 10) {
                    n5 = 4368459;
                } else if (this.id < 20) {
                    n5 = 13978181;
                } else if (this.id < 30) {
                    n5 = 4425432;
                } else if (this.id < 40) {
                    n5 = 14727229;
                } else if (this.id < 50) {
                    n5 = 10180294;
                } else if (this.id >= 57 && this.id <= 61 || this.id >= 69 && this.id <= 73) {
                    n5 = 13867565;
                }
                mGraphics2.setColor(n5);
                mGraphics2.fillRect(n + 2, n2 + 2, 12, 12, bl);
                mGraphics2.setColor(0xFFFFFF);
                mGraphics2.drawRect(n + 4, n2 + 4, 7, 7, bl);
            }
            if (this.num > 1) {
                Font.smallFontYellow.drawString(mGraphics2, String.valueOf(this.num), n + 11, n2 + 11, 0);
            }
            if (this.numSelected > 1 && !this.isSelect || this.numSelected >= 1 && this.isSelect) {
                Font.smallFontRed.drawString(mGraphics2, String.valueOf(this.numSelected), n + 11, n2, 0);
            }
        }
        if (this.isSelect) {
            mGraphics2.setColor(0);
            mGraphics2.drawRect(n, n2, 16, 16, true);
        }
    }
}

