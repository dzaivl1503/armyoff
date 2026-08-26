/*
 * Decompiled with CFR 0.152.
 */
package item;

import CLib.mGraphics;
import coreLG.CCanvas;
import item.Item;
import model.CRes;
import model.Font;
import model.IAction;
import screen.CScreen;
import screen.PrepareScr;

public class MyItemIcon {
    int[] icon;
    public int select;
    int blank;
    int hBlank;
    int indexW;
    int indexH;
    int rangeCheck;
    public int shopW;
    int shopH;
    int size;
    int fullSize;
    int num;
    public static final byte UP = 0;
    public static final byte DOWN = 1;
    public static final byte LEFT = 2;
    public static final byte RIGHT = 3;
    public boolean isSetClip;
    int wP = 4;
    int line;
    private IAction _iAction;
    public String titleTem;
    int pa = 0;
    boolean trans = false;
    static int cmtoY;
    static int cmy;
    static int cmdy;
    static int cmvy;
    int x;
    int y;
    private int xTitle;
    private int yTitle;

    public MyItemIcon(int[] nArray, int n, int n2, int n3) {
        this.icon = nArray;
        this.blank = n;
        this.init();
        this.indexW = n2;
        this.indexH = this.icon.length / this.indexW;
        if (nArray.length % this.indexW != 0) {
            ++this.indexH;
        }
        this.shopW = this.indexW * (this.blank + this.size);
        this.shopH = n3 * (this.blank + this.size);
        if (CCanvas.isTouch) {
            this.shopH -= 2;
        }
        this.rangeCheck = (this.blank + this.size) * 2;
        this.isSetClip = true;
    }

    public MyItemIcon(int[] nArray, int n, int n2, int n3, boolean bl) {
        this.icon = nArray;
        this.blank = n;
        this.init();
        this.indexW = n2;
        this.indexH = n3;
        this.isSetClip = false;
    }

    void init() {
        this.num = this.icon.length;
        this.hBlank = this.blank / 2;
        if (!CCanvas.isTouch) {
            this.size = 16;
            this.wP = 0;
        } else {
            this.size = 37;
            this.wP = 9;
        }
        this.fullSize = this.blank + this.size;
    }

    public void setIAction(IAction iAction) {
        this._iAction = iAction;
    }

    public void checkCmtoY(int n) {
        int n2 = n / this.indexW * this.fullSize;
        if (n2 < cmy) {
            cmtoY = n2;
        } else if (n2 + this.fullSize > cmy + this.shopH) {
            cmtoY = n2 + this.fullSize - this.shopH;
        }
        int n3 = this.indexH * this.fullSize - this.shopH;
        if (n3 < 0) {
            n3 = 0;
        }
        if (cmtoY > n3) {
            cmtoY = n3;
        }
        if (cmtoY < 0) {
            cmtoY = 0;
        }
    }

    public void focusSelectionNow() {
        this.checkCmtoY(this.select);
        cmy = cmtoY;
        cmdy = 0;
        cmvy = 0;
    }

    public void moveCamera() {
        if (cmy != cmtoY) {
            cmvy = cmtoY - cmy << 2;
            cmy += (cmdy += cmvy) >> 4;
            cmdy &= 0xF;
        }
    }

    public void update() {
    }

    public void mainLoop() {
        this.moveCamera();
    }

    public void paint(int n, int n2, mGraphics mGraphics2, boolean bl, int[] nArray) {
        this.x = n;
        this.y = n2;
        if (this.isSetClip) {
            mGraphics2.setClip(n - 2, n2 - 2, this.shopW + 4, this.shopH + 18);
            mGraphics2.setColor(4156571);
            mGraphics2.fillRoundRect(n - 2, n2 - 2, this.shopW + 4, this.shopH + 6, 6, 7, true);
            mGraphics2.translate(0, -cmy);
        }
        int n3 = 0;
        int n4 = 0;
        int n5 = 0;
        int n6 = 0;
        if (CCanvas.isTouch) {
            mGraphics2.setColor(16767817);
        } else {
            mGraphics2.setColor(CCanvas.gameTick % 5 > 2 ? 0xFFFFFF : 0);
        }
        int n7 = CCanvas.isTouch ? 37 + this.blank : this.fullSize;
        for (int i = 0; i < this.num; ++i) {
            n5 = n + n4 * n7 + this.hBlank;
            n6 = n2 + n3 * n7 + this.hBlank;
            mGraphics2.setClip(n, n2 + cmy, this.shopW, this.shopH);
            if (this.select == i) {
                mGraphics2.fillRect(n5 - (CCanvas.isTouch ? 2 : 1), n6 - (CCanvas.isTouch ? 2 : 1), this.size + this.hBlank, this.size + this.hBlank, true);
            }
            Item.DrawItem(mGraphics2, this.icon[i], n5 + this.wP, n6 + this.wP);
            if (bl && nArray[i] >= 0 && i <= nArray.length - 1) {
                Font.smallFontYellow.drawString(mGraphics2, String.valueOf(nArray[i]), n5 + 9 + this.wP, n6 + 10 + this.wP, 0);
            }
            if (++n4 <= this.indexW - 1) continue;
            n4 = 0;
            ++n3;
        }
        if (this.isSetClip) {
            mGraphics2.translate(0, cmy);
            mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        }
        if (!CRes.isNullOrEmpty(this.titleTem)) {
            Font.borderFont.drawString(mGraphics2, this.titleTem, this.xTitle, this.yTitle, 2, false);
        }
    }

    public void setPosTitle(int n, int n2) {
        this.xTitle = n;
        this.yTitle = n2;
    }

    public int getIndexW() {
        return this.indexW;
    }

    public void onPointerDragged(int n, int n2, int n3) {
        int n4 = this.fullSize;
        if (CCanvas.isPointer(this.x, this.y, this.shopW + 4, this.shopH + 6, n3)) {
            if (!this.trans) {
                this.pa = cmy;
                this.trans = true;
            }
            if ((cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2) + 2) < 0) {
                cmtoY = 0;
            }
            if (cmtoY > this.indexH * n4 - this.shopH) {
                cmtoY = this.indexH * n4 - this.shopH;
            }
            if (cmtoY < 0) {
                cmtoY = 0;
            }
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
            if (CCanvas.keyPressed[2]) {
                this.select -= this.indexW;
            }
            if (CCanvas.keyPressed[8]) {
                this.select += this.indexW;
            }
            if (CCanvas.keyPressed[4]) {
                --this.select;
            }
            if (CCanvas.keyPressed[6]) {
                ++this.select;
            }
            if (this.select > this.icon.length - 1) {
                this.select = 0;
            }
            if (this.select < 0) {
                this.select = this.icon.length - 1;
            }
            this.checkCmtoY(this.select);
            CScreen.clearKey();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        this.trans = false;
        int n4 = this.fullSize;
        if (CCanvas.isPointer(this.x, this.y, this.shopW + 4, this.shopH + 6, n3)) {
            int n5 = (cmtoY + n2 - this.y - this.hBlank) / n4 * this.indexW + (n - this.x - this.hBlank) / n4;
            if (n5 == this.select) {
                if (CCanvas.curScr == CCanvas.shopItemScr && CCanvas.shopItemScr.left != null && CCanvas.isDoubleClick) {
                    CCanvas.shopItemScr.left.action.perform();
                }
                if (CCanvas.curScr == CCanvas.prepareScr && CCanvas.isDoubleClick && this._iAction != null) {
                    this._iAction.perform();
                }
            }
            if (n5 >= 0 && n5 < this.icon.length) {
                this.select = n5;
                this.checkCmtoY(this.select);
            }
        } else if (CCanvas.curScr == CCanvas.prepareScr) {
            PrepareScr prepareScr = CCanvas.prepareScr;
        }
    }

    public void onPointerHolder(int n, int n2, int n3) {
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return this.shopW;
    }

    public int getHeight() {
        return this.shopH;
    }

    public void close() {
    }

    public void resetTranslate(mGraphics mGraphics2) {
        mGraphics2.translate(0, cmy);
    }
}

