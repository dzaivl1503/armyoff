/*
 * Decompiled with CFR 0.152.
 */
package shop;

import CLib.mGraphics;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import item.Item;
import item.MyItemIcon;
import java.util.Vector;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.PrepareScr;
import screen.TabScreen;

public class ShopItem
extends TabScreen {
    Command cmdTroRa;
    Command cmdHoanTatGiaoDich;
    Command cmdBatDauMua;
    public static MyItemIcon ItemIcon;
    static Vector sellItem;
    static final int maxItemBuy = 99;
    boolean isChooseAItem = false;
    static int numItemMua;
    static int tongTien;
    private boolean trans;
    byte money;
    final int XU;
    final int LUONG;
    String giaText;

    public void show(CScreen cScreen) {
        super.show(cScreen);
        this.xPaint = CScreen.w - ShopItem.ItemIcon.shopW >> 1;
        this.yPaint = (CCanvas.hieght - CScreen.cmdH) / 2 - 80;
        if (!CCanvas.isTouch) {
            this.hTabScreen = 157;
            this.n = 3;
        } else {
            this.n = 4;
            this.hTabScreen = 177;
        }
        this.title = Language.cuahang();
        if (ItemIcon != null) {
            ItemIcon.focusSelectionNow();
        }
        this.getCommand();
    }

    public ShopItem() {
        this.XU = 0;
        this.LUONG = 0;
        this.nameCScreen = " ShopItem screen!";
        this.title = Language.cuahang();
        this.getCommand();
    }

    public void getCommand() {
        this.center = new Command(Language.buy(), new IAction(){

            public void perform() {
                if (!(ShopItem.this.isChooseAItem || ShopItem.getCurI().type != 36 && ShopItem.getCurI().type != 37)) {
                    CCanvas.startYesNoDlg(Language.muavasudung(), new IAction(){

                        public void perform() {
                            ShopItem.this.isChooseAItem = true;
                            CCanvas.endDlg();
                            ShopItem.this.center.action.perform();
                        }
                    }, new IAction(){

                        public void perform() {
                            ShopItem.this.isChooseAItem = false;
                            CCanvas.endDlg();
                        }
                    });
                }
                if (!ShopItem.this.isChooseAItem) {
                    if (!(ShopItem.this.isChooseAItem || ShopItem.getCurI().isFreeItem || ShopItem.getCurI().isCannotBuy)) {
                        if (ShopItem.getCurI().num < 99) {
                            ShopItem.this.isChooseAItem = true;
                            numItemMua = 99 - ShopItem.getCurI().num < ShopItem.getCurI().nCurBuyPackage ? 99 - ShopItem.getCurI().num : (int)ShopItem.getCurI().nCurBuyPackage;
                            ShopItem.checkTongTien(ShopItem.ItemIcon.select, numItemMua);
                        } else {
                            CCanvas.startOKDlg(Language.fullItem());
                        }
                    }
                } else if (ShopItem.getCurI().price != -1 && ShopItem.getCurI().price2 != -1) {
                    Vector<Command> vector = new Vector<Command>();
                    Command command = new Command(Language.muaXu(), new IAction(){

                        public void perform() {
                            ShopItem.this.buyAChooseItem((byte)0, ShopItem.getCurI().type, (byte)numItemMua);
                        }
                    });
                    Command command2 = new Command(Language.muaLuong(), new IAction(){

                        public void perform() {
                            ShopItem.this.buyAChooseItem((byte)1, ShopItem.getCurI().type, (byte)numItemMua);
                        }
                    });
                    vector.addElement(command);
                    vector.addElement(command2);
                    CCanvas.menu.startAt(vector, 2);
                } else if (ShopItem.getCurI().price != -1) {
                    ShopItem.this.buyAChooseItem((byte)0, ShopItem.getCurI().type, (byte)numItemMua);
                } else if (ShopItem.getCurI().price2 != -1) {
                    ShopItem.this.buyAChooseItem((byte)1, ShopItem.getCurI().type, (byte)numItemMua);
                }
            }
        });
        this.right = this.cmdTroRa = new Command(Language.back(), new IAction(){

            public void perform() {
                if (ShopItem.this.isChooseAItem) {
                    ShopItem.this.isChooseAItem = false;
                } else {
                    ShopItem.this.isClose = true;
                }
            }
        });
        this.cmdHoanTatGiaoDich = new Command(Language.dathang(), new IAction(){

            public void perform() {
            }
        });
    }

    public static void setItemVector(Vector vector) {
        int n;
        sellItem = vector;
        int n2 = n = sellItem.size();
        int[] nArray = new int[n];
        for (int i = 0; i < n; ++i) {
            nArray[i] = ShopItem.getI((int)i).type;
        }
        ItemIcon = !CCanvas.isTouch ? new MyItemIcon(nArray, 4, 6, 3) : new MyItemIcon(nArray, 4, 4, 2);
        int[] nArray2 = new int[n];
        for (int i = 0; i < n2; ++i) {
            nArray2[i] = ShopItem.getI((int)i).type;
        }
        PrepareScr.prepareScrItemIcon = !CCanvas.isTouch ? new MyItemIcon(nArray2, 4, 5, 3) : new MyItemIcon(nArray2, 4, 4, 2);
    }

    public static Item getI(int n) {
        return (Item)sellItem.elementAt(n);
    }

    public static Item getCurI() {
        return (Item)sellItem.elementAt(ShopItem.ItemIcon.select);
    }

    public static void consumeOwnedItem(int n) {
        for (int i = 0; i < sellItem.size(); ++i) {
            Item item = (Item)sellItem.elementAt(i);
            if (item == null || item.type != n) continue;
            if (item.num > 0) {
                item.num = (byte)(item.num - 1);
            }
            return;
        }
    }

    public static int[] getItemNum() {
        int[] nArray = new int[sellItem.size()];
        for (int i = 0; i < sellItem.size(); ++i) {
            nArray[i] = ShopItem.getI((int)i).num;
            nArray[0] = -1;
            nArray[1] = -1;
        }
        return nArray;
    }

    public static void resetItemBuy() {
        for (int i = 0; i < sellItem.size(); ++i) {
            ShopItem.getI((int)i).numToBuy = 0;
        }
        tongTien = 0;
    }

    public void update() {
        super.update();
    }

    public void mainLoop() {
        super.mainLoop();
        ItemIcon.mainLoop();
    }

    private static boolean canAfford(byte by, byte by2, byte by3) {
        Item item = ShopItem.getI(by2);
        if (by == 0) {
            return item.price > 0 && item.price * by3 <= TerrainMidlet.myInfo.xu;
        }
        return item.price2 > 0 && item.price2 * by3 <= TerrainMidlet.myInfo.luong;
    }

    public void buyAChooseItem(byte by, byte by2, byte by3) {
        ShopItem.checkTongTien(by2, by3);
        boolean bl = ShopItem.canAfford(by, by2, by3);
        if (bl) {
            Item item = ShopItem.getCurI();
            item.numToBuy = (byte)(item.numToBuy + by3);
            item = ShopItem.getCurI();
            item.num = (byte)(item.num + by3);
            if (tongTien > 0 && this.n > 0) {
                GameService.gI().requestBuyItem(by, by2, by3);
                ShopItem.resetItemBuy();
                CCanvas.endDlg();
            }
            if (this.isChooseAItem) {
                this.isChooseAItem = false;
            }
        } else {
            CCanvas.startOKDlg(Language.kocotien());
        }
    }

    public static void checkTongTien(int n, int n2) {
        tongTien = 0;
        for (int i = 0; i < sellItem.size(); ++i) {
            int n3;
            if (i == n) {
                n3 = n2 * ShopItem.getI((int)i).price;
                if (n3 == -1) {
                    n3 = n2 * ShopItem.getI((int)i).price2;
                }
                tongTien += n3;
                continue;
            }
            if (ShopItem.getI((int)i).numToBuy <= 0) continue;
            n3 = ShopItem.getI((int)i).numToBuy * ShopItem.getI((int)i).price;
            if (n3 == -1) {
                n3 = n2 * ShopItem.getI((int)i).price2;
            }
            tongTien += n3;
        }
    }

    public static void checkItemWhenChose(int[] nArray) {
        int n;
        for (n = 0; n < sellItem.size(); ++n) {
            ShopItem.getI((int)n).numUsed = 0;
        }
        for (n = 0; n < nArray.length; ++n) {
            if (nArray[n] <= 0) continue;
            ShopItem.getI((int)nArray[n]).numUsed = (byte)(ShopItem.getI((int)nArray[n]).numUsed + 1);
        }
    }

    public static int[] checkSetItem(int[] nArray) {
        int[] nArray2 = nArray;
        if (ShopItem.getI((int)12).num > 0) {
            if (nArray[nArray.length - 4] == -1) {
                nArray[nArray.length - 4] = -2;
            }
        } else {
            nArray[nArray.length - 4] = -1;
        }
        if (ShopItem.getI((int)13).num > 0) {
            if (nArray[nArray.length - 3] == -1) {
                nArray[nArray.length - 3] = -2;
            }
        } else {
            nArray[nArray.length - 3] = -1;
        }
        if (ShopItem.getI((int)14).num > 0) {
            if (nArray[nArray.length - 2] == -1) {
                nArray[nArray.length - 2] = -2;
            }
        } else {
            nArray[nArray.length - 2] = -1;
        }
        if (ShopItem.getI((int)15).num > 0) {
            if (nArray[nArray.length - 1] == -1) {
                nArray[nArray.length - 1] = -2;
            }
        } else {
            nArray[nArray.length - 1] = -1;
        }
        for (int i = 0; i < nArray2.length; ++i) {
            if (nArray2[i] > 0 && ShopItem.getI((int)nArray2[i]).num < 1) {
                nArray2[i] = -2;
            }
            if (i == nArray2.length - 1 && ShopItem.getI((int)15).num < 1) {
                nArray2[i] = -1;
                continue;
            }
            if (i == nArray2.length - 2 && ShopItem.getI((int)14).num < 1) {
                nArray2[i] = -1;
                continue;
            }
            if (i == nArray2.length - 3 && ShopItem.getI((int)13).num < 1) {
                nArray2[i] = -1;
                continue;
            }
            if (i != nArray2.length - 4 || ShopItem.getI((int)12).num >= 1) continue;
            nArray2[i] = -1;
        }
        return nArray2;
    }

    public static void receiveAItemBuy(byte n, byte[] byArray, byte[] byArray2, int n2, int n3) {
        for (int i = 0; i < n; ++i) {
            ShopItem.getI((int)byArray[i]).num = byArray2[i];
        }
        TerrainMidlet.myInfo.xu = n2;
        TerrainMidlet.myInfo.luong = n3;
        CCanvas.startOKDlg(Language.thanks());
    }

    public void paint(mGraphics mGraphics2) {
        super.paint(mGraphics2);
        Font.normalYFont.drawString(mGraphics2, "  ", 0, 0, 0, true);
        Font.normalYFont.drawString(mGraphics2, "  ", 0, 0, 0, true);
        ShopItem.paintItem(ItemIcon, this.xPaint, this.yPaint, mGraphics2);
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint, 2, false);
        mGraphics2.setColor(2509680);
        int n = CCanvas.isTouch ? 20 : 0;
        ShopItem.paintTien(this.xPaint - 2, this.yPaint + 88 + n, mGraphics2);
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint - 5, 2, false);
        this.paintDetail(mGraphics2, n, ItemIcon.getWidth(), ItemIcon.getHeight());
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint + 2, 2, false);
        if (this.isChooseAItem && ShopItem.getCurI().type != 36 && ShopItem.getCurI().type != 37) {
            ShopItem.paintBuyBar(ShopItem.ItemIcon.select, CScreen.w - 140 >> 1, CScreen.h - 80 >> 1, mGraphics2);
        }
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint + 5, 2, false);
        ShopItem.painSeller(CScreen.w - 5, CScreen.h - 20, mGraphics2);
        Font.borderFont.drawString(mGraphics2, " ", this.xPaint, this.yPaint + 10, 2, false);
        this.paintSuper(mGraphics2);
    }

    public static void paintItem(MyItemIcon myItemIcon, int n, int n2, mGraphics mGraphics2) {
        myItemIcon.paint(n, n2 + 25, mGraphics2, true, ShopItem.getItemNum());
    }

    public void paintDetail(mGraphics mGraphics2, int n, int n2, int n3) {
        this.giaText = !ShopItem.getCurI().isFreeItem ? (ShopItem.getCurI().price != -1 ? ShopItem.getCurI().price + Language.xu() : "") + (ShopItem.getCurI().price2 != -1 ? (ShopItem.getCurI().price != -1 ? "-" : "") + ShopItem.getCurI().price2 + " " + Language.luong() : "") : Language.price() + ": " + Language.freeItem();
        mGraphics2.fillRoundRect(this.xPaint - 2, this.yPaint + 105 + n, n2 + 4, 46, 6, 7, false);
        int n4 = this.xPaint + 5;
        Font.normalYFont.drawString(mGraphics2, ShopItem.getI((int)ShopItem.ItemIcon.select).decription, n4, this.yPaint + 107 + n, 0);
        Font.normalYFont.drawString(mGraphics2, this.giaText, this.xPaint + 4, this.yPaint + 121 + n, 0);
        Font.normalYFont.drawString(mGraphics2, ShopItem.getCurI().num > 0 ? Language.having() + ": " + ShopItem.getCurI().num + " " + Language.per() : "", this.xPaint + 4, this.yPaint + 135 + n, 0);
    }

    public static void paintTileBar(byte by, int n, int n2, mGraphics mGraphics2) {
        ShopItem.paintBorderRect(mGraphics2, n2, 3, 147, "=====");
    }

    public static void paintTien(int n, int n2, mGraphics mGraphics2) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        String string = playerInfo.xu + Language.xu() + "-" + playerInfo.luong + Language.luong();
        Font.normalFont.drawString(mGraphics2, string, CCanvas.width / 2, n2 + 2, 3);
    }

    public static void paintSoluong(int n, int n2, int n3, mGraphics mGraphics2) {
    }

    public static void painSeller(int n, int n2, mGraphics mGraphics2) {
    }

    public static void paintBuyBar(int n, int n2, int n3, mGraphics mGraphics2) {
        ShopItem.paintDefaultPopup(n2 - 5, n3, 150, 75, mGraphics2);
        Font.normalFont.drawString(mGraphics2, Language.howMuch(), CCanvas.hw, n3 + 7, 2);
        ShopItem.getI(n).drawThisItem(mGraphics2, n2 + 20, n3 + 25);
        Font.normalFont.drawString(mGraphics2, numItemMua + " " + Language.per(), n2 + 70, n3 + 25, 0);
        Font.normalFont.drawString(mGraphics2, (ShopItem.getI((int)n).price != -1 ? numItemMua * ShopItem.getI((int)n).price + Language.xu() : "") + (ShopItem.getI((int)n).price2 != -1 ? (ShopItem.getI((int)n).price != -1 ? "/" : "") + numItemMua * ShopItem.getI((int)n).price2 + " luong" : ""), CCanvas.hw, n3 + 52, 2);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 4, n2 + 45 + CCanvas.gameTick % 3, n3 + 27, 0, false);
        mGraphics2.drawRegion(PrepareScr.imgReady[3], 0, 0, 13, 11, 7, n2 + 115 - CCanvas.gameTick % 3, n3 + 27, 0, false);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (!this.isChooseAItem) {
            if (ItemIcon != null) {
                ItemIcon.onPointerPressed(n, n2, n3);
            }
        } else if (CCanvas.keyPressed[4] || CCanvas.keyPressed[6]) {
            if (CCanvas.keyPressed[4] && (numItemMua -= ShopItem.getCurI().nCurBuyPackage) < ShopItem.getCurI().nCurBuyPackage) {
                numItemMua = ShopItem.getCurI().nCurBuyPackage;
            }
            if (CCanvas.keyPressed[6] && (numItemMua += ShopItem.getCurI().nCurBuyPackage) > 99 - ShopItem.getCurI().num) {
                numItemMua = 99 - ShopItem.getCurI().num;
            }
            ShopItem.checkTongTien(ShopItem.ItemIcon.select, numItemMua);
            CScreen.clearKey();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        this.trans = false;
        if (CCanvas.isPointer(0, 0, CCanvas.width, CCanvas.hieght - CScreen.cmdH, n3)) {
            int n4 = CScreen.w - 140 >> 1;
            int n5 = CScreen.h - 80 >> 1;
            if (!this.isChooseAItem) {
                if (ItemIcon != null) {
                    ItemIcon.onPointerReleased(n, n2, n3);
                }
                if (CCanvas.isDoubleClick && this.center != null) {
                    this.center.action.perform();
                }
            } else {
                if (!CCanvas.isPointer(n4 - 5, n5, 150, 75, n3)) {
                    this.isChooseAItem = false;
                    return;
                }
                if (CCanvas.isPointer(n4 + 45, n5 + 27, 40, 40, n3)) {
                    if ((numItemMua -= ShopItem.getCurI().nCurBuyPackage) < ShopItem.getCurI().nCurBuyPackage) {
                        numItemMua = ShopItem.getCurI().nCurBuyPackage;
                    }
                    ShopItem.checkTongTien(ShopItem.ItemIcon.select, numItemMua);
                }
                if (CCanvas.isPointer(n4 + 115, n5 + 27, 40, 40, n3)) {
                    if ((numItemMua += ShopItem.getCurI().nCurBuyPackage) > 99 - ShopItem.getCurI().num) {
                        numItemMua = 99 - ShopItem.getCurI().num;
                    }
                    ShopItem.checkTongTien(ShopItem.ItemIcon.select, numItemMua);
                }
            }
        }
        super.onPointerReleased(n, n2, n3);
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!this.isChooseAItem && ItemIcon != null) {
            ItemIcon.onPointerDragged(n, n2, n3);
        }
    }

    static {
        sellItem = new Vector();
    }
}

