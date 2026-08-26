/*
 * Decompiled with CFR 0.152.
 */
package shop;

import CLib.mGraphics;
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
import screen.TabScreen;

public class ShopEquipment
extends TabScreen {
    private static final int PANEL_W = 170;
    private static final int GRID_BG_Y = 23;
    private static final int GRID_PAD = 6;
    private static final int GRID_ROWS_MIN = 2;
    private static final int GRID_H_BUDGET = 66;
    private static final int DETAIL_GAP = 2;
    private static final int DETAIL_H = 78;
    private static final int DETAIL_TEXT_W = 158;
    private int panelX;
    private int gridRows;
    private int gridViewH;
    private int gridBgH;
    private int detailYOffset;
    private int gridPaintX;
    private int gridPaintY;
    int select;
    private Vector items = new Vector();
    private int W;
    private int nLine;
    private int hLine;
    private int wTab;
    private int wXp;
    private int wYp;
    private int cmtoYI;
    private int cmyI;
    private int cmdyI;
    private int cmvyI;
    private int cmyILim;
    private int cmtoYID;
    private int cmyID;
    private int cmdyID;
    private int cmvyID;
    private int cmyIDLim;
    Vector myShop = new Vector();
    Position transText1 = new Position(0, 1);
    Position transText2 = new Position(0, 1);
    Equip eSelect;
    public String equipDetail = "";
    public String equipName = "";
    public String price = "";
    int size = 0;
    public boolean expandDetail;
    private int xExpand;
    private int yExpand;
    public static int pa = 0;
    public static int paID = 0;
    public static boolean trans = false;
    int speed = 1;
    private int lastDetailSelect = -1;
    private int keyNavCooldown;

    public ShopEquipment() {
        this.nameCScreen = " ShopEquipment screen!";
        this.right = new Command(Language.back(), new IAction(){

            public void perform() {
                ShopEquipment.this.doClose();
            }
        });
        this.n = 4;
        this.title = Language.shoptrangbi();
        this.layout();
    }

    public void show(CScreen cScreen) {
        this.layout();
        this.recalculateGrid();
        super.show(cScreen);
        this.cmtoYI = 0;
        this.cmtoYID = 0;
        this.getCommand();
        this.getDetail();
    }

    public void paint(mGraphics mGraphics2) {
        this.layout();
        super.paint(mGraphics2);
        int n = this.yPaint + 23;
        mGraphics2.setColor(3832504);
        mGraphics2.fillRoundRect(this.panelX, n, 170, this.gridBgH, 6, 6, false);
        this.gridPaintX = this.panelX + (170 - this.nLine * this.wTab) / 2;
        this.gridPaintY = n + 6;
        this.paintEquip(mGraphics2, this.gridPaintX, this.gridPaintY, this.myShop, this.select);
        this.paintDetail(mGraphics2, this.panelX, this.yPaint + this.detailYOffset);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        mGraphics2.translate(0, -mGraphics2.getTranslateY());
        this.paintSuper(mGraphics2);
    }

    public void update() {
        super.update();
        this.handleGridKeys();
    }

    public void mainLoop() {
        super.mainLoop();
        this.itemCamera();
    }

    public void getCommand() {
        final Command command = new Command(Language.muaXu(), new IAction(){

            public void perform() {
                CCanvas.startYesNoDlg(Language.bancochac() + ShopEquipment.this.eSelect.xu + Language.xu(), new IAction(){

                    public void perform() {
                        CCanvas.startOKDlg(Language.pleaseWait());
                        GameService.gI().buy_sell_Equip((byte)0, null, (short)ShopEquipment.this.getCurrEq().index, (byte)0);
                    }
                });
            }
        });
        final Command command2 = new Command(Language.muaLuong(), new IAction(){

            public void perform() {
                CCanvas.startYesNoDlg(Language.bancochac() + ShopEquipment.this.eSelect.luong + Language.luong(), new IAction(){

                    public void perform() {
                        CCanvas.startOKDlg(Language.pleaseWait());
                        GameService.gI().buy_sell_Equip((byte)0, null, (short)ShopEquipment.this.getCurrEq().index, (byte)1);
                    }
                });
            }
        });
        Command command3 = new Command("Menu", new IAction(){

            public void perform() {
                Vector<Command> vector = new Vector<Command>();
                vector.addElement(command);
                vector.addElement(command2);
                CCanvas.menu.startAt(vector, 0);
            }
        });
        this.left = this.eSelect == null ? command : (this.eSelect.luong != -1 && this.eSelect.xu != -1 ? command3 : (this.eSelect.xu == -1 && this.eSelect.luong != -1 ? command2 : command));
    }

    private void itemCamera() {
        if (this.cmyI != this.cmtoYI) {
            this.cmvyI = this.cmtoYI - this.cmyI << 2;
            this.cmdyI += this.cmvyI;
            this.cmyI += this.cmdyI >> 3;
            this.cmdyI &= 0xF;
            if (Math.abs(this.cmyI - this.cmtoYI) < 3) {
                this.cmyI = this.cmtoYI;
                this.cmdyI = 0;
                this.cmvyI = 0;
            }
        }
        if (this.cmyI > this.cmyILim) {
            this.cmyI = this.cmyILim;
        }
        if (this.cmyI < 0) {
            this.cmyI = 0;
        }
        if (this.cmyID != this.cmtoYID) {
            this.cmvyID = this.cmtoYID - this.cmyID << 2;
            this.cmdyID += this.cmvyID;
            this.cmyID += this.cmdyID >> 4;
            this.cmdyID &= 0xF;
        }
        if (this.cmyID > this.cmyIDLim) {
            this.cmyID = this.cmyIDLim;
        }
        if (this.cmyID < 0) {
            this.cmyID = 0;
        }
    }

    public void doClose() {
        this.isClose = true;
    }

    public Equip getCurrEq() {
        Equip equip = (Equip)this.myShop.elementAt(this.select);
        return equip;
    }

    public void getMyShop() {
        this.myShop.removeAllElements();
        if (TerrainMidlet.myInfo == null) {
            return;
        }
        byte by = TerrainMidlet.myInfo.gun;
        for (int i = 0; i < this.items.size(); ++i) {
            Equip equip = (Equip)this.items.elementAt(i);
            if (equip == null || equip.glass != by) continue;
            equip.index = this.myShop.size();
            this.myShop.addElement(equip);
        }
    }

    public void setItems(Vector vector) {
        this.select = 0;
        this.items.removeAllElements();
        this.items = vector;
        this.configureGridLayout();
        this.getMyShop();
        this.size = this.myShop.size();
        if (this.size == 0) {
            this.eSelect = null;
            this.equipName = "";
            this.price = "";
            this.equipDetail = "";
            return;
        }
        this.recalculateGrid();
        this.eSelect = (Equip)this.myShop.elementAt(this.select);
        this.cmtoYI = 0;
        this.cmyI = 0;
        this.cmdyI = 0;
        this.cmvyI = 0;
        this.lastDetailSelect = -1;
        this.syncScrollToSelect();
        this.refreshDetailIfNeeded();
    }

    private void layout() {
        this.W = CCanvas.width;
        this.configureGridLayout();
        this.getW();
        this.xPaint = this.panelX = this.W / 2 - 85;
        this.yPaint = (CCanvas.hieght - CScreen.cmdH - this.hTabScreen) / 2;
    }

    private void configureGridLayout() {
        if (CCanvas.isTouch) {
            this.nLine = 4;
            this.wTab = 40;
        } else {
            this.nLine = 8;
            this.wTab = 20;
        }
        this.wYp = this.wXp = (this.wTab - 16) / 2;
        this.gridRows = Math.max(2, 66 / this.wTab);
        this.gridViewH = this.gridRows * this.wTab;
        this.gridBgH = this.gridViewH + 12;
        this.detailYOffset = 23 + this.gridBgH + 2;
        this.hTabScreen = this.detailYOffset + 78;
    }

    private void recalculateGrid() {
        this.hLine = this.nLine <= 0 ? 0 : (this.myShop.size() + this.nLine - 1) / this.nLine;
        this.cmyILim = Math.max(0, this.hLine * this.wTab - this.gridViewH);
        if (this.cmtoYI > this.cmyILim) {
            this.cmtoYI = this.cmyILim;
        }
        if (this.cmyI > this.cmyILim) {
            this.cmyI = this.cmyILim;
        }
    }

    private void syncScrollToSelect() {
        if (this.cmyILim <= 0) {
            this.cmtoYI = 0;
            return;
        }
        int n = this.select / this.nLine;
        int n2 = n * this.wTab;
        int n3 = n2 + this.wTab;
        if (n2 < this.cmtoYI) {
            this.cmtoYI = n2;
        } else if (n3 > this.cmtoYI + this.gridViewH) {
            this.cmtoYI = n3 - this.gridViewH;
        }
        if (this.cmtoYI < 0) {
            this.cmtoYI = 0;
        } else if (this.cmtoYI > this.cmyILim) {
            this.cmtoYI = this.cmyILim;
        }
    }

    private void refreshDetailIfNeeded() {
        if (this.select == this.lastDetailSelect || this.size == 0) {
            return;
        }
        this.lastDetailSelect = this.select;
        this.getDetail();
    }

    private void moveSelect(int n, int n2) {
        if (this.myShop.size() == 0) {
            return;
        }
        int n3 = this.select;
        if (n != 0) {
            if ((n3 += n) < 0) {
                n3 = this.myShop.size() - 1;
            } else if (n3 >= this.myShop.size()) {
                n3 = 0;
            }
        } else if (n2 != 0) {
            if ((n3 += n2 * this.nLine) < 0) {
                n3 = 0;
            } else if (n3 >= this.myShop.size()) {
                n3 = this.myShop.size() - 1;
            }
        }
        this.select = n3;
        this.syncScrollToSelect();
        this.refreshDetailIfNeeded();
    }

    private void handleGridKeys() {
        boolean bl;
        if (this.myShop.size() == 0 || this.expandDetail) {
            return;
        }
        boolean bl2 = CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8];
        boolean bl3 = bl = CCanvas.keyHold[2] || CCanvas.keyHold[4] || CCanvas.keyHold[6] || CCanvas.keyHold[8];
        if (!bl2 && !bl) {
            this.keyNavCooldown = 0;
            return;
        }
        if (!bl2) {
            --this.keyNavCooldown;
            if (this.keyNavCooldown > 0) {
                return;
            }
            this.keyNavCooldown = 5;
        } else {
            this.keyNavCooldown = 10;
        }
        if (CCanvas.keyPressed[2] || CCanvas.keyHold[2]) {
            this.moveSelect(0, -1);
        } else if (CCanvas.keyPressed[8] || CCanvas.keyHold[8]) {
            this.moveSelect(0, 1);
        } else if (CCanvas.keyPressed[4] || CCanvas.keyHold[4]) {
            this.moveSelect(-1, 0);
        } else if (CCanvas.keyPressed[6] || CCanvas.keyHold[6]) {
            this.moveSelect(1, 0);
        }
        CScreen.clearKey();
    }

    private static String formatEquipDisplayName(Equip equip) {
        if (equip == null || equip.name == null) {
            return "";
        }
        return equip.name.trim();
    }

    private void paintEquip(mGraphics mGraphics2, int n, int n2, Vector vector, int n3) {
        mGraphics2.translate(0, -this.cmyI);
        mGraphics2.setClip(this.panelX, n2 + this.cmyI, 170, this.gridViewH);
        for (int i = 0; i < vector.size(); ++i) {
            Equip equip = (Equip)vector.elementAt(i);
            int n4 = n + i % this.nLine * this.wTab;
            int n5 = n2 + i / this.nLine * this.wTab;
            int n6 = n4 + this.wXp;
            int n7 = n5 + this.wYp;
            if (i == n3) {
                mGraphics2.setColor(16767817);
                mGraphics2.fillRect(n4, n5, this.wTab, this.wTab, true);
            }
            if (equip.isSelect) {
                mGraphics2.setColor(5612786);
                mGraphics2.fillRect(n6, n7, 16, 16, true);
            }
            equip.drawIcon(mGraphics2, n6, n7, true);
        }
        mGraphics2.translate(0, this.cmyI);
        mGraphics2.resetClip();
    }

    public void paintDetail(mGraphics mGraphics2, int n, int n2) {
        int n3;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        String string = Language.money() + ": " + playerInfo.xu + Language.xu() + " - " + playerInfo.luong + Language.luong();
        int n4 = Font.normalYFont.getWidth(this.equipDetail);
        if (n4 > 158) {
            CRes.transTextLimit(this.transText1, n4 - 158);
        }
        if ((n3 = Font.normalYFont.getWidth(this.price)) > 158) {
            CRes.transTextLimit(this.transText2, n3 - 158);
        }
        mGraphics2.setColor(2378093);
        mGraphics2.fillRoundRect(n, n2 + 14, 170, 16, 6, 6, false);
        mGraphics2.fillRoundRect(n, n2 + 34, 170, 16, 6, 6, false);
        mGraphics2.fillRoundRect(n, n2 + 54, 170, 16, 6, 6, false);
        mGraphics2.setClip(n + 6, n2 - 3, 158, 73);
        Font.normalFont.drawString(mGraphics2, string, n + 85, n2 - 1, 3);
        Font.normalGFont.drawString(mGraphics2, this.equipName, n + 6, n2 + 15, 0);
        Font.normalYFont.drawString(mGraphics2, this.price, n + 6 + this.transText2.x, n2 + 35, 0);
        Font.normalYFont.drawString(mGraphics2, this.equipDetail, n + 6 + this.transText1.x, n2 + 55, 0);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
    }

    public void getDetail() {
        if (this.select < this.size) {
            this.eSelect = (Equip)this.myShop.elementAt(this.select);
            this.equipDetail = this.eSelect.getStrShopDetail();
            String string = null;
            if (this.eSelect.glass == 0) {
                string = "Gunner";
            }
            if (this.eSelect.glass == 1) {
                string = "Miss 6";
            }
            if (this.eSelect.glass == 2) {
                string = "Electician";
            }
            if (this.eSelect.glass == 3) {
                string = "KingKong";
            }
            if (this.eSelect.glass == 4) {
                string = "Rocketer";
            }
            if (this.eSelect.glass == 5) {
                string = "Granos";
            }
            if (this.eSelect.glass == 6) {
                string = "Chicken";
            }
            if (this.eSelect.glass == 7) {
                string = "Tarzan";
            }
            if (this.eSelect.glass == 8) {
                string = "Apache";
            }
            if (this.eSelect.glass == 9) {
                string = "Magenta";
            }
            this.equipName = ShopEquipment.formatEquipDisplayName(this.eSelect) + " (lvl" + this.eSelect.level + ")";
            String string2 = (this.eSelect.xu != -1 ? "-" : "") + this.eSelect.luong + Language.luong();
            if (this.eSelect.luong == -1) {
                string2 = "";
            }
            String string3 = this.eSelect.xu + Language.xu();
            if (this.eSelect.xu == -1) {
                string3 = "";
            }
            String string4 = this.eSelect.date > 0 ? " (" + this.eSelect.date + Language.ngay() + ")" : "";
            this.price = Language.price() + ": " + string3 + string2 + string4;
            this.getCommand();
            this.transText1.x = 0;
            this.transText2.x = 0;
            this.cmyIDLim = this.eSelect.shopDetailNunStrs != null ? Math.max(0, this.eSelect.shopDetailNunStrs.size() * ITEM_HEIGHT - 14) : 0;
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    private boolean isPointerInGrid(int n) {
        return CCanvas.isPointer(this.panelX, this.yPaint + 23, 170, this.gridBgH, n);
    }

    private int hitTestGridItem(int n, int n2) {
        if (n2 < this.gridPaintY || n2 >= this.gridPaintY + this.gridViewH) {
            return -1;
        }
        int n3 = n - this.gridPaintX;
        int n4 = n2 - this.gridPaintY + this.cmyI;
        if (n3 < 0 || n4 < 0 || n3 >= this.nLine * this.wTab) {
            return -1;
        }
        int n5 = n3 / this.wTab;
        int n6 = n4 / this.wTab;
        if (n5 < 0 || n5 >= this.nLine || n6 < 0 || n6 >= this.hLine) {
            return -1;
        }
        int n7 = n6 * this.nLine + n5;
        if (n7 < 0 || n7 >= this.myShop.size()) {
            return -1;
        }
        return n7;
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        boolean bl = trans;
        trans = false;
        if (!this.isPointerInGrid(n3)) {
            return;
        }
        if (bl) {
            return;
        }
        int n4 = this.hitTestGridItem(n, n2);
        if (n4 >= 0) {
            if (n4 == this.select && this.left != null && CCanvas.isDoubleClick) {
                this.left.action.perform();
            }
            this.select = n4;
            this.syncScrollToSelect();
            this.refreshDetailIfNeeded();
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!trans) {
            pa = this.cmyI;
            paID = this.cmyID;
            trans = true;
        }
        this.speed = 1;
        if (this.isPointerInGrid(n3)) {
            this.cmtoYI = pa + (CCanvas.pyFirst[n3] - n2) * this.speed;
            if (this.cmtoYI < 0) {
                this.cmtoYI = 0;
            }
            if (this.cmtoYI > this.cmyILim) {
                this.cmtoYI = this.cmyILim;
            }
        }
        if (this.expandDetail && CCanvas.isPointer(this.panelX, this.yPaint + this.detailYOffset + 54, 170, 16, n3)) {
            this.cmtoYID = paID + (CCanvas.pyFirst[n3] - n2) * this.speed;
            if (this.cmtoYID < 0) {
                this.cmtoYID = 0;
            }
            if (this.cmyIDLim > 0 && this.cmtoYID > this.cmyIDLim) {
                this.cmtoYID = this.cmyIDLim;
            }
        }
    }
}

