/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.Image;
import CLib.mGraphics;
import CLib.mImage;
import com.teamobi.mobiarmy2.GameMidlet;
import coreLG.CCanvas;
import effect.Cloud;
import java.util.Vector;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import network.Command;
import network.GameService;
import player.CPlayer;
import screen.CScreen;
import screen.MenuScr;
import screen.PrepareScr;

public class ListScr
extends CScreen {
    public static mImage imgBoard;
    Vector list;
    int selected;
    public int page;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    public byte roomID;
    public int type = 0;
    Command cmdAddFriend;
    Command cmdDeleteFriend;
    Command cmdRefresh;
    Command cmdNext;
    private String[] title = Language.top();
    private String[] bangxephang = new String[]{Language.topCaothu(), Language.topDaiGiaXu(), Language.topDaigiaLuong(), Language.topCaothuTuan(), Language.topXuTuan()};
    public boolean isArmy2;
    int disY = 40;
    boolean isNext;
    public String typeList;
    int pa = 0;
    boolean trans = false;
    int pxFirst;

    public void getPlayerIcon(short s, Image image) {
        for (int i = 0; i < this.list.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.list.elementAt(i);
            if (playerInfo.clanID != s) continue;
            playerInfo.clanIcon = new mImage(image);
        }
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy >> 2;
            this.cmy += this.cmvy;
        }
    }

    public ListScr() {
        this.nameCScreen = " ListScr screen!";
        this.cmdAddFriend = new Command(Language.addFriend(), new IAction(){

            public void perform() {
                ListScr.this.doAddFriend();
            }
        });
        this.cmdRefresh = new Command(Language.update(), new IAction(){

            public void perform() {
                ListScr.this.doRefresh();
            }
        });
        this.cmdNext = new Command(Language.more(), new IAction(){

            public void perform() {
                ListScr.this.doNext();
            }
        });
        this.cmdDeleteFriend = new Command(Language.deleteFriend(), new IAction(){

            public void perform() {
                ListScr.this.doDeleteFriend();
            }
        });
    }

    protected void doNext() {
        this.isNext = true;
        if (this.type != 2 && this.type != 3) {
            if (this.type <= 0) {
                GameService.gI().bangxephang((byte)(-this.type), this.page + 1);
            } else if (this.type == 5) {
                GameService.gI().clanMember((byte)(this.page + 1), CCanvas.clanScreen.clan.id);
                CCanvas.startWaitDlg(Language.gettingList());
            } else {
                GameService.gI().requestStrongest(this.page + 1);
                CCanvas.startWaitDlg(Language.gettingList());
            }
        }
    }

    protected void doBack() {
        this.isNext = true;
        if (this.type != 2 && this.type != 3 && this.page >= 0) {
            if (this.type <= 0) {
                GameService.gI().bangxephang((byte)(-this.type), this.page - 1);
            } else if (this.type == 5) {
                GameService.gI().clanMember((byte)(this.page - 1), CCanvas.clanScreen.clan.id);
                CCanvas.startWaitDlg(Language.gettingList());
            } else {
                GameService.gI().requestStrongest(this.page - 1);
                CCanvas.startWaitDlg(Language.gettingList());
            }
        }
    }

    protected void doRefresh() {
        this.isNext = false;
        if (this.type <= 0) {
            GameService.gI().bangxephang((byte)(-this.type), this.page);
        } else if (this.type == 3) {
            GameService.gI().inviteFriend(true, -1);
            CCanvas.startWaitDlg(Language.gettingList());
        } else if (this.type == 5) {
            GameService.gI().clanMember((byte)this.page, CCanvas.clanScreen.clan.id);
            CCanvas.startWaitDlg(Language.gettingList());
        } else {
            GameService.gI().requestStrongest(this.page);
            CCanvas.startWaitDlg(Language.gettingList());
        }
    }

    protected void doDeleteFriend() {
        int n = ((PlayerInfo)this.list.elementAt((int)this.selected)).IDDB;
        GameService.gI().deleteFriend(n);
        CCanvas.startWaitDlg(Language.deleting());
    }

    protected void doAddFriend() {
        CCanvas.inputDlg.setInfo(Language.inputName(), new IAction(){

            public void perform() {
                if (CCanvas.inputDlg.tfInput.getText().length() < 4) {
                    CCanvas.startOKDlg(Language.input4());
                } else {
                    CCanvas.startWaitDlg(Language.searching());
                    GameService.gI().searchFriend(CCanvas.inputDlg.tfInput.getText());
                }
            }
        }, new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }, 3);
        CCanvas.inputDlg.show();
    }

    protected void doShowMenu() {
        Vector<Command> vector = new Vector<Command>();
        if (this.type == 2) {
            vector.addElement(this.cmdAddFriend);
            if (this.selected >= 0 && this.selected < this.list.size()) {
                vector.addElement(this.cmdDeleteFriend);
            }
        } else {
            vector.addElement(this.cmdRefresh);
            vector.addElement(this.cmdNext);
        }
        CCanvas.menu.startAt(vector, 0);
    }

    protected void doSendMessage() {
        if (this.selected >= 0 && this.selected < this.list.size()) {
            if (CCanvas.waitSendMessage > 0) {
                CCanvas.startOKDlg(Language.justSent());
            } else {
                PlayerInfo playerInfo = (PlayerInfo)this.list.elementAt(this.selected);
                CCanvas.inputDlg.setInfo(Language.sendTo() + playerInfo.name + ":", new IAction(){

                    public void perform() {
                        PlayerInfo playerInfo = (PlayerInfo)ListScr.this.list.elementAt(ListScr.this.selected);
                        String string = CCanvas.inputDlg.tfInput.getText();
                        if (string.length() != 0) {
                            GameService.gI().chatTo(playerInfo.IDDB, string);
                            CCanvas.startOKDlg(Language.hasSent());
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

    private void doExit() {
        CCanvas.curScr = null;
        lastSCreen.show();
    }

    public void paint(mGraphics mGraphics2) {
        ListScr.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        for (int i = 0; i <= CCanvas.width; i += 32) {
            mGraphics2.drawImage(PrepareScr.imgBack, i, CCanvas.hieght - 62, 0, false);
        }
        this.paintRichList(mGraphics2);
        mGraphics2.translate(0, -mGraphics2.getTranslateY());
        mGraphics2.setColor(6606845);
        mGraphics2.fillRect(0, 0, CCanvas.width, 25 + ITEM_HEIGHT, false);
        mGraphics2.setColor(1407674);
        mGraphics2.fillRect(0, 25, CCanvas.width, ITEM_HEIGHT, false);
        if (this.type != 5) {
            if (this.type > 0) {
                Font.bigFont.drawString(mGraphics2, this.title[this.type], CCanvas.width / 2, 3, mGraphics.HCENTER | mGraphics.TOP);
                Font.normalYFont.drawString(mGraphics2, Language.name(), 10, 28, 0);
            } else {
                Font.bigFont.drawString(mGraphics2, MenuScr.subMenuString[7][-this.type], CCanvas.width / 2, 3, mGraphics.HCENTER | mGraphics.TOP);
                if (MenuScr.subMenuString[7][-this.type].equals("DANH S\u00c1CH")) {
                    mGraphics2.drawImage(cup, CCanvas.width / 2 - 60, 4, 0, false);
                }
                Font.normalYFont.drawString(mGraphics2, Language.name(), 10, 28, 0);
                Font.normalYFont.drawString(mGraphics2, this.typeList, CCanvas.width - 10, 28, 1);
            }
        } else {
            String string = CCanvas.clanScreen.clan.name;
            Font.bigFont.drawString(mGraphics2, Language.THANHVIENDOI(), 10, 3, 0);
            if (Font.normalYFont.getWidth(string) > CCanvas.width - 20) {
                string = Font.normalYFont.splitFontBStrInLine(string, CCanvas.width - 20)[0] + "...";
            }
            Font.normalYFont.drawString(mGraphics2, string, 10, 28, 0);
        }
        if (this.list.size() == 0 && this.type == 2) {
            Font.borderFont.drawString(mGraphics2, Language.chuacoban(), CCanvas.hw, 50, 2);
            Font.borderFont.drawString(mGraphics2, Language.xinchonmenu(), CCanvas.hw, 75, 2);
            Font.borderFont.drawString(mGraphics2, Language.themtuphongcho(), CCanvas.hw, 90, 2);
        }
        super.paint(mGraphics2);
    }

    private void paintRichList(mGraphics mGraphics2) {
        mGraphics2.translate(0, ITEM_HEIGHT + 25);
        mGraphics2.translate(0, -this.cmy);
        int n = 0;
        for (int i = 0; i < this.list.size(); ++i) {
            if (i == this.selected) {
                mGraphics2.setColor(16767817);
                mGraphics2.fillRect(0, n, CCanvas.width, this.disY, true);
            }
            if (i * this.disY + this.disY > -mGraphics2.getTranslateY() && i * this.disY < -mGraphics2.getTranslateY() + CScreen.h) {
                PlayerInfo playerInfo = (PlayerInfo)this.list.elementAt(i);
                String string = playerInfo.name;
                int n2 = this.type != 2 && this.type != 5 ? 0 : 9;
                String string2 = PlayerInfo.strLevelCaption != null && playerInfo.nQuanHam2 >= 0 && playerInfo.nQuanHam2 < PlayerInfo.strLevelCaption.length ? PlayerInfo.strLevelCaption[playerInfo.nQuanHam2] : "";
                String string3 = playerInfo.STT + "." + string2;
                if (playerInfo.level2 != 0) {
                    string3 = string3 + " (" + playerInfo.level2 + (playerInfo.level2Percen >= 0 ? "+" : "") + playerInfo.level2Percen + "%)";
                }
                Font.borderFont.drawString(mGraphics2, string3, 30 + n2, n + 3, 0);
                if (playerInfo.aa != null && playerInfo.aa != "") {
                    Font.borderFont.drawString(mGraphics2, playerInfo.aa, CCanvas.width - 5, n + 19, 1);
                }
                if (playerInfo.clanIcon != null) {
                    mGraphics2.drawImage(playerInfo.clanIcon, 30, n + 20, 0, true);
                    Font.borderFont.drawString(mGraphics2, string, 45, n + 19, 0);
                } else {
                    Font.borderFont.drawString(mGraphics2, string, 30, n + 19, 0);
                }
                CPlayer.paintSimplePlayer(playerInfo.gun, 0, 12, n + (this.type != 5 ? 35 : 30), 2, playerInfo.myEquip, mGraphics2);
                PrepareScr.paintQuanHam(playerInfo.nQuanHam2, 22, n + (this.type != 5 ? 10 : 5), mGraphics.VCENTER | mGraphics.HCENTER, mGraphics2);
                if (this.type == 2 || this.type == 5) {
                    mGraphics2.drawImage(PrepareScr.imgReady[playerInfo.isReady ? 0 : 1], 33, n + 12, 3, false);
                }
                if (this.type == 5) {
                    Font.borderFont.drawString(mGraphics2, playerInfo.clanContribute1, 5, n + 35, 0);
                    Font.borderFont.drawString(mGraphics2, playerInfo.clanContribute2, 5, n + 51, 0);
                    mGraphics2.drawImage(cup, CCanvas.width - 5, n + 20, mGraphics.TOP | mGraphics.RIGHT, false);
                    Font.borderFont.drawString(mGraphics2, String.valueOf(playerInfo.cup), CCanvas.width - 10, n + 40, 1);
                }
            }
            n += this.disY;
        }
    }

    public void update() {
        Cloud.updateCloud();
        this.moveCamera();
    }

    public void show(CScreen cScreen) {
        if (this.isNext) {
            this.cmtoY = 0;
            this.cmy = 0;
            this.selected = 0;
        }
        super.show(cScreen);
        for (int i = 0; i < this.list.size(); ++i) {
            PlayerInfo playerInfo = (PlayerInfo)this.list.elementAt(i);
            GameService.gI().getClanIcon(playerInfo.clanID);
        }
    }

    public void setList(int n, Vector vector) {
        this.type = n;
        this.list = vector;
        this.selected = 0;
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
        this.selected = 0;
        this.cmtoY = 0;
        this.cmy = 0;
        this.center = new Command(Language.sendMess(), new IAction(){

            public void perform() {
                ListScr.this.doSendMessage();
            }
        });
        this.left = n == 2 ? new Command(Language.update(), new IAction(){

            public void perform() {
                ListScr.this.doShowMenu();
            }
        }) : this.cmdRefresh;
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                ListScr.this.doExit();
            }
        });
        this.disY = 40 + (n != 5 ? 0 : 30);
        this.cmyLim = vector.size() * this.disY - (CCanvas.hieght - (ITEM_HEIGHT * 2 + cmdH)) + 10;
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
    }

    public void doInvite() {
        if (this.list != null && this.list.size() > 0 && this.selected < this.list.size() && this.selected >= 0) {
            PlayerInfo playerInfo = (PlayerInfo)this.list.elementAt(this.selected);
            GameService.gI().inviteFriend(false, playerInfo.IDDB);
            CCanvas.startOKDlg(Language.invited());
        }
    }

    public void setInviteList(int n, Vector vector) {
        this.type = n;
        this.list = vector;
        this.cmyLim = vector.size() * 57 - (CCanvas.hieght - ITEM_HEIGHT * 3 - 7);
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
        this.selected = 0;
        this.cmtoY = 0;
        this.cmy = 0;
        this.center = new Command(Language.moichoi(), new IAction(){

            public void perform() {
                ListScr.this.doInvite();
            }
        });
        this.left = this.cmdRefresh;
        this.right = new Command(Language.close(), new IAction(){

            public void perform() {
                ListScr.this.doExit();
            }
        });
    }

    public void doUpdate() {
    }

    public void setFriendFind() {
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

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.trans = false;
        int n4 = ITEM_HEIGHT;
        int n5 = CCanvas.isTouch ? 40 : ITEM_HEIGHT;
        int n6 = (this.cmtoY - ITEM_HEIGHT + n2 - n4) / 40;
        if (n6 == this.selected) {
            if (this.center != null) {
                if (CCanvas.isDoubleClick) {
                    this.center.action.perform();
                }
            } else if (this.left != null && CCanvas.isDoubleClick) {
                this.left.action.perform();
            }
        }
        if (n6 >= 0 && n6 < this.list.size()) {
            this.selected = n6;
        }
        if (CCanvas.isPointer(0, 0, w, CCanvas.hieght - cmdH, n3) && Math.abs(n - CCanvas.pxFirst[n3]) > 50 && GameMidlet.server != 2) {
            if (n > CCanvas.pxFirst[n3]) {
                this.doNext();
            } else {
                this.doBack();
            }
        }
    }
}

