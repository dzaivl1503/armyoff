/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import com.teamobi.mobiarmy2.MotherCanvas;
import coreLG.CCanvas;
import effect.Cloud;
import java.util.Vector;
import model.BoardInfo;
import model.Font;
import model.IAction;
import model.Language;
import model.RoomInfo;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.GameScr;
import screen.PrepareScr;

public class BoardListScr
extends CScreen {
    public static mImage imgMode = GameScr.imgMode;
    public static mImage lock = GameScr.lock;
    public static String boardName;
    int nBoardPerLine = CCanvas.width / 55;
    int defX = CCanvas.width - this.nBoardPerLine * 55 >> 1;
    public static Vector boardList;
    public static int selected;
    public static int cmtoY;
    public static int cmy;
    public static int cmdy;
    public static int cmvy;
    public static int cmyLim;
    public byte roomID;
    int xList;
    int yList;
    static int boxX;
    static int boxY;
    static int boxW;
    static int boxH;
    int boxMaxW;
    int boxMaxH;
    int boxSpeed = 4;
    static boolean isOpenBox;
    static boolean isAllowPaintBoard;
    static Vector roomInfo;
    public static int currRoom;
    int pa = 0;
    boolean trans = false;

    public void moveCamera() {
        if (cmy != cmtoY) {
            cmvy = cmtoY - cmy << 2;
            cmy += (cmdy += cmvy) >> 4;
            cmdy &= 0xF;
        }
    }

    public BoardListScr() {
        this.center = new Command(Language.join(), new IAction(){

            public void perform() {
                if (boardList.size() != 0) {
                    BoardListScr.this.doJoinBoard();
                }
            }
        });
        this.left = new Command(Language.update(), new IAction(){

            public void perform() {
                BoardListScr.this.doUpdate();
            }
        });
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                BoardListScr.this.doExitBoardList();
                isOpenBox = false;
            }
        });
        this.boxMaxW = CScreen.w - 20 > 170 ? CScreen.w - 20 : 170;
        this.boxMaxW = CScreen.w;
        this.boxMaxH = CScreen.h - 120 > 180 ? CScreen.h - 120 : 180;
        boxW = this.boxMaxW;
        this.nameCScreen = "BoardListScr screen!";
    }

    public static void setBoardName(int n, String string) {
        boardName = string != null && !string.equals("") ? Language.area() + ": " + string : Language.area() + ": " + n;
    }

    protected void doJoinBoard() {
        BoardInfo boardInfo = (BoardInfo)boardList.elementAt(selected);
        BoardListScr.setBoardName(boardInfo.boardID, boardInfo.name);
        if (boardInfo.isPass) {
            CCanvas.inputDlg.show();
        } else {
            PrepareScr.currentRoom = this.roomID;
            GameService.gI().joinBoard(this.roomID, boardInfo.boardID, "");
            CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 13);
        }
    }

    private void doExitBoardList() {
    }

    private void doUpdate() {
        CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 14);
        GameService.gI().requestBoardList(this.roomID);
    }

    public void show() {
        super.show();
        this.xList = CScreen.w;
        isOpenBox = true;
        selected = 0;
        cmtoY = selected * ITEM_HEIGHT - (CCanvas.hh - 2 * ITEM_HEIGHT);
        if (cmtoY > cmyLim) {
            cmtoY = cmyLim;
        }
        if (cmtoY < 0) {
            cmtoY = 0;
        }
        if (selected == boardList.size() - 1 || selected == 0) {
            cmy = cmtoY;
        }
    }

    public static void activeBox() {
        boxW = 0;
        boxH = 0;
        isOpenBox = true;
        isAllowPaintBoard = false;
    }

    private void updateBox() {
        if (isOpenBox) {
            if (boxW < this.boxMaxW) {
                boxW += Math.max(this.boxMaxW / this.boxSpeed, 1);
            }
            if (boxH < this.boxMaxH) {
                boxH += Math.max(this.boxMaxH / this.boxSpeed, 1);
            } else {
                isAllowPaintBoard = true;
            }
            if (boxX != CScreen.w / 2) {
                boxX += CScreen.w / 2 - boxX;
            }
            if (boxY != CScreen.h / 2) {
                boxY += CScreen.h / 2 - boxY;
            }
        } else {
            if (boxW > 0) {
                boxW -= boxW / this.boxSpeed;
                Math.max(boxW, 1);
            }
            if (boxH > 0) {
                boxH -= boxH / this.boxSpeed;
                Math.max(boxH, 1);
            }
        }
    }

    public void update() {
        Cloud.updateCloud();
        this.moveCamera();
        if (isOpenBox && this.xList > 0) {
            this.xList -= Math.max(this.xList / 2, 1);
        }
    }

    public void paint(mGraphics mGraphics2) {
        BoardListScr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        for (int i = 0; i <= CCanvas.width; i += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, i, CCanvas.hieght - 62, 0, false);
        }
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        currRoom = ((RoomInfo)BoardListScr.roomInfo.elementAt((int)CCanvas.roomListScr2.selected)).id;
        Font.bigFont.drawString(mGraphics2, Language.ROOM() + " " + this.roomID, 10, 3, 0);
        mGraphics2.setColor(2378093);
        mGraphics2.fillRect(0, 25, CCanvas.width, ITEM_HEIGHT, false);
        Font.normalYFont.drawString(mGraphics2, Language.battleArea(), 10, 28, 0);
        if (isAllowPaintBoard) {
            BoardListScr.paintRichList(this.xList, 0, mGraphics2);
        }
        super.paint(mGraphics2);
    }

    public static void paintRichList(int n, int n2, mGraphics mGraphics2) {
        int n3 = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        mGraphics2.translate(n, n2 + n3 + (CCanvas.isTouch ? 14 : 25));
        mGraphics2.setClip(n, n2 + (CCanvas.isTouch ? -10 : 1), CCanvas.width, CCanvas.hieght);
        mGraphics2.translate(n, n2 - cmy);
        int n4 = n2 - n3;
        for (int i = 0; i < boardList.size(); ++i) {
            if ((n4 += n3) < cmy - n3 || n4 > cmy + CCanvas.hieght) continue;
            if (i == selected) {
                mGraphics2.setColor(16767817);
                mGraphics2.fillRect(n, n4 - (CCanvas.isTouch ? 10 : 0), CCanvas.width, n3, false);
            }
            BoardInfo boardInfo = (BoardInfo)boardList.elementAt(i);
            String string = Language.area() + " " + boardInfo.boardID;
            if (!boardInfo.name.equals("")) {
                string = boardInfo.name;
            }
            mGraphics2.drawRegion(imgMode, 0, boardInfo.mode * 17, 18, 17, 0, n + 8, n4 + 1, 0, false);
            Font.borderFont.drawString(mGraphics2, string, n + 30, n4 + 2, 0);
            Font.borderFont.drawString(mGraphics2, boardInfo.nPlayer + "/" + boardInfo.maxPlayer, n + CCanvas.width - 3, n4 + 2, 1);
            Font.borderFont.drawString(mGraphics2, boardInfo.money + Language.xu(), n + CCanvas.width - 30, n4 + 2, 1);
            BoardInfo boardInfo2 = (BoardInfo)boardList.elementAt(i);
            if (!boardInfo2.isPass) continue;
            mGraphics2.drawImage(lock, n + CCanvas.width - 30 - Font.borderFont.getWidth(boardInfo.money + Language.xu()) - 5, n4 + 1, 24, false);
        }
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
    }

    public void input() {
    }

    public void setBoardList(Vector vector) {
        roomInfo = CCanvas.roomListScr2.roomList;
        boardList = vector;
        int n = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        int n2 = CCanvas.isTouch ? 5 : 0;
        cmyLim = boardList.size() * n - (CCanvas.hieght - ITEM_HEIGHT * 4 - n2);
    }

    private void updateScrollToSelected() {
        int n = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        cmtoY = selected * n - (CCanvas.hh - 2 * ITEM_HEIGHT);
        if (cmtoY > cmyLim) {
            cmtoY = cmyLim;
        }
        if (cmtoY < 0) {
            cmtoY = 0;
        }
        if (selected == boardList.size() - 1 || selected == 0) {
            cmy = cmtoY;
        }
    }

    private void moveSelection(int n) {
        if (boardList == null || boardList.size() == 0) {
            return;
        }
        if ((selected += n) < 0) {
            selected = boardList.size() - 1;
        }
        if (selected >= boardList.size()) {
            selected = 0;
        }
        this.updateScrollToSelected();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (CCanvas.keyPressed[2]) {
            this.moveSelection(-1);
            CScreen.clearKey();
        }
        if (CCanvas.keyPressed[8]) {
            this.moveSelection(1);
            CScreen.clearKey();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        if (boardList == null || boardList.size() == 0) {
            return;
        }
        int n4 = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        int n5 = (cmtoY + n2 - n4) / n4;
        if (!MotherCanvas.touchDrag && CCanvas.isPointer(0, 0, w, CCanvas.hieght - cmdH, n3)) {
            if (n5 == selected && this.center != null && CCanvas.isDoubleClick) {
                this.center.action.perform();
            }
            if (n5 >= 0 && n5 < boardList.size()) {
                selected = n5;
                this.updateScrollToSelected();
            }
        }
    }

    static {
        isAllowPaintBoard = true;
    }
}

