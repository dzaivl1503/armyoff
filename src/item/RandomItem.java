/*
 * Decompiled with CFR 0.152.
 */
package item;

import CLib.mGraphics;
import CLib.mImage;
import model.CRes;
import screen.GameScr;

public class RandomItem {
    int x;
    int y;
    int w;
    int h;
    mImage img;
    static boolean visible = false;
    boolean falling = true;
    int turnCount = 0;
    int nexty;

    public RandomItem() {
        this.w = this.img.image.getWidth();
        this.h = this.img.image.getHeight();
    }

    public void newPos(int n, int n2) {
        this.x = n;
        this.y = n2;
        visible = true;
        this.falling = true;
    }

    public void update() {
        if (this.turnCount == 3) {
            if (visible) {
                visible = false;
            } else {
                this.newPos(CRes.random(300, 800), 400);
            }
            this.turnCount = 0;
        }
        if (visible && this.falling) {
            this.nexty = this.y + 5;
            while (this.y < this.nexty) {
                ++this.y;
                if (!GameScr.mm.isLand(this.x, this.y)) continue;
                this.falling = false;
                break;
            }
            if (this.y > GameScr.HEIGHT) {
                visible = false;
                this.falling = false;
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        if (visible) {
            mGraphics2.drawImage(this.img, this.x, this.y, mGraphics.BOTTOM | mGraphics.HCENTER, false);
        }
    }
}

