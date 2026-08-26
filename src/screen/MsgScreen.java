/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.Image;
import CLib.mGraphics;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import java.util.Vector;
import model.Font;
import model.IAction;
import model.Language;
import model.MsgInfo;
import model.MsgPopup;
import network.Command;
import network.GameService;
import screen.CScreen;
import screen.PrepareScr;

public class MsgScreen
extends CScreen {
    public static Image imgBoard;
    public Vector list = new Vector();
    int selected;
    public int page;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    public int xL;
    public byte roomID;
    public CScreen lastScr;
    int pa = 0;
    boolean trans = false;

    public void show(CScreen cScreen) {
        this.nameCScreen = " MsgScreen screen!";
        this.lastScr = cScreen;
        CCanvas.arrPopups.removeAllElements();
        CCanvas.msgPopup.nMessage = 0;
        super.show();
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy << 2;
            this.cmdy += this.cmvy;
            this.cmy += this.cmdy >> 4;
            this.cmdy &= 0xF;
        }
    }

    public MsgScreen() {
        this.center = new Command(Language.see(), new IAction(){

            public void perform() {
                MsgScreen.this.doDetail();
            }
        });
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                MsgScreen.this.doClose();
            }
        });
        this.left = new Command("Menu", new IAction(){

            public void perform() {
                MsgScreen.this.doShowMenu();
            }
        });
    }

    protected void doShowMenu() {
        Vector<Command> vector = new Vector<Command>();
        vector.addElement(new Command(Language.delete(), new IAction(){

            public void perform() {
                MsgScreen.this.doDelete();
            }
        }));
        vector.addElement(new Command(Language.deleteAll(), new IAction(){

            public void perform() {
                MsgScreen.this.doDeleteAll();
            }
        }));
        vector.addElement(new Command(Language.newMess(), new IAction(){

            public void perform() {
                CCanvas.startOKDlg(Language.toNewMess());
            }
        }));
        vector.addElement(new Command(Language.addFriend(), new IAction(){

            public void perform() {
                MsgScreen.this.doAddFriend();
            }
        }));
        CCanvas.menu.startAt(vector, 0);
    }

    protected void doClose() {
        this.lastScr.show();
    }

    protected void doDetail() {
        if (this.selected >= 0 && this.selected < this.list.size()) {
            MsgInfo msgInfo = (MsgInfo)this.list.elementAt(this.selected);
            IAction iAction = new IAction(){

                public void perform() {
                    MsgScreen.this.doSendMessage();
                }
            };
            CCanvas.msgdlg.setInfo(msgInfo.message, new Command(Language.reply(), iAction), new Command("", iAction), new Command(Language.close(), new IAction(){

                public void perform() {
                    CCanvas.endDlg();
                }
            }));
            CCanvas.msgdlg.show();
        }
    }

    protected void doDeleteAll() {
        this.list.removeAllElements();
    }

    protected void doAddFriend() {
        if (this.selected >= 0 && this.selected < this.list.size()) {
            MsgInfo msgInfo = (MsgInfo)this.list.elementAt(this.selected);
            if (msgInfo.fromID != TerrainMidlet.myInfo.IDDB) {
                GameService.gI().addFriend(msgInfo.fromID);
                CCanvas.startWaitDlg(Language.adding() + "...");
            }
        }
    }

    protected void doDelete() {
        if (this.selected >= 0 && this.selected < this.list.size()) {
            this.list.removeElementAt(this.selected);
        }
        this.cmyLim = this.list.size() * 40 - (CCanvas.hieght - 100);
    }

    protected void doSendMessage() {
        if (this.selected >= 0 && this.selected < this.list.size()) {
            if (CCanvas.waitSendMessage > 0) {
                CCanvas.startOKDlg(Language.justSent());
            } else {
                MsgInfo msgInfo = (MsgInfo)this.list.elementAt(this.selected);
                CCanvas.inputDlg.setInfo(Language.sendTo() + msgInfo.fromName + ":", new IAction(){

                    public void perform() {
                        MsgInfo msgInfo = (MsgInfo)MsgScreen.this.list.elementAt(MsgScreen.this.selected);
                        String string = CCanvas.inputDlg.tfInput.getText();
                        if (string.length() != 0) {
                            GameService.gI().chatTo(msgInfo.fromID, string);
                            msgInfo.isReply = true;
                            CCanvas.startOKDlg(Language.hasSent());
                            CCanvas.waitSendMessage = 100;
                        }
                    }
                }, new IAction(){

                    public void perform() {
                        CCanvas.endDlg();
                    }
                }, 0);
                CCanvas.inputDlg.show();
            }
        }
    }

    public void paint(mGraphics mGraphics2) {
        this.lastScr.paint(mGraphics2);
        mGraphics2.setClip(10, 20, CCanvas.width - 19, CCanvas.hieght - 59);
        mGraphics2.setColor(13302783);
        mGraphics2.fillRect(10, 20, CCanvas.width - 20, CCanvas.hieght - 60, false);
        mGraphics2.setColor(5215093);
        mGraphics2.drawRect(10, 20, CCanvas.width - 20, CCanvas.hieght - 60, false);
        mGraphics2.translate(10, 20);
        mGraphics2.translate(this.xL, 0);
        Font.bigFont.drawString(mGraphics2, Language.MESS(), 10, 3, 0);
        mGraphics2.setColor(1407674);
        mGraphics2.fillRect(1, 25, CCanvas.width - 21, ITEM_HEIGHT, false);
        Font.normalYFont.drawString(mGraphics2, Language.message(), 10, 28, 0);
        Font.normalYFont.drawString(mGraphics2, Language.reply(), CCanvas.width - 45, 28, 2);
        if (this.list.size() == 0) {
            Font.borderFont.drawString(mGraphics2, Language.noMess1(), CCanvas.hw - 10, 50, 2);
            Font.borderFont.drawString(mGraphics2, Language.noMess2(), CCanvas.hw - 10, 75, 2);
            Font.borderFont.drawString(mGraphics2, Language.noMess3(), CCanvas.hw - 10, 90, 2);
        }
        this.paintRichList(mGraphics2);
        super.paint(mGraphics2);
    }

    private void paintRichList(mGraphics mGraphics2) {
        mGraphics2.translate(0, ITEM_HEIGHT + 25);
        mGraphics2.setClip(0, 0, CCanvas.width - 20, CCanvas.hieght - 25 - 21 - 40 - ITEM_HEIGHT);
        mGraphics2.translate(0, -this.cmy);
        int n = 0;
        for (int i = 0; i < this.list.size(); ++i) {
            if (i == this.selected) {
                mGraphics2.setColor(16765440);
                mGraphics2.fillRect(2, n, CCanvas.width - 21 - 2, 38, false);
            }
            MsgInfo msgInfo = (MsgInfo)this.list.elementAt(i);
            mGraphics2.drawImage(MsgPopup.imgMsg[msgInfo.isRead ? 1 : 0], 10, n + 4, 0, false);
            Font.borderFont.drawString(mGraphics2, msgInfo.fromName, 30, n + 3, 0);
            Font.borderFont.drawString(mGraphics2, Font.borderFont.splitFontBStrInLine(msgInfo.message, CCanvas.width - 80)[0], 10, n + ITEM_HEIGHT, 0);
            mGraphics2.drawImage(PrepareScr.imgReady[msgInfo.isReply ? 0 : 1], CCanvas.width - 40, n + 20, 3, false);
            n += 40;
        }
    }

    public void setBoardList(Vector vector) {
        this.list = vector;
        this.cmyLim = vector.size() * 40 - (CCanvas.hieght - 100);
    }

    public void update() {
        if (this.xL != 0) {
            this.xL += -this.xL >> 1;
        }
        if (this.xL == -1) {
            this.xL = 0;
        }
        this.moveCamera();
    }

    public void addMsg(MsgInfo msgInfo) {
        this.list.insertElementAt(msgInfo, 0);
        this.cmyLim = this.list.size() * 40 - (CCanvas.hieght - 100);
    }

    public void onPointerDragged(int n, int n2, int n3) {
        super.onPointerDragged(n, n2, n3);
        if (!this.trans) {
            this.pa = this.cmy;
            this.trans = true;
        }
        this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2);
        if (this.cmtoY > this.cmyLim) {
            this.cmtoY = this.cmyLim;
        }
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.trans = false;
        if (CCanvas.isPointer(0, 20, w, CCanvas.hieght - 60, n3)) {
            int n4 = 20 + 2 * ITEM_HEIGHT;
            int n5 = (this.cmtoY + n2 - n4) / 40;
            if (n5 == this.selected) {
                if (this.center != null) {
                    if (CCanvas.isDoubleClick) {
                        this.center.action.perform();
                    }
                } else if (this.left != null && CCanvas.isDoubleClick) {
                    this.left.action.perform();
                }
            }
            this.selected = n5;
            if (this.selected < 0) {
                this.selected = 0;
            }
            if (this.selected >= this.list.size()) {
                this.selected = this.list.size() - 1;
            }
        }
    }

    public void onPointerHolder(int n, int n2, int n3) {
    }

    public void input() {
    }
}

