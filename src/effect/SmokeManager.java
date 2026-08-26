/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.mGraphics;
import effect.Smoke;
import java.util.Vector;
import model.CRes;
import screen.GameScr;

public class SmokeManager {
    public Vector smokes = new Vector();
    public boolean notPaint = false;

    public void addSmoke(int n, int n2, byte by) {
        int n3 = 300;
        if (GameScr.curGRAPHIC_LEVEL == 1) {
            n3 = 20;
        }
        if (GameScr.curGRAPHIC_LEVEL == 2) {
            n3 = 0;
        }
        if (this.smokes.size() < (by != 7 && by != 13 && by != 19 && by != 21 ? 30 : n3)) {
            if (by == 4) {
                if (CRes.random(2) == 0) {
                    this.smokes.addElement(new Smoke(n, n2, by));
                }
            } else {
                this.smokes.addElement(new Smoke(n, n2, by));
            }
        }
    }

    public void addLazer(int n, int n2, int n3, int n4, int n5) {
        this.smokes.addElement(new Smoke(n, n2, n3, n4, n5));
    }

    public void addBat(int n, int n2, int n3, int n4) {
        this.smokes.addElement(new Smoke(n, n2, n3, n4));
    }

    public void addRock(int n, int n2, int n3, int n4, byte by) {
        int n5 = 100;
        if (GameScr.curGRAPHIC_LEVEL == 1) {
            n5 = 20;
        } else if (GameScr.curGRAPHIC_LEVEL == 2) {
            n5 = 10;
        }
        if (by == 10) {
            this.smokes.addElement(new Smoke(n, n2, n3, n4, by));
        } else if (by != 6 && by != 8 && by != 9 && by != 12) {
            if (this.smokes.size() < n5) {
                this.smokes.addElement(new Smoke(n, n2, n3, n4, by));
            }
        } else if (this.smokes.size() < n5) {
            this.smokes.addElement(new Smoke(n, n2, n3, n4, by));
        }
    }

    public void update() {
        for (int i = 0; i < this.smokes.size(); ++i) {
            ((Smoke)this.smokes.elementAt(i)).update();
        }
    }

    public void paint(mGraphics mGraphics2) {
        if (!this.notPaint) {
            for (int i = 0; i < this.smokes.size(); ++i) {
                ((Smoke)this.smokes.elementAt(i)).paint(mGraphics2);
            }
        }
    }

    public void removeSmoke(Object object) {
        this.smokes.removeElement(object);
    }

    public void onClearMap() {
    }
}

