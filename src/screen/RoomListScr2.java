/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mImage;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.MotherCanvas;
import com.teamobi.mobiarmy2.OfflineBossReward;
import coreLG.CCanvas;
import effect.Cloud;
import java.util.Vector;
import model.CRes;
import model.Font;
import model.IAction;
import model.Language;
import model.RoomInfo;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.GameScr;
import screen.PrepareScr;
import screen.RoomListScr;

public class RoomListScr2
extends CScreen {
    private static RoomListScr instance;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    int hTab = 0;
    public int selected;
    public static String[] roomLevelText;
    public String title = "Room";
    public static mImage imgRoomStat;
    public static mImage imgRoom;
    public static mImage imgTrs;
    public static mImage imgIcon;
    public static Command cmdFriendList;
    public static Command findRoom;
    public Vector roomList = new Vector();
    public boolean isEmptyRoom;
    public boolean isOfflineBossList;
    int pa = 0;
    boolean trans = false;

    public static RoomListScr gI() {
        if (instance == null) {
            instance = new RoomListScr();
        }
        return instance;
    }

    public RoomListScr2() {
        this.nameCScreen = " => RoomListScr2";
        this.center = new Command(Language.enter(), new IAction(){

            public void perform() {
                RoomListScr2.this.doSelectRoom();
            }
        });
        findRoom = new Command(Language.findArea(), new IAction(){
            int roomID;
            int zoneID;

            public void perform() {
                CCanvas.inputDlg.setInfo(Language.nhapSoPhong(), new IAction(){

                    public void perform() {
                        if (CCanvas.inputDlg.tfInput.getText() != null && !CCanvas.inputDlg.tfInput.getText().equals("")) {
                            roomID = Integer.parseInt(CCanvas.inputDlg.tfInput.getText());
                            CCanvas.inputDlg.setInfo(Language.nhapKhuVuc(), new IAction(){

                                public void perform() {
                                    if (CCanvas.inputDlg.tfInput.getText() != null && !CCanvas.inputDlg.tfInput.getText().equals("")) {
                                        zoneID = Integer.parseInt(CCanvas.inputDlg.tfInput.getText());
                                        GameService.gI().requestEmptyRoom((byte)2, (byte)-1, String.valueOf(roomID * 1000 + zoneID));
                                        CCanvas.endDlg();
                                        CCanvas.startOKDlg(Language.pleaseWait());
                                    }
                                }
                            }, new IAction(){

                                public void perform() {
                                    CCanvas.endDlg();
                                }
                            }, 1);
                        }
                    }
                }, new IAction(){

                    public void perform() {
                        CCanvas.endDlg();
                    }
                }, 1);
                CCanvas.inputDlg.show();
            }
        });
        this.left = new Command("Menu", new IAction(){

            public void perform() {
                Vector<Command> vector = new Vector<Command>();
                if (RoomListScr2.this.isOfflineBossList) {
                    vector.addElement(new Command("Ph\u1ea7n th\u01b0\u1edfng", new IAction(){

                        public void perform() {
                            CCanvas.endDlg();
                            if (RoomListScr2.this.selected >= 0 && RoomListScr2.this.selected < RoomListScr2.this.roomList.size()) {
                                RoomInfo roomInfo = (RoomInfo)RoomListScr2.this.roomList.elementAt(RoomListScr2.this.selected);
                                if (roomInfo.id != -1) {
                                    CCanvas.startOKDlg(OfflineBossReward.describeRewards(roomInfo.lv));
                                }
                            }
                        }
                    }));
                } else {
                    vector.addElement(new Command(Language.update(), new IAction(){

                        public void perform() {
                            RoomListScr2.this.doUpdate();
                        }
                    }));
                    vector.addElement(findRoom);
                }
                CCanvas.menu.startAt(vector, 0);
            }
        });
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                CCanvas.curScr = null;
                CCanvas.menuScr.show();
            }
        });
    }

    private void doUpdate() {
        GameService.gI().requestEmptyRoom((byte)0, (byte)-1, null);
        CCanvas.startWaitDlg(Language.pleaseWait());
    }

    protected void doSelectRoom() {
        if (this.selected != -1) {
            RoomInfo roomInfo = (RoomInfo)this.roomList.elementAt(this.selected);
            if (roomInfo.id == -1) {
                return;
            }
            if (this.isOfflineBossList) {
                CCanvas.endDlg();
                GameMidlet.openOfflineBossRoom(roomInfo.lv);
                return;
            }
            CCanvas.startOKDlg("Khu v\u1ef1c th\u01b0\u1eddng ch\u01b0a h\u1ed7 tr\u1ee3 offline. H\u00e3y d\u00f9ng \u0110\u1ea4U BOSS.");
            return;
        }
    }

    public void show() {
        super.show();
    }

    public void paint(mGraphics mGraphics2) {
        RoomListScr2.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        for (int i = 0; i <= CCanvas.width; i += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, i, CCanvas.hieght - 62, 0, false);
        }
        this.paintRoomList(mGraphics2);
        super.paint(mGraphics2);
    }

    public void changeName(int n, String string) {
        if (this.roomList == null || string == null) {
            return;
        }
        if (n >= 0 && n < this.roomList.size()) {
            ((RoomInfo)this.roomList.elementAt((int)n)).name = string;
        }
    }

    public void updateRoomName(byte by, byte by2, String string) {
        if (this.roomList == null || string == null) {
            return;
        }
        RoomInfo roomInfo = null;
        for (int i = 0; i < this.roomList.size(); ++i) {
            RoomInfo roomInfo2 = (RoomInfo)this.roomList.elementAt(i);
            if (roomInfo2.id == -1) continue;
            if (roomInfo2.id == by && roomInfo2.lv == by2) {
                roomInfo2.name = string;
                return;
            }
            if (roomInfo2.id != by) continue;
            roomInfo = roomInfo2;
        }
        if (roomInfo != null) {
            roomInfo.name = string;
            return;
        }
        this.changeName(by + by2 + 1, string);
    }

    private void paintRoomList(mGraphics mGraphics2) {
        if (CCanvas.isTouch) {
            mGraphics2.translate(0, 6);
        }
        mGraphics2.translate(0, -this.cmy);
        int n = 0;
        int n2 = CCanvas.isTouch ? 10 : 0;
        for (int i = 0; i < this.roomList.size(); ++i) {
            int n3;
            RoomInfo roomInfo = (RoomInfo)this.roomList.elementAt(i);
            if (i == this.selected && roomInfo.id != -1) {
                mGraphics2.setColor(16767817);
                mGraphics2.fillRect(1, n + 3 - n2, CCanvas.width - 2, ITEM_HEIGHT + 2 * n2, false);
            }
            if (roomInfo.id != -1) {
                try {
                    n3 = RoomListScr2.imgIcon.image.getHeight() / 14 - roomInfo.lv;
                    mGraphics2.drawRegion(imgIcon, 0, 14 * n3, 12, 14, 0, 10, n + 6, 0, false);
                }
                catch (Exception exception) {
                    mGraphics2.drawRegion(imgIcon, 0, 0 * roomInfo.lv, 12, 14, 0, 10, n + 6, 0, false);
                }
                if (CRes.isNullOrEmpty(roomInfo.name)) {
                    Font.borderFont.drawString(mGraphics2, Language.room() + " " + roomInfo.id, 30, n + 5, 0);
                } else {
                    Font.borderFont.drawString(mGraphics2, roomInfo.name, 30, n + 5, 0);
                }
                if (roomInfo.boardID != -1) {
                    Font.borderFont.drawString(mGraphics2, roomInfo.playerMax, CCanvas.width - 5, n + 5, 1);
                }
                if (roomInfo.money != 0) {
                    Font.borderFont.drawString(mGraphics2, roomInfo.money + " " + Language.xu(), CCanvas.width - 35, n + 5, 1);
                }
                n += CCanvas.isTouch ? 40 : ITEM_HEIGHT;
                continue;
            }
            if (!CCanvas.isTouch) {
                for (n3 = 0; n3 <= CCanvas.width / RoomListScr2.imgTrs.image.getWidth(); ++n3) {
                    mGraphics2.drawImage(imgTrs, n3 * RoomListScr2.imgTrs.image.getWidth(), n + 2, 0, false);
                }
            }
            Font.bigFont.drawString(mGraphics2, roomInfo.name.toUpperCase(), 10, n + 3, 0);
            n += CCanvas.isTouch ? 43 : ITEM_HEIGHT + 3;
        }
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy << 2;
            this.cmdy += this.cmvy;
            this.cmy += this.cmdy >> 4;
            this.cmdy &= 0xF;
        }
        if (Math.abs(this.cmtoY - this.cmy) < 15 && this.cmy < 0) {
            this.cmtoY = 0;
        }
        if (Math.abs(this.cmtoY - this.cmy) < 10 && this.cmy > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
    }

    public void update() {
        super.update();
        Cloud.updateCloud();
    }

    public void mainLoop() {
        super.mainLoop();
        this.moveCamera();
    }

    public void setRoomList(Vector vector) {
        this.roomList = vector;
        this.setCam();
    }

    private void setCam() {
        this.cmtoY = 0;
        this.cmy = 0;
        this.selected = this.isOfflineBossList ? 0 : 1;
        int n = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        this.cmyLim = this.roomList.size() * n - (CCanvas.hieght - 70 - this.hTab);
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
    }

    public static void quickSort(Vector vector) {
        RoomListScr2.recQuickSort(vector, 0, vector.size() - 1);
    }

    private static void recQuickSort(Vector vector, int n, int n2) {
        if (n2 - n > 0) {
            byte by = ((RoomInfo)vector.elementAt((int)n2)).lv;
            int n3 = RoomListScr2.partitionIt(vector, n, n2, by);
            RoomListScr2.recQuickSort(vector, n, n3 - 1);
            RoomListScr2.recQuickSort(vector, n3 + 1, n2);
        }
    }

    private static int partitionIt(Vector vector, int n, int n2, int n3) {
        int n4 = n - 1;
        int n5 = n2;
        while (true) {
            if (((RoomInfo)vector.elementAt((int)(++n4))).lv < n3) {
                continue;
            }
            while (n5 > 0 && ((RoomInfo)vector.elementAt((int)(--n5))).lv > n3) {
            }
            if (n4 >= n5) {
                RoomListScr2.swap(vector, n4, n2);
                return n4;
            }
            RoomListScr2.swap(vector, n4, n5);
        }
    }

    private static void swap(Vector vector, int n, int n2) {
        Object e = vector.elementAt(n2);
        vector.setElementAt(vector.elementAt(n), n2);
        vector.setElementAt(e, n);
    }

    private void updateScrollToSelected() {
        int n = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        this.cmtoY = this.selected * n - (CCanvas.hh - 2 * ITEM_HEIGHT);
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
        if (this.selected == this.roomList.size() - 1 || this.selected == 0) {
            this.cmy = this.cmtoY;
        }
    }

    private void moveSelection(int n) {
        if (this.roomList.size() == 0) {
            return;
        }
        this.selected += n;
        if (this.selected < 0) {
            this.selected = this.roomList.size() - 1;
        }
        if (this.selected >= this.roomList.size()) {
            this.selected = 0;
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
        this.trans = false;
        if (CCanvas.isTouchOnGamePad(n, n2) || CCanvas.isTouchOnGamePad(CCanvas.pxLast[n3], CCanvas.pyLast[n3])) {
            return;
        }
        int n4 = 5;
        int n5 = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        int n6 = (this.cmtoY + n2 - n4) / n5;
        if (!MotherCanvas.touchDrag && CCanvas.isPointer(0, 0, w, CCanvas.hieght - cmdH, n3)) {
            if (n6 == this.selected) {
                if (this.center != null) {
                    if (CCanvas.isDoubleClick) {
                        this.center.action.perform();
                    }
                } else if (this.left != null && CCanvas.isDoubleClick) {
                    this.left.action.perform();
                }
            }
            if (n6 >= 0 && n6 < this.roomList.size()) {
                this.selected = n6;
                this.updateScrollToSelected();
            }
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!this.trans) {
            this.pa = this.cmy;
            this.trans = true;
        }
        this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2);
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
    }

    static {
        roomLevelText = null;
        imgRoomStat = GameScr.imgRoomStat;
        imgTrs = GameScr.imgTrs;
        imgIcon = GameScr.imgIcon;
    }
}

