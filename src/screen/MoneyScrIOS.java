/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import CLib.mSystem;
import InApp.MainActivity;
import coreLG.CCanvas;
import effect.Cloud;
import java.util.Vector;
import model.AvatarInfo;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import model.MoneyInfo;
import network.Command;
import screen.CScreen;
import screen.PrepareScr;

public class MoneyScrIOS
extends CScreen {
    Vector avs;
    public int selected;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    public int xL;
    public static mImage imgCoin;
    public static String url_Nap;
    int pa = 0;
    boolean trans = false;

    public int priceFromID(int n) {
        for (int i = 0; i < this.avs.size(); ++i) {
            AvatarInfo avatarInfo = (AvatarInfo)this.avs.elementAt(i);
            if (avatarInfo.ID != n) continue;
            return avatarInfo.price;
        }
        return 0;
    }

    public void startAnimLeft() {
        this.xL = -CCanvas.width;
    }

    public MoneyScrIOS() {
        this.nameCScreen = " MoneyScrIOS screen!";
        IAction iAction = new IAction(){

            public void perform() {
                MoneyScrIOS.this.doBuy();
            }
        };
        this.center = new Command(Language.select(), iAction);
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                CCanvas.menuScr.show();
            }
        });
    }

    protected void doBuy() {
        if (this.avs != null && this.avs.size() > 0) {
            if (this.selected >= 5) {
                MoneyInfo moneyInfo = (MoneyInfo)this.avs.elementAt(this.selected);
                if (moneyInfo != null && moneyInfo.id.equals("napWeb") && !CRes.isNullOrEmpty(url_Nap)) {
                    mSystem.openUrl(url_Nap);
                }
            } else {
                MainActivity.makePurchase(MainActivity.google_productIds[this.selected]);
            }
        } else {
            CCanvas.startOKDlg("Error Inapp purchase");
        }
    }

    public MoneyInfo getSelectMoney() {
        return this.avs == null ? null : (MoneyInfo)this.avs.elementAt(this.selected);
    }

    public void showInputCard() {
    }

    public void startAnimRight() {
        this.xL = CCanvas.width << 1;
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy << 2;
            this.cmdy += this.cmvy;
            this.cmy += this.cmdy >> 4;
            this.cmdy &= 0xF;
        }
    }

    public void paint(mGraphics mGraphics2) {
        MoneyScrIOS.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        for (int i = 0; i <= CCanvas.width; i += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, i, CCanvas.hieght - 62, 0, false);
        }
        mGraphics2.translate(this.xL, 0);
        Font.bigFont.drawString(mGraphics2, Language.charge(), 10, 3, 0);
        mGraphics2.setColor(1407674);
        mGraphics2.fillRect(0, 25, CCanvas.width, ITEM_HEIGHT, false);
        Font.normalYFont.drawString(mGraphics2, Language.payMethod(), 10, 28, 0);
        this.paintRichList(mGraphics2);
        super.paint(mGraphics2);
    }

    private void paintRichList(mGraphics mGraphics2) {
        if (this.avs != null && this.avs.size() > 0) {
            mGraphics2.translate(0, ITEM_HEIGHT + 25);
            mGraphics2.translate(0, -this.cmy);
            int n = 0;
            for (int i = 0; i < this.avs.size(); ++i) {
                if (i == this.selected) {
                    mGraphics2.setColor(16765440);
                    mGraphics2.fillRect(0, n, CCanvas.width, 20, false);
                }
                MoneyInfo moneyInfo = (MoneyInfo)this.avs.elementAt(i);
                mGraphics2.drawImage(imgCoin, 10, n + 2, 0, false);
                String string = moneyInfo.info + "          " + moneyInfo.smsContent;
                Font.borderFont.drawString(mGraphics2, string, 40, n + 2, 0);
                if (!CCanvas.isTouch) {
                    n += 20;
                    continue;
                }
                n += 30;
            }
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (this.avs != null) {
            if (!this.trans) {
                this.pa = this.cmy;
                this.trans = true;
            }
            this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2);
            if (this.cmtoY < 0) {
                this.cmtoY = 0;
            }
            if (this.cmtoY > this.cmyLim) {
                this.cmtoY = this.cmyLim;
            }
            if (this.selected >= this.avs.size() - 1 || this.selected == 0) {
                this.cmy = this.cmtoY;
            }
        }
    }

    public void onPointerHold(int n, int n2, int n3) {
        super.onPointerHold(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        if (this.avs != null) {
            if (CCanvas.isPointerDown[n3]) {
                if (!this.trans) {
                    this.pa = this.cmy;
                    this.trans = true;
                }
                this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2);
                if (this.cmtoY < 0) {
                    this.cmtoY = 0;
                }
                if (this.cmtoY > this.cmyLim) {
                    this.cmtoY = this.cmyLim;
                }
                if (this.selected >= this.avs.size() - 1 || this.selected == 0) {
                    this.cmy = this.cmtoY;
                }
            }
            this.trans = false;
            int n4 = (this.cmtoY + n2 - ITEM_HEIGHT - 25) / 30;
            if (n4 == this.selected && CCanvas.isDoubleClick) {
                this.center.action.perform();
            }
            this.selected = n4;
            if (this.selected < 0) {
                this.selected = 0;
            }
            if (this.selected > this.avs.size() - 1) {
                this.selected = this.avs.size() - 1;
            }
        }
    }

    public void update() {
        if (this.xL != 0) {
            this.xL += -this.xL >> 1;
        }
        if (this.xL == -1) {
            this.xL = 0;
        }
        this.moveCamera();
        Cloud.updateCloud();
    }

    public void setAvatarList(Vector vector) {
        this.avs = vector;
        this.selected = 0;
        this.cmtoY = 0;
        this.cmy = 0;
        this.cmyLim = vector.size() * 20 - (CCanvas.hh - 40);
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
    }

    static {
        try {
            imgCoin = mImage.createImage("/coin.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

