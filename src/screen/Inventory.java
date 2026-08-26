/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import Equipment.Equip;
import com.teamobi.mobiarmy2.OfflineSpecialShop;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import model.Position;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.EquipScreen;
import screen.PrepareScr;
import screen.TabScreen;

public class Inventory
extends TabScreen {
    int wTab;
    static int cmtoYI;
    static int cmyI;
    static int cmdyI;
    static int cmvyI;
    static int cmyILim;
    int size = 0;
    int nLine = 8;
    private int wXp;
    private int wYp;
    private Vector equips = new Vector();
    Command cmdXacnhan;
    Command menu;
    int dem;
    boolean isCombine = false;
    Position transText1 = new Position(0, 1);
    int combineSelect;
    public int select2;
    Equip eSelect;
    String equipDetail;
    String equipName;
    String date;
    int numCombine = 1;
    boolean isCombineNum;
    int hLine;
    int pa = 0;
    boolean trans = false;
    Command cmdCombine;
    private int gridRows;
    private int gridViewH;

    public Inventory() {
        this.xPaint = CCanvas.width / 2 - 85;
        this.yPaint = (CCanvas.hieght - CScreen.cmdH) / 2 - 85;
        this.hTabScreen = 180;
        this.title = Language.ruongdo();
        this.getW();
        if (CCanvas.isTouch) {
            this.nLine = 4;
            this.wXp = this.wBlank / 4;
            this.wYp = 5;
            this.wTab = 40;
            this.gridRows = 2;
        } else {
            this.nLine = 8;
            this.wXp = 0;
            this.wYp = 0;
            this.wTab = 20;
            this.gridRows = 5;
        }
        this.gridViewH = this.gridRows * this.wTab;
        this.cmdCombine = new Command(Language.select(), new IAction(){

            public void perform() {
                if (!Inventory.this.isCombineNum) {
                    Inventory.this.doCombineSelect();
                } else {
                    Inventory.this.isCombineNum = false;
                }
            }
        });
        this.nameCScreen = "Inventory screen!";
    }

    public void show(CScreen cScreen) {
        super.show(cScreen);
        this.init();
    }

    public void init() {
        this.select2 = 0;
        this.menuScroll = false;
        if (this.size == 0) {
            cmtoYI = 0;
        }
        this.size = EquipScreen.inventory.size();
        this.hLine = this.size / this.nLine;
        if (this.size % this.nLine != 0) {
            ++this.hLine;
        }
        this.select = 0;
        this.getCommand();
        if (this.size != 0) {
            this.getDetail();
        }
    }

    public void getCommand() {
        this.center = new Command(Language.select(), new IAction(){

            public void perform() {
            }
        });
        this.center = this.cmdCombine;
        new Command(Language.xacnhan(), new IAction(){

            public void perform() {
                Inventory.this.doCombine();
            }
        });
        this.menu = new Command("Menu", new IAction(){

            public void perform() {
                Vector<Command> vector = new Vector<Command>();
                vector.addElement(Inventory.this.cmdXacnhan);
                if (!Inventory.this.getEquipSelect().isMaterial && Inventory.this.getEquipSelect().socketCount() > 0) {
                    vector.addElement(new Command("Th\u00e1o ng\u1ecdc", new IAction(){

                        public void perform() {
                            OfflineSpecialShop.requestRemoveGems(Inventory.this.getEquipSelect());
                        }
                    }));
                }
                if (Inventory.this.getEquipSelect().isMaterial && Inventory.this.getEquipSelect().id >= 0 && Inventory.this.getEquipSelect().id < 50 && Inventory.this.getEquipSelect().id % 10 != 9 && Inventory.this.getEquipSelect().num >= 5) {
                    vector.addElement(new Command("Gh\u00e9p t\u1ea5t c\u1ea3", new IAction(){

                        public void perform() {
                            Equip equip = Inventory.this.getEquipSelect();
                            if (equip == null) {
                                return;
                            }
                            int n = equip.num / 5;
                            if (n > 51) {
                                n = 51;
                            }
                            OfflineSpecialShop.imbue((byte)0, (byte)1, new int[]{equip.id}, new byte[]{(byte)(n * 5)});
                        }
                    }));
                }
                if (Inventory.this.getEquipSelect().isMaterial && OfflineSpecialShop.isExpPotion(Inventory.this.getEquipSelect().id) && Inventory.this.getEquipSelect().num > 0) {
                    vector.addElement(new Command("S\u1eed d\u1ee5ng t\u1ea5t c\u1ea3", new IAction(){

                        public void perform() {
                            Equip equip = Inventory.this.getEquipSelect();
                            if (equip == null) {
                                return;
                            }
                            int n = equip.num > 255 ? 255 : equip.num;
                            OfflineSpecialShop.imbue((byte)0, (byte)1, new int[]{equip.id}, new byte[]{(byte)n});
                        }
                    }));
                }
                if (!Inventory.this.getEquipSelect().isMaterial) {
                    vector.addElement(new Command(Language.ban(), new IAction(){

                        public void perform() {
                            Equip equip = Inventory.this.getEquipSelect();
                            if (equip == null) {
                                return;
                            }
                            CCanvas.startYesNoDlg(Language.ban() + " " + equip.name + "?", new IAction(){

                                public void perform() {
                                    Equip equip = Inventory.this.getEquipSelect();
                                    if (equip != null) {
                                        int[] nArray = new int[]{-equip.dbKey - 1};
                                        GameService.gI().buy_sell_Equip((byte)1, nArray, (short)-1, (byte)-1);
                                    }
                                }
                            }, new IAction(){

                                public void perform() {
                                    CCanvas.endDlg();
                                }
                            });
                        }
                    }));
                }
                vector.addElement(new Command(Language.detail(), new IAction(){

                    public void perform() {
                        if (!Inventory.this.getEquipSelect().isMaterial) {
                            CCanvas.startOKDlg(Inventory.this.getEquipSelect().getStrInvDetail());
                        } else if (Inventory.this.getEquipSelect().strDetail.startsWith(Language.fomula())) {
                            CCanvas.startOKDlg(Language.pleaseWait());
                            GameService.gI().getFomula((byte)Inventory.this.getEquipSelect().id, (byte)1, (byte)-1);
                        } else {
                            CCanvas.startOKDlg(Inventory.this.getEquipSelect().strDetail);
                        }
                    }
                }));
                CCanvas.menu.startAt(vector, 0);
            }
        });
        this.cmdXacnhan = new Command(Language.use(), new IAction(){

            public void perform() {
                Inventory.this.doCombine();
            }
        });
        this.right = new Command(Language.back(), new IAction(){

            public void perform() {
                if (!Inventory.this.isCombineNum) {
                    CCanvas.equipScreen.isClose = false;
                    CCanvas.equipScreen.show(CCanvas.menuScr);
                } else {
                    Inventory.this.isCombineNum = false;
                    Equip equip = Inventory.this.getEquipSelect();
                    if (equip != null) {
                        equip.isSelect = false;
                    }
                }
            }
        });
    }

    public void doUse() {
        if (this.size != 0) {
            int[] nArray = new int[this.dem];
            int n = 0;
            this.size = EquipScreen.inventory.size();
            for (int i = 0; i < this.size; ++i) {
                Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
                if (!equip.isSelect) continue;
                nArray[n] = equip.dbKey;
                ++n;
            }
        }
    }

    public Equip getEquipSelect() {
        Equip equip = null;
        this.size = EquipScreen.inventory.size();
        if (this.size > 0) {
            equip = (Equip)EquipScreen.inventory.elementAt(this.select2);
        }
        return equip;
    }

    public void unSelectEquip() {
        this.size = EquipScreen.inventory.size();
        for (int i = 0; i < this.size; ++i) {
            ((Equip)EquipScreen.inventory.elementAt((int)i)).isSelect = false;
            ((Equip)EquipScreen.inventory.elementAt((int)i)).numSelected = 0;
        }
        this.dem = 0;
    }

    public void combineYesNo(String string) {
        CCanvas.startYesNoDlg(string, new IAction(){

            public void perform() {
                GameService.gI().imbue((byte)1, (byte)-1, null, null);
                Inventory.this.unSelectEquip();
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
                Inventory.this.unSelectEquip();
            }
        });
    }

    public void doCombine() {
        this.size = EquipScreen.inventory.size();
        if (this.size > 0) {
            int n;
            Object object;
            this.isCombine = false;
            this.size = EquipScreen.inventory.size();
            for (int i = 0; i < EquipScreen.inventory.size(); ++i) {
                object = (Equip)EquipScreen.inventory.elementAt(i);
                if (!((Equip)object).isSelect) continue;
                ++this.dem;
            }
            int[] nArray = new int[this.dem];
            object = new byte[this.dem];
            int n2 = 0;
            for (n = 0; n < this.size; ++n) {
                Equip equip = (Equip)EquipScreen.inventory.elementAt(n);
                if (!equip.isSelect) continue;
                if (equip.isMaterial) {
                    this.isCombine = true;
                    nArray[n2] = equip.id;
                } else {
                    nArray[n2] = -equip.dbKey - 1;
                }
                ((byte[])object)[n2] = (byte)equip.numSelected;
                ++n2;
                equip.isSelect = false;
            }
            if (this.isCombine) {
                GameService.gI().imbue((byte)0, (byte)this.dem, nArray, (byte[])object);
            } else {
                for (n = 0; n < nArray.length; ++n) {
                    GameService.gI().buy_sell_Equip((byte)1, nArray, (short)-1, (byte)-1);
                }
            }
            this.dem = 0;
        }
    }

    public void doCombineSelect() {
        Equip equip;
        this.size = EquipScreen.inventory.size();
        if (this.size != 0 && (equip = this.getEquipSelect()) != null) {
            if (equip.num > 1) {
                equip.numSelected = 1;
                this.isCombineNum = true;
                this.numCombine = 1;
                equip.isSelect = true;
            } else {
                equip.numSelected = 1;
                equip.isSelect = !equip.isSelect;
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        super.paint(mGraphics2);
        mGraphics2.setColor(3832504);
        mGraphics2.fillRoundRect(CCanvas.width / 2 - 85, this.yPaint + 23, 170, 115, 6, 6, true);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        String string = Language.money() + ": " + playerInfo.xu + Language.xu() + "-" + playerInfo.luong + Language.luong();
        Font.normalFont.drawString(mGraphics2, string, CCanvas.width / 2, this.yPaint + 160, 3);
        Font.normalFont.drawString(mGraphics2, this.equipName, CCanvas.width / 2, this.yPaint + 142, 3);
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint + 12, 2, false);
        this.paintMaterial(mGraphics2, CCanvas.width / 2 - 78, this.yPaint + 29);
        if (this.isCombineNum) {
            this.paintCombineSelect(this.combineSelect, CCanvas.width / 2, CCanvas.hieght / 2, mGraphics2);
        }
        this.paintSuper(mGraphics2);
    }

    public void paintMaterial(mGraphics mGraphics2, int n, int n2) {
        int n3 = 0;
        int n4 = 0;
        mGraphics2.setClip(n - 2, n2 - 2, 170, this.gridViewH + 2);
        mGraphics2.setColor(16767817);
        this.size = EquipScreen.inventory.size();
        for (int i = 0; i < this.size; ++i) {
            Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
            int n5 = n + n3 * this.wTab + this.wXp;
            int n6 = n2 + n4 * this.wTab + this.wYp;
            int n7 = n6 - cmyI;
            if (i == this.select2) {
                mGraphics2.fillRect(n5 - (CCanvas.isTouch ? 12 : 2), n7 - (CCanvas.isTouch ? 12 : 2), CCanvas.isTouch ? 40 : 20, CCanvas.isTouch ? 40 : 20, true);
            }
            if (equip.isSelect) {
                mGraphics2.setColor(5612786);
                mGraphics2.fillRect(n5, n7, 16, 16, true);
            }
            equip.drawIcon(mGraphics2, n5, n7, true);
            if (!equip.isMaterial) {
                for (int j = 0; j < equip.socketCount(); ++j) {
                    if (i != this.select2) {
                        mGraphics2.setColor(16377901);
                    } else {
                        mGraphics2.setColor(0);
                    }
                    mGraphics2.fillRect(n5 + j * 4, n7, 2, 2, true);
                }
            }
            if (++n3 != this.nLine) continue;
            n3 = 0;
            ++n4;
        }
        mGraphics2.setClip(0, 0, 1000, 1000);
    }

    public void paintDetail(mGraphics mGraphics2, int n, int n2) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        String string = Language.money() + ": " + playerInfo.xu + Language.xu() + "-" + playerInfo.luong + Language.luong();
        int n3 = Font.normalFont.getWidth(this.equipDetail);
        if (n3 > 155) {
            CRes.transTextLimit(this.transText1, n3 - 155);
        }
        int n4 = this.transText1.x;
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint + 2, 2, false);
        Font.normalFont.drawString(mGraphics2, string, CCanvas.width / 2, n2 - 1, 3, false);
        mGraphics2.setColor(2378093);
        mGraphics2.fillRoundRect(n, n2 + 14, 170, 16, 6, 6, false);
        mGraphics2.fillRoundRect(n, n2 + 34, 170, 16, 6, 6, false);
        mGraphics2.fillRoundRect(n, n2 + 54, 170, 16, 6, 6, false);
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint + 2, 2, false);
        Font.normalGFont.drawString(mGraphics2, this.equipName, n + 6, n2 + 15, 0, false);
        Font.normalYFont.drawString(mGraphics2, this.date, n + 6, n2 + 35, 0, false);
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint + 2, 2, false);
        Font.normalYFont.drawString(mGraphics2, this.equipDetail, n + 6 + n4, n2 + 55, 0, false);
    }

    public void requestServer(String string) {
        CCanvas.startYesNoDlg(string, new IAction(){

            public void perform() {
                GameService.gI().buy_sell_Equip((byte)2, null, (short)-1, (byte)-1);
                CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 19);
                Inventory.this.select2 = 0;
                Inventory.this.dem = 0;
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
                Inventory.this.select2 = 0;
            }
        });
    }

    public void getDetail() {
        this.eSelect = (Equip)EquipScreen.inventory.elementAt(this.select2);
        this.equipDetail = this.eSelect.getStrShopDetail();
        this.equipName = this.eSelect.name;
        this.date = this.eSelect.date < 0 ? Language.expr() + ": V\u0129nh vi\u1ec5n" : Language.expr() + ": " + this.eSelect.date;
        this.transText1.x = 0;
    }

    private void scrollSelectionIntoView() {
        if (CCanvas.isTouch || this.size == 0) {
            return;
        }
        int n = this.select2 / this.nLine * this.wTab;
        int n2 = n + this.wTab;
        if (n < cmtoYI) {
            cmtoYI = n;
        } else if (n2 > cmtoYI + this.gridViewH) {
            cmtoYI = n2 - this.gridViewH;
        }
        if (cmtoYI > cmyILim) {
            cmtoYI = cmyILim;
        }
        if (cmtoYI < 0) {
            cmtoYI = 0;
        }
    }

    public void itemCamera() {
        if (cmyI != cmtoYI) {
            cmvyI = cmtoYI - cmyI << 2;
            cmdyI &= 0xF;
            if (CRes.abs((cmyI += (cmdyI += cmvyI) >> 3) - cmtoYI) < 3) {
                cmyI = cmtoYI;
                cmdyI = 0;
                cmvyI = 0;
            }
        }
        if (cmyI > cmyILim) {
            cmyI = cmyILim;
        }
        if (cmyI < 0) {
            cmyI = 0;
        }
    }

    public void paintCombineSelect(int n, int n2, int n3, mGraphics mGraphics2) {
        Inventory.paintDefaultPopup(n2 - 75, n3 - 30, 150, 60, mGraphics2);
        Font.normalFont.drawString(mGraphics2, Language.nhapsoluong(), CCanvas.hw, n3 - 15, 2);
        Font.normalFont.drawString(mGraphics2, this.numCombine + " " + Language.per(), n2, n3 + 18 - 15, 2);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 4, n2 - 40 + CCanvas.gameTick % 3, n3 + 20 - 15, 0, true);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 7, n2 + 30 - CCanvas.gameTick % 3, n3 + 20 - 15, 0, true);
    }

    public void update() {
        super.update();
        cmyILim = this.hLine * this.wTab - this.gridViewH;
        if (cmyILim < 0) {
            cmyILim = 0;
        }
        this.scrollSelectionIntoView();
        this.itemCamera();
        this.left = this.isCombineNum ? null : this.menu;
    }

    public void removeEquip(int n, int n2) {
        this.size = EquipScreen.inventory.size();
        for (int i = 0; i < this.size; ++i) {
            Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
            if (!equip.isMaterial) {
                if (equip.dbKey != n) continue;
                equip.num -= n2;
                if (equip.num <= 0) {
                    equip.num = 0;
                    EquipScreen.inventory.removeElement(equip);
                }
                return;
            }
            if (equip.id != n) continue;
            equip.num -= n2;
            if (equip.num <= 0) {
                equip.num = 0;
                EquipScreen.inventory.removeElement(equip);
            }
            return;
        }
    }

    public Equip getEquip(int n) {
        this.size = EquipScreen.inventory.size();
        for (int i = 0; i < this.size; ++i) {
            Equip equip = (Equip)EquipScreen.inventory.elementAt(i);
            CRes.out("DB KEY E= " + equip.dbKey);
            if (equip.dbKey != n) continue;
            return equip;
        }
        return null;
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!CCanvas.isPointer(n, n2, 150, 60, n3)) {
            this.isCombineNum = false;
        }
        if (!this.isCombineNum) {
            if (!this.trans) {
                this.pa = cmyI;
                this.trans = true;
            }
            if ((cmtoYI = this.pa + (CCanvas.pyFirst[n3] - n2)) < 0) {
                cmtoYI = 0;
            }
            if (cmtoYI > this.hLine * 40 - 40) {
                cmtoYI = this.hLine * 40 - 40;
            }
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (this.isCombineNum) {
            if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
                Equip equip = this.getEquipSelect();
                if (CCanvas.keyPressed[4] || CCanvas.keyPressed[8]) {
                    this.numCombine = equip.num > 5 ? (this.numCombine -= 5) : --this.numCombine;
                    if (this.numCombine <= 0) {
                        equip.isSelect = false;
                        this.numCombine = 0;
                    }
                    equip.numSelected = this.numCombine;
                }
                if (CCanvas.keyPressed[6] || CCanvas.keyPressed[2]) {
                    if (equip.num > 5) {
                        this.numCombine += this.numCombine == 1 ? 4 : 5;
                        if (this.numCombine > equip.num) {
                            this.numCombine -= 5;
                        }
                    } else {
                        this.numCombine = this.numCombine >= equip.num ? equip.num : ++this.numCombine;
                    }
                    equip.numSelected = this.numCombine;
                    equip.isSelect = true;
                }
                CScreen.clearKey();
            }
        } else if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
            if (CCanvas.keyPressed[2]) {
                this.select2 -= this.nLine;
            }
            if (CCanvas.keyPressed[8]) {
                this.select2 += this.nLine;
            }
            if (CCanvas.keyPressed[4]) {
                --this.select2;
            }
            if (CCanvas.keyPressed[6]) {
                ++this.select2;
            }
            if (this.select2 > EquipScreen.inventory.size() - 1) {
                this.select2 = 0;
            }
            if (this.select2 < 0) {
                this.select2 = EquipScreen.inventory.size() - 1;
            }
            if (EquipScreen.inventory.size() > 0) {
                this.getDetail();
            }
            CScreen.clearKey();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        int n4;
        int n5;
        this.trans = false;
        super.onPointerReleased(n, n2, n3);
        if (!CCanvas.isPointer(n, n2, 150, 60, n3)) {
            this.isCombineNum = false;
        }
        if (this.isCombineNum) {
            if (!CCanvas.isPointer(n - 75, n2 - 30, 150, 60, n3)) {
                this.isCombineNum = false;
            } else {
                Equip equip = this.getEquipSelect();
                if (CCanvas.isPointer(CCanvas.width / 2 - 100, CCanvas.hieght / 2 - 100, 100, 200, n3)) {
                    this.numCombine = equip.num > 5 ? (this.numCombine -= 5) : --this.numCombine;
                    if (this.numCombine <= 0) {
                        equip.isSelect = false;
                        this.numCombine = 0;
                    }
                    equip.numSelected = this.numCombine;
                }
                if (CCanvas.isPointer(CCanvas.width / 2, CCanvas.hieght / 2 - 100, 100, 200, n3)) {
                    if (equip.num > 5) {
                        this.numCombine += this.numCombine == 1 ? 4 : 5;
                        if (this.numCombine > equip.num) {
                            this.numCombine -= 5;
                        }
                    } else {
                        this.numCombine = this.numCombine >= equip.num ? equip.num : ++this.numCombine;
                    }
                    equip.numSelected = this.numCombine;
                    equip.isSelect = true;
                }
            }
        } else if (CCanvas.isPointer(this.xPaint, this.yPaint, this.wTabScreen, this.hTabScreen, n3) && CCanvas.isPointer(n5 = CCanvas.width / 2 - 78, n4 = this.yPaint + 29, 160, 120, n3)) {
            int n6 = (cmtoYI + n2 - n4) / this.wTab * this.nLine + (n - n5 - 8) / this.wTab;
            if (n6 == -1) {
                return;
            }
            if (n6 == this.select2 && this.center != null) {
                this.center.action.perform();
            }
            this.select2 = n6;
            if (this.select2 < 0) {
                this.select2 = 0;
            }
            if (this.select2 > this.size - 1) {
                this.select2 = this.size - 1;
            }
            this.getDetail();
        }
    }
}

