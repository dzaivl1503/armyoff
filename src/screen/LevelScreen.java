/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.GameScr;
import screen.TabScreen;

public class LevelScreen
extends TabScreen {
    static mImage ability;
    static mImage plus;
    static mImage arrow;
    public static byte point;
    public static byte level;
    int select = 0;
    short currPoint;
    short[] currAbility = new short[5];
    byte[] deltaA = new byte[5];
    Command cmdSelect;
    public static final String[] strAbility;
    Command cmdLamlai;
    byte[] canUp = new byte[5];
    byte[] canDown = new byte[5];
    private static final int ROW_COUNT = 5;
    private static final int ROW_SPACING = 22;
    private static final int ROW_TOP = 57;
    private static final int CONTENT_DX = -15;
    private static final int CONTENT_DY = -33;
    private static final int PANEL_HEIGHT = 160;
    private static final int PANEL_HEIGHT_TOUCH = 175;
    private static final int PANEL_INSET_LEFT = 10;

    public void show(CScreen cScreen) {
        super.show(cScreen);
        CCanvas.arrPopups.removeAllElements();
        this.hTabScreen = CCanvas.isTouch ? 175 : 160;
        this.layoutForCurrentScreen();
        this.currPoint = TerrainMidlet.myInfo.point;
        for (int i = 0; i < 5; ++i) {
            this.currAbility[i] = TerrainMidlet.myInfo.ability[i];
            this.canDown[i] = 0;
        }
        this.deltaA = new byte[5];
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        this.title = "Lvl " + playerInfo.level2 + (playerInfo.level2Percen >= 0 ? "+" : "") + playerInfo.level2Percen + "%";
        if (playerInfo.point > 0) {
            for (int i = 0; i < 5; ++i) {
                this.canUp[i] = 1;
            }
        }
    }

    public void doClose() {
        for (int i = 0; i < 5; ++i) {
            this.canDown[i] = 0;
        }
        this.isClose = true;
    }

    public LevelScreen() {
        this.nameCScreen = "LevelScreen screen!";
        this.n = CCanvas.isTouch ? 4 : 3;
        this.hTabScreen = CCanvas.isTouch ? 175 : 160;
        this.layoutForCurrentScreen();
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                LevelScreen.this.restartPoint();
                LevelScreen.this.doClose();
            }
        });
        this.cmdSelect = new Command(Language.xacnhan(), new IAction(){

            public void perform() {
                LevelScreen.this.doFire();
            }
        });
        this.cmdLamlai = new Command(Language.lamlai(), new IAction(){

            public void perform() {
                LevelScreen.this.restartPoint();
                CCanvas.menu.showMenu = false;
            }
        });
        this.title = "";
        this.getW();
    }

    private void layoutForCurrentScreen() {
        int n;
        this.getW();
        int n2 = CCanvas.width / 2 - this.wTabScreen / 2;
        this.xPaint = n2 + 10 - -15;
        this.yPaint = (CCanvas.hieght - CScreen.cmdH) / 2 - this.hTabScreen / 2;
        if (this.yPaint < 4) {
            this.yPaint = 4;
        }
        if ((n = CCanvas.hieght - CScreen.cmdH - this.hTabScreen - 4) < 4) {
            n = 4;
        }
        if (this.yPaint > n) {
            this.yPaint = n;
        }
    }

    public void doFire() {
        CCanvas.startYesNoDlg(Language.areYouSure(), new IAction(){

            public void perform() {
                int n;
                GameService.gI().addPoint(LevelScreen.this.deltaA);
                LevelScreen.this.currPoint = TerrainMidlet.myInfo.point;
                for (n = 0; n < 5; ++n) {
                    LevelScreen.this.canDown[n] = 0;
                }
                if (TerrainMidlet.myInfo.point > 0) {
                    for (n = 0; n < 5; ++n) {
                        LevelScreen.this.canUp[n] = 1;
                    }
                }
                for (n = 0; n < 5; ++n) {
                    LevelScreen.this.deltaA[n] = 0;
                    LevelScreen.this.canDown[n] = 0;
                }
                CCanvas.endDlg();
            }
        }, new IAction(){

            public void perform() {
                LevelScreen.this.restartPoint();
                CCanvas.endDlg();
            }
        });
    }

    public void restartPoint() {
        TerrainMidlet.myInfo.point = this.currPoint;
        if (this.currPoint != 0) {
            for (int i = 0; i < 5; ++i) {
                short[] sArray = this.currAbility;
                sArray[i] = (short)(sArray[i] - this.deltaA[i]);
                this.deltaA[i] = 0;
                this.canDown[i] = 0;
                this.canUp[i] = 1;
            }
        }
    }

    public void doUp() {
        if (TerrainMidlet.myInfo.point > 0) {
            int n = this.select;
            this.deltaA[n] = (byte)(this.deltaA[n] + 1);
            TerrainMidlet.myInfo.point = (short)(TerrainMidlet.myInfo.point - 1);
            int n2 = this.select;
            this.currAbility[n2] = (short)(this.currAbility[n2] + 1);
            this.canDown[this.select] = 1;
        }
        if (TerrainMidlet.myInfo.point == 0) {
            for (int i = 0; i < 5; ++i) {
                this.canUp[i] = 0;
            }
        }
    }

    public void doDown() {
        short s = TerrainMidlet.myInfo.ability[this.select];
        if (this.currAbility[this.select] > s) {
            int n = this.select;
            this.deltaA[n] = (byte)(this.deltaA[n] - 1);
            TerrainMidlet.myInfo.point = (short)(TerrainMidlet.myInfo.point + 1);
            int n2 = this.select;
            this.currAbility[n2] = (short)(this.currAbility[n2] - 1);
            this.canUp[this.select] = 1;
        }
        if (this.currAbility[this.select] == s) {
            this.canDown[this.select] = 0;
        }
        if (TerrainMidlet.myInfo.point > 0) {
            for (int i = 0; i < 5; ++i) {
                this.canUp[i] = 1;
            }
        }
    }

    public static void paintLevelPercen(mGraphics mGraphics2, int n, int n2) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        mGraphics2.setColor(1521982);
        mGraphics2.fillRect(n, n2, 102, 17, false);
        mGraphics2.setColor(2378093);
        mGraphics2.fillRect(n + 1, n2 + 1, 100, 15, false);
        int n3 = playerInfo.level2Percen * 100 / 100;
        mGraphics2.setColor(16767817);
        mGraphics2.fillRect(n + 1, n2 + 1, n3, 15, false);
        Font.borderFont.drawString(mGraphics2, playerInfo.exp + "/" + playerInfo.nextExp, n + 51, n2, 2);
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        this.layoutForCurrentScreen();
        super.paint(mGraphics2);
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        int n2 = playerInfo.level2;
        mGraphics2.translate(-15, -33);
        int n3 = 22;
        int n4 = CCanvas.isTouch ? 15 : 0;
        int n5 = CCanvas.isTouch ? 10 : 0;
        for (n = 0; n < 5; ++n) {
            mGraphics2.drawRegion(ability, 0, n * 16, 16, 16, 0, this.xPaint, this.yPaint + 57 + n * n3, 0, false);
            if (!CCanvas.isTouch) {
                if (n == this.select) {
                    Font.normalYFont.drawString(mGraphics2, strAbility[n], this.xPaint + 25, this.yPaint + 57 + 1 + n * n3, 0);
                } else {
                    Font.normalFont.drawString(mGraphics2, strAbility[n], this.xPaint + 25, this.yPaint + 57 + 1 + n * n3, 0);
                }
            } else {
                Font.normalFont.drawString(mGraphics2, strAbility[n], this.xPaint + 25, this.yPaint + 57 + 1 + n * n3, 0);
            }
            short s = this.currAbility[n];
            mGraphics2.setColor(1521982);
            mGraphics2.fillRect(this.xPaint + 98 + n4, this.yPaint + 57 + n * n3, 30, 16, false);
            if (this.canDown[n] == 1) {
                Font.normalGFont.drawString(mGraphics2, String.valueOf(s), this.xPaint + 113 + n4, this.yPaint + 57 + n * n3, 3);
            } else {
                Font.normalYFont.drawString(mGraphics2, String.valueOf(s), this.xPaint + 113 + n4, this.yPaint + 57 + n * n3, 3);
            }
            if (this.canDown[n] == 1) {
                mGraphics2.drawImage(arrow, this.xPaint + 96 + n4 - n5, this.yPaint + 57 + 5 + n * n3, 24, false);
            }
            if (this.canUp[n] != 1) continue;
            mGraphics2.drawRegion(arrow, 0, 0, 4, 7, 2, this.xPaint + 130 + n4 + n5, this.yPaint + 57 + 5 + n * n3, 0, false);
        }
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        n = CCanvas.isTouch ? 15 : 0;
        Font.borderFont.drawString(mGraphics2, "Point: " + playerInfo.point, CCanvas.hw, this.yPaint + 57 + 110 + 2 + n + -33, 3);
        this.paintSuper(mGraphics2);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        int n4;
        super.onPointerReleased(n, n2, n3);
        this.select = n4 = (n2 - (this.yPaint + 57 + -33)) / 22;
        if (this.select < 0) {
            this.select = 0;
        }
        if (this.select > 4) {
            this.select = 4;
        }
        CRes.out("==> sellect = " + n4);
        int n5 = this.yPaint + 57 + -33 + this.select * 22 - 7;
        if (CCanvas.isPointer(this.xPaint + 70 + -15, n5, 30, 22, n3)) {
            this.doDown();
        }
        if (CCanvas.isPointer(this.xPaint + 130 + -15, n5, 30, 22, n3)) {
            this.doUp();
        }
    }

    public void update() {
        super.update();
        GameScr.sm.update();
        this.handleKeyInput();
        if (TerrainMidlet.myInfo.point >= 0) {
            this.center = this.cmdSelect;
            this.left = this.cmdLamlai;
        } else {
            this.center = null;
            this.left = null;
        }
    }

    private void handleKeyInput() {
        if (CCanvas.keyPressed[2]) {
            --this.select;
            if (this.select < 0) {
                this.select = 4;
            }
            CCanvas.keyPressed[2] = false;
        }
        if (CCanvas.keyPressed[8]) {
            ++this.select;
            if (this.select >= 5) {
                this.select = 0;
            }
            CCanvas.keyPressed[8] = false;
        }
        if (CCanvas.keyPressed[4]) {
            this.doDown();
            CCanvas.keyPressed[4] = false;
        }
        if (CCanvas.keyPressed[6]) {
            this.doUp();
            CCanvas.keyPressed[6] = false;
        }
    }

    static {
        strAbility = new String[]{Language.heath(), Language.dam(), Language.defend(), Language.lucky(), Language.team()};
        try {
            ability = mImage.createImage("/item/ability.png");
            plus = mImage.createImage("/item/+.png");
            arrow = mImage.createImage("/map/arrow1.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}

