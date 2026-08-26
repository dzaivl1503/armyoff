/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;
import java.util.Vector;
import model.CRes;
import model.Font;
import model.Language;
import network.Command;
import screen.CScreen;

public class Menu {
    public boolean showMenu;
    public Vector menuItems;
    public int menuSelectedItem;
    public int menuX;
    public int menuY;
    public int menuW;
    public int menuH;
    public int menuTemY;
    public static int cmtoY;
    public static int cmy;
    public static int cmdy;
    public static int cmvy;
    public static int cmyLim;
    public static int xc;
    public int dis;
    int pa = 0;
    boolean trans = false;

    public void startAt(Vector vector, int n) {
        int n2;
        int n3;
        this.menuItems = vector;
        this.dis = CCanvas.isTouch ? 10 : 0;
        this.menuW = 0;
        this.menuH = 0;
        for (n3 = 0; n3 < vector.size(); ++n3) {
            Command command = (Command)vector.elementAt(n3);
            int n4 = Font.normalFont.getWidth(command.caption);
            if (n4 > this.menuW) {
                this.menuW = n4;
            }
            this.menuH += CScreen.ITEM_HEIGHT + this.dis;
        }
        this.menuW += 20;
        if (this.menuW < 115) {
            this.menuW = 115;
        }
        this.menuH += 4;
        this.menuX = n == 0 ? 2 : (n == 1 ? CCanvas.width - this.menuW - 2 : (CCanvas.width >> 1) - (this.menuW >> 1));
        this.menuY = CCanvas.hieght - 21 - this.menuH - 6 - this.dis;
        this.menuTemY = CCanvas.hieght - (CScreen.ITEM_HEIGHT + this.dis);
        n3 = (CCanvas.hieght - CScreen.cmdH - 10) / (CScreen.ITEM_HEIGHT + this.dis);
        int n5 = n2 = CCanvas.isTouch ? n3 : 7;
        if (vector.size() > n2) {
            this.menuY = CCanvas.hieght - (CScreen.ITEM_HEIGHT + this.dis) * n2 - 31 - this.dis;
            this.menuH = (CScreen.ITEM_HEIGHT + this.dis) * n2 + 4;
        }
        if (CCanvas.hieght < 200 && !CCanvas.isTouch) {
            this.menuY += 10;
        }
        this.showMenu = true;
        this.menuSelectedItem = 0;
        cmyLim = this.menuItems.size() * (CScreen.ITEM_HEIGHT + this.dis) - (CScreen.ITEM_HEIGHT + this.dis) * n2;
        if (cmyLim < 0) {
            cmyLim = 0;
        }
        cmtoY = 0;
        cmy = 0;
    }

    public void moveCamera() {
        if (cmy != cmtoY) {
            cmvy = cmtoY - cmy << 2;
            cmy += (cmdy += cmvy) >> 4;
            cmdy &= 0xF;
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (!CCanvas.keyPressed[2] && !CScreen.keyUp) {
            if (!CCanvas.keyPressed[8] && !CScreen.keyDown) {
                if (!(CCanvas.keyPressed[5] || CCanvas.keyPressed[12] || CScreen.getCmdPointerPressed((byte)2, n3, true) || CScreen.getCmdPointerPressed((byte)0, n3, true))) {
                    if (CCanvas.keyPressed[13] || CScreen.getCmdPointerPressed((byte)1, n3, true)) {
                        this.showMenu = false;
                    }
                } else {
                    this.showMenu = false;
                    if (this.menuItems.size() > 0) {
                        ((Command)this.menuItems.elementAt((int)this.menuSelectedItem)).action.perform();
                    }
                }
            } else {
                ++this.menuSelectedItem;
                if (this.menuSelectedItem > this.menuItems.size() - 1) {
                    this.menuSelectedItem = 0;
                }
                cmtoY = this.menuSelectedItem * (CScreen.ITEM_HEIGHT + this.dis) - 2 * (CScreen.ITEM_HEIGHT + this.dis);
            }
        } else {
            --this.menuSelectedItem;
            if (this.menuSelectedItem < 0) {
                this.menuSelectedItem = this.menuItems.size() - 1;
            }
            cmtoY = this.menuSelectedItem * (CScreen.ITEM_HEIGHT + this.dis) - 2 * (CScreen.ITEM_HEIGHT + this.dis);
        }
        if (cmtoY > cmyLim) {
            cmtoY = cmyLim;
        }
        if (cmtoY < 0) {
            cmtoY = 0;
        }
        this.trans = false;
        CScreen.clearKey();
    }

    public void onPointerReleased(int n, int n2, int n3) {
        this.trans = false;
        if (CCanvas.isTouchOnGamePad(n, n2)) {
            return;
        }
        if (CCanvas.isPointer(this.menuX, this.menuY, this.menuW, this.menuH, n3)) {
            this.trans = false;
            int n4 = (n2 - this.menuTemY + cmtoY) / (CScreen.ITEM_HEIGHT + this.dis);
            if (this.menuSelectedItem != n4 && CCanvas.isTouch) {
                this.menuSelectedItem = n4;
            } else {
                this.menuSelectedItem = n4;
                try {
                    this.showMenu = false;
                    ((Command)this.menuItems.elementAt((int)this.menuSelectedItem)).action.perform();
                }
                catch (Exception exception) {
                    CRes.out("=====> onpointer is over list");
                }
            }
        } else {
            this.showMenu = false;
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        if (!this.trans) {
            this.pa = cmy;
            this.trans = true;
        }
        if ((cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2)) > cmyLim) {
            cmtoY = cmyLim;
        }
        if (cmtoY < 0) {
            cmtoY = 0;
        }
        if (this.menuSelectedItem == this.menuItems.size() - 1 || this.menuSelectedItem == 0) {
            cmy = cmtoY;
        }
    }

    public void updateMenuKey() {
    }

    public static void paintDefaultPopup(mGraphics mGraphics2) {
        mGraphics2.setColor(14279153);
        mGraphics2.fillRoundRect(8, CCanvas.hieght - 102, CCanvas.width - 16, 69, 6, 6, false);
        mGraphics2.setColor(4682453);
        mGraphics2.fillRect(10, CCanvas.hieght - 100, CCanvas.width - 20, 65, false);
    }

    public void paintMenu(mGraphics mGraphics2) {
        int n;
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        CScreen.paintDefaultPopup(this.menuX - 2, this.menuY - 6, this.menuW, this.menuH + 12, mGraphics2);
        mGraphics2.translate(this.menuX + 5, this.menuTemY + 2);
        mGraphics2.setClip(-5, 0, this.menuW, this.menuH);
        mGraphics2.translate(0, -cmy);
        int n2 = CCanvas.isTouch ? 5 : 0;
        for (n = 0; n < this.menuItems.size(); ++n) {
            mGraphics2.setColor(0);
            if (n == this.menuSelectedItem && this.menuY == this.menuTemY) {
                mGraphics2.setColor(16767817);
                if (!CCanvas.isTouch) {
                    mGraphics2.fillRect(0, n * (CScreen.ITEM_HEIGHT + this.dis) + n2, this.menuW - 14, CScreen.ITEM_HEIGHT, false);
                } else {
                    mGraphics2.fillRect(0, n * (CScreen.ITEM_HEIGHT + this.dis) + n2 - 4, this.menuW - 14, CScreen.ITEM_HEIGHT + 8, false);
                }
            }
            if (this.menuY != this.menuTemY) continue;
            Font.normalFont.drawString(mGraphics2, ((Command)this.menuItems.elementAt((int)n)).caption, 5, 3 + n * (CScreen.ITEM_HEIGHT + this.dis) + n2, 0);
        }
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        mGraphics2.setClip(-1000, -1000, 2000, 2000);
        n = CCanvas.hieght - CScreen.cmdH / 2 - Font.normalFont.getHeight() / 2;
        Font.normalFont.drawString(mGraphics2, Language.select(), 5, n, 0);
        Font.normalFont.drawString(mGraphics2, Language.no(), CCanvas.width - 5, n, 1);
    }

    public void update() {
        this.moveCamera();
        if (this.menuTemY > this.menuY) {
            int n = this.menuTemY - this.menuY >> 1;
            if (n < 1) {
                n = 1;
            }
            this.menuTemY -= n;
        }
        this.menuTemY = this.menuY;
        if (Math.abs(cmtoY - cmy) < 15 && cmy < 0) {
            cmtoY = 0;
        }
        if (Math.abs(cmtoY - cmy) < 10 && cmy > cmyLim) {
            cmtoY = cmyLim;
        }
    }

    public void mainLoop() {
        this.moveCamera();
        if (this.menuTemY > this.menuY) {
            int n = this.menuTemY - this.menuY >> 1;
            if (n < 1) {
                n = 1;
            }
            this.menuTemY -= n;
        }
        this.menuTemY = this.menuY;
        if (Math.abs(cmtoY - cmy) < 15 && cmy < 0) {
            cmtoY = 0;
        }
        if (Math.abs(cmtoY - cmy) < 10 && cmy > cmyLim) {
            cmtoY = cmyLim;
        }
    }
}

