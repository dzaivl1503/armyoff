/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import model.Font;
import model.IAction;
import model.PlayerInfo;
import network.Command;
import screen.CScreen;
import screen.ChangePlayerCSr;
import screen.MenuScr;
import screen.PrepareScr;

public class SquadSelectScr
extends CScreen {
    private CScreen lastScr;
    private PlayerEquip[] equip = new PlayerEquip[11];
    private boolean[] selected = new boolean[11];
    private int curMenu;
    private final int nMainIcon;
    private int blankW = w / 2 - 50;
    private int[] _iconX;
    private int _centerIX;
    private static final int COLOR_MAIN = 2003199;
    private static final int COLOR_SELECTED = 16762880;

    public SquadSelectScr() {
        this.nMainIcon = 11;
        this.getClass();
        int n = (w >> 1) - 11 * (24 + this.blankW) / 2;
        this.getClass();
        this._iconX = new int[11];
        int n2 = 0;
        while (true) {
            this.getClass();
            if (n2 >= 11) break;
            this._iconX[n2] = n + n2 * this.blankW;
            ++n2;
        }
        this._centerIX = w >> 1;
        this.nameCScreen = "SquadSelectScr screen!";
        this.left = new Command("Hu\u1ef7", new IAction(){

            public void perform() {
                SquadSelectScr.this.doClose();
            }
        });
        this.right = new Command("X\u00e1c nh\u1eadn", new IAction(){

            public void perform() {
                SquadSelectScr.this.doConfirm();
            }
        });
        this.center = new Command("Ch\u1ecdn/B\u1ecf", new IAction(){

            public void perform() {
                SquadSelectScr.this.toggleCurrent();
            }
        });
    }

    private void loadEquipIcons() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        for (int i = 0; i < 11; ++i) {
            boolean bl = TerrainMidlet.isVip[i];
            short[] sArray = new short[]{(short)i, 0, !bl ? playerInfo.equipID[i][0] : playerInfo.equipVipID[i][0]};
            short[] sArray2 = new short[]{(short)i, 1, !bl ? playerInfo.equipID[i][1] : playerInfo.equipVipID[i][1]};
            short[] sArray3 = new short[]{(short)i, 2, !bl ? playerInfo.equipID[i][2] : playerInfo.equipVipID[i][2]};
            short[] sArray4 = new short[]{(short)i, 3, !bl ? playerInfo.equipID[i][3] : playerInfo.equipVipID[i][3]};
            short[] sArray5 = new short[]{(short)i, 4, !bl ? playerInfo.equipID[i][4] : playerInfo.equipVipID[i][4]};
            short[] sArray6 = new short[]{(short)i, 5, !bl ? playerInfo.equipID[i][5] : playerInfo.equipVipID[i][5]};
            this.equip[i] = new PlayerEquip(new short[][]{sArray, sArray2, sArray3, sArray4, sArray5, sArray6});
        }
    }

    private int countSelected() {
        int n = 0;
        for (int i = 0; i < this.selected.length; ++i) {
            if (!this.selected[i]) continue;
            ++n;
        }
        return n;
    }

    private void toggleCurrent() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (this.curMenu == playerInfo.gun) {
            CCanvas.startOKDlg("\u0110\u00e2y l\u00e0 nh\u00e2n v\u1eadt ch\u00ednh, lu\u00f4n tham chi\u1ebfn.");
            return;
        }
        if (ChangePlayerCSr.isUnlock[this.curMenu] == 0) {
            CCanvas.startOKDlg("Nh\u00e2n v\u1eadt n\u00e0y ch\u01b0a m\u1edf kho\u00e1.");
            return;
        }
        if (!this.selected[this.curMenu]) {
            if (this.countSelected() >= 3) {
                CCanvas.startOKDlg("Ch\u1ec9 \u0111\u01b0\u1ee3c ch\u1ecdn t\u1ed1i \u0111a 3 nh\u00e2n v\u1eadt ph\u1ee5.");
                return;
            }
            this.selected[this.curMenu] = true;
        } else {
            this.selected[this.curMenu] = false;
        }
    }

    private void doConfirm() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        byte[] byArray = new byte[]{-1, -1, -1};
        int n = 0;
        for (int i = 0; i < this.selected.length && n < byArray.length; ++i) {
            if (!this.selected[i]) continue;
            byArray[n] = (byte)i;
            ++n;
        }
        playerInfo.squadExtra = byArray;
        OfflineSave.save();
        MenuScr.refreshLapDoiSubMenu();
        this.doClose();
    }

    private void doClose() {
        CCanvas.endDlg();
        if (CCanvas.menu != null) {
            CCanvas.menu.showMenu = false;
        }
        if (this.lastScr instanceof MenuScr) {
            MenuScr menuScr = (MenuScr)this.lastScr;
            menuScr.hide = false;
            menuScr.scrollDown = false;
            menuScr.scrollUp = false;
            menuScr.finishScrollOpen();
        }
        this.lastScr.show();
    }

    public void show(CScreen cScreen) {
        int n;
        this.lastScr = cScreen;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        for (n = 0; n < this.selected.length; ++n) {
            this.selected[n] = false;
        }
        for (n = 0; n < playerInfo.squadExtra.length; ++n) {
            if (playerInfo.squadExtra[n] < 0) continue;
            this.selected[playerInfo.squadExtra[n]] = true;
        }
        this.curMenu = playerInfo.gun;
        this.loadEquipIcons();
        CCanvas.arrPopups.removeAllElements();
        CCanvas.msgPopup.nMessage = 0;
        super.show();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (CCanvas.keyPressed[4] || keyLeft || CCanvas.keyPressed[2]) {
            this.moveTo(this.getLastP(this.curMenu));
        }
        if (CCanvas.keyPressed[6] || keyRight || CCanvas.keyPressed[8]) {
            this.moveTo(this.getNextP(this.curMenu));
        }
        if (CCanvas.isPointerClick[n3]) {
            if (CCanvas.isPointer(0, 0, CCanvas.width / 2 - 30, CCanvas.hieght - cmdH, n3)) {
                this.moveTo(this.getLastP(this.curMenu));
            }
            if (CCanvas.isPointer(CCanvas.width / 2 + 30, 0, CCanvas.width / 2 - 30, CCanvas.hieght - cmdH, n3)) {
                this.moveTo(this.getNextP(this.curMenu));
            }
            if (CCanvas.isPointer(CCanvas.width / 2 - 30, 0, 60, CCanvas.hieght - cmdH, n3)) {
                this.toggleCurrent();
            }
        }
        SquadSelectScr.clearKey();
    }

    private void moveTo(int n) {
        this.curMenu = n;
    }

    public int getNextP(int n) {
        this.getClass();
        return n + 1 > 11 - 1 ? 0 : n + 1;
    }

    public int getLastP(int n) {
        int n2;
        if (n - 1 < 0) {
            this.getClass();
            n2 = 11 - 1;
        } else {
            n2 = n - 1;
        }
        return n2;
    }

    public void update() {
        Cloud.updateCloud();
        this.moveMenu();
    }

    private void moveMenu() {
        int n = Math.max(Math.abs(this._centerIX - this._iconX[this.curMenu] >> 1), 1);
        if (this._iconX[this.curMenu] < this._centerIX) {
            int n2 = 0;
            while (true) {
                this.getClass();
                if (n2 < 11) {
                    int n3 = n2++;
                    this._iconX[n3] = this._iconX[n3] + n;
                    continue;
                }
                break;
            }
        } else if (this._iconX[this.curMenu] > this._centerIX) {
            int n4 = 0;
            while (true) {
                this.getClass();
                if (n4 >= 11) break;
                int n5 = n4++;
                this._iconX[n5] = this._iconX[n5] - n;
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        SquadSelectScr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        int n = h >> 1;
        Font.bigFont.drawString(mGraphics2, PrepareScr.GUN_NAME[this.curMenu], w / 2, n - 62, 2);
        String string = "\u0110\u00e3 ch\u1ecdn: " + this.countSelected() + "/3";
        Font.borderFont.drawString(mGraphics2, string, w / 2, n - 88, 2);
        this.drawMenuIcon(n - 11, mGraphics2);
        mGraphics2.setColor(0xFFFFFF);
        super.paint(mGraphics2);
    }

    private void drawMenuIcon(int n, mGraphics mGraphics2) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        int n2 = 0;
        while (true) {
            this.getClass();
            if (n2 >= 11) break;
            if (this.selected[n2] || n2 == playerInfo.gun) {
                mGraphics2.setColor(n2 == playerInfo.gun ? 2003199 : 16762880);
                mGraphics2.fillRoundRect(this._iconX[n2] - 16, n - 16, 32, 40, 10, 10, false);
                mGraphics2.setColor(0xFFFFFF);
                mGraphics2.drawRoundRect(this._iconX[n2] - 16, n - 16, 32, 40, 10, 10, false);
            }
            this.equip[n2].paint(mGraphics2, 0, 0, this._iconX[n2], n);
            if (ChangePlayerCSr.isUnlock[n2] == 0) {
                mGraphics2.drawImage(ChangePlayerCSr.lockImg, this._iconX[n2], n + 5, mGraphics.TOP | mGraphics.HCENTER, false);
            } else if (n2 == playerInfo.gun) {
                Font.borderFont.drawString(mGraphics2, "Ch\u00ednh", this._iconX[n2], n + 30, 2);
            } else if (this.selected[n2]) {
                Font.borderFont.drawString(mGraphics2, "\u0110\u00e3 ch\u1ecdn", this._iconX[n2], n + 30, 2);
            }
            ++n2;
        }
    }
}

