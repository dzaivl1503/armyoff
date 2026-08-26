/*
 * Decompiled with CFR 0.152.
 */
package effect;

import CLib.mGraphics;
import Equipment.Equip;
import coreLG.CCanvas;
import effect.Camera;
import model.Font;

public class GiftEffect {
    public String text;
    public Equip equip;
    int xFly;
    int yFly;
    int tFly = 0;
    public boolean isFly;

    public GiftEffect(String string, Equip equip) {
        this.text = string;
        this.equip = equip;
        this.xFly = CCanvas.width / 2;
        this.yFly = CCanvas.hieght - 50;
    }

    public void paint(mGraphics mGraphics2) {
        if (this.isFly) {
            if (this.equip == null) {
                Font.borderFont.drawString(mGraphics2, this.text, this.xFly + Camera.x, this.yFly + Camera.y, 3);
            }
            if (this.equip != null) {
                Font.borderFont.drawString(mGraphics2, this.text, this.xFly + Camera.x, this.yFly + Camera.y, 3);
                int n = CCanvas.width / 2 + Camera.x;
                int n2 = this.yFly + Camera.y + 17;
                mGraphics2.setColor(4156571);
                mGraphics2.fillRoundRect(n - 2, n2 - 2, 20, 20, 4, 4, false);
                mGraphics2.setColor(16774532);
                mGraphics2.fillRect(n - 1, n2 - 1, 18, 18, false);
                this.equip.drawIcon(mGraphics2, n, n2, false);
            }
        }
    }

    public void update() {
        if (this.isFly) {
            ++this.tFly;
            this.yFly -= 2;
            if (this.tFly == 80) {
                this.tFly = 0;
                CCanvas.gameScr.vGift.removeElement(this);
            }
        }
    }
}

