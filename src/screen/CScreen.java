/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import coreLG.CCanvas;
import map.Background;
import model.CRes;
import model.Font;
import model.IAction;
import network.Command;
import screen.GameScr;

public abstract class CScreen {
    public String nameCScreen;
    protected byte indexScreen;
    public static int w;
    public static int h;
    public static CScreen instance;
    public static final int ITEM_HEIGHT;
    public static boolean isSetClip;
    public Command left;
    public Command center;
    public Command right;
    public int[] _x;
    public int[] _y;
    public boolean menuScroll = false;
    public int cmtoX;
    public int cmx;
    public int cmdx;
    public int cmvx;
    public int cmxLim;
    public static int cmdW;
    public static int cmdH;
    public static final byte CMD_LEFT = 0;
    public static final byte CMD_RIGHT = 1;
    public static final byte CMD_CENTER = 2;
    public int midCommandW = 32;
    public static mImage yes;
    public static mImage no;
    public static mImage imgInfoPopup;
    public static mImage tab_1;
    public static mImage tab_2;
    public static mImage tab_3;
    public static mImage tab_4;
    public static mImage tab_5;
    public static mImage conner;
    public static mImage arrow;
    public static mImage levelup;
    public static mImage cup;
    private boolean leftHold;
    private boolean rightHold;
    private boolean centerHold;
    public static boolean isMoto;
    public static boolean isBB;
    public static boolean isNX;
    public static CScreen lastSCreen;
    static int t;
    static int y;
    public static int currKey;
    public static boolean keyUp;
    public static boolean keyDown;
    public static boolean keyLeft;
    public static boolean keyRight;
    public static boolean keyFire;

    public CScreen() {
        instance = this;
        this._x = new int[3];
        this._y = new int[3];
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        if (isSetClip) {
            mGraphics2.setClip(0, 0, w, h);
        }
        this.paintCommand(mGraphics2);
        if (CCanvas.isTouch) {
            int n = 12;
        } else {
            int n = 3;
        }
        if (CCanvas.currentDialog == null && !CCanvas.menu.showMenu) {
            int n = CCanvas.hieght - cmdH / 2 - Font.normalFont.getHeight() / 2;
            this._x[0] = 5;
            this._y[0] = n;
            this._x[1] = CCanvas.hw;
            this._y[1] = n;
            this._x[2] = CCanvas.width - 5;
            this._y[2] = n;
            if (CCanvas.isDebugging()) {
                mGraphics2.setColor(1407674);
                mGraphics2.fillRect(0, h - cmdH, cmdW, cmdH, false);
                mGraphics2.setColor(1407674);
                mGraphics2.fillRect(w - cmdW, h - cmdH, cmdW, cmdH, false);
                mGraphics2.setColor(1407674);
                mGraphics2.fillRect(w - cmdW >> 1, h - cmdH, cmdW, cmdH, false);
            }
            if (this.left != null) {
                if (this.leftHold) {
                    Font.normalFont.drawString(mGraphics2, this.left.caption, this._x[0] + 2, this._y[0] + 2, 0, true);
                } else {
                    Font.normalFont.drawString(mGraphics2, this.left.caption, this._x[0], this._y[0], 0, true);
                }
            }
            if (this.center != null) {
                if (this.centerHold) {
                    Font.normalFont.drawString(mGraphics2, this.center.caption, this._x[1] + 2, this._y[1] + 2, 2, true);
                } else {
                    Font.normalFont.drawString(mGraphics2, this.center.caption, this._x[1], this._y[1], 2, true);
                }
            }
            if (this.right != null) {
                if (this.rightHold) {
                    Font.normalFont.drawString(mGraphics2, this.right.caption, this._x[2] + 2, this._y[2] + 2, 1, true);
                } else {
                    Font.normalFont.drawString(mGraphics2, this.right.caption, this._x[2], this._y[2], 1, true);
                }
            }
        }
    }

    public static void paintDefaultBg(mGraphics mGraphics2) {
        Background.paintMenuBackGround(mGraphics2);
    }

    public static void paintLevelUp(mGraphics mGraphics2, int n) {
    }

    public void update() {
    }

    public void mainLoop() {
    }

    public boolean isShowing() {
        return CCanvas.curScr == this;
    }

    public void moveCamera() {
        if (this.cmx != this.cmtoX) {
            this.cmvx = this.cmtoX - this.cmx << 2;
            this.cmdx += this.cmvx;
            this.cmx += this.cmdx >> 3;
            this.cmdx &= 0xF;
        }
    }

    public static void clearKey() {
        int n;
        for (n = 0; n < CCanvas.keyPressed.length; ++n) {
            CCanvas.keyHold[n] = false;
            CCanvas.keyPressed[n] = false;
        }
        for (n = 0; n < CCanvas.isPointerClick.length; ++n) {
            CCanvas.isPointerClick[n] = false;
        }
        keyUp = false;
        keyDown = false;
        keyLeft = false;
        keyRight = false;
    }

    public void show() {
        CScreen.clearKey();
        CCanvas.curScr = this;
    }

    public void show(CScreen cScreen) {
        lastSCreen = cScreen;
        CScreen.clearKey();
        CCanvas.curScr = this;
    }

    public void show(IAction iAction) {
        CScreen.clearKey();
        CCanvas.curScr = this;
    }

    public void close() {
        CScreen.clearKey();
    }

    public static void paintWhitePopup(mGraphics mGraphics2, int n, int n2, int n3, int n4) {
        mGraphics2.setColor(0xFFFFFF);
        mGraphics2.fillRect(n2, n, n3, n4, false);
        mGraphics2.setColor(0);
        mGraphics2.drawRect(n2 - 1, n - 1, n3 + 1, n4 + 1, false);
    }

    public void keyPressed(int n) {
    }

    public void keyReleased(int n) {
    }

    public static boolean getCmdPointerPressed(byte by, int n, boolean bl) {
        if (!bl) {
            if (CCanvas.menu.showMenu) {
                return false;
            }
            if (CCanvas.currentDialog != null) {
                return false;
            }
        }
        return CCanvas.isPointerClick[n] && (by == 0 ? CCanvas.isPointer(0, h - cmdH, cmdW, cmdH, n) : (by == 1 ? CCanvas.isPointer(w - cmdW, h - cmdH, cmdW, cmdH, n) : CCanvas.isPointer(w - cmdW >> 1, h - cmdH, cmdW, cmdH, n)));
    }

    public static boolean getCmdPointerLast(byte by, int n) {
        if (CCanvas.menu.showMenu) {
            return false;
        }
        if (CCanvas.currentDialog != null) {
            return false;
        }
        int n2 = cmdH + 20;
        return by == 0 ? CCanvas.isPointer(0, h - n2, w / 2, n2, n) : (by == 1 ? CCanvas.isPointer(w / 2, h - n2, w - w / 2, n2, n) : CCanvas.isPointer(w - cmdW >> 1, h - n2, cmdW, n2, n));
    }

    public void paintCommand(mGraphics mGraphics2) {
        int n = cmdH;
        mGraphics2.setColor(12965614);
        mGraphics2.fillRect(0, h - n, w + 10, n, false);
        mGraphics2.setColor(0x303030);
        mGraphics2.drawLine(0, h - n, w + 10, h - n, false);
        mGraphics2.setColor(0xFFFFFF);
        mGraphics2.drawLine(0, h - n + 1, w + 10, h - n + 1, false);
    }

    public void paintTapUp(mGraphics mGraphics2) {
    }

    public static void paintBorderRect(mGraphics mGraphics2, int n, int n2, int n3, String string) {
        int n4;
        int n5 = n2;
        int n6 = n2 * 32 + 56;
        int n7 = CCanvas.width / 2 - n6 / 2;
        mGraphics2.setColor(12965614);
        mGraphics2.fillRect(n7, n + 20, n6 - 1, n3 - 25, false);
        mGraphics2.setColor(0x303030);
        mGraphics2.drawRect(n7, n + 10, n6 - 1, n3 - 15, false);
        mGraphics2.drawImage(tab_1, n7, n, 0, false);
        for (n4 = 0; n4 < n5; ++n4) {
            mGraphics2.drawImage(tab_2, n7 + 23 + n4 * 32, n, 0, false);
        }
        mGraphics2.drawImage(tab_3, n7 + 23 + n5 * 32, n, 0, false);
        mGraphics2.drawImage(tab_4, n7, n + n3 - CScreen.tab_4.image.getHeight(), 0, false);
        mGraphics2.drawImage(tab_5, n7 + n6 - CScreen.tab_5.image.getWidth(), n + n3 - CScreen.tab_5.image.getHeight(), 0, false);
        mGraphics2.setColor(12965614);
        mGraphics2.fillRect(n7 + 23, n + n3 - 5, n6 - 46, 5, false);
        mGraphics2.setColor(0x303030);
        mGraphics2.drawLine(n7 + 23, n + n3 - 1, n7 + n6 - 23, n + n3 - 1, false);
        mGraphics2.setColor(8620444);
        mGraphics2.drawLine(n7 + 23, n + n3 - 2, n7 + n6 - 23, n + n3 - 2, false);
        mGraphics2.setColor(8620444);
        mGraphics2.drawLine(n7 + 1, n + 20, n7 + 1, n + n3 - 5, false);
        if (!CRes.isNullOrEmpty(string)) {
            mGraphics2.setColor(12965614);
            n4 = Font.borderFont.getWidth(string);
            mGraphics2.fillRect(n7 + n6 / 2 - n4 / 2 - 5, n + 1, n4 + 10, 18, false);
            mGraphics2.setColor(6985149);
            mGraphics2.drawRect(n7 + n6 / 2 - n4 / 2 - 5, n + 1, n4 + 10, 17, false);
            Font.borderFont.drawString(mGraphics2, string, n7 + n6 / 2, n + 3, 2, false);
        }
    }

    public static void paintDefaultPopup(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        mGraphics2.setClip(0, 0, 1000, 1000);
        mGraphics2.setColor(12965614);
        mGraphics2.fillRect(n + 5, n2 + 5, n3 - 10, n4 - 10, false);
        mGraphics2.setColor(6457531);
        mGraphics2.drawRect(n + 6, n2 + 6, n3 - 13, n4 - 13, false);
        mGraphics2.drawImage(conner, n, n2, 0, false);
        mGraphics2.drawRegion(conner, 0, 0, 14, 14, 5, n + n3, n2, 24, false);
        mGraphics2.drawRegion(conner, 0, 0, 14, 14, 3, n + n3, n2 + n4, 40, false);
        mGraphics2.drawRegion(conner, 0, 0, 14, 14, 6, n, n2 + n4, 36, false);
        mGraphics2.setColor(6457531);
        mGraphics2.fillRect(n + 14, n2 + 1, n3 - 28, 4, false);
        mGraphics2.fillRect(n + 14, n2 + n4 - 6, n3 - 28, 4, false);
        mGraphics2.fillRect(n + 1, n2 + 14, 4, n4 - 28, false);
        mGraphics2.fillRect(n + n3 - 6, n2 + 14, 4, n4 - 28, false);
        mGraphics2.setColor(4678279);
        mGraphics2.drawRect(n + 14, n2 + 1, n3 - 28, 4, false);
        mGraphics2.drawRect(n + 14, n2 + n4 - 6, n3 - 28, 4, false);
        mGraphics2.drawRect(n + 1, n2 + 14, 4, n4 - 28, false);
        mGraphics2.drawRect(n + n3 - 6, n2 + 14, 4, n4 - 28, false);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        if (CCanvas.keyPressed[5] || CScreen.getCmdPointerLast((byte)2, n3)) {
            this.centerHold = true;
        }
        if (CCanvas.keyPressed[12] || CScreen.getCmdPointerLast((byte)0, n3)) {
            this.leftHold = true;
        }
        if (CCanvas.keyPressed[13] || CScreen.getCmdPointerLast((byte)1, n3)) {
            this.rightHold = true;
        }
        if (CCanvas.keyPressed[5]) {
            this.performOkAction(n3);
        }
        if (CCanvas.keyPressed[12]) {
            CCanvas.keyPressed[12] = false;
            if (this.left != null && this.left.action != null) {
                this.left.action.perform();
                this.leftHold = false;
            }
        }
        if (CCanvas.keyPressed[13]) {
            CCanvas.keyPressed[13] = false;
            if (this.right != null && this.right.action != null) {
                this.right.action.perform();
                this.rightHold = false;
            }
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        this.input(n, n2, n3);
        this.centerHold = false;
        this.leftHold = false;
        this.rightHold = false;
    }

    public void onPointerDragged(int n, int n2, int n3) {
        if (CCanvas.keyPressed[5] || CScreen.getCmdPointerLast((byte)2, n3)) {
            this.centerHold = true;
        }
        if (CCanvas.keyPressed[12] || CScreen.getCmdPointerLast((byte)0, n3)) {
            this.leftHold = true;
        }
        if (CCanvas.keyPressed[13] || CScreen.getCmdPointerLast((byte)1, n3)) {
            this.rightHold = true;
        }
    }

    public void onPointerHold(int n, int n2, int n3) {
        if (CScreen.getCmdPointerLast((byte)2, n3)) {
            this.centerHold = true;
        }
        if (CScreen.getCmdPointerLast((byte)0, n3)) {
            this.leftHold = true;
        }
        if (CScreen.getCmdPointerLast((byte)1, n3)) {
            this.rightHold = true;
        }
    }

    public void onKeyPress(int n) {
    }

    public void onKeyPressHold(char c) {
    }

    public void onKeyRelease(int n) {
    }

    public byte getIndexSCreen() {
        return this.indexScreen;
    }

    private void input(int n, int n2, int n3) {
        if (CCanvas.keyPressed[5] || CScreen.getCmdPointerLast((byte)2, n3)) {
            this.performOkAction(n3);
        }
        if (CCanvas.keyPressed[12] || CScreen.getCmdPointerLast((byte)0, n3)) {
            CCanvas.keyPressed[12] = false;
            if (this.left != null && this.left.action != null) {
                this.left.action.perform();
                this.leftHold = false;
            }
        }
        if (CCanvas.keyPressed[13] || CScreen.getCmdPointerLast((byte)1, n3)) {
            CCanvas.keyPressed[13] = false;
            if (this.right != null && this.right.action != null) {
                this.right.action.perform();
                this.rightHold = false;
            }
        }
    }

    protected void performOkAction(int n) {
        if (!CCanvas.keyPressed[5] && !CScreen.getCmdPointerLast((byte)2, n)) {
            return;
        }
        CCanvas.keyPressed[5] = false;
        this.centerHold = false;
        if (this.center != null && this.center.action != null) {
            this.center.action.perform();
        } else if (this.left != null && this.left.action != null) {
            this.left.action.perform();
        }
    }

    public static int Clamp(int n, int n2, int n3) {
        if (n < n2) {
            n = n2;
        }
        if (n > n3) {
            n = n3;
        }
        return n;
    }

    protected void onClose() {
    }

    static {
        ITEM_HEIGHT = Font.normalFont.getHeight() + 6;
        isSetClip = true;
        cmdW = Font.normalFont.getWidth("ABCDEFGHJKL");
        cmdH = 50;
        imgInfoPopup = GameScr.imgInfoPopup;
        try {
            tab_1 = mImage.createImage("/map/tab_1.png");
            tab_2 = mImage.createImage("/map/tab_2.png");
            tab_3 = mImage.createImage("/map/tab_3.png");
            tab_4 = mImage.createImage("/map/tab_4.png");
            tab_5 = mImage.createImage("/map/tab_5.png");
            conner = mImage.createImage("/map/corner.png");
            arrow = mImage.createImage("/map/arrow.png");
            levelup = mImage.createImage("/lever-up.png");
            cup = mImage.createImage("/cup.png");
            yes = mImage.createImage("/x.png");
            no = mImage.createImage("/v.png");
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
        y = 1;
    }
}

