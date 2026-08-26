/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import com.teamobi.mobiarmy2.MotherCanvas;
import coreLG.CCanvas;
import java.util.Vector;
import map.MM;
import model.Font;
import model.IAction;
import model.Language;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.GameScr;
import screen.PrepareScr;

public class RoomListScr
extends CScreen {
    public static mImage imgArrowRed;
    public static mImage imgMap;
    public static mImage imgCurPos;
    public static mImage imgSmallCloud;
    public static Vector roomList;
    int selected;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    int nBoardPerLine;
    int defX;
    int numH;
    int dis;
    static int nMap;
    static int curMapIndex;
    static int[] _iconX;
    static int _centerIX;
    static int rangeSplit;
    static int imgMapIW;
    static int imgMapIH;
    static boolean isMoveMenu;
    static int[] cloudType;
    static int[] cloudX;
    static int[] cloudY;
    static int curPosIndex;
    static int radaX;
    static int radaY;
    static int mapX;
    static int mapY;
    static int k;
    static int roundCamera;
    public int NUMB;
    public boolean isBoss;
    int pa = 0;
    boolean trans = false;
    int speed = 1;

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy << 2;
            this.cmdy += this.cmvy;
            this.cmy += this.cmdy >> 4;
            this.cmdy &= 0xF;
        }
    }

    public void init() {
        int n;
        this.nameCScreen = " RoomListScr screen!";
        nMap = this.NUMB = !this.isBoss ? MM.NUM_MAP - PrepareScr.mapBossID.length : PrepareScr.mapBossID.length;
        this.nBoardPerLine = CCanvas.width / 90;
        this.defX = (CCanvas.width - this.nBoardPerLine * 90 >> 1) + 40;
        for (n = 0; n < nMap; ++n) {
            RoomListScr._iconX[n] = n * rangeSplit;
        }
        _centerIX = w >> 1;
        curMapIndex = nMap / 2;
        this.dis = (_centerIX - _iconX[curMapIndex]) / 2;
        isMoveMenu = true;
        if (cloudX == null) {
            cloudType = new int[]{0, 1, 1};
            cloudX = new int[]{-50, 100, 190};
            cloudY = new int[]{-20, 0, 130};
        }
        this.center = new Command(Language.select(), new IAction(){

            public void perform() {
                RoomListScr.this.doSelect();
            }
        });
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                CCanvas.prepareScr.show();
            }
        });
        this.dis = CCanvas.hieght - (5 + cmdH);
        this.numH = this.NUMB / this.nBoardPerLine;
        if (this.NUMB % this.nBoardPerLine != 0) {
            ++this.numH;
        }
        n = this.numH * 57 + 40;
        this.cmyLim = n - this.dis;
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
        this.setCamY();
    }

    public void show() {
        this.init();
        super.show();
    }

    protected void doSelect() {
        byte by;
        int n;
        CCanvas.startWaitDlg(Language.starting());
        int n2 = n = this.isBoss ? MM.NUM_MAP - PrepareScr.mapBossID.length : 0;
        if (curMapIndex < this.NUMB && (by = (byte)(curMapIndex + n)) != -1) {
            GameService.gI().mapSelect(by);
            CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 11);
        }
    }

    public void paint(mGraphics mGraphics2) {
        RoomListScr.paintDefaultBg(mGraphics2);
        this.paintRoomList(mGraphics2);
        super.paint(mGraphics2);
    }

    public static void paintMapBar(int n, mGraphics mGraphics2) {
    }

    private void paintRoomList(mGraphics mGraphics2) {
        mGraphics2.translate(0, -this.cmy);
        int n = this.isBoss ? MM.NUM_MAP - PrepareScr.mapBossID.length : 0;
        for (int i = 0; i < this.NUMB; ++i) {
            int n2 = i % this.nBoardPerLine;
            int n3 = i / this.nBoardPerLine;
            int n4 = n2 * 90 + this.defX;
            int n5 = n3 * 57 + 60;
            if (i == curMapIndex) {
                mGraphics2.setColor(3684546);
                mGraphics2.fillRect(n4 - (imgMapIW >> 1) - 5, n5 - (imgMapIH >> 1) - 5, imgMapIW + 10, imgMapIH + 10, false);
            }
            mGraphics2.setColor(0xFFFFFF);
            mGraphics2.fillRect(n4 - (imgMapIW >> 1) + 1, n5 - (imgMapIH >> 1) + 1, imgMapIW - 2, imgMapIH - 2, false);
            try {
                if (PrepareScr.imgMap[i + n] == null) continue;
                mGraphics2.drawImage(PrepareScr.imgMap[i + n], n4, n5, 3, false);
                mGraphics2.drawImage(PrepareScr.khungMap, n4, n5, 3, false);
                continue;
            }
            catch (Exception exception) {
            }
        }
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        mGraphics2.setColor(1133755);
        mGraphics2.fillRect(0, 0, CCanvas.width, 20, false);
        Font.borderFont.drawString(mGraphics2, curMapIndex + 1 + ". " + MM.mapName[curMapIndex + n], w >> 1, 2, 2);
    }

    public void setCamY() {
        int n;
        int n2 = curMapIndex / this.nBoardPerLine;
        this.cmtoY = n = n2 * 57 + 60 - (CCanvas.hieght / 2 - cmdH);
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
        if (curMapIndex == this.NUMB - 1 || curMapIndex == 0) {
            this.cmy = this.cmtoY;
        }
    }

    private void moveMapGrid(int n, int n2) {
        int n3;
        if (this.NUMB <= 0) {
            return;
        }
        int n4 = curMapIndex % this.nBoardPerLine;
        int n5 = curMapIndex / this.nBoardPerLine;
        int n6 = (this.NUMB + this.nBoardPerLine - 1) / this.nBoardPerLine;
        if (n != 0) {
            if ((n4 += n) < 0) {
                n4 = this.nBoardPerLine - 1;
                if (--n5 < 0) {
                    n5 = n6 - 1;
                }
            } else if (n4 >= this.nBoardPerLine) {
                n4 = 0;
                if (++n5 >= n6) {
                    n5 = 0;
                }
            }
        }
        if (n2 != 0) {
            if ((n5 += n2) < 0) {
                n5 = n6 - 1;
            } else if (n5 >= n6) {
                n5 = 0;
            }
        }
        if ((n3 = n5 * this.nBoardPerLine + n4) >= this.NUMB) {
            n3 = this.NUMB - 1;
        }
        if (n3 < 0) {
            n3 = 0;
        }
        curMapIndex = n3;
        this.setCamY();
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (CCanvas.keyPressed[2]) {
            this.moveMapGrid(0, -1);
            CScreen.clearKey();
        }
        if (CCanvas.keyPressed[8]) {
            this.moveMapGrid(0, 1);
            CScreen.clearKey();
        }
        if (CCanvas.keyPressed[4]) {
            this.moveMapGrid(-1, 0);
            CScreen.clearKey();
        }
        if (CCanvas.keyPressed[6]) {
            this.moveMapGrid(1, 0);
            CScreen.clearKey();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.trans = false;
        if (CCanvas.isTouchOnGamePad(n, n2) || CCanvas.isTouchOnGamePad(CCanvas.pxLast[n3], CCanvas.pyLast[n3])) {
            return;
        }
        int n4 = ITEM_HEIGHT + 5;
        int n5 = (this.cmtoY + n2 - n4) / 57 * this.nBoardPerLine + (n - 10) / 90;
        if (!MotherCanvas.touchDrag && CCanvas.isPointer(0, 0, w, CCanvas.hieght - cmdH, n3)) {
            if (n5 == curMapIndex) {
                if (this.center != null) {
                    if (CCanvas.isDoubleClick) {
                        this.center.action.perform();
                    }
                } else if (this.left != null && CCanvas.isDoubleClick) {
                    this.left.action.perform();
                }
            }
            if (n5 >= 0 && n5 < this.NUMB) {
                curMapIndex = n5;
                this.setCamY();
            }
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!this.trans) {
            this.pa = this.cmy;
            this.trans = true;
        }
        if (!CCanvas.isPc()) {
            this.speed = 3;
        }
        this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2) * this.speed;
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
    }

    public void update() {
    }

    public void mainLoop() {
        super.mainLoop();
        this.moveCamera();
    }

    static {
        imgMap = GameScr.imgMap;
        imgCurPos = GameScr.imgCurPos;
        imgSmallCloud = GameScr.imgSmallCloud;
        imgArrowRed = GameScr.imgArrowRed;
        nMap = MM.NUM_MAP;
        curMapIndex = 0;
        _iconX = new int[MM.NUM_MAP];
        rangeSplit = 100;
        imgMapIW = 80;
        imgMapIH = 50;
        isMoveMenu = false;
        mapX = 0;
        mapY = 0;
        k = 0;
        roundCamera = 80;
    }
}

