/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;
import model.CRes;
import model.Dialog;
import model.Font;
import model.IAction;
import model.Language;
import model.TField;
import network.Command;
import screen.CScreen;

public class InputDlg
extends Dialog {
    protected String[] info;
    public TField tfInput;
    public IAction okAction;
    public IAction backAction;
    int dis = CCanvas.isTouch ? 15 : 0;

    public InputDlg() {
        this.tfInput = new TField();
        this.tfInput.x = 20;
        this.tfInput.y = CCanvas.hieght - CScreen.ITEM_HEIGHT - 48 - this.dis;
        this.tfInput.width = CCanvas.width - 40;
        this.tfInput.height = CScreen.ITEM_HEIGHT + 2;
        this.tfInput.setisFocus(true);
        this.tfInput.setisVisible(false);
        this.right = this.tfInput.cmdClear;
    }

    public void setInfo(String string, IAction iAction, IAction iAction2, int n) {
        CRes.out("InputDlg ===============> setInfo " + (iAction != null) + "_" + (iAction2 != null) + "_type = " + n);
        this.tfInput.resetTextBox();
        this.tfInput.setMaxTextLenght(500);
        this.tfInput.setisVisible(true);
        this.tfInput.setText("");
        this.tfInput.setIputType(n);
        this.info = Font.normalFont.splitFontBStrInLine(string, CCanvas.width - 40);
        this.okAction = iAction;
        this.backAction = iAction2 != null ? iAction2 : new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        };
        this.left = new Command(Language.close(), this.backAction);
        this.center = new Command("OK", this.okAction);
    }

    public void update() {
        super.update();
        if (this.tfInput != null) {
            this.tfInput.update();
        }
    }

    public void paint(mGraphics mGraphics2) {
        super.paint(mGraphics2);
        CScreen.paintDefaultPopup(8, CCanvas.hieght - 102 - this.dis, CCanvas.width - 16, 69, mGraphics2);
        int n = CCanvas.hieght - 35 - 50 - (this.info.length >> 1) * Font.normalFont.getHeight();
        int n2 = 0;
        int n3 = n;
        while (n2 < this.info.length) {
            Font.normalFont.drawString(mGraphics2, this.info[n2], CCanvas.hw, n3 - this.dis, 2);
            ++n2;
            n3 += Font.normalFont.getHeight();
        }
        super.paint(mGraphics2);
        this.tfInput.paint(mGraphics2);
    }

    public void keyPress(int n) {
        if (CCanvas.keyPressed[5] || n == 10 || n == -5) {
            CCanvas.keyPressed[5] = false;
            if (this.center != null && this.center.action != null) {
                this.center.action.perform();
            }
            return;
        }
        if (CCanvas.keyPressed[12] || n == -6 || n == -21) {
            CCanvas.keyPressed[12] = false;
            if (this.left != null && this.left.action != null) {
                this.left.action.perform();
            }
            return;
        }
        if (CCanvas.keyPressed[13] || n == -7 || n == -22) {
            CCanvas.keyPressed[13] = false;
            if (this.right != null && this.right.action != null) {
                this.right.action.perform();
            }
            return;
        }
        this.tfInput.keyPressed(n);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (CCanvas.isPointer(this.tfInput.x, this.tfInput.y, this.tfInput.width, this.tfInput.height, n3)) {
            this.tfInput.setisFocus(true);
            if (CCanvas.isTouch) {
                this.tfInput.doChangeToTextBox();
            }
            return;
        }
        if (CCanvas.isPointer(CCanvas.width / 2 - 40, CCanvas.hieght - 40 - this.dis, 80, 40, n3)) {
            if (this.center != null && this.center.action != null) {
                this.center.action.perform();
            }
            return;
        }
        if (CCanvas.isPointer(5, CCanvas.hieght - 40 - this.dis, 60, 40, n3)) {
            if (this.left != null && this.left.action != null) {
                this.left.action.perform();
            }
            return;
        }
        if (CCanvas.isPointer(CCanvas.width - 65, CCanvas.hieght - 40 - this.dis, 60, 40, n3)) {
            if (this.right != null && this.right.action != null) {
                this.right.action.perform();
            }
            return;
        }
    }

    public void show() {
        CCanvas.currentDialog = this;
    }

    public void close() {
        this.tfInput.setisVisible(false);
        CRes.out("===========================> close InputDialog");
    }
}

