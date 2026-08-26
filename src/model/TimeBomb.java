/*
 * Decompiled with CFR 0.152.
 */
package model;

import CLib.mGraphics;
import coreLG.CCanvas;
import effect.Camera;
import effect.Explosion;
import item.Bullet;
import screen.GameScr;

public class TimeBomb {
    public int id;
    public int x;
    public int y;
    public boolean isExplore;
    public boolean isFall;
    public boolean falling;
    int dy;

    public TimeBomb(int n, int n2, int n3) {
        this.id = n;
        this.x = n2;
        this.y = n3;
        new Explosion(n2, n3, 1);
    }

    public void paint(mGraphics mGraphics2) {
        int n = CCanvas.gameTick % 3 == 0 ? 0 : 1;
        mGraphics2.drawRegion(Explosion.timeBomb, 0, n * 15, 28, 15, 0, this.x, this.y, mGraphics.HCENTER | mGraphics.BOTTOM, false);
    }

    public void update() {
        int n;
        if (this.isExplore) {
            Camera.shaking = 2;
            int[][] nArray = GameScr.getPointAround(this.x, this.y, 7);
            for (n = 0; n < 7; ++n) {
                new Explosion(nArray[0][n], nArray[1][n], 7);
            }
            GameScr.timeBombs.removeElement(this);
        }
        if (this.isFall) {
            this.falling = true;
            for (int i = 0; i < 14; ++i) {
                if (!GameScr.mm.isLand(this.x - 7 + i, this.y)) continue;
                this.falling = false;
                this.dy = 0;
                break;
            }
            this.isFall = false;
        }
        if (this.falling) {
            int n2 = this.x;
            n = this.y;
            ++this.dy;
            this.y += this.dy;
            for (int i = 0; i < 14; ++i) {
                if (!GameScr.mm.isLand(this.x - 7 + i, this.y)) continue;
                this.falling = false;
                this.dy = 0;
                int[] nArray = Bullet.getCollisionPoint(n2, n, this.x, this.y);
                if (nArray == null) break;
                this.y = nArray[1];
                break;
            }
        }
    }
}

