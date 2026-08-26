/*
 * Decompiled with CFR 0.152.
 */
package coreLG;

import CLib.Image;
import CLib.LibSysTem;
import CLib.RMS;
import CLib.mGraphics;
import CLib.mImage;
import CLib.mSound;
import CLib.mSystem;
import Equipment.Equip;
import Equipment.EquipGlass;
import Equipment.PlayerEquip;
import Equipment.TypeEquip;
import InterfaceComponents.GamePad;
import com.teamobi.mobiarmy2.GameMidlet;
import com.teamobi.mobiarmy2.IActionListener;
import com.teamobi.mobiarmy2.MotherCanvas;
import coreLG.TerrainMidlet;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import java.util.Vector;
import map.CMap;
import map.MM;
import map.MapFile;
import model.CRes;
import model.Dialog;
import model.IAction;
import model.IAction2;
import model.IconManager;
import model.InfoPopup;
import model.InputDlg;
import model.Language;
import model.Menu;
import model.MsgDlg;
import model.MsgPopup;
import model.PauseMenu;
import model.PlayerInfo;
import model.Popup;
import model.Position;
import network.Command;
import network.GameService;
import network.Message;
import network.RelayService;
import player.CPlayer;
import player.PM;
import screen.ArchivementScr;
import screen.BoardListScr;
import screen.CScreen;
import screen.ChangePlayerCSr;
import screen.ClanScreen;
import screen.CloudLoginScr;
import screen.ConfigScr;
import screen.EquipScreen;
import screen.FomulaScreen;
import screen.GameScr;
import screen.Inventory;
import screen.ItemLoadoutScr;
import screen.LevelScreen;
import screen.ListScr;
import screen.LoginScr;
import screen.LuckyGame;
import screen.LuckyGifrScreen;
import screen.MainMenuScr;
import screen.MenuScr;
import screen.MissionScreen;
import screen.MoneyScr;
import screen.MoneyScrIOS;
import screen.MsgScreen;
import screen.PrepareScr;
import screen.PvpBotSetupScr;
import screen.QuangCao;
import screen.RelayLobbyScr;
import screen.RoomListScr;
import screen.RoomListScr2;
import screen.ServerListScreen;
import screen.SettingsScr;
import screen.SplashScr;
import screen.SquadSelectScr;
import shop.ShopBietDoi;
import shop.ShopEquipment;
import shop.ShopItem;
import shop.ShopLinhTinh;

public class CCanvas
extends MotherCanvas
implements IActionListener {
    private static boolean currentScreen;
    boolean isRunning = true;
    public static int gameTick;
    public static int width;
    public static int hieght;
    public static int hh;
    public static int hw;
    public static CScreen curScr;
    public static SplashScr splashScr;
    public static MainMenuScr mainMenuScr;
    public static MenuScr menuScr;
    public static GameScr gameScr;
    public static LoginScr loginScr;
    public static CloudLoginScr cloudLoginScr;
    public static RoomListScr roomListScr;
    public static RoomListScr2 roomListScr2;
    public static BoardListScr boardListScr;
    public static ListScr listScr;
    public static PrepareScr prepareScr;
    public static MsgScreen msgScr;
    public static ShopItem shopItemScr;
    public static ShopEquipment shopEquipScr;
    public static ShopLinhTinh shopLinhtinh;
    public static ChangePlayerCSr changePScr;
    public static SquadSelectScr squadSelectScr;
    public static ItemLoadoutScr itemLoadoutScr;
    public static PvpBotSetupScr pvpBotSetupScr;
    public static SettingsScr settingsScr;
    public static LuckyGame luckyGame;
    public static ServerListScreen serverListScreen;
    public static MissionScreen missionScreen;
    public static RelayLobbyScr relayLobbyScr;
    public static ShopBietDoi shopBietDoi;
    public static boolean isVirHorizontal;
    public static InputDlg inputDlg;
    public static Menu menu;
    public static PauseMenu pausemenu;
    public static boolean isMoto;
    public static boolean isWifi;
    public static boolean isBB;
    public static Command cmdMenu;
    public static MsgDlg msgdlg;
    public static Dialog currentDialog;
    public static Vector arrPopups;
    public static MsgPopup msgPopup;
    public static InfoPopup infoPopup;
    public static int waitSendMessage;
    public static MoneyScr moneyScr;
    public static MoneyScrIOS moneyScrIOS;
    public static ConfigScr configScr;
    public static LevelScreen levelScreen;
    public static EquipScreen equipScreen;
    public static Inventory inventory;
    public static ClanScreen clanScreen;
    public static ClanScreen topClanScreen;
    public static LuckyGifrScreen luckyGifrScreen;
    public static FomulaScreen fomulaScreen;
    public static ArchivementScr archScreen;
    public static QuangCao quangCaoScr;
    public static byte[] fileData5;
    public static int[] pX;
    public static int[] pY;
    public static int[] pxLast;
    public static int[] pyLast;
    public static int[] pxFirst;
    public static int[] pyFirst;
    public static boolean[] keyPressed;
    public static boolean[] keyReleased;
    public static boolean[] keyHold;
    public static boolean[] isPointerDown;
    public static boolean[] isPointerRelease;
    public static boolean[] isPointerSelect;
    public static boolean[] isPointerMove;
    public static boolean[] isPointerClick;
    public static int pointer;
    public static int button;
    public static boolean isTouch;
    public static IconManager iconMn;
    public static int nBigImage;
    public static boolean isPurchaseIOS;
    public static GamePad gamePad;
    public static boolean isVirtualKey;
    public static boolean G;
    public static int hCan;
    int t;
    public static boolean isDoubleClick;
    private static int MAX_TIME_CLICK;
    private static long timeClick;
    public static boolean isInGameRunTime;
    public static boolean lockNotify;
    public static int countNotify;
    public static int tNotify;
    public static boolean isReconnect;
    public static byte tileMapVersion;
    public static byte mapIconVersion;
    public static byte mapValuesVersion;
    public static byte playerVersion;
    public static byte equipVersion;
    public static byte levelCVersion;
    boolean isTestMap = false;
    private static int indexBullet;
    public static Image imgTest;
    public static long timeHideStartWaitingDlg;
    public static Random r;
    public static long timeNow;
    public static boolean isSmallScreen;
    private Vector listPoint = new Vector();
    private int curPos;
    long timeHold = 0L;

    public CCanvas() {
        this.setFullScreenMode(true);
        isTouch = !CCanvas.isJ2ME();
        this.screenInit(true);
        CScreen.cmdH = 35;
        isInGameRunTime = false;
        mSound.init();
    }

    public void screenInit(boolean bl) {
        this.setFullScreenMode(true);
        width = w;
        hieght = h;
        hh = MotherCanvas.hh;
        hw = MotherCanvas.hw;
        isSmallScreen = width <= 400;
        splashScr = new SplashScr();
        isTouch = !CCanvas.isJ2ME();
        splashScr.show();
        CScreen.w = width;
        CScreen.h = hieght;
        GamePad.init();
        CCanvas.loadScreen();
    }

    public static void loadScreen() {
        luckyGame = new LuckyGame();
        shopEquipScr = new ShopEquipment();
        shopItemScr = new ShopItem();
        shopLinhtinh = new ShopLinhTinh();
        listScr = new ListScr();
        msgScr = new MsgScreen();
        infoPopup = new InfoPopup();
        prepareScr = new PrepareScr();
        inputDlg = new InputDlg();
        msgPopup = new MsgPopup();
        clanScreen = new ClanScreen(1);
        topClanScreen = new ClanScreen(0);
        serverListScreen = new ServerListScreen();
        luckyGifrScreen = new LuckyGifrScreen();
        fomulaScreen = new FomulaScreen();
        archScreen = new ArchivementScr();
        moneyScr = new MoneyScr();
        moneyScrIOS = new MoneyScrIOS();
        inventory = new Inventory();
        equipScreen = new EquipScreen();
        pausemenu = new PauseMenu();
        menu = new Menu();
        GameScr.mm = new MM();
        CMap.onInitCmap();
        iconMn = new IconManager();
    }

    public int getGameAction(int n) {
        return super.getGameAction(n);
    }

    public void mainLoop() {
        if (menu != null && CCanvas.menu.showMenu) {
            menu.update();
        }
        if (curScr != null) {
            curScr.mainLoop();
        }
    }

    public void update() {
        if (width != w || hieght != h) {
            width = w;
            hieght = h;
            hw = MotherCanvas.hw;
            hh = MotherCanvas.hh;
            CScreen.w = width;
            CScreen.h = hieght;
            GamePad.init();
        }
        if (++gameTick > 10000) {
            gameTick = 0;
        }
        RelayService.tick();
        if (waitSendMessage > 0) {
            --waitSendMessage;
        }
        if (currentDialog != null) {
            currentDialog.update();
        }
        for (int i = 0; i < arrPopups.size(); ++i) {
            ((Popup)arrPopups.elementAt(i)).update();
        }
        if (gamePad != null) {
            gamePad.updateKey();
        }
        if (curScr != null) {
            curScr.update();
        }
        if (GameScr.pm != null && PM.p != null && GameScr.myIndex >= 0 && GameScr.myIndex < PM.p.length && PM.p[GameScr.myIndex] != null && (GameScr.pm.isYourTurn() || GameScr.trainingMode)) {
            PM.p[GameScr.myIndex].updateHoldKey();
        }
        if (gamePad != null) {
            gamePad.finishPointerFrame();
        }
    }

    public void paint(mGraphics mGraphics2) {
        int n;
        if (curScr != null) {
            curScr.paint(mGraphics2);
        }
        if (currentDialog != null) {
            currentDialog.paint(mGraphics2);
        }
        if (menu != null && CCanvas.menu.showMenu) {
            menu.paintMenu(mGraphics2);
        } else if (pausemenu != null && CCanvas.pausemenu.isShow) {
            pausemenu.paint(mGraphics2);
        }
        for (n = 0; n < arrPopups.size(); ++n) {
            if (arrPopups.elementAt(n) instanceof MsgPopup) continue;
            ((Popup)arrPopups.elementAt(n)).paint(mGraphics2);
        }
        for (n = 0; n < arrPopups.size(); ++n) {
            Popup popup = (Popup)arrPopups.elementAt(n);
            if (!(popup instanceof MsgPopup)) continue;
            ((Popup)arrPopups.elementAt(n)).paint(mGraphics2);
            break;
        }
        if (gamePad != null) {
            gamePad.paint(mGraphics2);
        }
    }

    public void keyPressed(int n) {
        this.mapKeyPress(n);
    }

    public void mapKeyPress(int n) {
        if (!this.mapGameActionKey(n, true)) {
            switch (n) {
                case -39:
                case -2: {
                    CCanvas.keyHold[8] = true;
                    CCanvas.keyPressed[8] = true;
                    break;
                }
                case -38:
                case -1: {
                    CCanvas.keyHold[2] = true;
                    CCanvas.keyPressed[2] = true;
                    break;
                }
                case -22:
                case -7:
                case 113: {
                    CCanvas.keyHold[13] = true;
                    CCanvas.keyPressed[13] = true;
                    break;
                }
                case -21:
                case -6:
                case 112: {
                    CCanvas.keyHold[12] = true;
                    CCanvas.keyPressed[12] = true;
                    break;
                }
                case -5:
                case 10:
                case 13: {
                    CCanvas.keyHold[5] = true;
                    CCanvas.keyPressed[5] = true;
                    break;
                }
                case -4: {
                    CCanvas.keyHold[6] = true;
                    CCanvas.keyPressed[6] = true;
                    break;
                }
                case -3: {
                    CCanvas.keyHold[4] = true;
                    CCanvas.keyPressed[4] = true;
                    break;
                }
                case 35: {
                    CCanvas.keyHold[11] = true;
                    CCanvas.keyPressed[11] = true;
                    break;
                }
                case 42: {
                    CCanvas.keyHold[10] = true;
                    CCanvas.keyPressed[10] = true;
                }
            }
        }
        if (pausemenu != null && CCanvas.pausemenu.isShow) {
            pausemenu.onPointerPressed(0, 0, pointer);
            return;
        }
        if (menu != null && CCanvas.menu.showMenu) {
            menu.onPointerPressed(0, 0, pointer);
            return;
        }
        if (currentDialog != null) {
            Dialog dialog = currentDialog;
            dialog.keyPress(n);
            if (currentDialog != null && !(currentDialog instanceof InputDlg)) {
                currentDialog.handlePadKey(pointer);
            }
            return;
        }
        if (n == 0) {
            CCanvas.handleChatKey();
            return;
        }
        if (curScr instanceof GameScr && ((GameScr)curScr).isShowingResult()) {
            curScr.onPointerPressed(0, 0, pointer);
            return;
        }
        if (curScr instanceof GameScr && ((GameScr)curScr).isSelectingItem()) {
            curScr.onPointerPressed(0, 0, pointer);
            return;
        }
        if (curScr != null) {
            curScr.keyPressed(n);
            if (!(curScr instanceof GameScr)) {
                curScr.onPointerPressed(0, 0, pointer);
                return;
            }
        }
    }

    public void keyReleased(int n) {
        this.mapKeyRelease(n);
    }

    public void mapKeyRelease(int n) {
        if (this.mapGameActionKey(n, false)) {
            return;
        }
        switch (n) {
            case -39:
            case -2: {
                CCanvas.keyHold[8] = false;
                CCanvas.keyPressed[8] = false;
                return;
            }
            case -38:
            case -1: {
                CCanvas.keyHold[2] = false;
                CCanvas.keyPressed[2] = false;
                return;
            }
            case -22:
            case -7:
            case 113: {
                CCanvas.keyHold[13] = false;
                CCanvas.keyReleased[13] = true;
                CCanvas.keyPressed[13] = false;
                return;
            }
            case -21:
            case -6:
            case 112: {
                CCanvas.keyHold[12] = false;
                CCanvas.keyReleased[12] = true;
                CCanvas.keyPressed[12] = false;
                return;
            }
            case -5:
            case 10:
            case 13: {
                CCanvas.keyHold[5] = false;
                CCanvas.keyReleased[5] = true;
                CCanvas.keyPressed[5] = false;
                return;
            }
            case -4: {
                CCanvas.keyHold[6] = false;
                CCanvas.keyPressed[6] = false;
                return;
            }
            case -3: {
                CCanvas.keyHold[4] = false;
                CCanvas.keyPressed[4] = false;
                return;
            }
            case 35: {
                CCanvas.keyHold[11] = false;
                CCanvas.keyReleased[11] = true;
                return;
            }
            case 42: {
                CCanvas.keyHold[10] = false;
                CCanvas.keyReleased[10] = true;
                return;
            }
        }
        if (curScr != null) {
            curScr.keyReleased(n);
        }
    }

    private boolean mapGameActionKey(int n, boolean bl) {
        int n2 = 0;
        try {
            n2 = super.getGameAction(n);
        }
        catch (IllegalArgumentException illegalArgumentException) {
        }
        if (n2 == 1 || n == 1) {
            CCanvas.setMappedKey(2, bl);
            return true;
        }
        if (n2 == 6 || n == 6) {
            CCanvas.setMappedKey(8, bl);
            return true;
        }
        if (n2 == 2 || n == 2) {
            CCanvas.setMappedKey(4, bl);
            return true;
        }
        if (n2 == 5 || n == 5) {
            CCanvas.setMappedKey(6, bl);
            return true;
        }
        if (n2 == 8 || n == 8) {
            CCanvas.setMappedKey(5, bl);
            if (!bl) {
                CCanvas.keyReleased[5] = true;
            }
            return true;
        }
        return false;
    }

    private static void setMappedKey(int n, boolean bl) {
        CCanvas.keyHold[n] = bl;
        CCanvas.keyPressed[n] = bl;
    }

    public static void ensurePlayerForDataLoad() {
        if (TerrainMidlet.myInfo == null) {
            TerrainMidlet.myInfo = new PlayerInfo();
        }
        TerrainMidlet.myInfo.getAttribute();
    }

    public static void loadCachedGameData() {
        byte[] byArray;
        CCanvas.ensurePlayerForDataLoad();
        if (CCanvas.loadData("valuesdata2") != null) {
            CCanvas.readMess(CCanvas.loadData("valuesdata2"), (byte)0);
        }
        if (CCanvas.loadData("icondata2") != null && (PrepareScr.fileData = CCanvas.loadData("icondata2")) != null) {
            PrepareScr.init();
        }
        if (CCanvas.loadData("tiledata2") != null) {
            MM.fullData = CCanvas.loadData("tiledata2");
        }
        if ((CPlayer.fileData = CCanvas.loadData("playerdata2")) != null) {
            CPlayer.init();
        }
        if (CCanvas.loadData("equipdata2") != null && (byArray = CCanvas.loadData("equipdata2")) != null) {
            CCanvas.readMess(byArray, (byte)1);
        }
        if (CCanvas.loadData("levelCData2") != null && (byArray = CCanvas.loadData("levelCData2")) != null) {
            CCanvas.readMess(byArray, (byte)2);
        }
        if (MM.undestroyTile == null) {
            MM.undestroyTile = new short[0];
        }
        CCanvas.loadOfflineBigImages();
        if (TerrainMidlet.myInfo != null) {
            PlayerEquip.applyDefaultOfflineEquipIds(TerrainMidlet.myInfo);
        }
    }

    public static byte[] readResAssetBytes(String string) {
        try {
            int n;
            InputStream inputStream = LibSysTem.openResource("/" + LibSysTem.res + string);
            if (inputStream == null) {
                return null;
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] byArray = new byte[1024];
            while ((n = inputStream.read(byArray)) != -1) {
                byteArrayOutputStream.write(byArray, 0, n);
            }
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception exception) {
            return null;
        }
    }

    private static mImage loadOfflineBigImageSheet(int n) {
        byte[] byArray = CCanvas.loadData("bigImage" + n);
        if (byArray == null || byArray.length == 0) {
            byArray = CCanvas.readResAssetBytes("/bigImage" + n + ".png");
        }
        if (byArray == null || byArray.length == 0) {
            return null;
        }
        return mImage.createImage(byArray, 0, byArray.length);
    }

    private static void loadOfflineBigImages() {
        int n = 10;
        int n2 = n + 1;
        if (PlayerEquip.imgData == null || PlayerEquip.imgData.length < n2) {
            PlayerEquip.imgData = new mImage[n2];
        }
        for (int i = 0; i < n; ++i) {
            PlayerEquip.imgData[i] = CCanvas.loadOfflineBigImageSheet(i);
        }
        PlayerEquip.imgData[10] = CCanvas.loadOfflineBigImageSheet(10);
        nBigImage = n;
        CCanvas.loadOfflineBulletSheets();
    }

    private static void loadOfflineBulletSheets() {
        int n = 0;
        while (n < PlayerEquip.bullets.length) {
            if (PlayerEquip.bullets[n] != null) {
                ++n;
                continue;
            }
            byte[] byArray = CCanvas.loadData("bullet" + n);
            if (byArray == null || byArray.length == 0) {
                byArray = CCanvas.readResAssetBytes("/bullet" + n + ".png");
            }
            if (byArray != null && byArray.length > 0) {
                PlayerEquip.bullets[n] = mImage.createImage(byArray, 0, byArray.length);
            }
            ++n;
        }
    }

    public static void sendMapData() {
        byte[] byArray;
        CRes.out("=============================> SEND MAP DATA");
        mapIconVersion = (byte)CCanvas.loadVersion("iconversion2");
        if (mapIconVersion == -1) {
            mapIconVersion = 0;
        }
        if ((mapValuesVersion = (byte)CCanvas.loadVersion("valuesversion2")) == -1) {
            mapValuesVersion = 0;
        }
        if ((playerVersion = (byte)CCanvas.loadVersion("playerVersion2")) == -1) {
            playerVersion = 0;
        }
        if ((equipVersion = (byte)CCanvas.loadVersion("equipVersion2")) == -1) {
            equipVersion = 0;
        }
        if ((levelCVersion = (byte)CCanvas.loadVersion("levelCVersion2")) == -1) {
            levelCVersion = 0;
        }
        GameService.gI().sendVersion((byte)2, mapValuesVersion);
        if (CCanvas.loadData("valuesdata2") != null) {
            CCanvas.readMess(CCanvas.loadData("valuesdata2"), (byte)0);
        } else {
            LoginScr.isWait = true;
        }
        if (CCanvas.loadData("icondata2") != null && (PrepareScr.fileData = CCanvas.loadData("icondata2")) != null) {
            PrepareScr.init();
        }
        if (CCanvas.loadData("tiledata2") != null) {
            MM.fullData = CCanvas.loadData("tiledata2");
        }
        if ((CPlayer.fileData = CCanvas.loadData("playerdata2")) != null) {
            CPlayer.init();
        }
        CRes.out("=======> loadData(\"equipdata2\") != null " + (CCanvas.loadData("equipdata2") != null));
        if (CCanvas.loadData("equipdata2") != null && (byArray = CCanvas.loadData("equipdata2")) != null) {
            CCanvas.readMess(byArray, (byte)1);
        }
        if (CCanvas.loadData("levelCData2") != null && (byArray = CCanvas.loadData("levelCData2")) != null) {
            CCanvas.readMess(byArray, (byte)2);
        }
    }

    public static void readMess(byte[] byArray, byte by) {
        Message message = new Message(by, byArray);
        switch (by) {
            case 0: {
                try {
                    int n;
                    MM.NUM_MAP = (byte)(n = message.reader().readByte());
                    MM.mapName = new String[n];
                    MM.mapFileName = new String[n];
                    for (int i = 0; i < n; ++i) {
                        byte by2 = message.reader().readByte();
                        short s = message.reader().readShort();
                        byte[] byArray2 = new byte[s];
                        message.reader().read(byArray2, 0, s);
                        short[] sArray = new short[5];
                        for (int j = 0; j < sArray.length; ++j) {
                            sArray[j] = message.reader().readShort();
                        }
                        MM.mapName[i] = message.reader().readUTF();
                        MM.mapFileName[i] = message.reader().readUTF();
                        MapFile mapFile = new MapFile(byArray2, by2, sArray);
                        MM.mapFiles.addElement(mapFile);
                        Object var10_24 = null;
                    }
                    CRes.out("=============================> MM.mapFileName  " + n);
                }
                catch (Exception exception) {
                    exception.getMessage();
                }
                break;
            }
            case 1: {
                CRes.out("=============================> read Trang bi  type = 1 ");
                PlayerEquip.playerData = new Vector();
                try {
                    int n;
                    byte[] byArray3;
                    Vector<EquipGlass> vector = new Vector<EquipGlass>();
                    int n2 = message.reader().readByte();
                    nBigImage = n2;
                    byte[] byArray4 = new byte[n2];
                    short s = 0;
                    int n3 = 6;
                    int n4 = 0;
                    for (int i = 0; i < n2; ++i) {
                        byArray4[i] = message.reader().readByte();
                        s = message.reader().readShort();
                        EquipGlass equipGlass = new EquipGlass(byArray4[i]);
                        equipGlass.maxDamage = s;
                        int n5 = message.reader().readByte();
                        byArray3 = new byte[n5];
                        Vector<TypeEquip> vector2 = new Vector<TypeEquip>();
                        for (n = 0; n < n5; ++n) {
                            byArray3[n] = message.reader().readByte();
                            TypeEquip typeEquip = new TypeEquip(byArray3[n]);
                            Vector<Equip> vector3 = new Vector<Equip>();
                            n4 = message.reader().readByte();
                            for (int j = 0; j < n4; ++j) {
                                Equip equip = new Equip();
                                equip.id = message.reader().readShort();
                                if (byArray3[n] == 0) {
                                    equip.bullet = message.reader().readByte();
                                }
                                equip.type = byArray3[n];
                                equip.glass = byArray4[i];
                                equip.icon = message.reader().readShort();
                                equip.level = message.reader().readByte();
                                equip.x = new short[n3];
                                equip.y = new short[n3];
                                equip.w = new byte[n3];
                                equip.h = new byte[n3];
                                equip.dx = new byte[n3];
                                equip.dy = new byte[n3];
                                for (int k = 0; k < n3; ++k) {
                                    equip.x[k] = message.reader().readShort();
                                    equip.y[k] = message.reader().readShort();
                                    equip.w[k] = message.reader().readByte();
                                    equip.h[k] = message.reader().readByte();
                                    equip.dx[k] = message.reader().readByte();
                                    equip.dy[k] = message.reader().readByte();
                                }
                                byte[] byArray5 = new byte[10];
                                for (int k = 0; k < 10; ++k) {
                                    byArray5[k] = message.reader().readByte();
                                }
                                equip.setInvAtribute();
                                equip.getInvAtribute(byArray5);
                                equip.getShopAtribute(byArray5);
                                vector3.addElement(equip);
                            }
                            typeEquip.addEquip(vector3);
                            vector2.addElement(typeEquip);
                        }
                        equipGlass.addType(vector2);
                        vector.addElement(equipGlass);
                    }
                    PlayerEquip.addGlassEquip(vector);
                    PlayerEquip.installDrabyEquipGlass();
                    PlayerEquip.installTrangPhucPlaceholders();
                    if (TerrainMidlet.myInfo != null) {
                        PlayerEquip.applyDefaultOfflineEquipIds(TerrainMidlet.myInfo);
                    }
                    short s2 = message.reader().readShort();
                    byte[] byArray6 = new byte[s2];
                    message.reader().read(byArray6, 0, s2);
                    CRes.out("2 =============================> read Trang bi  type = 1 ");
                    mImage.createImage("/equip/01.png", new IAction2(){

                        public void perform(Object object) {
                            EquipScreen.imgIconEQ[0] = new mImage((Image)object);
                        }
                    });
                    mImage.createImage("/equip/02.png", new IAction2(){

                        public void perform(Object object) {
                            EquipScreen.imgIconEQ[1] = new mImage((Image)object);
                        }
                    });
                    mImage.createImage("/equip/03.png", new IAction2(){

                        public void perform(Object object) {
                            EquipScreen.imgIconEQ[2] = new mImage((Image)object);
                        }
                    });
                    mImage.createImage("/equip/04.png", new IAction2(){

                        public void perform(Object object) {
                            EquipScreen.imgIconEQ[3] = new mImage((Image)object);
                        }
                    });
                    mImage.createImage("/equip/05.png", new IAction2(){

                        public void perform(Object object) {
                            EquipScreen.imgIconEQ[4] = new mImage((Image)object);
                        }
                    });
                    mImage.createImage("/equip/06.png", new IAction2(){

                        public void perform(Object object) {
                            EquipScreen.imgIconEQ[5] = new mImage((Image)object);
                        }
                    });
                    CRes.out("3 =============================> read Trang bi  type = 1 ");
                    byArray3 = null;
                    for (int i = 0; i < 10; ++i) {
                        n = message.reader().readShort();
                        byArray3 = new byte[n];
                        message.reader().read(byArray3, 0, n);
                        mImage.createImage(byArray3, 0, n, new IAction2(){

                            public void perform(Object object) {
                                try {
                                    PlayerEquip.bullets[indexBullet] = new mImage((Image)object);
                                    indexBullet = indexBullet + 1;
                                }
                                catch (Exception exception) {
                                }
                            }
                        });
                    }
                    byArray3 = null;
                    CRes.out("===================> create PlayerEquip.playerData to set myEquip!");
                    CRes.out("__ =============================> read Trang bi  type = 1 have PlayerEquip.playerData " + (PlayerEquip.playerData != null));
                    CRes.out("4 =============================> read Trang bi  type = 1 !!!!! DONE!!!!");
                }
                catch (Exception exception) {
                    exception.printStackTrace();
                }
                break;
            }
            case 2: {
                try {
                    int n = message.reader().readByte();
                    PlayerInfo.strLevelCaption = new String[n];
                    PlayerInfo.levelCaption = new int[n];
                    for (int i = 0; i < n; ++i) {
                        String string = message.reader().readUTF();
                        int n6 = message.reader().readUnsignedByte();
                        PlayerInfo.strLevelCaption[i] = string;
                        PlayerInfo.levelCaption[i] = n6;
                    }
                    if (TerrainMidlet.myInfo == null) break;
                    TerrainMidlet.myInfo.getQuanHam();
                    break;
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
        }
        message.cleanup();
        message = null;
    }

    public static void saveData(String string, byte[] byArray) {
        try {
            RMS.saveRMS(string, byArray);
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static byte[] loadData(String string) {
        return CRes.loadRMSData(string);
    }

    public static void saveVersion(String string, byte by) {
        CRes.saveRMSInt(string, by);
    }

    public static int loadVersion(String string) {
        return CRes.loadRMSInt(string);
    }

    public static Image cutImage(mImage mImage2, int n) {
        mImage mImage3 = CCanvas.cutBulletFrame(mImage2, n);
        return CCanvas.rasterizeFrame(mImage3);
    }

    public static Image cutImage(mImage mImage2, int n, IAction2 iAction2) {
        mImage mImage3 = CCanvas.cutBulletFrame(mImage2, n);
        Image image = CCanvas.rasterizeFrame(mImage3);
        if (image != null && iAction2 != null) {
            iAction2.perform(image);
        }
        return image;
    }

    public static Image rasterizeFrame(mImage mImage2) {
        if (mImage2 == null || mImage2.image == null) {
            return null;
        }
        int n = 0;
        int n2 = 0;
        int n3 = mImage2.image.getWidth();
        int n4 = mImage2.image.getHeight();
        if (mImage2.isRegion) {
            n = mImage2.regionX;
            n2 = mImage2.regionY;
            n3 = mImage2.regionW > 0 ? mImage2.regionW : n3;
            int n5 = n4 = mImage2.regionH > 0 ? mImage2.regionH : n4;
        }
        if (n3 <= 0 || n4 <= 0) {
            return null;
        }
        int[] nArray = new int[n3 * n4];
        try {
            mImage2.image.getRGB(nArray, 0, n3, n, n2, n3, n4);
        }
        catch (Exception exception) {
            return null;
        }
        return Image.createImageNotRunable(nArray, n3, n4);
    }

    public static mImage cutBulletFrame(mImage mImage2, int n) {
        int n2;
        int n3;
        int n4;
        int n5;
        int n6;
        if (mImage2 == null || mImage2.image == null) {
            return null;
        }
        int n7 = mImage2.image.getWidth();
        int n8 = mImage2.image.getHeight();
        if (n7 <= 0 || n8 <= 0) {
            return null;
        }
        int n9 = n6 = n < 0 ? 0 : n;
        if (n7 > n8) {
            n5 = n8;
            n4 = n8;
            int n10 = Math.max(1, n7 / n5);
            if (n6 >= n10) {
                n6 = 0;
            }
            n3 = n6 * n5;
            n2 = 0;
        } else {
            n5 = n7;
            n4 = n7;
            int n11 = Math.max(1, n8 / n4);
            if (n6 >= n11) {
                n6 = 0;
            }
            n3 = 0;
            n2 = n6 * n4;
        }
        if (n3 + n5 > n7) {
            n5 = n7 - n3;
        }
        if (n2 + n4 > n8) {
            n4 = n8 - n2;
        }
        if (n5 <= 0 || n4 <= 0) {
            return null;
        }
        return mImage.cutRegion(mImage2, n3, n2, n5, n4);
    }

    public static Image rotateImage(Image image, int n, mGraphics mGraphics2, int n2, int n3, boolean bl) {
        int n4 = image.getWidth();
        int n5 = image.getHeight();
        int[] nArray = new int[n4 * n5];
        image.getRGB(nArray, 0, n4, 0, 0, n4, n5);
        int[] nArray2 = new int[n4 * n5];
        float f = CRes.sin(n);
        float f2 = CRes.cos(n);
        int n6 = (int)(256.0f * f) / 1000;
        int n7 = (int)(256.0f * f2) / 1000;
        int n8 = -(n5 >> 1);
        for (int i = 0; i < n5; ++i) {
            int n9 = i * n4;
            int n10 = n8 * n6 - (n4 >> 1) * n7 + (n4 >> 1 << 8);
            int n11 = n8 * n7 + (n4 >> 1) * n6 + (n5 >> 1 << 8);
            for (int j = 0; j < n4; ++j) {
                int n12 = n10 >> 8;
                int n13 = n11 >> 8;
                if (n12 < 0) {
                    n12 = 0;
                }
                if (n13 < 0) {
                    n13 = 0;
                }
                if (n12 > n4 - 1) {
                    n12 = n4 - 1;
                }
                if (n13 > n5 - 1) {
                    n13 = n5 - 1;
                }
                nArray2[n9++] = nArray[n12 + n13 * n4];
                n10 += n7;
                n11 -= n6;
            }
            ++n8;
        }
        bl = false;
        if (bl) {
            mGraphics2.drawRGB(nArray2, 0, n4, n2 - n4 / 2, n3 - n4 / 2, n4, n5, true);
            return null;
        }
        return Image.createRGBImage(nArray2, n4, n5, true);
    }

    public static void rotateImage(Image image, int n, mGraphics mGraphics2, int n2, int n3, boolean bl, IAction2 iAction2) {
        int n4 = image.getWidth();
        int n5 = image.getHeight();
        int[] nArray = new int[n4 * n5];
        image.getRGB(nArray, 0, n4, 0, 0, n4, n5);
        int[] nArray2 = new int[n4 * n5];
        float f = CRes.sin(n);
        float f2 = CRes.cos(n);
        int n6 = (int)(256.0f * f) / 1000;
        int n7 = (int)(256.0f * f2) / 1000;
        int n8 = -(n5 >> 1);
        for (int i = 0; i < n5; ++i) {
            int n9 = i * n4;
            int n10 = n8 * n6 - (n4 >> 1) * n7 + (n4 >> 1 << 8);
            int n11 = n8 * n7 + (n4 >> 1) * n6 + (n5 >> 1 << 8);
            for (int j = 0; j < n4; ++j) {
                int n12 = n10 >> 8;
                int n13 = n11 >> 8;
                if (n12 < 0) {
                    n12 = 0;
                }
                if (n13 < 0) {
                    n13 = 0;
                }
                if (n12 > n4 - 1) {
                    n12 = n4 - 1;
                }
                if (n13 > n5 - 1) {
                    n13 = n5 - 1;
                }
                nArray2[n9++] = nArray[n12 + n13 * n4];
                n10 += n7;
                n11 -= n6;
            }
            ++n8;
        }
        Image.createRGBImage(nArray2, n4, n5, true, iAction2);
    }

    public static void rotateImage(mImage mImage2, int n, mGraphics mGraphics2, int n2, int n3, boolean bl) {
        if (mImage2 == null || mGraphics2 == null) {
            return;
        }
        int w = mImage2.regionW > 0 ? mImage2.regionW : (mImage2.image != null ? mImage2.image.getWidth() : 0);
        int h = mImage2.regionH > 0 ? mImage2.regionH : (mImage2.image != null ? mImage2.image.getHeight() : 0);
        if (w <= 0 || h <= 0) {
            return;
        }
        int n9 = n % 360;
        if (n9 < 0) {
            n9 += 360;
        }
        int n4 = (n9 + 45) / 90 & 3;
        int n10 = n4 == 1 ? 5 : (n4 == 2 ? 3 : (n4 == 3 ? 6 : 0));
        mGraphics2.drawRegion(mImage2, 0, 0, w, h, n10, n2, n3, mGraphics.VCENTER | mGraphics.HCENTER, false);
    }

    public static boolean isPointer(int n, int n2, int n3, int n4, int n5) {
        if (!isPointerDown[n5] && !isPointerClick[n5]) {
            return false;
        }
        return pX[n5] >= n && pX[n5] <= n + n3 && pY[n5] >= n2 && pY[n5] <= n2 + n4;
    }

    public static boolean isPointerPad(int n, int n2, int n3, int n4) {
        if (!isPointerDown[0] && !isPointerRelease[0]) {
            return false;
        }
        return pX[0] >= n && pX[0] <= n + n3 && pY[0] >= n2 && pY[0] <= n2 + n4;
    }

    public static boolean isTouchOnGamePad(int n, int n2) {
        return gamePad != null && gamePad.containsPoint(n, n2);
    }

    private void dispatchScreenPointerPressed(int n, int n2, int n3) {
        if (pausemenu != null && CCanvas.pausemenu.isShow) {
            return;
        }
        if (menu != null && CCanvas.menu.showMenu) {
            return;
        }
        if (currentDialog != null) {
            currentDialog.onPointerPressed(n, n2, n3);
            if (inputDlg != null) {
                inputDlg.onPointerPressed(n, n2, n3);
            }
        }
    }

    private void syncPauseMenuState() {
        if (curScr instanceof GameScr) {
            ((GameScr)CCanvas.curScr).isShowPausemenu = pausemenu != null && CCanvas.pausemenu.isShow;
        }
    }

    private static void handleChatKey() {
        if (curScr instanceof GameScr) {
            ((GameScr)curScr).onMaxForceKey();
        } else if (curScr instanceof PrepareScr && PrepareScr.tfChat != null) {
            PrepareScr.tfChat.doChangeToTextBox();
        } else if (inputDlg != null && currentDialog == inputDlg && CCanvas.inputDlg.tfInput != null) {
            CCanvas.inputDlg.tfInput.doChangeToTextBox();
        }
    }

    public void onResize(int n, int n2) {
        this.recalcScreenSize();
        GamePad.init();
    }

    public static boolean isPointerLast(int n, int n2, int n3, int n4, int n5) {
        if (!isPointerDown[n5] && !isPointerClick[n5]) {
            return false;
        }
        return pxLast[n5] >= n && pxLast[n5] <= n + n3 && pyLast[n5] >= n2 && pyLast[n5] <= n2 + n4;
    }

    public static void startOKDlg(String string) {
        msgdlg.setInfo(string, null, new Command("OK", new IAction(){

            public void perform() {
                currentDialog = null;
                if (CCanvas.curScr.menuScroll) {
                    menuScr.startScrollDown();
                }
            }
        }), null);
        currentDialog = msgdlg;
    }

    public static void startOKDlg(String string, final IAction iAction) {
        msgdlg.setInfo(string, null, new Command("OK", new IAction(){

            public void perform() {
                if (CCanvas.curScr.menuScroll) {
                    menuScr.startScrollDown();
                }
                currentDialog = null;
                if (iAction != null) {
                    iAction.perform();
                }
            }
        }), null);
        currentDialog = msgdlg;
    }

    public static void startYesNoDlg(String string, IAction iAction) {
        msgdlg.setInfo(string, new Command(Language.yes(), iAction), new Command("", iAction), new Command(Language.no(), new IAction(){

            public void perform() {
                if (CCanvas.curScr.menuScroll) {
                    menuScr.startScrollDown();
                }
                currentDialog = null;
            }
        }));
        currentDialog = msgdlg;
    }

    public static void startYesNoDlg(String string, IAction iAction, IAction iAction2) {
        msgdlg.setInfo(string, new Command(Language.yes(), iAction), new Command("", iAction), new Command(Language.no(), iAction2));
        currentDialog = msgdlg;
    }

    public static void startWaitDlg(String string) {
        msgdlg.setInfo(string, null, new Command("Cancel", new IAction(){

            public void perform() {
                if (CCanvas.curScr.menuScroll) {
                    menuScr.startScrollDown();
                }
                currentDialog = null;
            }
        }), null);
        currentDialog = msgdlg;
    }

    public static void startWaitDlgWithoutCancel(String string, int n) {
        msgdlg.setInfo(string, null, null, null);
        currentDialog = msgdlg;
    }

    public static void startWaitDlgWithoutCancel(String string, long l, IAction iAction) {
        msgdlg.setInfo(string, l, iAction, null, null, null);
        currentDialog = msgdlg;
    }

    public static int random(int n, int n2) {
        return n + r.nextInt(n2 - n);
    }

    public static void endDlg() {
        if (currentDialog != null) {
            currentDialog.close();
        }
        currentDialog = null;
        for (int i = 0; i < keyHold.length; ++i) {
            keyHold[i] = false;
            keyPressed[i] = false;
        }
        CScreen.clearKey();
        CScreen.keyFire = false;
        if (curScr instanceof GameScr) {
            ((GameScr)curScr).timeDelayClosePauseMenu = mSystem.currentTimeMillis() + 350L;
            if (GameScr.pm != null && PM.getMyPlayer() != null) {
                PM.getMyPlayer().force = 0;
                PM.getMyPlayer().force_2 = 0;
                if (PM.getMyPlayer().getState() == 3) {
                    PM.getMyPlayer().setState((byte)0);
                }
            }
        }
    }

    public void stopGame() {
        this.isRunning = false;
        if (gameScr != null) {
            GameService.gI().leaveBoard();
        }
    }

    public void onPointerDragged(int n, int n2, int n3) {
        CCanvas.isPointerSelect[n3] = false;
        CCanvas.pX[n3] = n;
        CCanvas.pY[n3] = n2;
        if (isPointerMove[n3]) {
            this.listPoint.addElement(new Position(n, n2));
        } else if (CRes.abs(pX[n3] - pxLast[n3]) >= 15 || CRes.abs(pY[n3] - pyLast[n3]) >= 15) {
            CCanvas.isPointerMove[n3] = true;
        }
        ++this.curPos;
        if (this.curPos > 3) {
            this.curPos = 0;
        }
        if (currentDialog == null) {
            if (menu != null && CCanvas.menu.showMenu) {
                menu.onPointerDragged(n, n2, n3);
            } else if (gamePad != null && CCanvas.isTouchOnGamePad(n, n2)) {
                if (gamePad.onPointerMove(n, n2)) {
                    this.dispatchScreenPointerPressed(n, n2, n3);
                }
            } else if (curScr != null) {
                curScr.onPointerDragged(n, n2, n3);
            }
        }
    }

    public void onPointerPressed(int n, int n2, int n3, int n4) {
        CRes.out("==> press " + (mSystem.currentTimeMillis() - timeClick));
        isDoubleClick = mSystem.currentTimeMillis() - timeClick < (long)MAX_TIME_CLICK;
        CCanvas.isPointerDown[n3] = true;
        CCanvas.isPointerRelease[n3] = false;
        CCanvas.isPointerMove[n3] = false;
        CCanvas.isPointerSelect[n3] = false;
        CCanvas.pxFirst[n3] = n;
        CCanvas.pyFirst[n3] = n2;
        CCanvas.pX[n3] = n;
        CCanvas.pY[n3] = n2;
        pointer = n3;
        button = n4;
        for (int i = 0; i < arrPopups.size(); ++i) {
            ((Popup)arrPopups.elementAt(i)).onPointerPressed(n, n2, n3);
        }
        if (gamePad != null && CCanvas.isTouchOnGamePad(n, n2)) {
            gamePad.onPointerDown(n, n2);
            this.dispatchScreenPointerPressed(n, n2, n3);
            this.syncPauseMenuState();
            return;
        }
        if (pausemenu != null && CCanvas.pausemenu.isShow) {
            pausemenu.onPointerPressed(n, n2, n3);
        } else if (menu != null && CCanvas.menu.showMenu) {
            menu.onPointerPressed(n, n2, n3);
        } else if (currentDialog != null) {
            currentDialog.onPointerPressed(n, n2, n3);
            if (inputDlg != null) {
                inputDlg.onPointerPressed(n, n2, n3);
            }
        } else if (curScr != null) {
            curScr.onPointerPressed(n, n2, n3);
        }
    }

    public void onPointerReleased(int n, int n2, int n3, int n4) {
        timeClick = mSystem.currentTimeMillis();
        if (!isPointerMove[n3]) {
            CCanvas.isPointerSelect[n3] = true;
        }
        CCanvas.isPointerDown[n3] = false;
        CCanvas.isPointerRelease[n3] = true;
        CCanvas.isPointerMove[n3] = false;
        CCanvas.isPointerClick[n3] = true;
        CCanvas.pxLast[n3] = n;
        CCanvas.pyLast[n3] = n2;
        pointer = n3;
        button = n4;
        if (pausemenu != null && CCanvas.pausemenu.isShow) {
            pausemenu.onPointerRealeased(n, n2, n3);
            this.syncPauseMenuState();
        } else if (menu != null && CCanvas.menu.showMenu) {
            menu.onPointerReleased(n, n2, n3);
        } else if (currentDialog != null) {
            currentDialog.onPointerReleased(n, n2, n3);
        } else {
            for (int i = 0; i < arrPopups.size(); ++i) {
                Popup popup = (Popup)arrPopups.elementAt(i);
                if (!(popup instanceof MsgPopup)) continue;
                ((Popup)arrPopups.elementAt(i)).onPointerReleased(n, n2, n3);
            }
            if (curScr != null) {
                if (curScr instanceof GameScr) {
                    if (gamePad == null || !CCanvas.isTouchOnGamePad(n, n2)) {
                        curScr.onPointerReleased(n, n2, n3);
                    }
                } else {
                    curScr.onPointerReleased(n, n2, n3);
                }
            }
        }
        if (gamePad != null && CCanvas.isTouchOnGamePad(n, n2)) {
            gamePad.onPointerUp(n, n2);
        }
    }

    public void onPointerHolder(int n, int n2, int n3) {
        if (n3 != -1 && mSystem.currentTimeMillis() >= this.timeHold) {
            this.timeHold = mSystem.currentTimeMillis() + 50L;
            CCanvas.pX[n3] = n;
            CCanvas.pY[n3] = n2;
            pointer = n3;
            button = -1;
            if (CCanvas.isTouchOnGamePad(n, n2)) {
                return;
            }
            CCanvas.isPointerDown[n3] = true;
            CCanvas.isPointerRelease[n3] = false;
            CCanvas.isPointerMove[n3] = false;
            CCanvas.isPointerSelect[n3] = false;
            if (!(curScr == null || curScr instanceof GameScr && CCanvas.isTouchOnGamePad(n, n2))) {
                curScr.onPointerHold(n, n2, n3);
            }
        }
    }

    public void onPointerHolder() {
    }

    public void keyHold(int n) {
    }

    public void keyHold(char c) {
    }

    public void perform(int n, Object object) {
    }

    public static void clearGameplayKeys() {
        CCanvas.keyHold[2] = false;
        CCanvas.keyHold[4] = false;
        CCanvas.keyHold[5] = false;
        CCanvas.keyHold[6] = false;
        CCanvas.keyHold[8] = false;
        CCanvas.keyPressed[2] = false;
        CCanvas.keyPressed[4] = false;
        CCanvas.keyPressed[5] = false;
        CCanvas.keyPressed[6] = false;
        CCanvas.keyPressed[8] = false;
    }

    public static void clearKeyHold() {
    }

    public static void resetTrans(mGraphics mGraphics2) {
    }

    public void onClearMap() {
    }

    public static boolean isTouchAndKey() {
        return false;
    }

    public static boolean isTouchNoOrPC() {
        return !isTouch || CCanvas.isTouchAndKey();
    }

    public static boolean isJ2ME() {
        return GameMidlet.DEVICE == 0;
    }

    public static boolean isPc() {
        return GameMidlet.DEVICE == 4;
    }

    public static boolean isIos() {
        return GameMidlet.DEVICE == 2 || GameMidlet.DEVICE == 6;
    }

    public static boolean isIosStore() {
        return GameMidlet.DEVICE == 6;
    }

    public static boolean isGDX() {
        return CCanvas.isPc() || CCanvas.isIos();
    }

    public static boolean isAndroid() {
        return GameMidlet.DEVICE == 1 || GameMidlet.DEVICE == 5;
    }

    public static boolean isAndroidStore() {
        return GameMidlet.DEVICE == 5;
    }

    public static boolean isStore() {
        return GameMidlet.DEVICE == 5 || GameMidlet.DEVICE == 6;
    }

    public static boolean isDebugging() {
        return GameMidlet.COMPILE == 0;
    }

    public static boolean isTabScreen() {
        return false;
    }

    public static boolean isTabClanScreen() {
        return false;
    }

    public static int getIPdx() {
        return CCanvas.isIos() ? 20 : 0;
    }

    public static String getClassPathConfig(String string) {
        return "/res/" + string;
    }

    public void backAndroid() {
        if (curScr != null && CCanvas.curScr.right != null) {
            CCanvas.curScr.right.action.perform();
        }
    }

    public static void onClearCCanvas() {
        luckyGame = null;
        shopEquipScr = null;
        shopItemScr = null;
        shopLinhtinh = null;
        listScr = null;
        msgScr = null;
        infoPopup = null;
        prepareScr = null;
        inputDlg = null;
        msgPopup = null;
        clanScreen = null;
        topClanScreen = null;
        serverListScreen = null;
        luckyGifrScreen = null;
        fomulaScreen = null;
        archScreen = null;
        moneyScr = null;
        moneyScrIOS = null;
        inventory = null;
        equipScreen = null;
        gameScr = null;
        pausemenu = null;
        msgdlg = new MsgDlg();
        currentDialog = null;
        arrPopups = new Vector();
        msgPopup = null;
        infoPopup = null;
    }

    static {
        menu = new Menu();
        pausemenu = new PauseMenu();
        isWifi = false;
        msgdlg = new MsgDlg();
        arrPopups = new Vector();
        pX = new int[2];
        pY = new int[2];
        pxLast = new int[2];
        pyLast = new int[2];
        pxFirst = new int[2];
        pyFirst = new int[2];
        keyPressed = new boolean[55];
        keyReleased = new boolean[55];
        keyHold = new boolean[55];
        isPointerDown = new boolean[2];
        isPointerRelease = new boolean[2];
        isPointerSelect = new boolean[2];
        isPointerMove = new boolean[2];
        isPointerClick = new boolean[2];
        isVirtualKey = true;
        MAX_TIME_CLICK = 400;
        indexBullet = 0;
        r = new Random();
    }
}

