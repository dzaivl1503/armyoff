/*
 * Decompiled with CFR 0.152.
 */
package shop;

import CLib.mGraphics;
import CLib.mSystem;
import coreLG.CCanvas;
import effect.Cloud;
import java.util.Vector;
import model.ClanItem;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.PrepareScr;

public class ShopBietDoi
extends CScreen {
    private long currentTimeClick;
    public Vector items;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    int selected;
    private static final int LIST_TOP = 30;
    private static final int ROW_HEIGHT = 54;
    int disY = 54;
    boolean isPaintItemLish;
    int pa = 0;
    boolean trans = false;

    public ClanItem getCurrItem() {
        return (ClanItem)this.items.elementAt(this.selected);
    }

    public ClanItem getClanItem(byte by) {
        for (int i = 0; i < this.items.size(); ++i) {
            ClanItem clanItem = (ClanItem)this.items.elementAt(i);
            if (clanItem.id != by) continue;
            return clanItem;
        }
        return null;
    }

    public void initCommand() {
        final Command command = new Command(Language.muaXu(), new IAction(){

            public void perform() {
                CCanvas.startYesNoDlg(Language.areYouSure(), new IAction(){

                    public void perform() {
                        if (ShopBietDoi.this.getCurrItem() != null) {
                            GameService.gI().getShopBietDoi((byte)1, (byte)0, ShopBietDoi.this.getCurrItem().id);
                        }
                    }
                });
            }
        });
        final Command command2 = new Command(Language.muaLuong(), new IAction(){

            public void perform() {
                CCanvas.startYesNoDlg(Language.areYouSure(), new IAction(){

                    public void perform() {
                        if (ShopBietDoi.this.getCurrItem() != null) {
                            GameService.gI().getShopBietDoi((byte)1, (byte)1, ShopBietDoi.this.getCurrItem().id);
                        }
                    }
                });
            }
        });
        if (this.getCurrItem() != null && this.getCurrItem().xu != -1 && this.getCurrItem().luong != -1) {
            this.center = new Command("Menu", new IAction(){

                public void perform() {
                    Vector<Command> vector = new Vector<Command>();
                    vector.addElement(command);
                    vector.addElement(command2);
                    CCanvas.menu.startAt(vector, 2);
                }
            });
        } else {
            if (this.getCurrItem().xu != -1) {
                this.center = command;
            }
            if (this.getCurrItem().luong != -1) {
                this.center = command2;
            }
        }
        this.right = new Command(Language.back(), new IAction(){

            public void perform() {
                CCanvas.menuScr.show();
            }
        });
    }

    public void setItems(Vector vector) {
        this.items = vector;
        this.selected = 0;
        this.cmy = 0;
        this.cmtoY = 0;
        int n = CCanvas.hieght - 30 - cmdH;
        this.cmyLim = vector.size() * 54 - n;
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
    }

    public void show() {
        this.nameCScreen = " ShopBietDoi screen!";
        super.show();
        this.initCommand();
        CCanvas.endDlg();
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        ShopBietDoi.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        for (int i = 0; i <= CCanvas.width; i += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, i, CCanvas.hieght - 62, 0, true);
        }
        Font.bigFont.drawString(mGraphics2, Language.ITEM_DOI(), 10, 3, 0);
        this.paintItems(mGraphics2);
        super.paint(mGraphics2);
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy >> 2;
            this.cmy += this.cmvy;
        }
    }

    public void paintItems(mGraphics mGraphics2) {
        mGraphics2.translate(0, 30);
        mGraphics2.translate(0, -this.cmy);
        mGraphics2.setClip(0, this.cmy, CCanvas.width, CCanvas.hieght - 30 - cmdH);
        int n = 0;
        for (int i = 0; i < this.items.size(); ++i) {
            if (i == this.selected) {
                mGraphics2.setColor(16765440);
                mGraphics2.fillRect(0, n, CCanvas.width, 53, true);
            }
            if (i * 54 + 54 > this.cmy && i * 54 < this.cmy + CCanvas.hieght - 30 - cmdH) {
                ClanItem clanItem = (ClanItem)this.items.elementAt(i);
                String string = clanItem.name;
                Font.borderFont.drawString(mGraphics2, string, 5, n + 1, 0);
                String string2 = Language.price() + ": ";
                if (clanItem.xu != -1 && clanItem.luong != -1) {
                    string2 = string2 + clanItem.xu + Language.xu() + " - " + clanItem.luong + " " + Language.luong();
                } else {
                    if (clanItem.xu != -1) {
                        string2 = string2 + clanItem.xu + Language.xu();
                    }
                    if (clanItem.luong != -1) {
                        string2 = string2 + clanItem.luong + Language.luong();
                    }
                }
                Font.normalFont.drawString(mGraphics2, string2, 5, n + 19, 0);
                Font.normalFont.drawString(mGraphics2, Language.time() + ": " + clanItem.expDate + " " + Language.gio(), 5, n + 36, 0);
                Font.normalFont.drawString(mGraphics2, "Lv." + clanItem.levelRequire, CCanvas.width - 5, n + 36, 1);
            }
            n += 54;
        }
        mGraphics2.translate(0, -mGraphics2.getTranslateY());
        mGraphics2.resetClip();
    }

    public void update() {
        Cloud.updateCloud();
    }

    public void mainLoop() {
        super.mainLoop();
        this.moveCamera();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (this.items == null || this.items.size() == 0) {
            return;
        }
        if (CCanvas.keyPressed[2]) {
            --this.selected;
        } else if (CCanvas.keyPressed[8]) {
            ++this.selected;
        } else {
            return;
        }
        if (this.selected < 0) {
            this.selected = this.items.size() - 1;
        }
        if (this.selected >= this.items.size()) {
            this.selected = 0;
        }
        this.cmtoY = this.selected * this.disY - this.disY;
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
        this.initCommand();
        CScreen.clearKey();
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.trans = false;
        if (CCanvas.isPointer(0, 30, w, CCanvas.hieght - 30 - cmdH, n3)) {
            int n4 = (this.cmy + n2 - 30) / this.disY;
            if (n4 == this.selected && mSystem.currentTimeMillis() - this.currentTimeClick > 100L) {
                if (this.center != null) {
                    if (CCanvas.isDoubleClick) {
                        this.center.action.perform();
                    }
                } else if (this.left != null && CCanvas.isDoubleClick) {
                    this.left.action.perform();
                }
            }
            this.selected = n4;
            if (this.selected < 0) {
                this.selected = 0;
            }
            if (this.selected >= this.items.size()) {
                this.selected = this.items.size() - 1;
            }
            this.initCommand();
        }
        this.cmtoY = this.cmy;
        this.currentTimeClick = mSystem.currentTimeMillis();
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        this.currentTimeClick = mSystem.currentTimeMillis();
        if (!this.trans) {
            this.pa = this.cmy;
            this.trans = true;
        }
        this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2);
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
    }
}

