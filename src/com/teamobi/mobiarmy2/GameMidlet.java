/*
 * Decompiled with CFR 0.152.
 *
 * Could not load the following classes:
 *  javax.microedition.midlet.MIDlet
 *  javax.microedition.midlet.MIDletStateChangeException
 */
package com.teamobi.mobiarmy2;

import CLib.LibSysTem;
import CLib.RMS;
import CLib.mGraphics;
import CLib.mSystem;
import Equipment.PlayerEquip;
import com.teamobi.mobiarmy2.CloudSaveApi;
import com.teamobi.mobiarmy2.IActionListener;
import com.teamobi.mobiarmy2.MotherCanvas;
import com.teamobi.mobiarmy2.OfflineBossAI;
import com.teamobi.mobiarmy2.OfflineBossFight;
import com.teamobi.mobiarmy2.OfflineBulletAssets;
import com.teamobi.mobiarmy2.OfflineChest;
import com.teamobi.mobiarmy2.OfflineCombat;
import com.teamobi.mobiarmy2.OfflineEquipmentStats;
import com.teamobi.mobiarmy2.OfflineGunAngles;
import com.teamobi.mobiarmy2.OfflineMission;
import com.teamobi.mobiarmy2.OfflinePvpBot;
import com.teamobi.mobiarmy2.OfflinePvpBotAI;
import com.teamobi.mobiarmy2.OfflineSave;
import com.teamobi.mobiarmy2.OfflineSettings;
import com.teamobi.mobiarmy2.OfflineTeamItems;
import coreLG.CCanvas;
import coreLG.TerrainMidlet;
import item.BM;
import item.Item;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;
import map.MM;
import map.MiniMap;
import model.CRes;
import model.Font;
import model.IAction2;
import model.Language;
import model.PlayerInfo;
import model.RoomInfo;
import network.GameLogicHandler;
import network.GameService;
import network.Message;
import network.MessageHandler;
import network.Session_ME;
import player.CPlayer;
import player.PM;
import screen.BoardListScr;
import screen.ChangePlayerCSr;
import screen.GameScr;
import screen.MenuScr;
import screen.PrepareScr;
import screen.PvpBotSetupScr;
import screen.RoomListScr2;
import screen.ServerListScreen;
import shop.ShopItem;

public class GameMidlet
extends MIDlet
implements IActionListener {
    public static GameMidlet instance;
    public static CCanvas gameCanvas;
    public static String version;
    public static short versionByte;
    public static byte versioncode;
    public static byte server;
    public static String serverName;
    public static int timePingPaint;
    public static int pingCount;
    public static boolean ping;
    public static short versionServer;
    public static boolean isStartGame;
    public static final byte NONE = 0;
    public static final byte NOKIA_STORE = 1;
    public static final byte GOOGLE_STORE = 2;
    public static final byte IOS_STORE = 3;
    public static final byte DEVICE_TYPE_JAVA = 0;
    public static final byte DEVICE_TYPE_ANDROID = 1;
    public static final byte DEVICE_TYPE_IOS = 2;
    public static final byte DEVICE_TYPE_WINPHONE = 3;
    public static final byte DEVICE_TYPE_PC = 4;
    public static final byte DEVICE_TYPE_ANDROID_STORE = 5;
    public static final byte DEVICE_TYPE_IOS_STORE = 6;
    public static final byte DEVICE_TYPE_DEV = 7;
    public static byte DEVICE;
    public static final byte DEVELOPING = 0;
    public static final byte BUILD = 1;
    public static final byte OTHER = 2;
    public static byte COMPILE;
    public static boolean lowGraphic;
    public static byte currentIAPStore;
    public static byte langServer;
    public static byte ZOOM_IOS;
    public static byte PROVIDER;
    public static final byte BIG_PROVIDER = 0;
    public static String IP;
    public static int PORT;
    public static PlayerInfo myInfo;
    public static String AGENT;
    public static byte filePackVersion;
    public static boolean[] isVip;
    public static boolean isTeamClient;
    public static String linkGetHost;
    public static String linkReg;
    public static String latitude;
    public static String longitude;
    public static final boolean OFFLINE_MODE = true;
    public static String OFFLINE_VERSION_TEXT;
    private static boolean offlineVersionLoaded;
    public static final byte[] OFFLINE_BOSS_ROOM_ID;
    public static final byte[] OFFLINE_BOSS_BOARD_ID;
    private static final String[] BOSS_ROOM_DISPLAY_NAMES;
    public static byte pendingOfflineBossRoomIndex;
    public static boolean pendingOfflinePvpBot;

    public void initGame() {
        gameCanvas = new CCanvas();
        this.initGame2();
    }

    private void initGame2() {
        String string;
        InputStream inputStream = this.getClass().getResourceAsStream("/" + LibSysTem.res + "/provider.txt");
        try {
            byte[] byArray = new byte[inputStream.available()];
            inputStream.read(byArray);
            string = new String(byArray, "UTF-8");
            PROVIDER = Byte.parseByte(string);
        }
        catch (Exception exception) {
        }
        string = GameLogicHandler.loadIP();
        if (string != null && string.length() > 0) {
            try {
                int n = string.indexOf(":");
                String string2 = string.substring(0, n);
                String string3 = string.substring(n + 1);
                IP = string2;
                PORT = Integer.parseInt(string3);
            }
            catch (Exception exception) {
                System.err.println("===> error midlet connects " + exception);
            }
        }
        OfflineSettings.applyFps();
        gameCanvas.start();
        MessageHandler.gI().setGameLogicHandler(GameLogicHandler.gI());
        Session_ME.gI().setHandler(MessageHandler.gI());
        GameService.gI().setSession(Session_ME.gI());
        ChangePlayerCSr.ensureGunData();
        GameMidlet.setcurrentIAPStore();
    }

    private static void loadOfflineVersionText() {
        if (offlineVersionLoaded) {
            return;
        }
        offlineVersionLoaded = true;
        byte[] byArray = CCanvas.readResAssetBytes("/version.txt");
        if (byArray == null || byArray.length == 0) {
            return;
        }
        try {
            String string = new String(byArray, "UTF-8").trim();
            if (string.length() > 0) {
                OFFLINE_VERSION_TEXT = "v" + string;
            }
        }
        catch (Exception exception) {
        }
    }

    public static void ensureOfflineAssetsLoaded() {
        CCanvas.loadCachedGameData();
        OfflineBulletAssets.ensureHoleMasks();
        GameMidlet.loadOfflineVersionText();
    }

    public static void enterOfflineMenu() {
        GameMidlet.ensureOfflineAssetsLoaded();
        MenuScr.getIdMenu(0);
        GameScr.initMenuEffects();
        if (CCanvas.menuScr == null) {
            CCanvas.menuScr = new MenuScr();
        } else {
            CCanvas.menuScr.resetInputState();
            CCanvas.menuScr.activeCroll(0, 0);
        }
        CCanvas.menuScr.show();
    }

    public static void ensureMapRuntimeData() {
        MM.undestroyTile = new short[]{70, 71, 73, 74, 75, 77, 78, 79, 97};
    }

    public static void ensureMapPackLoaded() {
        byte[] byArray;
        GameMidlet.ensureOfflineAssetsLoaded();
        if (MM.mapFiles != null && MM.mapFiles.size() > 0) {
            return;
        }
        byte[] byArray2 = CCanvas.loadData("valuesdata2");
        if (byArray2 != null) {
            CCanvas.readMess(byArray2, (byte)0);
        }
        if ((byArray = CCanvas.loadData("tiledata2")) != null) {
            MM.fullData = byArray;
        }
    }

    public static void openOfflineBossRoomList() {
        GameMidlet.ensureOfflineAssetsLoaded();
        GameMidlet.ensureMapPackLoaded();
        GameMidlet.ensureOfflineBossData();
        GameMidlet.ensureMapRuntimeData();
        CCanvas.endDlg();
        Vector<RoomInfo> vector = new Vector<RoomInfo>();
        for (int i = 0; i < MenuScr.BOSS_ROOM_NAMES.length; ++i) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.id = OFFLINE_BOSS_ROOM_ID[i];
            roomInfo.boardID = OFFLINE_BOSS_BOARD_ID[i];
            roomInfo.lv = (byte)i;
            roomInfo.name = BOSS_ROOM_DISPLAY_NAMES[i];
            roomInfo.playerMax = Language.room() + " " + (i + 1);
            vector.addElement(roomInfo);
        }
        if (CCanvas.roomListScr2 == null) {
            CCanvas.roomListScr2 = new RoomListScr2();
        }
        CCanvas.roomListScr2.isEmptyRoom = false;
        CCanvas.roomListScr2.isOfflineBossList = true;
        CCanvas.roomListScr2.setRoomList(vector);
        CCanvas.roomListScr2.show();
    }

    public static void openOfflineAreaRoomList() {
        GameMidlet.ensureOfflineAssetsLoaded();
        GameMidlet.ensureMapPackLoaded();
        GameMidlet.ensureMapRuntimeData();
        CCanvas.endDlg();
        GameService.gI().requestRoomListOffline();
    }

    public static void leaveOfflineBattle() {
        CCanvas.endDlg();
        boolean bl = PrepareScr.isPvpBotRoom;
        pendingOfflineBossRoomIndex = (byte)-1;
        pendingOfflinePvpBot = false;
        OfflineBossAI.reset();
        OfflinePvpBotAI.reset();
        OfflineBossFight.currentRoomIndex = (byte)-1;
        Session_ME.receiveSynchronized = 0;
        CCanvas.lockNotify = false;
        CCanvas.tNotify = 0;
        GameScr.trainingMode = false;
        GameScr.res = "";
        if (CCanvas.gameScr != null) {
            CCanvas.gameScr.isShowPausemenu = false;
            if (GameScr.bm != null) {
                GameScr.bm.bullets.removeAllElements();
            }
            BM.active = false;
            MiniMap.leaveBattle();
            if (PM.p != null) {
                for (int i = 0; i < PM.p.length; ++i) {
                    PM.p[i] = null;
                }
            }
            if (GameScr.mm != null) {
                CCanvas.gameScr.onClearMap();
            }
        }
        if (bl) {
            PrepareScr.isPvpBotRoom = false;
            GameMidlet.openPvpBotSetup();
        } else if (PrepareScr.isBossRoom) {
            GameMidlet.openOfflineBossRoomList();
        } else {
            GameMidlet.enterOfflineMenu();
        }
    }

    public static void ensureOfflineBossData() {
        PrepareScr.mapBossID = new byte[]{30, 31, 32, 33, 34, 35, 36, 37, 38, 39};
        PrepareScr.bossID = new byte[]{12, 12, 13, 14, 15, 16, 17, 22, 25, 26};
    }

    public static void openOfflineBossRoom(byte by) {
        if (by < 0 || by >= MenuScr.BOSS_ROOM_NAMES.length) {
            return;
        }
        GameMidlet.ensureOfflineAssetsLoaded();
        GameMidlet.ensureMapPackLoaded();
        GameMidlet.ensureOfflineBossData();
        GameMidlet.ensureMapRuntimeData();
        CCanvas.endDlg();
        BoardListScr.boardName = BOSS_ROOM_DISPLAY_NAMES[by];
        PrepareScr.currentRoom = OFFLINE_BOSS_ROOM_ID[by];
        PrepareScr.isBossRoom = true;
        PrepareScr.isPvpBotRoom = false;
        PrepareScr.isRelayRoom = false;
        PrepareScr.currLevel = (byte)5;
        PrepareScr.curMap = PrepareScr.mapBossID[by];
        if (CCanvas.prepareScr == null) {
            CCanvas.prepareScr = new PrepareScr();
        }
        GameMidlet.populateOfflineBossParticipants(by);
        pendingOfflineBossRoomIndex = by;
        CCanvas.prepareScr.resetReady();
        CCanvas.prepareScr.show();
    }

    public static void beginPendingOfflineBossFight() {
        if (pendingOfflineBossRoomIndex < 0) {
            return;
        }
        byte by = pendingOfflineBossRoomIndex;
        pendingOfflineBossRoomIndex = (byte)-1;
        try {
            GameMidlet.startOfflineBossFight(by);
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            CCanvas.endDlg();
            CCanvas.startOKDlg("L\u1ed7i khi v\u00e0o tr\u1eadn: " + throwable);
        }
    }

    private static void populateOfflineBossParticipants(byte by) {
        if (CPlayer.fileData == null) {
            GameMidlet.ensureOfflineAssetsLoaded();
        }
        CPlayer.init();
        ChangePlayerCSr.ensureGunData();
        OfflineGunAngles.applyServerAngleLocks();
        TerrainMidlet.myInfo.ensureCombatEquip();
        TerrainMidlet.myInfo.getQuanHam();
        TerrainMidlet.myInfo.setAllEquipEffect();
        TerrainMidlet.myInfo.maxHP = OfflineEquipmentStats.maxHp(TerrainMidlet.myInfo);
        CCanvas.prepareScr.setOwner(TerrainMidlet.myInfo.IDDB);
        CCanvas.prepareScr.initItemCurrent();
        CCanvas.prepareScr.playerInfos.removeAllElements();
        CCanvas.prepareScr.playerInfos.addElement(TerrainMidlet.myInfo);
        int n = 1;
        for (int i = 0; i < TerrainMidlet.myInfo.squadExtra.length; ++i) {
            byte by2 = TerrainMidlet.myInfo.squadExtra[i];
            if (by2 < 0 || by2 == TerrainMidlet.myInfo.gun) continue;
            CCanvas.prepareScr.playerInfos.addElement(TerrainMidlet.myInfo.createSquadSnapshot(by2));
            ++n;
        }
        while (n < 8) {
            CCanvas.prepareScr.playerInfos.addElement(new PlayerInfo());
            ++n;
        }
        CCanvas.prepareScr.bossInfos.removeAllElements();
        PlayerInfo playerInfo = new PlayerInfo();
        playerInfo.IDDB = -1 - by;
        playerInfo.name = MenuScr.BOSS_ROOM_NAMES[by];
        OfflineBossFight.prepareBossInfo(by, playerInfo);
        CCanvas.prepareScr.bossInfos.addElement(playerInfo);
    }

    public static void openPvpBotSetup() {
        GameMidlet.ensureOfflineAssetsLoaded();
        GameMidlet.ensureMapPackLoaded();
        GameMidlet.ensureMapRuntimeData();
        CCanvas.endDlg();
        OfflinePvpBot.resetPending();
        if (CCanvas.pvpBotSetupScr == null) {
            CCanvas.pvpBotSetupScr = new PvpBotSetupScr();
        }
        CCanvas.pvpBotSetupScr.show(CCanvas.menuScr);
    }

    private static void populateOfflinePvpBotParticipants() {
        int n;
        if (CPlayer.fileData == null) {
            GameMidlet.ensureOfflineAssetsLoaded();
        }
        CPlayer.init();
        ChangePlayerCSr.ensureGunData();
        OfflineGunAngles.applyServerAngleLocks();
        TerrainMidlet.myInfo.ensureCombatEquip();
        TerrainMidlet.myInfo.getQuanHam();
        TerrainMidlet.myInfo.setAllEquipEffect();
        TerrainMidlet.myInfo.maxHP = OfflineEquipmentStats.maxHp(TerrainMidlet.myInfo);
        CCanvas.prepareScr.setOwner(TerrainMidlet.myInfo.IDDB);
        CCanvas.prepareScr.initItemCurrent();
        CCanvas.prepareScr.playerInfos.removeAllElements();
        CCanvas.prepareScr.playerInfos.addElement(TerrainMidlet.myInfo);
        int n2 = 1;
        for (int i = 0; i < TerrainMidlet.myInfo.squadExtra.length; ++i) {
            n = TerrainMidlet.myInfo.squadExtra[i];
            if (n < 0 || n == TerrainMidlet.myInfo.gun) continue;
            CCanvas.prepareScr.playerInfos.addElement(TerrainMidlet.myInfo.createSquadSnapshot((byte)n));
            ++n2;
        }
        OfflinePvpBot.pendingSquadCount = n2;
        CCanvas.prepareScr.bossInfos.removeAllElements();
        n = OfflinePvpBot.pendingBotCount;
        for (int i = 0; i < n && n2 < 8; ++n2, ++i) {
            PlayerInfo playerInfo = OfflinePvpBot.generateBot(i, OfflinePvpBot.pendingDifficulty);
            CCanvas.prepareScr.playerInfos.addElement(playerInfo);
            CCanvas.prepareScr.bossInfos.addElement(playerInfo);
        }
        while (n2 < 8) {
            CCanvas.prepareScr.playerInfos.addElement(new PlayerInfo());
            ++n2;
        }
    }

    public static void openOfflinePvpBotPrepare() {
        GameMidlet.ensureOfflineAssetsLoaded();
        GameMidlet.ensureMapPackLoaded();
        GameMidlet.ensureMapRuntimeData();
        CCanvas.endDlg();
        BoardListScr.boardName = "PVP Bot";
        PrepareScr.currentRoom = 0;
        PrepareScr.isBossRoom = false;
        PrepareScr.isPvpBotRoom = true;
        PrepareScr.isRelayRoom = false;
        PrepareScr.currLevel = (byte)5;
        PrepareScr.curMap = (byte)OfflinePvpBot.pendingMapId;
        if (CCanvas.prepareScr == null) {
            CCanvas.prepareScr = new PrepareScr();
        }
        GameMidlet.populateOfflinePvpBotParticipants();
        pendingOfflinePvpBot = true;
        CCanvas.prepareScr.resetReady();
        CCanvas.prepareScr.show();
    }

    public static void beginPendingOfflinePvpBotFight() {
        if (!pendingOfflinePvpBot) {
            return;
        }
        pendingOfflinePvpBot = false;
        try {
            GameMidlet.startOfflinePvpBotFight();
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            if (PM.p != null) {
                for (int i = 0; i < PM.p.length; ++i) {
                    PM.p[i] = null;
                }
            }
            OfflineBossAI.reset();
            OfflinePvpBotAI.reset();
            OfflineCombat.reset();
            try {
                GameMidlet.startOfflinePvpBotFight();
                return;
            }
            catch (Throwable throwable2) {
                throwable2.printStackTrace();
                CCanvas.endDlg();
                CCanvas.startOKDlg("L\u1ed7i khi v\u00e0o tr\u1eadn: " + throwable2);
            }
        }
    }

    private static void startOfflinePvpBotFight() {
        int n;
        int n2;
        int n3;
        int n4;
        byte by = (byte)OfflinePvpBot.pendingMapId;
        GameMidlet.ensureMapPackLoaded();
        if (MM.mapFiles == null || MM.mapFiles.size() == 0) {
            CCanvas.startOKDlg("Thi\u1ebfu file assets/rms/valuesdata2. Copy t\u1eeb server/cache ho\u1eb7c ch\u01a1i online m\u1ed9t l\u1ea7n.");
            return;
        }
        if (CCanvas.prepareScr == null) {
            CCanvas.prepareScr = new PrepareScr();
        }
        GameScr.trainingMode = false;
        if (CCanvas.gameScr == null) {
            CCanvas.gameScr = new GameScr();
        }
        if (MM.maps != null) {
            MM.maps.removeAllElements();
        }
        GameMidlet.ensureMapRuntimeData();
        GameScr.mm.createMap(by);
        if (MM.mapWidth <= 0 || MM.mapHeight <= 0) {
            CCanvas.startOKDlg("Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c map (id " + by + "). Ki\u1ec3m tra valuesdata2.");
            return;
        }
        PM.MAX_PLAYER = 8;
        PM.NUMB_PLAYER = 8;
        short[] sArray = new short[8];
        short[] sArray2 = new short[8];
        short[] sArray3 = new short[8];
        int n5 = 1 + TerrainMidlet.myInfo.getSquadSize();
        if (n5 + (n4 = OfflinePvpBot.pendingBotCount) > 8) {
            n4 = 8 - n5;
        }
        OfflinePvpBot.activeSquadCount = n5;
        OfflinePvpBot.activeBotCount = n4;
        OfflinePvpBot.activeDifficulty = OfflinePvpBot.pendingDifficulty;
        for (int i = 0; i < 8; ++i) {
            if (i == 0) {
                sArray[0] = (short)(MM.mapWidth / 4);
                sArray2[0] = (short)(MM.mapHeight - 80);
                int n6 = OfflineEquipmentStats.maxHp(TerrainMidlet.myInfo);
                if (n6 > Short.MAX_VALUE) {
                    n6 = Short.MAX_VALUE;
                }
                sArray3[0] = (short)n6;
                continue;
            }
            if (i < n5) {
                PlayerInfo playerInfo = (PlayerInfo)CCanvas.prepareScr.playerInfos.elementAt(i);
                sArray[i] = (short)(MM.mapWidth / 4 + i * 30);
                sArray2[i] = (short)(MM.mapHeight - 80);
                n3 = playerInfo.maxHP;
                if (n3 > Short.MAX_VALUE) {
                    n3 = Short.MAX_VALUE;
                }
                sArray3[i] = (short)n3;
                continue;
            }
            if (i < n5 + n4) {
                PlayerInfo playerInfo = (PlayerInfo)CCanvas.prepareScr.playerInfos.elementAt(i);
                n3 = i - n5;
                sArray[i] = (short)(MM.mapWidth * 3 / 4 + n3 * 30);
                sArray2[i] = (short)(MM.mapHeight - 80);
                n2 = playerInfo.maxHP;
                if (n2 > Short.MAX_VALUE) {
                    n2 = Short.MAX_VALUE;
                }
                sArray3[i] = (short)n2;
                continue;
            }
            sArray[i] = -1;
            sArray2[i] = -1;
            sArray3[i] = 0;
        }
        CCanvas.gameScr.initGame(by, (byte)30, sArray, sArray2, sArray3, 0);
        GameScr.myIndex = 0;
        TerrainMidlet.myInfo.index = 0;
        OfflineGunAngles.applyServerAngleLocks();
        int n7 = MM.mapWidth / 4;
        for (n3 = 0; n3 < n5 && n3 < PM.p.length; ++n3) {
            n2 = n7 + n3 * 26;
            if (n2 < 10) {
                n2 = 10;
            }
            if (MM.mapWidth > 20 && n2 > MM.mapWidth - 10) {
                n2 = MM.mapWidth - 10;
            }
            n2 = GameMidlet.pickGroundX(n2, n2);
            n = OfflineBossFight.findGroundY(n2);
            GameScr.pm.updatePlayerXY(n3, (short)n2, (short)n);
            CPlayer cPlayer = PM.p[n3];
            if (cPlayer == null) continue;
            cPlayer.falling = false;
            cPlayer.active = true;
            cPlayer.isCom = false;
            cPlayer.setState((byte)0);
            cPlayer.look = 2;
            cPlayer.team = true;
        }
        n2 = MM.mapWidth / 2 + 20;
        n = MM.mapWidth - 20;
        int n8 = n4 > 0 && n > n2 ? Math.max(1, (n - n2) / n4) : 1;
        for (int i = n5; i < n5 + n4 && i < PM.p.length; ++i) {
            int[] nArray;
            int n9 = i - n5;
            int n10 = n2 + n9 * n8;
            int n11 = Math.min(n10 + n8, n);
            int n12 = GameMidlet.pickGroundX(n10, n11);
            int n13 = OfflineBossFight.findGroundY(n12);
            GameScr.pm.updatePlayerXY(i, (short)n12, (short)n13);
            CPlayer cPlayer = PM.p[i];
            if (cPlayer == null) continue;
            cPlayer.falling = false;
            cPlayer.active = true;
            cPlayer.setState((byte)0);
            cPlayer.look = 0;
            cPlayer.team = false;
            if (OfflinePvpBot.activeDifficulty == 3) {
                int[] nArray2 = new int[8];
                nArray2[0] = 0;
                nArray2[1] = 32;
                nArray2[2] = 33;
                nArray2[3] = 6;
                nArray2[4] = 7;
                nArray2[5] = 8;
                nArray2[6] = 34;
                nArray = nArray2;
                nArray2[7] = 35;
            } else {
                int[] nArray3 = new int[6];
                nArray3[0] = 0;
                nArray3[1] = 32;
                nArray3[2] = 33;
                nArray3[3] = 6;
                nArray3[4] = 7;
                nArray = nArray3;
                nArray3[5] = 8;
            }
            cPlayer.item = nArray;
        }
        OfflinePvpBotAI.reset();
        OfflineCombat.reset();
        OfflineCombat.ensureLocalPlayerTurn();
        GameScr.changeWind(3, 1);
        if (GameScr.cam != null) {
            GameScr.cam.setPlayerMode(0);
        }
        CCanvas.gameScr.show();
        CCanvas.endDlg();
    }

    private static int pickGroundX(int n, int n2) {
        int n3;
        int n4;
        int n5;
        if (n2 < n) {
            n5 = n;
            n = n2;
            n2 = n5;
        }
        for (n5 = 0; n5 < 12; ++n5) {
            n4 = n2 > n ? CRes.random(n, n2 + 1) : n;
            if (OfflineBossFight.findGroundYStrict(n4 = GameMidlet.clampX(n4)) < 0) continue;
            return n4;
        }
        n4 = Math.max(2, (n2 - n + 1) / 40);
        for (int i = n; i <= n2; i += n4) {
            n3 = GameMidlet.clampX(i);
            if (OfflineBossFight.findGroundYStrict(n3) < 0) continue;
            return n3;
        }
        n3 = GameMidlet.clampX((n + n2) / 2);
        for (int i = 4; i < MM.mapWidth; i += 8) {
            int n6 = GameMidlet.clampX(n3 - i);
            if (OfflineBossFight.findGroundYStrict(n6) >= 0) {
                return n6;
            }
            int n7 = GameMidlet.clampX(n3 + i);
            if (OfflineBossFight.findGroundYStrict(n7) < 0) continue;
            return n7;
        }
        return n3;
    }

    private static int clampX(int n) {
        if (n < 10) {
            return 10;
        }
        if (MM.mapWidth > 20 && n > MM.mapWidth - 10) {
            return MM.mapWidth - 10;
        }
        return n;
    }

    private static void startOfflineBossFight(byte by) {
        int n;
        byte by2 = PrepareScr.mapBossID[by];
        GameMidlet.ensureMapPackLoaded();
        if (MM.mapFiles == null || MM.mapFiles.size() == 0) {
            CCanvas.startOKDlg("Thi\u1ebfu file assets/rms/valuesdata2. Copy t\u1eeb server/cache ho\u1eb7c ch\u01a1i online m\u1ed9t l\u1ea7n.");
            return;
        }
        if (CCanvas.prepareScr == null) {
            CCanvas.prepareScr = new PrepareScr();
        }
        GameMidlet.populateOfflineBossParticipants(by);
        GameScr.trainingMode = false;
        if (CCanvas.gameScr == null) {
            CCanvas.gameScr = new GameScr();
        }
        if (MM.maps != null) {
            MM.maps.removeAllElements();
        }
        GameMidlet.ensureMapRuntimeData();
        GameScr.mm.createMap(by2);
        if (MM.mapWidth <= 0 || MM.mapHeight <= 0) {
            CCanvas.startOKDlg("Kh\u00f4ng t\u1ea3i \u0111\u01b0\u1ee3c map boss (id " + by2 + "). Ki\u1ec3m tra valuesdata2.");
            return;
        }
        int n2 = OfflineBossFight.requiredBossSlots(by);
        PM.MAX_PLAYER = (byte)(8 + n2 + 12);
        PM.NUMB_PLAYER = 8;
        short[] sArray = new short[8];
        short[] sArray2 = new short[8];
        short[] sArray3 = new short[8];
        int n3 = 1 + TerrainMidlet.myInfo.getSquadSize();
        for (int i = 0; i < 8; ++i) {
            if (i == 0) {
                sArray[0] = (short)(MM.mapWidth / 4);
                sArray2[0] = (short)(MM.mapHeight - 80);
                int n4 = OfflineEquipmentStats.maxHp(TerrainMidlet.myInfo);
                if (n4 > Short.MAX_VALUE) {
                    n4 = Short.MAX_VALUE;
                }
                sArray3[0] = (short)n4;
                continue;
            }
            if (i < n3) {
                PlayerInfo playerInfo = (PlayerInfo)CCanvas.prepareScr.playerInfos.elementAt(i);
                sArray[i] = (short)(MM.mapWidth / 4 + i * 30);
                sArray2[i] = (short)(MM.mapHeight - 80);
                n = playerInfo.maxHP;
                if (n > Short.MAX_VALUE) {
                    n = Short.MAX_VALUE;
                }
                sArray3[i] = (short)n;
                continue;
            }
            sArray[i] = -1;
            sArray2[i] = -1;
            sArray3[i] = 0;
        }
        CCanvas.gameScr.initGame(by2, (byte)30, sArray, sArray2, sArray3, 0);
        short s = (short)(MM.mapWidth * 3 / 4);
        n = MM.mapHeight - 80;
        GameScr.pm.initBoss(new short[]{s}, new short[]{(short)n});
        OfflineBossFight.finishSetup(by);
        if (PM.p != null && PM.p[GameScr.myIndex] != null) {
            OfflineBulletAssets.prepareCombat(PM.p[GameScr.myIndex]);
        }
        GameScr.changeWind(3, 1);
        CCanvas.gameScr.show();
        CCanvas.endDlg();
    }

    public static void startNewOfflineGame(String string) {
        CloudSaveApi.logout();
        GameMidlet.initOfflineProfile(string);
        OfflineMission.reset();
        OfflineMission.onLogin();
        OfflineSave.save();
    }

    public static boolean continueOfflineGame() {
        if (!OfflineSave.load()) {
            return false;
        }
        if (TerrainMidlet.myInfo != null) {
            if (TerrainMidlet.myInfo.classLevel2 != null && TerrainMidlet.myInfo.classLevel2.length > 0) {
                if (TerrainMidlet.myInfo.classLevel2[0] < 50) {
                    TerrainMidlet.myInfo.classLevel2[0] = 50;
                    TerrainMidlet.myInfo.classPoint[0] = (short)Math.max(TerrainMidlet.myInfo.classPoint[0], 100);
                    TerrainMidlet.myInfo.classNextExp[0] = OfflineCombat.expThresholdForLevel(50);
                }
            }
            if (TerrainMidlet.myInfo.gun == 0 && TerrainMidlet.myInfo.level2 < 50) {
                TerrainMidlet.myInfo.level2 = 50;
                TerrainMidlet.myInfo.point = (short)Math.max(TerrainMidlet.myInfo.point, 100);
                TerrainMidlet.myInfo.nextExp = OfflineCombat.expThresholdForLevel(50);
            }
        }
        ChangePlayerCSr.ensureGunData();
        GameMidlet.ensureOfflineAssetsLoaded();
        PlayerEquip.migrateCostumeHats(TerrainMidlet.myInfo);
        TerrainMidlet.myInfo.getAttribute();
        TerrainMidlet.myInfo.ensureCombatEquip();
        OfflineChest.applyPendingLoad();
        TerrainMidlet.myInfo.getQuanHam();
        OfflineMission.onLogin();
        return true;
    }

    private static void initOfflineProfile(String string) {
        TerrainMidlet.myInfo = new PlayerInfo();
        TerrainMidlet.myInfo.name = string == null || string.length() == 0 ? "offline-player" : string;
        TerrainMidlet.myInfo.IDDB = 1;
        TerrainMidlet.myInfo.gun = 0;
        TerrainMidlet.myInfo.xu = 1000000;
        TerrainMidlet.myInfo.luong = 10000;
        TerrainMidlet.myInfo.level2 = 50;
        TerrainMidlet.myInfo.exp = 0;
        TerrainMidlet.myInfo.point = 100;
        TerrainMidlet.myInfo.nextExp = OfflineCombat.expThresholdForLevel(50);
        TerrainMidlet.myInfo.initClassProgressDefaults();
        TerrainMidlet.myInfo.getAttribute();
        MenuScr.getIdMenu(0);
        GameMidlet.ensureOfflineAssetsLoaded();
        OfflineChest.clear();
        OfflineTeamItems.reset();
        PlayerEquip.applyDefaultOfflineEquipIds(TerrainMidlet.myInfo);
        TerrainMidlet.myInfo.ensureCombatEquip();
        TerrainMidlet.myInfo.getQuanHam();
        ChangePlayerCSr.ensureGunData();
        GameMidlet.initOfflineShop();
    }

    private static void initOfflineShop() {
        Vector<Item> vector = new Vector<Item>();
        for (int i = 0; i < 36; ++i) {
            vector.addElement(new Item((byte)i, (byte)99, 1000, 5));
        }
        ShopItem.setItemVector(vector);
    }

    public static void doUpdateServer() {
        CCanvas.startWaitDlg(Language.pleaseWait());
        GameMidlet.connectHTTP(linkGetHost, new IAction2(){

            public void perform(Object object) {
                String string = "";
                if (object != null) {
                    string = (String)object;
                }
                GameMidlet.getServerList(string);
                GameMidlet.saveIP();
                CCanvas.startOKDlg(Language.updateServer());
            }
        });
    }

    public static void saveIP() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            int n;
            int n2 = ServerListScreen.nameServer.length;
            for (n = 0; n < ServerListScreen.nameServer.length; ++n) {
                if (versionByte < 240) {
                    if (ServerListScreen.nameServer[n].equals("Tr\u00e1i \u0110\u1ea5t")) {
                        --n2;
                    }
                    if (ServerListScreen.nameServer[n].equals("Sao H\u1ecfa")) {
                        --n2;
                    }
                }
                if (!ServerListScreen.nameServer[n].equals("LOCAL")) continue;
                --n2;
            }
            dataOutputStream.writeByte(n2);
            for (n = 0; n < n2; ++n) {
                dataOutputStream.writeUTF(ServerListScreen.nameServer[n]);
                dataOutputStream.writeUTF(ServerListScreen.address[n]);
                dataOutputStream.writeShort(ServerListScreen.port[n]);
            }
            try {
                RMS.saveRMS("ipArmy2", byteArrayOutputStream.toByteArray());
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
            dataOutputStream.close();
        }
        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void loadIP() {
        byte[] byArray = CRes.loadRMSData("ipArmy2");
        if (byArray == null) {
            GameMidlet.doUpdateServer();
        } else {
            CRes.out(" 1 ==================> loadIP");
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byArray);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            if (dataInputStream != null) {
                try {
                    int n = dataInputStream.readByte();
                    ServerListScreen.nameServer = new String[n];
                    ServerListScreen.address = new String[n];
                    ServerListScreen.port = new short[n];
                    if (versionByte < 240) {
                        ServerListScreen.nameServer = new String[n + 1];
                        ServerListScreen.address = new String[n + 1];
                        ServerListScreen.port = new short[n + 1];
                    }
                    if (versionByte < 240) {
                        ServerListScreen.nameServer[n] = "M\u1eb7t Tr\u1eddi";
                        ServerListScreen.address[n] = "27.0.12.164";
                        ServerListScreen.port[n] = 19149;
                    }
                    for (int i = 0; i < n; ++i) {
                        ServerListScreen.nameServer[i] = dataInputStream.readUTF();
                        ServerListScreen.address[i] = dataInputStream.readUTF();
                        ServerListScreen.port[i] = dataInputStream.readShort();
                    }
                    dataInputStream.close();
                }
                catch (IOException iOException) {
                    iOException.printStackTrace();
                }
            }
        }
    }

    public static void getServerList(String string) {
        String[] stringArray = CRes.split(string, ",");
        ServerListScreen.nameServer = new String[stringArray.length];
        ServerListScreen.address = new String[stringArray.length];
        ServerListScreen.port = new short[stringArray.length];
        for (int i = 0; i < stringArray.length; ++i) {
            String string2 = stringArray[i].trim();
            String[] stringArray2 = CRes.split(string2, ":");
            ServerListScreen.nameServer[i] = stringArray2[0];
            ServerListScreen.address[i] = stringArray2[1];
            ServerListScreen.port[i] = Short.parseShort(stringArray2[2].trim());
        }
    }

    public static void setZOOM_IOS() {
        ZOOM_IOS = (byte)mGraphics.zoomLevel;
        int n = 1;
        if (CCanvas.isPc()) {
            n = 1;
        }
        if (mGraphics.zoomLevel > n) {
            ZOOM_IOS = (byte)2;
        }
    }

    public static void setcurrentIAPStore() {
        currentIAPStore = DEVICE == 5 ? (byte)2 : (DEVICE == 6 ? (byte)3 : (byte)0);
    }

    protected void destroyApp(boolean bl)  {
    }

    public void destroy() {
        try {
            instance.destroyApp(true);
        }
        catch (Exception mIDletStateChangeException) {
            mIDletStateChangeException.printStackTrace();
        }
    }

    protected void pauseApp() {
    }

    public void startApp()  {
        if (!isStartGame) {
            instance = this;
            DEVICE = 0;
            CCanvas.isVirtualKey = false;
            this.initGame();
            gameCanvas.displayMe(instance);
            isStartGame = true;
        }
    }

    public static void exit() {
        MotherCanvas.bRun = false;
        System.gc();
        instance.notifyDestroyed();
    }

    public void perform(int n, Object object) {
    }

    public static void openUrl(String string) {
        mSystem.openUrl(string);
    }

    public static String loginPlus() {
        return "";
    }

    public static String connectHTTP(String string) {
        return mSystem.connectHTTP(string);
    }

    public static void connectHTTP(String string, IAction2 iAction2) {
        mSystem.connectHTTP(string, iAction2);
    }

    public void CheckPerGPS() {
        this.getLocation();
    }

    public void getLocation() {
        longitude = "";
        latitude = "";
    }

    public static void handleMessage(Message message) {
        try {
            String string = message.reader().readUTF();
        }
        catch (Exception exception) {
        }
    }

    public static void handleAllMessage() {
    }

    public static void serverInformation(Font font, mGraphics mGraphics2) {
        String string = "Offline Mod by CryCheese";
        font.drawString(mGraphics2, string, CCanvas.width - 2 - font.getWidth(string), 2 + font.getHeight(), 0, false);
        if (CCanvas.isDebugging()) {
            font.drawString(mGraphics2, String.valueOf(timePingPaint), CCanvas.width - 2 - font.getWidth(string), 2 + font.getHeight() * 2, 0, false);
        }
    }

    public static void serverInformation(Font font, mGraphics mGraphics2, int n) {
        String string = "Offline Mod by CryCheese";
        font.drawString(mGraphics2, string, CCanvas.width / 2, n, 3, false);
        if (CCanvas.isDebugging()) {
            font.drawString(mGraphics2, String.valueOf(timePingPaint), CCanvas.width / 2, n + font.getHeight(), 3, false);
        }
    }

    static {
        version = "2.4.1";
        versionByte = (short)241;
        versioncode = (byte)11;
        server = (byte)-2;
        versionServer = (short)3;
        DEVICE = (byte)4;
        COMPILE = 1;
        lowGraphic = false;
        currentIAPStore = 0;
        langServer = 0;
        ZOOM_IOS = 1;
        PROVIDER = 0;
        IP = "192.168.1.88";
        PORT = 19152;
        isVip = new boolean[10];
        isTeamClient = true;
        linkGetHost = "https://sv.pro.vn/server.txt";
        linkReg = "http://my.teamobi.com/app/view/register.php";
        latitude = "";
        longitude = "";
        OFFLINE_VERSION_TEXT = "v1.0.0";
        offlineVersionLoaded = false;
        OFFLINE_BOSS_ROOM_ID = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        OFFLINE_BOSS_BOARD_ID = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        BOSS_ROOM_DISPLAY_NAMES = new String[]{"Bom 1", "Bom 2", "Nh\u1ec7n m\u00e1y", "Robot", "T-rex", "Ufo", "Kh\u00ed c\u1ea7u", "Hang nh\u1ec7n", "B\u00f3ng ma", "B\u00f3ng ma 2"};
        pendingOfflineBossRoomIndex = (byte)-1;
    }
}

