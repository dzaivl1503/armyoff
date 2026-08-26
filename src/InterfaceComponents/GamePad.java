/*
 * Decompiled with CFR 0.152.
 */
package InterfaceComponents;

import CLib.mGraphics;
import CLib.mSystem;
import InterfaceComponents.ChatTextField;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.MotherCanvas;
import coreLG.CCanvas;
import effect.Camera;
import model.Font;
import model.Language;
import model.TField;
import screen.CScreen;
import screen.GameScr;

public final class GamePad {
    private static final int PAD_COLOR = 8705740;
    private static final int HIGHLIGHT_COLOR = 14279153;
    private static final int BORDER_DARK = 2378578;
    private static final int BORDER_MID = 6201499;
    private static final int BORDER_LIGHT = 2716523;
    private int x;
    private int y;
    private int w;
    private int h;
    private int cellW;
    private int cellH;
    private int keyPadX;
    private int keyPadY;
    private int keyCellW;
    private int keyCellH;
    private int cols;
    private int rows;
    private int keyCols;
    private int keyRows;
    private String[] labels;
    private String[] lowerKeys;
    private String[] upperKeys;
    private String[] numericKeys;
    private String[] displayKeys;
    private byte[] keyCodes;
    private int selectedIndex = -1;
    private int activeIndex = -1;
    private int lastPadIndex = -1;
    private boolean padTouched;
    private boolean pendingRelease;
    public boolean isPointerClick;
    private boolean chatHold;
    private long chatHoldTime;
    private final String[] dirNames = new String[]{"Top", "Down", "Left", "Right"};
    private final byte[] dirTransforms = new byte[]{4, 7, 0, 2};

    public GamePad() {
        int n = MotherCanvas.w;
        int n2 = MotherCanvas.h;
        if (n2 >= n) {
            int n3;
            CCanvas.G = false;
            int n4 = n2 / 6 << 1;
            MotherCanvas.h = n3 = n2 - n4;
            this.x = 0;
            this.y = n3;
            this.h = n4;
            this.w = n;
            this.cellW = this.w / 4;
            this.cellH = this.h / 2;
            this.keyPadX = this.x;
            this.keyPadY = this.y;
            this.keyCellH = this.h / 3;
            this.keyCellW = this.w / 4;
            this.cols = 4;
            this.rows = 2;
            this.keyCols = 4;
            this.keyRows = 3;
            this.labels = new String[]{"F1", "Top", "MAX", "F2", "Left", "Down", "Right", "OK"};
            this.lowerKeys = new String[]{".,?!1", "abc2", "def3", Language.delete(), "ghi4", "jkl5", "mno6", Language.finish(), "pqrs7", "tuv8", "wxyz9", "0"};
            this.upperKeys = new String[12];
            for (int i = 0; i < 12; ++i) {
                this.upperKeys[i] = this.lowerKeys[i].toUpperCase();
            }
            this.upperKeys[3] = this.lowerKeys[3];
            this.numericKeys = new String[]{"1", "2", "3", Language.delete(), "4", "5", "6", Language.finish(), "7", "8", "9", "0"};
            this.keyCodes = new byte[]{-6, -1, 0, -7, -3, -2, -4, -5};
        } else {
            CCanvas.G = true;
            int n5 = n / 6 << 1;
            MotherCanvas.w = n - n5;
            this.y = 0;
            this.h = n2;
            this.x = MotherCanvas.w;
            this.w = n5;
            this.cellW = this.w / 2;
            this.cellH = this.h / 4;
            this.keyPadX = this.x;
            this.keyPadY = this.y;
            this.keyCellH = this.h / 4;
            this.keyCellW = this.w / 3;
            this.cols = 2;
            this.rows = 4;
            this.keyCols = 3;
            this.keyRows = 4;
            this.labels = new String[]{"F1", "OK", "MAX", "Top", "Left", "Right", "F2", "Down"};
            this.lowerKeys = new String[]{".,?!1", "abc2", "def3", "ghi4", "jkl5", "mno6", "pqrs7", "tuv8", "wxyz9", Language.finish(), "0", Language.delete()};
            this.upperKeys = new String[12];
            for (int i = 0; i < 11; ++i) {
                this.upperKeys[i] = this.lowerKeys[i].toUpperCase();
            }
            this.upperKeys[11] = this.lowerKeys[11];
            this.numericKeys = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", Language.finish(), "0", Language.delete()};
            this.keyCodes = new byte[]{-7, -5, 0, -1, -3, -4, -6, -2};
        }
        this.chatHoldTime = -1L;
        this.refreshKeyLabels();
        GamePad.applyCanvasSize();
    }

    public static void init() {
        if (!CCanvas.isTouch || !CCanvas.isGDX()) {
            CCanvas.gamePad = null;
            CCanvas.G = false;
            GamePad.applyCanvasSize();
            return;
        }
        if (CCanvas.isGDX() && !CCanvas.isVirtualKey) {
            CCanvas.isVirtualKey = true;
        }
        if (CCanvas.isVirtualKey || CCanvas.isGDX()) {
            CCanvas.gamePad = new GamePad();
            CCanvas.isSmallScreen = MotherCanvas.w <= 400;
        } else {
            CCanvas.G = false;
            CCanvas.gamePad = null;
            GamePad.applyCanvasSize();
        }
    }

    private static void applyCanvasSize() {
        CCanvas.width = MotherCanvas.w;
        CCanvas.hieght = MotherCanvas.h;
        CCanvas.hw = CCanvas.width / 2;
        CCanvas.hh = CCanvas.hieght / 2;
        CCanvas.hCan = CCanvas.hieght;
        CScreen.w = CCanvas.width;
        CScreen.h = CCanvas.hieght;
    }

    private static void handleDelete() {
        if (CCanvas.inputDlg != null && CCanvas.currentDialog == CCanvas.inputDlg && CCanvas.inputDlg.tfInput != null) {
            CCanvas.inputDlg.tfInput.keyPressed(8);
            return;
        }
        if (ChatTextField.isShow && ChatTextField.gI().tfChat != null) {
            ChatTextField.gI().commandTab(0, 0);
            return;
        }
        if (CCanvas.currentDialog != null && CCanvas.currentDialog.right != null && Language.delete().equals(CCanvas.currentDialog.right.caption)) {
            CCanvas.currentDialog.right.action.perform();
        }
    }

    public boolean containsPoint(int n, int n2) {
        return n >= this.x && n <= this.x + this.w && n2 >= this.y && n2 <= this.y + this.h;
    }

    private boolean isActionKeyCode(byte by) {
        return by == -6 || by == -7 || by == 0;
    }

    private int getMainPadIndex(int n, int n2) {
        if (!this.containsPoint(n, n2)) {
            return -1;
        }
        int n3 = (n - this.x) / this.cellW;
        int n4 = (n2 - this.y) / this.cellH;
        if (n3 < 0 || n3 >= this.cols || n4 < 0 || n4 >= this.rows) {
            return -1;
        }
        return n4 * this.cols + n3;
    }

    private void maintainActiveKey() {
        byte by;
        if (this.activeIndex >= 0 && this.activeIndex < this.keyCodes.length && !this.isActionKeyCode(by = this.keyCodes[this.activeIndex])) {
            GameMidlet.gameCanvas.keyPressed(by);
        }
    }

    private void releaseActiveKey() {
        if (this.activeIndex >= 0 && this.activeIndex < this.keyCodes.length) {
            GameMidlet.gameCanvas.keyReleased(this.keyCodes[this.activeIndex]);
        }
        this.activeIndex = -1;
    }

    private void pressMainPadCell(int n) {
        if (n < 0 || n >= this.keyCodes.length) {
            return;
        }
        this.selectedIndex = n;
        if (n == 2) {
            if (CCanvas.curScr instanceof GameScr) {
                GameMidlet.gameCanvas.keyPressed(0);
            } else {
                this.chatHoldTime = mSystem.currentTimeMillis() / 10L;
                this.chatHold = true;
            }
            return;
        }
        byte by = this.keyCodes[n];
        boolean bl = this.shouldHoldPadKey();
        GameMidlet.gameCanvas.keyPressed(by);
        if (!this.isActionKeyCode(by) && bl) {
            this.activeIndex = n;
        }
    }

    private boolean shouldHoldPadKey() {
        if (!(CCanvas.curScr instanceof GameScr)) {
            return false;
        }
        if (CCanvas.currentDialog != null) {
            return false;
        }
        if (CCanvas.pausemenu != null && CCanvas.pausemenu.isShow) {
            return false;
        }
        if (CCanvas.menu != null && CCanvas.menu.showMenu) {
            return false;
        }
        if (CCanvas.curScr instanceof GameScr && ((GameScr)CCanvas.curScr).isSelectingItem()) {
            return false;
        }
        return Camera.mode == 0 || Camera.mode == 1;
    }

    public void onPointerDown(int n, int n2) {
        if (this.isPointerClick) {
            return;
        }
        int n3 = this.getMainPadIndex(n, n2);
        if (n3 < 0) {
            return;
        }
        this.releaseActiveKey();
        this.pressMainPadCell(n3);
        this.padTouched = true;
        this.lastPadIndex = n3;
    }

    public boolean onPointerMove(int n, int n2) {
        if (this.isPointerClick || !this.padTouched) {
            return false;
        }
        int n3 = this.getMainPadIndex(n, n2);
        if (n3 == this.lastPadIndex) {
            return false;
        }
        this.releaseActiveKey();
        if (n3 >= 0) {
            this.pressMainPadCell(n3);
        }
        this.lastPadIndex = n3;
        return n3 >= 0;
    }

    public void onPointerUp(int n, int n2) {
        if (this.isPointerClick) {
            return;
        }
        this.pendingRelease = true;
    }

    public void finishPointerFrame() {
        if (this.pendingRelease) {
            this.releaseActiveKey();
            this.pendingRelease = false;
            this.padTouched = false;
            this.lastPadIndex = -1;
            this.selectedIndex = -1;
        }
    }

    public void updateKey() {
        if (!this.isPointerClick) {
            if (this.chatHold && CCanvas.isPointerRelease[0]) {
                this.chatHold = false;
                if (mSystem.currentTimeMillis() / 10L - this.chatHoldTime > 40L) {
                    TField.keyPressedAscii();
                    this.refreshKeyLabels();
                } else {
                    this.selectedIndex = -1;
                    this.isPointerClick = true;
                }
            }
            if (this.padTouched && this.activeIndex >= 0) {
                this.maintainActiveKey();
            }
        } else if (CCanvas.isPointerPad(this.keyPadX, this.keyPadY, this.w, this.h)) {
            if (CCanvas.isPointerDown[0]) {
                int n = (CCanvas.pX[0] - this.keyPadX) / this.keyCellW;
                int n2 = (CCanvas.pY[0] - this.keyPadY) / this.keyCellH;
                this.selectedIndex = n2 * this.keyCols + n;
                if (CCanvas.G && this.selectedIndex < 9) {
                    GameMidlet.gameCanvas.keyPressed(this.selectedIndex + 49);
                } else if (!CCanvas.G && this.selectedIndex % 4 != 3) {
                    GameMidlet.gameCanvas.keyPressed(this.selectedIndex + 49 - this.selectedIndex / 4);
                } else {
                    switch (this.selectedIndex) {
                        case 3: {
                            GamePad.handleDelete();
                            break;
                        }
                        case 7:
                        case 9: {
                            this.isPointerClick = false;
                            break;
                        }
                        case 10: {
                            GameMidlet.gameCanvas.keyPressed(48);
                            break;
                        }
                        case 11: {
                            if (CCanvas.G) {
                                GamePad.handleDelete();
                                break;
                            }
                            GameMidlet.gameCanvas.keyPressed(48);
                            break;
                        }
                    }
                }
                CCanvas.isPointerDown[0] = false;
            }
            if (CCanvas.isPointerRelease[0] && this.selectedIndex != -1) {
                this.selectedIndex = -1;
                CCanvas.isPointerRelease[0] = false;
            }
        }
    }

    private void refreshKeyLabels() {
        switch (TField.mode) {
            case 0:
            case 1: {
                this.displayKeys = this.lowerKeys;
                return;
            }
            case 2: {
                this.displayKeys = this.upperKeys;
                return;
            }
            case 3: {
                this.displayKeys = this.numericKeys;
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        mGraphics2.setClip(this.x, this.y, this.w, this.h);
        if (this.isPointerClick) {
            this.paintNumericPad(mGraphics2);
        } else {
            this.paintMainPad(mGraphics2);
        }
        this.paintPadBorder(mGraphics2);
    }

    private void paintMainPad(mGraphics mGraphics2) {
        int n;
        mGraphics2.setClip(this.x, this.y, this.w, this.h);
        GamePad.fillRect(mGraphics2, this.x, this.y, this.w, this.h, 8705740);
        mGraphics2.setColor(0);
        mGraphics2.drawRect(this.x, this.y, this.w - 1, this.h - 1, false);
        for (n = 1; n < this.cols + 1; ++n) {
            mGraphics2.fillRect(this.x + n * this.cellW, this.y, 1, this.h, false);
        }
        for (n = 1; n < this.rows; ++n) {
            mGraphics2.fillRect(this.x, this.y + n * this.cellH, this.w, 1, false);
        }
        for (n = 0; n < this.labels.length; ++n) {
            if (this.selectedIndex == n) {
                mGraphics2.setColor(14279153);
                mGraphics2.fillRect(this.x + n % this.cols * this.cellW + 1, this.y + n / this.cols * this.cellH + 1, this.cellW - 2, this.cellH - 2, false);
            }
            int n2 = this.x + n % this.cols * this.cellW + this.cellW / 2;
            int n3 = this.y + n / this.cols * this.cellH + this.cellH / 2;
            if ("F1".equals(this.labels[n]) || "F2".equals(this.labels[n]) || "MAX".equals(this.labels[n]) || "OK".equals(this.labels[n])) {
                Font.normalFont.drawString(mGraphics2, this.labels[n], n2, n3 - 5, 2, false);
                continue;
            }
            this.paintDirection(mGraphics2, this.labels[n], n2, n3);
        }
    }

    private void paintNumericPad(mGraphics mGraphics2) {
        int n;
        mGraphics2.setClip(this.keyPadX, this.keyPadY, this.w, this.h);
        GamePad.fillRect(mGraphics2, this.keyPadX, this.keyPadY, this.w, this.h, 8705740);
        mGraphics2.setColor(1);
        mGraphics2.drawRect(this.keyPadX, this.keyPadY, this.w - 1, this.h - 1, false);
        for (n = 1; n < this.keyCols; ++n) {
            mGraphics2.fillRect(this.keyPadX + n * this.keyCellW, this.keyPadY, 1, this.h, false);
        }
        for (n = 1; n < this.keyRows; ++n) {
            mGraphics2.fillRect(this.keyPadX, this.keyPadY + n * this.keyCellH, this.w, 1, false);
        }
        for (n = 0; n < this.displayKeys.length; ++n) {
            int n2 = this.keyPadY + n / this.keyCols * this.keyCellH;
            mGraphics2.setClip(this.keyPadX + n % this.keyCols * this.keyCellW, n2 - 5, this.keyCellW, this.keyCellH + 5);
            if (this.selectedIndex == n) {
                mGraphics2.setColor(14279153);
                mGraphics2.fillRect(this.keyPadX + n % this.keyCols * this.keyCellW + 1, n2 + 1, this.keyCellW - 2, this.keyCellH - 2, false);
            }
            Font.normalFont.drawString(mGraphics2, this.displayKeys[n], this.keyPadX + n % this.keyCols * this.keyCellW + this.keyCellW / 2, n2 - 5 + this.keyCellH / 2, 2, false);
        }
    }

    private void paintDirection(mGraphics mGraphics2, String string, int n, int n2) {
        for (int i = 0; i < this.dirNames.length; ++i) {
            if (!this.dirNames[i].equals(string)) continue;
            if (CScreen.arrow != null) {
                mGraphics2.drawRegion(CScreen.arrow, 0, 0, CScreen.arrow.image.getWidth(), CScreen.arrow.image.getHeight(), this.dirTransforms[i], n, n2, mGraphics.HCENTER | mGraphics.VCENTER, false);
            } else {
                Font.normalFont.drawString(mGraphics2, string, n, n2 - 5, 2, false);
            }
            return;
        }
    }

    private void paintPadBorder(mGraphics mGraphics2) {
        mGraphics2.setClip(this.x, this.y, this.w, this.h);
        mGraphics2.setColor(2378578);
        if (CCanvas.G) {
            mGraphics2.drawRect(this.x, this.y, 1, this.h, false);
            mGraphics2.setColor(6201499);
            mGraphics2.fillRect(this.x, this.y + 1, 1, this.h - 2, false);
        } else {
            mGraphics2.drawRect(this.x, this.y, this.w, 1, false);
            mGraphics2.setColor(6201499);
            mGraphics2.fillRect(this.x + 1, this.y, this.w - 2, 1, false);
        }
    }

    private static void fillRect(mGraphics mGraphics2, int n, int n2, int n3, int n4, int n5) {
        mGraphics2.setColor(n5);
        mGraphics2.fillRect(n, n2, n3, n4, false);
    }
}

