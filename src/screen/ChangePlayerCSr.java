/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import Equipment.Equip;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineGunAngles;
import com.teamobi.mobiarmy2.OfflineSave;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import item.Bullet;
import java.util.Vector;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import network.Command;
import network.GameService;
import player.PM;
import screen.CScreen;
import screen.EquipScreen;
import screen.GameScr;
import screen.MenuScr;
import screen.PrepareScr;

public class ChangePlayerCSr
extends CScreen {
    public CScreen lastScr;
    public static final String[] GunDecription = new String[]{Language.bulletNumber(), Language.damage(), Language.windEffect()};
    private static final byte[] OFFLINE_BULLET = new byte[]{1, 2, 3, 4, 3, 5, 2, 1, 4, 1, 1};
    private static final byte[] OFFLINE_DAMAGE = new byte[]{28, 14, 10, 10, 12, 7, 23, 30, 11, 35, 32};
    private static final byte[] OFFLINE_WIND = new byte[]{80, 50, 80, 40, 50, 30, 20, 10, 30, 40, 0};
    private static final int[] OFFLINE_XU = new int[]{0, 0, 0, 40000, 30000, 20000, 100000, 50000, 70000, 120000, 100000};
    private static final int[] OFFLINE_LUONG = new int[]{0, 0, 0, 32, 24, 16, 80, 40, 64, 96, 500};
    public static final int[][] GunInfo = new int[11][];
    public static mImage lockImg = GameScr.lockImg;
    public static int curMenu = 2;
    private int blankW;
    private int[] _iconX;
    private int _centerIX;
    private int nMainIcon;
    public static int gunPassiveIndexSub = 3;
    public static final byte IS_UNLOCK = 1;
    public static final byte IS_LOCK = 0;
    public static byte[] isUnlock = new byte[11];
    public static int[] gunXu = new int[11];
    public static int[] gunLuong = new int[11];
    public static byte[] power;
    public static byte[] number;
    PlayerEquip[] equip;
    boolean isShowInfo;

    public static void ensureGunData() {
        if (number == null || power == null || Bullet.BULLset_WIND_AFFECT == null) {
            ChangePlayerCSr.initOfflineGunDefaults();
        } else {
            ChangePlayerCSr.applyOfflineGunCatalog();
        }
    }

    public static void initOfflineGunDefaults() {
        int n = 11;
        number = new byte[n];
        power = new byte[n];
        Bullet.BULLset_WIND_AFFECT = new byte[n];
        for (int i = 0; i < n; ++i) {
            ChangePlayerCSr.isUnlock[i] = i < 3 ? (byte)1 : 0;
        }
        ChangePlayerCSr.applyOfflineGunCatalog();
        PM.MAX_PLAYER = 8;
        PM.NUMB_PLAYER = 8;
    }

    private static void applyOfflineGunCatalog() {
        for (int i = 0; i < OFFLINE_BULLET.length; ++i) {
            ChangePlayerCSr.number[i] = OFFLINE_BULLET[i];
            ChangePlayerCSr.power[i] = OFFLINE_DAMAGE[i];
            Bullet.BULLset_WIND_AFFECT[i] = OFFLINE_WIND[i];
            ChangePlayerCSr.gunXu[i] = OFFLINE_XU[i];
            ChangePlayerCSr.gunLuong[i] = OFFLINE_LUONG[i];
        }
        OfflineGunAngles.applyServerAngleLocks();
        PM.MAX_PLAYER = 8;
        PM.NUMB_PLAYER = 8;
    }

    public static void changeGunOffline(byte by) {
        if (by < 0 || by >= isUnlock.length || isUnlock[by] == 0) {
            CCanvas.endDlg();
            return;
        }
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (by == playerInfo.gun) {
            CCanvas.endDlg();
            CCanvas.startOKDlg("\u0110\u00e2y \u0111\u00e3 l\u00e0 nh\u00e2n v\u1eadt \u0111ang d\u00f9ng.");
            return;
        }
        byte by2 = playerInfo.gun;
        for (int i = 0; i < playerInfo.squadExtra.length; ++i) {
            if (playerInfo.squadExtra[i] != by) continue;
            playerInfo.squadExtra[i] = by2;
            break;
        }
        TerrainMidlet.myInfo.switchGunProgress(by);
        TerrainMidlet.myInfo.ensureCombatEquip();
        TerrainMidlet.myInfo.setAllEquipEffect();
        TerrainMidlet.myInfo.getQuanHam();
        CCanvas.endDlg();
        if (CCanvas.changePScr != null) {
            CCanvas.changePScr.onChangeGun();
        }
        OfflineSave.save();
    }

    public static void buyGunOffline(byte by, byte by2) {
        int n = by + gunPassiveIndexSub;
        if (n < 0 || n >= isUnlock.length) {
            return;
        }
        if (isUnlock[n] == 1) {
            CCanvas.endDlg();
            return;
        }
        if (by2 == 0) {
            if (TerrainMidlet.myInfo.xu < gunXu[n]) {
                CCanvas.startOKDlg(Language.noMoney());
                return;
            }
            TerrainMidlet.myInfo.xu -= gunXu[n];
        } else {
            if (TerrainMidlet.myInfo.luong < gunLuong[n]) {
                CCanvas.startOKDlg(Language.noMoney());
                return;
            }
            TerrainMidlet.myInfo.luong -= gunLuong[n];
        }
        ChangePlayerCSr.isUnlock[n] = 1;
        CCanvas.endDlg();
        if (CCanvas.changePScr != null) {
            CCanvas.changePScr.doChangePlayer();
        }
        OfflineSave.save();
    }

    public ChangePlayerCSr() {
        ChangePlayerCSr.ensureGunData();
        this.blankW = w / 2 - 50;
        this.nMainIcon = 11;
        this.equip = new PlayerEquip[11];
        for (int i = 0; i < GunInfo.length; ++i) {
            ChangePlayerCSr.GunInfo[i] = new int[]{number[i], power[i], Bullet.BULLset_WIND_AFFECT[i]};
        }
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                ChangePlayerCSr.this.doClose();
            }
        });
        int n = (w >> 1) - this.nMainIcon * (24 + this.blankW) / 2;
        this._iconX = new int[this.nMainIcon];
        for (int i = 0; i < this.nMainIcon; ++i) {
            this._iconX[i] = n + i * this.blankW;
        }
        this._centerIX = w >> 1;
        this.nameCScreen = "ChangePlayerCSr screen!";
    }

    protected void doClose() {
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

    public void doChangePlayer() {
        if (isUnlock[curMenu] == 0) {
            final Command command = new Command(Language.muaXu(), new IAction(){

                public void perform() {
                    CCanvas.startYesNoDlg(Language.buyCharactor() + TerrainMidlet.myInfo.xu + Language.xu(), new IAction(){

                        public void perform() {
                            if (TerrainMidlet.myInfo.xu >= gunXu[curMenu]) {
                                GameService.gI().buyGun((byte)(curMenu - gunPassiveIndexSub), (byte)0);
                            } else {
                                CCanvas.startOKDlg(Language.noMoney());
                            }
                        }
                    });
                }
            });
            final Command command2 = new Command(Language.muaLuong(), new IAction(){

                public void perform() {
                    CCanvas.startYesNoDlg(Language.buyCharactor() + TerrainMidlet.myInfo.luong + Language.luong(), new IAction(){

                        public void perform() {
                            if (TerrainMidlet.myInfo.luong >= gunLuong[curMenu]) {
                                GameService.gI().buyGun((byte)(curMenu - gunPassiveIndexSub), (byte)1);
                            } else {
                                CCanvas.startOKDlg(Language.noMoney());
                            }
                        }
                    });
                }
            });
            Command command3 = new Command(Language.select(), new IAction(){

                public void perform() {
                    Vector<Command> vector = new Vector<Command>();
                    vector.addElement(command);
                    vector.addElement(command2);
                    CCanvas.menu.startAt(vector, 2);
                }
            });
            this.center = gunLuong[curMenu] != -1 ? command3 : command;
        } else {
            this.center = new Command(Language.select(), new IAction(){

                public void perform() {
                    GameService.gI().changeGun((byte)curMenu);
                }
            });
        }
    }

    public void getCurrEquip() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        PlayerEquip.applyDefaultOfflineEquipIds(playerInfo);
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

    public void show(CScreen cScreen) {
        this.lastScr = cScreen;
        if (TerrainMidlet.myInfo != null) {
            TerrainMidlet.myInfo.saveCurrentClassProgress();
            curMenu = TerrainMidlet.myInfo.gun;
        }
        CCanvas.arrPopups.removeAllElements();
        CCanvas.msgPopup.nMessage = 0;
        this.getCurrEquip();
        this.doChangePlayer();
        super.show();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        int n4;
        int n5;
        super.onPointerPressed(n, n2, n3);
        if (CCanvas.keyPressed[4] || keyLeft || CCanvas.keyPressed[2]) {
            curMenu = this.getLastP(curMenu, 1);
            n5 = this.getLastP(curMenu, 1);
            n4 = this.getLastP(n5, 1);
            this._iconX[n5] = this._iconX[curMenu] - this.blankW;
            this._iconX[n4] = this._iconX[curMenu] - this.blankW * 2;
            this.doChangePlayer();
        }
        if (CCanvas.keyPressed[6] || keyRight || CCanvas.keyPressed[8]) {
            curMenu = this.getNextP(curMenu, 1);
            n5 = this.getNextP(curMenu, 1);
            n4 = this.getNextP(n5, 1);
            this._iconX[n5] = this._iconX[curMenu] + this.blankW;
            this._iconX[n4] = this._iconX[curMenu] + this.blankW * 2;
            this.doChangePlayer();
        }
        if (CCanvas.isPointerClick[n3]) {
            if (CCanvas.isPointer(0, 0, CCanvas.width / 2 - 30, CCanvas.hieght - cmdH, n3)) {
                curMenu = this.getLastP(curMenu, 1);
                n5 = this.getLastP(curMenu, 1);
                n4 = this.getLastP(n5, 1);
                this._iconX[n5] = this._iconX[curMenu] - this.blankW;
                this._iconX[n4] = this._iconX[curMenu] - this.blankW * 2;
                this.doChangePlayer();
            }
            if (CCanvas.isPointer(CCanvas.width / 2 + 30, 0, CCanvas.width / 2 - 30, CCanvas.hieght - cmdH, n3)) {
                curMenu = this.getNextP(curMenu, 1);
                n5 = this.getNextP(curMenu, 1);
                n4 = this.getNextP(n5, 1);
                this._iconX[n5] = this._iconX[curMenu] + this.blankW;
                this._iconX[n4] = this._iconX[curMenu] + this.blankW * 2;
                this.doChangePlayer();
            }
            if (CCanvas.isPointer(CCanvas.width / 2 - 30, 0, 60, CCanvas.hieght - cmdH, n3)) {
                this.center.action.perform();
            }
        }
        ChangePlayerCSr.clearKey();
    }

    public void keyPressed(int n) {
        super.keyPressed(n);
    }

    public void input() {
    }

    public void update() {
        Cloud.updateCloud();
        this.moveMenu();
    }

    public void moveMenu() {
        this.isShowInfo = false;
        int n = Math.max(Math.abs(this._centerIX - this._iconX[curMenu] >> 1), 1);
        if (this._iconX[curMenu] < this._centerIX) {
            int n2 = 0;
            while (n2 < this.nMainIcon) {
                int[] nArray = this._iconX;
                int n3 = n2++;
                nArray[n3] = nArray[n3] + n;
            }
        } else if (this._iconX[curMenu] > this._centerIX) {
            int n4 = 0;
            while (n4 < this.nMainIcon) {
                int[] nArray = this._iconX;
                int n5 = n4++;
                nArray[n5] = nArray[n5] - n;
            }
        } else {
            this.isShowInfo = true;
        }
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        int n2;
        ChangePlayerCSr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        int n3 = h >> 1;
        Font.bigFont.drawString(mGraphics2, PrepareScr.GUN_NAME[curMenu], w / 2, n3 - 62, 2);
        if (TerrainMidlet.myInfo != null) {
            n2 = TerrainMidlet.myInfo.getClassLevel(curMenu);
            n = TerrainMidlet.myInfo.getClassExp(curMenu);
            int n4 = TerrainMidlet.myInfo.getClassNextExp(curMenu);
            int n5 = n4 > 0 ? n * 100 / n4 : 0;
            String string = "LV " + n2 + "  " + n5 + "%";
            int n6 = Font.borderFont.getWidth(string) + 14;
            int n7 = (w - n6) / 2;
            int n8 = n3 - 88;
            mGraphics2.setColor(0x252525);
            mGraphics2.fillRoundRect(n7, n8, n6, 17, 8, 8, false);
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.drawRoundRect(n7, n8, n6, 17, 8, 8, false);
            Font.normalYFont.drawString(mGraphics2, string, w / 2, n8 + 2, 2);
        }
        this.drawMenuIcon(n3 - 11, mGraphics2);
        if (this.isShowInfo) {
            n2 = n3 - 4;
            n = w / 2 - 60;
            Font.borderFont.drawString(mGraphics2, GunDecription[0], n, n2 + 20, 0);
            Font.borderFont.drawString(mGraphics2, String.valueOf(GunInfo[curMenu][0]), n + 76, n2 + 20, 0);
            Font.borderFont.drawString(mGraphics2, GunDecription[1], n, n2 + 34, 0);
            Font.borderFont.drawString(mGraphics2, GunDecription[2], n, n2 + 48, 0);
            mGraphics2.setColor(0);
            mGraphics2.fillRect(n + 70, n2 + 38, 50, 10, false);
            mGraphics2.fillRect(n + 70, n2 + 52, 50, 10, false);
            mGraphics2.setColor(0x4A4A4A);
            mGraphics2.fillRect(n + 72, n2 + 40, 46, 6, false);
            mGraphics2.fillRect(n + 72, n2 + 54, 46, 6, false);
            mGraphics2.setColor(16741888);
            mGraphics2.fillRect(n + 72, n2 + 40, GunInfo[curMenu][1] * 46 / 35, 6, false);
            mGraphics2.fillRect(n + 72, n2 + 54, GunInfo[curMenu][2] * 46 / 100, 6, false);
            Font.borderFont.drawString(mGraphics2, String.valueOf(GunInfo[curMenu][1]), n + 124, n2 + 34, 0);
            Font.borderFont.drawString(mGraphics2, GunInfo[curMenu][2] + "%", n + 124, n2 + 48, 0);
        }
        mGraphics2.setColor(0xFFFFFF);
        super.paint(mGraphics2);
    }

    public static void painRoundR(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        mGraphics2.setColor(0x7AAFFF);
        mGraphics2.fillRoundRect(n, n2, n3, n4, 10, 10, false);
        mGraphics2.setColor(0xFFFFFF);
        mGraphics2.drawRoundRect(n, n2, n3, n4, 10, 10, false);
    }

    private void drawMenuIcon(int n, mGraphics mGraphics2) {
        for (int i = 0; i < this.nMainIcon; ++i) {
            this.equip[i].paint(mGraphics2, 0, 0, this._iconX[i], n);
            if (isUnlock[i] != 0) continue;
            mGraphics2.drawImage(lockImg, this._iconX[i], n + 5, mGraphics.TOP | mGraphics.HCENTER, false);
            if (curMenu != i) continue;
            String string = gunXu[curMenu] + Language.xu();
            String string2 = gunLuong[curMenu] + Language.luong();
            if (gunLuong[curMenu] == -1) {
                string2 = "";
            }
            Font.borderFont.drawString(mGraphics2, string + "-" + string2, this._iconX[i], n + 30, 2);
            this.isShowInfo = false;
        }
    }

    public void changeEquipAttribute() {
        Vector vector = EquipScreen.inventory;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        for (int i = 0; i < playerInfo.myEquip.equips.length; ++i) {
            Equip equip = playerInfo.myEquip.equips[i];
            if (equip == null) continue;
            for (int j = 0; j < vector.size(); ++j) {
                Equip equip2 = (Equip)vector.elementAt(j);
                if (equip.id != playerInfo.myEquip.id) continue;
                playerInfo.addCurrEquip(equip2);
                CRes.out("TOI DAY TOI DAY TOI DAY");
            }
        }
    }

    public void onChangeGun() {
        TerrainMidlet.myInfo.ensureCombatEquip();
        TerrainMidlet.myInfo.setAllEquipEffect();
        if (CCanvas.equipScreen != null) {
            CCanvas.equipScreen.getMyEquip();
            CCanvas.equipScreen.getLastEquip();
        }
        this.doClose();
    }

    public int getNextP(int n, int n2) {
        return n + n2 > this.nMainIcon - n2 ? 0 : n + n2;
    }

    public int getLastP(int n, int n2) {
        return n - n2 < 0 ? this.nMainIcon - n2 : n - n2;
    }
}

