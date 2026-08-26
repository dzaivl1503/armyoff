/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import Equipment.Equip;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import model.MaterialIconMn;
import model.PlayerInfo;
import model.Position;
import network.Command;
import network.GameService;
import player.CPlayer;
import screen.CScreen;
import screen.GameScr;
import screen.LevelScreen;
import screen.TabScreen;

public class EquipScreen
extends TabScreen {
    int num;
    int select;
    int[] xE;
    int[] yE;
    byte[] typeE;
    int[] dbKeyChange = new int[5];
    boolean isSelect;
    public static Vector inventory = new Vector();
    public Vector myEquips = new Vector();
    public static mImage imgIcon;
    public static mImage imgMaterial;
    public static mImage[] imgIconEQ;
    public int wTab;
    public int wIndex;
    public int hIndex;
    public int wP;
    public Command cmdSelect;
    public Command menu;
    public static boolean isEquip;
    int dem;
    boolean isCompine;
    short mSelect;
    short mComSelect;
    public short[] lastDb = new short[5];
    public Vector vLastE = new Vector();
    public Equip[] lastEquip = new Equip[5];
    int W = CCanvas.width;
    static int cmtoYI;
    static int cmyI;
    static int cmdyI;
    static int cmvyI;
    int nLine;
    int ind;
    public byte[] addPoint = new byte[5];
    public int[] atts = new int[5];
    public Equip equipSelect;
    int dx = -1;
    String attribute = "";
    String name = "";
    int xName;
    int wName;
    int wDetail;
    boolean scroll;
    int ds;
    Position transText1 = new Position(0, 1);
    Position transText2 = new Position(0, 1);
    int cc;
    int ee;
    public Equip[] currEq = new Equip[5];
    PlayerEquip equip = null;
    int pa = 0;
    boolean trans = false;
    int speed = 1;
    int cmtoYITem;

    public EquipScreen() {
        this.nameCScreen = "EquipScreen screen!";
        this.xPaint = CCanvas.width / 2 - 75;
        this.yPaint = (CCanvas.hieght - CScreen.cmdH) / 2 - 85;
        this.wTabScreen = 150;
        this.hTabScreen = 170;
        this.xE = new int[]{this.W / 2 - 52, this.W / 2 - 77, this.W / 2 - 27, this.W / 2 - 37, this.W / 2 - 67};
        this.yE = new int[]{this.yPaint + 33, this.yPaint + 55, this.yPaint + 55, this.yPaint + 83, this.yPaint + 83};
        this.typeE = new byte[]{0, 1, 2, 3, 4};
        this.left = this.menu = new Command("Menu", new IAction(){

            public void perform() {
                Command command = new Command(Language.detail(), new IAction(){

                    public void perform() {
                        EquipScreen.this.doDetail();
                    }
                });
                Command command2 = new Command(Language.ruongdo(), new IAction(){

                    public void perform() {
                        if (inventory.size() == 0) {
                            CCanvas.startOKDlg(Language.beNotInventory());
                        } else {
                            EquipScreen.this.doInventory();
                        }
                    }
                });
                String string = !EquipScreen.this.isCompine ? Language.kethop() : Language.trangbi();
                new Command(string, new IAction(){

                    public void perform() {
                        boolean bl = EquipScreen.this.isCompine = !EquipScreen.this.isCompine;
                        if (!EquipScreen.this.isCompine) {
                            for (int i = 0; i < EquipScreen.this.myEquips.size(); ++i) {
                                ((Equip)EquipScreen.this.myEquips.elementAt((int)i)).isSelect = false;
                            }
                        }
                    }
                });
                Command command3 = new Command("L\u01b0u trang b\u1ecb", new IAction(){

                    public void perform() {
                        EquipScreen.this.doAgree();
                    }
                });
                Command command4 = new Command("Th\u00e1o t\u1ea5t c\u1ea3 trang b\u1ecb", new IAction(){

                    public void perform() {
                        EquipScreen.this.doUnequipAll();
                    }
                });
                Vector<Command> vector = new Vector<Command>();
                if (EquipScreen.this.myEquips.size() != 0) {
                    vector.addElement(command);
                }
                vector.addElement(command2);
                vector.addElement(command3);
                boolean bl = false;
                for (int i = 0; i < 5; ++i) {
                    if (!EquipScreen.this.isRemovableEquip((byte)i)) continue;
                    bl = true;
                    break;
                }
                if (bl) {
                    vector.addElement(command4);
                }
                CCanvas.menu.startAt(vector, 0);
            }
        });
        this.center = this.cmdSelect = new Command(Language.select(), new IAction(){

            public void perform() {
                EquipScreen.this.doFire();
            }
        });
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                EquipScreen.this.doClose();
            }
        });
        this.title = Language.trangbi();
        this.n = 4;
        this.getW();
        if (CCanvas.isTouch) {
            this.wTab = 30;
            this.wIndex = 2;
            this.wP = 5;
        } else {
            this.wTab = 20;
            this.wIndex = 3;
            this.wP = 0;
        }
    }

    public void init() {
        isEquip = true;
        this.isClose = false;
        this.select = 0;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        PlayerInfo.vipID = playerInfo.equipVipID[playerInfo.gun][1];
        this.getLastEquip();
        TerrainMidlet.myInfo.getMyEquip(9);
        TerrainMidlet.myInfo.setAllEquipEffect();
        for (int i = 0; i < 5; ++i) {
            this.dbKeyChange[i] = -1;
            this.lastDb[i] = -1;
        }
        this.getMyEquip();
        this.getDetail();
        if (inventory.size() == 0) {
        }
        this.vLastE = this.myEquips;
        this.setCurrEquip();
        this.getBaseAttribute();
        this.seeNextAttribute();
    }

    public void show(CScreen cScreen) {
        if (this.select < 0) {
            this.select = 0;
        }
        if (this.select > this.myEquips.size() - 1) {
            this.select = this.myEquips.size() - 1;
        }
        if (this.getEquipSelect() != null) {
            this.getDetail();
        }
        super.show(cScreen);
    }

    public Equip getEquip(int n) {
        for (int i = 0; i < this.myEquips.size(); ++i) {
            Equip equip = (Equip)this.myEquips.elementAt(i);
            if (equip.dbKey != n) continue;
            return equip;
        }
        return null;
    }

    public void removeEquip(int n, int n2) {
        for (int i = 0; i < this.myEquips.size(); ++i) {
            Equip equip = (Equip)this.myEquips.elementAt(i);
            if (!equip.isMaterial) {
                if (equip.dbKey != n) continue;
                equip.num -= n2;
                if (equip.num <= 0) {
                    equip.num = 0;
                    this.myEquips.removeElement(equip);
                    this.currEq[i] = null;
                }
                return;
            }
            if (equip.id != n) continue;
            equip.num -= n2;
            if (equip.num <= 0) {
                equip.num = 0;
                this.myEquips.removeElement(equip);
            }
            return;
        }
    }

    public int countCombine() {
        int n = 0;
        for (int i = 0; i < this.myEquips.size(); ++i) {
            Equip equip = (Equip)this.myEquips.elementAt(i);
            if (!equip.isSelect) continue;
            ++n;
        }
        return n;
    }

    public void getMyEquip() {
        PlayerInfo playerInfo;
        int n;
        this.myEquips.removeAllElements();
        for (n = 0; n < inventory.size(); ++n) {
            Equip equip = (Equip)inventory.elementAt(n);
            if (equip.isMaterial || equip.glass != TerrainMidlet.myInfo.gun) continue;
            OfflineChest.ensureDisplayName(equip);
            this.myEquips.addElement(equip);
        }
        n = -1;
        for (int i = 0; i < inventory.size(); ++i) {
            Equip equip = (Equip)inventory.elementAt(i);
            if (!equip.isMaterial || n == (byte)equip.id) continue;
            n = (byte)equip.id;
            if (equip.materialIcon != null) continue;
            if (MaterialIconMn.isExistIcon(equip.icon)) {
                equip.materialIcon = MaterialIconMn.getImageFromID(equip.icon);
            }
            GameService.gI().getMaterialIcon((byte)0, n, -1);
        }
        this.hIndex = this.myEquips.size() / this.wIndex;
        if (this.myEquips.size() % this.wIndex != 0) {
            ++this.hIndex;
        }
        if ((playerInfo = TerrainMidlet.myInfo) == null || playerInfo.myEquip == null) {
            return;
        }
        for (int i = 0; i < this.myEquips.size(); ++i) {
            Equip equip = (Equip)this.myEquips.elementAt(i);
            if (playerInfo.myEquip.equips[equip.type] == null || playerInfo.myEquip.equips[equip.type].dbKey != equip.dbKey) continue;
            playerInfo.myEquip.equips[equip.type].removeAbility();
            playerInfo.myEquip.equips[equip.type].addAbilityFromEquip(equip);
        }
    }

    public void addEquip(Equip equip, boolean bl) {
        boolean bl2 = true;
        for (int i = 0; i < inventory.size(); ++i) {
            Equip equip2 = (Equip)inventory.elementAt(i);
            if (!equip2.isMaterial || equip2.id != equip.id) continue;
            equip2.num = !bl ? ++equip2.num : (equip2.num += equip.num);
            bl2 = false;
            break;
        }
        if (bl2) {
            if (MaterialIconMn.isExistIcon(equip.icon)) {
                equip.materialIcon = MaterialIconMn.getImageFromID(equip.id);
            } else {
                GameService.gI().getMaterialIcon((byte)0, (byte)equip.id, -1);
            }
            inventory.insertElementAt(equip, 0);
        }
    }

    public void addMaterial(Equip equip) {
        inventory.addElement(equip);
    }

    public void getEquip(Vector vector) {
        inventory = new Vector();
        inventory = vector;
        this.getMyEquip();
    }

    public void getMaterialIcon(int n, byte[] byArray, int n2) {
        Equip equip;
        int n3;
        for (n3 = 0; n3 < inventory.size(); ++n3) {
            equip = (Equip)inventory.elementAt(n3);
            if (!equip.isMaterial || equip.id != n) continue;
            equip.materialIcon = mImage.createImage(byArray, 0, n2, "EquipScrenn" + equip.id, null);
        }
        for (n3 = 0; n3 < this.myEquips.size(); ++n3) {
            equip = (Equip)this.myEquips.elementAt(n3);
            if (!equip.isMaterial || equip.id != n) continue;
            equip.materialIcon = mImage.createImage(byArray, 0, n2, "EquipScrenn" + equip.id, null);
        }
    }

    public void getMaterialIcon(int n, Image image) {
        Equip equip;
        int n2;
        for (n2 = 0; n2 < inventory.size(); ++n2) {
            equip = (Equip)inventory.elementAt(n2);
            if (!equip.isMaterial || equip.id != n) continue;
            equip.materialIcon = new mImage(image);
        }
        for (n2 = 0; n2 < this.myEquips.size(); ++n2) {
            equip = (Equip)this.myEquips.elementAt(n2);
            if (!equip.isMaterial || equip.id != n) continue;
            equip.materialIcon = new mImage(image);
        }
    }

    public void addEquip(Equip equip) {
        inventory.insertElementAt(equip, 0);
        this.myEquips.insertElementAt(equip, 0);
    }

    public void doClose() {
        this.isClose = true;
        CCanvas.endDlg();
        this.resetEquip();
    }

    public void doAgree() {
        CCanvas.startWaitDlg(Language.pleaseWait());
        GameService.gI().changeEquip(this.dbKeyChange);
    }

    private boolean isRemovableEquip(byte by) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        short s = playerInfo.equipID[playerInfo.gun][by];
        if (s <= 0) {
            return false;
        }
        if (by >= 3) {
            return true;
        }
        short s2 = PlayerEquip.getStarterEquipId(playerInfo.gun, by);
        return s2 <= 0 || s != s2;
    }

    public void doUnequipAll() {
        byte by;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        boolean bl = false;
        for (by = 0; by < 5; by = (byte)(by + 1)) {
            if (!this.isRemovableEquip(by)) continue;
            playerInfo.equipID[playerInfo.gun][by] = -1;
            playerInfo.dbKey[by] = -1;
            if (playerInfo.myEquip != null) {
                playerInfo.myEquip.equips[by] = null;
            }
            bl = true;
        }
        if (!bl) {
            return;
        }
        for (by = 0; by < 3; by = (byte)(by + 1)) {
            if (playerInfo.equipID[playerInfo.gun][by] > 0) continue;
            playerInfo.equipID[playerInfo.gun][by] = PlayerEquip.getStarterEquipId(playerInfo.gun, by);
            playerInfo.dbKey[by] = 0;
        }
        playerInfo.getMyEquip(9);
        playerInfo.setAllEquipEffect();
        playerInfo.maxHP = OfflineEquipmentStats.maxHp(playerInfo);
        this.getLastEquip();
        this.setCurrEquip();
        this.getMyEquip();
        this.getBaseAttribute();
        OfflineChest.captureWorn(playerInfo);
        OfflineSave.save();
        CCanvas.startOKDlg("\u0110\u00e3 th\u00e1o t\u1ea5t c\u1ea3 trang b\u1ecb.");
    }

    public void doFire() {
        try {
            int n;
            if (this.myEquips.size() == 0) {
                return;
            }
            if (this.getEquipSelect() == null) {
                return;
            }
            if (this.getEquipSelect().isMaterial) {
                return;
            }
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            Equip equip = this.getEquipSelect();
            if (equip.vip == 1 || PlayerEquip.isMaskItem(playerInfo.gun, equip.id)) {
                equip.vip = 1;
                if (TerrainMidlet.isVip[playerInfo.gun] && equip.id == PlayerInfo.vipID) {
                    TerrainMidlet.isVip[playerInfo.gun] = false;
                    playerInfo.equipVipID[playerInfo.gun][1] = -1;
                    PlayerInfo.vipID = -1;
                    equip.isVip = false;
                } else {
                    TerrainMidlet.isVip[playerInfo.gun] = true;
                    playerInfo.equipVipID[playerInfo.gun][1] = equip.id;
                    PlayerInfo.vipID = equip.id;
                    equip.isVip = true;
                }
                playerInfo.getVipEquip();
                playerInfo.setAllEquipEffect();
                playerInfo.maxHP = OfflineEquipmentStats.maxHp(playerInfo);
                this.getBaseAttribute();
                this.setCurrEquip();
                OfflineChest.captureWorn(playerInfo);
                OfflineSave.save();
                return;
            }
            if (equip.level > playerInfo.level2) {
                CCanvas.startOKDlg(Language.banphaitren() + equip.level + Language.moicothe());
                return;
            }
            for (n = 0; n < 5; ++n) {
                if (playerInfo.myEquip.equips[n] == null || equip.dbKey != playerInfo.myEquip.equips[n].dbKey) continue;
                return;
            }
            for (n = 0; n < this.typeE.length; ++n) {
                if (equip.type != this.typeE[n]) continue;
                this.dbKeyChange[n] = equip.dbKey;
            }
            short s = playerInfo.equipID[playerInfo.gun][equip.type];
            Equip equip2 = PlayerEquip.createEquip(playerInfo.gun, equip.type, s);
            playerInfo.addChangeEquip(equip, equip2);
            this.changeEquip();
            this.setCurrEquip();
            this.getBaseAttribute();
        }
        catch (Exception exception) {
        }
    }

    public void doDetail() {
        CCanvas.startOKDlg(this.attribute);
    }

    public Equip getEquipSelect() {
        if (this.myEquips.size() > 0) {
            if (this.select <= 0) {
                this.select = 0;
            } else if (this.select >= this.myEquips.size()) {
                this.select = this.myEquips.size() - 1;
            }
            Equip equip = (Equip)this.myEquips.elementAt(this.select);
            return equip;
        }
        return null;
    }

    public void resetEquip() {
        try {
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            for (int i = 0; i < this.lastEquip.length; ++i) {
                if (this.lastEquip[i] == null) continue;
                playerInfo.myEquip.equips[i].changeToEquip(this.lastEquip[i]);
            }
            this.myEquips = this.vLastE;
        }
        catch (Exception exception) {
        }
    }

    public void getLastEquip() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        for (int i = 0; i < 5; ++i) {
            if (playerInfo.myEquip.equips[i] != null) {
                this.lastDb[i] = (short)playerInfo.myEquip.equips[i].dbKey;
                playerInfo.equipID[playerInfo.gun][i] = playerInfo.myEquip.equips[i].id;
                this.lastEquip[i] = new Equip();
                this.lastEquip[i].changeToEquip(playerInfo.myEquip.equips[i]);
            } else {
                this.lastDb[i] = -1;
                this.lastEquip[i] = null;
            }
            this.dbKeyChange[i] = this.lastDb[i];
        }
    }

    public void commitOfflineInventory() {
        this.getMyEquip();
    }

    public void changeEquip() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        Equip equip = this.getEquipSelect();
        if (equip != null) {
            if (playerInfo.myEquip.equips[equip.type] == null) {
                playerInfo.myEquip.equips[equip.type] = PlayerEquip.getEquip(equip.glass, equip.type, equip.id);
            }
            playerInfo.myEquip.equips[equip.type].changeToEquip(equip);
            playerInfo.equipID[playerInfo.gun][equip.type] = equip.id;
        }
        playerInfo.myEquip.equips[equip.type].icon = equip.icon;
        playerInfo.myEquip.equips[equip.type].x = equip.x;
        playerInfo.myEquip.equips[equip.type].y = equip.y;
        playerInfo.myEquip.equips[equip.type].dx = equip.dx;
        playerInfo.myEquip.equips[equip.type].dy = equip.dy;
        playerInfo.myEquip.equips[equip.type].w = equip.w;
        playerInfo.myEquip.equips[equip.type].h = equip.h;
        playerInfo.myEquip.equips[equip.type].bullet = equip.bullet;
        playerInfo.myEquip.equips[equip.type].frame = equip.frame;
        playerInfo.myEquip.equips[equip.type].addAbilityFromEquip(equip);
        playerInfo.myEquip.equips[equip.type].dbKey = equip.dbKey;
    }

    public void doInventory() {
        this.isClose = true;
        CCanvas.inventory.show(CCanvas.menuScr);
    }

    public void paintEquip(mGraphics mGraphics2, Image image, int n, int n2) {
        mGraphics2.setColor(4156571);
        mGraphics2.fillRoundRect(n - 1 - 9, n2 - 1 - 9, 20, 20, 4, 4, false);
        mGraphics2.setColor(16774532);
        mGraphics2.fillRect(n - 1 - 8, n2 - 1 - 8, 18, 18, false);
    }

    public void itemCamera() {
        int n;
        if (cmyI != cmtoYI) {
            cmvyI = cmtoYI - cmyI << 2;
            cmyI += (cmdyI += cmvyI) >> 4;
            cmdyI &= 0xF;
        }
        this.nLine = this.num / this.wIndex;
        if (this.num % this.wIndex != 0) {
            ++this.nLine;
        }
        if (cmyI > (n = this.nLine * this.wTab - 60)) {
            cmyI = n;
        }
        if (cmyI < 0) {
            cmyI = 0;
        }
    }

    public void paintItem(mGraphics mGraphics2, int n, int n2) {
        mGraphics2.setColor(4156571);
        mGraphics2.fillRoundRect(n - 5, this.yPaint + 96, 72, 67, 6, 6, false);
        mGraphics2.setClip(n - 1, n2 - 1, 62, 60);
        int n3 = 0;
        int n4 = 0;
        Equip equip = null;
        for (int i = 0; i < this.myEquips.size(); ++i) {
            equip = (Equip)this.myEquips.elementAt(i);
            int n5 = n + n4 * this.wTab + this.wP;
            int n6 = n2 + n3 * this.wTab + this.wP;
            int n7 = n5;
            int n8 = n6 - cmyI;
            int n9 = n7;
            int n10 = n8;
            if (equip != null) {
                PlayerInfo playerInfo = TerrainMidlet.myInfo;
                if (playerInfo.myEquip.equips[equip.type] != null && equip.dbKey == playerInfo.myEquip.equips[equip.type].dbKey) {
                    mGraphics2.setColor(4819660);
                    mGraphics2.fillRect(n7, n8, 16, 16, true);
                }
                if (equip.vip == 1) {
                    mGraphics2.setColor(5361158);
                    mGraphics2.fillRect(n7, n8, 16, 16, true);
                    if (TerrainMidlet.isVip[playerInfo.gun] && equip.id == PlayerInfo.vipID) {
                        mGraphics2.setColor(5963263);
                        mGraphics2.fillRect(n7, n8, 16, 16, true);
                    }
                }
                if (this.select == i) {
                    mGraphics2.setColor(16767817);
                    mGraphics2.fillRect(n7 - 1, n8 - 1, 18, 18, true);
                    if (!CCanvas.isTouch) {
                        cmtoYI = n6 - (n2 + 20);
                    }
                    Equip equip2 = (Equip)this.myEquips.elementAt(this.select);
                    if (equip != null) {
                        for (int j = 0; j < this.typeE.length; ++j) {
                            if (equip2.type != this.typeE[j]) continue;
                            this.ind = j;
                        }
                    }
                }
                if (equip.isSelect) {
                    mGraphics2.setColor(0xFFFFFF);
                    mGraphics2.fillRect(n7, n8, 16, 16, true);
                }
                equip.drawIcon(mGraphics2, n7, n8, true);
                if (!equip.isMaterial) {
                    for (int j = 0; j < equip.slot; ++j) {
                        if (n4 != this.select) {
                            mGraphics2.setColor(16377901);
                            mGraphics2.fillRect(n9 + j * 4, n10, 2, 2, true);
                            continue;
                        }
                        mGraphics2.setColor(0);
                        mGraphics2.fillRect(n9 + j * 4, n10, 2, 2, true);
                    }
                }
            }
            if (++n4 != this.wIndex) continue;
            ++n3;
            n4 = 0;
        }
        mGraphics2.translate(0, -mGraphics2.getTranslateY());
    }

    public void getBaseAttribute() {
        Equip equip;
        int n;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        int[] nArray = new int[5];
        int[] nArray2 = new int[5];
        Equip equip2 = null;
        if (TerrainMidlet.isVip[TerrainMidlet.myInfo.gun]) {
            for (n = 0; n < this.myEquips.size(); ++n) {
                equip = (Equip)this.myEquips.elementAt(n);
                if (equip.id != PlayerInfo.vipID) continue;
                equip2 = equip;
                break;
            }
        }
        for (n = 0; n < 5; ++n) {
            equip = playerInfo.myEquip.equips[n];
            if (equip == null) continue;
            for (int i = 0; i < 5; ++i) {
                int n2 = i;
                nArray[n2] = nArray[n2] + equip.inv_ability[i];
                int n3 = i;
                nArray2[n3] = nArray2[n3] + equip.inv_percen[i];
            }
        }
        if (equip2 != null) {
            for (n = 0; n < 5; ++n) {
                int n4 = n;
                nArray[n4] = nArray[n4] + equip2.inv_ability[n];
                int n5 = n;
                nArray2[n5] = nArray2[n5] + equip2.inv_percen[n];
            }
        }
        this.atts[0] = 1000 + playerInfo.ability[0] * 10 + nArray[0] * 10;
        int[] nArray3 = this.atts;
        nArray3[0] = nArray3[0] + (1000 + playerInfo.ability[0]) * nArray2[0] / 100;
        short s = PlayerEquip.getEquipGlass((byte)playerInfo.gun).maxDamage;
        int n6 = nArray[1] + playerInfo.ability[1];
        n = nArray[2] + playerInfo.ability[2];
        int n7 = nArray[3] + playerInfo.ability[3];
        int n8 = nArray[4] + playerInfo.ability[4];
        this.atts[1] = s * (n6 / 3 + 100 + nArray2[1]) / 100;
        this.atts[2] = n * 10;
        nArray3 = this.atts;
        nArray3[2] = nArray3[2] + this.atts[2] * nArray2[2] / 100;
        this.atts[3] = n7 * 10;
        nArray3 = this.atts;
        nArray3[3] = nArray3[3] + this.atts[3] * nArray2[3] / 100;
        this.atts[4] = n8 * 10;
        nArray3 = this.atts;
        nArray3[4] = nArray3[4] + this.atts[4] * nArray2[4] / 100;
        this.atts[0] = OfflineTeamItems.applyStat(0, this.atts[0]);
        this.atts[1] = OfflineTeamItems.applyStat(1, this.atts[1]);
        this.atts[2] = OfflineTeamItems.applyStat(2, this.atts[2]);
        this.atts[3] = OfflineTeamItems.applyStat(3, this.atts[3]);
    }

    public void paintAbility(mGraphics mGraphics2) {
        Font.normalFont.drawString(mGraphics2, "Level: " + TerrainMidlet.myInfo.level2, this.W / 2 + 24, this.yPaint + 22, 3);
        Font.normalFont.drawString(mGraphics2, "%", this.W / 2 + 75, this.yPaint + 22, 3);
        for (int i = 0; i < 5; ++i) {
            mGraphics2.drawRegion(LevelScreen.ability, 0, i * 16, 16, 16, 0, this.W / 2 - 1, this.yPaint + 46 + i * 18, 3, false);
            mGraphics2.setColor(2378093);
            mGraphics2.fillRect(CCanvas.width / 2 + 9, this.yPaint + 38 + i * 18, 35, 16, false);
            mGraphics2.fillRect(CCanvas.width / 2 + 46, this.yPaint + 38 + i * 18, 18, 16, false);
            mGraphics2.fillRect(CCanvas.width / 2 + 66, this.yPaint + 38 + i * 18, 19, 16, false);
            PlayerInfo playerInfo = TerrainMidlet.myInfo;
            String string = String.valueOf(Math.abs(playerInfo.attAddPoint1[i]));
            String string2 = String.valueOf(Math.abs(playerInfo.attAddPoint2[i]));
            int n = this.atts[i];
            Font.normalYFont.drawString(mGraphics2, String.valueOf(n), this.W / 2 + 26, this.yPaint + 39 + i * 18, 3);
            byte by = playerInfo.UpOrDown1[i];
            playerInfo.getClass();
            if (by == 0) {
                Font.normalYFont.drawString(mGraphics2, string, this.W / 2 + 56, this.yPaint + 39 + i * 18, 3);
            }
            by = playerInfo.UpOrDown1[i];
            playerInfo.getClass();
            if (by == 2) {
                Font.normalRFont.drawString(mGraphics2, string, this.W / 2 + 56, this.yPaint + 39 + i * 18, 3);
            }
            by = playerInfo.UpOrDown1[i];
            playerInfo.getClass();
            if (by == 1) {
                Font.normalGFont.drawString(mGraphics2, string, this.W / 2 + 56, this.yPaint + 39 + i * 18, 3);
            }
            by = playerInfo.UpOrDown2[i];
            playerInfo.getClass();
            if (by == 0) {
                Font.normalYFont.drawString(mGraphics2, string2, this.W / 2 + 75, this.yPaint + 39 + i * 18, 3);
            }
            by = playerInfo.UpOrDown2[i];
            playerInfo.getClass();
            if (by == 2) {
                Font.normalRFont.drawString(mGraphics2, string2, this.W / 2 + 75, this.yPaint + 39 + i * 18, 3);
            }
            by = playerInfo.UpOrDown2[i];
            playerInfo.getClass();
            if (by != 1) continue;
            Font.normalGFont.drawString(mGraphics2, string2, this.W / 2 + 75, this.yPaint + 39 + i * 18, 3);
        }
    }

    public void getDetail() {
        this.dx = -1;
        this.ds = 0;
        this.scroll = false;
        this.attribute = "";
        Equip equip = this.getEquipSelect();
        if (equip != null) {
            this.xName = this.W / 2 - 4;
            this.name = equip.name != null ? equip.name : "";
            this.wName = Font.normalFont.getWidth(this.name);
            this.attribute = equip.isMaterial ? equip.strDetail : equip.getStrInvDetail();
            this.wDetail = Font.normalFont.getWidth(this.name);
        }
    }

    public Position transTextLimit(Position position, int n) {
        position.x += position.y;
        if (position.y == -1 && Math.abs(position.x) > n) {
            position.y *= -1;
        }
        if (position.y == 1 && position.x > 5) {
            position.y *= -1;
        }
        return position;
    }

    public void paintMoney(mGraphics mGraphics2) {
        mGraphics2.setColor(1521982);
        mGraphics2.setClip(this.W / 2 - 9, this.yPaint + 40 + 90, 95, 60);
        mGraphics2.fillRoundRect(this.W / 2 - 9, this.yPaint + 40 + 90, 95, 16, 6, 6, false);
        mGraphics2.fillRoundRect(this.W / 2 - 9, this.yPaint + 58 + 90, 95, 16, 6, 6, false);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        String string = CRes.getMoneys(playerInfo.xu) + Language.xu() + "-" + playerInfo.luong + Language.luong2();
        int n = this.yPaint + 41 + 90;
        Font.normalYFont.drawString(mGraphics2, string, this.xName + this.cc, n, 0);
        int n2 = this.W / 2 - 4;
        int n3 = this.yPaint + 59 + 90;
        Font.normalGFont.drawString(mGraphics2, this.name, n2 + this.dx + this.ee, n3, 0);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
    }

    public void setCurrEquip() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        for (int i = 0; i < 5; ++i) {
            short s = playerInfo.equipID[playerInfo.gun][i];
            if (s > 0) {
                this.currEq[i] = PlayerEquip.getEquip(playerInfo.gun, (byte)i, s);
            } else {
                this.currEq[i] = null;
            }
        }
    }

    public void paintPlayer(mGraphics mGraphics2) {
        mGraphics2.setColor(16767817);
        mGraphics2.drawRect(this.xE[this.ind] - 9, this.yE[this.ind] - 9, 17, 17, false);
        mGraphics2.drawRect(this.xE[this.ind] - 10, this.yE[this.ind] - 10, 19, 19, false);
        mGraphics2.setColor(1521982);
        mGraphics2.drawRect(this.xE[this.ind] - 11, this.yE[this.ind] - 11, 21, 21, false);
        for (int i = 0; i < 5; ++i) {
            this.paintEquip(mGraphics2, GameScr.s_imgITEM.image, this.xE[i], this.yE[i]);
            if (this.currEq[i] != null) {
                this.currEq[i].drawIcon(mGraphics2, this.xE[i] - 8, this.yE[i] - 8, true);
                continue;
            }
            mGraphics2.drawRegion(GameScr.s_imgITEM, 0, 0, 16, 16, 0, this.xE[i], this.yE[i], 3, true);
        }
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        byte by = playerInfo.gun;
        this.equip = TerrainMidlet.isVip[playerInfo.gun] ? playerInfo.myVipEquip : playerInfo.myEquip;
        CPlayer.paintSimplePlayer(by, CCanvas.gameTick % 5 > 2 ? 5 : 4, CCanvas.width / 2 - 52, this.yPaint + 71, 0, this.equip, mGraphics2);
    }

    public void paint(mGraphics mGraphics2) {
        super.paint(mGraphics2);
        this.paintPlayer(mGraphics2);
        this.paintItem(mGraphics2, this.W / 2 - 78, this.yPaint + 102);
        this.paintMoney(mGraphics2);
        this.paintAbility(mGraphics2);
        this.paintSuper(mGraphics2);
    }

    public void seeNextAttribute() {
        if (this.myEquips.size() != 0) {
            try {
                PlayerInfo playerInfo = TerrainMidlet.myInfo;
                Equip equip = this.getEquipSelect();
                if (equip == null) {
                    return;
                }
                Equip equip2 = null;
                PlayerEquip.getEquip(playerInfo.gun, equip.type, playerInfo.equipID[playerInfo.gun][equip.type]);
                equip2 = playerInfo.myEquip.equips[equip.type];
                if (equip.isMaterial || this.isCompine || equip.vip == 1) {
                    equip2 = equip;
                }
                playerInfo.compareEquip(equip, equip2);
                this.getDetail();
                this.transText2.x = -1;
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    public void update() {
        super.update();
        this.num = this.myEquips.size();
        this.center = this.cmdSelect;
        this.left = this.menu;
        this.itemCamera();
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        PlayerInfo.vipID = playerInfo.equipVipID[playerInfo.gun][1];
        String string = CRes.getMoneys(TerrainMidlet.myInfo.xu) + Language.xu() + "-" + TerrainMidlet.myInfo.luong + Language.luong2();
        int n = Font.normalFont.getWidth(string);
        if (n > 85) {
            this.transTextLimit(this.transText1, n - 80);
        }
        this.cc = this.transText1.x;
        int n2 = Font.normalFont.getWidth(this.name);
        if (n2 > 85) {
            this.transTextLimit(this.transText2, n2 - 80);
        }
        this.ee = this.transText2.x;
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!this.trans) {
            this.pa = cmyI;
            this.trans = true;
        }
        if (CCanvas.isPc()) {
            this.speed = 3;
        }
        cmtoYI = this.pa + (CCanvas.pyFirst[n3] - n2) * this.speed;
        this.cmtoYITem = this.pa + (CCanvas.pyFirst[n3] - n2);
        if (cmtoYI <= 0) {
            cmtoYI = 0;
            this.cmtoYITem = 0;
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.trans = false;
        int n4 = this.W / 2 - 86;
        int n5 = this.yPaint + 96;
        if (CCanvas.isPointer(n4, n5 + this.wP, 72, 87, n3)) {
            int n6 = (this.cmtoYITem + n2 - n5 - this.wP) / this.wTab * this.wIndex + (n - n4 - this.wP) / this.wTab;
            if (n6 == this.select && this.center != null && CCanvas.isDoubleClick) {
                this.center.action.perform();
            }
            this.select = n6;
            this.getDetail();
            if (this.select < 0) {
                this.select = 0;
            }
            if (this.select > this.myEquips.size() - 1) {
                this.select = this.myEquips.size() - 1;
            }
        }
        this.seeNextAttribute();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        int n4 = this.W / 2 - 78;
        int n5 = this.yPaint + 102;
        if (CCanvas.isPointer(n4, n5, 72, 87, n3)) {
            int n6;
            this.select = n6 = (this.cmtoYITem + n2 - n5) / this.wTab * this.wIndex + (n - n4 - this.wP) / this.wTab;
            this.getDetail();
            if (this.select < 0) {
                this.select = 0;
            }
            if (this.select > this.myEquips.size() - 1) {
                this.select = this.myEquips.size() - 1;
            }
        }
        if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
            if (CCanvas.keyPressed[2]) {
                this.select -= this.wIndex;
            }
            if (CCanvas.keyPressed[8]) {
                this.select += this.wIndex;
            }
            if (CCanvas.keyPressed[4]) {
                --this.select;
            }
            if (CCanvas.keyPressed[6]) {
                ++this.select;
            }
            if (this.select > this.myEquips.size() - 1) {
                this.select = 0;
            }
            if (this.select < 0) {
                this.select = this.myEquips.size() - 1;
            }
            if ((cmtoYI = this.select / this.wIndex * 40 - 20) < 0) {
                cmtoYI = 0;
            }
            this.getDetail();
            this.seeNextAttribute();
            CScreen.clearKey();
        }
    }

    static {
        imgIconEQ = new mImage[6];
        isEquip = false;
    }
}

