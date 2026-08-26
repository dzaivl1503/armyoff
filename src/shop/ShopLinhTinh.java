/*
 * Decompiled with CFR 0.152.
 */
package shop;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import Equipment.Equip;
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
import screen.PrepareScr;
import screen.TabScreen;

public class ShopLinhTinh
extends TabScreen {
    int select;
    public static Vector items = new Vector();
    public int W = CCanvas.width;
    static int nLine = 8;
    public static int hLine;
    public static int wTab;
    static int wXp;
    static int wYp;
    Command cmdSelect;
    public static int cmtoYI;
    public static int cmyI;
    public static int cmdyI;
    public static int cmvyI;
    public static int cmyILim;
    Vector myShop = new Vector();
    int size = 0;
    Position transText1 = new Position(0, 1);
    Equip eSelect;
    public String equipDetail = "";
    public String equipName = "";
    public String price = "";
    int num = 1;
    boolean isSelectNum;
    String ngay;
    public static int pa;
    public static boolean trans;
    static int numItemMua;
    static int tongTien;

    public ShopLinhTinh() {
        this.xPaint = CCanvas.width / 2 - 85;
        this.yPaint = (CCanvas.hieght - CScreen.cmdH) / 2 - 85;
        this.hTabScreen = 180;
        this.n = 4;
        this.title = Language.shoDacBiet();
        this.getW();
        if (CCanvas.isTouch) {
            nLine = 4;
            wXp = 9;
            wYp = 5;
            wTab = 40;
        } else {
            nLine = 8;
            wXp = 0;
            wYp = 0;
            wTab = 20;
        }
        this.nameCScreen = " ShopLinhTinh screen!";
    }

    public void getCommand() {
        final Command command = new Command(Language.muaXu(), new IAction(){

            public void perform() {
                String string = "";
                string = ShopLinhTinh.this.getCurrEq().isBuyNum ? Language.bancochac() + ShopLinhTinh.this.eSelect.xu * ShopLinhTinh.this.num + " " + Language.xu() : Language.bancochac() + ShopLinhTinh.this.eSelect.xu + " " + Language.xu();
                CCanvas.startYesNoDlg(string, new IAction(){

                    public void perform() {
                        CCanvas.startOKDlg(Language.pleaseWait());
                        ShopLinhTinh.this.getCurrEq().numSelected = 0;
                        GameService.gI().getShopLinhtinh((byte)1, (byte)0, (byte)ShopLinhTinh.this.getCurrEq().id, (byte)ShopLinhTinh.this.num);
                    }
                });
            }
        });
        final Command command2 = new Command(Language.muaLuong(), new IAction(){

            public void perform() {
                String string = "";
                string = ShopLinhTinh.this.getCurrEq().isBuyNum ? Language.bancochac() + ShopLinhTinh.this.eSelect.luong * ShopLinhTinh.this.num + " " + Language.luong() : Language.bancochac() + ShopLinhTinh.this.eSelect.luong + " " + Language.luong();
                CCanvas.startYesNoDlg(string, new IAction(){

                    public void perform() {
                        CCanvas.startOKDlg(Language.pleaseWait());
                        ShopLinhTinh.this.getCurrEq().numSelected = 0;
                        GameService.gI().getShopLinhtinh((byte)1, (byte)1, (byte)ShopLinhTinh.this.getCurrEq().id, (byte)ShopLinhTinh.this.num);
                    }
                });
            }
        });
        Command command3 = new Command("Menu", new IAction(){

            public void perform() {
                Vector<Command> vector = new Vector<Command>();
                if (ShopLinhTinh.this.getCurrEq().strDetail.startsWith(Language.fomula())) {
                    vector.addElement(new Command(Language.detail(), new IAction(){

                        public void perform() {
                            GameService.gI().getFomula((byte)ShopLinhTinh.this.getCurrEq().id, (byte)1, (byte)-1);
                        }
                    }));
                }
                vector.addElement(command);
                vector.addElement(command2);
                CCanvas.menu.startAt(vector, 0);
            }
        });
        if (this.eSelect == null) {
            this.left = command;
        } else {
            this.left = this.eSelect.luong != -1 && this.eSelect.xu != -1 ? command3 : (this.eSelect.xu == -1 && this.eSelect.luong != -1 ? command2 : command);
            this.right = new Command(Language.back(), new IAction(){

                public void perform() {
                    if (!ShopLinhTinh.this.isSelectNum) {
                        ShopLinhTinh.this.doClose();
                    } else {
                        ShopLinhTinh.this.isSelectNum = false;
                        ShopLinhTinh.this.getCurrEq().numSelected = 0;
                    }
                }
            });
            this.cmdSelect = new Command(Language.select(), new IAction(){

                public void perform() {
                    ShopLinhTinh.this.isSelectNum = true;
                    ShopLinhTinh.this.num = 1;
                    for (int i = 0; i < ShopLinhTinh.this.myShop.size(); ++i) {
                        ((Equip)ShopLinhTinh.this.myShop.elementAt((int)i)).numSelected = 0;
                        ((Equip)ShopLinhTinh.this.myShop.elementAt((int)i)).isSelect = false;
                    }
                }
            });
        }
    }

    public static void itemCamera() {
        if (cmyI != cmtoYI) {
            cmvyI = cmtoYI - cmyI << 2;
            cmyI += (cmdyI += cmvyI) >> 4;
            cmdyI &= 0xF;
        }
        if (cmyI > cmyILim) {
            cmyI = cmyILim;
        }
        if (cmyI < 0) {
            cmyI = 0;
        }
    }

    public void doClose() {
        this.isClose = true;
    }

    public Equip getCurrEq() {
        Equip equip = (Equip)this.myShop.elementAt(this.select);
        return equip;
    }

    public Equip getCurrEq(int n) {
        if (n >= this.myShop.size()) {
            return null;
        }
        if (n < 0) {
            return null;
        }
        Equip equip = (Equip)this.myShop.elementAt(n);
        return equip;
    }

    public void getMyShop() {
        this.myShop.removeAllElements();
        for (int i = 0; i < items.size(); ++i) {
            Equip equip = (Equip)items.elementAt(i);
            this.myShop.addElement(equip);
        }
    }

    public void setItems(Vector vector) {
        this.select = 0;
        items.removeAllElements();
        items = vector;
        this.getMyShop();
        this.size = this.myShop.size();
        hLine = this.myShop.size() / nLine;
        if (this.size % nLine != 0) {
            ++hLine;
        }
        cmyI = 0;
        cmtoYI = 0;
        cmyILim = hLine * wTab - 72;
        if (cmyILim < 0) {
            cmyILim = 0;
        }
        this.eSelect = (Equip)this.myShop.elementAt(this.select);
        this.equipDetail = this.eSelect.strDetail;
        this.equipName = this.eSelect.name;
        this.price = Language.price() + ": " + this.eSelect.xu + Language.xu() + "(" + this.eSelect.date + Language.ngay() + ")";
    }

    public void getMaterialIcon(int n, byte[] byArray, int n2) {
        for (int i = 0; i < this.myShop.size(); ++i) {
            Equip equip = (Equip)this.myShop.elementAt(i);
            if (equip.id != n) continue;
            equip.materialIcon = mImage.createImage(byArray, 0, n2, "");
        }
    }

    public void getMaterialIcon(int n, Image image) {
        for (int i = 0; i < this.myShop.size(); ++i) {
            Equip equip = (Equip)this.myShop.elementAt(i);
            if (equip.id != n) continue;
            equip.materialIcon = new mImage(image);
        }
    }

    public static void paintEquip(mGraphics mGraphics2, int n, int n2, Vector vector, int n3) {
        int n4 = 0;
        int n5 = 0;
        mGraphics2.translate(0, -cmyI);
        mGraphics2.setColor(16767817);
        for (int i = 0; i < vector.size(); ++i) {
            mGraphics2.setClip(n - 7, n2 - 2 + cmyI, 170, 72);
            mGraphics2.beginClip();
            Equip equip = (Equip)vector.elementAt(i);
            int n6 = n + n4 * wTab + wXp;
            int n7 = n2 + n5 * wTab + wYp;
            if (i == n3) {
                mGraphics2.fillRect(n6 - (CCanvas.isTouch ? 12 : 2), n7 - (CCanvas.isTouch ? 12 : 2), CCanvas.isTouch ? 40 : 20, CCanvas.isTouch ? 40 : 20, false);
                if (!CCanvas.isTouch) {
                    cmtoYI = n7 - (n2 + 20);
                    if (cmtoYI > cmyILim) {
                        cmtoYI = cmyILim;
                    }
                    if (cmtoYI < 0) {
                        cmtoYI = 0;
                    }
                }
            }
            if (equip.isSelect) {
                mGraphics2.setColor(5612786);
                mGraphics2.fillRect(n6, n7, 16, 16, false);
            }
            equip.drawIcon(mGraphics2, n6, n7, false);
            if (++n4 != nLine) continue;
            n4 = 0;
            ++n5;
        }
        mGraphics2.translate(0, -mGraphics2.getTranslateY());
        mGraphics2.resetClip();
    }

    public void doNumSelect() {
        Equip equip;
        if (this.myShop.size() != 0 && (equip = this.getCurrEq()) != null) {
            if (equip.num > 1) {
                equip.numSelected = 1;
                this.isSelectNum = true;
                this.num = 1;
                equip.isSelect = true;
            } else {
                equip.numSelected = 1;
                equip.isSelect = !equip.isSelect;
            }
        }
    }

    public void paintBuyBar(int n, int n2, int n3, mGraphics mGraphics2) {
        ShopLinhTinh.paintDefaultPopup(n2 - 75, n3 - 30, 150, 60, mGraphics2);
        Font.normalFont.drawString(mGraphics2, Language.nhapsoluong(), CCanvas.hw, n3 - 15, 2);
        Font.normalFont.drawString(mGraphics2, this.num + " " + Language.per(), n2, n3 + 18 - 15, 2);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 4, n2 - 40 + CCanvas.gameTick % 3, n3 + 20 - 15, 0, false);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 7, n2 + 30 - CCanvas.gameTick % 3, n3 + 20 - 15, 0, false);
    }

    public void paintDetail(mGraphics mGraphics2, int n, int n2) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        String string = Language.money() + ": " + playerInfo.xu + Language.xu() + "-" + playerInfo.luong + Language.luong();
        int n3 = Font.normalFont.getWidth(this.equipDetail);
        if (n3 > 155) {
            CRes.transTextLimit(this.transText1, n3 - 150);
        }
        int n4 = this.transText1.x;
        Font.normalFont.drawString(mGraphics2, string, this.W / 2, n2 - 1, 3);
        mGraphics2.setColor(2378093);
        mGraphics2.fillRoundRect(n, n2 + 14, 170, 16, 6, 6, false);
        mGraphics2.fillRoundRect(n, n2 + 34, 170, 16, 6, 6, false);
        mGraphics2.fillRoundRect(n, n2 + 54, 170, 16, 6, 6, false);
        mGraphics2.setClip(n + 4, n2 + 14, 162, 16);
        Font.normalGFont.drawString(mGraphics2, this.equipName, n + 6, n2 + 15, 0, true);
        mGraphics2.resetClip();
        mGraphics2.setClip(n + 4, n2 + 34, 162, 16);
        Font.normalYFont.drawString(mGraphics2, this.price, n + 6, n2 + 35, 0, true);
        mGraphics2.resetClip();
        mGraphics2.setClip(n + 4, n2 + 54, 162, 16);
        Font.normalYFont.drawString(mGraphics2, this.equipDetail, n + 6 + n4, n2 + 55, 0, true);
        mGraphics2.resetClip();
    }

    public void paint(mGraphics mGraphics2) {
        super.paint(mGraphics2);
        mGraphics2.setColor(3832504);
        mGraphics2.fillRoundRect(this.W / 2 - 85, this.yPaint + 23, 170, 78, 6, 6, false);
        ShopLinhTinh.paintEquip(mGraphics2, this.W / 2 - 78, this.yPaint + 29, this.myShop, this.select);
        this.paintDetail(mGraphics2, this.W / 2 - 85, this.yPaint + 103);
        if (this.isSelectNum) {
            this.paintBuyBar(0, CCanvas.width / 2, CCanvas.hieght / 2, mGraphics2);
        }
        this.paintSuper(mGraphics2);
    }

    public void update() {
        super.update();
    }

    public void mainLoop() {
        super.mainLoop();
        ShopLinhTinh.itemCamera();
    }

    public void getDetail() {
        this.num = 1;
        for (int i = 0; i < this.myShop.size(); ++i) {
            ((Equip)this.myShop.elementAt((int)i)).numSelected = 0;
            ((Equip)this.myShop.elementAt((int)i)).isSelect = false;
        }
        this.eSelect = (Equip)this.myShop.elementAt(this.select);
        this.equipDetail = this.eSelect.strDetail;
        this.equipName = this.eSelect.name;
        String string = (this.eSelect.xu != -1 ? "-" : "") + this.eSelect.luong + Language.luong();
        if (this.eSelect.luong == -1) {
            string = "";
        }
        String string2 = this.eSelect.xu + Language.xu();
        if (this.eSelect.xu == -1) {
            string2 = "";
        }
        this.ngay = this.eSelect.date > 0 ? "(" + this.eSelect.date + Language.ngay() + ")" : "";
        this.price = Language.price() + ": " + string2 + string + this.ngay;
        this.getCommand();
        this.transText1.x = 0;
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (this.isSelectNum) {
            if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
                Equip equip;
                if (CCanvas.keyPressed[4] || CCanvas.keyPressed[8]) {
                    Equip equip2 = this.getCurrEq();
                    if (equip2 != null) {
                        if (this.num <= 0) {
                            this.num = 99;
                            equip2.isSelect = true;
                        } else {
                            --this.num;
                            if (this.num <= 0) {
                                equip2.isSelect = false;
                                this.num = 0;
                            }
                        }
                        equip2.numSelected = this.num < 0 ? 0 : this.num;
                    }
                } else if ((CCanvas.keyPressed[6] || CCanvas.keyPressed[2]) && (equip = this.getCurrEq()) != null) {
                    this.num = equip.num > 5 ? ++this.num : (this.num > 100 ? 100 : ++this.num);
                    equip.numSelected = this.num > 100 ? 100 : this.num;
                    equip.isSelect = true;
                }
                CScreen.clearKey();
            }
        } else if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
            if (CCanvas.keyPressed[2]) {
                this.select -= nLine;
            }
            if (CCanvas.keyPressed[8]) {
                this.select += nLine;
            }
            if (CCanvas.keyPressed[4]) {
                --this.select;
            }
            if (CCanvas.keyPressed[6]) {
                ++this.select;
            }
            if (this.select > this.myShop.size() - 1) {
                this.select = 0;
            }
            if (this.select < 0) {
                this.select = this.myShop.size() - 1;
            }
            if ((cmtoYI = this.select / nLine * wTab - 20) > cmyILim) {
                cmtoYI = cmyILim;
            }
            if (cmtoYI < 0) {
                cmtoYI = 0;
            }
            this.getDetail();
            CScreen.clearKey();
        } else if (CCanvas.keyPressed[5] && this.getCurrEq() != null && this.getCurrEq().isBuyNum) {
            this.cmdSelect.action.perform();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        int n4;
        super.onPointerReleased(n, n2, n3);
        trans = false;
        if (!this.isSelectNum) {
            n4 = -1;
            if (CCanvas.isPointer(this.xPaint, this.yPaint + 27, 170, 74, n3)) {
                n4 = (cmyI + n2 - this.yPaint - 29 - wYp) / wTab * nLine + (n - this.xPaint - 7 - wXp) / wTab;
            }
            if (n4 == this.select && CCanvas.isDoubleClick) {
                if (this.getCurrEq() != null && this.getCurrEq().isBuyNum) {
                    this.cmdSelect.action.perform();
                } else if (this.left != null) {
                    this.left.action.perform();
                }
            }
            if (n4 >= 0 && n4 < this.myShop.size()) {
                this.select = n4;
                this.getDetail();
            }
        }
        n4 = CCanvas.width / 2;
        int n5 = CCanvas.hieght / 2;
        if (this.isSelectNum) {
            Equip equip;
            if (CCanvas.isPointer(n4 - 40 + CCanvas.gameTick % 3, n5 + 20 - 15, 13, 13, n3) && (equip = this.getCurrEq()) != null) {
                --this.num;
                if (this.num <= 0) {
                    equip.isSelect = false;
                    this.num = 0;
                }
                int n6 = equip.numSelected = this.num < 0 ? 0 : this.num;
            }
            if (CCanvas.isPointer(n4 + 30 - CCanvas.gameTick % 3, n5 + 20 - 15, 40, 40, n3) && (equip = this.getCurrEq()) != null) {
                this.num = equip.num > 5 ? ++this.num : (this.num > 100 ? 100 : ++this.num);
                equip.numSelected = this.num > 100 ? 100 : this.num;
                equip.isSelect = true;
            }
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!trans) {
            pa = cmyI - 10;
            trans = true;
        }
        if ((cmtoYI = pa + (CCanvas.pyFirst[n3] - n2)) < 0) {
            cmtoYI = 0;
        }
        if (cmtoYI > this.hTabScreen * 40 - 40) {
            cmtoYI = this.hTabScreen * 40 - 40;
        }
    }

    public void show(CScreen cScreen) {
        super.show(cScreen);
        cmtoYI = 0;
        this.getCommand();
        this.getDetail();
    }

    static {
        pa = 0;
        trans = false;
    }
}

