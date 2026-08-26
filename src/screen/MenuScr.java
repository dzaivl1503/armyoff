/*
 * Decompiled with CFR 0.152.
 */
package screen;

import CLib.mGraphics;
import CLib.mSystem;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.MotherCanvas;
import com.teamobi.mobiarmy2.OfflineLeaderboard;
import com.teamobi.mobiarmy2.OfflineMission;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import effect.Camera;
import effect.Cloud;
import java.util.Vector;
import map.Background;
import model.Font;
import model.IAction;
import model.Language;
import model.PlayerInfo;
import model.Position;
import network.Command;
import network.GameService;
import network.RelayService;
import player.CPlayer;
import player.PM;
import screen.ArchivementScr;
import screen.CScreen;
import screen.ChangePlayerCSr;
import screen.ClanScreen;
import screen.CloudLoginScr;
import screen.GameScr;
import screen.ItemLoadoutScr;
import screen.LevelScreen;
import screen.LoginScr;
import screen.MissionScreen;
import screen.PrepareScr;
import screen.SquadSelectScr;
import shop.ShopEquipment;
import shop.ShopItem;

public class MenuScr
extends CScreen {
    public static final String[] BOSS_ROOM_NAMES = new String[]{"BOM 1", "BOM 2", "NH\u1ec6N M\u00c1Y", "ROBOT", "T-REX", "UFO", "KH\u00cd C\u1ea6U", "HANG NH\u1ec6N", "B\u00d3NG MA", "B\u00d3NG MA 2"};
    public int select = 0;
    int nselect = 12;
    public static byte MENU_CHOINGAY;
    public static byte MENU_CUAHANG;
    public static byte MENU_DANGNHAP;
    public static byte MENU_SUKIEN;
    public static byte MENU_NAPTIEN;
    public static byte MENU_BANGHOI;
    public static byte MENU_TINTUC;
    public static byte MENU_GIOITHIEU;
    private static final String GIOITHIEU_TEXT = "Game mod b\u1edfi CryCheese, th\u00f4ng tin chi ti\u1ebft t\u1ea1i https://emu.cheehouse.io.vn.";
    private static final String MARQUEE_TEXT = "\u0110\u00e2y l\u00e0 phi\u00ean b\u1ea3n fan-made, m\u1ecdi th\u00f4ng tin chi ti\u1ebft xin vui l\u00f2ng truy c\u1eadp https://emu.cheehouse.io.vn.";
    private static final int MARQUEE_H = 16;
    private static final int MARQUEE_SPEED = 2;
    private int marqueeOffset;
    private static int marqueeTextWidth;
    public static byte MENU_PHIENBAN;
    public static byte MENU_LAPDOI;
    public static byte MENU_CAUHINH;
    public static byte MENU_QUANGCAO;
    public static byte MENU_NGAUNHIEN;
    public static byte MENU_CHONBAN;
    public static final byte MENU_BATTLEMODE = 9;
    private static final String[] BATTLE_MODE_ITEMS;
    public static final byte MENU_CHOINHANH = 6;
    public static final byte MENU_XEPHANG = 7;
    public static final byte MENU_TUYCHONKHAC = 5;
    public static final byte MENU_1VS1 = 0;
    public static final byte MENU_2VS2 = 1;
    public static final byte MENU_3VS3 = 2;
    public static final byte MENU_4VS4 = 3;
    public static final byte MENU_DENKHUVUC = 0;
    public static final byte MENU_CHONNHANVAT = 1;
    public static final byte MENU_LEVEL = 2;
    public static final byte MENU_TRANGBI = 3;
    public static final byte MENU_LUYENTAP = 4;
    public static final byte MENU_QUAYSO = 5;
    public static final byte MENU_DAUBOSS = 4;
    public static final byte MENU_BOSS_SUB = 6;
    public static final byte MENU_BANXEPHANG = 0;
    public static final byte MENU_BANBE = 1;
    public static final byte MENU_TOPDOI = 2;
    public static final byte MENU_THANHTICH = 3;
    public static final byte MENU_ARCHIVEMENT = 4;
    public static final byte MENU_TINNHAN = 5;
    public static final byte MENU_DOIMATKHAU = 6;
    public static final byte MENU_CUAHANG_ITEM = 0;
    public static final byte MENU_CUAHANG_TRANGBI = 1;
    public static final byte MENU_CUAHANG_LINHTINH = 2;
    public static final byte MENU_CUAHANG_BIEDOI = 3;
    public static final byte MENU_CAUHINH1 = 0;
    public static final byte MENU_GAMEKHAC = 1;
    public static final byte MENU_ABOUT = 2;
    public static final byte TOPCAOTHU = 0;
    public static final byte TOP_DAIGIAXU = 1;
    public static final byte TOP_DAIGIALUONG = 2;
    public static final byte TOP_CAOTHUTUAN = 3;
    public static final byte TOP_XUTUAN = 4;
    public static String suKienStr;
    public static String linkWapStr;
    public static String linkTeam;
    int yB;
    int hB;
    int hBMax;
    public static boolean isTraining;
    public static String[][] subMenuString;
    public static int curMenuLevel;
    public static int curMenuSelect;
    private static int curSubMenuSelect;
    public static boolean viewInfo;
    public static String[] menuString;
    public static String gameContent;
    public static String gameLink;
    String[] str = new String[]{Language.playnow(), Language.toArea(), Language.selectCharactor(), Language.training(), Language.shop(), Language.topScore(), Language.FRIEND(), Language.achievement(), Language.MESS(), Language.charge(), Language.option(), Language.otherGame()};
    public static Vector menuCroll;
    public static int[] menuX;
    Command cmdExit;
    Command cmdExitGame;
    int dis;
    public int yMenu;
    int nItemShow;
    Position transText1 = new Position(0, 1);
    boolean scrollUp = false;
    boolean scrollDown = false;
    int dyUp;
    int dyDown = 20;
    boolean levelUp;
    int time;
    public static int dem2;
    public boolean hide;
    public static boolean IS_TEST_POS;
    public int cmtoY;
    public int cmy;
    public int cmdy;
    public int cmvy;
    public int cmyLim;
    public int xL;
    int hMenu;
    int W;
    int pa = 0;
    boolean trans = false;
    int speed = 1;
    private static final int MIN_Y_MENU = 100;

    public static void refreshLapDoiSubMenu() {
        if (MENU_LAPDOI < 0) {
            return;
        }
        boolean bl = TerrainMidlet.myInfo != null && TerrainMidlet.myInfo.getSquadSize() > 0;
        MenuScr.subMenuString[MenuScr.MENU_LAPDOI] = bl ? new String[]{"CH\u1eccN TH\u00c0NH VI\u00caN", "\u0110\u1ed4I NH\u00c2N V\u1eacT"} : new String[]{"CH\u1eccN TH\u00c0NH VI\u00caN"};
        if (menuX == null || menuX.length != subMenuString[MENU_LAPDOI].length) {
            menuX = new int[subMenuString[MENU_LAPDOI].length];
            for (int i = 0; i < menuX.length; ++i) {
                MenuScr.menuX[i] = CCanvas.width >> 1;
            }
        }
    }

    public static void getIdMenu(int n) {
        curMenuLevel = 0;
        curMenuSelect = 0;
        curSubMenuSelect = 0;
        if (n == 0) {
            MENU_CHOINGAY = 0;
            MENU_DANGNHAP = 1;
            MENU_TINTUC = (byte)2;
            MENU_CAUHINH = (byte)3;
            MENU_GIOITHIEU = (byte)4;
            MENU_PHIENBAN = (byte)5;
            MENU_CUAHANG = (byte)-1;
            MENU_QUANGCAO = (byte)-1;
            menuString = new String[]{Language.startGame(), "CLOUD SAVE", "BẢNG XẾP HẠNG", "CÀI ĐẶT", "GIỚI THIỆU", "PHIÊN BẢN"};
            MENU_LAPDOI = (byte)7;
            boolean bl = TerrainMidlet.myInfo != null && TerrainMidlet.myInfo.getSquadSize() > 0;
            MenuScr.subMenuString[MenuScr.MENU_LAPDOI] = bl ? new String[]{"CHỌN THÀNH VIÊN", "ĐỔI NHÂN VẬT"} : new String[]{"CHỌN THÀNH VIÊN"};
        }
        if (n == 1) {
            MENU_CHOINGAY = 0;
            MENU_CUAHANG = 1;
            MENU_QUANGCAO = (byte)2;
            MENU_TINTUC = (byte)3;
            MENU_GIOITHIEU = (byte)-1;
            MENU_DANGNHAP = (byte)-1;
            menuString = new String[]{Language.startGame(), Language.CUAHANG(), Language.information()};
        }
        MenuScr.subMenuString[MenuScr.MENU_CHOINGAY] = new String[]{"CHƠI NGAY", Language.selectCharactor(), "LẬP ĐỘI", Language.CUAHANG(), Language.NANGCAP(), Language.INVENTORY(), "CHỌN ITEM"};
        if (MENU_TINTUC >= 0) {
            MenuScr.subMenuString[MenuScr.MENU_TINTUC] = OfflineLeaderboard.TOP_MENU_LABELS;
        }
        MenuScr.subMenuString[6] = BOSS_ROOM_NAMES;
        MenuScr.subMenuString[8] = new String[]{Language.shop(), Language.shop_eq(), Language.DODACBIET(), Language.ITEM_DOI()};
        MenuScr.subMenuString[9] = BATTLE_MODE_ITEMS;
    }

    public MenuScr() {
        this.nameCScreen = " MenuScr screen!";
        w = CCanvas.width;
        h = CCanvas.hieght;
        this.dis = CCanvas.isTouch ? 32 : 20;
        this.activeCroll(curMenuLevel, curMenuSelect);
        this.createIAction();
        TerrainMidlet.myInfo.getMyEquip(10);
        if (TerrainMidlet.isVip[TerrainMidlet.myInfo.gun]) {
            TerrainMidlet.myInfo.getVipEquip();
        }
        this.menuScroll = true;
    }

    public void goToGame() {
        CCanvas.quangCaoScr.show();
    }

    private void paintMarquee(mGraphics mGraphics2) {
        if (marqueeTextWidth < 0) {
            marqueeTextWidth = Font.borderFont.getWidth(MARQUEE_TEXT);
        }
        mGraphics2.setClip(0, 0, CCanvas.width, 16);
        int n = CCanvas.width + marqueeTextWidth;
        int n2 = CCanvas.width - this.marqueeOffset % n;
        Font.borderFont.drawString(mGraphics2, MARQUEE_TEXT, n2, 1, 0, true);
        mGraphics2.setClip(0, 0, CCanvas.width, CCanvas.hieght);
    }

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

    public void getRectHeight() {
        this.yMenu = !CCanvas.isTouch ? Math.max((h >> 1) - 20, 100) : 80;
        this.yB = this.yMenu - 25;
        this.nItemShow = (CCanvas.hh - 20) / this.dis;
        if (CCanvas.isTouch) {
            this.nItemShow = (CCanvas.hieght - this.yMenu - cmdH - 10) / this.dis;
        }
        if (this.nItemShow >= menuX.length) {
            this.nItemShow = menuX.length;
        }
        if (this.nItemShow > 4) {
            this.nItemShow = 4;
        }
        int n = CCanvas.hieght - cmdH + 15 - this.yMenu;
        int n2 = !CCanvas.isTouch ? Math.min(154, n) : n;
        int n3 = (n2 - 35) / this.dis;
        if (n3 < 1) {
            n3 = 1;
        }
        if (this.nItemShow > n3) {
            this.nItemShow = n3;
        }
        this.hBMax = this.nItemShow * this.dis + 35;
        if (this.hBMax > n2) {
            this.hBMax = n2;
        }
    }

    public void resetInputState() {
        this.scrollDown = false;
        this.scrollUp = false;
        this.hide = false;
        this.trans = false;
        viewInfo = false;
        if (CCanvas.menu != null) {
            CCanvas.menu.showMenu = false;
        }
        CCanvas.endDlg();
    }

    public void finishScrollOpen() {
        this.getRectHeight();
        if (this.hBMax <= 0) {
            int n = this.hBMax = !CCanvas.isTouch ? 154 : CCanvas.hieght - cmdH + 15 - this.yMenu;
        }
        if (this.nItemShow > 6) {
            this.nItemShow = 6;
        }
        int n = this.nItemShow * this.dis;
        this.hB = this.hBMax;
        this.hMenu = n;
        this.cmy = this.cmtoY;
        this.cmdy = 0;
        this.cmvy = 0;
        this.scrollDown = false;
        this.scrollUp = false;
    }

    public void show() {
        GameScr.initMenuEffects();
        LoginScr.isLoadData = true;
        TerrainMidlet.myInfo.getMyEquip(15);
        TerrainMidlet.myInfo.getVipEquip();
        this.resetInputState();
        if (menuX == null) {
            this.activeCroll(curMenuLevel, curMenuSelect);
        }
        this.getRectHeight();
        this.finishScrollOpen();
        super.show();
        OfflineSave.save();
    }

    public void activeCroll(int n, int n2) {
        int n3;
        if (n == 0) {
            menuX = new int[menuString.length];
        } else if (n == 1) {
            menuX = new int[subMenuString[n2].length];
        } else if (n == 2) {
            menuX = new int[subMenuString[n2].length];
        }
        curMenuLevel = n;
        for (n3 = 0; n3 < menuX.length; ++n3) {
            MenuScr.menuX[n3] = CCanvas.width >> 1;
        }
        this.getRectHeight();
        if (n == 0) {
            this.select = n2;
            this.activeCMTOY(n2, menuX.length);
            this.right = this.cmdExitGame;
        } else if (n == 1) {
            curMenuSelect = n2;
            this.select = 0;
            this.activeCMTOY(0, menuX.length + 2);
            this.right = this.cmdExit;
        }
        n3 = CCanvas.isTouch ? 5 : 0;
        this.cmyLim = this.yMenu + menuX.length * this.dis - (CCanvas.hieght - cmdH - 30 - n3);
    }

    public void startScrollDown() {
        this.hide = false;
        this.scrollDown = true;
        this.hB = 50;
        int n = CCanvas.isTouch ? 5 : 0;
        this.cmyLim = this.yMenu + menuX.length * this.dis - (CCanvas.hieght - cmdH - 30 - n);
    }

    public void startScrollUp(boolean bl) {
        this.hide = false;
        this.scrollUp = true;
        this.levelUp = bl;
    }

    public void scrollUp() {
        if (this.scrollUp) {
            this.hB -= this.hBMax / 4;
            this.hMenu = this.hB - 30;
            if (this.hMenu < 40) {
                this.hMenu = 0;
            }
            if (this.hB < 38) {
                this.hB = 38;
                this.hMenu = 0;
                if (this.levelUp) {
                    this.getMenuLevel();
                } else {
                    int n = curMenuLevel - 1;
                    if (n < 0) {
                        n = 0;
                    }
                    this.activeCroll(n, curMenuSelect);
                    this.getRectHeight();
                    if (n == 0) {
                        this.finishScrollOpen();
                    }
                }
                this.scrollUp = false;
                if (!this.hide && curMenuLevel != 0) {
                    this.scrollDown = true;
                }
            }
        }
    }

    public void scrollDown() {
        if (this.scrollDown) {
            if (this.nItemShow > 6) {
                this.nItemShow = 6;
            }
            int n = this.nItemShow * this.dis;
            this.hB += this.hBMax / 4;
            if (this.hBMax <= 0) {
                this.hBMax = !CCanvas.isTouch ? 154 : CCanvas.hieght - cmdH + 15 - this.yMenu;
            }
            this.hMenu = this.hB - 30;
            if (this.hMenu > n) {
                this.hMenu = n;
            }
            if (this.hB > this.hBMax) {
                this.hB = this.hBMax;
                this.hMenu = n;
                this.scrollDown = false;
            }
        }
    }

    public void createIAction() {
        IAction iAction = new IAction(){

            public void perform() {
                MenuScr.this.doFire();
            }
        };
        this.cmdExit = new Command(Language.back(), new IAction(){

            public void perform() {
                MenuScr.this.hide = false;
                MenuScr.this.startScrollUp(false);
            }
        });
        this.cmdExitGame = new Command(Language.exit(), new IAction(){

            public void perform() {
                MenuScr.this.doExit();
            }
        });
        this.left = new Command(Language.select(), iAction);
        this.center = null;
        this.right = this.cmdExitGame;
    }

    protected void doExit() {
        IAction iAction = new IAction(){

            public void perform() {
                GameMidlet.exit();
            }
        };
        CCanvas.msgdlg.setInfo(Language.wantExit(), new Command(Language.yes(), iAction), new Command("", iAction), new Command(Language.no(), new IAction(){

            public void perform() {
                CCanvas.endDlg();
            }
        }));
        CCanvas.msgdlg.show();
    }

    public void update() {
        if (GameScr.sm != null) {
            GameScr.sm.update();
        }
        Cloud.updateCloud();
        Cloud.balloonUpdate();
        this.marqueeOffset += 2;
        super.update();
    }

    public void mainLoop() {
        super.mainLoop();
        Camera.x += 3;
        this.moveCamera();
        this.scrollDown();
        this.scrollUp();
    }

    public void doClan() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo.clanID > 0) {
            GameService.gI().clanInfo(playerInfo.clanID);
            CCanvas.startWaitDlgWithoutCancel(Language.pleaseWait(), 2000L + mSystem.currentTimeMillis(), new IAction(){

                public void perform() {
                    CCanvas.startOKDlg(Language.noTimeRespond());
                }
            });
            ClanScreen.isFromMenu = true;
        } else {
            CCanvas.startYesNoDlg(Language.chuacodoi(), new IAction(){

                public void perform() {
                    MenuScr.this.doGoToTeam(linkTeam);
                }
            });
        }
    }

    public void doLevelUp() {
        if (CCanvas.levelScreen == null) {
            CCanvas.levelScreen = new LevelScreen();
        }
        CCanvas.levelScreen.show(this);
    }

    private void doShowOfflineInfo() {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        if (CCanvas.archScreen == null) {
            CCanvas.archScreen = new ArchivementScr();
        }
        playerInfo.getQuanHam();
        CCanvas.archScreen.level = playerInfo.level2;
        CCanvas.archScreen.levelPercen = playerInfo.level2Percen;
        CCanvas.archScreen.xu = playerInfo.xu;
        CCanvas.archScreen.luong = playerInfo.luong;
        CCanvas.archScreen.exp = playerInfo.exp;
        CCanvas.archScreen.nextExp = playerInfo.nextExp;
        CCanvas.archScreen.cup = playerInfo.cup;
        CCanvas.archScreen.rank = PlayerInfo.strLevelCaption != null && playerInfo.nQuanHam2 >= 0 && playerInfo.nQuanHam2 < PlayerInfo.strLevelCaption.length ? PlayerInfo.strLevelCaption[playerInfo.nQuanHam2] : "";
        CCanvas.archScreen.imgClan = null;
        CCanvas.archScreen.show();
    }

    public void doEquip() {
        GameMidlet.ensureOfflineAssetsLoaded();
        CCanvas.equipScreen.init();
        CCanvas.equipScreen.show(this);
    }

    public void doTopClan() {
        ClanScreen.isFromMenu = false;
        CCanvas.startOKDlg(Language.pleaseWait());
        GameService.gI().topClan((byte)0);
    }

    private void doGoToTeam(String string) {
        mSystem.openUrl(string);
    }

    public void getMenuLevel() {
        if (curMenuLevel == 0) {
            if (this.select == MENU_CHOINGAY) {
                this.hide = false;
                this.activeCroll(1, this.select);
            } else if (MENU_DANGNHAP >= 0 && this.select == MENU_DANGNHAP) {
                this.hide = true;
                this.doOpenCloudLogin();
            } else if (this.select == MENU_TINTUC) {
                this.hide = false;
                this.activeCroll(1, this.select);
            } else if (MENU_CAUHINH >= 0 && this.select == MENU_CAUHINH) {
                this.hide = true;
                if (CCanvas.settingsScr == null) {
                    CCanvas.settingsScr = new SettingsScr();
                }
                CCanvas.settingsScr.show(this);
            } else if (MENU_GIOITHIEU >= 0 && this.select == MENU_GIOITHIEU) {
                this.hide = true;
                CCanvas.startOKDlg(GIOITHIEU_TEXT);
            } else if (MENU_PHIENBAN >= 0 && this.select == MENU_PHIENBAN) {
                this.hide = true;
                CCanvas.startOKDlg("Phiên bản Offline: " + GameMidlet.OFFLINE_VERSION_TEXT);
            }
        } else if (curMenuLevel == 1) {
            if (curMenuSelect == MENU_CHOINGAY) {
                this.hide = true;
                switch (this.select) {
                    case 0: {
                        this.hide = false;
                        curSubMenuSelect = 2;
                        this.select = 0;
                        this.activeCroll(2, 9);
                        break;
                    }
                    case 1: {
                        this.doChangePlayer();
                        break;
                    }
                    case 2: {
                        this.hide = false;
                        curSubMenuSelect = 3;
                        this.select = 0;
                        this.activeCroll(2, MENU_LAPDOI);
                        break;
                    }
                    case 3: {
                        this.hide = false;
                        curSubMenuSelect = 4;
                        this.select = 0;
                        this.activeCroll(2, 8);
                        break;
                    }
                    case 4: {
                        this.hide = true;
                        viewInfo = true;
                        this.doLevelUp();
                        break;
                    }
                    case 5: {
                        this.hide = true;
                        this.doEquip();
                        break;
                    }
                    case 6: {
                        this.hide = true;
                        this.doChooseItemLoadout();
                        break;
                    }
                }
            } else if (curMenuSelect == MENU_TINTUC) {
                this.hide = true;
                OfflineLeaderboard.showLeaderboard(this.select);
            } else if (curMenuSelect == MENU_CUAHANG) {
                this.hide = true;
                this.doShopMenuSelect();
            }
        } else if (curMenuLevel == 2) {
            this.hide = true;
            if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 1) {
                this.doSelectBossRoom((byte)this.select);
            } else if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 4) {
                this.doShopMenuSelect();
            } else if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 2) {
                switch (this.select) {
                    case 0: {
                        GameMidlet.openOfflineBossRoomList();
                        break;
                    }
                    case 1: {
                        GameMidlet.openPvpBotSetup();
                        break;
                    }
                    case 2: {
                        this.hide = true;
                        RelayService.enter();
                        break;
                    }
                    case 3: {
                        if (CCanvas.missionScreen == null) {
                            CCanvas.missionScreen = new MissionScreen();
                        }
                        CCanvas.missionScreen.setMission(OfflineMission.buildMissionList());
                        CCanvas.missionScreen.show();
                        break;
                    }
                    case 4: {
                        this.doShowOfflineInfo();
                        break;
                    }
                }
            } else if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 3) {
                if (this.select == 0) {
                    if (CCanvas.squadSelectScr == null) {
                        CCanvas.squadSelectScr = new SquadSelectScr();
                    }
                    CCanvas.squadSelectScr.show(this);
                } else if (this.select == 1) {
                    this.doOpenSquadSwitchPopup();
                }
            }
        }
        this.getRectHeight();
    }

    protected void doFire() {
        this.startScrollUp(true);
    }

    private void doShowLeaderBoard(byte by) {
        CCanvas.startWaitDlg(Language.pleaseWait());
        GameService.gI().requestStrongest(0);
    }

    private void doOpenSquadSwitchPopup() {
        final PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        Vector<Command> vector = new Vector<Command>();
        vector.addElement(new Command(PrepareScr.GUN_NAME[playerInfo.gun], new IAction(){

            public void perform() {
                MenuScr.this.doSwitchToSquadMember(playerInfo.gun);
            }
        }));
        for (int i = 0; i < playerInfo.squadExtra.length; ++i) {
            if (playerInfo.squadExtra[i] < 0) continue;
            final byte by = playerInfo.squadExtra[i];
            vector.addElement(new Command(PrepareScr.GUN_NAME[by], new IAction(){

                public void perform() {
                    MenuScr.this.doSwitchToSquadMember(by);
                }
            }));
        }
        CCanvas.menu.startAt(vector, 0);
    }

    private void doSwitchToSquadMember(byte by) {
        PlayerInfo playerInfo = TerrainMidlet.myInfo;
        if (playerInfo == null) {
            return;
        }
        if (by == playerInfo.gun) {
            CCanvas.startOKDlg("\u0110\u00e2y \u0111\u00e3 l\u00e0 nh\u00e2n v\u1eadt \u0111ang d\u00f9ng.");
            return;
        }
        byte by2 = playerInfo.gun;
        for (int i = 0; i < playerInfo.squadExtra.length; ++i) {
            if (playerInfo.squadExtra[i] != by) continue;
            playerInfo.squadExtra[i] = by2;
            break;
        }
        ChangePlayerCSr.changeGunOffline(by);
        CCanvas.startOKDlg("\u0110\u00e3 \u0111\u1ed5i sang " + PrepareScr.GUN_NAME[by] + ".");
    }

    private void doChangePlayer() {
        if (CCanvas.changePScr != null) {
            CCanvas.changePScr.show(this);
        } else {
            CCanvas.changePScr = new ChangePlayerCSr();
            CCanvas.changePScr.show(this);
        }
    }

    private void doOpenCloudLogin() {
        if (CCanvas.cloudLoginScr == null) {
            CCanvas.cloudLoginScr = new CloudLoginScr();
        }
        CCanvas.cloudLoginScr.show(this);
    }

    private void doChooseItemLoadout() {
        if (CCanvas.itemLoadoutScr == null) {
            CCanvas.itemLoadoutScr = new ItemLoadoutScr();
        }
        CCanvas.itemLoadoutScr.show(this);
    }

    private void doGoToWap(String string) {
        mSystem.openUrl(string);
    }

    public void doTraining(byte by, byte by2, short[] sArray, short[] sArray2, short[] sArray3, short[] sArray4) {
        int n;
        PlayerInfo playerInfo;
        CCanvas.prepareScr.playerInfos.removeAllElements();
        if (!IS_TEST_POS) {
            playerInfo = new PlayerInfo();
            playerInfo.name = "";
            playerInfo.gun = TerrainMidlet.myInfo.gun;
            playerInfo.level2 = 1;
            playerInfo.getQuanHam();
            for (n = 0; n < 5; ++n) {
                playerInfo.equipID[playerInfo.gun][n] = sArray4[n];
            }
            playerInfo.getMyEquip(11);
            CCanvas.prepareScr.playerInfos.addElement(playerInfo);
            playerInfo = new PlayerInfo();
            playerInfo.name = "Enemy";
            playerInfo.gun = TerrainMidlet.myInfo.gun;
            playerInfo.level2 = 1;
            playerInfo.getQuanHam();
            for (n = 0; n < 5; ++n) {
                playerInfo.equipID[playerInfo.gun][n] = sArray4[n];
            }
            playerInfo.getMyEquip(12);
            CCanvas.prepareScr.playerInfos.addElement(playerInfo);
        }
        if (IS_TEST_POS) {
            for (n = 0; n < 8; ++n) {
                playerInfo = new PlayerInfo();
                playerInfo.name = "p" + n;
                CCanvas.prepareScr.playerInfos.addElement(playerInfo);
            }
            CCanvas.gameScr.initGame(by, by2, sArray, sArray2, sArray3, 0);
        } else {
            CCanvas.gameScr.initGame(by, by2, sArray, sArray2, sArray3, 0);
        }
        CCanvas.gameScr.show();
        GameScr.trainingMode = true;
        GameScr.cam.setPlayerMode(0);
        GameScr.pm.setNextPlayer((byte)0);
        PM.p[0].isCom = false;
        PM.p[0].item = PrepareScr.getStrainingItem();
        GameScr.trainingStep = 0;
        PM.p[0].hp = 70;
        GameScr.myIndex = 0;
    }

    private void doShowShopItem() {
        ShopItem shopItem = CCanvas.shopItemScr;
        ShopItem.resetItemBuy();
        CCanvas.shopItemScr.show(this);
    }

    private void doShopMenuSelect() {
        switch (this.select) {
            case 0: {
                this.hide = true;
                this.doShowShopItem();
                break;
            }
            case 1: {
                this.hide = true;
                GameService.gI().getShopEquip();
                break;
            }
            case 2: {
                this.hide = true;
                GameService.gI().getShopLinhtinh((byte)0, (byte)-1, (byte)-1, (byte)-1);
                break;
            }
            case 3: {
                this.hide = true;
                GameService.gI().getShopBietDoi((byte)0, (byte)-1, (byte)-1);
            }
        }
    }

    public void doEquipItem() {
        if (CCanvas.shopEquipScr == null) {
            CCanvas.shopEquipScr = new ShopEquipment();
        }
        CCanvas.shopEquipScr.show(this);
    }

    private void doShowInfo() {
        GameService.gI().requestInfoOf(TerrainMidlet.myInfo.IDDB);
        CCanvas.startWaitDlg(Language.pleaseWait());
    }

    public void doChargeMoney() {
        if (!CCanvas.isIos()) {
            if (CCanvas.moneyScr != null) {
                CCanvas.moneyScr.show();
            }
        } else if (CCanvas.moneyScrIOS != null) {
            CCanvas.moneyScrIOS.show();
        }
    }

    public void doRequestRoomList() {
        GameService.gI().requestRoomList();
        CCanvas.startWaitDlg(Language.pleaseWait());
    }

    private void doSelectBossRoom(byte by) {
        if (by < 0 || by >= BOSS_ROOM_NAMES.length) {
            return;
        }
        PrepareScr.isBossRoom = true;
        GameMidlet.openOfflineBossRoom(by);
    }

    private void doPlayNow(byte by) {
        PrepareScr.isBossRoom = by == 5;
        GameService.gI().joinAnyBoard(by);
        CCanvas.startWaitDlg(Language.pleaseWait());
    }

    private void doShowFriend() {
        GameService.gI().requestFriendList();
        CCanvas.startWaitDlg(Language.pleaseWait());
    }

    private void doViewMessage() {
        CCanvas.msgScr.show(this);
    }

    private void doChangePass() {
        final String[] stringArray = new String[3];
        final String[] stringArray2 = new String[]{Language.oldPass(), Language.newPass(), Language.retypeNewPass()};
        CCanvas.inputDlg.setInfo(stringArray2[0], new IAction(){

            public void perform() {
                if (CCanvas.inputDlg.tfInput.getText().equals("")) {
                    CCanvas.startOKDlg(Language.plOldPass(), new IAction(){

                        public void perform() {
                            CCanvas.inputDlg.show();
                        }
                    });
                } else {
                    stringArray[0] = CCanvas.inputDlg.tfInput.getText();
                    CCanvas.inputDlg.setInfo(stringArray2[1], new IAction(){

                        public void perform() {
                            if (CCanvas.inputDlg.tfInput.getText().equals("")) {
                                CCanvas.startOKDlg(Language.plNewPass(), new IAction(){

                                    public void perform() {
                                        CCanvas.inputDlg.show();
                                    }
                                });
                            } else {
                                stringArray[1] = CCanvas.inputDlg.tfInput.getText();
                                CCanvas.inputDlg.setInfo(stringArray2[2], new IAction(){

                                    public void perform() {
                                        if (CCanvas.inputDlg.tfInput.getText().equals("")) {
                                            CCanvas.startOKDlg(Language.plRetypeNewPass(), new IAction(){

                                                public void perform() {
                                                    CCanvas.inputDlg.show();
                                                }
                                            });
                                        } else {
                                            stringArray[2] = CCanvas.inputDlg.tfInput.getText();
                                            if (stringArray[2].equals(stringArray[1])) {
                                                GameService.gI().requestChangePass(stringArray[0], stringArray[1]);
                                                CCanvas.endDlg();
                                                CCanvas.startOKDlg(Language.pleaseWait());
                                            } else {
                                                CCanvas.startOKDlg(Language.newPassNotMath());
                                            }
                                        }
                                    }
                                }, new IAction(){

                                    public void perform() {
                                        MenuScr.this.startScrollDown();
                                        CCanvas.endDlg();
                                    }
                                }, 2);
                            }
                        }
                    }, new IAction(){

                        public void perform() {
                            MenuScr.this.startScrollDown();
                            CCanvas.endDlg();
                        }
                    }, 2);
                    CCanvas.inputDlg.show();
                }
            }
        }, new IAction(){

            public void perform() {
                MenuScr.this.startScrollDown();
                CCanvas.endDlg();
            }
        }, 2);
        CCanvas.inputDlg.show();
    }

    public void moveCamera() {
        if (this.cmy != this.cmtoY) {
            this.cmvy = this.cmtoY - this.cmy << 2;
            this.cmdy += this.cmvy;
            this.cmy += this.cmdy >> 4;
            this.cmdy &= 0xF;
        }
    }

    public void paint(mGraphics mGraphics2) {
        MenuScr.paintDefaultBg(mGraphics2);
        Background.paintTree(mGraphics2);
        Cloud.paintBalloonWithCloud(mGraphics2);
        super.paint(mGraphics2);
        if (!this.hide) {
            int n;
            int n2;
            int n3;
            int n4 = 3;
            MenuScr.paintBorderRect(mGraphics2, this.yB, n4, this.hB, null);
            if (curMenuLevel == 1 || curMenuLevel == 2) {
                n3 = CCanvas.width / 2 - this.W / 2;
                n2 = this.yB;
                if (curMenuSelect == MENU_CHOINGAY) {
                    this.paintInformation(mGraphics2, n3, n2);
                }
                if (!CCanvas.isTouch) {
                    Font.bigFont.drawString(mGraphics2, menuString[curMenuSelect], w >> 1, 5, 2);
                }
            }
            n2 = CCanvas.width / 2 - this.W / 2;
            this.W = n4 * 32 + 23 + 33;
            mGraphics2.setClip(n2 + 5, this.yMenu, this.W - 10, this.nItemShow * this.dis);
            int n5 = n3 = CCanvas.isTouch ? 5 : 0;
            if (!this.scrollUp) {
                mGraphics2.setColor(3374591);
                mGraphics2.fillRect(n2, this.yMenu - this.cmy + this.select * this.dis + n3, this.W, 22, true);
                n = this.yMenu - this.cmy + this.select * this.dis + n3;
                mGraphics2.drawImage(GameScr.arrowMenu, n2, n, 0, true);
                mGraphics2.drawRegion(GameScr.arrowMenu, 0, 0, 14, 21, 3, n2 + this.W, n, 24, true);
            }
            for (n = 0; n < menuX.length; ++n) {
                if (curMenuLevel == 0) {
                    String string = menuString[n];
                    int n6 = Font.normalFont.getWidth(string);
                    if (n6 > 85) {
                        this.transTextLimit(this.transText1, n6 - 85);
                    }
                    int n7 = this.transText1.x;
                    Font.bigFont.drawString(mGraphics2, menuString[n], n6 > 85 ? n2 + 8 + n7 : menuX[n], this.yMenu - this.cmy + n * this.dis + n3, n6 > 85 ? 0 : 2);
                    continue;
                }
                if (curMenuLevel == 1) {
                    Font.bigFont.drawString(mGraphics2, subMenuString[curMenuSelect][n], menuX[n], this.yMenu - this.cmy + n * this.dis + n3, 2);
                    continue;
                }
                if (curMenuLevel == 2) {
                    if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 1) {
                        Font.bigFont.drawString(mGraphics2, subMenuString[6][n], menuX[n], this.yMenu - this.cmy + n * this.dis + n3, 2);
                    }
                    if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 2) {
                        Font.bigFont.drawString(mGraphics2, subMenuString[9][n], menuX[n], this.yMenu - this.cmy + n * this.dis + n3, 2);
                    }
                    if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 3) {
                        Font.bigFont.drawString(mGraphics2, subMenuString[MENU_LAPDOI][n], menuX[n], this.yMenu - this.cmy + n * this.dis + n3, 2);
                    }
                    if (curMenuSelect == MENU_CHOINGAY && curSubMenuSelect == 4) {
                        Font.bigFont.drawString(mGraphics2, subMenuString[MENU_CUAHANG][n], menuX[n], this.yMenu - this.cmy + n * this.dis + n3, 2);
                    }
                    if (curMenuSelect != MENU_TINTUC) continue;
                    Font.bigFont.drawString(mGraphics2, subMenuString[7][n], menuX[n], this.yMenu - this.cmy + n * this.dis + n3, 2);
                    continue;
                }
                if (curMenuLevel != 3 || curMenuSelect != 0) continue;
                Font.bigFont.drawString(mGraphics2, subMenuString[MENU_NGAUNHIEN][n], menuX[n], this.yMenu - this.cmy + n * this.dis + n3, 2);
            }
            if (curMenuLevel == 0) {
                this.paintMarquee(mGraphics2);
                GameMidlet.serverInformation(Font.borderFont, mGraphics2, this.yB - Font.borderFont.getHeight() - 2);
            }
        }
    }

    private void paintInformation(mGraphics mGraphics2, int n, int n2) {
        n2 -= 5;
        boolean n3 = OfflineTeamItems.isActive(4);
        boolean bl = OfflineTeamItems.isExpCardActive();
        if (n3 || bl) {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append('x').append(OfflineTeamItems.expMultiplier()).append(" KN:");
            if (n3) {
                stringBuffer.append(" \u0111\u1ed9i ").append(OfflineTeamItems.remainingHours(4)).append('h');
            }
            if (bl) {
                stringBuffer.append(" th\u1ebb ").append(OfflineTeamItems.expCardRemainingHours()).append('h');
            }
            Font.borderFont.drawString(mGraphics2, stringBuffer.toString(), CCanvas.width - 4, n2 - Font.borderFont.getHeight() * 3, 1, false);
        }
        Font.borderFont.drawString(mGraphics2, Language.name() + ": " + TerrainMidlet.myInfo.name, n + 20, n2 - Font.borderFont.getHeight() * 3, 0, false);
        Font.borderFont.drawString(mGraphics2, "Level: " + TerrainMidlet.myInfo.level2 + "   " + Language.cup() + ": " + TerrainMidlet.myInfo.cup, n + 20, n2 - Font.borderFont.getHeight() * 2, 0, false);
        Font.borderFont.drawString(mGraphics2, TerrainMidlet.myInfo.xu + Language.xu() + " - " + TerrainMidlet.myInfo.luong + Language.luong(), n + 20, n2 - Font.borderFont.getHeight(), 0, false);
        int nQuanHam = TerrainMidlet.myInfo.nQuanHam2;
        if (PrepareScr.imgQuanHam != null) {
            mGraphics2.drawRegion(PrepareScr.imgQuanHam, 0, 12 * nQuanHam, 12, 12, 0, n + 6, n2 - Font.borderFont.getHeight() * 2 - 5, mGraphics.TOP | mGraphics.HCENTER, false);
        }
        PlayerEquip playerEquip = null;
        playerEquip = TerrainMidlet.isVip[TerrainMidlet.myInfo.gun] ? TerrainMidlet.myInfo.myVipEquip : TerrainMidlet.myInfo.myEquip;
        if (playerEquip != null && playerEquip.equips[0] == null && TerrainMidlet.myInfo.gun != 10) {
            playerEquip = null;
        }
        CPlayer.paintSimplePlayer(TerrainMidlet.myInfo.gun, CCanvas.gameTick % 10 > 2 ? 5 : 4, n + 6, n2, 0, playerEquip, mGraphics2);
    }

    public void paintRoundR(int n, int n2, int n3, int n4, mGraphics mGraphics2) {
        mGraphics2.setColor(0x7AAFFF);
        mGraphics2.fillRoundRect(n, n2, n3, n4, 10, 10, false);
        mGraphics2.setColor(0xFFFFFF);
        mGraphics2.drawRoundRect(n, n2, n3, n4, 10, 10, false);
    }

    public void onPointerPressed(int n, int n2, int n3) {
        super.onPointerPressed(n, n2, n3);
        if (CCanvas.keyPressed[2] || CCanvas.keyPressed[8]) {
            if (CCanvas.keyPressed[2]) {
                --this.select;
                if (this.select < 0) {
                    this.select = menuX.length - 1;
                }
            }
            if (CCanvas.keyPressed[8]) {
                ++this.select;
                if (this.select > menuX.length - 1) {
                    this.select = 0;
                }
            }
            this.activeCMTOY(this.select, menuX.length);
            CScreen.clearKey();
        }
    }

    public void onPointerReleased(int n, int n2, int n3) {
        if (!this.scrollDown && !this.scrollUp) {
            super.onPointerReleased(n, n2, n3);
            this.trans = false;
            if (CCanvas.isPointer(0, this.yMenu, CCanvas.width, this.nItemShow * this.dis + 33, n3)) {
                int n4 = (n2 - this.yMenu + this.cmy) / this.dis;
                if (n4 < 0) {
                    n4 = 0;
                }
                if (n4 > menuX.length - 1) {
                    n4 = menuX.length - 1;
                }
                if (!MotherCanvas.touchDrag) {
                    if (this.select != n4) {
                        this.select = n4;
                    } else {
                        this.doFire();
                    }
                }
            }
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        if (!this.scrollDown && !this.scrollUp) {
            super.onPointerDragged(n, n2, n3);
            if (!this.trans) {
                this.pa = this.cmy;
                this.trans = true;
            }
            if (!CCanvas.isPc()) {
                this.speed = 3;
            }
            if (CCanvas.isTouch) {
                this.cmtoY = this.pa + (CCanvas.pyFirst[n3] - n2) * this.speed;
            }
            this.cmtoY = MenuScr.Clamp(this.cmtoY, 0, (menuX.length - this.nItemShow) * this.dis);
        }
    }

    public void activeCMTOY(int n, int n2) {
        int n3;
        this.cmtoY = n * this.dis - this.hMenu / 2;
        if (CCanvas.isTouch && n != 0) {
            this.cmtoY += 10;
        }
        if (this.cmtoY > n2 * this.dis - (n3 = this.nItemShow * this.dis)) {
            this.cmtoY = n2 * this.dis - n3;
        }
        if (this.cmtoY < 0) {
            this.cmtoY = 0;
        }
    }

    static {
        MENU_DANGNHAP = (byte)-1;
        MENU_GIOITHIEU = (byte)-1;
        marqueeTextWidth = -1;
        MENU_PHIENBAN = (byte)-1;
        MENU_LAPDOI = (byte)-1;
        MENU_NGAUNHIEN = (byte)8;
        MENU_CHONBAN = (byte)9;
        BATTLE_MODE_ITEMS = new String[]{"\u0110\u1ea4U BOSS", "PVP BOT", "MULTIPLAYER", "NHI\u1ec6M V\u1ee4", "XEM TH\u00d4NG TIN"};
        isTraining = false;
        subMenuString = new String[20][];
        MenuScr.subMenuString[7] = new String[]{Language.topCaothu(), Language.topDaiGiaXu(), Language.topDaigiaLuong(), Language.topCaothuTuan(), Language.topXuTuan()};
        MenuScr.subMenuString[5] = new String[]{Language.option(), Language.otherGame(), "ABOUT"};
        MenuScr.subMenuString[9] = BATTLE_MODE_ITEMS;
        menuCroll = new Vector();
        dem2 = 0;
        IS_TEST_POS = false;
    }
}

