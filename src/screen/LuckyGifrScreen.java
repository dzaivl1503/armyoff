/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import com.teamobi.mobiarmy2.OfflineLuckyGift;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Explosion;
import java.util.Vector;
import model.CTime;
import model.Font;
import model.IAction;
import model.Language;
import model.LuckyGift;
import model.PlayerInfo;
import network.Command;
import player.Boss;
import screen.CScreen;
import screen.GameScr;

public class LuckyGifrScreen
extends CScreen {
    public static String[] info;
    private LuckyGift[] gifts = new LuckyGift[12];
    boolean showAll;
    public int num = 12;
    public static CTime time;
    public int[] giftDelete = new int[this.num];
    int count;
    public int wLine;
    public int hLine;
    public int disW;
    public int x;
    public int y;
    int xSelect;
    int ySelect;
    public int select;
    public boolean isShow;
    int dem = 0;
    boolean isDem;

    public LuckyGifrScreen() {
        this.nameCScreen = " LuckyGifrScreen screen!";
        this.disW = CCanvas.width < 240 ? 40 : 50;
        this.wLine = 4;
        this.hLine = 3;
        this.x = (CCanvas.width - this.disW * this.wLine) / 2 + this.disW / 2;
        this.y = (CCanvas.width > CCanvas.hieght ? 40 : 70) + this.disW / 2;
        if (CCanvas.width < 200) {
            this.y = 25 + this.disW / 2;
        }
    }

    public void show() {
        GameScr.exs = new Vector();
        this.giftDelete = new int[this.num];
        this.count = 0;
        this.init();
        super.show();
    }

    private void init() {
        if (this.isShow) {
            this.right = new Command(Language.exit(), new IAction(){

                public void perform() {
                    if (LuckyGifrScreen.this.isShow) {
                        OfflineLuckyGift.finish();
                    } else {
                        OfflineLuckyGift.finish();
                    }
                }
            });
            this.left = null;
            this.center = null;
        } else {
            if (this.gifts != null) {
                for (int i = 0; i < this.gifts.length; ++i) {
                    if (this.gifts[i] == null) continue;
                    this.gifts[i].isShow = false;
                    this.gifts[i].isWait = false;
                }
            }
            this.center = new Command(Language.select(), new IAction(){

                public void perform() {
                    LuckyGifrScreen.this.isDem = true;
                    if (LuckyGifrScreen.this.dem == 0) {
                        if (LuckyGifrScreen.this.giftDelete[LuckyGifrScreen.this.select] != -1) {
                            OfflineLuckyGift.openBox(LuckyGifrScreen.this, (byte)LuckyGifrScreen.this.select);
                        }
                        new Explosion(LuckyGifrScreen.this.xSelect, LuckyGifrScreen.this.ySelect, 1);
                        ++LuckyGifrScreen.this.count;
                    }
                }
            });
            this.left = new Command("Xong", new IAction(){

                public void perform() {
                    OfflineLuckyGift.finish();
                    LuckyGifrScreen.this.left = null;
                    LuckyGifrScreen.this.center = null;
                }
            });
            this.right = null;
        }
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        int n2;
        LuckyGifrScreen.paintDefaultBg(mGraphics2);
        for (n2 = 0; n2 < info.length; ++n2) {
            Font.normalFont.drawString(mGraphics2, info[n2], CCanvas.width / 2, 5 + n2 * 20, 3);
        }
        int n3 = 5 + info.length * 20;
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        Font.borderFont.drawString(mGraphics2, playerInfo.xu + " " + Language.xu() + " - " + playerInfo.luong + " " + Language.luong(), CCanvas.width / 2, n3, 3);
        this.y = n3 + 20 + this.disW / 2;
        n2 = 0;
        int n4 = 0;
        if (time != null) {
            time.paint(mGraphics2);
        }
        for (int i = 0; i < this.num; ++i) {
            n = this.x + n2 * this.disW;
            int n5 = this.y + n4 * this.disW;
            if (i == this.select && !this.isShow) {
                this.xSelect = n;
                this.ySelect = n5;
                mGraphics2.setColor(3374591);
                mGraphics2.fillRect(n - 2 - Boss.gift_1.image.getWidth() / 2, n5 - 2 - Boss.gift_1.image.getWidth() / 2, Boss.gift_1.image.getWidth() + 4, Boss.gift_1.image.getWidth() + 4, false);
            }
            if (OfflineLuckyGift.isRealOpen(i)) {
                mGraphics2.setColor(16766720);
                mGraphics2.drawRect(n - 2 - Boss.gift_1.image.getWidth() / 2, n5 - 2 - Boss.gift_1.image.getWidth() / 2, Boss.gift_1.image.getWidth() + 4, Boss.gift_1.image.getWidth() + 4, false);
            }
            if (this.gifts[i] != null) {
                this.gifts[i].paint(mGraphics2, n, n5);
            } else {
                mGraphics2.drawImage(Boss.gift_1, n, n5, 3, false);
            }
            if (++n2 != this.wLine) continue;
            ++n4;
            n2 = 0;
        }
        for (n = 0; n < GameScr.exs.size(); ++n) {
            ((Explosion)GameScr.exs.elementAt(n)).paint(mGraphics2);
        }
        super.paint(mGraphics2);
    }

    public void update() {
        int n;
        if (this.isDem) {
            ++this.dem;
            if (this.dem == 20) {
                this.dem = 0;
                this.isDem = false;
            }
        }
        if (time != null) {
            time.update();
            if (CTime.seconds <= 0) {
                time = null;
                OfflineLuckyGift.onTimeExpired();
            }
        }
        OfflineLuckyGift.tick(this);
        for (n = 0; n < 12; ++n) {
            if (this.gifts[n] == null) continue;
            this.gifts[n].update();
        }
        for (n = 0; n < GameScr.exs.size(); ++n) {
            ((Explosion)GameScr.exs.elementAt(n)).update();
        }
        super.update();
    }

    public void setGiftByItemID(LuckyGift luckyGift) {
        this.gifts[luckyGift.id] = luckyGift;
    }

    public LuckyGift getGiftByItemID(int n) {
        for (int i = 0; i < this.gifts.length; ++i) {
            if (this.gifts[i] == null || this.gifts[i].id != n) continue;
            return this.gifts[i];
        }
        return null;
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (!this.isShow) {
            if (CCanvas.keyPressed[2] || CCanvas.keyPressed[4] || CCanvas.keyPressed[6] || CCanvas.keyPressed[8]) {
                this.moveSelectByKeys();
                return;
            }
            if (CCanvas.keyPressed[5]) {
                CCanvas.keyPressed[5] = false;
                if (this.center != null) {
                    this.center.action.perform();
                }
                return;
            }
        }
        super.onPointerPressed(n, n2, n3);
    }

    private void moveSelectByKeys() {
        if (CCanvas.keyPressed[4]) {
            --this.select;
        }
        if (CCanvas.keyPressed[6]) {
            ++this.select;
        }
        if (CCanvas.keyPressed[2]) {
            this.select -= this.wLine;
        }
        if (CCanvas.keyPressed[8]) {
            this.select += this.wLine;
        }
        while (this.select < 0) {
            this.select += this.num;
        }
        while (this.select >= this.num) {
            this.select -= this.num;
        }
        CScreen.clearKey();
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        int n4 = (CCanvas.pY[n3] - this.y + this.disW / 2) / this.disW * this.wLine + (CCanvas.pX[n3] - this.x + this.disW / 2) / this.disW;
        if (n4 != -1) {
            if (n4 == this.select && this.center != null && CCanvas.isDoubleClick) {
                this.center.action.perform();
            }
            if (n4 >= 0 && n4 < this.num) {
                this.select = n4;
            }
        }
    }
}

