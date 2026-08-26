/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Cloud;
import java.util.Vector;
import model.CRes;
import model.Clan;
import model.Font;
import model.IAction;
import model.Language;
import model.Position;
import network.Command;
import network.GameService;
import screen.CScreen;

public class ClanScreen
extends CScreen {
    Vector clans = new Vector();
    public int selected;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    public int type;
    public static final int TOP_CLAN = 0;
    public static final int CLAN_INFO = 1;
    public Clan clan;
    public static boolean isFromMenu;
    public static boolean backToTop;
    public int[] endTime;
    public long[] dieTime;
    public long[] currentTime;
    public String[] strEndTime;
    Position transText1 = new Position(0, 1);
    Position transText2 = new Position(0, 1);
    Position transText3 = new Position(0, 1);
    Position transText4 = new Position(0, 1);
    public byte page;
    int pa = 0;
    boolean trans = false;

    public Position transTextLimit(Position position, int n) {
        position.x += position.y;
        if (position.y == -1 && Math.abs(position.x) > n) {
            position.y *= -1;
        }
        if (position.y == 1 && position.x > 5) {
            position.y *= -1;
        }
        return position;
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy << 2;
            this.cmdy += this.cmvy;
            this.cmy += this.cmdy >> 4;
            this.cmdy &= 0xF;
        }
    }

    public ClanScreen(int n) {
        this.type = n;
        this.nameCScreen = "ClanScreen screen!";
    }

    public void doDetail() {
        GameService.gI().clanInfo(this.clanSelected().id);
        CCanvas.startOKDlg(Language.pleaseWait(), new IAction(){

            public void perform() {
                CRes.out(" ======> Ko respond server ");
            }
        });
        backToTop = true;
    }

    public void getClanList(byte by, Vector vector) {
        this.clans = vector;
        this.page = by;
        for (int i = 0; i < this.clans.size(); ++i) {
            Clan clan = (Clan)this.clans.elementAt(i);
            GameService.gI().getClanIcon(clan.id);
        }
        this.cmyLim = this.clans.size() * 50 - (CCanvas.hieght - (ITEM_HEIGHT + cmdH) - 12);
        if (this.cmyLim < 0) {
            this.cmyLim = 0;
        }
    }

    public Clan clanSelected() {
        Clan clan = (Clan)this.clans.elementAt(this.selected);
        return clan;
    }

    public Clan findClan(int n) {
        for (int i = 0; i < this.clans.size(); ++i) {
            Clan clan = (Clan)this.clans.elementAt(i);
            if (clan.id != n) continue;
            return clan;
        }
        return null;
    }

    public void paint(mGraphics mGraphics2) {
        String string;
        String string2;
        String string3;
        int n;
        int n2;
        int n3;
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
        ClanScreen.paintDefaultBg(mGraphics2);
        Cloud.paintCloud(mGraphics2);
        if (this.type == 0) {
            Font.bigFont.drawString(mGraphics2, Language.TOPCLAN(), CCanvas.width / 2, 3, 3);
            mGraphics2.translate(0, ITEM_HEIGHT + 5);
            mGraphics2.translate(0, -this.cmy);
            n3 = 0;
            for (n2 = 0; n2 < this.clans.size(); ++n2) {
                mGraphics2.setClip(0, this.cmy, CCanvas.width, CCanvas.hieght - 2 * ITEM_HEIGHT - 5 + this.cmy);
                if (n2 == this.selected) {
                    mGraphics2.setColor(16765440);
                    mGraphics2.fillRect(0, n3, CCanvas.width, 49, true);
                }
                Clan clan = (Clan)this.clans.elementAt(n2);
                if (clan.icon != null) {
                    mGraphics2.drawImage(clan.icon, 4, n3 + 1, 0, true);
                } else {
                    mGraphics2.drawImage(CRes.empty, 4, n3 + 1, 0, true, true);
                }
                n3 = 0;
                n = Font.normalFont.getWidth(clan.name);
                int n4 = Font.normalFont.getWidth(Language.doitruong() + ": " + clan.master);
                if (n2 == this.selected) {
                    if (n > CCanvas.width - 30) {
                        this.transTextLimit(this.transText2, n - (CCanvas.width - 30));
                    }
                    n3 = this.transText2.x;
                    if (n4 > CCanvas.width - 20) {
                        this.transTextLimit(this.transText3, n4 - (CCanvas.width - 20));
                    }
                    n2 = this.transText3.x;
                }
                string3 = clan.name;
                string2 = "Level: " + clan.level + "+" + clan.percen + "%";
                string = Language.doitruong() + ": " + clan.master;
                mGraphics2.setClip(20, this.cmy, CCanvas.width, CCanvas.hieght - 2 * ITEM_HEIGHT - 5 + this.cmy);
                Font.borderFont.drawString(mGraphics2, string3, 20 + n3, n3, 0);
                mGraphics2.setClip(5, this.cmy, CCanvas.width, CCanvas.hieght - 2 * ITEM_HEIGHT - 5 + this.cmy);
                Font.normalFont.drawString(mGraphics2, string2, 7, n3 + 34, 0);
                mGraphics2.setClip(0, this.cmy, CCanvas.width, CCanvas.hieght - 2 * ITEM_HEIGHT - 5 + this.cmy);
                Font.normalFont.drawString(mGraphics2, string, 7 + n2, n3 + 17, 0);
                mGraphics2.drawImage(cup, CCanvas.width - 25, n3 + 16, 0, true);
                Font.borderFont.drawString(mGraphics2, String.valueOf(clan.cup), CCanvas.width - 30, n3 + 17, 1);
                n3 += 50;
            }
        }
        if (this.type == 1) {
            mGraphics2.translate(0, -this.cmy);
            int n5 = Font.normalFont.getWidth(this.clan.name);
            n3 = Font.normalFont.getWidth("( " + this.clan.slogan + " )");
            if (n5 > CCanvas.width - 20) {
                this.transTextLimit(this.transText1, n5 - (CCanvas.width - 40));
            }
            if (n3 > CCanvas.width - 20) {
                this.transTextLimit(this.transText4, n3 - (CCanvas.width - 40));
            }
            n3 = this.transText1.x;
            n2 = this.transText4.x;
            if (n5 > CCanvas.width - 20) {
                Font.borderFont.drawString(mGraphics2, this.clan.name, 20 + n3, 15, 0);
            } else {
                Font.borderFont.drawString(mGraphics2, this.clan.name, CCanvas.width / 2 + 5, 15, 3);
            }
            if (n3 > CCanvas.width - 20) {
                Font.borderFont.drawString(mGraphics2, "( " + this.clan.slogan + " )", 20 + n2, 35, 0);
            } else {
                Font.borderFont.drawString(mGraphics2, "( " + this.clan.slogan + " )", CCanvas.width / 2, 35, 3);
            }
            if (this.clan.icon != null) {
                n = Font.borderFont.getWidth(this.clan.name);
                if (n5 > CCanvas.width - 20) {
                    mGraphics2.drawImage(this.clan.icon, 5 + n3, 17, 0, true);
                } else {
                    mGraphics2.drawImage(this.clan.icon, CCanvas.width / 2 - n / 2 - 10, 17, 0, true);
                }
            }
            mGraphics2.drawImage(cup, CCanvas.width / 2 - 20, 55, 0, true);
            Font.borderFont.drawString(mGraphics2, String.valueOf(this.clan.cup), CCanvas.width / 2 + 2, 56, 0);
            String string4 = Language.thanhvien() + ": " + this.clan.count + "/" + this.clan.max;
            string2 = Language.ngansach() + ": " + this.clan.money + Language.xu() + " - " + this.clan.money2 + Language.luong();
            String string5 = "Level: " + this.clan.level + "+" + this.clan.percen + "%";
            string3 = Language.ngaythanhlap() + ": " + this.clan.date;
            Font.normalFont.drawString(mGraphics2, string4, CCanvas.width / 2, 75, 3);
            Font.normalFont.drawString(mGraphics2, string2, CCanvas.width / 2, 90, 3);
            Font.normalFont.drawString(mGraphics2, string5, CCanvas.width / 2, 105, 3);
            mGraphics2.setColor(1521982);
            mGraphics2.fillRect(CCanvas.width / 2 - 54, 120, 110, 17, true);
            mGraphics2.setColor(2378093);
            mGraphics2.fillRect(CCanvas.width / 2 - 54 + 1, 121, 108, 15, true);
            int n6 = this.clan.percen * 108 / 100;
            mGraphics2.setColor(16767817);
            mGraphics2.fillRect(CCanvas.width / 2 - 54 + 1, 121, n6, 15, true);
            Font.borderFont.drawString(mGraphics2, this.clan.exp + "/" + this.clan.nextExp, CCanvas.width / 2 - 54 + 55, 120, 2);
            Font.normalFont.drawString(mGraphics2, string3, CCanvas.width / 2, 140, 3);
            if (this.clan.item.length == 0) {
                string = Language.clanItem();
                String[] stringArray = Font.normalFont.splitFontBStrInLine(string, CCanvas.width - 50);
                for (int i = 0; i < stringArray.length; ++i) {
                    Font.borderFont.drawString(mGraphics2, stringArray[i], CCanvas.width / 2, 185 + i * 15, 3);
                }
            } else {
                int[] nArray = new int[this.clan.item.length];
                for (int i = 0; i < this.clan.item.length; ++i) {
                    if (this.dieTime[i] != 0L) {
                        this.currentTime[i] = System.currentTimeMillis() / 1000L;
                        if (this.currentTime[i] > this.dieTime[i]) {
                            this.dieTime[i] = 0L;
                        }
                        long l = this.currentTime[i] - this.dieTime[i];
                        nArray[i] = (int)((long)nArray[i] + -l);
                        this.strEndTime[i] = CRes.formatIntoDDHHMMSS(nArray[i], true);
                    }
                    Font.borderFont.drawString(mGraphics2, this.clan.item[i], CCanvas.width / 2, 165 + i * 30, 3);
                    Font.normalFont.drawString(mGraphics2, Language.time() + ": " + this.strEndTime[i], CCanvas.width / 2, 180 + i * 30, 3);
                }
            }
        }
        mGraphics2.translate(-mGraphics2.getTranslateX(), -mGraphics2.getTranslateY());
        super.paint(mGraphics2);
    }

    protected void doNext() {
        GameService.gI().topClan((byte)(this.page + 1));
        CCanvas.startOKDlg(Language.gettingList());
    }

    protected void doRefresh() {
        GameService.gI().topClan(this.page);
        CCanvas.startOKDlg(Language.gettingList());
    }

    public void commandInit() {
        if (this.type == 0) {
            this.center = new Command(Language.detail(), new IAction(){

                public void perform() {
                    ClanScreen.this.doDetail();
                }
            });
            this.right = new Command(Language.back(), new IAction(){

                public void perform() {
                    CRes.out("isFrommenu= " + isFromMenu);
                    CCanvas.menuScr.show();
                }
            });
            this.left = new Command("Menu", new IAction(){

                public void perform() {
                    Vector<Command> vector = new Vector<Command>();
                    Command command = new Command(Language.update(), new IAction(){

                        public void perform() {
                            ClanScreen.this.doRefresh();
                        }
                    });
                    Command command2 = new Command(Language.more(), new IAction(){

                        public void perform() {
                            ClanScreen.this.doNext();
                        }
                    });
                    vector.addElement(command);
                    vector.addElement(command2);
                    CCanvas.menu.startAt(vector, 0);
                }
            });
        }
        if (this.type == 1) {
            GameService.gI().getClanIcon(this.clan.id);
            this.left = new Command("Menu", new IAction(){

                public void perform() {
                    Vector<Command> vector = new Vector<Command>();
                    Command command = new Command(Language.topClan(), new IAction(){

                        public void perform() {
                            GameService.gI().topClan((byte)0);
                        }
                    });
                    Command command2 = new Command(Language.thanhvien(), new IAction(){

                        public void perform() {
                            GameService.gI().clanMember(ClanScreen.this.page, ClanScreen.this.clan.id);
                        }
                    });
                    Command command3 = new Command(Language.goptienXu(), new IAction(){

                        public void perform() {
                            CCanvas.inputDlg.setInfo(Language.nhapsoxu(), new IAction(){

                                public void perform() {
                                    if (CCanvas.inputDlg.tfInput.getText() != null && CCanvas.inputDlg.tfInput.getText() != "") {
                                        GameService.gI().inputMoney((byte)0, Integer.parseInt(CCanvas.inputDlg.tfInput.getText()));
                                        CCanvas.endDlg();
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
                    Command command4 = new Command(Language.goptienLuong(), new IAction(){

                        public void perform() {
                            CCanvas.inputDlg.setInfo(Language.nhapsoLuong(), new IAction(){

                                public void perform() {
                                    if (CCanvas.inputDlg.tfInput.getText() != null && CCanvas.inputDlg.tfInput.getText() != "") {
                                        GameService.gI().inputMoney((byte)1, Integer.parseInt(CCanvas.inputDlg.tfInput.getText()));
                                        CCanvas.endDlg();
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
                    vector.addElement(command);
                    vector.addElement(command2);
                    if (ClanScreen.this.clan.id == TerrainMidlet.myInfo.clanID) {
                        vector.addElement(command3);
                        vector.addElement(command4);
                    }
                    CCanvas.menu.startAt(vector, 0);
                }
            });
            this.right = new Command(Language.back(), new IAction(){

                public void perform() {
                    if (!backToTop) {
                        CCanvas.menuScr.show();
                    } else {
                        CCanvas.topClanScreen.show();
                    }
                }
            });
        }
    }

    public void show(CScreen cScreen) {
        super.show(cScreen);
        this.commandInit();
        if (this.type == 1) {
            this.endTime = new int[this.clan.time.length];
            this.currentTime = new long[this.clan.time.length];
            this.dieTime = new long[this.clan.time.length];
            this.strEndTime = new String[this.clan.time.length];
            for (int i = 0; i < this.clan.time.length; ++i) {
                this.endTime[i] = this.clan.time[i];
                this.currentTime[i] = System.currentTimeMillis();
                this.dieTime[i] = System.currentTimeMillis() / 1000L + (long)this.endTime[i];
            }
            this.cmyLim = 165 + this.clan.item.length * 30 - (CCanvas.hieght - cmdH - 25);
            if (this.cmyLim < 0) {
                this.cmyLim = 0;
            }
        } else {
            this.selected = 0;
            this.cmtoY = 0;
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

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
    }

    public void onPointerReleased(int n, int n2, int n3) {
        super.onPointerReleased(n, n2, n3);
        this.trans = false;
        if (this.type == 0 && this.clans.size() >= 1 && CCanvas.isPointer(0, ITEM_HEIGHT, w, CCanvas.hieght - cmdH, n3)) {
            int n4 = ITEM_HEIGHT;
            int n5 = (this.cmtoY + n2 - n4) / 50;
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
            if (this.selected >= this.clans.size()) {
                this.selected = this.clans.size() - 1;
            }
        }
    }

    public void onPointerHolder(int n, int n2, int n3) {
    }

    public void update() {
        super.update();
        Cloud.updateCloud();
        this.moveCamera();
    }
}

