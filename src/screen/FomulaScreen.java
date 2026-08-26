/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import coreLG.CCanvas;
import java.util.Vector;
import model.Fomula;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import network.GameService;
import screen.CScreen;

public class FomulaScreen
extends CScreen {
    public Vector fomulas = new Vector();
    public int select = 0;
    public CScreen lastScr;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    int pa = 0;
    boolean trans = false;

    public void setFomula(Fomula fomula) {
        this.fomulas.addElement(fomula);
    }

    public void show() {
    }

    public void show(CScreen cScreen) {
        this.nameCScreen = "FomulaScreen screen!";
        this.lastScr = cScreen;
        this.commandInit();
        super.show();
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy << 2;
            this.cmdy += this.cmvy;
            this.cmy += this.cmdy >> 4;
            this.cmdy &= 0xF;
        }
    }

    public void input() {
    }

    public void commandInit() {
        this.right = new Command(Language.back(), new IAction(){

            public void perform() {
                FomulaScreen.this.lastScr.show();
            }
        });
        this.left = new Command("Menu", new IAction(){

            public void perform() {
                Vector<Command> vector = new Vector<Command>();
                for (int i = 0; i < FomulaScreen.this.fomulas.size(); ++i) {
                    final int n = i;
                    vector.addElement(new Command(Language.congthuccap() + " " + (i + 1), new IAction(){

                        public void perform() {
                            FomulaScreen.this.select = n;
                            FomulaScreen.this.commandInit();
                        }
                    }));
                }
                CCanvas.menu.startAt(vector, 0);
            }
        });
        this.center = new Command(Language.chedo(), new IAction(){

            public void perform() {
                Fomula fomula = (Fomula)FomulaScreen.this.fomulas.elementAt(FomulaScreen.this.select);
                GameService.gI().getFomula((byte)fomula.ID, (byte)2, (byte)FomulaScreen.this.select);
            }
        });
    }

    public void paint(mGraphics mGraphics2) {
        FomulaScreen.paintDefaultBg(mGraphics2);
        mGraphics2.translate(0, -this.cmy);
        ((Fomula)this.fomulas.elementAt(this.select)).paint(mGraphics2);
        mGraphics2.translate(0, this.cmy);
        super.paint(mGraphics2);
        if (this.cmyLim > 0) {
            Font.borderFont.drawString(mGraphics2, "(giu phim len/xuong de xem them)", CCanvas.width / 2, CCanvas.hieght - cmdH - 12, 3);
        }
    }

    public void update() {
        if (CCanvas.keyPressed[4] && this.select > 0) {
            --this.select;
            this.commandInit();
            this.cmtoY = 0;
            this.cmy = 0;
            CScreen.clearKey();
        }
        if (CCanvas.keyPressed[6] && this.select < this.fomulas.size() - 1) {
            ++this.select;
            this.commandInit();
            this.cmtoY = 0;
            this.cmy = 0;
            CScreen.clearKey();
        }
        Fomula fomula = (Fomula)this.fomulas.elementAt(this.select);
        this.cmyLim = fomula.contentBottomY() - (CCanvas.hieght - cmdH - 25);
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
        int n = 6;
        if (CCanvas.keyHold[2]) {
            this.cmtoY -= n;
        }
        if (CCanvas.keyHold[8]) {
            this.cmtoY += n;
        }
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
        this.moveCamera();
        fomula.update();
    }

    public Fomula getFomula(int n) {
        return (Fomula)this.fomulas.elementAt(n);
    }
}

