/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import item.Item;
import java.util.Vector;
import model.CRes;
import model.Font;
import model.FrameImage;
import model.IAction;
import model.IAction2;
import model.Language;
import network.Command;
import network.GameService;
import network.Session_ME;
import screen.CScreen;

public class LuckyGame
extends CScreen {
    Command cmdStart;
    Command cmdBack;
    Command cmdMoneyLvl;
    int x = CCanvas.width / 2 - 85;
    int y = CCanvas.hieght / 2 - 85;
    int w = 170;
    int h = 170;
    int degree;
    int xd;
    int yd;
    int r = this.w / 2;
    int stopAngle;
    int t;
    int g = 0;
    int count = 0;
    int d = 0;
    int point;
    public int money;
    public int myMoney;
    public Vector gifts = new Vector();
    int[] p = new int[]{239, 203, 167, 131, 95, 59, 23, 0, 311, 275};
    int[] color = new int[]{16315529, 9621318, 504643, 701933, 27571, 16315529, 9621318, 504643, 701933, 27571};
    boolean ready;
    boolean start;
    boolean stop;
    boolean hit;
    static mImage vong;
    static mImage kim;
    static mImage vong_tron;
    static FrameImage s_frBar;
    final int[] moneyLvl = new int[]{200, 500, 1000, 1500, 2000};
    int _xu = 1000;
    int _luong = 1;
    int hdegree1 = 250;
    int hdegree2 = 290;
    int hdegree3 = 270;
    int power;
    int min;
    int max;
    boolean isHoldFire;
    boolean _quay;
    int b;
    int tf;

    public LuckyGame() {
        this.nameCScreen = " LuckyGame screen!";
        this.money = this._xu;
        this.cmdStart = new Command(Language.quay(), new IAction(){

            public void perform() {
                LuckyGame.this.doQuaySo(LuckyGame.this.money);
            }
        });
        this.cmdMoneyLvl = new Command(Language.muctien(), new IAction(){

            public void perform() {
                Vector<Command> vector = new Vector<Command>();
                Command command = new Command("1000 " + Language.xu(), new IAction(){

                    public void perform() {
                        LuckyGame.this.money = LuckyGame.this._xu;
                    }
                });
                Command command2 = new Command("1 " + Language.luong(), new IAction(){

                    public void perform() {
                        LuckyGame.this.money = LuckyGame.this._luong;
                    }
                });
                vector.addElement(command);
                vector.addElement(command2);
                CCanvas.menu.startAt(vector, 0);
            }
        });
        this.cmdBack = new Command(Language.back(), new IAction(){

            public void perform() {
                CCanvas.menuScr.show();
            }
        });
        if (!CCanvas.isTouch) {
            this.center = this.cmdStart;
        }
        this.right = this.cmdBack;
        this.left = this.cmdMoneyLvl;
        if (this.y + this.w > CCanvas.hieght - cmdH) {
            this.y = 5;
        }
    }

    public void doQuaySo(final int n) {
        CCanvas.startYesNoDlg(Language.bancomuon() + n + (n == this._xu ? Language.xu() : Language.luong()) + "?", new IAction(){

            public void perform() {
                if (n == LuckyGame.this._xu) {
                    GameService.gI().sendRulet((byte)0);
                    if (n < 1000) {
                        LuckyGame.this.power = 0;
                        LuckyGame.this._quay = false;
                    }
                }
                if (n == LuckyGame.this._luong) {
                    GameService.gI().sendRulet((byte)1);
                    if (n < 1) {
                        LuckyGame.this.power = 0;
                        LuckyGame.this._quay = false;
                    }
                }
                CCanvas.startOKDlg(Language.pleaseWait());
                LuckyGame luckyGame = LuckyGame.this;
                luckyGame.myMoney -= n;
            }
        });
    }

    public void show(CScreen cScreen) {
        lastSCreen = cScreen;
        this.money = this._xu;
        this.myMoney = TerrainMidlet.myInfo.xu;
        super.show();
    }

    public void getGifts(Vector vector, int n) {
        this.gifts = vector;
        this.point = n;
        this.center = null;
        this.right = null;
        this.left = null;
        this.ready = true;
        this.degree = 0;
        this.stopAngle = 0;
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        int n7;
        LuckyGame.paintDefaultBg(mGraphics2);
        int n8 = (2 * this.r / 8 - 5) * CRes.cos(CRes.fixangle(this.hdegree1)) >> 10;
        int n9 = -((2 * this.r / 8 - 5) * CRes.sin(CRes.fixangle(this.hdegree1))) >> 10;
        int n10 = (2 * this.r / 8 - 5) * CRes.cos(CRes.fixangle(this.hdegree2)) >> 10;
        int n11 = -((2 * this.r / 8 - 5) * CRes.sin(CRes.fixangle(this.hdegree2))) >> 10;
        int n12 = 3 * this.r / 5 * CRes.cos(CRes.fixangle(this.hdegree3)) >> 10;
        int n13 = -(3 * this.r / 5 * CRes.sin(CRes.fixangle(this.hdegree3))) >> 10;
        int n14 = this.x + this.w / 2 + n8 + 7;
        int n15 = this.y + this.w / 2 + n9 - 15;
        int n16 = this.x + this.w / 2 + n10;
        int n17 = this.y + this.w / 2 + n11;
        int n18 = this.x + this.w / 2 + n12;
        int n19 = this.y + this.w / 2 + n13;
        mGraphics2.drawImage(vong_tron, this.x + this.w / 2 + 5, this.y + this.w / 2 + 5, 3, true);
        for (n7 = 0; n7 < 10; ++n7) {
            int n20;
            if (this.degree > 360) {
                this.degree -= 360;
            }
            n6 = this.degree + n7 * 36 + 18;
            n5 = this.degree + n7 * 36;
            n4 = 4 * this.r / 5 * CRes.cos(CRes.fixangle(n6)) >> 10;
            n3 = -(4 * this.r / 5 * CRes.sin(CRes.fixangle(n6))) >> 10;
            n2 = this.x + this.w / 2 + n4;
            n = this.y + this.w / 2 + n3 - Font.normalFont.getHeight() / 2;
            int n21 = 4 * this.r / 5 * CRes.cos(CRes.fixangle(n5)) >> 10;
            mGraphics2.drawRegion(vong, 0, 0, 90, 90, 0, this.x - 5, this.y - 5, 0, true);
            mGraphics2.drawRegion(vong, 0, 0, 90, 90, 2, this.x + 90 - 5, this.y - 5, 0, true);
            mGraphics2.drawRegion(vong, 0, 0, 90, 90, 1, this.x - 5, this.y + 90 - 5, 0, true);
            mGraphics2.drawRegion(vong, 0, 0, 90, 90, 7, this.x + 90 - 5, this.y + 90 - 5, 0, true);
            if (!this.start && !this.stop) continue;
            int n22 = n20 = this.count >= 10 ? 15 : this.count + 1;
            if (n21 > 0 || n21 + n20 < 0) continue;
            this.hit = true;
        }
        for (n7 = 0; n7 < 10; ++n7) {
            if (this.degree > 360) {
                this.degree -= 360;
            }
            n6 = this.degree + n7 * 36 + 18;
            n5 = this.degree + n7 * 36;
            n4 = 4 * this.r / 5 * CRes.cos(CRes.fixangle(n6)) >> 10;
            n3 = -(4 * this.r / 5 * CRes.sin(CRes.fixangle(n6))) >> 10;
            n2 = this.x + this.w / 2 + n4;
            n = this.y + this.w / 2 + n3 - Font.normalFont.getHeight() / 2;
            if (this.gifts == null || this.gifts.size() == 0) continue;
            Gift gift = (Gift)this.gifts.elementAt(n7);
            gift.paintGift(mGraphics2, n2, n);
        }
        mGraphics2.drawImage(kim, n14, n15, 3, true);
        if (CCanvas.h >= 200) {
            Font.normalFont.drawString(mGraphics2, TerrainMidlet.myInfo.xu + " " + Language.xu(), 5, CCanvas.h - cmdH - 40, 0);
            Font.normalFont.drawString(mGraphics2, TerrainMidlet.myInfo.luong + " " + Language.luong(), 5, CCanvas.h - cmdH - 20, 0);
        }
        n6 = CCanvas.w / 2;
        n7 = CCanvas.h > 220 ? 10 : CCanvas.h / 2 - 30;
        if (CCanvas.w >= 320) {
            n6 = 35;
            n7 = CCanvas.h / 2 - 30;
        }
        s_frBar.drawFrame(3, n6, n7, 3, 0, mGraphics2);
        s_frBar.fillFrame(2, n6, n7, this.power * 100 / 200, 3, 0, mGraphics2, false);
        super.paint(mGraphics2);
    }

    public void getMinMax(int n) {
        this.min = Math.abs(this.p[n]);
        this.max = this.min + (n != 7 ? 26 : 13);
    }

    public void getResult() {
        Gift gift = (Gift)this.gifts.elementAt(this.point);
        this.myMoney = TerrainMidlet.myInfo.xu;
        CCanvas.startOKDlg(gift.info, new IAction(){

            public void perform() {
                LuckyGame.this._quay = false;
            }
        });
    }

    public void releasePoint() {
        if (this.isHoldFire) {
            this.isHoldFire = false;
            if (this.power > 20) {
                if (this.money == this._xu) {
                    GameService.gI().sendRulet((byte)0);
                    this._quay = true;
                }
                if (this.money == this._luong) {
                    this._quay = true;
                    GameService.gI().sendRulet((byte)1);
                }
            }
        }
        if (this.money == this._xu && TerrainMidlet.myInfo.xu < 1000) {
            this.power = 0;
            this._quay = false;
            this.isHoldFire = false;
        }
        if (this.money == this._luong && TerrainMidlet.myInfo.luong < 1) {
            this.power = 0;
            this._quay = false;
            this.isHoldFire = false;
        }
    }

    public void ready() {
        ++this.t;
        if (this.t == 20) {
            this.start = true;
            this.ready = false;
            this.t = 0;
            this.getMinMax(this.point);
            this.b = CRes.random(15, 18);
        }
    }

    public boolean other() {
        return this.point == 7 && (this.degree >= this.min && this.degree <= this.max || this.degree >= 347 && this.degree <= 360);
    }

    public void start() {
        if (this.count == 19) {
            this.power -= 2;
            if (this.power <= 0) {
                if (!(this.degree >= this.min && this.degree <= this.max || this.other())) {
                    this.degree += 19;
                } else {
                    this.count = 19;
                    this.stopAngle = this.degree;
                    this.d = 0;
                    this.start = false;
                    this.stop = true;
                    this.power = 0;
                }
            } else {
                this.degree += 19;
            }
        } else {
            this.degree += this.count;
            if (this.count == this.b) {
                if (CCanvas.gameTick % 1 == 0) {
                    ++this.count;
                }
            } else {
                ++this.count;
            }
        }
    }

    public void stop() {
        if (this.d < 2850) {
            this.d += this.count;
            this.degree += this.count;
            ++this.g;
            if (this.g == 15) {
                this.g = 0;
                --this.count;
            }
        } else {
            int n;
            int n2 = n = this.t < 15 ? 2 : 4;
            if (CCanvas.gameTick % n == 0) {
                ++this.t;
                ++this.degree;
            }
            if (this.t == 30) {
                this.t = 0;
                this.d = 0;
                this.count = 0;
                this.stop = false;
                this.center = this.cmdStart;
                this.right = this.cmdBack;
                this.left = this.cmdMoneyLvl;
                this.getResult();
                Session_ME.receiveSynchronized = 0;
            }
        }
    }

    public void hit() {
        this.hit = false;
        if (this.d < 2850) {
            int n = this.count + 1;
            if (n < 3) {
                n = 3;
            }
            this.hdegree1 += n;
            if (this.hdegree1 > 260) {
                this.hdegree1 = 260;
            }
            this.hdegree2 += n;
            if (this.hdegree2 >= 300) {
                this.hdegree2 = 300;
            }
            this.hdegree3 += n;
            if (this.hdegree3 >= 280) {
                this.hdegree3 = 280;
            }
        }
    }

    public void arrowUpdate() {
        if (this.hdegree1 != 250) {
            this.hdegree1 -= 2;
        }
        if (this.hdegree1 < 250) {
            this.hdegree1 = 250;
        }
        if (this.hdegree2 != 290) {
            this.hdegree2 -= 2;
        }
        if (this.hdegree2 < 290) {
            this.hdegree2 = 290;
        }
        if (this.hdegree3 != 270) {
            this.hdegree3 -= 2;
        }
        if (this.hdegree3 < 270) {
            this.hdegree3 = 270;
        }
    }

    public void update() {
        super.update();
        if (this.ready) {
            this.ready();
        }
        if (this.start) {
            this.start();
        }
        if (this.stop) {
            this.stop();
        }
        if (this.hit) {
            this.hit();
        }
        if (this.power < 0) {
            this.power = 0;
        }
        if (this.power > 200) {
            this.power = 200;
        }
        this.arrowUpdate();
        lastSCreen.update();
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.releasePoint();
    }

    public void onPointerHold(int n, int n2, int n3) {
        super.onPointerHold(n, n2, n3);
        if (!CCanvas.isTouch) {
            if (!this._quay && CCanvas.keyHold[5]) {
                this.isHoldFire = true;
                this.power += 5;
                if (this.power > 200) {
                    this.power = 200;
                }
            }
            if (!CCanvas.keyHold[5]) {
                this.releasePoint();
            }
        }
        if (CCanvas.isTouch && !this._quay && CCanvas.isPointer(0, 0, CCanvas.width, CCanvas.hieght - cmdH, n3)) {
            this.isHoldFire = true;
            this.power += 5;
            if (this.power > 200) {
                this.power = 200;
            }
        }
    }

    static {
        try {
            mImage.createImage("/vong.png", new IAction2(){

                public void perform(Object object) {
                    vong = new mImage((Image)object);
                }
            });
            mImage.createImage("/kim.png", new IAction2(){

                public void perform(Object object) {
                    kim = new mImage((Image)object);
                }
            });
            mImage.createImage("/vong_tron.png", new IAction2(){

                public void perform(Object object) {
                    vong_tron = new mImage((Image)object);
                }
            });
            mImage.createImage("/gui/barMove.png", new IAction2(){

                public void perform(Object object) {
                    s_frBar = new FrameImage((Image)object, 53, 12, false);
                }
            });
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static class Gift {
        byte type;
        byte id;
        int n;
        public String info;

        public Gift(byte by, byte by2, int n) {
            this.type = by;
            this.id = by2;
            this.n = n;
            if (by == 0) {
                this.info = Language.xinchucmung() + n + " item " + Item.ITEM_NAME[by2] + ".";
            }
            if (by == 1) {
                this.info = Language.xinchucmung() + n + Language.xu() + ".";
            }
            if (by == 2) {
                this.info = Language.xinchucmung() + n + "XP.";
            }
            if (by == 3) {
                this.info = Language.lansau();
            }
        }

        public void paintGift(mGraphics mGraphics2, int n, int n2) {
            if (this.type == 0) {
                Font.borderFont.drawString(mGraphics2, String.valueOf(this.n), n, n2 + 10, 3);
            }
            if (this.type == 1) {
                Font.borderFont.drawString(mGraphics2, this.n + Language.xu(), n, n2, 3);
            }
            if (this.type == 2) {
                Font.borderFont.drawString(mGraphics2, this.n + "XP", n, n2, 3);
            }
            if (this.type != 3) {
            }
        }
    }
}

